package com.psu.group9;

public abstract class Entity {

    private int idNumber;
    private String name;
    private String address;
    private String city;
    private String state;
    private String zipcode;
    private boolean status;

    public Entity(int newIdNumber, String newName, String newAddress, String newCity, String newState,
                  String newZip, boolean newStatus)
                  throws InputException {

        String exceptionString = "";


        if (!setIdNumber(newIdNumber)) {
            exceptionString += "ID must be a positive 9-digit number.\n";
        }
        if (!setName(newName)) {
            exceptionString += "Name cannot be blank, and must be 25 characters or less.\n";
        }
        if (!setAddress(newAddress)) {
            exceptionString += "Address cannot be blank, and must be 25 characters or less.\n";
        }
        if (!setCity(newCity)) {
            exceptionString += "City cannot be blank, and must be 14 characters or less.\n";
        }
        if (!setState(newState)) {
            exceptionString += "State must be two characters.\n";
        }
        if (!setZip(newZip)) {
            exceptionString += "Zipcode must be five numeric characters.\n";
        }
        setStatus(newStatus);

        if (!exceptionString.isEmpty()) {
            exceptionString = exceptionString.substring(0,
                    exceptionString.length() - 1);
            throw new InputException(exceptionString);
        }
    }

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

    public boolean setIdNumber(int newNumber) {
        boolean ret = false;

        if (newNumber <= 999999999 && newNumber >= 0) {
            this.idNumber = newNumber;

            ret = true;
        }

        return ret;
    }

    public boolean setName(String newName) {
        boolean ret = false;

        if (isValidLength(newName, 25)) {
            this.name = newName;

            ret = true;
        }

        return ret;
    }

    public boolean setAddress(String newAddress) {
        boolean ret = false;

        if (isValidLength(newAddress, 25)) {
            this.address = newAddress;

            ret = true;
        }

        return ret;
    }

    public boolean setCity(String newCity) {
        boolean ret = false;

        if (isValidLength(newCity, 14)) {
            this.city = newCity;

            ret = true;
        }

        return ret;
    }

    public boolean setState(String newState) {
        boolean ret = false;

        if (newState.matches("^[a-zA-Z]{2}$")) {
            this.state = newState;

            ret = true;
        }

        return ret;
    }

    public boolean setZip(String newZip) {
        boolean ret = false;

        if (newZip.matches("^\\d{5}$")) {
            this.zipcode = newZip;

            ret = true;
        }

        return ret;
    }

    public boolean setStatus(boolean newStatus) {
        this.status = newStatus;

        return true;
    }

    @Override
    public String toString() {
        return "ID: " + idNumber + "\n" +
                "Name: " + name + "\n" +
                "Address: " + address + "\n" +
                "City: " + city + "\n" +
                "State: " + state + "\n" +
                "Zipcode: " + zipcode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if(!Entity.class.isAssignableFrom(obj.getClass())) {
            return false;
        }

        final Entity other = (Entity) obj;

        // And we have to do this for every single string...

        if(!(compareStrings(name, other.getName())) ||
                !(compareStrings(address, other.getAddress())) ||
                !(compareStrings(city, other.getCity())) ||
                !(compareStrings(state, other.getState())) ||
                !(compareStrings(zipcode, other.getZip()))) {
            return false;
        }

        return true;
    }

    private Boolean compareStrings(String s1, String s2) {
        if(s1 == null) {
            if(s2 != null) {
                return false;
            }
        }

        else {
            if(!(s1.equals(s2))) {
                return false;
            }
        }

        return true;
    }

    private boolean isValidLength(String theString, int maxLength) {
        boolean ret = false;

        if (theString.length() > 0 && theString.length() <= maxLength) {
            ret = true;
        }

        return ret;
    }

}