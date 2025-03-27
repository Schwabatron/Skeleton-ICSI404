import java.util.*;

public class Assembler {

    static HashMap<String, String> opcodes = new HashMap<>() {{ //Hashmap for making opcodes a little easier
        put("halt", "fffff");
        put("add", "fffft");
        put("and", "ffftf");
        put("multiply", "ffftt");
        put("leftshift", "fftff");
        put("subtract", "fftft");
        put("or", "ffttf");
        put("rightshift", "ffttt");
        put("syscall", "ftfff");
        put("call", "ftfft");
        put("return", "ftftf");
        put("compare", "ftftt");
        put("ble", "fttff");
        put("blt", "fttft");
        put("bge", "ftttf");
        put("bgt", "ftttt");
        put("beq", "tffff");
        put("bne", "tffft");
        put("load", "tfftf");
        put("store", "tfftt");
        put("copy", "tftff");
    }};

    static Set<String> call_return = new HashSet<>(){{ //set containing all the call/return opcodes for easier checking later
        add("syscall");
        add("call");
        add("ble");
        add("blt");
        add("bge");
        add("bgt");
        add("beq");
        add("bne");
    }};



    public static String[] assemble(String[] input) {
        String[] assembled = new String[input.length];
        int i = 0;


        for (String instruction : input) {
            StringBuilder final_instruction = new StringBuilder(); //Stringbuilder that will keep track of the current instruction in bit string form

            String[] instructions = instruction.split(" "); //breaking the instruction into its main arguments using the split method (regex as a space) with [0] always being the opcode

            if (instructions.length > 3) { //checking for if there are too many arguments provided ex: Add r2 r3 r5 is invalid
                throw new IllegalArgumentException("Instruction '" + instruction + "' is not valid");
            }

            if(!opcodes.containsKey(instructions[0].toLowerCase()))
            {
                throw new IllegalArgumentException(instructions[0] + " is not a valid opcode"); //if the opcode is not found within the hashmap we will assume it is not valid and throw an error
            }
            else
            {
                final_instruction.append(opcodes.get(instructions[0])); //append the opcode to the final string
            }


            if(instructions[0].equalsIgnoreCase("halt") || instructions[0].equalsIgnoreCase("return")) //after we add the opcode we can check if return or halt is used, in these scenarios we ignore the value and just use the opcode
            {
                final_instruction.append("fffffffffff");
                assembled[i] = final_instruction.toString();
                i++;
                continue;
            }
            else if(instructions.length == 1) //since we ruled out the only 2 opcodes that are valid with no arguments anything past this point will throw an error ex: (Add) by itself isnt valid
            {
                throw new IllegalArgumentException("Instruction '" + instruction + "' is not valid with no arguments");
            }

            if(call_return.contains(instructions[0].toLowerCase())) //checking if this is a call return, if it is then we will read the number after and append it
            {
                if(instructions[1].charAt(0) == 'r')
                {
                    throw new IllegalArgumentException("Instruction '" + instruction + "' is not valid with a register as an argument");
                }

                final_instruction.append(bit_string(Integer.parseInt(instructions[1]), instructions[0]));
                assembled[i] = final_instruction.toString();
                i++;
                continue;
            }

            //Past this point the only codes are going to be either 2R or immediate

            if(instructions[2].charAt(0) != 'r') //all 2R and immediate opcodes must have a register as the second argument
            {
                throw new IllegalArgumentException("Instruction '" + instruction + "' needs a register as a second argument");
            }

            if(instructions[1].charAt(0) == 'r')
            {
                final_instruction.append("f"); //2R
            }
            else
            {
                final_instruction.append("t"); //immediate
            }

            for(int j = 1; j < instructions.length; j++) //for loop to get the rest of the arguments
            {
                if(instructions[j].charAt(0) == 'r') //2R
                {
                    String register_number = instructions[j].substring(1); //removing the r so we can convert the register number
                    final_instruction.append(bit_string(Integer.parseInt(register_number), instructions[0]));
                }
                else //immediate
                {
                    final_instruction.append(bit_string(Integer.parseInt(instructions[j]), instructions[0]));
                }
            }

            assembled[i] = final_instruction.toString();

            i++;
        }


        return assembled;
    }


    private static String bit_string(int number, String opcode)
    {

        StringBuilder bit_String = new StringBuilder();

        if(call_return.contains(opcode))
        {
            for (int i = 10; i >= 0; i--) {
                boolean bitVal = ((number >> i) & 1) == 1;
                bit_String.append(bitVal ? 't' : 'f');
            }
        }
        else {
            for (int i = 4; i >= 0; i--) {
                boolean bitVal = ((number >> i) & 1) == 1;
                bit_String.append(bitVal ? 't' : 'f');
            }
        }

        return bit_String.toString();
    }

    public static String[] finalOutput(String[] input) {

        int size = input.length;
        ArrayList<String> result = new ArrayList<>();
        String halt = "ffffffffffffffff"; //A string of 16 Fs to represent a halt


        //Checking if there are an odd amount of strings passed to the final output
        if((input.length & 1) == 1)
        {
            String[] new_array = Arrays.copyOf(input, input.length + 1); //making a new array with one extra space for the halt
            new_array[new_array.length - 1] = halt; //putting halt at the end if the array to get a new array with an even number of instructions
            size = new_array.length;
            input = new_array;
        }


        for(int i = 0; i < size; i+=2) //combine instructions into 32 bit blocks rather then 16 bit blocks
        {
            result.add(input[i] + input[i+1]);
        }


        return result.toArray(new String[0]); //converting the string arraylist into a string array of the same size
    }
}
