package pl.javastart.budget;

import pl.javastart.budget.dao.BudgetDAO;
import pl.javastart.budget.factory.ConnectionFactory;
import pl.javastart.budget.model.Transaction;

import java.lang.annotation.Target;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Scanner;


public class Budget {
    static Scanner scanner = new Scanner(System.in);
    static BudgetDAO budgetDAO;

    static {
        try {
            budgetDAO = new BudgetDAO(new ConnectionFactory());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public Budget() throws SQLException, ClassNotFoundException {
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException, ParseException {
        menu();
        scanner.close();
        budgetDAO.close();
    }

    private static void menu() throws SQLException, ParseException {
        while(true) {

            System.out.println("Co chcesz zrobić?");
            System.out.println("1. Dodaj transakcje");
            System.out.println("2. Modyfikuj transakcje");
            System.out.println("3. Usun transakcje");
            System.out.println("4. Wyswietl wszystkie przychody");
            System.out.println("5. Wyswietl wszystkie wydatki");
            System.out.println("0. Koniec");

            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    newTransaction();
                    break;
                case 2:
                    editTransaction();
                    break;
                case 3:
                    removeTransaction();
                    break;
                case 4:
                    showTranssaction(true);
                    break;
                case 5:
                    showTranssaction(false);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Błędny wybór!");
            }
        }
    }

    private static void menuEdit(Transaction transaction, int idTransaction) throws SQLException, ParseException {
        while(true) {

            System.out.println(budgetDAO.getTransactionById(idTransaction));
            System.out.println("Co chcesz edytowac?");
            System.out.println("1. Typ");
            System.out.println("2. Opis");
            System.out.println("3. Kwota");
            System.out.println("4. Data");
            System.out.println("5. Wyjscie do menu");

            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    System.out.println("Podaj nowy typ dla transakcji");
                    transaction.setType(scanner.nextLine());
                    break;
                case 2:
                    System.out.println("Podaj nowy opis dla transakcji");
                    transaction.setDescription(scanner.nextLine());
                    break;
                case 3:
                    System.out.println("Podaj nowy kwote dla transakcji");
                    transaction.setAmount(scanner.nextDouble());
                    scanner.nextLine();
                    break;
                case 4:
                    System.out.println("Podaj nowy date dla transakcji");
                    transaction.setDate(scanner.nextLine());
                    break;
                case 5:
                    budgetDAO.editTransaction(transaction, idTransaction);
                    menu();

                default:
                    System.out.println("Błędny wybór!");
            }
        }
    }

    private static void newTransaction () throws SQLException, ParseException {
        Transaction transaction = new Transaction();
        System.out.println("Podaj typ transakcji");
        transaction.setType(scanner.nextLine());
        System.out.println("Podaj opis transakcji");
        transaction.setDescription(scanner.nextLine());
        System.out.println("Podaj kwotę transakcji");
        transaction.setAmount(scanner.nextDouble());
        scanner.nextLine();
        System.out.println("Podaj datę transakcji");
        transaction.setDate(scanner.nextLine());
        budgetDAO.addTransaction(transaction);
        System.out.println("Transakcja dodana");
    }

    private static void editTransaction () throws SQLException, ParseException {
        System.out.println("Podaj id transakcji do modyfikacji");
        int idTransaction = scanner.nextInt();
        scanner.nextLine();
        Transaction transaction = budgetDAO.getTransactionById(idTransaction);
        menuEdit(transaction, idTransaction);

    }

    private static void removeTransaction () throws SQLException, ParseException {
        System.out.println("Podaj id transakcji do usuniecia");
        int idTransaction = scanner.nextInt();
        scanner.nextLine();
        budgetDAO.removeTransaction(idTransaction);
        System.out.println("Transakcja " + idTransaction + " usunieta!");
    }

    private static void showTranssaction (boolean isIncome) throws SQLException {
        List<Transaction> transactionList = budgetDAO.showTransaction(isIncome);
        for (Transaction transaction : transactionList){
            System.out.println(transaction);
        }
    }
}
