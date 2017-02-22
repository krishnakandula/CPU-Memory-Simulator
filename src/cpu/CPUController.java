package cpu;

import memory.MemoryCommands;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Created by Krishna Chaitanya Kandula on 2/20/17.
 */
public class CPUController {
    private static final String CLASS_PATH = "out/production/CPU-Memory-Simulator memory.MemoryController";
    private static int IR;
    private static int AC;

    public static void main (String... args){
        try {
            Runtime rt = Runtime.getRuntime();

            Process proc = rt.exec("java -classpath " + CLASS_PATH);

            InputStream is = proc.getInputStream();
            OutputStream os = proc.getOutputStream();

            //Initialize memory
            PrintWriter pw = new PrintWriter(os);

            Scanner sc = new Scanner(is);
            String line;
            while(sc.hasNext() && !(line = sc.nextLine()).isEmpty()) {
                IR = Integer.parseInt(line);
                System.out.println("" + IR);
                switch (IR){
                    case 50:
                        System.exit(0);
                        break;
                    default:
                        System.out.println("hi");
                        System.exit(0);
                        break;
                }
            }

            proc.waitFor();

            int exitVal = proc.exitValue();

            System.out.println("Process exited: " + exitVal);

        }
        catch (Throwable t) {
            t.printStackTrace();
        }
    }

}
