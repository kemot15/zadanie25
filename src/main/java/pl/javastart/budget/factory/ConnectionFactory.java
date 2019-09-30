package pl.javastart.budget.factory;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    public Connection budgetConnection () throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/budget?serverTimezone=UTC&characterEncoding=utf8";
        String username = "root";
        String pass = "java.12345";
        return (Connection) DriverManager.getConnection(url, username, pass);
    }
}
