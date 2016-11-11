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
			
		} catch (ClassNotFoundException | SQLException e) {
			System.err.println("Connection with the database failed.");
			return;
		}
		
		createPatientTable();
		
	}
	
	private void createPatientTable(){
		
	}
	
}