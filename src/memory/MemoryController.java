package memory;

import java.util.Scanner;

/**
 * Created by Krishna Chaitanya Kandula on 2/10/17.
 */
public class MemoryController {
    private static SystemMemory memory;
    public static void main(String... args){
        try {
            Scanner sc = new Scanner(System.in);
            //Initial commands
            initialize(args[0]);
            String line;
            while(sc.hasNextLine()) {
                line = sc.nextLine();
                getCommandFromInput(line);
            }
        }
        catch (Throwable t){
            t.printStackTrace();
        }
    }

    /**
     *
     * @param command
     */
    private static void doCommand(String command, int address, int data){
        switch (command){
            case MemoryCommands.READ:
                System.out.println(read(address));
                break;
            case MemoryCommands.WRITE:
                write(address, data);
                break;
            default:
                System.exit(0);
        }
    }

    /**
     *
     */
    private static void initialize(String inputFile){
        if(memory == null)
            memory = new SystemMemory(inputFile);
        memory.initialize();
//        memory.printMemory();
    }

    /**
     *
     * @param input
     */
    private static void getCommandFromInput(String input){
        //Default values for data and address
        int data = 0;
        int address = 0;
        String[] parameters = input.split(" ");
        if (parameters.length > 1)
            address = Integer.parseInt(parameters[1]);
        if(parameters.length > 2)
            data = Integer.parseInt(parameters[2]);
        doCommand(parameters[0], address, data);
    }

    /**
     *
     * @param address
     */
    private static int read(int address){
        return memory.readFromMemory(address);
    }

    private static void write(int address, int data){
        memory.writeToMemory(address, data);
    }
}
