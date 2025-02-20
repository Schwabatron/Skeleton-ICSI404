public class Multiplier {
    public static void multiply(Word32 a, Word32 b, Word32 result) {//two 32 bit numbers will equal 64 bits but we will be ignoring the top 32 bits

        int shifter = 0;
        for(int i = 31; i >= 0; i--) {
            Bit geta = new Bit(false);
            Word32 add_to_result = new Word32();
            Word32 add_to_result_shifted = new Word32(); //have to make a seperate value for this because of the way shift works
            a.getBitN(i, geta); //geta now has the bit we are using as the multiplier
            if(geta.getValue() == Bit.boolValues.TRUE)// if it is one we will copy b into add to result shift it by the shifter
            {
                b.copy(add_to_result);//add to result now holds b
                Shifter.LeftShift(add_to_result, shifter, add_to_result_shifted); //shift the value
                Adder.add(result, add_to_result_shifted, result);//add to result now holds the shifted value
            }

            shifter++;

        }
    }
}