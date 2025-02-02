public class Word32 {
    private static final int size = 32;
    private final Bit[] Word;

    public Word32() {
        this.Word = new Bit[size];
        for(int i = 0; i < size; i++)
        {
            this.Word[i] = new Bit(false);
        }
    }

    public Word32(Bit[] in) {
        this.Word = new Bit[size];

        for(int i = 0; i < size; i++)
        {
            this.Word[i] = in[i];
        }
    }

    public void getTopHalf(Word16 result) {// sets result = bits 0-15 of this word. use bit.assign
        for(int i = 0; i < size/2; i++)
        {
            result.setBitN(i, this.Word[i]);
        }
    }

    public void getBottomHalf(Word16 result) { // sets result = bits 16-31 of this word. use bit.assign
        for(int i = 0; i < size/2; i++)
        {
            result.setBitN(i, this.Word[i+16]);
        }
    }

    public void copy(Word32 result) {
        for (int i = 0; i < size; i++)
        {
            result.Word[i].assign(this.Word[i].getValue());
        }// sets result's bit to be the same as this. use bit.assign
    }

    public boolean equals(Word32 other) {
        return equals(this, other);
    }

    public static boolean equals(Word32 a, Word32 b) {
        for(int i = 0; i < size; i++)
        {
            if(a.Word[i].getValue() != b.Word[i].getValue())
            {
                return false;
            }
        }
        return true;
    }

    public void getBitN(int n, Bit result) {// use bit.assign
        result.assign(this.Word[n].getValue());
    }

    public void setBitN(int n, Bit source) { //  use bit.assign
        this.Word[n] = source;
    }

    public void and(Word32 other, Word32 result) {
        and(this, other, result);
    }

    public static void and(Word32 a, Word32 b, Word32 result) {
        for(int i = 0; i < size; i++)
        {
            a.Word[i].and(b.Word[i], result.Word[i]);
        }
    }

    public void or(Word32 other, Word32 result) {
        or(this, other, result);
    }

    public static void or(Word32 a, Word32 b, Word32 result) {
        for(int i = 0; i < size; i++)
        {
            a.Word[i].or(b.Word[i], result.Word[i]);
        }
    }

    public void xor(Word32 other, Word32 result) {
        xor(this, other, result);
    }

    public static void xor(Word32 a, Word32 b, Word32 result) {
        for(int i = 0; i < size; i++)
        {
            a.Word[i].xor(b.Word[i], result.Word[i]);
        }
    }

    public void not( Word32 result) {
        not(this, result);
    }

    public static void not(Word32 a, Word32 result) {
        for(int i = 0; i < size; i++)
        {
            a.Word[i].not(result.Word[i]);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Bit bit : Word) {
            sb.append(bit.toString());
            sb.append(",");
        }

        return sb.toString();
    }
}
