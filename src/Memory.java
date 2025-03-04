public class Memory {
    public Word32 address= new Word32();
    public Word32 value = new Word32();
    private int load_slot = 0;

    private final Word32[] dram= new Word32[1000];


    //completed but need further verification about what the method wanted is
    public int addressAsInt() {
        int total = TestConverter.toInt(address);

        if(total < 0 || total > 999)
        {
            throw new IllegalArgumentException("valid addresses ranges from 0 to 999");
        }

        return total;//returning the final count
    }

    public Memory() {
        for(int i = 0; i < 1000; i++) {
            dram[i] = new Word32();
        }
    }

    public void read() {
        int addr = addressAsInt();
        for(int i = 0 ; i < 32; i++)
        {
            Bit temp = new Bit(false); //bit to store the value of the address
            dram[addr].getBitN(i, temp); //getting the bit to copy into value
            value.setBitN(i, temp); //setting the bit in value equal to the bit in memory
        }
    }


    public void write() {
        int addr = addressAsInt();
        for(int i = 0; i < 32; i++)
        {
            Bit temp = new Bit(false);
            value.getBitN(i, temp);
            dram[addr].setBitN(i, temp);
        }
    }

    public void load(String[] data) { //array of strings in the format (["ttff...", "ttffft...", and so on])
        if(data.length > 1000) //if the data being added is greater than the size of dram
        {
            throw new IllegalArgumentException("Dram overflow error");
        }

        for (int i = 0; i < data.length; i++) { //loop through the data array
            if(data[i].length() !=32) //if the current string at i has a length not equal to 32 then throw error
            {
                throw new IllegalArgumentException("data entries cannot be greater than 32 bits");
            }
            for(int j=0; j <data[i].length(); j++) //looping through the string at index i
            {
                char ch = data[i].charAt(j);//getting the char at index j of the string at index i
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
                    throw new IllegalArgumentException("cannot use a value other than t or f");
                }
            }
            load_slot++; //incrementing the slot
        }
    }
}
