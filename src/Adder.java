public class Adder {

    public static void subtract(Word32 a, Word32 b, Word32 result) {
        Word32 one = new Word32();
        one.setBitN(31, new Bit(true));

        Word32 temp = new Word32();
        b.not(temp);

        add(a, temp , result);
        add(result, one, result);

    }


    //c(in) = carry in
    //c(out) = carry out
    /*
    Sum = b1 xor b2 xor c(in)
    c(out) = b1 and b2 or (( b1 xor b2) and c(in))
     */

    /*
    Idea: the first bit will have no carry in (logic) so the first bit will just be b1 xor b2 xor 0
    we can then use the c(out) formula to calculate the bit out for the next bit
    if its 1 then we have a c(in) of 1 and if not then 0

    - make 2 methods
    -sum
    -c(out)
    using these we will make the wordiness of add much less overwhelming

     */
    public static void add(Word32 a, Word32 b, Word32 result) {
        Bit Cin = new Bit(false); //init Cin
        Bit Cout = new Bit(false);//init Cout

        for(int i = 31; i >= 0; i--)
        {
            if(Cout.getValue() == Bit.boolValues.TRUE)
            {
                Cin.assign(Bit.boolValues.TRUE);
            }
            else
            {
                Cin.assign(Bit.boolValues.FALSE);
            }
            Bit A = new Bit(false);
            a.getBitN(i , A);
            Bit B = new Bit(false);
            b.getBitN(i , B);

            result.setBitN(i , sum(A, B, Cin, new Bit(false)));
            Cout = Cout(A, B, Cin, new Bit(false));

        }

    }

    private static Bit sum(Bit a, Bit b, Bit Cin, Bit result) { //function to calculate the sum
       a.xor(b, result);
       result.xor(Cin, result);
       return result;
    }

    private static Bit Cout(Bit a, Bit b, Bit Cin, Bit result) { //function to calculate the cout
        a.and(b, result);
        Bit second_part = new Bit(false);
        a.xor(b, second_part);
        second_part.and(Cin, second_part);
        result.or(second_part, result);
        return result;
    }



}
