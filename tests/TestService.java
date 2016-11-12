import static org.junit.Assert.*;

import org.junit.Test;

public class TestService {
	
	@Test
	public void testService() {
		try {
			new Service("Massage", 1, 60.85);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testName() {
		Service service = new Service("Massage", 0, 0);
		String result = service.getName();
		assertEquals("Massage", result);
	}
	
	@Test
	public void testCode() {
		Service service = new Service("", 1, 0);
		int result = service.getCode();
		assertEquals(1, result);
	}
	
	@Test
	public void testFee() {
		Service service = new Service("", 0, 60.85);
		Double expectedResult = 60.85;
		Double actualResult = service.getFee();
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testStatus() {
		Service service = new Service("", 0, 60.85);
		boolean result = service.getStatus();
		assertEquals(true, result);
	}

	@Test 
	public void testToString() {
		Service service = new Service("Massage", 1, 60.85);
		String expectedResult = "1\tMassage\t$60.85";
		String actualResult = service.toString();
		assertEquals(expectedResult, actualResult);
	}
}
