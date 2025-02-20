public class Shifter {
    public static void LeftShift(Word32 source, int amount, Word32 result) {
        for (int i = 0; i <= 31 - amount; i++) { //loops from 0 to 31 - the amout to shift
            Bit b = new Bit(false); //init new bit
            source.getBitN(i + amount, b); //set b to i+the shift amount
            result.setBitN(i, b); //set i to b in the result word
        }

    }


    //right shift uses similar logic to left just reversed
    public static void RightShift(Word32 source, int amount, Word32 result) {
        for (int i = amount; i < 32; i++) {
            Bit b = new Bit(false);
            source.getBitN(i - amount, b);
            result.setBitN(i, b);
        }

    }
}
