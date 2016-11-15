import java.util.Scanner;
import java.util.jar.Pack200;
import java.util.InputMismatchException;
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

        /*
        create database - check validity
         */

        // run terminal VM
        final String terminalMenu =   "@MainMenu \n" + "" +
                                        "Options: (1) Manager, (2) Operator, (3) Provider, (4) Quit \n";
        final String userPrompt = "Enter option: ";
        int tmMax = 4;
        int tmMin = 1;
        int userOption = 0;

        System.out.print(terminalMenu);
        while (userOption != 4){
            userOption = getInt(userPrompt,tmMin, tmMax);

            switch(userOption) {
                case 1: System.out.println("Entering Manager Terminal...." + '\n');
                        managerTerminal();
                        break;
                case 2: System.out.println("Operator Terminal:" + '\n');
                        break;
                case 3: System.out.println("Provider Terminal:" + '\n');
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
     * @param dbWrapper
     */
    private static void managerTerminal(){
        /* Alternate menu
        final String terminalMenu = "Manager Terminal Menu: \n(1) List Services, (2) Add Services, (3) Update Services, (4) Delete Services \n" +
                                    "(5) Print EFT Report, (6) Print Summary Report, (7) Print Patient Report, (8) Print Provider Report";
        */
        final String managerMenu =  "@Manager Terminal \n" +
                                    "For Services: (1) List, (2) Add,     (3) Update,  (4) Delete \n" +
                                    "For Reports:  (5) EFT,  (6) Summary, (7) Patient, (8) Provider \n" +
                                    "Other:        (9) Exit Manger Terminal \n";
        final String prompt = "Enter option: ";
        int mmMax = 9;
        int mmMin = 1;
        int option = 0;


        System.out.println(managerMenu);
        while (option != 9){

            option = getInt(prompt, mmMin, mmMax);

            switch(option){
                case 1: //List services
                        break;
                case 2: //Add service
                        System.out.println("Adding service...");
                        Service service = getService();
                        /* CALL DB TO ADD SERVICE
                        *  GET ID IN RETURN
                        *  PRINT TO USER*/
                        break;
                case 3: //Update service
                       // System.out.println("Please enter the new ");
                        break;
                case 4: //Delete service
                        break;
                case 5: //EfT
                        break;
                case 6: //Summary
                        break;
                case 7: //Patient report
                        break;
                case 8: //Provider report
                        break;
                case 9: //quit...
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
    private static void providerTerminal(){


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
     * @param Float minimum allowable value
     * @param Float maximum allowable value
     */
    private static Float getFloat(String prompt, Float min, Float max)
    {
        Float ret = -1f;
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
