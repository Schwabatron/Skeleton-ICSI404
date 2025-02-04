
public class Bit {

    public enum boolValues { FALSE, TRUE } //enum to keep track of the boolean value associated with a Bit

    private boolValues single_bit; // private value to store the current value held by the current bit instance

    //constructor that takes a boolean value
    //true -> boolValues.TRUE
    //false -> boolValues.FALSE
    public Bit(boolean value) {
        this.single_bit = value ? Bit.boolValues.TRUE : Bit.boolValues.FALSE;
    }

    //Gets the value of the current bit instance
    public boolValues getValue() {
        return single_bit;
    }

    //Re-assigns the value of the current bit instance based on the Boolvalues input
    public void assign(boolValues value) {
        single_bit = value == boolValues.TRUE ? Bit.boolValues.TRUE : Bit.boolValues.FALSE;
    }

    //And operation
    public void and(Bit b2, Bit result) {
       and(this, b2, result);
    }

    public static void and(Bit b1, Bit b2, Bit result) {
        if(b1.getValue() == boolValues.FALSE)
        {
            result.single_bit = boolValues.FALSE;
        }
        else if(b2.getValue() == boolValues.TRUE)
        {
            result.single_bit = boolValues.TRUE;
        }
    }

    //or operation
    public void or(Bit b2, Bit result) {
        or(this, b2, result);
    }

    public static void or(Bit b1, Bit b2, Bit result) {
       if(b1.getValue() == boolValues.TRUE)
       {
           result.single_bit = boolValues.TRUE;
       }
       else if(b2.getValue() == boolValues.TRUE)
       {
           result.single_bit = boolValues.TRUE;
       }
       else
       {
           result.single_bit = boolValues.FALSE;
       }
    }

    //exclusive or operation
    public void xor(Bit b2, Bit result) {
        xor(this, b2, result);
    }

    public static void xor(Bit b1, Bit b2, Bit result) {
       if(b1.getValue() == boolValues.TRUE)
       {
           if(b2.getValue() == boolValues.TRUE)
           {
               result.single_bit = boolValues.FALSE;
           }
           else if(b2.getValue() == boolValues.FALSE)
           {
               result.single_bit = boolValues.TRUE;
           }
       }
       else if(b2.getValue() == boolValues.TRUE)
       {
           result.single_bit = boolValues.TRUE;
       }
       else
       {
           result.single_bit = boolValues.FALSE;
       }
    }

    //not operation
    public void not(Bit result) {
        not(this, result);
    }

    public static void not(Bit b2, Bit result) {
        if(b2.getValue() == boolValues.FALSE) {
            result.assign(boolValues.TRUE);
        }
        else {
            result.assign(boolValues.FALSE);
        }
    }

    //ToString method for the Bit class
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
