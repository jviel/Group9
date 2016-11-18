package com.psu.group9;
import com.psu.group9.Patient;

import java.sql.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

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
            /*---Retrieve tables---*/
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet patientSet = meta.getTables(null, null, "Patients", null);
            ResultSet serviceSet = meta.getTables(null, null, "Services", null);
            ResultSet transactionSet = meta.getTables(null, null, "Transactions", null);
            ResultSet providerSet = meta.getTables(null, null, "Providers", null);

            /*---Queries used to create tables---*/
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
                " Fee FLOAT NOT NULL, "+
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
            }
            if(!serviceSet.next()){
                execQuery(serviceQuery);
            }
            if(!transactionSet.next()){
                execQuery(transactionQuery);
            }
            if(!providerSet.next()){
                execQuery(providerQuery);
            }

            /*---Check if the columns are corrupted---*/
            checkColumns("Patients", patientColumns);
            checkColumns("Providers", providerColumns);
            checkColumns("Transactions", transactionColumns);
            checkColumns("Services", serviceColumns);      

            /*---Get last used ID's. Set them to correct size---*/
            patientNum	= 100000000 + getRowsCount("Patients");
            providerNum	= 100000000 + getRowsCount("Providers");
            transactionNum = 100000000 + getRowsCount("Transactions");
            serviceNum 	= 100000 + getRowsCount("Services");

            patientSet.close();
            serviceSet.close();
            transactionSet.close();
            providerSet.close();
        } 
        /*---Kill the program if there were any problems with the database---*/
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

    /*---Performs executeUpdate of a given query---*/
    private void execQuery(String query) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(query);
        stmt.close();
    }

    /*----Returns the row count of a specific table----*/
    private int getRowsCount(String tableName) throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        int rowsCount = 0;

        stmt = conn.createStatement();
        rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM " + tableName);
        rowsCount = rs.getInt("total");
        stmt.close();
        rs.close();

        return rowsCount;
    }   

    /*---Checks if a given entry by ID exists in the db---*/
    private Boolean entryExists(String table, int ID) {
        Statement stmt = null;
        ResultSet rs = null;
        Boolean exists = false;
        String column = table.substring(0, table.length() - 1);
        column += "ID";

        try {
            stmt=conn.createStatement(); 
            rs = stmt.executeQuery("SELECT * FROM " + table + " WHERE " + column + " = " + Integer.toString(ID));
            if(rs.next()){
                exists = true;
            }
            stmt.close();
            rs.close();
        }
        catch(SQLException e) {
            System.err.println(e.getMessage());
        }
        return exists;
    }

    /*----Adds a patient to the database, return ID----*/
    public int addPatient(Patient newPatient){
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            //Check if a patient already exists in the database
            stmt = conn.prepareStatement("SELECT * FROM Patients WHERE Name=? AND Address=? AND City=? AND State=? " +
                    "AND Zipcode=?");
            stmt.setString(1, newPatient.getName());
            stmt.setString(2, newPatient.getAddress());
            stmt.setString(3, newPatient.getCity());
            stmt.setString(4, newPatient.getState());
            stmt.setString(5, newPatient.getZip());
            rs = stmt.executeQuery();

            if(rs.next()){
                stmt.close();
                rs.close();
                return -2;
            }

            //Otherwise adds to database and returns patient id
            stmt = conn.prepareStatement("INSERT INTO Patients VALUES (?,?,?,?,?,?,?,?)");
            stmt.setInt(1, patientNum);
            stmt.setString(2, newPatient.getName());
            stmt.setString(3, newPatient.getAddress());
            stmt.setString(4, newPatient.getCity());
            stmt.setString(5, newPatient.getState());
            stmt.setString(6, newPatient.getZip());
            stmt.setBoolean(7, true);
            stmt.setBoolean(8, true);
            stmt.executeUpdate();
            patientNum++;
            stmt.close();
            rs.close();

        }
        catch(SQLException e){
            System.err.println("Error occured in the database while adding the patient data.");
            return -1;
        }
        return patientNum - 1;
    }

    /*----Updates patient data in the database----*/
    public Boolean updatePatient(int ID, Patient patient){
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            if(!entryExists("Patients", ID)){
                return false;
            }

            // We check to make sure that what the Patient is being updated to isn't already a
            // duplicate of something in the DB.
            stmt = conn.prepareStatement("SELECT * FROM Patients WHERE Name=? AND Address=? AND City=? AND State=? " +
                    "AND Zipcode=?");
            stmt.setString(1, patient.getName());
            stmt.setString(2, patient.getAddress());
            stmt.setString(3, patient.getCity());
            stmt.setString(4, patient.getState());
            stmt.setString(5, patient.getZip());        
            rs = stmt.executeQuery();

            if(rs.next()){
                stmt.close();
                rs.close();
                return false;
            }

            stmt = conn.prepareStatement("UPDATE Patients SET Name=?, Address=?, City=?, " +
                    "State=?, Zipcode=?, FinancialStanding=?, Status=? " +
                    "WHERE PatientID=?");
            stmt.setString(1, patient.getName());
            stmt.setString(2, patient.getAddress());
            stmt.setString(3, patient.getCity());
            stmt.setString(4, patient.getState());
            stmt.setString(5, patient.getZip());
            stmt.setBoolean(6, patient.getFinancialStanding());
            stmt.setBoolean(7, patient.getStatus());
            stmt.setInt(8, ID);
            stmt.executeUpdate();
            stmt.close();
            rs.close();
        } 
        catch (SQLException e) {
            System.err.println("Error occured in the database while updating the patient data.");
            return false;
        }
        return true;
    }

    /*---Sets patient Status=0, marking as deleted---*/
    public Boolean removePatient(int ID){
        Statement stmt = null;

        try {
            if(!entryExists("Patients", ID)){
                return false;
            }
            stmt = conn.createStatement();
            stmt.executeUpdate("UPDATE Patients SET Status = 0 " +
                    "WHERE PatientID = " + Integer.toString(ID));
            stmt.close();
        } 
        catch (SQLException e) {
            System.err.println("Error occurred in the database while removing a patient.");
            return false;
        }
        return true;
    }

    /*---Sets patient FinancialStanding=0, marking as suspended---*/
    public Boolean suspendPatient(int ID) {
        Statement stmt = null;

        try {
            if(!entryExists("Patients", ID)) {
                return false;
            }
            stmt = conn.createStatement();
            stmt.executeUpdate("UPDATE Patients SET FinancialStanding = 0 " +
                    "WHERE PatientID = " + Integer.toString(ID));
            stmt.close();
        }
        catch (SQLException e) {
            System.err.println("Error occurred in the database while suspending a patient.");
            return false;
        }
        return true;
    }

    /*---Sets patient FinancialStanding=1, marking as active---*/
    public Boolean reinstatePatient(int ID) {
        Statement stmt = null;

        try {
            if(!entryExists("Patients", ID)) {
                return false;
            }
            stmt = conn.createStatement();
            stmt.executeUpdate("UPDATE Patients SET FinancialStanding = 1 " +
                    "WHERE PatientID = " + Integer.toString(ID));
            stmt.close();
        }
        catch (SQLException e) {
            System.err.println("Error occurred in the database while reinstating a patient.");
            return false;
        }
        return true;
    }

    /*---Adds a service to the database, return ID---*/
    public int addService(Service newService) {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            //Check if the service record already exists
            stmt = conn.prepareStatement("SELECT * FROM Services WHERE Name=? AND Fee=?");
            stmt.setString(1, newService.getName());
            stmt.setFloat(2, newService.getFee());
            rs = stmt.executeQuery();

            if(rs.next()){
                stmt.close();
                return -2;
            }

            //Otherwise adds to database and returns service id
            stmt = conn.prepareStatement("INSERT INTO Services VALUES (?,?,?,?)");
            stmt.setInt(1, serviceNum);
            stmt.setString(2, newService.getName());
            stmt.setDouble(3, newService.getFee());
            stmt.setBoolean(4, true);
            stmt.executeUpdate();
            serviceNum++;
            stmt.close();
            rs.close();

        }
        catch(SQLException e){
            System.err.println("Error occured in the database while adding the service data.");
            return -1;
        }
        return serviceNum - 1;
    }

    /*---Updates service data in the database---*/
    public Boolean updateService(int ID, Service service){
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            if(!entryExists("Services", ID)){
                return false;
            }

            // We check to make sure that the updated service isn't an exact duplicate of
            // something already in the DB.
            stmt = conn.prepareStatement("SELECT * FROM Services WHERE Name=? AND Fee=?");
            stmt.setString(1, service.getName());
            stmt.setFloat(2, service.getFee());
            rs = stmt.executeQuery();

            if(rs.next()){
                rs.close();
                stmt.close();
                return false;
            }

            stmt = conn.prepareStatement("UPDATE Services SET Name=?, Fee=? WHERE ServiceID=?");
            stmt.setString(1, service.getName());
            stmt.setDouble(2, service.getFee());
            stmt.setInt(3, ID);
            stmt.executeUpdate();
            stmt.close();
        } 
        catch (SQLException e) {
            System.err.println("Error occured in the database while updating the service data.");
            return false;
        }
        return true;	
    }

    /*---Sets service Status=0, marking as deleted---*/
    public Boolean removeService(int ID){
        Statement stmt = null;

        try {
            if(!entryExists("Services", ID)){
                return false;
            }
            stmt = conn.createStatement();
            stmt.executeUpdate("UPDATE Services SET Status = 0 " +
                    "WHERE ServiceID = " + Integer.toString(ID));
        } 
        catch (SQLException e) {
            System.err.println("Error occured in the database while removing a patient.");
            return false;
        }
        return true;
    }

    /*---Adds a provider to the database, return ID---*/
    public int addProvider(Provider newProvider){
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            //Check if the provider record already exists
            stmt = conn.prepareStatement("SELECT * FROM Providers WHERE Name=? AND Address=? AND City=? AND State=? " +
                    "AND Zipcode=?");
            stmt.setString(1, newProvider.getName());
            stmt.setString(2, newProvider.getAddress());
            stmt.setString(3, newProvider.getCity());
            stmt.setString(4, newProvider.getState());
            stmt.setString(5, newProvider.getZip());
            rs = stmt.executeQuery();

            if(rs.next()){
                stmt.close();
                rs.close();
                return -2;
            }

            //Otherwise adds to database and returns provider id
            stmt = conn.prepareStatement("INSERT INTO Providers VALUES (?,?,?,?,?,?,?)");
            stmt.setInt(1, providerNum);
            stmt.setString(2, newProvider.getName());
            stmt.setString(3, newProvider.getAddress());
            stmt.setString(4, newProvider.getCity());
            stmt.setString(5, newProvider.getState());
            stmt.setString(6, newProvider.getZip());
            stmt.setBoolean(7, true);
            stmt.executeUpdate();
            providerNum++;
            stmt.close();
            rs.close();

        }
        catch(SQLException e){
            System.err.println("Error occured in the database while adding the provider data.");
            return -1;
        }
        return providerNum - 1;
    }

    /*---Updates provider data in the database---*/
    public Boolean updateProvider(int ID, Provider provider){
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            if(!entryExists("Patients", ID)){
                return false;
            }

            // We check to make sure that what the Patient is being updated to isn't already a
            // duplicate of something in the DB.
            stmt = conn.prepareStatement("SELECT * FROM Providers WHERE Name=? AND Address=? AND City=? AND State=? " +
                    "AND Zipcode=?");
            stmt.setString(1, provider.getName());
            stmt.setString(2, provider.getAddress());
            stmt.setString(3, provider.getCity());
            stmt.setString(4, provider.getState());
            stmt.setString(5, provider.getZip());
            rs = stmt.executeQuery();

            if(rs.next()){
                stmt.close();
                rs.close();
                return false;
            }

            //Otherwise update data
            stmt = conn.prepareStatement("UPDATE Providers SET Name=?, Address=?, City=?, " +
                    "State=?, Zipcode=?, Status=? " +
                    "WHERE ProviderID=?");

            stmt.setString(1, provider.getName());
            stmt.setString(2, provider.getAddress());
            stmt.setString(3, provider.getCity());
            stmt.setString(4, provider.getState());
            stmt.setString(5, provider.getZip());
            stmt.setBoolean(6, provider.getStatus());
            stmt.setInt(7, ID);
            stmt.executeUpdate();
            stmt.close();
            rs.close();
        } 
        catch (SQLException e) {
            System.err.println("Error occured in the database while updating the provider data.");
            return false;
        }
        return true;	
    }

    /*---Sets provider Status=0, marking as deleted---*/
    public Boolean removeProvider(int ID){
        Statement stmt = null;

        try {
            if(!entryExists("Providers", ID)){
                return false;
            }
            stmt = conn.createStatement();
            stmt.executeUpdate("UPDATE Providers SET Status = 0 " +
                    "WHERE ProviderID = " + Integer.toString(ID));
        } 
        catch (SQLException e) {
            System.err.println("Error occured in the database while removing a provider.");
            return false;
        }
        return true;
    }  

    /////Unused
    /*public void addPatients(String filename) {
      String line;
      Patient currentPatient;
      int currentPatientID;
      int lineNumber = 1;

    // Fatal exception try.
    try {
    BufferedReader reader = new BufferedReader(
    new FileReader(filename)
    );

    while((line = reader.readLine()) != null) {
    String [] splitLine = line.split(",");
    // Individual line exception try.
    try {
    currentPatient = new Patient(
    splitLine[0], // Name
    splitLine[1], // Address
    splitLine[2], // City
    splitLine[3], // State
    splitLine[4], // Zipcode
    1,            // Enrollment Status
    1             // Financial Standing
    );
    currentPatientID = addPatient(currentPatient);

    if(currentPatientID != -1) {
    System.out.println("Added " + currentPatient.getName() +
    " to database. ID = " +
    currentPatientID);
    }

    else {
    System.out.println("Tried to add " +
    currentPatient.getName() + 
    ", but patient already exists.");
    }
    }
    catch(InputException e) {
    System.out.println("Error for " + splitLine[0] + ": " +
    e.getMessage());
    }
    catch(ArrayIndexOutOfBoundsException e) {
    System.out.println("ArrayIndexOutOfBounds exception on line " +
    Integer.toString(lineNumber));
    }
    catch(NumberFormatException e) {
    System.out.println("NumberFormatException on line " +
    Integer.toString(lineNumber));
    }


    lineNumber++;
    }
    reader.close();
    }
    catch(IOException e) {
    System.out.println(e.getMessage());
    return;
    }

    return;
    }

    ///Unused
    /*public void addProviders(String filename) {
    String line;
    Provider currentProvider;
    int currentProviderID;
    int lineNumber = 1;

    // Fatal exception try.
    try {
    BufferedReader reader = new BufferedReader(
    new FileReader(filename)
    );

    while((line = reader.readLine()) != null) {
    String [] splitLine = line.split(",");
    // Individual line exception try.
    try {
    currentProvider = new Provider(
    splitLine[0], // Name
    splitLine[1], // Address
    splitLine[2], // City
    splitLine[3], // State
    splitLine[4], // Zipcode
    1             // Enrollment Status
    );
    currentProviderID = addProvider(currentProvider);

    if(currentProviderID != -1) {
    System.out.println("Added " + currentProvider.getName() +
    " to database. ID = " +
    currentProviderID);
    }

    else {
    System.out.println("Tried to add " +
    currentProvider.getName() + 
    ", but provider already exists.");
    }
    }
    catch(InputException e) {
    System.out.println("Error for " + splitLine[0] + ": " +
    e.getMessage());
    }
    catch(ArrayIndexOutOfBoundsException e) {
    System.out.println("ArrayIndexOutOfBounds exception on line " +
    Integer.toString(lineNumber));
    }
    catch(NumberFormatException e) {
    System.out.println("NumberFormatException on line " +
    Integer.toString(lineNumber));
    }


    lineNumber++;
    }

    reader.close();

    }
    catch(IOException e) {
    System.out.println(e.getMessage());
    return;
    }

    return;
    }

    ///Unused
    /*public void addServices(String filename) {
    String line;
    Service currentService;
    int currentServiceID;
    int lineNumber = 1;

    // Fatal exception try.
    try {
    BufferedReader reader = new BufferedReader(
    new FileReader(filename)
    );

    while((line = reader.readLine()) != null) {
    String [] splitLine = line.split(",");
    // Individual line exception try.
    try {
    currentService = new Service(
    splitLine[0],                  // Name
    Float.parseFloat(splitLine[1]) // Price
    );
    currentServiceID = addService(currentService);

    if(currentServiceID != -1) {
    System.out.println("Added " + currentService.getName() +
    " to database. ID = " +
    currentServiceID);
    }

    else {
    System.out.println("Tried to add " +
    currentService.getName() + 
    ", but service already exists.");
    }
    }
    catch(InputException e) {
    System.out.println("Error for " + splitLine[0] + ": " +
    e.getMessage());
    }
    catch(ArrayIndexOutOfBoundsException e) {
    System.out.println("ArrayIndexOutOfBounds exception on line " +
    Integer.toString(lineNumber));
    }
    catch(NumberFormatException e) {
    System.out.println("NumberFormatException on line " +
    Integer.toString(lineNumber));
    }


    lineNumber++;
    }

    reader.close();

    }
    catch(IOException e) {
    System.out.println(e.getMessage());
    return;
    }

    return;
    }
    */

    /*---Checks if an entry exists and is active---*/
    private Boolean entryExistsAndIsActive(String tableName, int ID) throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        String column = tableName.substring(0, tableName.length() - 1);
        column += "ID";

        stmt = conn.createStatement();
        rs = stmt.executeQuery("SELECT 1 FROM " + tableName + " WHERE " +
                column + " = " + Integer.toString(ID) + " AND Status = 1");

        if(rs.next()) {
            return true;
        }

        return false;
    }

    /*---Prints all patients---*/
    public void printAllPatients() {
        Statement stmt = null;
        ResultSet rs = null;
        Patient currentPatient;

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM Patients");

            while(rs.next()) {
                currentPatient = new Patient(rs.getInt("PatientID"),
                        rs.getString("Name"),
                        rs.getString("Address"),
                        rs.getString("City"),
                        rs.getString("State"),
                        rs.getString("Zipcode"),
                        rs.getInt("Status"),
                        rs.getInt("FinancialStanding"));
                System.out.println(currentPatient + "\n");
            }

            rs.close();
            stmt.close();
        }
        catch (Exception e) {
            System.err.println("Error occured in the database while printing patients.");
        }
    }

    /*---Prints all providers---*/
    public void printAllProviders() {
        Statement stmt = null;
        ResultSet rs = null;
        Provider currentProvider;

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery ("SELECT * FROM Providers");

            while(rs.next()) {
                currentProvider = new Provider(rs.getInt("ProviderID"),
                        rs.getString("Name"),
                        rs.getString("Address"),
                        rs.getString("City"),
                        rs.getString("State"),
                        rs.getString("Zipcode"),
                        rs.getBoolean("Status"));
                System.out.println(currentProvider + "\n");
            }
            rs.close();
            stmt.close();
        }
        catch (Exception e) {
            System.err.println("Error occured in the database while printing providers.");
        }
    }

    /*---Prints all services---*/
    public void printAllServices() {
        Statement stmt = null;
        ResultSet rs = null;
        Service currentService;

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery ("SELECT * FROM Services");

            while(rs.next()) {
                currentService = new Service(rs.getInt("ServiceID"),
                        rs.getString("Name"),
                        rs.getFloat("Fee"),
                        rs.getInt("Status"));
                System.out.println(currentService + "\n");
            }

            rs.close();
            stmt.close();
        }
        catch (Exception e) {
            System.err.println("Error occured in the database while printing services.");
        }
    }

    /*--Adds new transaction, returns ID---*/
    public int addTransaction(Transaction newTransaction) {
        PreparedStatement stmt = null;

        try {
            // We need to ensure that the Provider, Patient, and Service IDs
            // exist.

            if(!entryExistsAndIsActive("Patients", newTransaction.getPatientID())) {
                return -3;
            }

            if(!entryExistsAndIsActive("Providers", newTransaction.getProviderID())) {
                return -4;
            }

            if(!entryExistsAndIsActive("Services", newTransaction.getServiceID())) {
                return -5;
            }

            // Otherwise, we're good!

            stmt = conn.prepareStatement ("INSERT INTO Transactions VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

            stmt.setInt(1, transactionNum);
            stmt.setString(2, newTransaction.getDateTime());
            stmt.setString(3, toSQLDate(newTransaction.getServiceDate()));
            stmt.setString(4, newTransaction.getComments());
            stmt.setInt(5, newTransaction.getProviderID());
            stmt.setInt(6, newTransaction.getPatientID());
            stmt.setInt(7, newTransaction.getServiceID());
            stmt.setInt(8, newTransaction.getConsultationNumber());
            stmt.executeUpdate();
            stmt.close();
            transactionNum++;


        } catch(SQLException e) {
            System.err.println("Error occured in the database while adding the transaction data.");
            return -1;
        }

        return transactionNum - 1;
    }

    ///Unused
    /*public void addTransactions(String filename) {
      String line;
      Transaction currentTransaction;
      int currentTransactionID;
      int lineNumber = 1;

    // Fatal exception try.
    try {
    BufferedReader reader = new BufferedReader(
    new FileReader(filename)
    );

    while((line = reader.readLine()) != null) {
    String [] splitLine = line.split(",");
    // Individual line exception try.
    try {
    currentTransaction = new Transaction(
    splitLine[0],                      // Service Date
    Integer.parseInt(splitLine[1]),    // Provider ID 
    Integer.parseInt(splitLine[2]),    // Patient ID
    Integer.parseInt(splitLine[3]),    // Service ID
    Integer.parseInt(splitLine[4]),    // Consult ID
    splitLine[5]                       // Comment
    );
    currentTransactionID = addTransaction(currentTransaction);

    if(currentTransactionID >= 0) {
    System.out.println("Added " +
    currentTransaction.getProviderID() +
    " -> " +
    currentTransaction.getPatientID() + 
    "to database. ID = " + currentTransactionID);
    }
    }
    catch(InputException e) {
    System.out.println("Error for " + 
    splitLine[1] + " -> " + splitLine[2] + ": " +
    e.getMessage());
    }
    catch(ArrayIndexOutOfBoundsException e) {
    System.out.println("ArrayIndexOutOfBounds exception on line " +
    Integer.toString(lineNumber));
    }
    catch(NumberFormatException e) {
    System.out.println("NumberFormatException on line " +
    Integer.toString(lineNumber));
    }

    lineNumber++;
    }
    reader.close();

    }
    catch(IOException e) {
    System.out.println(e.getMessage());
    return;
    }

    return;
    }

    /* ///Not sure if needed
    public Boolean updateTransaction(int ID, Transaction updateTransaction) {
    PreparedStatement pStatement = null;

    try {
    // Check if the Transaction is there.

    if(!(entryExists("Transactions", ID))) {
    return false;
    }

    // Next, we check to see if the various components exist or not.

    if(!(entryExistsAndIsActive("Patients", updateTransaction.getPatientID())) ||
    !(entryExistsAndIsActive("Providers", updateTransaction.getProviderID())) ||
    !(entryExistsAndIsActive("Services", updateTransaction.getServiceID()))) {
    return false;
    }

    // If it exists, we update with the updateTransaction object.

    pStatement = conn.prepareStatement(
    "UPDATE Transactions " +
    "SET " +
    "DateTime = ?, " +
    "ServiceDate = ?, " +
    "Comment = ?, " +
    "PatientID = ?, " +
    "ServiceID = ?, " +
    "ConsultID = ?, " +
    "WHERE TransactionID = ?"
    );

    pStatement.setString(1, updateTransaction.getDateTime());
    pStatement.setString(2, toSQLDate(updateTransaction.getServiceDate()));
    pStatement.setString(3, updateTransaction.getComment());
    pStatement.setInt(4, updateTransaction.getProviderID());
    pStatement.setInt(5, updateTransaction.getPatientID());
    pStatement.setInt(6, updateTransaction.getServiceID());
    pStatement.setInt(7, updateTransaction.getConsultID());

    pStatement.executeUpdate();
    pStatement.close();
    }

    catch (SQLException e) {
    System.err.println(e.getClass() + ": " + e.getMessage());
    return false;
    }

    return true;
    }
    */

    /*---Prints all transactions---*/
    public void printAllTransactions() {
        Statement stmt = null;
        ResultSet rs = null;
        Transaction currentTransaction;

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery ("SELECT * FROM Transactions");

            while(rs.next()) {
                currentTransaction = new Transaction(
                        rs.getInt("TransactionID"),
                        rs.getInt("PatientID"),
                        rs.getInt("ProviderID"),
                        rs.getInt("ServiceID"),
                        rs.getInt("ConsultID"),
                        rs.getString("DateTime"),
                        toOutputDate(rs.getString("ServiceDate")),
                        rs.getString("Comment"));
                System.out.println(currentTransaction + "\n");
            }
            rs.close();
            stmt.close();
        }
        catch (Exception e) {
            System.err.println("Error occured in the database while printing transactions.");
        }
    }

    /*---Returns a vector of patient or provider objects by ID---*/
    private Vector<Entity> getEntityByID(String table, int ID) {
        Statement stmt = null;
        ResultSet rs = null;
        String IDColumn = table;
        IDColumn = IDColumn.substring(0, IDColumn.length() - 1) + "ID";
        Vector<Entity> returnVec = new Vector<Entity>();

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT 1 FROM " + table + " WHERE " + 
                    IDColumn + " = " + Integer.toString(ID));

            // Because the function can build a vector of Patients or Providers, we
            // need to have different conditional blocks for whether we grabbed from
            // the Patient row or the Provider row.
            if(rs.next()){
                if(IDColumn.matches("PatientID")) {
                    returnVec.add( new Patient(
                                rs.getInt(IDColumn),
                                rs.getString("Name"),
                                rs.getString("Address"),
                                rs.getString("City"),
                                rs.getString("State"),
                                rs.getString("Zipcode"),
                                rs.getInt("Status"),
                                rs.getInt("FinancialStanding")
                                )
                            );
                } else if(IDColumn.matches("ProviderID")) {
                    returnVec.add( new Provider(
                                rs.getInt(IDColumn),
                                rs.getString("Name"),
                                rs.getString("Address"),
                                rs.getString("City"),
                                rs.getString("State"),
                                rs.getString("Zipcode"),
                                rs.getBoolean("Status")
                                )
                            );
                }
            }
            stmt.close();
            rs.close();
        }
        catch(SQLException | InputException e) {
            System.err.println("Error occured in the database while retrieving patients/providers.");
        }

        return returnVec;
    }

    /*---Returns a vector of patient or provider objects by a String(like name)---*/
    private Vector<Entity> getEntityByString(String table, String column, String criteria) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Vector<Entity> returnVec = new Vector<Entity>();
        String IDColumn = table.substring(0, table.length() - 1) + "ID";

        try {
            stmt = conn.prepareStatement("SELECT * FROM " + table + " WHERE " + column + " = ?");
            stmt.setString(1, criteria);
            rs = stmt.executeQuery();

            // Because the function can build a vector of Patients or Providers, we
            // need to have different conditional blocks for whether we grabbed from
            // the Patient row or the Provider row.
            while(rs.next()) {
                if(IDColumn.matches("PatientID")) {
                    returnVec.add( new Patient(
                                rs.getInt("PatientID"),
                                rs.getString("Name"),
                                rs.getString("Address"),
                                rs.getString("City"),
                                rs.getString("State"),
                                rs.getString("Zipcode"),
                                rs.getInt("Status"),
                                rs.getInt("FinancialStanding")
                                )
                            );
                }

                if(IDColumn.matches("ProviderID")) {
                    returnVec.add( new Provider(
                                rs.getInt("ProviderID"),
                                rs.getString("Name"),
                                rs.getString("Address"),
                                rs.getString("City"),
                                rs.getString("State"),
                                rs.getString("Zipcode"),
                                rs.getBoolean("Status")
                                )
                            );
                }
            }
            stmt.close();
            rs.close();
        }
        catch(SQLException | InputException e) {
            System.err.println("Error occured in the database while retrieving patients/providers.");
        }

        return returnVec;
    }

    // All of the database getter functions, which execute the private getter
    // functions listed above with various tables, columns, and criteria.

    public Vector<Entity> getPatientByID(int ID) {
        return getEntityByID("Patients", ID);
    }

    public Vector<Entity> getPatientsByName(String name) {
        return getEntityByString("Patients", "Name", name);
    }

    public Vector<Entity> getPatientsByAddress(String address) {
        return getEntityByString("Patients", "Address", address);
    }

    public Vector<Entity> getPatientsByCity(String city)  {
        return getEntityByString("Patients", "City", city);
    }

    public Vector<Entity> getPatientsByState(String state)  {
        return getEntityByString("Patients", "State", state);
    }

    public Vector<Entity> getPatientsByZipcode(String zipcode)
    {
        return getEntityByString("Patients", "Zipcode", zipcode);
    }

    public Vector<Entity> getProviderByID(int ID)  {
        return getEntityByID("Providers", ID);
    }

    public Vector<Entity> getProvidersByName(String name)  {
        return getEntityByString("Providers", "Name", name);
    }

    public Vector<Entity> getProvidersByAddress(String address)
    {
        return getEntityByString("Providers", "Address", address);
    }

    public Vector<Entity> getProvidersByCity(String city)  {
        return getEntityByString("Providers", "City", city);
    }

    public Vector<Entity> getProvidersByState(String state)  {
        return getEntityByString("Providers", "State", state);
    }

    public Vector<Entity> getProvidersByZipcode(String zipcode)
    {
        return getEntityByString("Providers", "Zipcode", zipcode);
    }

    // Since Services and Transactions are not Entity objects, we have to
    // manually define them. Unfortunately.

    /*---Returns a vector of service objects by ID---*/
    public Vector<Service> getServiceByID(int ID)  {
        Statement stmt = null;
        ResultSet rs = null;
        Vector<Service> returnVec = new Vector<Service>();

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM " + "Services" + " WHERE " + "ServiceID = " + Integer.toString(ID));

            if(rs.next()) {
                returnVec.add( new Service(
                            rs.getInt("ServiceID"),
                            rs.getString("Name"),
                            rs.getFloat("Fee"),
                            rs.getInt("Status")
                            )
                        );
            }
            stmt.close();
            rs.close();
        }
        catch(SQLException | InputException e) {
            System.err.println("Error occured in the database while retrieving services.");
        }

        return returnVec;
    }

    /*---Returns a vector of service objects by name---*/
    public Vector<Service> getServicesByName(String name)  {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Vector<Service> returnVec = new Vector<Service>();

        try {
            stmt = conn.prepareStatement("SELECT * FROM Services WHERE Name = ?");
            stmt.setString(1, name);
            rs = stmt.executeQuery();

            while(rs.next()) {
                returnVec.add( new Service(
                            rs.getInt("ServiceID"),
                            rs.getString("Name"),
                            rs.getFloat("Fee"),
                            rs.getInt("Status")
                            )
                        );    
            }
            stmt.close();
            rs.close();
        }
        catch(SQLException | InputException e) {
            System.err.println("Error occured in the database while retrieving services.");
        }

        return returnVec;
    }

    /*---Returns a vector of service objects by price---*/
    public Vector<Service> getServiceByPrice(float price)  {
        Statement stmt = null;
        ResultSet rs = null;
        Vector<Service> returnVec = new Vector<Service>();

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM " + "Services" + " WHERE " + "Fee = " + Float.toString(price));

            while(rs.next()) {
                returnVec.add( new Service(
                            rs.getInt("ServiceID"),
                            rs.getString("Name"),
                            rs.getFloat("Fee"),
                            rs.getInt("Status")
                            )
                        );
            }
            stmt.close();
            rs.close();
        }
        catch(SQLException | InputException e) {
            System.err.println("Error occured in the database while retrieving services.");
        }

        return returnVec;
    }

    // Since so many Transaction columns are ints, we can create a private
    // function that specifies the column that it will search by.

    /*---Returns a vector of transaction objects by some column and integer---*/
    private Vector<Transaction> getTransactionByInt(String column, int criteria) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Vector<Transaction> returnVec = new Vector<Transaction>();

        try {
            stmt = conn.prepareStatement("SELECT * FROM " + "Transactions" + " WHERE " + column + " = ?");
            stmt.setInt(1, criteria);
            rs = stmt.executeQuery();

            while(rs.next()) {
                returnVec.add( new Transaction(
                            rs.getInt("TransactionID"),
                            rs.getInt("PatientID"),
                            rs.getInt("ProviderID"),
                            rs.getInt("ServiceID"),
                            rs.getInt("ConsultID"),
                            rs.getString("DateTime"),
                            toOutputDate(rs.getString("ServiceDate")),
                            rs.getString("Comment")
                            )
                        );
            }
            stmt.close();
            rs.close();
        }
        catch(SQLException | InputException e) {
            System.err.println("Error occured in the database while retrieving transactions.");
        }

        return returnVec;
    }

    // Same thing with DateTime and ServiceDate.
    /*---Returns a vector of transaction by column and string(like name)---*/
    private Vector<Transaction> getTransactionByString(String column, String criteria)  {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Vector<Transaction> returnVec = new Vector<Transaction>();

        try {
            stmt = conn.prepareStatement("SELECT * FROM " + "Transactions" + " WHERE " + column + " = ?");
            stmt.setString(1, criteria);
            rs = stmt.executeQuery();

            while(rs.next()) {
                returnVec.add( new Transaction(
                            rs.getInt("TransactionID"),
                            rs.getInt("PatientID"),
                            rs.getInt("ProviderID"),
                            rs.getInt("ServiceID"),
                            rs.getInt("ConsultID"),
                            rs.getString("DateTime"),
                            toOutputDate(rs.getString("ServiceDate")),
                            rs.getString("Comment")
                            )
                        );
            }
            stmt.close();
            rs.close();
        }
        catch(SQLException | InputException e) {
            System.err.println("Error occured in the database while retrieving transactions.");
        }

        return returnVec;
    }

    public Vector<Transaction> getTransactionByID(int ID) {
        return getTransactionByInt("TransactionID", ID);
    }

    public Vector<Transaction> getTransactionsByDateTime(String dateTime) 
    {
        return getTransactionByString("DateTime", dateTime);
    }

    // Note that the query transforms the specified date, which is in
    // MM-DD-YYYY, into YYYY-MM-DD.
    public Vector<Transaction> getTransactionsByServiceDate(String serviceDate) 
    {
        System.out.println("ServiceDate: " + toSQLDate(serviceDate));
        return getTransactionByString("ServiceDate", toSQLDate(serviceDate));
    }

    public Vector<Transaction> getTransactionsByProviderID(int ID) {
        return getTransactionByInt("ProviderID", ID);
    }

    public Vector<Transaction> getTransactionsByPatientID(int ID) {
        return getTransactionByInt("PatientID", ID);
    }

    public Vector<Transaction> getTransactionsByServiceID(int ID) {
        return getTransactionByInt("ServiceID", ID);
    }

    public Vector<Transaction> getTransactionsByConsultID(int ID) {
        return getTransactionByInt("ConsultID", ID);
    }

    // Returns a full table of active patients or providers, meaning those with
    // an Status column equal to 1.

    /*---Returns a vector of objects of all active patients and providers---*/
    private Vector<Entity> getAllActiveEntities(String table)  {
        Statement stmt = null;
        ResultSet rs = null;
        String IDColumn = table.substring(0, table.length() - 1) + "ID";
        Vector<Entity> returnVec = new Vector<Entity>();

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM " + table + " WHERE Status = 1");

            while(rs.next()) {
                if(IDColumn.matches("PatientID")) {
                    returnVec.add( new Patient(
                                rs.getInt(IDColumn),
                                rs.getString("Name"),
                                rs.getString("Address"),
                                rs.getString("City"),
                                rs.getString("State"),
                                rs.getString("Zipcode"),
                                rs.getInt("Status"),
                                rs.getInt("FinancialStanding")
                                )
                            );
                } else if(IDColumn.matches("ProviderID")) {
                    returnVec.add( new Provider(
                                rs.getInt(IDColumn),
                                rs.getString("Name"),
                                rs.getString("Address"),
                                rs.getString("City"),
                                rs.getString("State"),
                                rs.getString("Zipcode"),
                                rs.getBoolean("Status")
                                )
                            );
                }
            }
            stmt.close();
            rs.close();
        }
        catch(SQLException | InputException e) {
            System.err.println("Error occured in the database while retrieving patients/providers.");
        }

        return returnVec;
    }

    public Vector<Entity> getAllActivePatients()  {
        return getAllActiveEntities("Patients");
    }

    public Vector<Entity> getAllActiveProviders()  {
        return getAllActiveEntities("Providers");
    }

    /*---Returns a vector of objects of all active services---*/
    public Vector<Service> getAllActiveServices()  {
        Statement stmt = null;
        ResultSet rs = null;
        Vector<Service> returnVec = new Vector<Service>();

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM Services WHERE Status = 1");

            while(rs.next()) {
                returnVec.add( new Service(
                            rs.getInt("ServiceID"),
                            rs.getString("Name"),
                            rs.getFloat("Fee"),
                            rs.getInt("Status")
                            )
                        );
            }
            stmt.close();
            rs.close();
        }
        catch(SQLException | InputException e) {
            System.err.println("Error occured in the database while retrieving patients/providers.");
        }

        return returnVec;
    }

    // Gets all transactions by either PatientID or ProviderID within one week
    // of the specified date.

    /*---Return a vector of transaction objects for a week---*/
    private Vector<Transaction> getWeekTransactions(String column,int ID, String date) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        SimpleDateFormat inputFormat = new SimpleDateFormat("MM-dd-yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        Vector<Transaction> returnVec = new Vector<Transaction>();

        try {
            Date currentDate = inputFormat.parse(date);
            Date pastDate = new Date();
            // pastDate = 7 days before currentDate.
            pastDate.setTime(currentDate.getTime() - (long)7 * 1000 * 60 * 60 * 24);

            stmt = conn.prepareStatement("SELECT * FROM " + "Transactions" + " WHERE " + column + 
                    " = ? AND " + "ServiceDate >= ? AND ServiceDate <= ?");

            stmt.setInt(1, ID);
            stmt.setString(2, outputFormat.format(pastDate));
            stmt.setString(3, outputFormat.format(currentDate));
            rs = stmt.executeQuery();

            while (rs.next()) {
                returnVec.add( new Transaction(
                            rs.getInt("TransactionID"), 
                            rs.getInt("PatientID"),
                            rs.getInt("ProviderID"),
                            rs.getInt("ServiceID"),
                            rs.getInt("ConsultID"),
                            rs.getString("DateTime"),
                            toOutputDate(rs.getString("ServiceDate")),
                            rs.getString("Comment")
                            )
                        );
            }
            stmt.close();
            rs.close();
        }

        catch(SQLException | InputException | ParseException e) {
            System.err.println("Error occured in the database while retrieving weekly transactions.");
        }
        return returnVec;
    }

    // Calls the private function defined above with the specified column.
    public Vector<Transaction> getWeekTransactionsByPatient(int ID, String date){
        return getWeekTransactions("PatientID", ID, date);
    }

    public Vector<Transaction> getWeekTransactionsByProvider(int ID, String date) {
        return getWeekTransactions("ProviderID", ID, date);
    }

    /*---Converts MM-DD-YYYY to YYYY-MM-DD--*/
    private String toSQLDate(String outputDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("MM-dd-yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date tryDate = inputFormat.parse(outputDate);
            return outputFormat.format(tryDate);
        }

        catch (ParseException e) {
            return "";
        }
    }      

    /*---Converts YYYY-MM-DD to MM-DD-YYYY---*/
    private String toOutputDate(String SQLDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("MM-dd-yyyy");

        try {
            Date tryDate = inputFormat.parse(SQLDate);
            return outputFormat.format(tryDate);
        }

        catch(ParseException e) {
            return "";
        }
    }
}
