package pl.javastart.budget.dao;

import pl.javastart.budget.factory.ConnectionFactory;
import pl.javastart.budget.model.Transaction;

import javax.swing.table.TableRowSorter;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class BudgetDAO {
    private Connection connection;

    public BudgetDAO(ConnectionFactory connectionFactory) throws SQLException, ClassNotFoundException {
        connection = connectionFactory.budgetConnection();
    }

    public void addTransaction (Transaction transaction) throws SQLException, ParseException {
        String query = "INSERT INTO transaction (type, description, amount, date) VALUES (?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1,transaction.getType());
        preparedStatement.setString(2,transaction.getDescription());
        preparedStatement.setDouble(3,transaction.getAmount());
        preparedStatement.setString(4,transaction.getDate());

        preparedStatement.executeUpdate();
    }

    public Transaction getTransactionById (int id) throws SQLException {
        String query = "SELECT id, type, description, amount, date FROM transaction WHERE id = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        Transaction transaction = new Transaction();
        if (resultSet.next()){
            transaction.setId(resultSet.getInt("id"));
            transaction.setType(resultSet.getString("type"));
            transaction.setDescription(resultSet.getString("description"));
            transaction.setAmount(resultSet.getInt("amount"));
            transaction.setDate(resultSet.getString("date"));
        }
        return transaction;
    }

    public void editTransaction (Transaction transaction, int id) throws SQLException, ParseException {
        String query = "UPDATE transaction SET type = ?, description = ?, amount = ?, date = ?  WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, transaction.getType());
        preparedStatement.setString(2, transaction.getDescription());
        preparedStatement.setDouble(3, transaction.getAmount());
        preparedStatement.setString(4,transaction.getDate());
        preparedStatement.setInt(5, id);
        preparedStatement.executeUpdate();
        System.out.println("Edycja " + transaction.toString());
    }

    public void removeTransaction (int id) throws SQLException, ParseException {
        String query = "DELETE FROM transaction WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }

    public List<Transaction> showTransaction (boolean isIncome) throws SQLException {
        String query = "SELECT id, type, description, amount, date FROM transaction WHERE type = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        if (isIncome)
            preparedStatement.setString(1, "przychod");
        else
            preparedStatement.setString(1, "wydatek");
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Transaction> transactionList = new ArrayList<>();


        while (resultSet.next()){
                Transaction transaction = new Transaction();
                transaction.setId(resultSet.getInt("id"));
                transaction.setType(resultSet.getString("type"));
                transaction.setDescription(resultSet.getString("description"));
                transaction.setAmount(resultSet.getInt("amount"));
                transaction.setDate(resultSet.getString("date"));
                transactionList.add(transaction);
        }

        return transactionList;
    }

    public void close () throws SQLException {
        connection.close();
    }
}
