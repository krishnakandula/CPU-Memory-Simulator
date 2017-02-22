package memory;

import java.util.Scanner;

/**
 * Created by Krishna Chaitanya Kandula on 2/20/17.
 */
public class MemoryController {
    private static SystemMemory memory;
    public static void main(String... args){
        //Initial commands
        initialize();
//        read(MemoryCommands.READ + "0");
        try {
            Scanner sc = new Scanner(System.in);
            String line;
            while(sc.hasNext() && !(line = sc.nextLine()).isEmpty())
                doCommand(line);
        }
        catch (Throwable t){
            t.printStackTrace();
        }
    }

    /**
     *
     * @param input
     */
    private static void doCommand(String input){
        switch (input){
            //Initialize SystemMemory
            case MemoryCommands.INIT:
                initialize();
                break;
            case MemoryCommands.READ:
                read(input);
                break;
            default:
                System.exit(0);
        }
    }

    private static void initialize(){
        if(memory == null)
            memory = new SystemMemory();
        memory.initialize();
        System.out.println(0);
    }

    /**
     *
     * @param input
     */
    private static void read(String input){
        String address = input.substring(MemoryCommands.READ.length());
        int memoryAddress = SystemMemory.getIntFromString(address, 0);
        int data = memory.readFromMemory(memoryAddress);
        System.out.println("" + data);
    }

    private static void write(String input){

    }

    private static void write(int address, int data){

    }
}
