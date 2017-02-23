package cpu;

import memory.MemoryCommands;
import memory.MemoryController;
import memory.SystemMemory;

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
    private static int SP = 0;
    private static PrintWriter writer;
    private static Scanner scan;
    private static boolean kernelMode = false;    //False = user mode, True = kernel mode
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
        checkMode(address);
        String command = String.format("%s %d", MemoryCommands.READ, address);
        writer.println(command);
        writer.flush();

        return Integer.parseInt(scan.nextLine());
    }

    private static void writeToMemory(int address, int data){
        checkMode(address);
        String command = String.format("%s %d %d", MemoryCommands.WRITE, address, data);
        writer.println(command);
        writer.flush();
    }

    private static void checkMode(int address){
        if(address > SystemMemory.USER_MEMORY_BOUNDARY && !kernelMode)
            System.exit(1);
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
                //LoadIdxX addr
                fetchInstructionToIR();     //Contains address
                AC = readFromMemory(IR + X);
                break;
            case 5:
                //LoadIdxY addr
                break;
            case 7:
                //Store addr
                fetchInstructionToIR();     //Contains the address
                writeToMemory(IR, AC);
                break;
            case 8:
                //Get
                AC = generateRandomInteger();
                break;
            case 9:
                fetchInstructionToIR();
                if(IR == 1)
                    System.out.println("" + AC);
                else
                    System.out.println((char)AC);
                break;
            case 10:
                //AddX
                AC += X;
                break;
            case 11:
                //AddY
                AC += Y;
                break;
            case 12:
                //SubX
                AC -= X;
                break;
            case 13:
                //SubY
                AC -= Y;
                break;
            case 14:
                //CopyToX
                X = AC;
                break;
            case 15:
                //CopyFromX
                AC = X;
                break;
            case 16:
                //CopyToY
                Y = AC;
                break;
            case 17:
                //CopyFromY
                AC = Y;
                break;
            case 18:
                //CopyToSp
                SP = AC;
                break;
            case 19:
                //CopyFromSp
                AC = SP;
                break;
            case 20:
                //Jump addr
                jumpToAddress();
                break;
            case 21:
                //JumpIfEqual addr
                if(AC == 0)
                    jumpToAddress();
                break;
            case 22:
                //JumpIfNotEqual addr
                if(AC != 0)
                    jumpToAddress();
                break;
            case 23:
                //Call addr
                jumpToAddress();
                PC++;
                pushToStack(PC);
                break;
            case 24:
                //Ret
                int returnAddress = popFromStack();
                PC = returnAddress;
                break;
            case 25:
                //IncX
                X++;
                break;
            case 26:
                //DecX
                X--;
                break;
            case 27:
                //Push
                pushToStack(AC);
                break;
            case 28:
                //Pop
                AC = popFromStack();
                break;
            case 29:
                //Int
                kernelMode = true;
                pushToStack(++PC);
                pushToStack(AC);
                pushToStack(X);
                pushToStack(Y);
                pushToStack(SP);
                break;
            case 30:
                //IRet
                SP = popFromStack();
                Y = popFromStack();
                X = popFromStack();
                AC = popFromStack();
                PC = popFromStack();
            case 50:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid Instruction");
                System.exit(0);
                break;
        }
    }

    private static void jumpToAddress(){
        fetchInstructionToIR();
        PC = IR;
    }

    private static void pushToStack(int data){
        writeToMemory(SystemMemory.USER_MEMORY_BOUNDARY - SP, data);
        SP++;
    }

    private static int popFromStack(){
        int data = readFromMemory(SystemMemory.USER_MEMORY_BOUNDARY - SP);
        SP--;
        return data;
    }

    private static int generateRandomInteger(){
        int min = 0;
        int max = 100;
        int randomNumber = (int) ((Math.random() * (max - min)) + min);
        return randomNumber;
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
