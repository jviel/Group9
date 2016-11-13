
public class Provider extends Entity{

    public Provider(int newIdNumber, String newName, String newAddress, String newCity,
                    String newState, String newZip, boolean newStatus)
                    throws InputException {

        super(newIdNumber, newName, newAddress, newCity, newState, newZip, newStatus);
    }

    @Override
    public String toString() {
        return "Provider ID : " + getIdNumber() + "\n" +
                "Name: " + getName() + "\n" +
                "Address: " + getAddress() + "\n" +
                "City: " + getCity() + "\n" +
                "State: " + getState() + "\n" +
                "Zipcode: " + getZip() + "\n" +
                "Enrollment Status: " +
                ((getStatus())? "Active" : "Inactive");
    }
}
