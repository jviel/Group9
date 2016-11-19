package com.psu.group9;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.jar.Pack200;
import java.util.Scanner;

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

        Database db= new Database("database.db");

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
                case 2: System.out.println("Operator Terminal:" + '\n');
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
        int mmMax = 9;
        int mmMin = 1;
        int option = 0;


        System.out.println(managerMenu);
        while (option != 9){

            option = getInt(prompt, mmMin, mmMax);

            switch(option){
                case 1: //List services
                        /* -- TODO: Update print vector when Mike changes print function */
                        db.printAllServices();
                        break;
                case 2: //Add service
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
                        /* --- TODO: Add confirmation for service object update */
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
                            System.out.println("Reinstate ID " + reinstateServiceId);
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
                System.out.println("Invalid input: " + e + "\n please try again..");
            }
        }

        return service;
    }

    /**
     *
     */
    private static void operatorTerminal(){



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
