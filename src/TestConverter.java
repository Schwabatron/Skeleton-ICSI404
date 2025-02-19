public class TestConverter {
    /*
        don't use any java built in methods
     */
    public static void fromInt(int value, Word32 result) {
        for(int i = 31; i >= 0; i--)
        {
            boolean Bit_val = (value & 1) == 1; //Determining the least significant bit of the int (ex 13 (1101) will isolate 1). this can then be checked

            result.setBitN(i, new Bit(Bit_val)); //Setting the bit in the same position as the one we are checking

            value >>= 1 ; //right shift the value to "drop" the LSB. allowing us to work with the next digit (ex 13 >> 1 = 0110) now what was the second digit
                          //is the new LSB so on the next iteration we can determine the value to place in the correct position
        }
    }

    public static int toInt(Word32 value) {
        int total = 0; //init total
        int pow = 0; //init pow

        for(int i = 31; i >= 0; i--) //iterating from least significant bit to most significant bit
        {
            Bit cur_bit = new Bit(false); //init new bit
            value.getBitN(i, cur_bit); //Get the value of the current bit we are working with
            if(cur_bit.getValue() == Bit.boolValues.TRUE) //if the bit is true (ie 1)
            {
                total |= (1 << pow); // total = total | (1 << pow)
                                     // 1 << pow will shift the 1 into each binary "slot" to represent that current bit ie 1 << 0 = 0...0001 and so on
                                     // total | the mask will compare the total value at the same slot using bitwise or and flip it to 1 if its 0 hence recreating the binary in
                                     // decimal
            }
            pow++; //iterating the power
        }

        return total;//returning the final count
    }
}