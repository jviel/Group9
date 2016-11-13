package src;
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

-
*/


/**
 * Created by andykeene on 11/10/16.
 */
public class Terminal {

    static Scanner sc = new Scanner(System.in);

    public static void main (String [] args){

        //Here we should create the database and add parameters to terminals
        System.out.println("Hey andy");
        managerTerminal();

        int x = 0;
        while (true) {
            x = getInt("please enter an int: ", 0, 25);
            System.out.println(x);
        }
    }


    /**
     * Description
     *
     */

    private static void managerTerminal(){

        System.out.println("In maanger terminal");

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

    private static String getString(String prompt, int min, int max)
    {

        return "";
    }

    private static int getInt(String prompt, int min, int max) throws InputMismatchException
    {
        int ret = -1;
        boolean valid = false;

        while(!valid)
        {
            try {
                System.out.print(prompt);
                ret = sc.nextInt(); // throws InputMismatchException if not an int
                if (ret < min || ret > max) {
                    throw new InputMismatchException();
                } else {
                    valid = true;
                }
            }
            catch (InputMismatchException ex) {
                System.out.println("Value must be a number between " + min + " and " + max);
                sc.nextLine();
            }
        }
        return ret;
    }

    private static Float getFloat()
    {

        return 0f;
    }
}
