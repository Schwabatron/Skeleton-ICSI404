public class Bit {
    public enum boolValues { FALSE, TRUE }

    private boolValues single_bit;//Private member representing the bit we are working with



    /*
    Constructor for the Bit class
    Parameters: Boolean value
    Using the parameter value the constructor will check the boolean value
    if the boolean value is true the bit will be assigned to TRUE in the boolValues enum
    if the boolean value is false the bit will be assigned to FALSE in the boolValues enum
     */
    public Bit(boolean value) {
        if(value) { //If the value is true
            this.single_bit = boolValues.TRUE; //The bit is TRUE
        }
        else {
            this.single_bit = boolValues.FALSE; //If false the bit is FALSE
        }
    }

    /*
    getValue method
    Parameters: N/A
    the getValue method is a simple getter method that will simply return the enum contained
    in the single_bit variable (either boolValues.TRUE or boolValues.FALSE)
     */
    public boolValues getValue() {
        return single_bit;
    }

    public void assign(boolValues value) {
        if(value == boolValues.TRUE) { //If the value is true
            single_bit = boolValues.TRUE; //The bit is TRUE
        }
        else {
            single_bit = boolValues.FALSE; //If false the bit is FALSE
        }
    }

    public void and(Bit b2, Bit result) {
       Bit new_bit = new Bit(single_bit == boolValues.TRUE);
       and(new_bit, b2, result);
    }


    public static void and(Bit b1, Bit b2, Bit result) {
        if((b1.getValue() == boolValues.TRUE) && (b2.getValue() == boolValues.TRUE)) {
            result.assign(boolValues.TRUE);
        }
        else
        {
            result.assign(boolValues.FALSE);
        }
    }

    public void or(Bit b2, Bit result) {
        Bit new_bit = new Bit(single_bit == boolValues.TRUE);
        or(new_bit, b2, result);
    }

    public static void or(Bit b1, Bit b2, Bit result) {
        if((b1.getValue() == boolValues.TRUE) || (b2.getValue() == boolValues.TRUE)) {
            result.assign(boolValues.TRUE);
        }
        else
        {
            result.assign(boolValues.FALSE);
        }
    }

    public void xor(Bit b2, Bit result) {
        Bit new_bit = new Bit(single_bit == boolValues.TRUE);
        xor(new_bit, b2, result);
    }

    public static void xor(Bit b1, Bit b2, Bit result) {
        if(((b1.getValue() == boolValues.TRUE) || (b2.getValue() == boolValues.TRUE)) && (b1.getValue() != b2.getValue())) {
            result.assign(boolValues.TRUE);
        }
        else
        {
            result.assign(boolValues.FALSE);
        }
    }

    public static void not(Bit b2, Bit result) {
        if(b2.getValue() == boolValues.FALSE) {
            result.assign(boolValues.TRUE);
        }
        else
        {
            result.assign(boolValues.FALSE);
        }
    }

    public void not(Bit result) {
        Bit new_bit = new Bit(single_bit == boolValues.TRUE);
        not(new_bit, result);
    }


    /*
    Name: toString
    Parameters: N/A
    Takes the private member single_bit and uses a switch statement to see if its TRUE or FALSE
    if it is TRUE then it returns a single lowercase "f"
    if it is FALSE then it returns a single lowercase "t"
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
