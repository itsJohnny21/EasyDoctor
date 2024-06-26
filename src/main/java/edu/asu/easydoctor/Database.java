package edu.asu.easydoctor;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.
PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.mail.MessagingException;

import edu.asu.easydoctor.exceptions.ExpiredResetPasswordTokenException;
import edu.asu.easydoctor.exceptions.InvalidResetPasswordTokenException;

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

    public enum HealthConditionSeverity {
        ACUTE, CHRONIC
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

    public enum CreationType {
        ONLINE, IN_PERSON;
    }

    public enum DrugType {
        FOOD, DRUG , ENVIRONMENTAL , INSECT , ANIMAL , PLANT , OTHER;
    }

    public enum PrescriptionForm {
         TABLET , CAPSULE , LIQUID , INJECTION , CREAM , OINTMENT , INHALER , SUPPOSITORY , SOLUTION , SUSPENSION , SYRUP , SPRAY , LOZENGE , POWDER , GEL;
    }

    public enum HealthConditionType {
        PHYSICAL, MENTAL;
    }

    public enum Units {
        ML , MG , G , IU , UNITS;
    }

    public enum VisitStatus {
        PENDING, ACTIVE, COMPLETED, MISSED, CANCELLED, UPCOMING;
    }

    public enum AllergyType {
        FOOD, DRUG, ENVIRONMENTAL, INSECT, ANIMAL, PLANT, OTHER
    }

    public enum VaccineGroup {
        COVID_19, Influenza, Hepatitis_A, Hepatitis_B, Varicella, Polio, Pneumococcal, MMR, HPV, Shingles
    }

    public static Connection connection;
    public static Integer userID;
    public static Role role;
    public static HashMap<String, HashMap<String, Boolean>> updatePermissions = new HashMap<String, HashMap<String, Boolean>>();
    public static HashMap<String, HashMap<String, Boolean>> selectPermissions;
    public static HashMap<String, HashMap<String, Boolean>> insertPermissions;
    public static HashMap<String, Boolean> deletePermissions;
    public final static Duration TOKEN_LIFESPAN = Duration.ofMinutes(5);
    public final static Duration VISIT_GRACE_PERIOD = Duration.ofMinutes(15);
    public final static Duration VISIT_DURATION = Duration.ofMinutes(15);
    public final static Duration CONNECTION_TIMEOUT_LENGTH = Duration.ofSeconds(2);

    public static void changeRole(Role role) throws Exception {
        disconnect();
        Database.role = role;
        connect();
    }

    public static void connectAsAdmin() throws SQLException {
        connection = DriverManager.getConnection(App.properties.getProperty("db_admin_url"));
    }

    public static void ensureConnection() throws SQLException {
        if (connection == null || !connection.isValid((int) CONNECTION_TIMEOUT_LENGTH.toSeconds())) {
            connect();
        }
    }

    public static void connect() throws SQLException {
        if (connection != null && connection.isValid((int) CONNECTION_TIMEOUT_LENGTH.toSeconds())) return;

        String url = null;

        if (role == Role.PATIENT) {
            url = App.properties.getProperty("db_patient_url");
        } else if (role == Role.DOCTOR) {
            url = App.properties.getProperty("db_doctor_url");
        } else if (role == Role.NURSE) {
            url = App.properties.getProperty("db_nurse_url");
        } else {
            url = App.properties.getProperty("db_neutral_url");
        }

        connection = DriverManager.getConnection(url);
    }

    public static void disconnect() throws SQLException {
        if (connection != null) {
            role = null;
            connection.close();
        }
    }

    public static String getGrantee() {
        if (role == null) return "\"'neutral'@'%'\"";
        
        return String.format("\"'%s'@'%%'\"", role.toString().toLowerCase());
    }

    public static String getPermissedColumns(String tableName, String operation) throws SQLException {
        ensureConnection();
        
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
        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement("SELECT ID FROM users WHERE username = ?;");
        statement.setString(1, username);

        ResultSet resultSet = statement.executeQuery();
        boolean userExists = resultSet.next();
        resultSet.close();

        return userExists;
    }

    public static ResultSet selectAllWithRole(Role role) throws SQLException {
        String tableName = "users";
        String columns = getPermissedColumns(tableName, "SELECT");
        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement(String.format("SELECT %s FROM %s WHERE role = ?;", columns, tableName));
        statement.setString(1, role.toString());
        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }

    public static ResultSet selectRow(int rowID, String tableName) throws SQLException {
        String colummns = getPermissedColumns(tableName, "SELECT");
        ensureConnection();
        
        PreparedStatement statement2 = connection.prepareStatement(String.format("SELECT %s FROM %s WHERE ID = ?;", colummns, tableName));
        statement2.setInt(1, rowID);

        ResultSet resultSet2 = statement2.executeQuery();

        return resultSet2;
    }

    public static ResultSet selectMultiRow(int userID, String tableName) throws SQLException {
        String colummns = getPermissedColumns(tableName, "SELECT");
        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement(String.format("SELECT %s FROM %s WHERE userID = ?;", colummns, tableName));
        statement.setInt(1, userID);

        ResultSet resultSet = statement.executeQuery();

        return resultSet;
    }

    public static void updateRow(int rowID, String table, String column, String newValue) throws SQLException {
        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement(String.format("UPDATE %s SET %s = ? WHERE ID = ?;", table, column));
        statement.setString(1, newValue);
        statement.setInt(2, rowID);
        statement.executeUpdate();
    }

    public static Integer insertUser(String username, String password, Role role) throws Exception {
        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement("INSERT INTO users (username, password, role) VALUES (?, ?, ?);");
        statement.setString(1, username);
        statement.setString(2, Encrypter.SHA256(password));
        statement.setString(3, role.toString());
        statement.executeUpdate();

        statement = connection.prepareStatement("SELECT LAST_INSERT_ID();");
        ResultSet resultSet = statement.executeQuery();

        resultSet.next();
        Integer userID = resultSet.getInt(1);

        return userID;
    }

    public static void deleteRow(String tableName, int rowID) throws SQLException {
        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement(String.format("DELETE FROM %s WHERE ID = ?;", tableName));
        statement.setInt(1, rowID);
        statement.executeUpdate();
    }

    public static void insertEmployee(String username, String password, Role role, String firstName, String lastName, Sex sex, String birthDate, String email, String phone, String address, String managerUsername, String managerPassword, Race race, Ethnicity ethnicity) throws Exception {
        if (role != Role.DOCTOR && role != Role.NURSE) {
            throw new SQLException("Invalid role");
        }

        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement("SELECT ID FROM users WHERE username = ? AND password = ? AND role = ?;");
        statement.setString(1, managerUsername);
        statement.setString(2, Encrypter.SHA256(managerPassword));
        statement.setString(3, Role.DOCTOR.toString());

        ResultSet resultSet = statement.executeQuery();

        if (!resultSet.next()) {
            throw new SQLException("Invalid manager credentials");
        }

        int managerID = resultSet.getInt("ID");
        resultSet.close();

        int userID = insertUser(username, password, role);

        statement = connection.prepareStatement("INSERT INTO employees (ID, firstName, lastName, sex, birthDate, hireDate, email, phone, address, managerID, race, ethnicity) VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, ?);");
        statement.setInt(1, userID);
        statement.setString(2, firstName);
        statement.setString(3, lastName);
        statement.setString(4, sex.toString());
        statement.setString(5, birthDate);
        statement.setString(6, email);
        statement.setString(7, phone);
        statement.setString(8, address);
        statement.setInt(9, managerID);
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

        ensureConnection();
        
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

    public static void refreshUpdatePermissions() throws SQLException {
        ensureConnection();
        
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
        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement(String.format("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLE_PRIVILEGES WHERE PRIVILEGE_TYPE = 'DELETE' AND GRANTEE = %s;", getGrantee()));
        ResultSet resultSet = statement.executeQuery();
        deletePermissions = new HashMap<String, Boolean>();

        while (resultSet.next()) {
            String tableName = resultSet.getString("TABLE_NAME");

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
        changeRole(null);
        
        PreparedStatement statement = connection.prepareStatement("SELECT ID, username, role FROM users WHERE username = ? AND password = ?;");
        statement.setString(1, username);
        statement.setString(2, Encrypter.SHA256(password));
        
        ResultSet resultSet = statement.executeQuery();
        boolean successful = false;
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
        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement("INSERT INTO logbook (userID, IP, type) VALUES (?, ?, ?);");
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
        ensureConnection();
        
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
        ensureConnection();
        
        PreparedStatement statement;
        if (role == Role.PATIENT) {
            statement = connection.prepareStatement(String.format("SELECT %s FROM patients JOIN users ON users.ID = patients.ID WHERE patients.ID = ?;", columnName));
            statement.setInt(1, userID);

            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            return resultSet.getString(columnName);
        } else {
            statement = connection.prepareStatement(String.format("SELECT %s FROM employees JOIN users ON users.ID = employees.ID WHERE employees.ID = ?;", columnName));
            statement.setInt(1, userID);

            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            return resultSet.getString(columnName);
        }
    }

    public static Integer insertResetPasswordToken(String usernameOrEmail, Role role) throws SQLException, UnknownHostException, MessagingException {
        int token = generateRandomToken();
        String IP = InetAddress.getLocalHost().getHostAddress();

        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement("SELECT users.ID, email, users.username FROM users JOIN patients ON users.ID = patients.ID WHERE username = ? AND role = ? UNION SELECT patients.ID, email, users.username FROM patients JOIN users on users.ID = patients.ID WHERE email = ? AND role = ? UNION SELECT employees.ID, email, users.username FROM employees JOIN users ON users.ID = employees.ID WHERE email = ? AND role = ?;");
        statement.setString(1, usernameOrEmail);
        statement.setString(2, role.toString());
        statement.setString(3, usernameOrEmail);
        statement.setString(4, role.toString());
        statement.setString(5, usernameOrEmail);
        statement.setString(6, role.toString());

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            int userID = resultSet.getInt("ID");

            statement = connection.prepareStatement("INSERT INTO resetPasswordTokens (token, userID, sourceIP) VALUES (?, ?, ?);");
            statement.setInt(1, token);
            statement.setInt(2, userID);
            statement.setString(3, IP);
            statement.executeUpdate();

            return token;

        } else {
            throw new IllegalArgumentException("Invalid username or email");
        }

    }

    public static void resetPassword(int token, String password) throws SQLException, ExpiredResetPasswordTokenException, InvalidResetPasswordTokenException, NoSuchAlgorithmException, UnsupportedEncodingException {
        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement("SELECT userID, UNIX_TIMESTAMP(creationTime) * 1000 AS 'creationTimeMillis', used, UNIX_TIMESTAMP() * 1000 AS 'nowTimeMillis' FROM resetPasswordTokens WHERE token = ? ORDER BY creationTime DESC LIMIT 1;");
        statement.setInt(1, token);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            int userID = resultSet.getInt("userID");
            long creationTimeMillis = resultSet.getLong("creationTimeMillis");
            long nowTimeMillis = resultSet.getLong("nowTimeMillis");
            boolean used = resultSet.getBoolean("used");

            if (used) {
                throw new InvalidResetPasswordTokenException();
            }

            if (nowTimeMillis - creationTimeMillis > TOKEN_LIFESPAN.toMillis()) {
                throw new ExpiredResetPasswordTokenException();
            }

            statement = connection.prepareStatement("UPDATE users SET password = ? WHERE ID = ?;");
            statement.setString(1, Encrypter.SHA256(password));
            statement.setInt(2, userID);
            statement.executeUpdate();

            statement = connection.prepareStatement("UPDATE resetPasswordTokens SET used = TRUE WHERE token = ? AND userID = ?;");
            statement.setInt(1, token);
            statement.setInt(2, userID);
            statement.executeUpdate();

        } else {
            throw new InvalidResetPasswordTokenException();
        }
    }

    public static ResultSet getDoctors() throws SQLException {
        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement("SELECT employees.ID, employees.firstName, employees.lastName FROM users JOIN employees ON users.ID = employees.ID WHERE users.role = ?;");
        statement.setString(1, Role.DOCTOR.toString());

        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }

    public static Integer getMyDoctorID() throws SQLException {
        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement("SELECT preferredDoctorID from patients WHERE ID = ?;");
        statement.setInt(1, userID);

        ResultSet resultSet = statement.executeQuery();
        if (!resultSet.next()) {
            return null;
        }

        int preferredDoctorID = resultSet.getInt("preferredDoctorID");
        if (resultSet.wasNull()) {
            return null;
        }
        
        return preferredDoctorID;
    }

    public static String getMyDoctorName() throws SQLException {
        Integer doctorID = getMyDoctorID();
        if (doctorID == null) {
            return null;
        }

        return getEmployeeNameFor(doctorID);
    }

    public static String getEmployeeNameFor(int employeeID) throws SQLException {
        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement("SELECT firstName, lastName FROM employees WHERE employees.ID = ?;");
        statement.setInt(1, employeeID);

        ResultSet resultSet = statement.executeQuery();
        if (!resultSet.next()) {
            throw new SQLException("Employee does not exist");
        }

        String firstName = resultSet.getString("firstName");
        String lastName = resultSet.getString("lastName");

        return firstName + " " + lastName;
    }

    public static String getPatientNameFor(int patientID) throws SQLException {
        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement("SELECT firstName, lastName from patients WHERE ID = ?;");
        statement.setInt(1, patientID);

        ResultSet resultSet = statement.executeQuery();
        if (!resultSet.next()) {
            throw new SQLException("Patient does not exist");
        }

        String firstName = resultSet.getString("firstName");
        String lastName = resultSet.getString("lastName");

        return firstName + " " + lastName;
    }


    public static void insertVisitFor(int patientID, CreationType creationType, int doctorID, String reason, String description, String date, String time) throws SQLException {
        ensureConnection();

        ResultSet visit = getUpcomingVisitFor(patientID);

        if (visit.next()) {
            throw new SQLException("Patient already has an upcoming visit");
        }

        try {
            LocalDateTime dateTime = LocalDateTime.parse(date + "T" + time);

            if (dateTime.isBefore(LocalDateTime.now())) {
                throw new SQLException("Cannot schedule a visit for the past");
            }
        } catch (DateTimeParseException e) {
            throw new SQLException("Invalid date or time");
        }
        
        PreparedStatement statement = connection.prepareStatement(
            "INSERT INTO visits (patientID, creationType, localdate, date, time, doctorID, reason, description) " +
            "VALUES(?, ?, ?, DATE(CONVERT_TZ(CONCAT(?, ' ', ?), ?, '+00:00')), TIME(CONVERT_TZ(CONCAT(?, ' ', ?), ?, '+00:00')), ?, ?, ?);");

        String systemZoneId = ZoneId.systemDefault().getId();
        statement.setInt(1, patientID);
        statement.setString(2, creationType.toString());
        statement.setString(3, date);
        statement.setString(4, date);
        statement.setString(5, time);
        statement.setString(6, systemZoneId);
        statement.setString(7, date);
        statement.setString(8, time);
        statement.setString(9, systemZoneId);
        statement.setInt(10, doctorID);
        statement.setString(11, reason);
        statement.setString(12, description);

        statement.executeUpdate();
    }

    public static void insertMyVisit(CreationType creationType, int doctorID, String reason, String description, String date, String time) throws SQLException {
        insertVisitFor(getMyID(), creationType, doctorID, reason, description, date, time);
    }

    public static ResultSet getVisitsFor(int patientID) throws SQLException {
        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement(
            "SELECT visits.*, DATE(CONVERT_TZ(CONCAT(date, ' ', time), '+00:00', ?)) AS 'localdate', TIME(CONVERT_TZ(CONCAT(date, ' ', time), '+00:00', ?)) AS 'localtime', " +
            "CASE  " +
                "WHEN completed = TRUE THEN ? " +
                "WHEN active = TRUE THEN ? " +
                "WHEN cancelled = TRUE THEN ? " +
                "WHEN CONCAT(date, ' ', time) < TIMESTAMPADD(MINUTE, -15, NOW()) THEN ? " +
                "WHEN CONCAT(date, ' ', time) BETWEEN TIMESTAMPADD(MINUTE, -15, NOW()) AND TIMESTAMPADD(MINUTE, 15, NOW()) THEN ? " +
                "ELSE ? " +
            "END AS 'status' " +
            "FROM visits WHERE patientID = ? ORDER BY date DESC, time DESC;");
        
        statement.setString(1, ZoneId.systemDefault().getId());
        statement.setString(2, ZoneId.systemDefault().getId());
        statement.setString(3, VisitStatus.COMPLETED.toString());
        statement.setString(4, VisitStatus.ACTIVE.toString());
        statement.setString(5, VisitStatus.CANCELLED.toString());
        statement.setString(6, VisitStatus.MISSED.toString());
        statement.setString(7, VisitStatus.PENDING.toString());
        statement.setString(8, VisitStatus.UPCOMING.toString());
        statement.setInt(9, patientID);

        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }

    public static ResultSet getMyVisits() throws SQLException {
        return getVisitsFor(getMyID());
    }

    public static ResultSet getVisit(int rowID) throws SQLException {
        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement(
            "SELECT visits.*, DATE(CONVERT_TZ(CONCAT(date, ' ', time), '+00:00', ?)) AS 'localdate', TIME(CONVERT_TZ(CONCAT(date, ' ', time), '+00:00', ?)) AS 'localtime', " +
            "CASE  " +
                "WHEN completed = TRUE THEN ? " +
                "WHEN active = TRUE THEN ? " +
                "WHEN cancelled = TRUE THEN ? " +
                "WHEN CONCAT(date, ' ', time) < TIMESTAMPADD(MINUTE, -15, NOW()) THEN ? " +
                "WHEN CONCAT(date, ' ', time) BETWEEN TIMESTAMPADD(MINUTE, -15, NOW()) AND TIMESTAMPADD(MINUTE, 15, NOW()) THEN ? " +
                "ELSE ? " +
            "END AS 'status' " +
            "FROM visits WHERE ID = ?;");

        String systemZoneId = ZoneId.systemDefault().getId();
        statement.setString(1, systemZoneId);
        statement.setString(2, systemZoneId);
        statement.setString(3, VisitStatus.COMPLETED.toString());
        statement.setString(4, VisitStatus.ACTIVE.toString());
        statement.setString(5, VisitStatus.CANCELLED.toString());
        statement.setString(6, VisitStatus.MISSED.toString());
        statement.setString(7, VisitStatus.PENDING.toString());
        statement.setString(8, VisitStatus.UPCOMING.toString());
        statement.setInt(9, rowID);

        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }

    public static ResultSet getUpcomingVisitFor(int patientID) throws SQLException {
        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement(
            "SELECT * FROM ( " +
                "SELECT ID, creationType, DATE(CONVERT_TZ(CONCAT(date, ' ', time), '+00:00', ?)) AS 'localdate', TIME(CONVERT_TZ(CONCAT(date, ' ', time), '+00:00', ?)) AS 'localtime', patientID, doctorID, reason, description,  " +
                "CASE  " +
                    "WHEN completed = TRUE THEN ? " +
                    "WHEN active = TRUE THEN ? " +
                    "WHEN cancelled = TRUE THEN ? " +
                    "WHEN CONCAT(date, ' ', time) < TIMESTAMPADD(MINUTE, -15, NOW()) THEN ? " +
                    "WHEN CONCAT(date, ' ', time) BETWEEN TIMESTAMPADD(MINUTE, -15, NOW()) AND TIMESTAMPADD(MINUTE, 15, NOW()) THEN ? " +
                    "ELSE ? " +
                "END AS 'status' " +
                "FROM visits WHERE patientID = ? " +
            ") AS visits WHERE status = 'UPCOMING' ORDER BY localdate ASC LIMIT 1;");
        String systemZoneId = ZoneId.systemDefault().getId();
        statement.setString(1, systemZoneId);
        statement.setString(2, systemZoneId);
        statement.setString(3, VisitStatus.COMPLETED.toString());
        statement.setString(4, VisitStatus.ACTIVE.toString());
        statement.setString(5, VisitStatus.CANCELLED.toString());
        statement.setString(6, VisitStatus.MISSED.toString());
        statement.setString(7, VisitStatus.PENDING.toString());
        statement.setString(8, VisitStatus.UPCOMING.toString());
        statement.setInt(9, patientID);

        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }

    public static ResultSet getMyUpcomingVisit() throws SQLException {
        return getUpcomingVisitFor(getMyID());
    }

    public static ResultSet getTodaysVisits() throws SQLException {
        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement(
            "SELECT visits.*, DATE(CONVERT_TZ(CONCAT(date, ' ', time), '+00:00', ?)) AS 'localdate', TIME(CONVERT_TZ(CONCAT(date, ' ', time), '+00:00', ?)) AS 'localtime', " +
            "CASE  " +
                "WHEN completed = TRUE THEN ? " +
                "WHEN active = TRUE THEN ? " +
                "WHEN cancelled = TRUE THEN ? " +
                "WHEN CONCAT(date, ' ', time) < TIMESTAMPADD(MINUTE, -15, NOW()) THEN ? " +
                "WHEN CONCAT(date, ' ', time) BETWEEN TIMESTAMPADD(MINUTE, -15, NOW()) AND TIMESTAMPADD(MINUTE, 15, NOW()) THEN ? " +
                "ELSE ? " +
            "END AS 'status' " +
            "FROM visits WHERE DATE(CONVERT_TZ(CONCAT(date, ' ', time), '+00:00', ?)) = DATE(CONVERT_TZ(NOW(), '+00:00', ?)) ORDER BY date DESC;");
            
        String systemZoneId = ZoneId.systemDefault().getId();
        statement.setString(1, systemZoneId);
        statement.setString(2, systemZoneId);
        statement.setString(3, VisitStatus.COMPLETED.toString());
        statement.setString(4, VisitStatus.ACTIVE.toString());
        statement.setString(5, VisitStatus.CANCELLED.toString());
        statement.setString(6, VisitStatus.MISSED.toString());
        statement.setString(7, VisitStatus.PENDING.toString());
        statement.setString(8, VisitStatus.UPCOMING.toString());
        statement.setString(9, systemZoneId);
        statement.setString(10, systemZoneId);

        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }

    public static void updateVisitActivate(int visitID) throws SQLException {
        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement("UPDATE visits SET active = TRUE WHERE ID = ? AND CONCAT(date, ' ', time) BETWEEN TIMESTAMPADD(MINUTE, -" + VISIT_GRACE_PERIOD.toMinutes() + ", NOW()) AND TIMESTAMPADD(MINUTE, " + VISIT_GRACE_PERIOD.toMinutes() + ", NOW());");
        statement.setInt(1, visitID);
        statement.executeUpdate();

        if (statement.getUpdateCount() == 0) {
            throw new SQLException("Visit cannot be started");
        }
    }

    public static void updateVisitComplete(int visitID, int nurseID) throws SQLException {
        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement("UPDATE visits SET completed = TRUE, nurseID = ?, active = FALSE WHERE ID = ?;");
        statement.setInt(1, nurseID);
        statement.setInt(2, visitID);
        statement.executeUpdate();

        if (statement.getUpdateCount() == 0) {
            throw new SQLException("Visit cannot be completed");
        }
    }

    public static void updateVisitCancel(int visitID) throws SQLException {
        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement("UPDATE visits SET cancelled = TRUE WHERE active = FALSE AND completed = FALSE AND ID = ?;");
        statement.setInt(1, visitID);
        statement.executeUpdate();

        if (statement.getUpdateCount() == 0) {
            throw new SQLException("Visit cannot be cancelled");
        }
    }

    public static void updateVisitPutBack(int visitID) throws SQLException {
        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement("UPDATE visits SET active = FALSE WHERE active = TRUE AND ID = ?;");
        statement.setInt(1, visitID);
        statement.executeUpdate();

        if (statement.getUpdateCount() == 0) {
            throw new SQLException("Visit cannot be put back");
        }
    }

    public static ResultSet getActiveViits() throws SQLException {
        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement("SELECT visits.*, DATE(CONVERT_TZ(CONCAT(date, ' ', time), '+00:00', ?)) AS 'localdate', TIME(CONVERT_TZ(CONCAT(date, ' ', time), '+00:00', ?)) AS 'localtime' FROM visits WHERE active = TRUE;");
        String systemZoneId = ZoneId.systemDefault().getId();
        statement.setString(1, systemZoneId);
        statement.setString(2, systemZoneId);

        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }

    public static void updateActiveVisitCurrentPage(int activeVisitID, int currentPage) throws SQLException {
        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement("UPDATE visits SET currentPage = ? WHERE active = TRUE AND ID = ?;");
        statement.setInt(1, currentPage);
        statement.setInt(2, activeVisitID);
        statement.executeUpdate();
    }

    public static void updateActiveVisit(int activeVisitID, int currentPage, int weight, int height, int systolicBloodPressure, int diastolicBloodPressure, int heartRate, int bodyTemperature, String notes) throws SQLException {
        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement("UPDATE visits SET currentPage = ?, weight = ?, height = ?, systolicBloodPressure = ?, diastolicBloodPressure = ?, heartRate = ?, bodyTemperature = ?, notes = ? WHERE active = TRUE AND ID = ?;");
        statement.setInt(1, currentPage);
        statement.setInt(2, weight);
        statement.setInt(3, height);
        statement.setInt(4, systolicBloodPressure);
        statement.setInt(5, diastolicBloodPressure);
        statement.setInt(6, heartRate);
        statement.setInt(7, bodyTemperature);
        statement.setString(8, notes);
        statement.setInt(9, activeVisitID);
        statement.executeUpdate();
    }

    public static void updateActiveVisitNurse(int activeVisitID, int nurseID) throws SQLException {
        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement("UPDATE visits SET nurseID = ? WHERE active = TRUE AND ID = ?;");
        statement.setInt(1, nurseID);
        statement.setInt(2, activeVisitID);
        statement.executeUpdate();
    }

    public static void finishVisit(int visitID) throws SQLException {
        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement("UPDATE visits SET completed = TRUE, active = FALSE WHERE active = TRUE AND ID = ?;");
        statement.setInt(1, visitID);
        statement.executeUpdate();
    }

    public static boolean isVisitAvailable(String date, String time) throws SQLException {
        ensureConnection();

        LocalDateTime localDate = LocalDateTime.of(LocalDate.parse(date), LocalTime.parse(time));
        if (localDate.isBefore(LocalDateTime.now())) {
            return false;
        }
        
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM visits WHERE CONCAT(date, ' ', time) = CONVERT_TZ(CONCAT(?, ' ', ?), ?, '+00:00') LIMIT 1;");
        statement.setString(1, date);
        statement.setString(2, time);
        statement.setString(3, ZoneId.systemDefault().getId());

        ResultSet resultSet = statement.executeQuery();
        boolean available = !resultSet.next();
        resultSet.close();

        return available;
    }

    public static ResultSet getMyMessages() throws SQLException {
        if (role == Role.PATIENT) {
            return getMessagesForPatient(userID);
        } else {
            return getMessagesForEmployee(userID);
        }
    }

    public static ResultSet getMessagesForEmployee(int userID) throws SQLException {
        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement("SELECT creationTime, message, readStatus, senderID, receiverID FROM conversations WHERE receiverID = ? OR senderID = ? ORDER BY creationTime ASC;");
        statement.setInt(1, userID);
        statement.setInt(2, userID);

        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }

    public static ResultSet getMessagesForPatient(int userID) throws SQLException {
        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement("SELECT preferredDoctorID from patients WHERE ID = ?;");
        statement.setInt(1, userID);

        ResultSet resultSet = statement.executeQuery();
        resultSet.next();

        int doctorID = resultSet.getInt("preferredDoctorID");

        statement = connection.prepareStatement("SELECT creationTime, message, readStatus, senderID, receiverID FROM conversations WHERE (receiverID = ? AND senderID = ?) OR (senderID = ? AND receiverID = ?) ORDER BY creationTime ASC;");
        statement.setInt(1, doctorID);
        statement.setInt(2, userID);
        statement.setInt(3, doctorID);
        statement.setInt(4, userID);

        resultSet = statement.executeQuery();
        return resultSet;
    }

    public static ResultSet getMyMessagesWith(int receiverID) throws SQLException {
        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement("SELECT creationTime, message, readStatus, senderID, receiverID FROM conversations WHERE (receiverID = ? AND senderID = ?) OR (senderID = ? AND receiverID = ?) ORDER BY creationTime ASC;");
        statement.setInt(1, receiverID);
        statement.setInt(2, userID);
        statement.setInt(3, receiverID);
        statement.setInt(4, userID);

        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }

    public static ResultSet getMyMessages2(boolean ascending) throws SQLException {
        ensureConnection();
        
        PreparedStatement statement;
        
        if (ascending) {
            statement = connection.prepareStatement("SELECT ID, creationTime, message, readStatus, senderID, receiverID FROM conversations WHERE receiverID = ? OR senderID = ? ORDER BY creationTime ASC;");
        } else {
            statement = connection.prepareStatement("SELECT ID, creationTime, message, readStatus, senderID, receiverID FROM conversations WHERE receiverID = ? OR senderID = ? ORDER BY creationTime DESC;");
        }

        statement.setInt(1, userID);
        statement.setInt(2, userID);

        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }

    public static Integer getMyID() {
        return userID;
    }

    public static void sendMessageTo(int receiverID, String message) throws SQLException {
        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement("INSERT INTO conversations (senderID, receiverID, message) VALUES (?, ?, ?);");
        statement.setInt(1, userID);
        statement.setInt(2, receiverID);
        statement.setString(3, message);

        statement.executeUpdate();
    }

    public static void sendMessageToMyDoctor(String message) throws SQLException {
        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement("SELECT preferredDoctorID from patients WHERE ID = ?;");
        statement.setInt(1, userID);

        ResultSet resultSet = statement.executeQuery();
        resultSet.next();

        int doctorID = resultSet.getInt("preferredDoctorID");

        statement = connection.prepareStatement("INSERT INTO conversations (senderID, receiverID, message) VALUES (?, ?, ?);");
        statement.setInt(1, userID);
        statement.setInt(2, doctorID);
        statement.setString(3, message);

        statement.executeUpdate();
    }

    public static void readAllMessagesWith(int senderID) throws SQLException {
        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement("UPDATE conversations SET readStatus = TRUE WHERE receiverID = ? AND senderID = ?;");
        statement.setInt(1, getMyID());
        statement.setInt(2, senderID);

        statement.executeUpdate();
    }

    public static ResultSet getPatient(int patientID) throws SQLException {
        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement("SELECT users.username, users.role, insuranceProviders.name AS 'insuranceProviderName', pharmacies.name AS 'pharmacyName', employees.firstName AS 'preferredDoctorFirstName', employees.lastName AS 'preferredDoctorLastName', patients.* FROM patients JOIN users ON users.ID = patients.ID LEFT JOIN insuranceProviders ON patients.insuranceProviderID = insuranceProviders.ID LEFT JOIN pharmacies ON patients.pharmacyID = pharmacies.ID LEFT JOIN employees ON patients.preferredDoctorID = employees.ID WHERE patients.ID = ?;");
        statement.setInt(1, patientID);

        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }

    public static Integer getPatientIDByFirstNameLastNameBirthDate(String firstName, String lastName, String birthDate) throws SQLException {
        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement("SELECT patients.ID FROM patients JOIN users on users.ID = patients.ID WHERE patients.firstName = ? AND patients.lastName = ? AND patients.birthDate = ? LIMIT 1;");
        statement.setString(1, firstName);
        statement.setString(2, lastName);
        statement.setString(3, birthDate);

        ResultSet resultSet = statement.executeQuery();

        if (!resultSet.next()) {
            return null;
        }

        return resultSet.getInt("ID");
    }

    public static Integer getPatientIDByUsername(String username) throws SQLException {
        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement("SELECT patients.ID FROM patients JOIN users on users.ID = patients.ID WHERE users.username = ? LIMIT 1;");
        statement.setString(1, username);

        ResultSet resultSet = statement.executeQuery();

        if (!resultSet.next()) {
            return null;
        }

        return resultSet.getInt("ID");
    }

    public static Integer getPatientIDByEmail(String email) throws SQLException {
        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement("SELECT patients.ID FROM patients JOIN users on users.ID = patients.ID WHERE patients.email = ? LIMIT 1;");
        statement.setString(1, email);

        ResultSet resultSet = statement.executeQuery();

        if (!resultSet.next()) {
            return null;
        }

        return resultSet.getInt("ID");
    }

    public static Integer getPatientIDByPhoneNumber(String phoneNumber) throws SQLException {
        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement("SELECT patients.ID FROM patients JOIN users on users.ID = patients.ID  WHERE patients.phone = ? LIMIT 1;");
        statement.setString(1, phoneNumber);

        ResultSet resultSet = statement.executeQuery();

        if (!resultSet.next()) {
            return null;
        }

        return resultSet.getInt("ID");
    }

    public static void readAllMessagesWithMyDoctor() throws SQLException {
        readAllMessagesWith(getMyDoctorID());
    }

    public static String getUsernameFor(int userID) throws SQLException {
        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement("SELECT username FROM users WHERE ID = ?;");
        statement.setInt(1, userID);

        ResultSet resultSet = statement.executeQuery();
        resultSet.next();

        return resultSet.getString("username");
    }

    public static ResultSet getHealthConditionsFor(int patientID) throws SQLException {
        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM healthConditions WHERE userID = ?;");
        statement.setInt(1, patientID);

        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }

    public static ResultSet getAllergiesFor(int patientID) throws SQLException {
        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM allergies WHERE userID = ?;");
        statement.setInt(1, patientID);

        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }

    public static ResultSet getVaccinesFor(int patientID) throws SQLException {
        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM vaccines WHERE userID = ? ORDER BY date ASC;");
        statement.setInt(1, patientID);

        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }
    
    public static int generateRandomToken() {
        return (int) (Math.random() * 900000 + 100000);
    }

    public static long getExpirationTimeForResetPasswordToken(int token) throws InvalidResetPasswordTokenException, SQLException {
        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement("SELECT UNIX_TIMESTAMP(creationTime) * 1000 AS 'creationTimeMillis' FROM resetPasswordTokens WHERE token = ? ORDER BY creationTime DESC LIMIT 1;");
        statement.setInt(1, token);

        ResultSet resultSet = statement.executeQuery();

        if (!resultSet.next()) {
            throw new InvalidResetPasswordTokenException();
        }

        long creationTimeMillis = resultSet.getLong("creationTimeMillis");
        return creationTimeMillis + TOKEN_LIFESPAN.toMillis();
    }

    public static ArrayList<LocalTime> getVisitTimesFor(DayOfWeek dayOfWeek) throws SQLException {
        ensureConnection();

        PreparedStatement statement = connection.prepareStatement("SELECT startTime, endTime FROM visitTimes WHERE dayOfWeek = ?;");
        statement.setString(1, dayOfWeek.toString());
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();

        LocalTime startTime = resultSet.getTime("startTime").toLocalTime();
        LocalTime endTime = resultSet.getTime("endTime").toLocalTime();

        ArrayList<LocalTime> visitTimes = new ArrayList<LocalTime>();
        while (startTime.isBefore(endTime)) {
            visitTimes.add(startTime);

            startTime = startTime.plusMinutes(VISIT_DURATION.toMinutes());
        }

        return visitTimes;
    }

    public static void insertPrescriptionFor(int patientID, int doctorID, int drugID, String intakeDay, String intakeTime, String dosageQuantity, String dosageUnits, String description ) throws SQLException {
        ensureConnection();

        PreparedStatement statement = connection.prepareStatement("INSERT INTO prescriptions (userID, doctorID, drugID, intakeDay, intakeTime, dosageQuantity, dosageUnits, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
        statement.setInt(1, patientID);
        statement.setInt(2, doctorID);
        statement.setInt(3, drugID);
        statement.setString(4, intakeDay);
        statement.setString(5, intakeTime);
        statement.setString(6, dosageQuantity);
        statement.setString(7, dosageUnits);
        statement.setString(8, description);

        statement.executeUpdate();
    }

    public static ResultSet getFilteredPrescriptionsFor(int userID, String filter) throws SQLException {
        ensureConnection();
        
        filter = "%" + filter + "%";
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM prescriptions WHERE drugID IN (SELECT ID FROM drugs WHERE name LIKE ?) OR dosageQuantity LIKE ? OR dosageUnits LIKE ? OR intakeDay LIKE ? OR intakeTime LIKE ? OR description LIKE ?;");
        statement.setString(1, filter);
        statement.setString(2, filter);
        statement.setString(3, filter);
        statement.setString(4, filter);
        statement.setString(5, filter);
        statement.setString(6, filter);

        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }

    public static ResultSet getAllDrugs() throws SQLException {
        ensureConnection();
        
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM drugs;");
        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }
}