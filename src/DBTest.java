import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

import java.util.Vector;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DBTest {
    Database db = new Database("database.db");
    Connection conn = null;	
    
	
    int patientID = 100000001;	//When tested with a clean DB. Will refer to Test name in addPatientTest
    int serviceID = 100000;		//When tested with a clean DB. Will refer to Test service in addServiceTest
    int dummyID = 1;
    
    @BeforeClass
        public static void deleteDatabase() {
            Path dbPath = Paths.get("database.db");
    	    try {
    	    	Files.deleteIfExists(dbPath);
    	    }
    	    catch(Exception e) {
    	    	e.printStackTrace();
    	    }
    }
    
    // Patient Unit Tests

    @Test
        public void A001addPatientTest() {
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

    // We add a duplicate here.
    @Test
        public void A002addPatientTest2() {
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
    
    // We add a *close* match, meaning one that has the same name and address but different city.
    
    @Test
        public void A003addPatientTest3() {
    	int ID;
    	try {
    		Patient newPatient = new Patient("Test name", "4500 NE Sandy Blvd.", "Hillsboro", "OR", "97123", 1, 1);
    		ID = db.addPatient(newPatient);
    		assertTrue(ID > 99999999);
    	}
    	catch (InputException e) {
    		e.printStackTrace();
    	}
    }
    
    // We update the first patient, setting it equal to "Test name", "4501 NE Sandy Blvd.", "Hillsboro", "OR", "97123", 1, 1.
    @Test
        public void A004updatePatientTest() {
    	Vector<Entity> patientVec = db.getPatientsByName("Test name");
    	if(!(patientVec.isEmpty())) {
    		int ID = patientVec.get(0).getID();
    		
    		try {
    		    Patient updatePatient = new Patient("Test name2", "4500 NE Sandy Blvd.", "Hillsboro", "OR", "97123", 1, 1);
    		    Boolean updated = db.updatePatient(ID, updatePatient);
    		    assertTrue(updated);
    		}
    		catch(InputException e) {
    			fail("InputException thrown.");
    			e.printStackTrace();
    		}
    	}
    	
    	else {
    		fail("Patient not found.");
    	}
    	
    }
    
    // We update that same patient, setting it equal to "Test name", "4500 NE Sandy Blvd.", "Hillsboro", "OR", "97123", 1, 1).
    // Note that this is an exact duplicate of the patient added in A003addPatientTest3, so the database should refuse it.
    
    @Test
    public void A005updatePatientTest2() {
    	Vector<Entity> patientVec = db.getPatientsByName("Test name2");
    	if(!(patientVec.isEmpty())) {
    		int ID = patientVec.get(0).getID();
    		
    		try {
    		    Patient updatePatient = new Patient("Test name", "4500 NE Sandy Blvd.", "Hillsboro", "OR", "97123", 1, 1);
    		    Boolean updated = db.updatePatient(ID, updatePatient);
    		    assertFalse(updated);
    		}
    		catch(InputException e) {
    			fail("InputException thrown.");
    			e.printStackTrace();
    		}
    	}
    	
    	else {
    		fail("Patient not found.");
    	}
    }
    
    // We set that patient to Inactive.
    @Test
        public void A006removePatientTest(){
    	    Vector<Entity> patientVector = db.getPatientsByName("Test name");
    	    int ID = patientVector.get(0).getID();
    	    
            Boolean removed = db.removePatient(ID);
            assertTrue(removed);
        }
    
    // We get the patient that was removed and verify that it was actually set to Inactive.
    @Test
        public void A007removePatientVerify() {
    	    Vector<Entity> patientVector = db.getPatientsByName("Test name");
    	    assertFalse(patientVector.get(0).getEnrollmentStatus());
    }
    
    // We attempt to remove an ID that is not in the database.
    @Test
        public void A008removePatientTest2() {
    	    Boolean status = db.removePatient(891279199);
    	    assertFalse(status);
    }
    
    // Provider Unit Tests
    

    @Test
        public void B001addProviderTest() {
            int ID;	
            try {
            	Provider newProvider = new Provider("Test Provider", "24 Dartmouth Dr", "Framingham", "MA", "01701", 1);
                ID = db.addProvider(newProvider);
                assertTrue(ID > 99999999);

            } catch (InputException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }		
        }

    // We add a duplicate here.
    @Test
        public void B002addProviderTest2() {
            int ID;	
            try {
                Provider newProvider = new Provider("Test Provider", "24 Dartmouth Dr", "Framingham", "MA", "01701", 1);
                ID = db.addProvider(newProvider);
                assertTrue(ID < 0);

            } catch (InputException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }		
        }
    
    // We add a *close* match, meaning one that has the same name and address but different city.
    
    @Test
        public void B003addProviderTest3() {
    	int ID;
    	try {
    		Provider newProvider = new Provider("Test Provider", "24 Dartmouth Dr", "Southborough", "MA", "01772", 1);
    		ID = db.addProvider(newProvider);
    		assertTrue(ID > 99999999);
    	}
    	catch (InputException e) {
    		e.printStackTrace();
    	}
    }
    
    // We update the first provider, setting it equal to "Test Provider2", "25 Dartmouth Dr", "Framingham", "MA", "01701", 1.
    @Test
        public void B004updateProviderTest() {
    	Vector<Entity> providerVec = db.getProvidersByName("Test Provider");
    	if(!(providerVec.isEmpty())) {
    		int ID = providerVec.get(0).getID();
    		
    		try {
    		    Provider updateProvider = new Provider("Test Provider2", "25 Dartmouth Dr", "Framingham", "MA", "01701", 1);
    		    Boolean updated = db.updateProvider(ID, updateProvider);
    		    assertTrue(updated);
    		}
    		catch(InputException e) {
    			fail("InputException thrown.");
    			e.printStackTrace();
    		}
    	}
    	
    	else {
    		fail("Patient not found.");
    	}
    	
    }
    
    // We update that same provider, setting it equal to "Test Provider", "24 Dartmouth Dr", "Southborough", "MA", "01772", 1.
    // Note that this is an exact duplicate of the Provider added in B003addProviderTest3, so the database should refuse it.
    
    @Test
    public void B005updateProviderTest2() {
    	Vector<Entity> ProviderVec = db.getProvidersByName("Test Provider2");
    	if(!(ProviderVec.isEmpty())) {
    		int ID = ProviderVec.get(0).getID();
    		
    		try {
    		    Provider updateProvider = new Provider("Test Provider", "24 Dartmouth Dr", "Southborough", "MA", "01772", 1);
    		    Boolean updated = db.updateProvider(ID, updateProvider);
    		    assertFalse(updated);
    		}
    		catch(InputException e) {
    			fail("InputException thrown.");
    			e.printStackTrace();
    		}
    	}
    	
    	else {
    		fail("Provider not found.");
    	}
    }
    
    // We set that Provider to Inactive.
    @Test
        public void B006removeProviderTest(){
    	    Vector<Entity> ProviderVector = db.getProvidersByName("Test Provider");
    	    int ID = ProviderVector.get(0).getID();
    	    
            Boolean removed = db.removeProvider(ID);
            assertTrue(removed);
        }
    
    // We get the Provider that was removed and verify that it was actually set to Inactive.
    @Test
        public void B007removeProviderVerify() {
    	    Vector<Entity> ProviderVector = db.getProvidersByName("Test Provider");
    	    assertFalse(ProviderVector.get(0).getEnrollmentStatus());
    }
    
    // We attempt to remove an ID that is not in the database.
    @Test
        public void B008removeProviderTest2() {
    	    Boolean status = db.removeProvider(891279199);
    	    assertFalse(status);
    }
    
    
    
    
    
    
    // Service Unit Tests
    
    // We add a Service to the DB.
    @Test
        public void A222addServiceTest(){
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
    

    // I don't think that we need this test, but I'm keeping it just in case. We'll comment it out in the meantime.
    /*
    @Test 
        public void A005removeRow(){
            try {
            	Class.forName("org.sqlite.JDBC");
            	conn = DriverManager.getConnection("jdbc:sqlite:database.db");
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM Patients WHERE PatientID=?");
                stmt.setInt(1, patientID);
                stmt.executeUpdate();

                stmt = conn.prepareStatement("DELETE FROM Services WHERE ServiceID=?");
                stmt.setInt(1, serviceID);
                stmt.executeUpdate();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    */
}
