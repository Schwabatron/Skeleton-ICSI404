public class InstructionCache {
    public L2Cache l2cache;

    public Word32 cache[] = new Word32[8]; //cache holding 8 words

    public Word32 addr; //address represented by the first of index of the cache (this will be the PC of the current instruction)

    public int clock = 0;


    //pass l2 cache as a parameter
    public InstructionCache(L2Cache l2cache) {
        for(int i = 0; i < 8; i++) { //initialize the cache
            cache[i] = new Word32();
        }

        this.l2cache = new L2Cache(); //init l2cache

        addr = new Word32();
    }

    public Word32 Read(Word32 address) //temp
    {
        int starting_addr = addressAsInt(addr);
        int addr_int = addressAsInt(address); //getting the starting address as


        if(addr_int >= starting_addr && addr_int < (starting_addr + 8)) //if the requested address is between the starting address and the end of the array
        {
            clock += 10;
            return cache[addr_int - starting_addr]; //returning the value held at the index addr_int - starting_addr
                                                    //ie if we have addresses 8 - 16 and address 9 is requested it is at index 1 (9-8)

        }else{
            clock += 50;
            Word32 retval = l2cache.Read(address); //passing the addr now to l2cache
            return retval;
        }
    }


    public int addressAsInt(Word32 address) {
        int total = 0; //init total
        int pow = 0; //init pow

        for(int i = 31; i >= 0; i--) //iterating from least significant bit to most significant bit
        {
            Bit cur_bit = new Bit(false); //init new bit
            address.getBitN(i, cur_bit); //Get the value of the current bit we are working with
            if(cur_bit.getValue() == Bit.boolValues.TRUE) //if the bit is true (ie 1)
            {
                total |= (1 << pow); // total = total | (1 << pow)
                // 1 << pow will shift the 1 into each binary "slot" to represent that current bit ie 1 << 0 = 0...0001 and so on
                // total | the mask will compare the total value at the same slot using bitwise or and flip it to 1 if its 0 hence recreating the binary in
                // decimal
            }
            pow++; //iterating the power
        }

        if(total < 0 || total > 999)
        {
            throw new IllegalArgumentException("valid addresses ranges from 0 to 999");
        }

        return total;//returning the final count
    }

}
