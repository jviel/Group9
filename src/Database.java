import java.sql.*;
import java.io.FileReader;
import java.util.Vector;

public class Database {
    int patientNum;
    int serviceNum;
    int	transactionNum;
    int	providerNum;
    Connection 	conn = null;

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
            System.err.println("Error: " + e.getMessage());
            System.err.println("The program will be shut down.");
            System.exit(1);
        }

        checkDatabase();

    }

    /*----Checks the database and creates tables during the first run---*/
    private void checkDatabase(){
        try {
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet patientSet = meta.getTables(null, null, "Patients", null);
            ResultSet serviceSet = meta.getTables(null, null, "Services", null);
            ResultSet transactionSet = meta.getTables(null, null, "Transactions", null);
            ResultSet providerSet = meta.getTables(null, null, "Providers", null);

            String patientQuery = 
                "CREATE TABLE Patients " +
                "(PatientID INT NOT NULL," +
                " Name CHAR(25) NOT NULL," +
                " Address CHAR(25) NOT NULL,"+
                " City CHAR(25) NOT NULL," +
                " State CHAR(2) NOT NULL," +
                " Zipcode CHAR(5) NOT NULL," +
                " FinancialStanding BIT NOT NULL,"+
                " Status BIT NOT NULL," +
                " PRIMARY KEY(PatientID))";

            String providerQuery = 
                "CREATE TABLE Providers " +
                "(ProviderID INT NOT NULL, "+
                " Name CHAR(25) NOT NULL, "+
                " Address CHAR(25) NOT NULL,"+
                " City CHAR(25) NOT NULL," +
                " State CHAR(2) NOT NULL," +
                " Zipcode CHAR(5) NOT NULL," +
                " Status BIT NOT NULL," +
                "PRIMARY KEY(ProviderID))";

            String transactionQuery = 
                "CREATE TABLE Transactions " +
                "(TransactionID INT NOT NULL," +
                " DateTime CHAR(18) NOT NULL," +
                " ServiceDate CHAR(10) NOT NULL," +
                " Comment CHAR(100) NOT NULL," +
                " PatientID INT NOT NULL," +
                " ProviderID INT NOT NULL," +
                " ServiceID INT NOT NULL," +
                " ConsultID INT NOT NULL," +
                " PRIMARY KEY(TransactionID))";

            String serviceQuery = 
                "CREATE TABLE Services " +
                "(ServiceID INT NOT NULL," +
                " Name CHAR(25) NOT NULL," +
                " Fee INT NOT NULL, "+
                " Status BIT NOT NULL," +
                " PRIMARY KEY(ServiceID))";

            String [] patientColumns = {"PatientID", "Name", "Address", "City", "State",
                                        "Zipcode", "FinancialStanding", "Status"};

            String [] providerColumns = {"ProviderID", "Name", "Address", "City", "State",
                                        "Zipcode", "Status"};

            String [] serviceColumns = {"ServiceID", "Name", "Fee", "Status"};

            String [] transactionColumns = {"TransactionID", "DateTime", "ServiceDate",
                                            "Comment", "PatientID", "ProviderID",
                                            "ServiceID", "ConsultID"};

            /*----Check if the tables exist---*/
            if(!patientSet.next()) {
                execQuery(patientQuery);
                //createPatientTable();
            }
            if(!serviceSet.next()){
                execQuery(serviceQuery);
                //createServiceTable();
            }
            if(!transactionSet.next()){
                execQuery(transactionQuery);
                //createTransactionTable();
            }
            if(!providerSet.next()){
                execQuery(providerQuery);
                //createProviderTable();
            }

            /*---Check if the columns are corrupted---*/
            checkColumns("Patients", patientColumns);
            checkColumns("Providers", providerColumns);
            checkColumns("Transactions", transactionColumns);
            checkColumns("Services", serviceColumns);      

            patientNum	= 100000000 + getRowsCount("Patients");
            providerNum	= 100000000 + getRowsCount("Providers");
            transactionNum = 100000000 + getRowsCount("Transactions");
            serviceNum 	= 100000 + getRowsCount("Services");
            

            patientSet.close();
            serviceSet.close();
            transactionSet.close();
            providerSet.close();
        } 
        catch (SQLException e) {
            System.err.println("There was an error with the database.");
            System.err.println("Error: " + e.getMessage());
            System.err.println("The program will be shut down.");
            System.exit(1);
        }
    }

    /*----Checks for corrupted columns----*/
    private void checkColumns(String tableName, String [] columns) throws SQLException{
        DatabaseMetaData meta = conn.getMetaData();
        ResultSet columnSet = null;

        for(String column : columns){
            columnSet = meta.getColumns(null, null, tableName, column);
            if(!columnSet.next()){
                throw new SQLException("Corrupted Database. " + tableName);
            }
        }
        columnSet.close();
    }

    private void execQuery(String query){
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
        } 
        catch (SQLException e) {
            System.err.println("There was an error with the database.");
            System.err.println("Error: " + e.getMessage());
            System.err.println("The program will be shut down.");
            System.exit(1);
        }
    }

    public int addPatient(Patient newPatient){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Patient currentPatient = null;

        try {
            stmt = conn.prepareStatement("SELECT * FROM Patients WHERE Name=? AND City=?");
            stmt.setString(1, newPatient.getName());
            stmt.setString(2, newPatient.getCity());
            rs = stmt.executeQuery();

            while(rs.next()){
                currentPatient = new Patient(rs.getString("Name"), rs.getString("Address"),
                        rs.getString("City"), rs.getString("State"), rs.getString("Zipcode"),
                        rs.getInt("FinancialStanding"), rs.getInt("Status"));
                if(currentPatient.equals(newPatient)){
                    System.out.println("Patient already exists.");
                    stmt.close();
                    return -1;
                }
            }

            stmt = conn.prepareStatement("INSERT INTO Patients VALUES (?,?,?,?,?,?,?,?)");
            stmt.setInt(1, patientNum);
            stmt.setString(2, newPatient.getName());
            stmt.setString(3, newPatient.getAddress());
            stmt.setString(4, newPatient.getCity());
            stmt.setString(5, newPatient.getState());
            stmt.setString(6, newPatient.getZipcode());
            stmt.setBoolean(7, true);
            stmt.setBoolean(8, true);
            stmt.executeUpdate();
            patientNum++;
            stmt.close();

        } catch (SQLException | InputException e) {
            System.err.println("Invalid patient data. The patient will not be added.");
            return -1;
        }
        return patientNum - 1;
    }

    private int getRowsCount(String tableName){
        Statement stmt = null;
        ResultSet rs = null;
        int rowsCount = 0;

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM " + tableName);
            rowsCount = rs.getInt("total");
        } 
        catch (SQLException e) {
            System.err.println("There was an error with the database.");
            System.err.println("Error: " + e.getMessage());
            System.err.println("The program will be shut down.");
            System.exit(1);
        }

        return rowsCount;
    }
   
}
