public class Memory {
    public Word32 address= new Word32();
    public Word32 value = new Word32();


    //1000 slot array holding type Word32()
    private final Word32[] dram= new Word32[1000];


    //convert the address into an integer
    public int addressAsInt() {
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

    //initialize memory
    public Memory() {
        for(int i = 0; i < 1000; i++) {
            dram[i] = new Word32();
        }
    }

    //the value is filled with the value at index address dram[address]
    public void read() {
        int addr = addressAsInt();
        for(int i = 0 ; i < 32; i++)
        {
            Bit temp = new Bit(false); //bit to store the value of the address
            dram[addr].getBitN(i, temp); //getting the bit to copy into value
            value.setBitN(i, temp); //setting the bit in value equal to the bit in memory
        }
    }

    //value is written to dram at index address
    public void write() {
        int addr = addressAsInt();
        for(int i = 0; i < 32; i++)
        {
            Bit temp = new Bit(false);
            value.getBitN(i, temp);
            dram[addr].setBitN(i, temp);
        }
    }

    //load data inserts word32 objects into the dram starting from 0 and incrementing from there
    public void load(String[] data) { //array of strings in the format (["ttff...", "ttffft...", and so on])
        int load_slot = 0; //making sure we start from the first index

        if(data.length > 1000) //if the data being added is greater than the size of dram
        {
            throw new IllegalArgumentException("Dram overflow error");
        }

        for (String word : data) { //loop through the data array
            if(word.length() !=32) //if the current string at i has a length not equal to 32 then throw error
            {
                throw new IllegalArgumentException("data entries cannot be greater than 32 bits");
            }
            for(int j=0; j <word.length(); j++) //looping through the string at index i
            {
                char ch = word.charAt(j);//getting the char at index j of the string at index i
                if(ch == 't')
                {
                    dram[load_slot].setBitN(j, new Bit(true)); //change the memory located at index j of load_slot to match the data present in the data array
                }
                else if(ch == 'f')
                {
                    dram[load_slot].setBitN(j, new Bit(false));
                }
                else
                {
                    throw new IllegalArgumentException("cannot use a value other than t or f when loading dram");
                }
            }
            load_slot++; //incrementing the slot
        }
    }
}
