import static org.junit.Assert.*;
import org.junit.Test;

public class DBTest {
	Database db = new Database("database.db");
	int patientID;
	
	//@Test
	/*public void addTest() {
		try {
			Patient newPatient = new Patient("Johny Doe", "4500 NE Sandy Blvd.", "Portland", "OR", "97233", 1, 1);
			patientID = db.addPatient(newPatient);
			assertTrue(patientID > 99999999);

		} catch (InputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}*/
	
	@Test
	public void removeTest(){
		Boolean removed = db.removePatient(100000000);
		System.out.print("RR" + removed);
		assertTrue(removed);
	}
}
