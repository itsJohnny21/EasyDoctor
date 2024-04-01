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
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import application.Database.Row.User;

public abstract class Database {

    		
    public enum Role {
        NEUTRAL, DOCTOR, NURSE, PATIENT;

        public static ValueOption createValueOption(String label) throws Exception{
            Role[] roles = {DOCTOR, NURSE, PATIENT};
            ArrayList<Datum> options = new ArrayList<Datum>();

            for (Role role : roles) {
                Datum option = Datum.createParentless(role.toString(), "role");
                options.add(option);
            }

            ValueOption option = new ValueOption(options, label);
            return option;
        }
    }
    
    public enum Sex {
        MALE, FEMALE, OTHER;

        public static ValueOption createValueOption(Datum sexDatum, String label) throws Exception{
            ArrayList<Datum> options = new ArrayList<Datum>();
    
            for (Sex sex : Sex.values()) {
                if (sexDatum.originalValue.equals(sex.toString())) {
                    continue;
                }

                Datum option = new Datum(sexDatum.parent, sex.toString(), sexDatum.columnName);
                options.add(option);
            }
    
            ValueOption option = new ValueOption(options, label);
            option.setOption(sexDatum);
            return option;
        }

        public static ValueOption createValueOption(String label) throws Exception{
            ArrayList<Datum> options = new ArrayList<Datum>();
    
            for (Sex sex : Sex.values()) {
                Datum option = Datum.createParentless(sex.toString(), "sex");
                options.add(option);
            }
    
            ValueOption option = new ValueOption(options, label);
            return option;
        }
    }

    public enum Race {
        WHITE, BLACK, HISPANIC, ASIAN, NATIVE, AMERICAN, PACIFIC, ISLANDER, OTHER;

        public static ValueOption createValueOption(String label) throws Exception{
            ArrayList<Datum> options = new ArrayList<Datum>();
    
            for (Race race : Race.values()) {
                Datum option = Datum.createParentless(race.toString(), "race");
                options.add(option);
            }
    
            ValueOption option = new ValueOption(options, label);
            return option;
        }
    }

    public enum Ethnicity {
        HISPANIC, NON_HISPANIC;

        public static ValueOption createValueOption(String label) throws Exception{
            ArrayList<Datum> options = new ArrayList<Datum>();
    
            for (Ethnicity ethnicity : Ethnicity.values()) {
                Datum option = Datum.createParentless(ethnicity.toString(), "ethnicity");
                options.add(option);
            }
    
            ValueOption option = new ValueOption(options, label);
            return option;
        }        
    }


    public enum Severity {
        MILD, MODERATE, SEVERE;

        public static ValueOption createValueOption(String label) throws Exception{
            ArrayList<Datum> options = new ArrayList<Datum>();
    
            for (Severity severity : Severity.values()) {
                Datum option = Datum.createParentless(severity.toString(), "severity");
                options.add(option);
            }
    
            ValueOption option = new ValueOption(options, label);
            return option;
        }        
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

        public static ValueOption createValueOption(Datum bloodTypeDatum, String label) throws Exception {
            ArrayList<Datum> options = new ArrayList<Datum>();
    
            for (BloodType bloodType : BloodType.values()) {
                Datum option = new Datum(bloodTypeDatum.parent, bloodType.toString(), bloodTypeDatum.columnName);
                options.add(option);
            }
    
            ValueOption option = new ValueOption(options, label);
            option.setOption(bloodTypeDatum);
            return option;
        }

        public static ValueOption createValueOption(String label) throws Exception{
            ArrayList<Datum> options = new ArrayList<Datum>();
    
            for (BloodType bloodType : BloodType.values()) {
                Datum option = Datum.createParentless(bloodType.toString(), "bloodType");
                options.add(option);
            }
    
            ValueOption option = new ValueOption(options, label);
            return option;
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

    public static void connectAs(Role role) throws SQLException {

        switch (role) {
            case DOCTOR:
                connection = DriverManager.getConnection("jdbc:mysql://easydoctor.c1wkcaa6ol0w.us-east-2.rds.amazonaws.com:3306/easydoctor?user=doctor&password=doctor123");
                break;
            case NURSE:
                connection = DriverManager.getConnection("jdbc:mysql://easydoctor.c1wkcaa6ol0w.us-east-2.rds.amazonaws.com:3306/easydoctor?user=nurse&password=nurse123");
                break;
            case PATIENT:
                connection = DriverManager.getConnection("jdbc:mysql://easydoctor.c1wkcaa6ol0w.us-east-2.rds.amazonaws.com:3306/easydoctor?user=patient&password=patient123");
                break;
            case NEUTRAL:
                connection = DriverManager.getConnection("jdbc:mysql://easydoctor.c1wkcaa6ol0w.us-east-2.rds.amazonaws.com:3306/easydoctor?user=neutral&password=neutral123");
                break;
            default:
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

    public static void insertEmployee(String username, String password, Role role, String firstName, String lastName, Sex sex, String birthDate, String email, String phone, String address, String managerID) throws Exception {
        if (role != Role.DOCTOR && role != Role.NURSE) {
            throw new SQLException("Invalid role");
        }

        int userID = insertUser(username, password, role);

        PreparedStatement statement2 = connection.prepareStatement("INSERT INTO employees (ID, firstName, lastName, sex, birthDate, hireDate, email, phone, address, managerID) VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?);");
        statement2.setInt(1, userID);
        statement2.setString(2, firstName);
        statement2.setString(3, lastName);
        statement2.setString(4, sex.toString());
        statement2.setString(5, birthDate);
        statement2.setString(6, email);
        statement2.setString(7, phone);
        statement2.setString(8, address);
        statement2.setString(9, managerID);

        statement2.executeUpdate();

        if (statement2.getUpdateCount() == 0) {
            throw new SQLException("Employee not created");
        }
    }

    public static void insertPatient(String username, String password, String firstName, String lastName, String sex, String birthDate, String email, String phone, String address, String race, String ethnicity) throws Exception {
        Integer userID = insertUser(username, password, Role.PATIENT);

        PreparedStatement statement = connection.prepareStatement("INSERT INTO patients (ID, firstName, lastName, sex, birthDate, email, phone, address, race, ethnicity) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
        statement.setInt(1, userID);
        statement.setString(2, firstName);
        statement.setString(3, lastName);
        statement.setString(4, sex);
        statement.setString(5, birthDate);
        statement.setString(6, email);
        statement.setString(7, phone);
        statement.setString(8, address);
        statement.setString(9, race);
        statement.setString(10, ethnicity);
        System.out.println(statement.toString());

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
            User user = new User(resultSet);
            role = Role.valueOf(user.role.originalValue);
            userID = Integer.parseInt(user.userID.originalValue);

            reconnectAs(role);
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
        reconnectAs(Role.NEUTRAL);
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

    public abstract static class Row {
        public String tableName;
        public Integer rowID;
    
        public static class User extends Row {
            public Datum userID;
            public Datum role;
            public Datum username;

            public static User getFor(int userID) throws Exception {
                ResultSet resultSet = Database.selectRow(userID, "users");
    
                if (!resultSet.next()) {
                    return null;
                }
    
                User user = new User(resultSet);
                return user;
            }

            public User(ResultSet resultSet) throws Exception {
                this.tableName = "users";
    
                String rowIDColumn = "ID";
                String userIDColumn = "ID";
                String roleColumn = "role";
                String usernameColumn = "username";
    
                this.rowID = resultSet.getInt(rowIDColumn);
                this.userID = new Datum(this, resultSet.getString(userIDColumn), userIDColumn);
                this.role = new Datum(this, resultSet.getString(roleColumn), roleColumn);
                this.username = new Datum(this, resultSet.getString(usernameColumn), usernameColumn);
            }
        }
    
        public static class Allergy extends Row {
            public Datum userID;
            public Datum allergen;
            public Datum commonSource;
            public Datum severity;
            public Datum type;
            public Datum notes;
    
            public static ArrayList<Allergy> getAllFor(int userID) throws Exception {
                ResultSet resultSet = Database.selectMultiRow(userID, "allergies");
                ArrayList<Allergy> allergies = new ArrayList<Allergy>();
                
                while (resultSet.next()) {
                    allergies.add(new Allergy(resultSet));
                }
    
                if (allergies.size() == 0) {
                    return null;
                }
    
                return allergies;
            }
    
            public Allergy(ResultSet resultSet) throws Exception {
                this.tableName = "allergies";
    
                String userIDColumn = "userID";
                String allergenColumn = "allergen";
                String commonSourceColumn = "commonSource";
                String severityColumn = "severity";
                String typeColumn = "type";
                String notesColumn = "notes";
    
    
                this.rowID = resultSet.getInt("ID");
                this.userID = new Datum(this, resultSet.getString(userIDColumn), userIDColumn);
                this.allergen = new Datum(this, resultSet.getString(allergenColumn), allergenColumn);
                this.commonSource = new Datum(this, resultSet.getString(commonSourceColumn), commonSourceColumn);
                this.severity = new Datum(this, resultSet.getString(severityColumn), severityColumn);
                this.type = new Datum(this, resultSet.getString(typeColumn), typeColumn);
                this.notes = new Datum(this, resultSet.getString(notesColumn), notesColumn);
            }
        }

        public static class Surgery extends Row {
            public Datum userID;
            public Datum doctorID;
            public Datum date;
            public Datum type;
            public Datum location;
            public Datum notes;

            public static ArrayList<Surgery> getAllFor(int userID) throws Exception {
                ResultSet resultSet = Database.selectMultiRow(userID, "surgeries");
                ArrayList<Surgery> surgeries = new ArrayList<Surgery>();
                
                while (resultSet.next()) {
                    surgeries.add(new Surgery(resultSet));
                }
    
                return surgeries;
            }

            public Surgery(ResultSet resultSet) throws Exception {
                this.tableName = "surgeries";

                String userIDColumn = "userID";
                String doctorIDColumn = "doctorID";
                String typeColumn = "type";
                String dateColumn = "date";
                String locationColumn = "location";
                String notesColumn = "notes";

                this.rowID = resultSet.getInt("ID");
                this.userID = new Datum(this, resultSet.getString(userIDColumn), userIDColumn);
                this.doctorID = new Datum(this, resultSet.getString(doctorIDColumn), doctorIDColumn);
                this.type = new Datum(this, resultSet.getString(typeColumn), typeColumn);
                this.date = new Datum(this, resultSet.getString(dateColumn), dateColumn);
                this.location = new Datum(this, resultSet.getString(locationColumn), locationColumn);
                this.notes = new Datum(this, resultSet.getString(notesColumn), notesColumn);
            }
        }

        public static class Patient extends Row {
            public Datum userID;
            public Datum firstName;
            public Datum lastName;
            public Datum sex;
            public Datum birthDate;
            public Datum email;
            public Datum phone;
            public Datum address;
            public Datum preferredDoctorID;
            public Datum bloodType;
            public Datum height;
            public Datum weight;
            public Datum race;
            public Datum ethnicity;
            public Datum insuranceProvider;
            public Datum insuranceID;
            public Datum emergencyContactName;
            public Datum emergencyContactPhone;
            public Datum motherFirstName;
            public Datum motherLastName;
            public Datum fatherFirstName;
            public Datum fatherLastName;

            public Patient(ResultSet resultSet) throws Exception {
                this.tableName = "patients";

                String rowIDColumn = "ID";
                String userIDColumn = "ID";
                String firstNameColumn = "firstName";
                String lastNameColumn = "lastName";
                String sexColumn = "sex";
                String birthDateColumn = "birthDate";
                String emailColumn = "email";
                String phoneColumn = "phone";
                String addressColumn = "address";
                String preferredDoctorIDColumn = "preferredDoctorID";
                String bloodTypeColumn = "bloodType";
                String heightColumn = "height";
                String weightColumn = "weight";
                String raceColumn = "race";
                String ethnicityColumn = "ethnicity";
                String insuranceProviderColumn = "insuranceProvider";
                String insuranceIDColumn = "insuranceID";
                String emergencyContactNameColumn = "emergencyContactName";
                String emergencyContactPhoneColumn = "emergencyContactPhone";
                String motherFirstNameColumn = "motherFirstName";
                String motherLastNameColumn = "motherLastName";
                String fatherFirstNameColumn = "fatherFirstName";
                String fatherLastNameColumn = "fatherLastName";

                this.rowID = resultSet.getInt(rowIDColumn);
                this.userID = new Datum(this, resultSet.getString(userIDColumn), userIDColumn);
                this.firstName = new Datum(this, resultSet.getString(firstNameColumn), firstNameColumn);
                this.lastName = new Datum(this, resultSet.getString(lastNameColumn), lastNameColumn);
                this.sex = new Datum(this, resultSet.getString(sexColumn), sexColumn);
                this.birthDate = new Datum(this, resultSet.getString(birthDateColumn), birthDateColumn);
                this.email = new Datum(this, resultSet.getString(emailColumn), emailColumn);
                this.phone = new Datum(this, resultSet.getString(phoneColumn), phoneColumn);
                this.address = new Datum(this, resultSet.getString(addressColumn), addressColumn);
                this.preferredDoctorID = new Datum(this, resultSet.getString(preferredDoctorIDColumn), preferredDoctorIDColumn);
                this.bloodType = new Datum(this, resultSet.getString(bloodTypeColumn), bloodTypeColumn);
                this.height = new Datum(this, resultSet.getString(heightColumn), heightColumn);
                this.weight = new Datum(this, resultSet.getString(weightColumn), weightColumn);
                this.race = new Datum(this, resultSet.getString(raceColumn), raceColumn);
                this.ethnicity = new Datum(this, resultSet.getString(ethnicityColumn), ethnicityColumn);
                this.insuranceProvider = new Datum(this, resultSet.getString(insuranceProviderColumn), insuranceProviderColumn);
                this.insuranceID = new Datum(this, resultSet.getString(insuranceIDColumn), insuranceIDColumn);
                this.emergencyContactName = new Datum(this, resultSet.getString(emergencyContactNameColumn), emergencyContactNameColumn);
                this.emergencyContactPhone = new Datum(this, resultSet.getString(emergencyContactPhoneColumn), emergencyContactPhoneColumn);
                this.motherFirstName = new Datum(this, resultSet.getString(motherFirstNameColumn), motherFirstNameColumn);
                this.motherLastName = new Datum(this, resultSet.getString(motherLastNameColumn), motherLastNameColumn);
                this.fatherFirstName = new Datum(this, resultSet.getString(fatherFirstNameColumn), fatherFirstNameColumn);
                this.fatherLastName = new Datum(this, resultSet.getString(fatherLastNameColumn), fatherLastNameColumn);
            }

            public static Patient getFor(int userID) throws Exception {
                ResultSet resultSet = Database.selectRow(userID, "patients");

                if (!resultSet.next()) {
                    return null;
                }

                Patient patient = new Patient(resultSet);
                return patient;
            }

            public Datum getPreferredDoctor() throws Exception {
                Employee employee = Employee.getFor(Integer.parseInt(preferredDoctorID.originalValue));
                preferredDoctorID.newValue = employee.firstName.originalValue + " " + employee.lastName.originalValue;
                return preferredDoctorID;
            }
        }

        public static class Employee extends Row {
            public Datum userID;
            public Datum firstName;
            public Datum lastName;
            public Datum sex;
            public Datum birthDate;
            public Datum email;
            public Datum phone;
            public Datum address;
            public Datum managerID;

            public Employee(ResultSet resultSet) throws Exception {
                this.tableName = "employees";

                String rowIDColumn = "ID";
                String userIDColumn = "ID";
                String firstNameColumn = "firstName";
                String lastNameColumn = "lastName";
                String sexColumn = "sex";
                String birthDateColumn = "birthDate";
                String emailColumn = "email";
                String phoneColumn = "phone";
                String addressColumn = "address";
                String managerIDColumn = "managerID";

                this.rowID = resultSet.getInt(rowIDColumn);
                this.userID = new Datum(this, resultSet.getString(userIDColumn), userIDColumn);
                this.firstName = new Datum(this, resultSet.getString(firstNameColumn), firstNameColumn);
                this.lastName = new Datum(this, resultSet.getString(lastNameColumn), lastNameColumn);
                this.sex = new Datum(this, resultSet.getString(sexColumn), sexColumn);
                this.birthDate = new Datum(this, resultSet.getString(birthDateColumn), birthDateColumn);
                this.email = new Datum(this, resultSet.getString(emailColumn), emailColumn);
                this.phone = new Datum(this, resultSet.getString(phoneColumn), phoneColumn);
                this.address = new Datum(this, resultSet.getString(addressColumn), addressColumn);
                this.managerID = new Datum(this, resultSet.getString(managerIDColumn), managerIDColumn);
            }

            public static ArrayList<Employee> getAllDoctors() throws Exception {
                ArrayList<Employee> doctors = new ArrayList<Employee>();
                ResultSet resultSet = Database.selectAllWithRole(Role.DOCTOR);

                while (resultSet.next()) {
                    Employee doctor = Employee.getFor(resultSet.getInt("ID"));
                    doctors.add(doctor);
                }
                return doctors;
            }

            public static Employee getFor(int userID) throws Exception {
                ResultSet resultSet = Database.selectRow(userID, "employees");

                if (!resultSet.next()) {
                    return null;
                }

                Employee employee = new Employee(resultSet);
                return employee;
            }

            public static ArrayList<Datum> getAllDoctorNames() throws Exception {
                ResultSet resultSet = Database.selectAllWithRole(Role.DOCTOR);
                ArrayList<Datum> doctors = new ArrayList<Datum>();

                while (resultSet.next()) {
                    User doctor = new User(resultSet);
                    doctors.add(doctor.userID);
                }

                return doctors;
            }
        }

    }
}