public class L2Cache {
    public Word32[][] cache = new Word32[4][8]; //the cache (4 groups of 8)

    public Word32[] addr = new Word32[4]; // starting values for the four groups (their addresses

    int clock = 0;

    public L2Cache() {
        for(int i = 0; i < cache.length; i++) //initializing
        {
            for(int j = 0; j < cache[i].length; j++)
            {
                cache[i][j] = new Word32();
            }
        }

        for(int i = 0; i < addr.length; i++) //initializing addresses
        {
            addr[i] = new Word32();
        }
    }



    public Word32 Read(Word32 address)
    {
        if(0==0/* look for the address in one of the staches*/)
        {
            //if found incremenet by 50 and then return
            return new Word32();
        }
        else {
            //we need to load from memory (somehow) into the blocks and then return the requested addr
            //for 350 cycles
        }
        return new Word32(); //temp
    }


    public Word32 Write(Word32 address, Word32 value)
    {
        //somehow need to get this to interact with main memory
        return new Word32();
    }



}
