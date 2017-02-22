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
    private static int PC = 0;
    private static int X = 0;
    private static int Y = 0;
    private static PrintWriter writer;
    private static Scanner scan;

    public static void main (String... args){
        try {
            Runtime rt = Runtime.getRuntime();

            Process proc = rt.exec("java -classpath " + CLASS_PATH);

            InputStream is = proc.getInputStream();
            OutputStream os = proc.getOutputStream();

            writer = new PrintWriter(os);
            scan = new Scanner(is);

            while(true){
//                System.out.println("PC = " + PC);
                fetchInstructionToIR();
                runInstruction();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int readFromMemory(int address){
        String command = String.format("%s %d", MemoryCommands.READ, address);
        writer.println(command);
        writer.flush();

        return Integer.parseInt(scan.nextLine());
    }

    private static void writeToMemory(int address, int data){
        String command = String.format("%s %d %d", MemoryCommands.WRITE, address, data);
        writer.println(command);
        writer.flush();
    }

    private static void runInstruction(){
        switch (IR){
            case 1:
                fetchInstructionToAC();
                break;
            case 2:
                fetchInstructionToAC();
                AC = readFromMemory(AC);
                break;
            case 3:
                //LoadInd addr
                fetchInstructionToAC();
                AC = readFromMemory(AC);
                AC = readFromMemory(AC);
                break;
            case 4:

                break;
            case 7:
                fetchInstructionToIR();     //Contains the address
                writeToMemory(IR, AC);
                break;
            case 9:
                fetchInstructionToIR();
                if(IR == 1)
                    System.out.println("" + AC);
                break;
            case 50:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid Instruction");
                System.exit(0);
                break;
        }
    }

    private static void fetchInstructionToIR(){
        IR = readFromMemory(PC++);
//        System.out.println("IR = " + IR);
    }

    private static void fetchInstructionToAC(){
        AC = readFromMemory(PC++);
//        System.out.println("AC = " + AC);
    }
}
