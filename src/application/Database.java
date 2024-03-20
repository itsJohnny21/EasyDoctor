package application;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public abstract class Database {

    		
    public enum Role {
        NEUTRAL, DOCTOR, NURSE, PATIENT;
    }

    public static Connection connection;
    public static Integer userID;
    public static Role role;
    public static HashMap<String, Boolean> updatePermissions;


    public static void connectAs(Role role) throws SQLException {

        if (role == Role.DOCTOR) {
            connection = DriverManager.getConnection("jdbc:mysql://easydoctor.c1wkcaa6ol0w.us-east-2.rds.amazonaws.com:3306/easydoctor?user=doctor&password=doctor123"); //! hide
        } else if (role == Role.NURSE) {
            connection = DriverManager.getConnection("jdbc:mysql://easydoctor.c1wkcaa6ol0w.us-east-2.rds.amazonaws.com:3306/easydoctor?user=nurse&password=nurse123"); //! hide
        } else if (role == Role.PATIENT) {
            connection = DriverManager.getConnection("jdbc:mysql://easydoctor.c1wkcaa6ol0w.us-east-2.rds.amazonaws.com:3306/easydoctor?user=patient&password=patient123"); //! hide
        } else if (role == Role.NEUTRAL) {
            connection = DriverManager.getConnection("jdbc:mysql://easydoctor.c1wkcaa6ol0w.us-east-2.rds.amazonaws.com:3306/easydoctor?user=neutral&password=neutral123"); //! hide
        } else {
            throw new SQLException("Invalid role");
        }

        if (connection == null) {
            throw new SQLException("Connection to the database failed");
        }
        Database.role = role;
    }

    public static void reconnectAs(Role role) throws Exception {
        disconnect();
        connectAs(role);
    }


    public static void disconnect() throws SQLException, UnknownHostException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        if (connection != null) {
            connection.close();
        }
    }

    public static void updateRow(int rowID, String table, String column, String newValue) throws Exception {
        PreparedStatement statement = connection.prepareStatement(String.format("UPDATE %s SET %s = ? WHERE ID = ?;", table, column));
        statement.setString(1, newValue);
        statement.setInt(2, rowID);

        statement.executeUpdate();
    }

    public static boolean signIn(String username, String password) throws SQLException, UnknownHostException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, Exception {
        boolean successful = false;

        PreparedStatement statement = connection.prepareStatement("SELECT ID, username, role FROM users WHERE username = ? AND passwordHash = ? LIMIT 1;");
        statement.setString(1, username);
        statement.setString(2, password);

        ResultSet resultSet = statement.executeQuery();
        successful = resultSet.next();
        
        if (successful) {
            role = Role.valueOf(resultSet.getString("role"));
            userID = resultSet.getInt("ID");

            reconnectAs(role);
            statement = connection.prepareStatement("INSERT INTO logbook (userID, IP, type) VALUES (?, ?, ?);");
            String IP = InetAddress.getLocalHost().getHostAddress();
            
            statement.setInt(1, userID);
            statement.setString(2, IP);
            statement.setString(3, "SIGN_IN");
            statement.executeUpdate();
            System.out.println(username + " signed in");
        }

        return successful;
    }

    public static void signOut() throws SQLException, UnknownHostException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, Exception {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO logbook (userID, IP, type) VALUES (?, ?, ?);");
        String IP = InetAddress.getLocalHost().getHostAddress();
    
        statement.setInt(1, userID);
        statement.setString(2, IP);
        statement.setString(3, "SIGN_OUT");
        statement.executeUpdate();

        System.out.println(getMy("username") + " signed out");
        userID = null;
        reconnectAs(Role.NEUTRAL);
    }

    public static ResultSet getEmployeeInfo(int userID) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT users.username, users.role, employees.* FROM users JOIN employees ON users.ID = employees.userID WHERE users.ID = ?;");
        statement.setInt(1, userID);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return resultSet;
        } else {
            throw new SQLException("Employee not found");
        }
    }

    public static ResultSet getPatientInfo(int userID) throws SQLException{
        PreparedStatement statement = connection.prepareStatement("SELECT users.username, users.role, patients.* FROM users JOIN patients ON users.ID = patients.userID WHERE users.ID = ?;");
        statement.setInt(1, userID);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return resultSet;
        } else {
            throw new SQLException("Patient not found");
        }
    }

    public static String getMy(String columnName) throws SQLException {
        if (role == Role.PATIENT) {
            return getPatientInfo(userID).getString(columnName);
        } else {
            return getEmployeeInfo(userID).getString(columnName);
        }
    }

    public static ResultSet getContactInformationFor(int userID) throws Exception {
        PreparedStatement statement = connection.prepareStatement("SELECT patients.firstName AS 'First Name', patients.lastName AS 'Last Name', patients.sex AS 'Sex', patients.birthDate AS 'Birth Date', patients.phone AS 'Phone', patients.email AS 'Email', patients.address AS 'Address', users.username AS 'Username', patients.race AS 'Race', patients.ethnicity AS 'Ethnicity', patients.emergencyContactName AS 'Emergency Contact Name', patients.emergencyContactPhone AS 'Emergency Contact Phone', patients.motherFirstName AS 'Mother First Name', patients.motherLastName AS 'Mother Last Name', patients.fatherFirstName AS 'Father First Name', patients.fatherLastName AS 'Father Last Name' FROM patients JOIN users ON users.ID = patients.userID WHERE patients.userID = ?;");
        statement.setInt(1, userID);

        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }

    public static ResultSet getMyContactInformation() throws Exception {
        return getContactInformationFor(userID);
    }

    public static ResultSet getMedicalInformationFor(int userID) throws Exception {
        PreparedStatement statement = connection.prepareStatement("SELECT CONCAT(employees.firstName, ' ', employees.lastName) AS 'Preferred Doctor', patients.insuranceProvider AS 'Insurance Provider', patients.insuranceID AS 'Insurance ID', patients.bloodType AS 'Blood Type', patients.height AS 'Height', patients.weight AS 'Weight' FROM patients JOIN employees ON patients.preferredDoctorID = employees.userID WHERE patients.userID = ?;");
        statement.setInt(1, userID);

        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }

    public static ResultSet getMyMedicalInformation() throws Exception {
        return getMedicalInformationFor(userID);
    }

    public static ResultSet getHealthConditionsFor(int userID) throws Exception {
        PreparedStatement statement = connection.prepareStatement("SELECT healthCondition, severity, type, notes FROM healthConditions WHERE patientID = ?;");
        statement.setInt(1, userID);

        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }

    public static ResultSet getMyHealthConditions() throws Exception {
        return getHealthConditionsFor(userID);
    }

    public static ResultSet getVaccineRecordFor(int userID) throws Exception {
        PreparedStatement statement = connection.prepareStatement("SELECT vaccineGroup, date FROM vaccineRecords WHERE patientID = ? ORDER BY DATE;");
        statement.setInt(1, userID);

        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }

    public static ResultSet getMyVaccineRecord() throws Exception {
        return getVaccineRecordFor(userID);
    }

    public static ResultSet getAllergiesFor(int userID) throws Exception {
        PreparedStatement statement = connection.prepareStatement("SELECT allergen, commonSource, severity, type, notes FROM allergies WHERE patientID = ?;");
        statement.setInt(1, userID);

        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }

    public static ResultSet getMyAllergies() throws Exception {
        return getAllergiesFor(userID);
    }

    public static ResultSet getSurgeriesFor(int userID) throws Exception {
        PreparedStatement statement = connection.prepareStatement("SELECT employees.firstName, employees.lastName, date, type, location, notes FROM surgeries JOIN employees ON surgeries.doctorID = employees.userID WHERE surgeries.patientID = ?;");
        statement.setInt(1, userID);

        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }

    public static ResultSet getMySurgeries() throws Exception {
        return getSurgeriesFor(userID);
    }

    public static HashMap<String, Boolean> getUpdatePermissionsFor(Role role) throws Exception {
        HashMap<String, Boolean> updatePermissions = new HashMap<String, Boolean>();
        PreparedStatement statement = connection.prepareStatement("SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMN_PRIVILEGES WHERE PRIVILEGE_TYPE = 'UPDATE' AND GRANTEE = ?;");
        String grantee = "'" + role.toString().toLowerCase() + "'@'%'";
        statement.setString(1, grantee);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            updatePermissions.put(resultSet.getString("COLUMN_NAME"), true);
            System.out.println(resultSet.getString("COLUMN_NAME") + " is updatable");
        }
        
        return updatePermissions;
    }

    public static ResultSet getVisitsFor(int userID) throws Exception {
        PreparedStatement statement = connection.prepareStatement("SELECT employees.firstName, employees.lastName, date, reason, completed, visits.ID FROM visits JOIN employees ON visits.doctorID = employees.userID WHERE visits.patientID = ?;");
        statement.setInt(1, userID);

        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }

    public static ResultSet getMyVisits() throws Exception { //! Make sure that the proper time zones are used to compare the date and time and check if the visits have been missed, completed, or are upcoming
        PreparedStatement statement = connection.prepareStatement("employees.firstName, employees.lastName, date, reason, completed, visits.ID FROM visits JOIN employees ON visits.doctorID = employees.userID WHERE visits.patientID = ?;");
        statement.setInt(1, userID);

        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }

    // public static ResultSet getMyUpdatePermissions() throws Exception {
    //     return getUpdatePermissionsFor(role);
    // }

    public static ResultSet getHealthConditionsFor2(int userID) throws Exception {
        PreparedStatement statement = connection.prepareStatement(String.format("SELECT * FROM healthConditions WHERE patientID = ?;"));
        statement.setInt(1, userID);
        statement.executeQuery();

        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }

    public static class Encrypter {
        private final static String key = "1234561234567890";
        private final static String algorithm = "AES";

        private Encrypter() {
            System.out.println("Encrypter created! This should not happen! Use static methods instead!");
        }

        public static String SHA256(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(input.getBytes("UTF-8"));
            byte[] digest = md.digest();
            return String.format("%064x", new BigInteger(1, digest));
        }
    
        public static String encrypt(String input) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), algorithm);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
    
            byte[] encrypted = cipher.doFinal(input.getBytes());
            String ecnryptedString = Base64.getEncoder().encodeToString(encrypted);
            return ecnryptedString;
        }
    }
}