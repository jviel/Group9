package com.psu.group9;
import com.sun.tools.javac.util.*;

import javax.swing.tree.TreeNode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.jar.Pack200;
import java.util.Scanner;
import java.util.Vector;


/**
 * Created by andykeene on 11/10/16.
 */
public class Terminal {

    static Scanner sc = new Scanner(System.in);

    public static void main(String [] args){


        //create database, terminates if errored
        Database db = new Database("database.db");

        // run terminal VM
        final String terminalMenu =  "@MainMenu \n" +
                                     "Options: (1) Manager, (2) Operator, (3) Provider, (4) Quit \n";
        final String prompt = "Enter option: ";
        int tmMax = 4;
        int tmMin = 1;
        int userOption = 0;

        //Kick off main menu
        while (userOption != 4){
            System.out.print(terminalMenu);
            userOption = getInt(prompt,tmMin, tmMax);

            switch(userOption) {
                case 1: System.out.println("Entering Manager Terminal...." + '\n');
                        managerTerminal(db);
                        break;
                case 2: System.out.println("Entering Operator Terminal:" + '\n');
                        operatorTerminal(db);
                        break;
                case 3: System.out.println("Entering Provider Terminal:" + '\n');
                        providerTerminal(db);
                        break;
                case 4: System.out.println("Thank you for using CA!");
                        break;
                default:
                        System.out.println("Invalid selection, please try again...");
                        break;
            }


        }
    }


    /**
     * Virtualizes manager terminal
     * @param Database
     */
    private static void managerTerminal(Database db){

        final String managerMenu =  "@Manager Terminal \n" +
                                    "For Services: (1) List (2) Add     (3) Update  (4) Delete   (5) Reinstate  \n" +
                                    "For Reports:  (6) EFT  (7) Summary (8) Patient (9) Provider \n" +
                                    "Other:        (10) Exit Manger Terminal \n";
        final String prompt = "Enter option: ";
        int mmMax = 10;
        int mmMin = 1;
        int option = 0;


       // System.out.println(managerMenu);
        while (option != 10){
            System.out.println(managerMenu);
            option = getInt(prompt, mmMin, mmMax);

            switch(option){
                case 1: //List services
                        Vector<Service> services = db.getAllServices();
                        System.out.println("All services");
                        for (Service s : services){
                            System.out.println(s);
                        }
                        break;
                case 2: //Add service
                     /* --- TODO: Add confirmation for service object add? */
                        System.out.println("Adding service...");
                        Service service = getService();
                        int id = db.addService(service);
                        if (id > 0){
                            System.out.println("Added: \n" + service.toString());
                        } else if (id < -1) {
                            System.out.println("Service " + service.getName() + " already exists.");
                        }
                        break;
                case 3: //Update service
                        /* --- TODO: Add confirmation for service object update? */
                        int updateServiceId = getInt("Please enter the service code: ", 0, 999999);
                        System.out.println("Please enter the new...");
                        Service updateService = getService();
                        if(db.updateService(updateServiceId, updateService)){
                            System.out.println("Updated service code " + updateServiceId);  // -- NOTE: Needs prettier print
                        } else {
                            System.out.println("service code " + updateServiceId + " did not exist.");
                        }
                        break;
                case 4: //Delete service
                        /* --- TODO: Add confirmation for service object to delete */
                        int deleteServiceID = getInt("Please enter the code for the service you'd like to delete: ", 0, 999999);
                        if(db.removeService(deleteServiceID)){
                            System.out.println("Service deleted.");
                        } else {
                            System.out.println("Failed to delete service.");
                        }
                        break;
                case 5: //Reinstate deleted service
                        int reinstateServiceId = getInt("Please enter the code for the service you'd like to reinstate: ", 0, 999999);
                        if(db.reinstateService(reinstateServiceId)){
                            System.out.println("Reinstated service code " + reinstateServiceId);
                        } else {
                            System.out.println("Failed to reinstate service code " + reinstateServiceId);
                        }
                        break;

                case 6: //Print EFT Report
                        break;
                case 7: //Print Summary Report
                        break;
                case 8: //Print Patient Report
                        break;
                case 9: //Print Provider report
                        break;
                case 10: //quit...
                        break;
                default: System.out.println("Option " + option + " not valid");
                        break;

            }
        }

        System.out.println("Exiting Manager Terminal...\n");
        //return?
    }
    /**
     * For creating a Service object with name and fee fields
     *
     * @return Constructed service with valid name and fee
     */
    private static Service getService(){
        Service service = null;
        boolean validInput = false;

        while(!validInput) {
            //Takes name and fee - database handles the rest for add, ID is passed separate for Update
            String name = getString("Please enter the service name: ", 0, 21);
            Float fee = getFloat("Please enter the fee: ", -1f, 1000f);

            try {
                service = new Service(0, name, fee, 1);
                validInput = true;
            } catch (InputException e) {
                //Prompt exception, force valid input
                System.out.println("Invalid input: " + e.getMessage() + "\n please try again..");
            }
        }

        return service;
    }

    /**
     * Virtualizes operator terminal
     * @param Database
     */
    private static void operatorTerminal(Database db){
        final String operatorMenu =  "@Operator Terminal \n" +
                "For Patient:   (1) List All (2) Add  (3) Update  (4) Delete (5) Reinstate\n" +
                "For Provider:  (6) List All (7) Add  (8) Update  (9) Delete (10) Reinstate\n" +
                "Other:         (11) Exit Operator Terminal \n";
        final String prompt = "Enter option: ";
        int omMax = 11;
        int omMin = 1;
        int option = 0;

        while (option != omMax ){
            System.out.print(operatorMenu);
            option = getInt(prompt, omMin, omMax);


            switch(option) {

                case 1: //List all patients
                        Vector<Entity> patients = db.getAllPatients();
                        System.out.println("\n" + "Printing all Patients...");
                        for(Entity e: patients ){
                            System.out.println(e + "\n");
                        }
                        break;
                case 2: //Add patient
                        Patient newPatient = getPatient();

                        if(newPatient != null) {
                            int newPatientId = db.addPatient(newPatient);

                            //db returns int corresponding to id OR error - inform user
                            if (newPatientId > 0) {
                                newPatient.setIdNumber(newPatientId);
                                System.out.println("Successfully added: \n" + newPatient + "\n");
                            } else if (newPatientId < -1) {
                                System.out.println("Patient \"" + newPatient.getName() + "\" already exists\n");
                            }
                        }
                        break;
                case 3: //Update patient
                        int updatePatientId = getInt("Please enter the ID of the patient to update: ", 0, 999999999);
                        Vector<Entity> patientVec = db.getPatientByID(updatePatientId);

                        //If the patient exists, we update - prompt user accordingly
                        if(patientVec.size() != 0) {

                            //Verify we want to update this user
                            if(getConfirmation("\nUpdate following patient?\n" + patientVec.elementAt(0) + "\n")) {

                                System.out.println("Please enter the patients updated information...");
                                Patient updatePatient = getPatient();
                                //Ensure we have a valid patient object and report success of update to user
                                if (updatePatient != null && db.updatePatient(updatePatientId, updatePatient)) {
                                    System.out.println("Updated patient " + updatePatientId + "\n");
                                } else {
                                    System.out.println("Failed to update Patient " + updatePatientId + "\n");
                                }
                            } else {
                                break;
                            }
                        } else {
                            System.out.println("Patient " + updatePatientId + " does not exist \n");
                        }
                        break;
                case 4: //Delete patient
                        int deletePatientId = getInt("Please enter the ID for the Patient to be deleted: ", 0, 999999999);
                        //Report success of deletion to user
                        if(db.removePatient(deletePatientId)){
                            System.out.println("Removed patient " + deletePatientId + "\n");
                        } else {
                            System.out.println("Failed to remove patient " + deletePatientId + "\n");
                        }
                        break;
                case 5: //Reinstate patient
                        int reinstatePatientId = getInt("Please enter the ID for the Patient to be reinstated: ", 0, 999999999);
                        if(db.reinstatePatient(reinstatePatientId)){
                            System.out.println("Reinstated " + reinstatePatientId + "\n");
                        } else {
                            System.out.println("Failed to reinstate " + reinstatePatientId + "\n");
                        }
                        break;
                case 6: //List all providers
                        Vector<Entity> providers = db.getAllProviders();
                        System.out.println("\n" + "All providers");
                        for(Entity e : providers){
                            System.out.println(e + "\n");
                        }
                        break;
                case 7: //Add provider
                        Provider newProvider = getProvider();
                        //If valid object, add to db
                        if(newProvider != null){

                            int newProviderId = db.addProvider(newProvider);
                            //db returns int corresponding to id OR error - inform user
                            if (newProviderId > 0) {
                                newProvider.setIdNumber(newProviderId);
                                System.out.println("Successfully added: \n" + newProvider + "\n");
                            } else if (newProviderId < -1) {
                                System.out.println("Provider \"" + newProvider.getName() + "\nalready exists \n");
                            }
                        }
                        break;
                case 8: //Update provider
                        int updateProviderId = getInt("Please enter the ID of the patient to update: ", 0, 999999999);
                        Vector<Entity> providerVec = db.getPatientByID(updateProviderId);

                        //If the patient exists, we update - prompt user accordingly
                        if(providerVec.size() != 0) {

                            //Verify we want to update this user
                            if(getConfirmation("\nUpdate following provider?\n" + providerVec.elementAt(0) + "\n")) {

                                System.out.println("Please enter the provider updated information...");
                                Provider updateProvider = getProvider();
                                //Ensure we have a valid patient object and report success of update to user
                                if (updateProvider != null && db.updateProvider(updateProviderId, updateProvider)) {
                                    System.out.println("Updated provider " + updateProviderId +"\n");
                                } else {
                                    System.out.println("Failed to update provider " + updateProviderId + "\n");
                                }
                            } else {
                                break;
                            }
                        } else {
                            System.out.println("Provider " + updateProviderId + " does not exist \n");
                        }
                        break;
                case 9: //Delete provider
                        int deleteProviderId = getInt("Please enter the ID for the provider to be deleted: ", 0, 999999999);
                        //Report success of deletion to user
                        if(db.removeProvider(deleteProviderId)){
                            System.out.println("Removed provider " + deleteProviderId +"\n");
                        } else {
                            System.out.println("Failed to remove provider " + deleteProviderId + "\n");
                        }
                        break;
                case 10: //Reinstate provider
                        int reinstateProviderId = getInt("Please enter the ID for the provider to be reinstated: ", 0, 999999999);
                        if(db.reinstateProvider(reinstateProviderId)){
                           System.out.println("Reinstated provider " + reinstateProviderId +"\n");
                        } else {
                           System.out.println("Failed to reinstate provider" + reinstateProviderId + "\n");
                        }
                        break;
                case 11: //quit
                        break;
                default:
                    System.out.println("Invalid selection, please try again..." + "\n");
                    break;
            }
        }

    }
    /**
     * Creates a provider without ID
     *
     * @return Provider object without ID
     */
    private static Provider getProvider(){

        Provider provider = null;
        boolean valid = false;

        while(!valid) {
            //Takes name and fee - database handles the rest for add, ID is passed separate for Update
            String name = getString("Please enter the providers name: ", 1, 25);
            String address = getString("Please enter the providers address: ", 1, 25);
            String city = getString("Please enter the providers city: ", 1, 14);
            String state = getString("Please enter the providers state (ex. OR, AZ): ", 2, 2);
            /* TODO: Validate 5-digit zip with regex? */
            String zip = getString("Please enter the providers zip (5 digits): ", 5, 5);
            boolean status = getConfirmation("Is provider status active? ");


            //Try creating new provider
            try {
                provider = new Provider(0, name, address, city, state, zip, status);
                valid = true;
            } catch (InputException e) {
                //Prompt exception, force valid input
                System.out.println("Invalid input: " + e.getMessage() + "\n");
                //Allows the user to exit
                if(!getConfirmation("Would you like to try again?")){
                    valid = true;
                }
            }
        }

        return provider;
    }
    /** Creates patient object with valid name, address, city, state, zip

        @return Patient object with PII
     */

    private static Patient getPatient()
    {
        Patient patient = null;
        boolean valid = false;

        while(!valid) {
            //Takes name and fee - database handles the rest for add, ID is passed separate for Update
            String name = getString("Please enter the patient name: ", 1, 25);
            String address = getString("Please enter the patients address: ", 1, 25);
            String city = getString("Please enter the patients city: ", 1, 14);
            String state = getString("Please enter the patients state (ex. OR, AZ): ", 2, 2);
            /* TODO: Validate 5-digit zip with regex? */
            String zip = getString("Please enter the patients zip (5 digits): ", 5, 5);
            boolean status = getConfirmation("Is patient status active? ");
            //financial standing is only handled by Acme
            boolean financialStanding = true;
            // Removed below b/c this is only done by acme!
            // boolean financialStanding = getConfirmation("Is patient in good financial standing? ");

            //Try creating new patient
            try {
                patient = new Patient(0, name, address, city, state, zip, status, financialStanding);
                valid = true;
            } catch (InputException e) {
                //Prompt exception, force valid input
                System.out.println("Invalid input: " + e.getMessage() + "\n");
                //Allows the user to exit
                if(!getConfirmation("Would you like to try again?")){
                    valid = true;
                }
            }
        }

        return patient;
    }

    /**
     * Provider Virtual Terminal Interface
     */
    private static void providerTerminal(Database db){

        final String providerMenu =  "@Provider Terminal \n" +
                "(1) Start new consultation\n" +
                "(2) List available services\n" +
                "(3) List patients in consultation history\n" +
                "(4) Logout of provider terminal\n";
        final String prompt = "Enter option: ";
        int mmMax = 4;
        int mmMin = 1;
        int option = 0;

        // TODO: add provider id verification. Need id for adding transactions

        /* --- TEST DATA --- */
//        try {
//            db.addService(new Service(10001, "Haircut", 45.00f, 1));
//            db.addPatient(new Patient(10000, "Wonderboy", "123 main st.", "Portland","OR","97202",1,1));
//            db.addProvider(new Provider(10000, "Spa-tan extreme", "123 main st.", "Portland","OR","97202", true));
//        }
//        catch (InputException ex){
//            System.out.println("no good");
//        }
        /* --- END TEST DATA --- */

        while (option != mmMax){

            System.out.println("\n" + providerMenu);
            option = getInt(prompt, mmMin, mmMax);

            switch(option){
                case 1: // Start consultation, needs submenu
                    addConsultation(db);
                    break;
                case 2: // List services
                    for (Service svc : db.getAllActiveServices())
                        System.out.println(svc);
                    break;
                case 3: // List patients in history
                    // --- TODO: set provider id to login value --- //
                    // --- TODO: add getPatientsByProviderID?   --- //
                    break;
                case 4: // quit
                    System.out.println("Logging out of provider terminal\n");
                    break;
                default: System.out.println("Option " + option + " not valid");
                    break;
            }
        }
    }

    private static void addConsultation(Database db) {
        final String consulationMenu =
                "@Consultation menu\n" +
                "(1) List available services\n" +
                "(2) Add service to this consultation\n" +
                "(3) View consultation so far\n" +
                "(4) Save consultation and return to provider menu\n" +
                "(5) Cancel\n";
        final String prompt = "Select option: ";
        String consultDate = "";
        boolean validPatient = false;
        Vector<Entity> patient = new Vector<>();
        Vector<Transaction> consultation = new Vector<>();
        int option = 0;
        int mmMin = 1;
        int mmMax = 6;

        do{
            consultDate = getString("Enter the consultation date in the form \"MM-DD-YYYY\": ", 10, 10);
        } while (!isValidShortDate(consultDate));

        do{
            int id = getInt("Enter patient id: ", 0, 999999999);
            patient = db.getPatientByID(id);
            if (patient.size() > 0) {
                if(getConfirmation("\nAdd consultation for following patient?\n" + patient.elementAt(0) + "\n"))
                    validPatient = true;
            }
            else
                System.out.println("Could not find patient with id " + id);
        } while (!validPatient);

        while (option != mmMax){

            System.out.println("\nConsultation " + consultDate + " for patient: " + patient.firstElement().getName() +
                               " (id: " + patient.firstElement().getIdNumber() + ")");
            System.out.println(consulationMenu);
            option = getInt(prompt, mmMin, mmMax);

            switch(option)
            {
                case 1: // List services
                    for (Service svc : db.getAllActiveServices())
                        System.out.println(svc);
                    break;

                case 2: // Add Service to consultation
                    int id = getInt("Enter service ID: ", 0, 999999);
                    String comment = getString("Add optional comment: ", 0, 100);
                    try {
                        consultation.add(new Transaction(
                                patient.firstElement().getIdNumber(), // patient id
                                123456789,                            // provider id
                                id,                                   // service id
                                123456,                               // consultation number
                                consultDate,                          // service date
                                comment                               // comments
                        ));
                    } catch (InputException ex){
                        System.out.println("Error adding service: " + ex.getMessage());
                    }
                    break;

                case 3: // View consultation
                    if (consultation.size() == 0) {
                        System.out.println("No services added to consultation yet");
                        break;
                    }
                    int count = 1;
                    System.out.println("\nConsultation " + consultDate + " for patient: " + patient.firstElement().getName() +
                            " (id: " + patient.firstElement().getIdNumber() + ")");
                    for (Transaction t : consultation) {
                        System.out.println("Service " + count++);
                        System.out.println(t);
                    }
                    break;

                case 4: // Save
                    if (db.addConsultation(consultation) >= 0);
                    System.out.println("Added Consultation successfully.");
                    option = mmMax; // break
                    break;

                case 5: // Cancel
                    System.out.println("Consultation canceled.");
                    option = mmMax;
                    break;

                default:
                    System.out.println("Invalid option.");
                    break;
            }
        }
        System.out.println("Leaving consultation menu");
    }

    /* ---- helper functions ---- */
    private static Boolean isValidShortDate(String date) {
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

    /**
     * For getting user input in the form of a string fitting a certain length. Error checking within
     * @param String prompt to display to user
     * @param int minimum allowable length
     * @param int maximum allowable length
     */
    private static String getString(String prompt, int min, int max)
    {
        String ret = "";
        boolean valid = false;

        while(!valid)
        {
            System.out.print(prompt);
            ret = sc.nextLine();
            if (ret.length() < min || ret.length() > max)
                System.out.println("Input length must be between " + min + " and " + max + " characters");
            else
                valid = true;
        }
        return ret;
    }

    /**
     * For getting user input in the form of an int within given range. Error checking within
     * @param String prompt to display to user
     * @param int minimum allowable value
     * @param int maximum allowable value
     */
    private static int getInt(String prompt, int min, int max)
    {
        int ret = -1;
        boolean valid = false;

        while(!valid)
        {
            try {
                System.out.print(prompt);
                ret = sc.nextInt(); // throws InputMismatchException if not an int
                if (ret < min || ret > max)
                    throw new InputMismatchException();
                else
                    valid = true;
            }
            catch (InputMismatchException ex) {
                System.out.println("Value must be a number between " + min + " and " + max);
            }
            clearScanner(sc);
        }
        return ret;
    }

    /**
     * For getting user input in the form of a float within given range. Error checking within
     * @param String prompt to display to user
     * @param float minimum allowable value
     * @param float maximum allowable value
     */
    private static float getFloat(String prompt, float min, float max)
    {
        float ret = -1f;
        boolean valid = false;

        while(!valid)
        {
            try {
                System.out.print(prompt);
                ret = sc.nextFloat(); // throws InputMismatchException if not a float
                if (ret < min || ret > max)
                    throw new InputMismatchException();
                else
                    valid = true;
            }
            catch (InputMismatchException ex) {
                System.out.println("Value must be a decimal number between " + min + " and " + max);
            }
            clearScanner(sc);
        }
        return ret;
    }

    /**
     * For getting user confirmation. Prompt + (Yes/No): is displayed, and user response is returned
     * @param prompt
     * @return True is yes is entered, False if else
     */

    private static boolean getConfirmation(String prompt)
    {
        String ret;
        boolean confirmation = false;

        System.out.print(prompt + " (Yes/No):");
        ret = sc.nextLine();
        if (ret.equalsIgnoreCase("yes") || ret.equalsIgnoreCase("y")) {
            confirmation = true;
        } else {
            confirmation = false;
        }

        return confirmation;
    }

    /**
     * When getting numerical input from user, if they enter a non-number, an exception is thrown but buffer
     * retains input including '\n' character. This needs to be cleared to enter a new number.
     * Does not apply after Scanner.nextLine() because this pops the '\n'
     * @param Scanner
     */
    private static void clearScanner(Scanner s)
    {
        if (s.hasNextLine()) // presence of a newline character.
            s.nextLine();
    }
}
