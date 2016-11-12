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
		Service service = new Service("Massage", 1, 60.85);
		service.setName("Massage");
		String result = service.getName();
		assertEquals("Massage", result);
	}

}
