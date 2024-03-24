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

import application.Database.Table.User;

public abstract class Database {

    		
    public enum Role {
        NEUTRAL, DOCTOR, NURSE, PATIENT;
    }

    public static Connection connection;
    public static Integer userID;
    public static Role role;
    public static HashMap<String, HashMap<String, Boolean>> updatePermissions;

    static {
        updatePermissions = new HashMap<String, HashMap<String, Boolean>>();
    }

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

    public static ResultSet selectFor(int userID, String tableName) throws Exception {
        String grantee = String.format("'%s'@'%%'", role.toString().toLowerCase());
        PreparedStatement statement = connection.prepareStatement(String.format("SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMN_PRIVILEGES WHERE TABLE_NAME = ? AND PRIVILEGE_TYPE = ? AND GRANTEE = \"%s\";", grantee));
        String operation = "SELECT"; //! do something here
        statement.setString(1, tableName);
        statement.setString(2, operation);

        ResultSet resultSet = statement.executeQuery();
        ArrayList<String> columnNames = new ArrayList<String>();
        
        while (resultSet.next()) {
            columnNames.add(resultSet.getString("COLUMN_NAME"));
        }

        String columnNamesString = String.join(", ", columnNames);
        
        PreparedStatement statement2 = connection.prepareStatement(String.format("SELECT %s FROM %s WHERE userID = ?;", columnNamesString, tableName));
        statement2.setInt(1, userID);

        ResultSet resultSet2 = statement2.executeQuery();

        return resultSet2;
    }

    public static void updateRow(int rowID, String table, String column, String newValue) throws Exception {
        PreparedStatement statement = connection.prepareStatement(String.format("UPDATE %s SET %s = ? WHERE userID = ?;", table, column));
        statement.setString(1, newValue);
        statement.setInt(2, rowID);
        System.out.println(statement.toString());

        statement.executeUpdate();
    }

    public static void refreshPermissions() throws SQLException {
        PreparedStatement statement = connection.prepareStatement(String.format("SELECT TABLE_NAME, COLUMN_NAME, IF(PRIVILEGE_TYPE = 'UPDATE', true, false) AS Updatable FROM INFORMATION_SCHEMA.COLUMN_PRIVILEGES WHERE GRANTEE = \"'%s'@'%%'\";", role.toString().toLowerCase()));
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            String tableName = resultSet.getString("TABLE_NAME");
            String columnName = resultSet.getString("COLUMN_NAME");
            Boolean updatable = resultSet.getBoolean("Updatable");

            if (!updatePermissions.containsKey(tableName)) {
                updatePermissions.put(tableName, new HashMap<String, Boolean>());
            }

            updatePermissions.get(tableName).put(columnName, updatable);
        }
    }

    public static boolean signIn(String username, String password) throws SQLException, UnknownHostException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, Exception {
        boolean successful = false;

        PreparedStatement statement = connection.prepareStatement("SELECT userID, username, role FROM users WHERE username = ? AND password = SHA2(?, 256);");
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

            refreshPermissions();

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

    public static boolean isUpdatable(String tableName, String columnName) {
        if (!updatePermissions.get(tableName).containsKey(columnName)) {
            return false;
        }

        return updatePermissions.get(tableName).get(columnName);
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

    // public static void UpdatePermissionsFor(Role role) throws Exception {
    //     HashMap<String, Boolean> updatePermissions = new HashMap<String, Boolean>();
    //     PreparedStatement statement = connection.prepareStatement("SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMN_PRIVILEGES WHERE PRIVILEGE_TYPE = 'UPDATE' AND GRANTEE = ?;");
    //     String grantee = "'" + role.toString().toLowerCase() + "'@'%'";
    //     statement.setString(1, grantee);
    //     ResultSet resultSet = statement.executeQuery();

    //     while (resultSet.next()) {
    //         updatePermissions.put(resultSet.getString("COLUMN_NAME"), true);
    //         System.out.println(resultSet.getString("COLUMN_NAME") + " is updatable");
    //     }
        
    //     return updatePermissions;
    // }

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
    
    public static class Table {
        public String tableName;
        public int rowID;
    
        public Table(ResultSet resultSet) throws Exception {
        }
    
        public Table() throws Exception {}
    
        public static class User extends Table {
            public final static String TABLE_NAME = "users";
            public Datum userID;
            public Datum role;
            public Datum username;
    
            public static User getFor(int userID) throws Exception {
                ResultSet resultSet = Database.selectFor(userID, TABLE_NAME);
    
                if (!resultSet.next()) {
                    return null;
                }
    
                User user = new User(resultSet);
                return user;
            }
    
            public User(ResultSet resultSet) throws Exception {
                this.tableName = TABLE_NAME;
    
                String rowIDColumn = "userID";
                String userIDColumn = "userID";
                String roleColumn = "role";
                String usernameColumn = "username";
    
                this.rowID = resultSet.getInt(rowIDColumn);
                this.userID = new Datum(this, resultSet.getString(userIDColumn), userIDColumn);
                this.role = new Datum(this, resultSet.getString(roleColumn), roleColumn);
                this.username = new Datum(this, resultSet.getString(usernameColumn), usernameColumn);
            }
        }
    
        public static class Allergy extends Table {
            public final static String TABLE_NAME = "allergies";
            public Datum userID;
            public Datum allergen;
            public Datum commonSource;
            public Datum severity;
            public Datum type;
            public Datum notes;
    
            public static ArrayList<Allergy> getAllFor(int userID) throws Exception {
                ResultSet resultSet = Database.selectFor(userID, TABLE_NAME);
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
                this.tableName = TABLE_NAME;
    
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
                
            public Datum[] getData() {
                return new Datum[]{userID, allergen, commonSource, severity, type, notes};
            }
        }

        public static class Patient extends Table {
            public final static String TABLE_NAME = "patients";
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
                this.tableName = TABLE_NAME;

                String rowIDColumn = "userID";
                String userIDColumn = "userID";
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
                ResultSet resultSet = Database.selectFor(userID, TABLE_NAME);

                if (!resultSet.next()) {
                    return null;
                }

                Patient patient = new Patient(resultSet);
                return patient;
            }
        }

        public static class Employee extends Table {
            public final static String TABLE_NAME = "employees";
            public Datum userID;
            public Datum firstName;
            public Datum lastName;
            public Datum gender;
            public Datum birthDate;
            public Datum email;
            public Datum phone;
            public Datum address;
            public Datum mangerID;

            public Employee(ResultSet resultSet) throws Exception {
                this.tableName = TABLE_NAME;

                String rowIDColumn = "userID";
                String userIDColumn = "userID";
                String firstNameColumn = "firstName";
                String lastNameColumn = "lastName";
                String genderColumn = "gender";
                String birthDateColumn = "birthDate";
                String emailColumn = "email";
                String phoneColumn = "phone";
                String addressColumn = "address";
                String managerIDColumn = "managerID";

                this.rowID = resultSet.getInt(rowIDColumn);
                this.userID = new Datum(this, resultSet.getString(userIDColumn), userIDColumn);
                this.firstName = new Datum(this, resultSet.getString(firstNameColumn), firstNameColumn);
                this.lastName = new Datum(this, resultSet.getString(lastNameColumn), lastNameColumn);
                this.gender = new Datum(this, resultSet.getString(genderColumn), genderColumn);
                this.birthDate = new Datum(this, resultSet.getString(birthDateColumn), birthDateColumn);
                this.email = new Datum(this, resultSet.getString(emailColumn), emailColumn);
                this.phone = new Datum(this, resultSet.getString(phoneColumn), phoneColumn);
                this.address = new Datum(this, resultSet.getString(addressColumn), addressColumn);
                this.mangerID = new Datum(this, resultSet.getString(managerIDColumn), managerIDColumn);
            }

            public static Employee getFor(int userID) throws Exception {
                ResultSet resultSet = Database.selectFor(userID, TABLE_NAME);

                if (!resultSet.next()) {
                    return null;
                }

                Employee employee = new Employee(resultSet);
                return employee;
            }

            public static ArrayList<String> getDoctors() throws Exception {
                PreparedStatement statement = connection.prepareStatement("SELECT firstName, lastName FROM employees JOIN users ON employees.userID = users.userID WHERE role = 'DOCTOR';");
                ResultSet resultSet = statement.executeQuery();
                ArrayList<String> doctors = new ArrayList<String>();
        
                while (resultSet.next()) {
                    String doctor = resultSet.getString("firstName") + " " + resultSet.getString("lastName");
                    doctors.add(doctor);
                }
        
                return doctors;
            }
        }

    }
}