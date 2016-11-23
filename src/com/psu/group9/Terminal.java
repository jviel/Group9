package com.psu.group9;

import com.sun.tools.javac.util.Pair;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
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
        /*TODO: Maybe add new Integer(int).toString(tmMax) for all menus? -- Time intensive method calls*/
        while (userOption != 4){
            System.out.print(terminalMenu);
            userOption = getInt(prompt, 0, 4);

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
                            System.out.println("Added: " + service.getName() + "\n" +
                                               "ID:    " + id + "\n" +
                                               "fee:   " + service.getFee());
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
                        eftReport(db);
                        break;
                case 7: //Print Summary Report
                        break;
                case 8: //Print Patient Report
                        patientReport(db);
                        break;
                case 9: //Print Provider report
                        providerReport(db);
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
     * Inefficiently prints patient reports for week prior to day of method call
     * @param Database with weeks transactions
     */

    /*TODO: Further testing */
    private static void patientReport(Database db)
    {
        String today = getDate();
        System.out.println("Today is: " + today);


        /*TODO: REMOVE THIS TEST*/
        //today = getString("Please enter a date: ", 0, 15);

        System.out.println("##### Beginning Patient Report ####\n");

        //Look at all patients because some may have been invalidated in past week
        Vector<Entity> patients = db.getAllPatients();

        for(Entity p : patients){

            //Get weeks transactions for patient ID
            int id = p.getIdNumber();
            Vector<Transaction> weekTransactions = db.getWeekTransactionsByPatient(id, today);

            //if patient has transactions for this week, format report to string
            if(!weekTransactions.isEmpty()){
                int serviceCount = 1;

                String pReport = "Patient Name: "     + p.getName()     + "\n"
                                + "Patient ID: "      + p.getIdNumber() + "\n"
                                + "Patient Address: " + p.getAddress()  + "\n"
                                + "Patient City: "    + p.getCity()     + "\n"
                                + "Patient State: "   + p.getState()    + "\n"
                                + "Patient Zip:"      + p.getZip()      + "\n";

                for (Transaction t : weekTransactions){
                    pReport += "---Service " + serviceCount + "----\n";

                    //Prerequisite 1: Get service name  - prints unresolved if db cannot find it
                    String serviceName = "Unresolved";
                    Vector<Service> service = db.getServiceByID(t.getServiceID());
                    if (!service.isEmpty()){
                        serviceName = service.elementAt(0).getName();
                    }

                    pReport += "Service date: " + t.getServiceDate() + "\n"
                            + "Provider name: " + t.getProviderID()  + "\n"
                            + "Service name: "  + serviceName        + "\n";

                    serviceCount++;
                }
                System.out.println(pReport);
            }
        }

        System.out.println("\n##### Ending Patient Report ####");
    }

    /**
     * Prints provider reports for the week prior to day of method call
     *
     * @param Database that holds report data
     */

    /*TODO: Further testing, aaaand use a string builder??*/
    private static void providerReport(Database db)
    {
        String today = getDate();
        System.out.println("Today is: " + today);
        System.out.println("##### Beginning Provider Report ####\n");
        /*TODO: REMOVE THIS TEST*/
        today = getString("Please enter a date: ", 0, 15);

        //Look at all providers because some may have been invalidated in past week
        Vector<Entity> providers = db.getAllProviders();

        for( Entity p : providers){

            //Get week transaction for patient ID
            int id = p.getIdNumber();
            Vector<Transaction> weekTransactions = db.getWeekTransactionsByPatient(id, today);

            //if patient has transactions for this week, format report to string
            if(!weekTransactions.isEmpty()){
                float feeTotal = 0;                                 //Total transaction fees
                int serviceCount = 1;                               //Service # for printing
                Set<Integer> consultations = new HashSet();         //Track unique consultation #'s

                String pReport = "Provider Name: "     + p.getName() +     "\n"
                                + "Provider ID: "      + p.getIdNumber() + "\n"
                                + "Provider Address: " + p.getAddress()  + "\n"
                                + "Provider City: "    + p.getCity()     + "\n"
                                + "Provider State: "   + p.getState()    + "\n"
                                + "Provider Zip:"      + p.getZip()      + "\n";

                for (Transaction t : weekTransactions){
                    pReport += "---Service " + serviceCount + "----\n";

                    //Prerequisite 1: Get patient name - prints as "unresolved" if db cannot find it
                    String patientName = "Unresolved";
                    Vector<Entity> patient = db.getPatientByID(t.getPatientID());
                    if (!patient.isEmpty()){
                        patientName = patient.elementAt(0).getName();
                    }

                    //Prerequisite 2: Get service fee as String - prints as "Unresolved" if not found by db
                    float serviceFee = -1f;
                    Vector<Service> service = db.getServiceByID(t.getServiceID());
                    if (!service.isEmpty()){
                        serviceFee = service.elementAt(0).getFee();
                        feeTotal += serviceFee;

                    }

                    //Add report information
                    pReport += "Service date: "   + t.getServiceDate()         + "\n"
                            + "Submission Time: " + t.getDateTime()            + "\n"
                            + "Patient Name: "    + patientName                + "\n"
                            + "Patient ID: "      + t.getPatientID()           + "\n"
                            + "Service ID: "      + t.getServiceID()           + "\n"
                            + "Fee: "             + Float.toString(serviceFee) + "\n";       /*TODO: Should I clean this up?*/

                    serviceCount++;                                                        //Increment service count
                    consultations.add(new Integer(t.getConsultationNumber()));             //Add consultation ID to set
                }

                pReport += "*Number of consultations: " + consultations.size() + "\n"
                        +  "*Total Fee Owed: " + feeTotal + "\n\n";
                System.out.println(pReport);                                              //Report prints here
            }
        }

        System.out.println("\n##### Ending Provider Report ####");
    }


    /**
     * Not sure what this one needs to do - just print data for now
     * @param Database that contains transactional data
     */

    private static void eftReport(Database db)
    {
        String today = getDate();
        System.out.println("Today is: " + today);

        // Test data
        Vector<Transaction> allTransactions = db.getAllTransactions();
        for(Transaction t : allTransactions){
            System.out.println(t);
        }

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
            Float fee = getFloat("Please enter the fee: ", 0f, 9999.99f);

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
     * Creates a provider without ID or status
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

            /*TODO: Validate update with new db merge*/
            //boolean status = getConfirmation("Is provider status active? ");


            //Try creating new provider - ID is assigned by db, and status is assumed to be active
            try {
                provider = new Provider(0, name, address, city, state, zip, true);
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

            //boolean status = getConfirmation("Is patient status active? ");
            //financial standing is only handled by Acme
            //boolean financialStanding = true;
            // Removed below b/c this is only done by acme!
            // boolean financialStanding = getConfirmation("Is patient in good financial standing? ");

            /*TODO: Validate update with new db merge*/
            //Try creating new patient - DB handles ID, where financial standing and status are assumed to be true
            try {
                patient = new Patient(0, name, address, city, state, zip, true, true);
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

        final String providerMenu =
                "(1) Check in a patient\n" +
                "(2) Start new consultation\n" +
                "(3) List Provider Directory\n" +
                "(4) Logout of provider terminal\n";
        final String prompt = "Enter option: ";
        int mmMax = 4;
        int mmMin = 1;
        int option = 0;

        // LOGIN: Get Provider ID info
        int id = getInt("Please enter your provider ID number: ", 0, 999999999);
        Vector<Entity> entity = new Vector<Entity>(db.getProviderByID(id));
        Provider provider = null;
        if (entity.size() == 0 || !(entity.elementAt(0) instanceof Provider)){
            System.out.println("Invalid provider number");
            return;
        }
        provider = (Provider)entity.elementAt(0);

        while (option != mmMax){

            System.out.print("@Provider terminal : " + provider.getName());
            System.out.println("\n" + providerMenu);
            option = getInt(prompt, mmMin, mmMax);

            switch(option){
                case 1: // Check in patient
                    validatePatient(db);
                    break;
                case 2: // Start consultation
                    addConsultation(db, provider);
                    break;
                case 3: // List services
                    for (Service svc : db.getAllActiveServices())
                        System.out.println(svc);
                    break;
                case 4: // quit
                    System.out.println("Logging out of provider terminal\n");
                    option = mmMax;
                    break;
                default:
                    System.out.println("Option " + option + " not valid");
                    break;
            }
        }
    }

    private static void addConsultation(Database db, Provider provider) {
        // TODO: add option to remove service from transaction
        final String consulationMenu =
                "@Consultation menu : " + provider.getName() + "\n" +
                "(1) List available services\n" +
                "(2) Add service to this consultation\n" +
                "(3) View consultation so far\n" +
                "(4) Save consultation and return to provider menu\n" +
                "(5) Cancel\n";
        final String prompt = "Select option: ";
        int option = 0;
        int mmMin = 1;
        int mmMax = 5;

        String consultDate = "";
        Patient patient = null;
        Vector<Pair<Transaction, Service>> consultation = new Vector<>();

        /* -- Get Patient info -- */
        patient = validatePatient(db);
        if (patient == null) {
            System.out.println("Consultation canceled.\n");
            return;
        }
//        if(!getConfirmation("\nAdd consultation for following patient?\n" + patient + "\n")) {
//            System.out.println("Consultation canceled.\n");
//            return;
//        }

        /* -- Enter consultation date -- */
        do{
            consultDate = getString("Enter the consultation date in the form \"MM-DD-YYYY\": ", 10, 10);
        } while (!isValidShortDate(consultDate));


        /* -- Main consultation menu -- */
        while (option != mmMax){
            System.out.println("\nConsultation " + consultDate + " for patient: " + patient.getName() +
                               " (id: " + patient.getIdNumber() + ")");
            System.out.println(consulationMenu);
            option = getInt(prompt, mmMin, mmMax);

            switch(option)
            {
                case 1: // List services
                    for (Service svc : db.getAllActiveServices())
                        System.out.println(svc);
                    break;

                case 2: // Add Service to consultation
                    Transaction t = null;
                    Service s = validateService(db);
                    if (s == null)
                        break;
                    String comment = getString("Add optional comment: ", 0, 100);
                    try {
                        t = new Transaction(
                                patient.getIdNumber(),   // patient id
                                provider.getIdNumber(),  // provider id
                                s.getCode(),             // service id
                                0,                       // arbitrary consultation id (set by db.addTransaction())
                                consultDate,             // service date
                                comment                  // comments
                        );
                    } catch (InputException ex){
                        System.out.println("Error adding service: " + ex.getMessage());
                    }
                    consultation.add(new Pair<>(t, s)); // keep track of successful adds
                    break;

                case 3: // View consultation
                    if (consultation.size() == 0) {
                        System.out.println("No services added to consultation yet");
                        break;
                    }
                    int count = 1;
                    System.out.println("\n\tConsultation " + consultDate + " for patient: " + patient.getName() +
                            " (id: " + patient.getIdNumber() + ")");
                    for (Pair<Transaction, Service> pair : consultation) {
                        System.out.println("\t---------------------------");
                        System.out.println("\tItem " + count++);
                        System.out.println("\tService ID:   " + pair.snd.getCode());
                        System.out.println("\tService name: " + pair.snd.getName());
                        System.out.println("\tComments:     " + pair.fst.getComments());
                    }
                    break;

                case 4: // Save
                    if (consultation.size() == 0)
                        System.out.println("No services added. Canceling consultation.");

                    // Glob consultation data
                    float totalFee = 0;
                    Vector<Transaction> transactions = new Vector<>();
                    for (Pair<Transaction, Service> pair : consultation) {
                        transactions.add(pair.fst);
                        totalFee += pair.snd.getFee();
                    }

                    if (db.addConsultation(transactions) >= 0)
                        System.out.println("Added Consultation successfully. Total billed to ChocAn: $" + String.format("%.2f", totalFee));
                    else
                        System.out.println("There was an error adding the consultation to the database");
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
        String input;
        Scanner linebuf;

        while(!valid)
        {
            try {
                System.out.print(prompt);
                input = sc.nextLine();

                if (input.length() == 0)
                    throw new InputMismatchException();
                linebuf = new Scanner(input);
                ret = linebuf.nextInt(); // throws InputMismatchException if not an int
                linebuf.close();

                if (ret < min || ret > max)
                    throw new InputMismatchException();
                else
                    valid = true;
            }
            catch (NoSuchElementException e) {
                System.out.println("Value must be a number between " + min + " and " + max);
            }
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
        String input;
        Scanner linebuf;

        while(!valid)
        {
            try {
                System.out.print(prompt);
                input = sc.nextLine();

                if (input.length() == 0)
                    throw new InputMismatchException();
                linebuf = new Scanner(input);
                ret = linebuf.nextFloat(); // throws InputMismatchException if not a float
                linebuf.close();

                if (ret < min || ret > max)
                    throw new InputMismatchException();
                else
                    valid = true;
            }
            catch (NoSuchElementException e) {
                System.out.println("Value must be a decimal number between " + min + " and " + max);
            }
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

    //Returns today in MM-dd-yyy format
    private static String getDate()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        Date date = new Date();
        return sdf.format(date);
    }

    private static Patient validatePatient(Database db)
    {
        int id = getInt("Enter patient id: ", 0, 999999999);
        Vector<Entity> entity = db.getPatientByID(id);
        Patient p;

        if (entity.isEmpty()) {
            System.out.println("Patient " + id + " not found");
            return null;
        }

        p = (Patient)entity.elementAt(0);

        // See if patient is active and not suspended
        if (!p.getStatus()) {
            System.out.println("*** Member is no longer active ***\n" + p + "\n --> Consult ChocAn for details");
            p = null;
        }
        else if (!p.getFinancialStanding()) {
            System.out.println("*** Member Suspended ***\n" + p + "\n --> Consult ChocAn for details");
            p = null;
        }
        else {
            // is correct patient
            System.out.println(p);
            if(getConfirmation("Is this the correct patient?"))
                System.out.println("*** Validated ***");
            else
                p = null;
        }
        return p;
    }

    private static Service validateService(Database db)
    {
        int id = getInt("Enter service ID: ", 0, 999999);
        Vector<Service> v = db.getServiceByID(id);
        if (v.isEmpty()) {
            System.out.println("Could not find service");
            return null;
        }

        Service s = v.firstElement();
        System.out.println(s);
        if(getConfirmation("Is this the correct service?"))
            return s;
        else
            return null;
    }

}
