public class InstructionCache {
    public Word32 cache[] = new Word32[8]; //cache holding 8 words

    public Word32 addr; //address represented by the first of index of the cache (this will be the PC of the current instruction)

    public int clock = 0;

    public L2Cache l2cache;

    public InstructionCache() {
        for(int i = 0; i < 8; i++) { //initialize the cache
            cache[i] = new Word32();
        }
        l2cache = new L2Cache(); //init l2cache

        addr = new Word32();
    }

    public Word32 Read(Word32 PC) //temp
    {
        if(0==0/* address in range of this cache*/)
        {
            //add 10 to the clock cycles
            //return the correct cached instruction

        }else {

            //call L2staches read method and also increase by 50
            l2cache.Read(addr);
        }


        return new Word32();
    }

}
