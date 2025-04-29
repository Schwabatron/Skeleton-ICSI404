import java.util.*;

public class Processor {
    private Memory mem;
    public List<String> output = new LinkedList<>();

    private boolean halt_found = false; //boolean flag to keep track if a halt instruction was encountered

    private boolean Instruction_Read_Cycle = true;

    private boolean PC_changed = false;

    private Word32 Program_Counter = new Word32(); //A word 32 program counter that will keep track of the program position in main memory

    private Word16 Current_Instruction = new Word16(); //A word 16 that holds the current instruction we are working with

    private Word32 Op1 = new Word32(); // A word32 that will hold the operators necessary to complete the instruction
    private Word32 Op2 = new Word32(); // A word32 that will hold the operators necessary to complete the instruction

    private Word32[] Registers = new Word32[32];

    private int opcode; //an int to keep track of the current opcode

    private Stack<Word32> Call_Stack = new Stack<>(); //a stack to keep track of call returns

    private ALU alu = new ALU();



    public Processor(Memory m) {
        mem = m; //initialize memory

        for(int i = 0; i < Registers.length; i++) { //Initialize the registers
            Registers[i] = new Word32();
        }
    }

    public void run() {
        while(true) { //infinite loop (until halt is called)
            Op1 = new Word32();
            Op2 = new Word32();
            PC_changed = false;
            fetch();
            decode();
            execute();
            store();
            Program_Counter.copy(mem.address);
            if(halt_found) { //If a halt was found, then break the loop and "end" instructions
                break;
            }
        }
    }

    private void fetch() {
        if(Instruction_Read_Cycle) {
            mem.read(); //Reading from memory
            mem.value.getTopHalf(Current_Instruction); //Copying the first instruction into the current instruction
        }
        else {
            mem.value.getBottomHalf(Current_Instruction); //Copying the second instruction into current instruction
        }
        Instruction_Read_Cycle = !Instruction_Read_Cycle; //Flipping the Instruction read cycle flag to prevent reading every cycle
    }

    private void decode() {
        opcode = Converter(4, 0);
        if(opcode != 0) {
            populateOp1();
            populateOp2();
        }
    }

    private void execute() {
        Word32 temp_PC = new Word32();
        Program_Counter.copy(temp_PC); //saving a copy of PC to check against PC after execution to see if flags need to be set

        switch(opcode) {
            case 0 -> halt_found = true; //halt
            case 1,2,3,4,5,6,7,11 -> { // if the opcode is one that is handled by the alu then set the operators and have the alu do the instruction
                Current_Instruction.copy(alu.instruction);
                Op1.copy(alu.op1);
                Op2.copy(alu.op2);
                alu.doInstruction();
            }
            case 8 -> { //SysCall
                Bit Syscall = new Bit(false);
                Current_Instruction.getBitN(15, Syscall);
                if(Syscall.getValue().equals(Bit.boolValues.TRUE)) {
                    printMem();
                }
                printReg();
            }
            case 9 -> { //Call
                Word32 Pushed_address = new Word32();
                Program_Counter.copy(Pushed_address);
                Pushed_address.Increment();
                Call_Stack.push(Pushed_address); //Add the program counter + 1 to the call stack
                Adder.add(Program_Counter, Op2, Program_Counter); //Set program counter equal to PC + immediate
            }
            case 10 -> { //Return
                Call_Stack.pop().copy(Program_Counter); //set the program counter to the popped value
            }
            case 12 -> { //BLE
                if(alu.less.getValue().equals(Bit.boolValues.TRUE) || alu.equal.getValue().equals(Bit.boolValues.TRUE)) {
                    Adder.add(Program_Counter, Op2, Program_Counter);
                }
            }
            case 13 -> { //BLT
                if(alu.less.getValue().equals(Bit.boolValues.TRUE)) {
                    Adder.add(Program_Counter, Op2, Program_Counter);
                }
            }
            case 14 -> { //BGE
                if(alu.less.getValue().equals(Bit.boolValues.FALSE) || alu.equal.getValue().equals(Bit.boolValues.TRUE)) {
                    Adder.add(Program_Counter, Op2, Program_Counter);
                }
            }
            case 15 -> { //BGT
                if(alu.less.getValue().equals(Bit.boolValues.FALSE) && alu.equal.getValue().equals(Bit.boolValues.FALSE)) {
                    Adder.add(Program_Counter, Op2, Program_Counter);
                }
            }
            case 16 -> { //BEQ
                if(alu.equal.getValue().equals(Bit.boolValues.TRUE)) {
                    Adder.add(Program_Counter, Op2, Program_Counter);
                }
            }
            case 17 -> { //BNE
                if(alu.equal.getValue().equals(Bit.boolValues.FALSE)) {
                    Adder.add(Program_Counter, Op2, Program_Counter);
                }
            }
        }
        if(!Program_Counter.equals(temp_PC))
        {
            PC_changed = true;
            Instruction_Read_Cycle = true;
        }
    }

    private void store() {
        int Destination = Converter(15, 11);
        switch(opcode) {
            case 1,2,3,4,5,6,7 -> {
                alu.result.copy(Registers[Destination]);
            }
            case 18 -> {
                Word32 Value_copy = new Word32();
                mem.value.copy(Value_copy);
                if(is2R()) {
                    Op2.copy(mem.address); //setting the mem address to the value held in the source register
                    mem.read();
                    mem.value.copy(Registers[Destination]);
                }
                else {
                    Word32 address = new Word32();
                    Adder.add(Op1, Op2, address);
                    address.copy(mem.address);
                    mem.read();
                    mem.value.copy(Registers[Destination]);
                }
                Value_copy.copy(mem.value);
            }
            case 19 -> {
                Word32 Value_copy = new Word32();
                mem.value.copy(Value_copy);
                Op2.copy(mem.value);
                Op1.copy(mem.address);
                mem.write();
                Value_copy.copy(mem.value);
            }
            case 20 -> {
                Op2.copy(Registers[Destination]);
            }
        }
        if(!PC_changed && Instruction_Read_Cycle)
        {
            Program_Counter.Increment();
        }
    }

    private void printReg() {
        for (int i = 0; i < 32; i++) {
            var line = "r"+ i + ":" + Registers[i].toString();
            output.add(line);
            System.out.println(line);
        }
    }

    private void printMem() {
        for (int i = 0; i < 1000; i++) {
            Word32 addr = new Word32();
            Word32 value = new Word32();
            TestConverter.fromInt(i, addr);
            // Convert i to Word32 here...
            addr.copy(mem.address);
            mem.read();
            mem.value.copy(value);
            var line = i + ":" + value;
            output.add(line);
            System.out.println(line);
        }
    }

    private boolean isCallReturn() {
        int Opcode = Converter(4, 0);
        if((Opcode >= 8 && Opcode <= 10) || (Opcode >= 12 && Opcode <= 17)){ //if the opcode falls within one of the valid ranges then it is a call return
            return true;
        }
        return false; //otherwise it is not it is immediate/2R
    }

    private boolean is2R() {
        Bit format_bit = new Bit(false);
        Current_Instruction.getBitN(5, format_bit);
        if(format_bit.getValue() == Bit.boolValues.FALSE && !isCallReturn()) //if the format bit is set to 0
        {
            return true; //this is a 2R instruction
        }
        return false; //otherwise false
    }


    private void populateOp1(){
        if(!isCallReturn())
        {
            int Destination = Converter(15, 11);
            Registers[Destination].copy(Op1);
        }
    }

    private void populateOp2(){
        if(is2R()) {
            int Source = Converter(10, 6);
            Registers[Source].copy(Op2);
        }
        else{
            int starting_Bit = 31;
            Bit Negative_Bit = new Bit(false);

            if(isCallReturn())
                Current_Instruction.getBitN(5, Negative_Bit);
            else
                Current_Instruction.getBitN(6, Negative_Bit);

            if(Negative_Bit.getValue().equals(Bit.boolValues.TRUE)){ //The signed immediate is a negative number
                Op2.not(Op2); //sign extending op2
                Get_immediate(starting_Bit);
            }
            else { //the signed immediate is a positive number
                Get_immediate(starting_Bit);
            }
        }

    }

    private void Get_immediate(int starting_Bit) {

        if(isCallReturn())
        {
            for(int i = 15; i >= 5; i--) {
                Bit temp = new Bit(false);
                Current_Instruction.getBitN(i, temp); //getting the bit values for the signed immediate
                if(temp.getValue().equals(Bit.boolValues.TRUE)) {
                    Op2.setBitN(starting_Bit, new Bit(true));
                }
                else{
                    Op2.setBitN(starting_Bit, new Bit(false));
                }
                starting_Bit--;
            }
        }
        else{
            for(int i = 10; i >= 6; i--) {
                Bit temp = new Bit(false);
                Current_Instruction.getBitN(i, temp); //getting the bit values for the signed immediate
                if(temp.getValue().equals(Bit.boolValues.TRUE)) {
                    Op2.setBitN(starting_Bit, new Bit(true));
                }
                else{
                    Op2.setBitN(starting_Bit, new Bit(false));
                }
                starting_Bit--;
            }
        }
    }


    private int Converter(int startingIndex, int endingIndex) {
        int total = 0; //init total
        int pow = 0; //init pow

        for(int i = startingIndex; i >= endingIndex; i--) //iterating from least significant bit to most significant bit
        {
            Bit cur_bit = new Bit(false); //init new bit
            Current_Instruction.getBitN(i, cur_bit); //Get the value of the current bit we are working with
            if(cur_bit.getValue() == Bit.boolValues.TRUE) //if the bit is true (ie 1)
            {
                total |= (1 << pow); // total = total | (1 << pow)
                // 1 << pow will shift the 1 into each binary "slot" to represent that current bit ie 1 << 0 = 0...0001 and so on
                // total | the mask will compare the total value at the same slot using bitwise or and flip it to 1 if its 0 hence recreating the binary in
                // decimal
            }
            pow++; //iterating the power
        }

        return total;
    }
}