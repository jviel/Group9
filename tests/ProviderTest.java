
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ProviderTest {

    // Testing for getters
    @org.junit.Test
    public void getIdNumber() throws Exception {
        final Provider TestProvider = new Provider(123456789, "Donald Trump", "Trump Tower", "NYC", "NY", "12345", true);

        final int result = TestProvider.getIdNumber();

        assertEquals("idNumber wasn't retrieved properly", result, 123456789);
    }

    @org.junit.Test
    public void getName() throws Exception {
        final Provider TestProvider = new Provider(123456789, "Donald Trump", "Trump Tower", "NYC", "NY", "12345", true);

        final String result = TestProvider.getName();

        assertEquals("Name wasn't retrieved properly", result, "Donald Trump");
    }

    @org.junit.Test
    public void getAddress() throws Exception {
        final Provider TestProvider = new Provider(123456789, "Donald Trump", "Trump Tower", "NYC", "NY", "12345", true);

        final String result = TestProvider.getAddress();

        assertEquals("Address wasn't retrieved properly", result, "Trump Tower");
    }

    @org.junit.Test
    public void getCity() throws Exception {
        final Provider TestProvider = new Provider(123456789, "Donald Trump", "Trump Tower", "NYC", "NY", "12345", true);

        final String result = TestProvider.getCity();

        assertEquals("City wasn't retrieved properly", result, "NYC");
    }

    @org.junit.Test
    public void getState() throws Exception {
        final Provider TestProvider = new Provider(123456789, "Donald Trump", "Trump Tower", "NYC", "NY", "12345", true);

        final String result = TestProvider.getState();

        assertEquals("State wasn't retrieved properly", result, "NY");
    }

    @org.junit.Test
    public void getZip() throws Exception {
        final Provider TestProvider = new Provider(123456789, "Donald Trump", "Trump Tower", "NYC", "NY", "12345", true);

        final String result = TestProvider.getZip();

        assertEquals("Zipcode wasn't retrieved properly", result, "12345");
    }

    @org.junit.Test
    public void getStatus() throws Exception {
        final Provider TestProvider = new Provider(123456789, "Donald Trump", "Trump Tower", "NYC", "NY", "12345", true);

        final boolean result = TestProvider.getStatus();

        assertEquals("State wasn't retrieved properly", result, true);
    }

    // Testing for setters
    @org.junit.Test
    public void setIdNumberIdeal() throws Exception {
        final Provider TestProvider = new Provider(123456789, "Donald Trump", "Trump Tower", "NYC", "NY", "12345", true);

        TestProvider.setIdNumber(987654321);

        final int result = TestProvider.getIdNumber();

        assertEquals("idNumber wasn't set properly", result, 987654321);
    }

    @org.junit.Test
    public void setIdNumberZero() throws Exception {
        final Provider TestProvider = new Provider(123456789, "Donald Trump", "Trump Tower", "NYC", "NY", "12345", true);

        TestProvider.setIdNumber(0);

        final int result = TestProvider.getIdNumber();

        assertEquals("idNumber wasn't set properly", result, 0);
    }

    @org.junit.Test
    public void setIdNumberTooLong() throws Exception {
        final Provider TestProvider = new Provider(123456789, "Donald Trump", "Trump Tower", "NYC", "NY", "12345", true);

        TestProvider.setIdNumber(1234567891);

        final int result = TestProvider.getIdNumber();

        assertNotEquals("idNumber wasn't set properly", result, 1234567891);
    }

    @org.junit.Test
    public void setIdNumberNegative() throws Exception {
        final Provider TestProvider = new Provider(123456789, "Donald Trump", "Trump Tower", "NYC", "NY", "12345", true);

        TestProvider.setIdNumber(-123456789);

        final int result = TestProvider.getIdNumber();

        assertNotEquals("idNumber wasn't set properly", result, -123456789);
    }

    @org.junit.Test
    public void setName() throws Exception {
        final Provider TestProvider = new Provider(123456789, "Donald Trump", "Trump Tower", "NYC", "NY", "12345", true);

        String testName = "Donald J Trump";

        TestProvider.setName(testName);

        assertEquals("Name was not set.", testName, TestProvider.getName());
    }

    @org.junit.Test
    public void setNameTooLong() throws Exception {
        final Provider TestProvider = new Provider(123456789, "Donald Trump", "Trump Tower", "NYC", "NY", "12345", true);

        TestProvider.setName("abcdefghijklmnopqrstuvwxyz");

        String result = TestProvider.getName();

        assertNotEquals("Name wasn't set properly", result, "abcdefghijklmnopqrstuvwxyz");
    }

    @org.junit.Test
    public void setNameEmpty() throws Exception {
        final Provider TestProvider = new Provider(123456789, "Donald Trump", "Trump Tower", "NYC", "NY", "12345", true);

        TestProvider.setName("");

        String result = TestProvider.getName();

        assertNotEquals("Name wasn't set properly", result, "");
    }

    @org.junit.Test
    public void setAddress() throws Exception {
        final Provider TestProvider = new Provider(123456789, "Donald Trump", "Trump Tower", "NYC", "NY", "12345", true);

        String testAddress = "Trump Tower 2";

        TestProvider.setAddress(testAddress);

        assertEquals("Address was not set.", testAddress, TestProvider.getAddress());
    }

    @org.junit.Test
    public void setAddressTooLong() throws Exception {
        final Provider TestProvider = new Provider(123456789, "Donald Trump", "Trump Tower", "NYC", "NY", "12345", true);

        TestProvider.setAddress("abcdefghijklmnopqrstuvwxyz");

        String result = TestProvider.getAddress();

        assertNotEquals("Address wasn't set properly", result, "abcdefghijklmnopqrstuvwxyz");
    }

    @org.junit.Test
    public void setAddressEmpty() throws Exception {
        final Provider TestProvider = new Provider(123456789, "Donald Trump", "Trump Tower", "NYC", "NY", "12345", true);

        TestProvider.setAddress("");

        String result = TestProvider.getAddress();

        assertNotEquals("Address wasn't set properly", result, "");
    }

    @org.junit.Test
    public void setCity() throws Exception {
        final Provider TestProvider = new Provider(123456789, "Donald Trump", "Trump Tower", "NYC", "NY", "12345", true);

        String testCity = "Hillsboro";

        TestProvider.setCity(testCity);

        assertEquals("City was not set.", testCity, TestProvider.getCity());
    }

    @org.junit.Test
    public void setCityTooLong() throws Exception {
        final Provider TestProvider = new Provider(123456789, "Donald Trump", "Trump Tower", "NYC", "NY", "12345", true);

        TestProvider.setCity("abcdefghijklmnopqrstuvwxyz");

        String result = TestProvider.getCity();

        assertNotEquals("City wasn't set properly", result, "abcdefghijklmnopqrstuvwxyz");
    }

    @org.junit.Test
    public void setCityEmpty() throws Exception {
        final Provider TestProvider = new Provider(123456789, "Donald Trump", "Trump Tower", "NYC", "NY", "12345", true);

        TestProvider.setCity("");

        String result = TestProvider.getCity();

        assertNotEquals("City wasn't set properly", result, "");
    }

    @org.junit.Test
    public void setState() throws Exception {
        final Provider TestProvider = new Provider(123456789, "Donald Trump", "Trump Tower", "NYC", "NY", "12345", true);

        String testState = "OR";

        TestProvider.setState(testState);

        assertEquals("State was not set.", testState, TestProvider.getState());
    }

    @org.junit.Test
    public void setStateTooLong() throws Exception {
        final Provider TestProvider = new Provider(123456789, "Donald Trump", "Trump Tower", "NYC", "NY", "12345", true);

        TestProvider.setState("abcdefghijklmnopqrstuvwxyz");

        String result = TestProvider.getState();

        assertNotEquals("State wasn't set properly", result, "abcdefghijklmnopqrstuvwxyz");
    }

    @org.junit.Test
    public void setStateEmpty() throws Exception {
        final Provider TestProvider = new Provider(123456789, "Donald Trump", "Trump Tower", "NYC", "NY", "12345", true);

        TestProvider.setState("");

        String result = TestProvider.getState();

        assertNotEquals("State wasn't set properly", result, "");
    }

    @org.junit.Test
    public void setZip() throws Exception {
        final Provider TestProvider = new Provider(123456789, "Donald Trump", "Trump Tower", "NYC", "NY", "12345", true);

        String zip = "23451";

        TestProvider.setZip(zip);

        assertEquals("Zipcode was not set.", zip, TestProvider.getZip());
    }

    @org.junit.Test
    public void setZipTooLong() throws Exception {
        final Provider TestProvider = new Provider(123456789, "Donald Trump", "Trump Tower", "NYC", "NY", "12345", true);

        TestProvider.setZip("123456");

        String result = TestProvider.getZip();

        assertNotEquals("Zipcode wasn't set properly", result, "123456");
    }

    @org.junit.Test
    public void setZipEmpty() throws Exception {
        final Provider TestProvider = new Provider(123456789, "Donald Trump", "Trump Tower", "NYC", "NY", "12345", true);

        TestProvider.setZip("");

        String result = TestProvider.getZip();

        assertNotEquals("Zipcode wasn't set properly", result, "");
    }

    @org.junit.Test
    public void testStatus() throws Exception {
        Provider TestProvider = new Provider(123456789, "Donald Trump", "Trump Tower", "NYC", "NY", "12345", true);

        TestProvider.setStatus(false);

        assertEquals("Status was set incorrectly", false, TestProvider.getStatus());
    }

}