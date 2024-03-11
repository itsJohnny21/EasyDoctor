package application;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Database {
    public static Connection connection;

    public static void connect() throws SQLException {
        connection = DriverManager.getConnection("easydoctor.c1wkcaa6ol0w.us-east-2.rds.amazonaws.com", "admin", "Grapes6421!");
        if (connection == null) {
            throw new SQLException("Connection to the database failed.");
        } else {
            System.out.println("Connected to the database.");
        }
    }

    public static void close() throws SQLException {
        connection.close();
    }

    public static ResultSet signIn(String username, String password) throws SQLException, UnknownHostException {
        Inet4Address sourceIp = (Inet4Address) Inet4Address.getLocalHost();
        String ipSource = sourceIp.toString();

        PreparedStatement statement = connection.prepareStatement(String.format(
            "SELECT * FROM employees WHERE username = '%s' AND password = SHA2('%s', 512);",
            username, password
            ));
            
        ResultSet resultSet = statement.executeQuery();

        boolean successful = resultSet.next();

        statement = connection.prepareStatement(String.format(
            "INSERT INTO signIns (sourceIp, username, password, successful) VALUES ('%s', '%s', '%s', %b);",
            ipSource, username, password, successful
        ));
        
        statement.executeUpdate();

        return (successful) ? resultSet : null;
    }

    public static boolean userExists(String username) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(String.format(
            "SELECT * FROM employees WHERE username = '%s';",
            username
        ));

        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }
}