package cpu;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Created by Krishna Chaitanya Kandula on 2/20/17.
 */
public class CPUController {
    private static final String CLASS_PATH = "out/production/CPU-Memory-Simulator memory.MemoryController";
    public static void main (String... args){
        try
        {
            int x;
            Runtime rt = Runtime.getRuntime();

            Process proc = rt.exec("java -classpath " + CLASS_PATH);

            InputStream is = proc.getInputStream();
            OutputStream os = proc.getOutputStream();

            PrintWriter pw = new PrintWriter(os);
            pw.printf("Greg\n");
            pw.flush();

            Scanner sc = new Scanner(is);
            String line;

            while(sc.hasNext() && !(line = sc.nextLine()).isEmpty()) {
                System.out.println(line);
            }

            proc.waitFor();

            int exitVal = proc.exitValue();

            System.out.println("Process exited: " + exitVal);

        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
    }
}
