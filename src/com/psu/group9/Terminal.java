package com.psu.group9;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.jar.Pack200;
import java.util.Scanner;
import java.util.Vector;

/*
JAVADOC NOTES:
- <code> </code> to highlight keywords
-

FEATURES
- Input Validation
- All terminals
- Reports (maybe create reports class?)
- Transactions
- Provider directory

ANDY DESIGN NOTES:
- Should we repeat menu every time? Better way? Who cares?
- Add, Update functions
- Wheres my db yo?!
*/
//

/**
 * Created by andykeene on 11/10/16.
 */
public class Terminal {

    static Scanner sc = new Scanner(System.in);

    public static void main(String [] args){

        /*
        create database - check validity
         */
        Database db = new Database("database.db");

        // run terminal VM
        final String terminalMenu =   "@MainMenu \n" + "" +
                                        "Options: (1) Manager, (2) Operator, (3) Provider, (4) Quit \n";
        final String userPrompt = "Enter option: ";
        int tmMax = 4;
        int tmMin = 1;
        int userOption = 0;

        while (userOption != 4){
            System.out.print(terminalMenu);
            userOption = getInt(userPrompt,tmMin, tmMax);

            switch(userOption) {
                case 1: System.out.println("Entering Manager Terminal...." + '\n');
                        managerTerminal(db);
                        break;
                case 2: System.out.println("Entering Operator Terminal:" + '\n');
                        operatorTerminal(db);
                        break;
                case 3: System.out.println("Provider Terminal:" + '\n');
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
                "For Patient:   (1) Add  (2) Update  (3) Delete (4) Reinstate\n" +
                "For Provider:  (5) Add  (6) Update  (7) Delete (8) Reinstate\n" +
                "Other:         (8) Exit Operator Terminal \n";
        final String prompt = "Enter option: ";
        int omMax = 8;
        int omMin = 1;
        int option = 0;

        while (option != 8 ){
            System.out.print(operatorMenu);
            option = getInt(prompt,omMin, omMax);

            /* TODO: Remove this testing method */
            Vector<Entity> patients = db.getAllPatients();
            for(Entity e: patients ){
                System.out.println(e + "\n");
            }
            switch(option) {




                case 1: //Add patient
                        Patient newPatient = getPatient();
                        int newPatientId = db.addPatient(newPatient);

                        if(newPatientId > 0){
                            newPatient.setIdNumber(newPatientId);
                            System.out.println("Successfully added: \n" + newPatient);
                        } else if (newPatientId < -1){
                            System.out.println("Patient " + newPatient.getName() + " already exists");
                        }
                        break;
                case 2: //Update patient
                        Patient updatePatient = getPatient();
                        int updatePatientId = getInt("Please enter patient ID: ", 0, 999999999);
                        //Report success of update to user
                        if(db.updatePatient(updatePatientId, updatePatient)) {
                            System.out.println("Updated patient " + updatePatientId);
                        } else {
                            System.out.println("Failed to update Patient " + updatePatientId);
                        }
                        break;
                case 3: //Delete patient
                        int deletePatientId = getInt("Please enter the ID for the Patient to be deleted: ", 0, 999999999);
                        //Report success of deletion to user
                        if(db.removePatient(deletePatientId)){
                            System.out.println("Removed patient " + deletePatientId);
                        } else {
                            System.out.println("Failed to remove patient " + deletePatientId);
                        }
                        break;
                case 4: //Reinstate patient
                        int reinstatePatientId = getInt("Please enter the ID for the Patient to be reinstated: ", 0, 999999999);
                        if(db.reinstatePatient(reinstatePatientId)){
                            System.out.println("Reinstated " + reinstatePatientId);
                        } else {
                            System.out.println("Failed to reinstate " + reinstatePatientId);
                        }
                        break;
                case 5: //Add provider
                        break;
                case 6: //Delete provider
                        break;
                case 7: //Reinstate provider
                        break;
                case 8: //Quit
                        break;
                default:
                    System.out.println("Invalid selection, please try again...");
                    break;
            }


        }




    }
    /** Creates patient object with valid name, address, city, state, zip

        @return Patient object with PII
     */

    private static Patient getPatient()
    {
        Patient patient = null;
        boolean validInput = false;

        while(!validInput) {
            //Takes name and fee - database handles the rest for add, ID is passed separate for Update
            String name = getString("Please enter the patient name: ", 1, 25);
            String address = getString("Please enter the patients address: ", 1, 25);
            String city = getString("Please enter the patients city: ", 1, 14);
            String state = getString("Please enter the patients state (ex. OR, AZ): ", 2, 2);
            /* TODO: Validate 5-digit zip with regex? */
            String zip = getString("Please enter the patients zip (5 digits): ", 5, 5);
            boolean status = getConfirmation("Is patient active? ");
            boolean financialStanding = getConfirmation("Is patient in good financial standing? ");
            //Try creating new patient
            try {
                patient = new Patient(0, name, address, city, state, zip, status, financialStanding);
                validInput = true;
            } catch (InputException e) {
                //Prompt exception, force valid input
                System.out.println("Invalid input: " + e.getMessage() + "\nplease try again..");

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

        /* --- TEST OBJECTS --- */
        try {
            db.addService(new Service(10001, "soothing massage", 45.00f, 1));
            db.addPatient(new Patient(10000, "Gilmore", "123 main st.", "Portland","OR","97202",1,1));
            db.addProvider(new Provider(10000, "Spa-tan Anonymous", "123 main st.", "Portland","OR","97202", true));
        }
        catch (InputException ex){
            System.out.println("no good");
        }
        /* --- END TEST OBJECTS --- */

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
            if (patient.size() > 0)
                // TODO: confirm correct patient
                validPatient = true;
            else
                System.out.println("Could not find patient with id " + id);
        } while (!validPatient);

        while (option != mmMax){

            System.out.println("\n" + consultDate + ": " +
                    patient.firstElement().getName() +
                    "(" + patient.firstElement().getIdNumber() + ")");
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
                                12345,                                // provider id
                                id,                                   // service id
                                123,                                  // consultation number
                                consultDate,                          // service date
                                comment                               // comments
                        ));
                    } catch (InputException ex){
                        System.out.println("Error adding service: " + ex.getMessage());
                    }
                    break;

                case 3: // View consultation
                    for (Transaction t : consultation)
                        System.out.println(t);
                    break;

                case 4: // Save
                    for (Transaction t : consultation)
                        db.addTransaction(t);
                    System.out.println("Added Consultation successfully.");
                    option = mmMax; // break
                    break;

                case 5: // Cancel
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
