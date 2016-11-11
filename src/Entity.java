public class Entity {

    private int idNumber;
    private String name;
    private String address;
    private String city;
    private String state;
    private String zipcode;
    private boolean status;

    /*public Entity(int newIdNumber, String newName, String newAddress, String newCity, String newState,
                  String newZip, boolean newStatus) {
        setIdNumber(newIdNumber);
        setName(newName);
        setAddress(newAddress);
        setCity(newCity);
        setState(newState);
        setZip(newZip);
        setStatus(newStatus);
    }*/

    public int getIdNumber() {
        return idNumber;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zipcode;
    }

    public boolean getStatus() {
        return status;
    }

    public void setIdNumber(int newNumber) {
        if (String.valueOf(newNumber).length() == 12) {
            this.idNumber = newNumber;
        }
    }

    public void setName(String newName) {
        if (newName.length() <= 25) {
            this.name = newName;
        }
    }

    public void setAddress(String newAddress) {
        if (newAddress.length() <= 25) {
            this.address = newAddress;
        }
    }

    public void setCity(String newCity) {
        if (newCity.length() <= 14) {
            this.city = newCity;
        }
    }

    public void setState(String newState) {
        if (newState.length() == 2) {
            this.state = newState;
        }
    }

    public void setZip(String newZip) {
        if (newZip.length() == 5) {
            this.zipcode = newZip;
        }
    }

    public void setStatus(boolean newStatus) {
        this.status = newStatus;
    }



}