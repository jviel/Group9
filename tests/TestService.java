import com.psu.group9.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class TestService {

    // test constructor that makes new objects
    @Test
    public void testServiceNew() {
        try {
            new Service("Massage", 60.85f);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    // test constructor that makes objects for DB Wrapper,
    // validates all variables
    @Test
    public void testService() {
        try {
            new Service(1, "Massage", 60.85f, 0);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testServiceActive() {
      try {
          Service service = new Service(1, "Massage", 60.85f, 1);
          boolean result = service.getStatus();
          assertEquals(true, result);
      } catch (InputException e) {
          fail(e.getMessage());
      }
    }

    @Test
    public void testServiceInactive() {
      try {
          Service service = new Service(1, "Massage", 60.85f, 0);
          boolean result = service.getStatus();
          assertEquals(false, result);
      } catch (InputException e) {
          fail(e.getMessage());
      }
    }

    @Test
    public void testServiceCodeNegative() {
        boolean thrown = false;
        try {
            Service service = new Service(-1, "Massage", 60.85f, 0);
        } catch (InputException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testServiceCodeTooLarge() {
        boolean thrown = false;
        try {
            Service service = new Service(1000000, "Massage", 60.85f, 0);
        } catch (InputException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testServiceNameTooLong() {
        boolean thrown = false;
        try {
            Service service = new Service(1, "TobleroneTobleroneToblerone", 60.85f, 0);
        } catch (InputException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testServiceFeeNegative() {
        boolean thrown = false;
        try {
            Service service = new Service(1, "Massage", -1.0f, 0);
        } catch (InputException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testServiceFeeTooLarge() {
        boolean thrown = false;
        try {
            Service service = new Service(1, "Massage", 10000.0f, 0);
        } catch (InputException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    // ---- Test setters and getters ----
    // all setters tested by their use in constructors
    @Test
    public void testName() {
        try {
            Service service = new Service("Massage", 0.0f);
            String result = service.getName();
            assertEquals("Massage", result);
        } catch (InputException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testCode() {
        try {
            Service service = new Service(0, "Robert", 0.0f, 1);
            int result = service.getCode();
            assertEquals(0, result);
        } catch (InputException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testFee() {
        try {
            Service service = new Service("Bob", 60.85f);
            float expectedResult = 60.85f;
            float actualResult = service.getFee();
            assertEquals(expectedResult, actualResult, 0.00000001);
        } catch (InputException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testStatus() {
        try {
            Service service = new Service(0, "James", 60.85f, 1);
            Boolean result = service.getStatus();
            assertEquals(true, result);
        } catch (InputException e) {
            fail(e.getMessage());
        }
    }

    // ---- Test overrides and helpers ----
    // tests toString override
    @Test
    public void testToStringActive() {
        try {
            Service service = new Service(100000, "Massage", 60.85f, 1);
            String expectedResult = "100000    Massage                 $60.85      Active";
            String actualResult = service.toString();
            assertEquals(expectedResult, actualResult);
        } catch (InputException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testToStringInactive() {
        try {
            Service service = new Service(100000, "Massage", 60.85f, 0);
            String expectedResult = "100000    Massage                 $60.85      Inactive";
            String actualResult = service.toString();
            assertEquals(expectedResult, actualResult);
        } catch (InputException e) {
            fail(e.getMessage());
        }
    }

    // tests equals and compareStrings methods
    // more tests could be added to check all equals conditionals
    @Test
    public void testEquals() {
        boolean equal = false;
        try {
            Service counseling = new Service(1, "Nutritionist", 60.85f, 1);
            Service sameCounseling = new Service(1, "Nutritionist", 60.85f, 1);
            equal = counseling.equals(sameCounseling);
        } catch (InputException e) {
            fail(e.getMessage());
        }
        assertTrue(equal);
    }

    @Test
    public void testEqualsFails() {
        boolean equal = false;
        try {
            Service counseling = new Service(1, "Massage", 60.85f, 1);
            Service massage = new Service(1, "Nutritionist", 60.85f, 1);
            equal = counseling.equals(massage);
        } catch (InputException e) {
            fail(e.getMessage());
        }
        assertFalse(equal);
    }
}
