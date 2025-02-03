
/**
 * The {@code Bit} class represents a single bit that can be either {@code TRUE} or {@code FALSE}.
 * It provides the following logical operations: AND, OR, XOR, and NOT.
 */
public class Bit {

    /**
     * Enum representing possible boolean values for the {@code Bit} class.
     */
    public enum boolValues { FALSE, TRUE }

    /**
     * Private member representing the bit we are working with
     */
    private boolValues single_bit;

    /**
     * Constructor for the {@code Bit} class.
     * Takes a bool parameter and initializes the bit value depending on the value.
     *
     * @param value {@code true} sets the bit to {@code boolValues.TRUE}, {@code false} sets it to {@code boolValues.FALSE}
     */
    public Bit(boolean value) {
        if(value) {
            this.single_bit = boolValues.TRUE;
        }
        else {
            this.single_bit = boolValues.FALSE;
        }
    }

    /**
     * Accessor method to find the current value of the bit
     *
     * @return The current {@code boolValues} stored in {@code single_bit}, either {@code TRUE} or {@code FALSE}.
     */
    public boolValues getValue() {
        return single_bit;
    }

    /**
     * Assigns a new value to the bit
     *
     * @param value The new {@code boolValues} to assign ({@code TRUE} or {@code FALSE}).
     */
    public void assign(boolValues value) {
        if(value == boolValues.TRUE) {
            single_bit = boolValues.TRUE;
        }
        else {
            single_bit = boolValues.FALSE;
        }
    }

    public void and(Bit b2, Bit result) {
       and(this, b2, result);
    }

    /**
     * Performs an AND operation between two bits and stores the result in a third bit.
     *
     * @param b1 The first {@code Bit} operand.
     * @param b2 The second {@code Bit} operand.
     * @param result The {@code Bit} where the result will be stored.
     */
    public static void and(Bit b1, Bit b2, Bit result) {
        if((b1.getValue() == boolValues.TRUE) && (b2.getValue() == boolValues.TRUE)) {
            result.assign(boolValues.TRUE);
        }
        else {
            result.assign(boolValues.FALSE);
        }
    }

    public void or(Bit b2, Bit result) {
        or(this, b2, result);
    }

    /**
     * Performs an OR operation between two bits and stores the result in a third bit.
     *
     * @param b1 The first {@code Bit} operand.
     * @param b2 The second {@code Bit} operand.
     * @param result The {@code Bit} where the result will be stored.
     */
    public static void or(Bit b1, Bit b2, Bit result) {
        if((b1.getValue() == boolValues.TRUE) || (b2.getValue() == boolValues.TRUE)) {
            result.assign(boolValues.TRUE);
        }
        else {
            result.assign(boolValues.FALSE);
        }
    }

    public void xor(Bit b2, Bit result) {
        xor(this, b2, result);
    }

    /**
     * Performs an XOR operation between two bits and stores the result in a third bit.
     *
     * @param b1 The first {@code Bit} operand.
     * @param b2 The second {@code Bit} operand.
     * @param result The {@code Bit} where the result will be stored.
     */
    public static void xor(Bit b1, Bit b2, Bit result) {
        if(((b1.getValue() == boolValues.TRUE) || (b2.getValue() == boolValues.TRUE)) && (b1.getValue() != b2.getValue())) {
            result.assign(boolValues.TRUE);
        }
        else {
            result.assign(boolValues.FALSE);
        }
    }

    public void not(Bit result) {
        not(this, result);
    }

    /**
     * Performs a NOT operation on the given bit and stores the result in another bit.
     *
     * @param b2 The {@code Bit} operand to be inverted.
     * @param result The {@code Bit} where the result will be stored.
     */
    public static void not(Bit b2, Bit result) {
        if(b2.getValue() == boolValues.FALSE) {
            result.assign(boolValues.TRUE);
        }
        else {
            result.assign(boolValues.FALSE);
        }
    }

    /**
     * Converts the private member {@code single_bit} from the {@code boolValues} enum to a string representation (either "t" or "f")
     * Uses a switch statement to check if the value in {@code single_bit} is either {@code TRUE} or {@code FALSE}
     *
     * @return "t" if {@code single_bit} is {@code TRUE},
     *         "f" if {@code single_bit} is {@code FALSE}
     *          otherwise returns an error message to the user
     */
    public String toString() {
        switch(single_bit) {
            case FALSE -> {
                return "f";
            }
            case TRUE -> {
                return "t";
            }
            default -> {
                return "Something has went very very wrong";
            }
        }
    }
}
