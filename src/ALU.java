public class ALU {

    //TODO: deconstruct the instruction into the OPCODE, format (2R vs immediate) , source register/signed immediate value, and destination register

    public Word16 instruction = new Word16(); //holds the instructions
    public Word32 op1 = new Word32(); //first operand of the operation (for now assume destination)
    public Word32 op2 = new Word32(); //second operand of the operation (for now assume source )

    public Word32 result = new Word32(); //area to store the computed result before putting it back in the destination register
    public Bit less = new Bit(false); //true if op1 < op2
    public Bit equal = new Bit(false); //true if op1 == op2



    public void doInstruction(){
        int opcode = getOpcode();

        switch (opcode) {
            case 0 ->{}
            case 1 ->{ //Adds source to destination, storing the result in destination
                result = new Word32();
                Adder.add(op1, op2, result);
            }
            case 2 ->{ //Bitwise AND of source and destination, storing the result in destination.
                result = new Word32();
                op1.and(op2, result);
            }
            case 3 ->{ //Multiplies destination by source, storing the result in destination
                result = new Word32();
                Multiplier.multiply(op2, op1, result);
            }
            case 4 ->{ //Left shifts destination by source bits, storing the result in destination.
                result = new Word32();
                int shiftAmount = getShiftAmount(op2);
                Shifter.LeftShift(op1, shiftAmount, result);
            }
            case 5 ->{ //Subtracts source from destination, storing the result in destination.
                result = new Word32();
                Adder.subtract(op1, op2, result);
            }
            case 6 ->{ //Bitwise OR of source and destination, storing the result in destination
                result = new Word32();
                op1.or(op2, result);
            }
            case 7 ->{ //Right shifts destination by source bits, storing the result in destination
                result = new Word32();
                int shiftAmount = getShiftAmount(op2);
                Shifter.RightShift(op1, shiftAmount, result);
            }
            case 8 ->{}
            case 9 ->{}
            case 10 ->{}
            case 11 ->{
                Word32 temp_result = new Word32();
                Adder.subtract(op1, op2, temp_result);
                boolean isNegative = isNegative(temp_result);
                boolean isZero = isZero(temp_result);
                if(isZero) {
                    less.assign(Bit.boolValues.FALSE);
                    equal.assign(Bit.boolValues.TRUE);
                } else if(isNegative) {
                    less.assign(Bit.boolValues.TRUE);
                    equal.assign(Bit.boolValues.FALSE);
                }
                else {
                    less.assign(Bit.boolValues.FALSE);
                    equal.assign(Bit.boolValues.FALSE);
                }


            }
            case 12 ->{}
            case 13 ->{}
            case 14 ->{}
            case 15 ->{}
            case 16 ->{}
            case 17 ->{}
            case 18 ->{}
            case 19 ->{}
            case 20 ->{}
        }

    } //Do the instruction

    /*
    helper methods
     */

    public int getOpcode() { //converting the opcode to int
        int opcode_as_int = 0;
        int pow = 0;

        // Iterate over the first 5 bits (bits 0 to 4) of the instruction
        for (int i = 4; i >= 0; i--) {
            Bit cur_bit = new Bit(false);
            instruction.getBitN(i, cur_bit); // Get bit i from the instruction

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

    private int getShiftAmount(Word32 word) //gets the shift amount
    {
        int shiftAmount = 0;
        int pow = 0;

        for (int i = 31; i >= 27; i--) { //only need to look at first 5 bits because anything past that is just the same since 2^5=32
            Bit bit = new Bit(false);
            word.getBitN(i, bit);
            if (bit.getValue() == Bit.boolValues.TRUE) {
                shiftAmount |= (1 << pow);
            }
            pow++;
        }
        return shiftAmount;
    }

    private boolean isNegative(Word32 word) { //tests if word is negative
        Bit temp = new Bit(false);
        word.getBitN(0, temp);
        return temp.getValue() == Bit.boolValues.TRUE;
    }

    private boolean isZero(Word32 word) { //tests if word is 0
        Bit temp = new Bit(false);
        for(int i = 0; i < 32; i++)
        {
            word.getBitN(i, temp);
            if(temp.getValue() == Bit.boolValues.TRUE)
            {
                return false;
            }
        }
        return true;
    }


}
