import static org.junit.Assert.*;

import org.junit.Test;

public class TestService {

  // Test constructor that makes new objects
  @Test
  public void testServiceNew() {
    try {
      new Service("Massage", 60.85);
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  // Test constructor that makes objects for DB Wrapper,
  // validates all variables
  @Test
  public void testService() {
    try {
      new Service(1, "Massage", 60.85, 0);
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testServiceActive() {
    Service service = new Service(1, "Massage", 60.85, 1);
    boolean result = service.getStatus();
    assertEquals(true, result);
  }

  @Test
  public void testServiceInactive() {
    Service service = new Service(1, "Massage", 60.85, 0);
    boolean result = service.getStatus();
    assertEquals(false, result);
  }

  @Test
  public void testServiceCodeNegative() {
    Service service = new Service(-1, "Massage", 60.85, 0);

  }



  @Test
  public void testName() {
    Service service = new Service("Massage", 0.0);
    String result = service.getName();
    assertEquals("Massage", result);
  }

  @Test
  public void testCode() {
    Service service = new Service("", 0.0);
    int result = service.getCode();
    assertEquals(0, result);
  }

  @Test
  public void testFee() {
    Service service = new Service("", 60.85);
    Double expectedResult = 60.85;
    Double actualResult = service.getFee();
    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testStatus() {
    Service service = new Service("", 60.85);
    Boolean result = service.getStatus();
    assertEquals(true, result);
  }

  @Test
  public void testToString() {
    Service service = new Service("Massage", 60.85);
    String expectedResult = "0\tMassage\t$60.85";
    String actualResult = service.toString();
    assertEquals(expectedResult, actualResult);
  }
}
