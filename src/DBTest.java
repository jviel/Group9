import static org.junit.Assert.*;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.sql.*;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DBTest {
    Database db = new Database("database.db");
    Connection conn = null;	
    int patientID = 100000001;	//When tested with a clean DB. Will refer to Test name in addPatientTest
    int serviceID = 100000;		//When tested with a clean DB. Will refer to Test service in addServiceTest
    int dummyID = 1;

    public DBTest(){
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:database.db");
        } catch (ClassNotFoundException | SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }	 
    }


    @Test
        public void addPatientTest() {
            int ID;	
            try {
                Patient newPatient = new Patient("Test name", "4500 NE Sandy Blvd.", "Portland", "OR", "97233", 1, 1);
                ID = db.addPatient(newPatient);
                assertTrue(ID > 99999999);

            } catch (InputException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }		
        }

    @Test
        public void addPatientTest2() {
            int ID;	
            try {
                Patient newPatient = new Patient("Test name", "4500 NE Sandy Blvd.", "Portland", "OR", "97233", 1, 1);
                ID = db.addPatient(newPatient);
                assertTrue(ID < 0);

            } catch (InputException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }		
        }
    //This test will always be performed on a dummy data in the db
    //which never gets removed from the db
    @Test
        public void removePatientTest(){
            Boolean removed = db.removePatient(dummyID);;
            assertTrue(removed);
        }

    @Test
        public void addServiceTest(){
            int ID;	
            try {
                Service newService = new Service("Test Service", 60.5F);
                ID = db.addService(newService);
                assertTrue(ID > 99999);

            } catch (InputException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }		
        }

    //This test will always pass. It deletes all previously created rows
    @Test 
        public void removeRow(){
            try {
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM Patients WHERE PatientID=?");
                stmt.setInt(1, patientID);
                stmt.executeUpdate();

                stmt = conn.prepareStatement("DELETE FROM Services WHERE ServiceID=?");
                stmt.setInt(1, serviceID);
                stmt.executeUpdate();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
}
