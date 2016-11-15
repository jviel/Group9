import static org.junit.Assert.*;

import org.junit.Test;
import src.InputException;
import src.Service;
import src.Transaction;

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



    // ---- Test setters and getters ----
    // all setters tested by their use in constructors



    // ---- Test Private helper functions ----



    // ---- Test overrides and helpers ----
    // test toString override
}
