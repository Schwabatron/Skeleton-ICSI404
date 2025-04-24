import java.util.*;

public class Processor {

    //cache:
    /*
    300 cycles whenever you access memory
    10 cycles for multiplication
    2 cycles for addition/subtraction and all other alu functions
     */

    public int cur_clock_cycle = 0; //current clock cycle to keep track of how many clock cycles are done for each instruction

    private Set<Integer> call_return_opcodes;
    private Memory mem;
    public List<String> output = new LinkedList<>();
    private ALU alu;
    private int PC; //program counter
    private Word16 current_instruction;
    private boolean halt_found;
    private Word32 op1;
    private Word32 op2;
    Bit instruction_cycle;
    Word32[] registers;
    private Stack<Integer> callStack;
    private int current_opcode;
    private boolean PCmodified;

    private InstructionCache cache;
    private L2Cache l2cache;


    public Processor(Memory m) {
        mem = m; //memory
        PC = 0; //PC
        current_instruction = new Word16(); //the current instruction were working with
        halt_found = false; //flag for when a halt is found
        instruction_cycle = new Bit(true); //Bit for knowing when to read a new instruction
        op1 = new Word32(); //op1 for alu
        op2 = new Word32(); //op2 for alu
        alu = new ALU(); //ALU
        registers = new Word32[32]; //array to hold the registers
        for(int i = 0; i < 32; i++) { //init registers to 0
            registers[i] = new Word32();
        }
        callStack = new Stack<>(); //stack
        call_return_opcodes = new HashSet<>(){{
            add(8);
            add(9);
            add(10);
            add(12);
            add(13);
            add(14);
            add(15);
            add(16);
            add(17);
        }}; //hashset for call_return opcodes
        PCmodified = false;
        l2cache = new L2Cache();
        cache = new InstructionCache(l2cache);
    }

    public void run() {
        while (true) { //loops through fetch decode execute store
            PCmodified = false;
            op1 = new Word32();
            op2 = new Word32();
            fetch();
            decode();
            execute();
            store();
            setAddress();
            if(halt_found) {
                break;
            }
        }

    }

    private void fetch() { //only read every 2 fetch calls
        if(instruction_cycle.getValue() == Bit.boolValues.TRUE)
        {
            mem.read(); cur_clock_cycle += 300; //memory access
            mem.value.getTopHalf(current_instruction);
        }
        else
        {
            mem.value.getBottomHalf(current_instruction);
        }
        instruction_cycle.not(instruction_cycle);
    }

    private void decode() { //get everything ready
        //check between call return and 2r/immediate
        current_opcode = getOpcode();
        System.out.println("Opcode: " + current_opcode);
        if(call_return_opcodes.contains(current_opcode)) {
            for(int i = 15; i >= 5; i--)
            {
                Bit temp = new Bit(true);
                current_instruction.getBitN(i, temp);
                op1.setBitN(i+16, temp);
            }

            Bit signBit = new Bit(false);
            current_instruction.getBitN(5, signBit);

            if (signBit.getValue() == Bit.boolValues.TRUE) {
                for (int i = 0; i < 21; i++) {
                    op1.setBitN(i, new Bit(true)); // Sign-extend with 1s
                }
            }
        }
        else
        {
            Bit flag = new Bit(false);
            current_instruction.getBitN(5, flag);
            if(flag.getValue() == Bit.boolValues.TRUE) //immediate
            {
                for(int i = 10; i >= 6; i--)
                {
                    Bit temp = new Bit(true);
                    current_instruction.getBitN(i, temp);
                    op2.setBitN(i+21, temp);
                }

                Bit sign = new Bit(false);
                current_instruction.getBitN(6, sign);

                if (sign.getValue() == Bit.boolValues.TRUE) {
                    for (int i = 0; i < 27; i++) {
                        op2.setBitN(i, sign);  // fill lower bits with 1s
                    }
                }


                int reg_index = getRegister2();

                registers[reg_index].copy(op1); //getting the value in the second register

            }
            else { //2R
                int[] register_indices = registers_as_int();
                int first_register = register_indices[0];
                int second_register = register_indices[1];
                registers[second_register].copy(op1);
                registers[first_register].copy(op2);
            }
        }



        switch (current_opcode) {
            case 1, 2, 3, 4, 5, 6, 7, 11 -> { //ALU methods (need to set the alu members in order to run do instruction in execute
                op1.copy(alu.op1);
                op2.copy(alu.op2);
                current_instruction.copy(alu.instruction);
            }
            case 0 ->
            {
                halt_found = true;
            }
        }
    }

    private void execute() {

        switch(current_opcode) {
            case 1, 2, 4, 5, 6, 7 -> { //if its an opcode that is handed in the alu then just call the alu method doinstruction
                alu.doInstruction(); cur_clock_cycle += 2; //alu general instruction
            }
            case 3 ->
            {
                alu.doInstruction(); cur_clock_cycle += 10; //multiplication
            }
            case 11 -> { //compare
                alu.doInstruction(); cur_clock_cycle += 2; //alu general instruction
            }
            case 8 ->{ //Syscall: switched to kernel and called kernel function 0 -> print registers, 1 -> print memory
                int immediate = getImmediate(); //gets the immediate
                if(immediate == 0)
                {
                    printReg();
                }else if(immediate == 1)
                {
                    printMem();
                }
                else
                {
                    throw new IllegalArgumentException("unknown kernel function for immediate value: " + immediate);
                }
            }
            case 9 -> { //Call: pushes the current PC + 1 on the stack and sets pc equal to pc + immediate
                int immediate = getImmediate();
                callStack.push(PC+1);
                PC += immediate;
                PCmodified = true;
            }
            case 10 -> { //Return: pops from the stack and sets the popped value
                PC = callStack.pop();
                PCmodified = true;
                instruction_cycle.not(instruction_cycle);
            }
            case 12 -> { //BLE: if status less or equal are set then set PC to PC + immediate
                if(alu.equal.getValue() == Bit.boolValues.TRUE || alu.less.getValue() == Bit.boolValues.TRUE)
                {
                    int immediate = getImmediate();
                    PC += immediate;
                    PCmodified = true;
                }
            }
            case 13 -> { //BLT: if status less is set then PC = PC + immediate
                if(alu.less.getValue() == Bit.boolValues.TRUE)
                {
                    int immediate = getImmediate();
                    PC += immediate;
                    PCmodified = true;
                }
            }
            case 14 -> { //BGE: if status greater or equal is set then pc = pc + immediate
                if(alu.equal.getValue() == Bit.boolValues.TRUE || alu.less.getValue() == Bit.boolValues.FALSE)
                {
                    int immediate = getImmediate();
                    PC += immediate;
                    PCmodified = true;
                }
            }
            case 15 -> { //BGT: if status greater is set then pc = pc + immediate
                if(alu.equal.getValue() == Bit.boolValues.FALSE && alu.less.getValue() == Bit.boolValues.FALSE)
                {
                    int immediate = getImmediate();
                    PC += immediate;
                    PCmodified = true;
                }
            }
            case 16 -> { //BEQ: if equal is true then pc = pc + immediate
                if(alu.equal.getValue() == Bit.boolValues.TRUE)
                {
                    int immediate = getImmediate();
                    PC += immediate;
                    PCmodified = true;
                }
            }
            case 17 -> { //BNE: if status equal is not set then pc = pc + immediate
                if(alu.equal.getValue() == Bit.boolValues.FALSE)
                {
                    int immediate = getImmediate();
                    PC += immediate;
                    PCmodified = true;
                }
            }
        }

    }

    private void printReg() { //syscall 0
        for (int i = 0; i < 32; i++) {
            var line = "r"+ i + ":" + registers[i].toString(); // TODO: add the register value here...
            output.add(line);
            System.out.println(line);
        }
    }

    private void printMem() { //syscall 1
        for (int i = 0; i < 1000; i++) {
            //Word32 addr = new Word32();
            Word32 value = new Word32();
            Word32 i_addr = bit_string(i);
            // Convert i to Word32 here...
            i_addr.copy(mem.address);
            mem.read(); cur_clock_cycle += 300; //memory access
            mem.value.copy(value);
            var line = i + ":" + value; //+ "(" + TestConverter.toInt(value) + ")";
            output.add(line);
            System.out.println(line);
        }
    }

    private void store() {
        //move stuff around
        switch (current_opcode) {
            case 1, 2, 3, 4, 5, 6, 7 -> { //moving alu result into destination register
                int destination = getRegister2();
                alu.result.copy(registers[destination]);
            }
            case 18 -> { //Load 2R/immediate **need to add things to handle negatives
                Bit temp = new Bit(false);
                current_instruction.getBitN(5, temp);
                if(temp.getValue() == Bit.boolValues.TRUE) //immediate
                {
                    Word32 backup = new Word32();
                    mem.value.copy(backup);
                    Word32 addr = new Word32();
                    Adder.add(op1, op2, addr);
                    addr.copy(mem.address); //setting the memory address to immediate + the register value
                    mem.read(); cur_clock_cycle += 300; //memory access
                    mem.value.copy(op1);
                    int destination = getRegister2();
                    op1.copy(registers[destination]);
                    backup.copy(mem.value); //setting value back
                }
                else //2R
                {
                    Word32 backup = new Word32();
                    mem.value.copy(backup);
                    op2.copy(mem.address);
                    mem.read(); cur_clock_cycle += 300; //memory access
                    mem.value.copy(op1);
                    int destination = getRegister2();
                    op1.copy(registers[destination]);
                    backup.copy(mem.value); //setting value back
                }
            }
            case 19 -> { //store 2R/immediate
                Word32 backup = new Word32();
                mem.value.copy(backup);
                op1.copy(mem.address);
                op2.copy(mem.value);
                mem.write(); cur_clock_cycle += 300; //memory access
                backup.copy(mem.value);
            }
            case 20 -> { //Copy 2R/immediate
                op2.copy(op1);
                int destination = getRegister2();
                op1.copy(registers[destination]);
            }

        }

        if(!PCmodified && instruction_cycle.getValue() == Bit.boolValues.TRUE)
        {
            PC++;
        }
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

    public int[] registers_as_int() {
        int register_1 = 0;
        int register_2 = 0;
        int pow = 0;

        for (int i = 10; i >= 6; i--) {
            Bit cur_bit = new Bit(false);
            current_instruction.getBitN(i, cur_bit);
            if (cur_bit.getValue() == Bit.boolValues.TRUE) {
                register_1 |= (1 << pow);
            }
            pow++;
        }

        // Reset power for register_2

        // Extract register_2: bits 11 to 15 (inclusive)
        register_2 = getRegister2();

        return new int[] { register_1, register_2 };

    }

    private int getRegister2() {
        int register_2 = 0;
        int pow = 0;
        for (int i = 15; i >= 11; i--) {
            Bit cur_bit = new Bit(false);
            current_instruction.getBitN(i, cur_bit);
            if (cur_bit.getValue() == Bit.boolValues.TRUE) {
                register_2 |= (1 << pow);
            }
            pow++;
        }
        return register_2;
    }

    private int getImmediate()
    {

        int immediate = 0;

        for (int i = 15; i >= 5; i--) {
            Bit cur_bit = new Bit(false);
            current_instruction.getBitN(i, cur_bit);
            if (cur_bit.getValue() == Bit.boolValues.TRUE) {
                immediate |= (1 << (15 - i));
            }
        }


        Bit signBit = new Bit(false);
        current_instruction.getBitN(5, signBit);
        if (signBit.getValue() == Bit.boolValues.TRUE) {
            // If negative, sign-extend to 32 bits
            immediate |= -(1 << 11);
        }

        return immediate;
    }

    private void setAddress(){
        Word32 new_address = bit_string(PC);
        new_address.copy(mem.address);
    }


    private Word32 bit_string(int number)
    {

        Word32 temp = new Word32();
            for (int i = 31; i >= 0; i--) {
                boolean bitVal = ((number) & 1) == 1;

                if (bitVal) {
                    temp.setBitN(i, new Bit(true));
                } else {
                    temp.setBitN(i, new Bit(false));
                }
                number >>= 1;
            }
        return temp;
    }





}