import java.util.Random;

public class L2Cache {
    Memory mem; //memory

    Word32 l2cache[][] = new Word32[4][8]; //4 size 8 caches

    Word32 Starting_addresses[] = new Word32[4];
    Boolean first_read = true;
    int current_cache_index = 0;


    public L2Cache(Memory memory) {
        this.mem = memory;

        for(int i = 0; i < l2cache.length; i++) //initialization for the l2cache
        {
            for(int j = 0; j < l2cache[i].length; j++)
            {
                l2cache[i][j] = new Word32();
            }
        }

        for(int i = 0; i < Starting_addresses.length; i++) //initializing the starting addresses array
        {
            Starting_addresses[i] = new Word32();
        }
    }


    public Word32[] Read(Word32 Requested_Address)
    {
        Word32 Cache_block_requested = new Word32();
        Word32 Cache_block = new Word32();
        Shifter.RightShift(Requested_Address, 3, Cache_block_requested); //getting the requested cache block level

        if(first_read)
        {
            int Cache_to_fill = current_cache_index;
            current_cache_index = (current_cache_index + 1) % 4;
            Word32 new_Starting_address = new Word32();
            Shifter.LeftShift(Cache_block_requested, 3, new_Starting_address);
            fill_cache(Cache_to_fill, new_Starting_address); //passing it the index of the cache i want to fill and the new starting address for when i read from memory
            first_read = false;
            return l2cache[Cache_to_fill];
        }

        for(int i = 0; i < Starting_addresses.length ; i++){ //check if there is any matching entries{
                Shifter.RightShift(Starting_addresses[i], 3, Cache_block); //getting the actual cache block level for each entry
                if(Cache_block.equals(Cache_block_requested)) //if they match
                {
                    return l2cache[i]; //return the l2cache level with the matching index
                }
                Cache_block = new Word32();
        }

        //fill one of the cache blocks from main memory with 8 mem addresses and return it
        int Cache_to_fill = current_cache_index;
        current_cache_index = (current_cache_index + 1) % 4;
        Word32 new_Starting_address = new Word32();
        Shifter.LeftShift(Cache_block_requested, 3, new_Starting_address);
        fill_cache(Cache_to_fill, new_Starting_address); //passing it the index of the cache i want to fill and the new starting address for when i read from memory
        return l2cache[Cache_to_fill];
    }

    public void Write(Word32 Address, Word32 Value)
    {
        Boolean found = false;
        Word32 Cache_block_requested = new Word32();
        Word32 Cache_block = new Word32();
        Shifter.RightShift(Address, 3, Cache_block_requested); //getting the requested cache block level

        for(int i = 0; i < Starting_addresses.length ; i++){ //check if there is any matching entries{
            Shifter.RightShift(Starting_addresses[i], 3, Cache_block); //getting the actual cache block level for each entry
            if(Cache_block.equals(Cache_block_requested)) //if they match
            {
                int offset = getOffset(Address);
                Value.copy(l2cache[i][offset]); //copy the value into l2cache at the correct index
                found = true;
            }
            Cache_block = new Word32();
        }

        if(!found)
        {
            //if it could not find the cache we need to load the block into l2cache from memory and then write to the correct address
            int Cache_to_fill = current_cache_index;
            current_cache_index = (current_cache_index + 1) % 4;
            Word32 new_Starting_address = new Word32();
            Shifter.LeftShift(Cache_block_requested, 3, new_Starting_address);
            fill_cache(Cache_to_fill, new_Starting_address); //passing it the index of the cache i want to fill and the new starting address for when i read from memory
            Value.copy(l2cache[Cache_to_fill][getOffset(Address)]);
        }

        //now we need to edit the value in main memory
        Address.copy(mem.address);
        Value.copy(mem.value);
        mem.write(); //writing the value to maim memory to simulate a cache write through
    }



    private void fill_cache(int index, Word32 Starting_address) {
        Word32 mem_addr_backup = new Word32();
        Word32 mem_value_backup = new Word32();
        mem.address.copy(mem_addr_backup);
        mem.value.copy(mem_value_backup);
        Starting_address.copy(Starting_addresses[index]); //setting the new starting index for this cache
        Starting_address.copy(mem.address); //setting the pointer in memory to the correct starting address
        for(int i = 0; i < l2cache[0].length; i++) //loop through the cache and fill 8 values
        {
            mem.read(); //reading the value from memory
            mem.value.copy(l2cache[index][i]); //inserting the value read from memory into the l2cache at index
            mem.address.Increment(); //increment the memory address
        }
        mem_addr_backup.copy(mem.address);
        mem_value_backup.copy(mem.value);
    }

    private int getOffset(Word32 Requested_Address) {
        Word32 Isolated_offset = new Word32(); //Word32 to hold the value to and with the requested address
        for(int i = 31; i >= 29; i--) //getting the bit value 000~~111 to and with the requested address to get the offset
            Isolated_offset.setBitN(i, new Bit(true));
        Word32 Offset = new Word32(); //Word32 to hold the value of the index we are requesting
        Requested_Address.and(Isolated_offset, Offset); //Anding the value with 000~~111 in order to isolate the index requested
        return addressAsInt(Offset); // return the index as an int that we need to return to the processor
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
