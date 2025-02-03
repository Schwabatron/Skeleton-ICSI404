
/**
 * The Word16 class represents a 16-bit word array of individual {@code Bit} objects.
 * It provides various logical operations and comparison methods
 */
public class Word16 {

    /**
     * The fixed size of the word (16 bits)
     */
    private static final int size = 16;

    /**
     * Array holding the bit that make up the word
     */
    private final Bit[] Word;

    /**
     * Default constructor that initializes all bits to false.
     */
    public Word16() {
        this.Word = new Bit[size];
        for(int i = 0; i < size; i++) {
            this.Word[i] = new Bit(false);
        }
    }

    /**
     * Constructor that initializes the Word16 object with values from an input array.
     * Each Bit value is then copied to a new bit in this instance
     *
     * @param in The array of Bit objects used for initialization.
     */
    public Word16(Bit[] in) {
        this.Word = new Bit[size];
        for(int i = 0; i < size; i++) {
            this.Word[i] = new Bit(false);
        }
        for(int i = 0; i < size; i++) {
            this.Word[i].assign(in[i].getValue());
        }
    }

    /**
     * sets the values in "result" to be the same as the values in this instance
     *
     * @param result The Word16 object that will receive the copied values.
     */
    public void copy(Word16 result) {
        for (int i = 0; i < size; i++) {
            result.Word[i].assign(this.Word[i].getValue());
        }
    }

    /**
     * Sets the nth bit of this word to the given bit value.
     *
     * @param n      The index of the bit to set (0-15).
     * @param source The Bit object whose value will be assigned.
     */
    public void setBitN(int n, Bit source) { // sets the nth bit of this word to "source"
        this.Word[n] = source;
    }

    /**
     * Retrieves the value of the nth bit and assigns it to the given Bit object.
     *
     * @param n      The index of the bit to retrieve (0-15).
     * @param result The Bit object where the value will be stored.
     */
    public void getBitN(int n, Bit result) { //sets result to be the same value as the nth bit of this word
        result.assign(this.Word[n].getValue());
    }

    public boolean equals(Word16 other) { // is other equal to this
        return equals(this, other);
    }

    /**
     * Checks if two Word16 instances are equal.
     *
     * @param a The first Word16 object.
     * @param b The second Word16 object.
     * @return {@code true} if both objects contain the same bit values, {@code false} otherwise.
     */
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

    /**
     * Performs a bitwise AND operation between two Word16 objects and stores the result in a third.
     *
     * @param a      The first operand.
     * @param b      The second operand.
     * @param result The Word16 object to store the result.
     */
    public static void and(Word16 a, Word16 b, Word16 result) {
        for(int i = 0; i < size; i++) {
            a.Word[i].and(b.Word[i], result.Word[i]);
        }
    }

    public void or(Word16 other, Word16 result) {
        or(this, other, result);
    }

    /**
     * Performs a bitwise OR operation between two Word16 objects and stores the result in a third.
     *
     * @param a      The first operand.
     * @param b      The second operand.
     * @param result The Word16 object to store the result.
     */
    public static void or(Word16 a, Word16 b, Word16 result) {
        for(int i = 0; i < size; i++) {
            a.Word[i].or(b.Word[i], result.Word[i]);
        }
    }

    public void xor(Word16 other, Word16 result) {
        xor(this, other, result);
    }

    /**
     * Performs a bitwise XOR operation between two Word16 objects and stores the result in a third.
     *
     * @param a      The first operand.
     * @param b      The second operand.
     * @param result The Word16 object to store the result.
     */
    public static void xor(Word16 a, Word16 b, Word16 result) {
        for(int i = 0; i < size; i++) {
            a.Word[i].xor(b.Word[i], result.Word[i]);
        }
    }

    public void not( Word16 result) {
        not(this, result);
    }

    /**
     * Performs a bitwise NOT operation on a Word16 object and stores the result in another Word16 object.
     *
     * @param a      The input Word16 object.
     * @param result The Word16 object to store the result.
     */
    public static void not(Word16 a, Word16 result) {
        for(int i = 0; i < size; i++) {
            a.Word[i].not(result.Word[i]);
        }
    }

    /**
     * Converts the Word16 object into a comma-separated string representation of its bits.
     *
     * @return A string representation of the Word16 object.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Bit bit : Word) {
            sb.append(bit.toString());
            sb.append(",");
        }
        return sb.toString();
    }
}