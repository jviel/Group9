import com.psu.group9.*;


import static org.junit.Assert.*;

import org.junit.Test;
import java.util.Date;
import java.text.SimpleDateFormat;

public class TestTransaction {

    // ---- Test constructors ----
    // test constructor that makes new objects
    @Test
    public void testTransactionNew() {
        try {
            new Transaction(123456789, 234567891, 345678912, 456789, 567891234, "09-08-1876 11:11:11", "12-10-2345",
                "The service was great!");
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    // test constructor that makes objects for DB Wrapper,
    // validates all variables
    @Test
    public void testTransaction() {
        try {
            new Transaction(234567891, 345678912, 456789, 567891234, "12-10-2345",
                "The service was great!");
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPatientIDNegative() {
        boolean thrown = false;
        try {
            new Transaction(-1, 345678912, 456789, 567891234, "12-10-2345",
                            "The service was great!");
        } catch (InputException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testProviderIDNegative() {
        boolean thrown = false;
        try {
            new Transaction(234567891, -1, 456789, 567891234, "12-10-2345",
                            "The service was great!");
        } catch (InputException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testServiceIDNegative() {
        boolean thrown = false;
        try {
          new Transaction(345345345, 345678912, -1, 567891234, "12-10-2345",
                "The service was great!");
        } catch (InputException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testServiceIDTooBig() {
        boolean thrown = false;
        try {
          new Transaction(345345345, 345678912, 453678559, 567891234, "12-10-2345",
                "The service was great!");
        } catch (InputException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testConsultationNumberNegative() {
        boolean thrown = false;
        try {
            new Transaction(234567891, 345678912, 456789, -1, "12-10-2345",
                            "The service was great!");
        } catch (InputException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }
    // tests constructor and isValidShortDate
    @Test
    public void testBadServiceDate() {
        boolean thrown = false;
        try {
            new Transaction(345345345, 345678912, 453678559, 567891234, "11-12-19455",
                            "The service was great!");
        } catch (InputException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    // tests constructor and isValidLongDate
    @Test
    public void testBadDateTime() {
        boolean thrown = false;
        try {
            new Transaction(123456789, 234567891, 345678912, 456789, 567891234, "09-08-1876 11:11:1111", "12-10-2345",
                            "The service was great!");
        } catch (InputException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testCommentsTooLong() {
        boolean thrown = false;
        try {
          new Transaction(345345345, 345678912, 453679, 567891234, "12-10-2345",
                          "Oh my god, the chocolate was so good last night... mmmm cocoa.... Oh I am so happy with cocoa in my life.");
        } catch (InputException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }


    // ---- Test setters and getters ----
    // all setters tested by their use in constructors
    @Test
    public void testIDNumber() {
        try {
          Transaction transaction = new Transaction(123456789, 234567891, 345678912, 456789, 567891234, "09-08-1876 11:11:11", "12-10-2345",
                "The service was great!");
            int result = transaction.getIDNumber();
            assertEquals(123456789, result);
        } catch (InputException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPatientID() {
        try {
          Transaction transaction = new Transaction(123456789, 234567891, 345678912, 456789, 567891234, "09-08-1876 11:11:11", "12-10-2345",
                "The service was great!");
            int result = transaction.getPatientID();
            assertEquals(234567891, result);
        } catch (InputException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testProviderID() {
        try {
          Transaction transaction = new Transaction(123456789, 234567891, 345678912, 456789, 567891234, "09-08-1876 11:11:11", "12-10-2345",
                "The service was great!");
            int result = transaction.getProviderID();
            assertEquals(345678912, result);
        } catch (InputException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testServiceID() {
        try {
          Transaction transaction = new Transaction(123456789, 234567891, 345678912, 456789, 567891234, "09-08-1876 11:11:11", "12-10-2345",
                "The service was great!");
            int result = transaction.getServiceID();
            assertEquals(456789, result);
        } catch (InputException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testConsultationNumber() {
        try {
          Transaction transaction = new Transaction(123456789, 234567891, 345678912, 456789, 567891234, "09-08-1876 11:11:11", "12-10-2345",
                "The service was great!");
            int result = transaction.getConsultationNumber();
            assertEquals(567891234, result);
        } catch (InputException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testDateTime() {
        try {
          Transaction transaction = new Transaction(123456789, 234567891, 345678912, 456789, 567891234, "09-08-1876 11:11:11", "12-10-2345",
                "The service was great!");
            String result = transaction.getDateTime();
            assertEquals("09-08-1876 11:11:11", result);
        } catch (InputException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testServiceDate() {
        try {
          Transaction transaction = new Transaction(123456789, 234567891, 345678912, 456789, 567891234, "09-08-1876 11:11:11", "12-10-2345",
                "The service was great!");
            String result = transaction.getServiceDate();
            assertEquals("12-10-2345", result);
        } catch (InputException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testComments() {
        try {
          Transaction transaction = new Transaction(123456789, 234567891, 345678912, 456789, 567891234, "09-08-1876 11:11:11", "12-10-2345",
                "The service was great!");
            String result = transaction.getComments();
            assertEquals("The service was great!", result);
        } catch (InputException e) {
            fail(e.getMessage());
        }
    }


    // ---- Test Private helper functions ----
    @Test
    public void testGetTodayDateLong() {
        try {
          Transaction transaction = new Transaction(234567891, 345678912, 456789, 567891234, "12-10-2345",
                "The service was great!");
            Date now = new Date();
            SimpleDateFormat ft = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
          String expectedResult = ft.format(now);
          String actualResult = transaction.getDateTime();
          assertEquals(expectedResult, actualResult);
        } catch (InputException e) {
            fail(e.getMessage());
        }
    }


    // ---- Test overrides ----
    @Test
    public void testToString() {
        try {
          Transaction transaction = new Transaction(123456789, 234567891, 345678912, 456789, 567891234, "09-08-1876 11:11:11", "12-10-2345",
                "The service was great!");
            String expectedResult = "Transaction ID: 123456789\n"
                + "Date-Time: 09-08-1876 11:11:11\n"
                + "Service Date: 12-10-2345\n"
                + "Provider ID: 345678912\n"
                + "Patient ID: 234567891\n"
                + "Service ID: 456789\n"
                + "Consultation Number: 567891234\n"
                + "Comments: The service was great!\n";
            String actualResult = transaction.toString();
            assertEquals(expectedResult, actualResult);
        } catch (InputException e) {
            fail(e.getMessage());
        }
    }
}
