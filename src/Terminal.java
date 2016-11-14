package src;
import java.util.Scanner;
import java.util.jar.Pack200;

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

-
*/
//

/**
 * Created by andykeene on 11/10/16.
 */
public class Terminal {

    static Scanner sc = new Scanner(System.in);

    public static void main (String [] args){

        /*
        create database - check validity
         */

        // run terminal VM
        final String TerminalPrompt = "Menu: (1) Manager, (2) Operator, (3) Provider, (4) Quit";
        int userOption = 0;

        while (userOption != 4){
            userOption = menu(TerminalPrompt);

            switch(userOption) {
                case 1: System.out.println("Manager Terminal:" + '\n');
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
     * Uses static scanner to take user input for prompt - No error checking done
     *
     */
    private static int menu(final String prompt){
        int option = 0;

        System.out.println(prompt);

        if(sc.hasNextInt()){
            option = sc.nextInt();
            sc.nextLine();

        } else {
            sc.nextLine();
        }


        return option;
    }
    private static void managerTerminal(){

        final String menuPrompt = "Menu: (1) "


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

}
