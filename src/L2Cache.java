public class L2Cache {
    public Word32[][] cache = new Word32[4][8]; //the cache (4 groups of 8)

    public Word32[] addr = new Word32[4]; // starting values for the four groups (their addresses

    Memory mem; //memory for l2cache (and by extension)

    public int clock = 0;

    public boolean found = false;

    Word32 one = new Word32();

    public L2Cache() {
        mem = new Memory();

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

        one.setBitN(31, new Bit(true));
    }



    public Word32 Read(Word32 address)
    {

        int address_int = addressAsInt(address);

        int starting = address_int - (address_int % 8);


        for(int i = 0; i < addr.length; i++)
        {
            int first_cache_addr = addressAsInt(addr[i]);
            if(first_cache_addr == starting)
            {
                found = true;
                return cache[i][address_int - starting]; //returning the correct address from the l2cache
            }
        }

        if(!found)
        {
            clock += 300;
            //somehow fill from memory (going to be random)
            int randomNumber = (int) (Math.random() * 4); //choosing a random cache block to be filled

            Word32 starting_as_binary = new Word32();
            TestConverter.fromInt(starting, starting_as_binary);

            addr[randomNumber] = starting_as_binary; //setting teh correct starting address

            starting_as_binary.copy(mem.address);

            for(int i = 0; i < 8; i++)
            {
                Word32 current_instruction = new Word32();
                mem.read();
                mem.value.copy(current_instruction);
                cache[randomNumber][i] = current_instruction;
                Word32 next_instruction = new Word32();
                Adder.add(current_instruction, one, next_instruction);
                next_instruction.copy(mem.address); //setting the new address
            }

            return cache[randomNumber][address_int - starting];
        }

        return null;
    }


    public Word32 Write(Word32 address, Word32 value)
    {
        //somehow need to get this to interact with main memory
        return new Word32();
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
