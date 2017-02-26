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
     * Performs the proper command given the input from the CPU
     * @param command The command to be performed
     * @param address The location in memory to be read/written
     * @param data The data needed to be written
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
     * Initializes the system memory
     */
    private static void initialize(String inputFile){
        if(memory == null)
            memory = new SystemMemory(inputFile);
        memory.initialize();
//        memory.printMemory();
    }

    /**
     * Formats the input to retrieve the command and data
     * @param input the input from the CPU
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
     * Reads data from system memory
     * @param address the location in memory where data needs to be read
     */
    private static int read(int address){
        return memory.readFromMemory(address);
    }

    /**
     * Writes data to system memory
     * @param address the location where the data needs to be written
     * @param data the data to be written
     */
    private static void write(int address, int data){
        memory.writeToMemory(address, data);
    }
}
