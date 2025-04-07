import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Processor {
    private Memory mem;
    public List<String> output = new LinkedList<>();

    private ALU alu;

    private int PC; //program counter


    private Word16 current_instruction;


    private boolean halt_found;



    private Word32 op1;
    private Word32 op2;

    Bit instruction_cycle;





    public Processor(Memory m) {
        mem = m;
        PC = 0;
        current_instruction = new Word16();
        halt_found = false;
        instruction_cycle = new Bit(false);
        op1 = new Word32();
        op2 = new Word32();
        alu = new ALU();
    }

    public void run() {
        while (true) { //loops through fetch decode execute store
            fetch();
            decode();
            execute();
            store();
            if(halt_found) {
                break;
            }
            //TODO: add in logic that checks for a "halt" to run if found then break
        }

    }

    private void fetch() { //only read every 2 fetch calls

        if(instruction_cycle.getValue() == Bit.boolValues.FALSE)
        {

            mem.read();
            mem.value.getTopHalf(current_instruction);



        }
        else
        {
            mem.value.getBottomHalf(current_instruction);
            PC++;
        }
        instruction_cycle.not(instruction_cycle);
    }

    private void decode() {
        //check between call return and 2r/immediate
        int opcode = getOpcode();
        //


        switch (opcode) {
            case 1, 2, 3, 4, 5, 6, 7, 11, 19, 20 -> { //ALU methods

            }
            case 0,10, 12, 13, 14, 15, 16, 17, 18 -> {

            }

        }

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


    public int getOpcode() { //converting the opcode to int
        int opcode_as_int = 0;
        int pow = 0;

        // Iterate over the first 5 bits (bits 0 to 4) of the instruction
        for (int i = 4; i >= 0; i--) {
            Bit cur_bit = new Bit(false);
            current_instruction.getBitN(i, cur_bit); // Get bit i from the instruction

            if (cur_bit.getValue() == Bit.boolValues.TRUE) {
                opcode_as_int |= (1 << pow); // Add to opcode integer using bitwise OR
            }
            pow++; // Increment the power for next bit
        }

        if(opcode_as_int > 20 || opcode_as_int < 0){
            throw new IllegalArgumentException("invalid instruction provided for ALU");
        }

        return opcode_as_int;
    }

}