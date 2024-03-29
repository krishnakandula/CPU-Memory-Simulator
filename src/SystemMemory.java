import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Krishna Chaitanya Kandula on 2/20/17.
 */
public class SystemMemory {
    private static int[] memory;
    protected static final int MEMORY_CAPACITY = 2000;
    public static final int USER_MEMORY_BOUNDARY = 999;
    private static String INPUT_FILE_PATH = new File("")
            .getAbsolutePath()
            .concat("/");

    protected SystemMemory(String inputFile){
        memory = new int[MEMORY_CAPACITY];
        INPUT_FILE_PATH = INPUT_FILE_PATH.concat(inputFile);
    }

    /**
     * Prints the memory for debug purposes
     */
    protected void printMemory(){
        System.out.println(Arrays.toString(memory));
    }

    /**
     * Writes data to memory
     * @param address The location where data needs to be written
     * @param data The data that needs to be written
     */
    protected void writeToMemory(int address, int data){
        memory[address] = data;
    }

    /**
     * Reads data from memory
     * @param address the location where data needs to be read from
     * @return the data in the memory cell
     */
    protected int readFromMemory(int address){
        return memory[address];
    }

    /**
     * Initializes memory using data read in from file
     */
    protected void initialize(){
        FileReader fileReader = null;
        BufferedReader reader = null;

        try{
            fileReader = new FileReader(INPUT_FILE_PATH);
            reader = new BufferedReader(fileReader);
            String line;
            int lastAddedIndex = 0;
            while((line = reader.readLine()) != null)
                lastAddedIndex = initializeMemoryIndex(line, lastAddedIndex);

        } catch (Exception e){
            System.out.println(e.getMessage());
        } finally {
            try {
                if(fileReader != null)
                    fileReader.close();
                if(reader != null)
                    reader.close();
            } catch (IOException e){
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Adds each line from file to memory based on input conditions.
     *
     * @param input   the instruction from the input file.
     * @param address the address in memory to add the instruction to.
     * @return the last index that an input was added to.
     */
    private int initializeMemoryIndex(String input, int address) {
        //Check if line is blank
        if (input.isEmpty())
            return address;

        //Check for space
        char space = ' ';
        if(input.charAt(0) == space)
            return address;

        //Check for comment
        char comment = '/';
        if (input.charAt(0) == comment && input.charAt(1) == comment)
            return address;

        //Check for period
        char period = '.';
        if (input.charAt(0) == period)
            //Get next load address
            return getIntFromString(input, 1);

        //Only option left is an integer
        memory[address] = getIntFromString(input, 0);
        return address + 1;
    }

    /**
     * Gets the numeric value of a String, making sure that non-numeric characters aren't included.
     *
     * @param input The String to extract an integer from.
     * @param start The starting index to extract the integer.
     * @return The numeric value of the String.
     */
    public static int getIntFromString(String input, int start) {
        //Loop through String and add each digit to the total
        int end = start;
        while (end < input.length() && Character.isDigit(input.charAt(end)))
            end++;
        if (end >= input.length())
            return Integer.parseInt(input.substring(start));
        else
            return Integer.parseInt(input.substring(start, end));

    }
}
