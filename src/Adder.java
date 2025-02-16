public class Adder {

    private static Word32 block1a = new Word32(); //rep the second half of word a
    private static Word32 block1b = new Word32(); //rep the first half of word a
    private static Word32 block2a = new Word32();
    private static Word32 block2b = new Word32();



    private final int Block_size = 16;
    private static final int Word_size = 32;

    public static void subtract(Word32 a, Word32 b, Word32 result) {
    }


    /*
    CSA addition:
    - Split into blocks
    - assume carry in 1 and 0 and compute 2 sums per block
    - use the previous blocks carry out to determine the current blocks carry in
    - selectively construct final answer using the correct blocks computed
     */
    public static void add(Word32 a, Word32 b, Word32 result) {


    }

    private static Word32 set_block(Word32 block,Word32 a, int start, int end) { //set block method to set the block
        for(int i = start; i <= end; i++) {
            Bit b = new Bit(true);
            a.getBitN(i, b);
            block.setBitN(i , b);
        }
        return block;
    }
}
