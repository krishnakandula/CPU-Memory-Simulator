package memory;

import java.util.Scanner;

/**
 * Created by Krishna Chaitanya Kandula on 2/20/17.
 */
public class MemoryController {
    public static void main(String... args){
        System.out.println("test");
        try
        {
            Scanner sc = new Scanner(System.in);
            String line = sc.nextLine();

            for (int i=0; i<line.length(); i++)
                System.out.println(":)");
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
    }


}
