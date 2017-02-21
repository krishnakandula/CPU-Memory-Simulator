package memory;

/**
 * Created by Krishna Chaitanya Kandula on 2/20/17.
 */
public class MemoryController {
    public static void main(String... args){
        SystemMemory memory = new SystemMemory();
        memory.initialize();
//        memory.printMemory();
    }

    private static int read(int address){
        return 0;
    }

    private static void write(int address, int data){
        return;
    }
}
