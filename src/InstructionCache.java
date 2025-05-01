public class InstructionCache {

    L2Cache l2cache; //l2 cache

    Word32[] instructions = new Word32[8]; //array of words to represent the cache

    Word32 Starting_Address;

    Word32 Cache_factor = new Word32();

    Boolean first_read = true;

    public InstructionCache(L2Cache l2cache) {
        this.l2cache = l2cache; //initializing l2cache
        for(int i = 0; i < instructions.length; i++) {instructions[i] = new Word32();} //Initializing instruction cache
        Starting_Address = new Word32(); //initializing the starting address

        Cache_factor.setBitN(28, new Bit(true)); //making a cache factor(8) since requested cache block * 8 will be the new starting address for the cache after a fill
    }

    public Word32 Read(Word32 Requested_Address)
    {

        Word32 Cache_Block_Requested = new Word32();
        Word32 Cache_Block = new Word32();
        Shifter.RightShift(Requested_Address, 3, Cache_Block_Requested); //getting the requested cache block level
        Shifter.RightShift(Starting_Address, 3, Cache_Block); //getting the actual cache block level
        int index = getOffset(Requested_Address); //getting the index of the requested address within the cache

        if(first_read)
        {
            Word32[] Read_from_l2cache = l2cache.Read(Requested_Address); //getting the array of words from the l2Cache
            fill_cache(Read_from_l2cache);
            Word32 new_starting_address = new Word32();
            Shifter.LeftShift(Cache_Block_Requested, 3, new_starting_address);
            new_starting_address.copy(Starting_Address); //assigning the new starting address after a fill
            first_read = false;
            return instructions[index];
        }

        if(Cache_Block.equals(Cache_Block_Requested)) //comparing to see if the requested address is being held in this cache block
        {
            return instructions[index]; // return the requested instruction
        }
        else
        {
            Word32[] Read_from_l2cache = l2cache.Read(Requested_Address); //getting the array of words from the l2Cache
            fill_cache(Read_from_l2cache);
            Word32 new_starting_address = new Word32();
            Shifter.LeftShift(Cache_Block_Requested, 3, new_starting_address);
            new_starting_address.copy(Starting_Address); //assigning the new starting address after a fill
            return instructions[index];
        }
    }


    private int getOffset(Word32 Requested_Address) {
        Word32 Isolated_offset = new Word32(); //Word32 to hold the value to and with the requested address
        for(int i = 31; i >= 29; i--) //getting the bit value 000~~111 to and with the requested address to get the offset
            Isolated_offset.setBitN(i, new Bit(true));
        Word32 Offset = new Word32(); //Word32 to hold the value of the index we are requesting
        Requested_Address.and(Isolated_offset, Offset); //Anding the value with 000~~111 in order to isolate the index requested
        return addressAsInt(Offset); // return the index as an int that we need to return to the processor
    }


    public void fill_cache(Word32[] Cache_Block) {
        for(int i = 0; i < instructions.length; i++) //copying each instruction in Cache_Block into instructions
            Cache_Block[i].copy(instructions[i]);
    }


    public int addressAsInt(Word32 Offset) {
        int total = 0; //init total
        int pow = 0; //init pow

        for(int i = 31; i >= 29; i--) //iterating from least significant bit to most significant bit
        {
            Bit cur_bit = new Bit(false); //init new bit
            Offset.getBitN(i, cur_bit); //Get the value of the current bit we are working with
            if(cur_bit.getValue() == Bit.boolValues.TRUE) //if the bit is true (ie 1)
            {
                total |= (1 << pow); // total = total | (1 << pow)
                // 1 << pow will shift the 1 into each binary "slot" to represent that current bit ie 1 << 0 = 0...0001 and so on
                // total | the mask will compare the total value at the same slot using bitwise or and flip it to 1 if its 0 hence recreating the binary in
                // decimal
            }
            pow++; //iterating the power
        }
        return total;//returning the final count
    }







}
