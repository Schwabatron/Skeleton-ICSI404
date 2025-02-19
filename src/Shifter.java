public class Shifter {
    public static void LeftShift(Word32 source, int amount, Word32 result) {
        for (int i = 0; i <= 31 - amount; i++) {
            Bit b = new Bit(false);
            source.getBitN(i + amount, b);
            result.setBitN(i, b);
        }

    }

    public static void RightShift(Word32 source, int amount, Word32 result) {
        for (int i = amount; i < 32; i++) {
            Bit b = new Bit(false);
            source.getBitN(i - amount, b);
            result.setBitN(i, b);
        }

    }
}
