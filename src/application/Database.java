package application;

import java.io.FileInputStream;
import java.io.IOException;
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
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Properties;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public abstract class Database {
    		
    public enum Role {
        DOCTOR, NURSE, PATIENT;
    }
    
    public enum Sex {
        MALE, FEMALE, OTHER;
    }

    public enum Race {
        WHITE, BLACK, HISPANIC, ASIAN, NATIVE_AMERICAN, PACIFIC_ISLANDER, OTHER;
    }

    public enum Ethnicity {
        HISPANIC, NON_HISPANIC;
    }


    public enum Severity {
        MILD, MODERATE, SEVERE;
    }

    public enum BloodType {
        A_POSITIVE("A+"), A_NEGATIVE("A-"), B_POSITIVE("B+"), B_NEGATIVE("B-"), AB_POSITIVE("AB+"), AB_NEGATIVE("AB-"), O_POSITIVE("O+"), O_NEGATIVE("O-"), UNKNOWN("THROW ERROR");

        private final String value;

        private BloodType(String value) {
            this.value = value;
        }

        public String toString() {
            return value;
        }
    }

    public static Connection connection;
    public static Integer userID;
    public static Role role;
    public static HashMap<String, HashMap<String, Boolean>> updatePermissions;
    public static HashMap<String, HashMap<String, Boolean>> selectPermissions;
    public static HashMap<String, HashMap<String, Boolean>> insertPermissions;
    public static HashMap<String, Boolean> deletePermissions;

    static {
        updatePermissions = new HashMap<String, HashMap<String, Boolean>>();
    }

    public static void changeRole(Role role) throws Exception {
        disconnect();
        Database.role = role;
        connect();
    }

    public static void connect() throws SQLException, IOException {
        Properties props = new Properties();
        FileInputStream in = new FileInputStream(".env");
        props.load(in);
        in.close();

        String url = null;

        if (Database.role == null) {
            url = props.getProperty("db_neutral_url");
        } else if (role == Role.PATIENT) {
            url = props.getProperty("db_patient_url");
        } else if (role == Role.DOCTOR) {
            url = props.getProperty("db_doctor_url");
        } else if (role == Role.NURSE) {
            url = props.getProperty("db_nurse_url");
        } else {
            throw new SQLException("Invalid role");
        }

        connection = DriverManager.getConnection(url);

        if (connection == null) {
            throw new SQLException("Connection to the database failed");
        }
    }

    public static void disconnect() throws SQLException, UnknownHostException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        if (connection != null) {
            connection.close();
        }
    }

    public static String getGrantee() {
        return String.format("\"'%s'@'%%'\"", role.toString().toLowerCase());
    }

    public static String getPermissedColumns(String tableName, String operation) throws Exception {
        PreparedStatement statement = connection.prepareStatement(String.format("SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMN_PRIVILEGES WHERE TABLE_NAME = ? AND PRIVILEGE_TYPE = ? AND GRANTEE = %s;", getGrantee()));
        statement.setString(1, tableName);
        statement.setString(2, operation);
        ResultSet resultSet = statement.executeQuery();
        
        ArrayList<String> columnNames = new ArrayList<String>();
        
        while (resultSet.next()) {
            columnNames.add(tableName + "." + resultSet.getString("COLUMN_NAME"));
        }
        
        return String.join(", ", columnNames);
    }

    public static boolean userExists(String username) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT ID FROM users WHERE username = ?;");
        statement.setString(1, username);

        ResultSet resultSet = statement.executeQuery();
        boolean userExists = resultSet.next();
        resultSet.close();

        return userExists;
    }

    public static ResultSet selectAllWithRole(Role role) throws Exception {
        String tableName = "users";
        String columns = getPermissedColumns(tableName, "SELECT");
        PreparedStatement statement = connection.prepareStatement(String.format("SELECT %s FROM %s WHERE role = ?;", columns, tableName));
        statement.setString(1, role.toString());
        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }

    public static ResultSet selectRow(int rowID, String tableName) throws Exception {
        String colummns = getPermissedColumns(tableName, "SELECT");
        PreparedStatement statement2 = connection.prepareStatement(String.format("SELECT %s FROM %s WHERE ID = ?;", colummns, tableName));
        statement2.setInt(1, rowID);

        ResultSet resultSet2 = statement2.executeQuery();

        return resultSet2;
    }

    public static ResultSet selectMultiRow(int userID, String tableName) throws Exception {
        String colummns = getPermissedColumns(tableName, "SELECT");
        PreparedStatement statement = connection.prepareStatement(String.format("SELECT %s FROM %s WHERE userID = ?;", colummns, tableName));
        statement.setInt(1, userID);

        ResultSet resultSet = statement.executeQuery();

        return resultSet;
    }

    public static void updateRow(int rowID, String table, String column, String newValue) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(String.format("UPDATE %s SET %s = ? WHERE ID = ?;", table, column));
        statement.setString(1, newValue);
        statement.setInt(2, rowID);
        statement.executeUpdate();
    }

    public static Integer insertUser(String username, String password, Role role) throws Exception {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO users (username, password, role) VALUES (?, SHA2(?, 256), ?);");
        statement.setString(1, username);
        statement.setString(2, password);
        statement.setString(3, role.toString());
        statement.executeUpdate();

        statement = connection.prepareStatement("SELECT LAST_INSERT_ID();");
        ResultSet resultSet = statement.executeQuery();

        resultSet.next();
        Integer userID = resultSet.getInt(1);

        return userID;
    }

    public static void deleteRow(String tableName, int rowID) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(String.format("DELETE FROM %s WHERE ID = ?;", tableName));
        statement.setInt(1, rowID);
        statement.executeUpdate();
    }

    public static void insertEmployee(String username, String password, Role role, String firstName, String lastName, Sex sex, String birthDate, String email, String phone, String address, String managerID, String managerPassword, Race race, Ethnicity ethnicity) throws Exception {
        if (role != Role.DOCTOR && role != Role.NURSE) {
            throw new SQLException("Invalid role");
        }

        if (!validateManager(managerID, managerPassword)) {
            throw new SQLException("Invalid manager");
        }

        int userID = insertUser(username, password, role);

        PreparedStatement statement = connection.prepareStatement("INSERT INTO employees (ID, firstName, lastName, sex, birthDate, hireDate, email, phone, address, managerID, race, ethnicity) VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, ?);");
        statement.setInt(1, userID);
        statement.setString(2, firstName);
        statement.setString(3, lastName);
        statement.setString(4, sex.toString());
        statement.setString(5, birthDate);
        statement.setString(6, email);
        statement.setString(7, phone);
        statement.setString(8, address);
        statement.setString(9, managerID);
        statement.setString(10, race.toString());
        statement.setString(11, ethnicity.toString());

        try {
            statement.executeUpdate();
        } catch (SQLException e) {
            if (userID != 0) {
                deleteRow("users", userID);
                throw e;
            }
        }
    }

    public static void insertPatient(String username, String password, String firstName, String lastName, Sex sex, String birthDate, String email, String phone, String address, Race race, Ethnicity ethnicity) throws Exception {
        Integer userID = insertUser(username, password, Role.PATIENT);

        PreparedStatement statement = connection.prepareStatement("INSERT INTO patients (ID, firstName, lastName, sex, birthDate, email, phone, address, race, ethnicity) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
        statement.setInt(1, userID);
        statement.setString(2, firstName);
        statement.setString(3, lastName);
        statement.setString(4, sex.toString());
        statement.setString(5, birthDate);
        statement.setString(6, email);
        statement.setString(7, phone);
        statement.setString(8, address);
        statement.setString(9, race.toString());
        statement.setString(10, ethnicity.toString());

        try {
            statement.executeUpdate();
        } catch (SQLException e) {
            if (userID != null) {
                deleteRow("users", userID);
                throw e;
            }
        }
    }

    public static boolean validateManager(String managerID, String managerPassword) throws Exception {
        PreparedStatement statement = connection.prepareStatement("SELECT ID FROM users WHERE ID = ? AND password = SHA2(?, 256) AND role = ?;");
        statement.setString(1, managerID);
        statement.setString(2, managerPassword);
        statement.setString(3, Role.DOCTOR.toString()); //! Change to manager in the future
        ResultSet resultSet = statement.executeQuery();

        return resultSet.next();
    }

    public static void refreshUpdatePermissions() throws SQLException {
        PreparedStatement statement = connection.prepareStatement(String.format("SELECT TABLE_NAME, COLUMN_NAME, IF(PRIVILEGE_TYPE = 'UPDATE', true, false) AS can_update FROM INFORMATION_SCHEMA.COLUMN_PRIVILEGES WHERE GRANTEE = %s;", getGrantee()));
        ResultSet resultSet = statement.executeQuery();

        updatePermissions = new HashMap<String, HashMap<String, Boolean>>();
        
        while (resultSet.next()) {
            String tableName = resultSet.getString("TABLE_NAME");
            String columnName = resultSet.getString("COLUMN_NAME");
            Boolean updatable = resultSet.getBoolean("can_update");
            
            
            if (!updatePermissions.containsKey(tableName)) {
                updatePermissions.put(tableName, new HashMap<String, Boolean>());
            }
            
            updatePermissions.get(tableName).put(columnName, updatable);
        }
    }

    public static void refreshDeletePermissions() throws Exception {
        PreparedStatement statement = connection.prepareStatement(String.format("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLE_PRIVILEGES WHERE PRIVILEGE_TYPE = 'DELETE' AND GRANTEE = %s;", getGrantee()));
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            String tableName = resultSet.getString("TABLE_NAME");

            deletePermissions = new HashMap<String, Boolean>();
            deletePermissions.put(tableName, true);
        }
    }

    public static void refreshAllPermissions() throws Exception {
        refreshUpdatePermissions();
        refreshDeletePermissions();
    }

    public static boolean canUpdate(String tableName, String columnName) {
        if (!updatePermissions.get(tableName).containsKey(columnName)) {
            return false;
        }
        return updatePermissions.get(tableName).get(columnName);
    }

    public static boolean canDelete(String tableName) {
        if (!deletePermissions.containsKey(tableName)) {
            return false;
        }

        return deletePermissions.get(tableName);
    }

    public static boolean signIn(String username, String password) throws SQLException, UnknownHostException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, Exception {
        boolean successful = false;

        PreparedStatement statement = connection.prepareStatement("SELECT ID, username, role FROM users WHERE username = ? AND password = SHA2(?, 256);");
        statement.setString(1, username);
        statement.setString(2, password);

        ResultSet resultSet = statement.executeQuery();
        successful = resultSet.next();
        
        if (successful) {
            role = Role.valueOf(resultSet.getString("role"));
            userID = resultSet.getInt("ID");

            changeRole(role);
            statement = connection.prepareStatement("INSERT INTO logbook (userID, IP, type) VALUES (?, ?, ?);");
            String IP = InetAddress.getLocalHost().getHostAddress();
            statement.setInt(1, userID);
            statement.setString(2, IP);
            statement.setString(3, "SIGN_IN");
            statement.executeUpdate();
            refreshAllPermissions();
            
            System.out.println(username + " signed in");
        }
        
        return successful;
    }
    
    public static void signOut() throws SQLException, UnknownHostException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, Exception {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO logbook (ID, IP, type) VALUES (?, ?, ?);");
        String IP = InetAddress.getLocalHost().getHostAddress();
        
        statement.setInt(1, userID);
        statement.setString(2, IP);
        statement.setString(3, "SIGN_OUT");
        statement.executeUpdate();

        System.out.println(getMy("username") + " signed out");
        userID = null;
        changeRole(null);
    }

    public static ArrayList<Datum> getOptionsFor(Datum datum) throws Exception {
        ArrayList<Datum> options = new ArrayList<Datum>();
        PreparedStatement statement = connection.prepareStatement("SELECT COLUMN_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = ? AND COLUMN_NAME = ?;");
        statement.setString(1, datum.parent.tableName);
        statement.setString(2, datum.columnName);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();

        String result = resultSet.getString("COLUMN_TYPE");

        String[] parts = result.substring(5, result.length() - 1).split(",");
        for (String part : parts) {
            String bloodType = part.substring(1, part.length() - 1);
            options.add(new Datum(datum.parent, bloodType, "bloodType"));
        }

        return options;
    }

    public static String getMy(String columnName) throws Exception {
        if (role == Role.PATIENT) {
            return selectRow(0, "patients").getString(columnName);
        } else {
            return selectRow(0, "employees").getString(columnName);
        }
    }

    public static class Encrypter {
        private final static String key;
        private final static String algorithm;

        static {
            Properties props = new Properties();

            try {
                FileInputStream in = new FileInputStream(".env");
                props.load(in);
                in.close();

            } catch (IOException e) {
                System.out.println("Error reading .env file");
            }

            key = props.getProperty("db_key");
            algorithm = props.getProperty("db_algorithm");
        }

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