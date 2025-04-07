import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Processor {
    private Memory mem;
    public List<String> output = new LinkedList<>();

    private int PC; //program counter

    private Word16 current_instruction;

    private boolean fetch_flag;

    private int address;





    public Processor(Memory m) {
        mem = m;
        PC = 0;
        current_instruction = new Word16();
        fetch_flag = false;
        address = 0;
    }

    public void run() {
        while (true) { //loops through fetch decode execute store
            fetch();
            decode();
            execute();
            store();
            //TODO: add in logic that checks for a "halt" to run if found then break
        }

    }

    private void fetch() { //only read every 2 fetch calls

        if(PC % 2 == 0)
        {

            mem.read();
            if(PC != 0)
            {
                PC++;
            }
            fromInt(PC, mem.address);
            mem.value.getTopHalf(current_instruction);


        }
        else
        {
            PC++;
            mem.value.getBottomHalf(current_instruction);
        }
    }

    private void decode() {
    }

    private void execute() {
    }

    private void printReg() {
        for (int i = 0; i < 32; i++) {
            var line = "r"+ i + ":" + ""; // TODO: add the register value here...
            output.add(line);
            System.out.println(line);
        }
    }

    private void printMem() {
        for (int i = 0; i < 1000; i++) {
            Word32 addr = new Word32();
            Word32 value = new Word32();
            // Convert i to Word32 here...
            addr.copy(mem.address);
            mem.read();
            mem.value.copy(value);
            var line = i + ":" + value + "(" + TestConverter.toInt(value) + ")";
            output.add(line);
            System.out.println(line);
        }
    }

    private void store() {
    }


    private void fromInt(int value, Word32 result) {
        for(int i = 31; i >= 0; i--)
        {
            boolean Bit_val = (value & 1) == 1; //Determining the least significant bit of the int (ex 13 (1101) will isolate 1). this can then be checked

            result.setBitN(i, new Bit(Bit_val)); //Setting the bit in the same position as the one we are checking

            value >>= 1 ; //right shift the value to "drop" the LSB. allowing us to work with the next digit (ex 13 >> 1 = 0110) now what was the second digit
            //is the new LSB so on the next iteration we can determine the value to place in the correct position
        }
    }
}