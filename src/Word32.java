
public class Word32 {

    private static final int size = 32; //the size of the Word32 bit array

    private final Bit[] Word; //Word array to hold the bits

    //Constructor that initializes all the values to false
    public Word32() {
        this.Word = new Bit[size];
        for(int i = 0; i < size; i++) {
            this.Word[i] = new Bit(false);
        }
    }

    //Constructor that takes a parameter and copies the values to the current instance of Word32
    public Word32(Bit[] in) {
        this.Word = new Bit[size];
        for(int i = 0; i < size; i++) {
            this.Word[i] = new Bit(in[i].getValue() == Bit.boolValues.TRUE);
        }
    }


    public void getTopHalf(Word16 result) {// sets result = bits 0-15 of this word. use bit.assign
        for(int i = 0; i < size/2; i++) {
            result.setBitN(i, this.Word[i]);
        }
    }

    public void getBottomHalf(Word16 result) { // sets result = bits 16-31 of this word. use bit.assign
        for(int i = 0; i < size/2; i++) {
            result.setBitN(i, this.Word[i+16]);
        }
    }

    public void copy(Word32 result) { // sets result's bit to be the same as this. use bit.assign
        for (int i = 0; i < size; i++) {
            result.Word[i].assign(this.Word[i].getValue());
        }
    }

    //Checks if two Word32 objects are equal
    public boolean equals(Word32 other) {
        return equals(this, other);
    }

    public static boolean equals(Word32 a, Word32 b) {
        for(int i = 0; i < size; i++) {
            if(a.Word[i].getValue() != b.Word[i].getValue()) {
                return false;
            }
        }
        return true;
    }

    //Assigns the value at the nth index to result
    public void getBitN(int n, Bit result) {// use bit.assign
        result.assign(this.Word[n].getValue());
    }

    //Sets the bit at the nth index to the value held in source
    public void setBitN(int n, Bit source) { //  use bit.assign
        this.Word[n].assign(source.getValue());
    }

    //and operation
    public void and(Word32 other, Word32 result) {
        and(this, other, result);
    }

    public static void and(Word32 a, Word32 b, Word32 result) {
        for(int i = 0; i < size; i++) {
            a.Word[i].and(b.Word[i], result.Word[i]);
        }
    }

    //or operation
    public void or(Word32 other, Word32 result) {
        or(this, other, result);
    }

    public static void or(Word32 a, Word32 b, Word32 result) {
        for(int i = 0; i < size; i++) {
            a.Word[i].or(b.Word[i], result.Word[i]);
        }
    }

    //exclusive or operation
    public void xor(Word32 other, Word32 result) {
        xor(this, other, result);
    }

    public static void xor(Word32 a, Word32 b, Word32 result) {
        for(int i = 0; i < size; i++) {
            a.Word[i].xor(b.Word[i], result.Word[i]);
        }
    }

    //not operation
    public void not( Word32 result) {
        not(this, result);
    }

    public static void not(Word32 a, Word32 result) {
        for(int i = 0; i < size; i++) {
            a.Word[i].not(result.Word[i]);
        }
    }

    //Increment function for word32
    public void Increment() {
        Word32 temp = new Word32();
        Word32 one = new Word32();
        one.setBitN(31, new Bit(true));
        this.copy(temp); //temp now holds the value of this
        Adder.add(temp, one, this);
    }

    //ToString method for the Word32 class
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Bit bit : Word) {
            sb.append(bit.toString());
            sb.append(",");
        }
        return sb.toString();
    }
}
