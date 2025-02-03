public class Word16 {
    private static final int size = 16;
    private final Bit[] Word;

    public Word16() {
        this.Word = new Bit[size];
        for(int i = 0; i < size; i++) {
            this.Word[i] = new Bit(false);
        }
    }

    public Word16(Bit[] in) {
        this.Word = new Bit[size];
        System.arraycopy(in, 0, this.Word, 0, size);
    }

    public void copy(Word16 result) { // sets the values in "result" to be the same as the values in this instance; use "bit.assign"
        for (int i = 0; i < size; i++) {
            result.Word[i].assign(this.Word[i].getValue());
        }
    }

    public void setBitN(int n, Bit source) { // sets the nth bit of this word to "source"
        this.Word[n] = source;
    }

    public void getBitN(int n, Bit result) { //sets result to be the same value as the nth bit of this word
        result.assign(this.Word[n].getValue());
    }

    public boolean equals(Word16 other) { // is other equal to this
        return equals(this, other);
    }

    public static boolean equals(Word16 a, Word16 b) {
        for(int i = 0; i < size; i++) {
            if(a.Word[i].getValue() != b.Word[i].getValue()) {
                return false;
            }
        }
        return true;
    }

    public void and(Word16 other, Word16 result) {
        and(this, other, result);
    }

    public static void and(Word16 a, Word16 b, Word16 result) {
        for(int i = 0; i < size; i++) {
            a.Word[i].and(b.Word[i], result.Word[i]);
        }
    }

    public void or(Word16 other, Word16 result) {
        or(this, other, result);
    }

    public static void or(Word16 a, Word16 b, Word16 result) {
        for(int i = 0; i < size; i++) {
            a.Word[i].or(b.Word[i], result.Word[i]);
        }
    }

    public void xor(Word16 other, Word16 result) {
        xor(this, other, result);
    }

    public static void xor(Word16 a, Word16 b, Word16 result) {
        for(int i = 0; i < size; i++) {
            a.Word[i].xor(b.Word[i], result.Word[i]);
        }
    }

    public void not( Word16 result) {
        not(this, result);
    }

    public static void not(Word16 a, Word16 result) {
        for(int i = 0; i < size; i++) {
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