package com.psu.group9;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Transaction {
    // variables
    private int idNumber;
    private int patientID;
    private int providerID;
    private int serviceID;
    private int consultationNumber;
    private String dateTime;
    private String serviceDate;
    private String comments;

    // constructors
    public Transaction(int idNumber, int patientID, int providerID, int serviceID,
        int consultationNumber, String dateTime, String serviceDate,
        String comments) throws InputException {
            String exceptionString = "";

            if (dateTime == "") {
              dateTime = getTodayDateLong();
            }

            if (idNumber < 0 || idNumber > 999999999) {
                exceptionString += "ID Number must be a nine-digit integer.\n";
            }

            if (!isValidLongDate(dateTime)) {
                exceptionString += "Date-Time must be formatted in form " +
                    "MM-DD-YYYY HH-MM-SS.\n";
            }

            if (!isValidShortDate(serviceDate)) {
                exceptionString += "Service Date must be formatted in form " +
                    "MM-DD-YYYY.\n";
            }

            if (patientID < 0 || patientID > 999999999) {
                exceptionString += "Patient ID must be a nine-digit integer.\n";
            }

            if (consultationNumber < 0 || consultationNumber > 999999999) {
                exceptionString += "Consultation ID must be a nine-digit integer.\n";
            }

            if (providerID < 0 || providerID > 999999999) {
                exceptionString += "Provider ID must be a nine-digit integer.\n";
            }

            if (serviceID < 0 || serviceID > 999999) {
                exceptionString += "Service ID must be a six-digit integer.\n";
            }

            if (dateTime.length() != 19) {
                exceptionString += "Date and Time must be formatted MM-DD-YYYY HH:MM:SS.\n";
            }

            if (serviceDate.length() != 10) {
                exceptionString += "Service date must be formatted MM-DD-YYYY.\n";
            }

            if (comments.length() > 100) {
                exceptionString += "Comments may be no longer than 100 characters.\n";
            }

            if (!(exceptionString.isEmpty())) {
                exceptionString = exceptionString.substring(0,
                    exceptionString.length() -1);
                throw new InputException(exceptionString);
            }

            setIDNumber(idNumber);
            setPatientID(patientID);
            setProviderID(providerID);
            setServiceID(serviceID);
            setConsultationNumber(consultationNumber);
            setDateTime(dateTime);
            setServiceDate(serviceDate);
            setComments(comments);
    }

    public Transaction(int patientID, int providerID, int serviceID,
        int consultationNumber, String serviceDate,
        String comments) throws InputException {
          this(0, patientID, providerID, serviceID, consultationNumber, "", serviceDate, comments);
      }


    // getters
    public int getIDNumber() {
      return idNumber;
    }

    public int getPatientID() {
      return patientID;
    }

    public int getProviderID() {
        return providerID;
    }

    public int getServiceID() {
        return serviceID;
    }

    public int getConsultationNumber() {
      return consultationNumber;
    }

    public String getDateTime() {
      return dateTime;
    }

    public String getServiceDate() {
      return serviceDate;
    }

    public String getComments() {
      return comments;
    }


    // setters
    private void setIDNumber(int idNumber) {
      this.idNumber = idNumber;
    }

    private void setPatientID(int patientID) {
      this.patientID = patientID;
    }

    private void setProviderID(int providerID) {
      this.providerID = providerID;
    }

    private void setServiceID(int serviceID) {
      this.serviceID = serviceID;
    }

    private void setConsultationNumber(int consultationNumber) {
      this.consultationNumber = consultationNumber;
    }

    private void setDateTime(String dateTime) {
      this.dateTime = dateTime;
    }

    private void setServiceDate(String serviceDate) {
      this.serviceDate = serviceDate;
    }

    private void setComments(String comments) {
      this.comments = comments;
    }


    // helper functions
    private String getTodayDateLong() {
          Date now = new Date();
          SimpleDateFormat ft = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
          return ft.format(now);
      }

    private Boolean isValidShortDate(String date) {
        if (date.length() != 10) {
            return false;
        }
        SimpleDateFormat ft = new SimpleDateFormat("MM-dd-yyyy");
        try {
            Date tryDate = ft.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private Boolean isValidLongDate(String date) {
          if (date.length() != 19) {
              return false;
          }
          SimpleDateFormat ft = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
          try {
              Date tryDate = ft.parse(date);
              return true;
          } catch(ParseException e) {
              return false;
          }
      }

    // override toString
    @Override
    public String toString() {
        return "Transaction ID: " + idNumber + "\n" +
            "Date-Time: " + dateTime + "\n" +
            "Service Date: " + serviceDate + "\n" +
            "Provider ID: " + Integer.toString(providerID) + "\n" +
            "Patient ID: " + Integer.toString(patientID) + "\n" +
            "Service ID: " + Integer.toString(serviceID) + "\n" +
            "Consultation Number: " + Integer.toString(consultationNumber) + "\n" +
            (!(comments.isEmpty()) ? "Comments: " : "") +
            comments + "\n";
    }
}
