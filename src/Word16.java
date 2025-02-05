
public class Word16 {

    private static final int size = 16; //The size of the word16 array

    private final Bit[] Word; //Private member to keep track of the current Word16

    //Constructor that initializes a new bit array of all false
    public Word16() {
        this.Word = new Bit[size];
        for(int i = 0; i < size; i++) {
            this.Word[i] = new Bit(false);
        }
    }

    //Constructor that takes an input array
    public Word16(Bit[] in) {
        this.Word = new Bit[size];
        for(int i = 0; i < size; i++) {
            this.Word[i] = new Bit(in[i].getValue() == Bit.boolValues.TRUE);
        }
    }

    //Copies the values in the current Word16 instance to result
    public void copy(Word16 result) {
        for (int i = 0; i < size; i++) {
            result.Word[i].assign(this.Word[i].getValue());
        }
    }

    //Sets the bit at the nth index to the value in source
    public void setBitN(int n, Bit source) { // sets the nth bit of this word to "source"
        this.Word[n].assign(source.getValue());
    }

    //Gets the bit at the nth index and assigns it to result
    public void getBitN(int n, Bit result) { //sets result to be the same value as the nth bit of this word
        result.assign(this.Word[n].getValue());
    }

    //non-static version of the equals method
    public boolean equals(Word16 other) { // is other equal to this
        return equals(this, other);
    }

    //Equals method used to check if two Word16 objects are equal
    public static boolean equals(Word16 a, Word16 b) {
        for(int i = 0; i < size; i++) {
            if(a.Word[i].getValue() != b.Word[i].getValue()) {
                return false;
            }
        }
        return true;
    }

    //non-static version of the and operation
    public void and(Word16 other, Word16 result) {
        and(this, other, result);
    }

    //And operation. true only if both are true
    public static void and(Word16 a, Word16 b, Word16 result) {
        for(int i = 0; i < size; i++) {
            a.Word[i].and(b.Word[i], result.Word[i]);
        }
    }

    //non-static version of the or operation
    public void or(Word16 other, Word16 result) {
        or(this, other, result);
    }

    //Or operation. can be one or the other or both
    public static void or(Word16 a, Word16 b, Word16 result) {
        for(int i = 0; i < size; i++) {
            a.Word[i].or(b.Word[i], result.Word[i]);
        }
    }

    //non-static version of the xor method
    public void xor(Word16 other, Word16 result) {
        xor(this, other, result);
    }

    //Exclusive Or operation, can be one or the other but not both
    public static void xor(Word16 a, Word16 b, Word16 result) {
        for(int i = 0; i < size; i++) {
            a.Word[i].xor(b.Word[i], result.Word[i]);
        }
    }

    //non-static version of the not method
    public void not( Word16 result) {
        not(this, result);
    }

    //Not operation to flip all the bits to the opposite bit
    public static void not(Word16 a, Word16 result) {
        for(int i = 0; i < size; i++) {
            a.Word[i].not(result.Word[i]);
        }
    }

    //ToString method to print the values in the word16 array
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Bit bit : Word) {
            sb.append(bit.toString());
            sb.append(",");
        }
        return sb.toString();
    }
}