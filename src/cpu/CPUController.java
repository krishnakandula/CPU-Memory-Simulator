package cpu;

import memory.MemoryCommands;

import java.io.IOException;
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
    private static int PC = -1;
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
            while(sc.hasNext() && !(line = sc.nextLine()).isEmpty()){
                pw.printf(MemoryCommands.READ);
                pw.flush();
                IR = Integer.parseInt(line);
                System.out.println("" + IR);
                runInstruction(sc, pw);

            }

            proc.waitFor();

            int exitVal = proc.exitValue();

            System.out.println("Process exited: " + exitVal);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void runInstruction(Scanner scan, PrintWriter pw){
        switch (IR){
            case 1:
                AC = getNextInstruction(scan, pw);
                break;
            case 50:
                System.exit(0);
                break;
            default:
                System.out.println("hi");
//                System.exit(0);
                break;
        }
    }

    private static int getNextInstruction(Scanner scan, PrintWriter pw) {
        pw.printf(MemoryCommands.READ + PC);
        pw.flush();
        String line;
        int data = Integer.MIN_VALUE;
        if (scan.hasNext()) {
            line = scan.nextLine();
            data = Integer.parseInt(line);
        }
        PC++;
        return data;
    }
}
