import java.sql.*;
import java.io.FileReader;
import java.util.Vector;

public class Database {
    Connection conn = null;

    //Constructor
    public Database(String dbName) {
        dbName = "jdbc:sqlite:" + dbName;

        //Try to make connection with the database
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(dbName);		
        } 
        catch (ClassNotFoundException | SQLException e) {
            System.err.println("Connection with the database failed.");
            return;
        }

        checkDatabase();
        //Create tables
        /* createPatientTable();
           createProviderTable();
           createTransactionTable();
           createServiceTable();*/
    }

    private void checkDatabase(){
        try {
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet patientSet = meta.getTables(null, null, "Patients", null);
            ResultSet serviceSet = meta.getTables(null, null, "Service", null);
            ResultSet transactionSet = meta.getTables(null, null, "Transactions", null);
            ResultSet providerSet = meta.getTables(null, null, "Provider", null);

            if(!patientSet.next()) {
                createPatientTable();
            }
            if(!serviceSet.next()){
                createServiceTable();
            }
            if(!transactionSet.next()){
                createTransactionTable();
            }
            if(!providerSet.next()){
                createProviderTable();
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*--------------Database Creation Methods-------*/

    //Creates a table if it doesn't already exist
    private void createPatientTable(){
        String query = 
            "CREATE TABLE Patients " +
            "(PatiendID	INT PRIMARY KEY	NOT NULL," +
            " Name CHAR(25) NOT NULL," +
            " Address CHAR(25) NOT NULL,"+
            " City CHAR(25) NOT NULL," +
            " State CHAR(2) NOT NULL," +
            " Zipcode CHAR(5) NOT NULL," +
            " FinancialStanding BIT NOT NULL,"+
            " Status BIT NOT NULL)";

        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
        } 
        catch (SQLException e) {
            return;
        }
    }

    //Creates a provider table if it doesn't already exist
    private void createProviderTable(){
        String query = 
            "CREATE TABLE Provider " +
            "(ProviderID INT PRIMARY KEY NOT NULL, "+
            " Name CHAR(25) NOT NULL, "+
            " Address CHAR(25) NOT NULL,"+
            " City CHAR(25) NOT NULL," +
            " State CHAR(2) NOT NULL," +
            " Zipcode CHAR(5) NOT NULL," +
            " Status BIT NOT NULL)";

        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        } 
        catch (SQLException e) {
            return;
        }
    }

    //Creates a transaction table if it doesn't already exist
    private void createTransactionTable(){
        String query = 
            "CREATE TABLE Transactions " +
            "(TransactionID INT PRIMARY KEY NOT NULL," +
            " DateTime CHAR(18) NOT NULL," +
            " ServiceDate CHAR(10) NOT NULL," +
            " Comment CHAR(100) NOT NULL," +
            " PatientID INT NOT NULL," +
            " ProviderID INT NOT NULL," +
            " ServiceID INT NOT NULL," +
            " ConsultID INT NOT NULL)";

        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        } 
        catch (SQLException e) {
            return;
        }
    }

    private void createServiceTable(){
        String query = 
            "CREATE TABLE Service " +
            "(ServiceID INT PRIMARY KEY NOT NULL," +
            " Name CHAR(25) NOT NULL," +
            " Fee INT NOT NULL, "+
            " Status BIT NOT NULL)";

        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        } 
        catch (SQLException e) {
            return;
        }
    }
    /*-----------End Database Creation Methods-------*/
}
