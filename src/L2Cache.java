public class L2Cache {
    Memory mem; //memory

    Word32 l2cache[][] = new Word32[4][8]; //4 size 8 caches

    public L2Cache(Memory memory) {
        this.mem = memory;

        for(int i = 0; i < l2cache.length; i++) //initialization for the l2cache
        {
            for(int j = 0; j < l2cache[i].length; j++)
            {
                l2cache[i][j] = new Word32();
            }
        }
    }


    public Word32[] Read(Word32 Requested_Address)
    {

    }

    public void Write(Word32 Address, Word32 Value)
    {

    }





}
