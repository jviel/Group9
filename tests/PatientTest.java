import static org.junit.Assert.assertEquals;

public class PatientTest {
    @org.junit.Test
    public void testFinancialStanding() throws Exception {
        Patient TestPatient = new Patient(123456789, "Donald Trump", "Trump Tower", "NYC", "NY", "12345", true, true);

        TestPatient.setFinancialStanding(false);

        assertEquals("Financial Standing was set incorrectly", false, TestPatient.getFinancialStanding());
    }
}
