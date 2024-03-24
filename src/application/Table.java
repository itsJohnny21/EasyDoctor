// package application;

// import java.sql.ResultSet;
// import java.util.ArrayList;

// public class Table {
//     public String tableName;
//     public int rowID;

//     public Table(ResultSet resultSet) throws Exception {
//     }

//     public Table() throws Exception {}

//     public static class User extends Table {
//         public final static String TABLE_NAME = "users";
//         public Datum userID;
//         public Datum role;
//         public Datum username;

//         public static User getFor(int userID) throws Exception {
//             ResultSet resultSet = Database.selectFor(userID, TABLE_NAME);

//             if (!resultSet.next()) {
//                 return null;
//             }

//             User user = new User(resultSet);
//             return user;
//         }

//         public User(ResultSet resultSet) throws Exception {
//             this.tableName = TABLE_NAME;

//             String rowIDColumn = "userID";
//             String userIDColumn = "userID";
//             String roleColumn = "role";
//             String usernameColumn = "username";

//             this.rowID = resultSet.getInt(rowIDColumn);
//             this.userID = new Datum(this, resultSet.getString(userIDColumn), userIDColumn);
//             this.role = new Datum(this, resultSet.getString(roleColumn), roleColumn);
//             this.username = new Datum(this, resultSet.getString(usernameColumn), usernameColumn);
//         }
//     }

//     public static class Allergy extends Table {
//         public final static String TABLE_NAME = "allergies";
//         public Datum userID;
//         public Datum allergen;
//         public Datum commonSource;
//         public Datum severity;
//         public Datum type;
//         public Datum notes;

//         public static ArrayList<Allergy> getAllFor(int userID) throws Exception {
//             ResultSet resultSet = Database.selectFor(userID, TABLE_NAME);
//             ArrayList<Allergy> allergies = new ArrayList<Allergy>();
//             for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
//                 System.out.println(resultSet.getMetaData().getColumnName(i + 1));
//             }
            
//             while (resultSet.next()) {
//                 allergies.add(new Allergy(resultSet));
//             }

//             if (allergies.size() == 0) {
//                 return null;
//             }

//             return allergies;
//         }

//         public Allergy(ResultSet resultSet) throws Exception {
//             this.tableName = TABLE_NAME;

//             String userIDColumn = "userID";
//             String allergenColumn = "allergen";
//             String commonSourceColumn = "commonSource";
//             String severityColumn = "severity";
//             String typeColumn = "type";
//             String notesColumn = "notes";


//             this.rowID = resultSet.getInt("ID");
//             this.userID = new Datum(this, resultSet.getString(userIDColumn), userIDColumn);
//             this.allergen = new Datum(this, resultSet.getString(allergenColumn), allergenColumn);
//             this.commonSource = new Datum(this, resultSet.getString(commonSourceColumn), commonSourceColumn);
//             this.severity = new Datum(this, resultSet.getString(severityColumn), severityColumn);
//             this.type = new Datum(this, resultSet.getString(typeColumn), typeColumn);
//             this.notes = new Datum(this, resultSet.getString(notesColumn), notesColumn);
//         }
//     }
// }