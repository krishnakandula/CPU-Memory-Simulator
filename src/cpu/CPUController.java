package cpu;

import memory.MemoryCommands;
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
    private static int userSP = 999;
    private static int systemSP = 1999;
    private static int SP = userSP;
    private static int timer = 1;
    private static int timeout = 10;        //default
    private static boolean interruptMode = false;
    private static PrintWriter writer;
    private static Scanner scan;
    private static boolean kernelMode = false;    //false = user mode, true = kernel mode
    private static boolean userCall = false;
    private static boolean debug = false;

    public static void main (String... args){
        try {
            if(args.length != 2){
                System.out.println("Incorrect commandline arguments. Please consult the README file for instructions.");
                System.exit(1);
            }
            Runtime rt = Runtime.getRuntime();
            //args[0] should contain input file, args[1] should contain timeout
            timeout = Integer.parseInt(args[1]);
            String executeCommand = String.format("java -classpath %s %s", CLASS_PATH, args[0]);
            Process proc = rt.exec(executeCommand);

            InputStream is = proc.getInputStream();
            OutputStream os = proc.getOutputStream();

            writer = new PrintWriter(os);
            scan = new Scanner(is);

            while(true){
                if(timer != 0 && timer % timeout == 0 && !interruptMode){
                    pushSystemToStack();
                    PC = 1000;
                }
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
        if(address > SystemMemory.USER_MEMORY_BOUNDARY && !kernelMode) {
            System.out.println("ERROR: Attempting to read/write from protected memory.");
            System.exit(1);
        }
    }

    private static void runInstruction(){
        if(!kernelMode)             //Only increment timer if in user mode
            timer++;
        if(debug)
            printDebug();
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
                fetchInstructionToIR();     //Contains address
                AC = readFromMemory(IR + Y);
                break;
            case 6:
                //LoadSpX
                AC = readFromMemory(SP + X);
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
                    System.out.print("" + AC);
                else {
                    System.out.print((char) AC);
                }
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
                fetchInstructionToIR();
                PC = IR;
                break;
            case 21:
                //JumpIfEqual addr
                fetchInstructionToIR(); //get address
                if(AC == 0)
                    PC = IR;            //jump
                break;
            case 22:
                //JumpIfNotEqual addr
                fetchInstructionToIR(); //get address
                if(AC != 0)
                    PC = IR;            //jump
                break;
            case 23:
                //Call addr
                fetchInstructionToIR(); //get address
                pushToStack(PC);        //save current address to stack
                PC = IR;                //jump
                break;
            case 24:
                //Ret
                PC = popFromStack();
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
                if (!interruptMode) {
                    interruptMode = true;
                    userCall = true;
                    pushSystemToStack();
                    PC = 1500;
                }
                break;
            case 30:
                //IRet
                popSystemFromStack();
                if(!userCall)
                    timer = 0;      //reset timer
                userCall = false;
                break;
            case 50:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid Instruction");
                if(!debug)
                    printDebug();

                System.exit(0);
                break;
        }
    }

    private static void pushSystemToStack(){
        SP = systemSP;
        interruptMode = true;
        kernelMode = true;
        pushToStack(userSP);
        pushToStack(PC);
    }

    private static void popSystemFromStack() {
        PC = popFromStack();
        SP = popFromStack();
        userSP = SP;
        kernelMode = false;
        interruptMode = false;
    }

    private static void pushToStack(int data){
        //Write data to stack, then decrement stack pointer
        writeToMemory(SP, data);
        SP--;
        if(kernelMode)
            systemSP--;
        else
            userSP--;

    }

    private static int popFromStack(){
        //Increment stack pointer to get to last index written to, then read
        SP++;
        int data = readFromMemory(SP);
        if(kernelMode)
            systemSP++;
        else
            userSP++;
        return data;
    }

    private static int generateRandomInteger(){
        int min = 0;
        int max = 100;
        int randomNumber = (int) ((Math.random() * (max - min)) + min);
        return randomNumber;
    }

    private static void fetchInstructionToIR(){
        IR = readFromMemory(PC++);          //PC points to next instruction
    }

    private static void fetchInstructionToAC(){
        AC = readFromMemory(PC++);
    }

    private static void printDebug(){
        System.out.println("PC = " + PC);
        System.out.println("IR = " + IR);
        System.out.println("AC = " + AC);
        System.out.println("SP = " + SP);
        System.out.println("X = " + X);
        System.out.println("Y = " + Y);
        System.out.println("Timer = " + timer);
        System.out.println("-----------");
    }
}
