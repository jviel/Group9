package com.psu.group9;

public class Patient extends Entity {

    private boolean financialStanding;

    public Patient(int newIdNumber, String newName, String newAddress, String newCity,
                   String newState, String newZip, boolean newStatus, boolean newFinancialStanding)
            throws InputException {
        super(newIdNumber, newName, newAddress, newCity, newState, newZip, newStatus);
        setFinancialStanding(newFinancialStanding);
    }

    public Patient(int newIdNumber, String newName, String newAddress, String newCity,
                   String newState, String newZip, int newStatus, int newFinancialStanding)
            throws InputException {
        super(newIdNumber, newName, newAddress, newCity, newState, newZip , newStatus != 0);
        setFinancialStanding(newFinancialStanding != 0);
    }

    public boolean getFinancialStanding() {
        return financialStanding;
    }

    public void setFinancialStanding(boolean financialStanding) {
        this.financialStanding = financialStanding;
    }

    @Override
    public String toString() {
        return "Patient ID : " + getIdNumber() + "\n" +
                "Name: " + getName() + "\n" +
                "Address: " + getAddress() + "\n" +
                "City: " + getCity() + "\n" +
                "State: " + getState() + "\n" +
                "Zipcode: " + getZip() + "\n" +
                "Enrollment Status: " +
                ((getStatus())? "Active" : "Inactive") + '\n' +
                "Financial Standing: " +
                ((getFinancialStanding())? "Active" : "Inactive");

    }
}

