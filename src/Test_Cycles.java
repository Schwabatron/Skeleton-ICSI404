import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;



public class Test_Cycles {





    public static Memory linkedlistmem()
    {
        Memory LinkedListmem = new Memory();

        int cur_val = 1;
        int cur_addr = 15;
        for(int i = 1; i < 21; i++)
        {
            cur_val = i;
            Word32 addr = new Word32();
            Word32 val = new Word32();
            TestConverter.fromInt(cur_val, val); //converting values to word32
            TestConverter.fromInt(cur_addr, addr); //converting 15 to a useable memory address for the memory
            val.copy(LinkedListmem.value);
            addr.copy(LinkedListmem.address);
            LinkedListmem.write(); //writing the value to the memory address
            cur_addr++; //iterating addr to make it 16 now
            TestConverter.fromInt(cur_addr, addr);
            cur_val = 15 * (i+1);
            cur_addr = cur_val;
            TestConverter.fromInt(cur_val, val);
            val.copy(LinkedListmem.value);
            addr.copy(LinkedListmem.address);
            LinkedListmem.write();
        }

        return LinkedListmem;
    }







    @Test
    public void testSum20Array() {
        String[] program = {                                  // PC:
                "copy 15 r1",  //initial memory address of array --0
                "copy 0 r2",    //initialize sum                 --0
                "copy 0 r3",     //initialize counter            --1
                "copy r1 r5", //first step of making r5 = 20     --1
                "add 5 r5", //making r5 equal to 20              --2
                "add 0 r5", //empty space to make the load at pc3--2
                "load r1 r4", //loading m[15(initially)] into r4 --3
                "add r4 r2", //add the loaded value into the sum --3
                "add 1 r1", //increment the memory address       --4
                "add 1 r3", //increment the counter              --4
                "compare r5 r3", //compare 20 to the counter     --5
                "blt -2", //branch back to 3                     --5
                "syscall 0", //print register values             --6
                "halt" //stop the program                        --6
        };

        var p = runProgram(program);
        System.out.println("the cost of running the program 20 integers is: " + p.cur_clock_cycle);
        // Optional: Assert specific register/memory state
        // Example: assertEquals("r2:210", p.output.get(X)); // if 1+2+...+20 is in memory
    }

    @Test
    public void testSum20ArrayReverse() {
        String[] program = {
                "copy 15 r1",  //initial memory address of array        --0
                "add 15 r1",   //Starting memory address of array is 30 --0
                "copy 0 r2",    //initialize sum                        --1
                "copy 0 r3",     //initialize counter                   --1
                "copy 15 r5", //first step of making r5 = 20            --2
                "add 5 r5", //making r5 equal to 20                     --2
                "add 0 r5", //empty space to make the load at pc 4      --3
                "add 0 r5", //empty stpace to make load at pc 4         --3
                "load r1 r4", //loading m[30(initially)] into r4        --4
                "add r4 r2", //add the loaded value into the sum registe--4
                "add -1 r1", //decrement the memory address             --5
                "add 1 r3", //increment the counter                     --5
                "compare r5 r3", //compare 20 to the counter            --6
                "blt -2", //branch back to 4                            --6
                "syscall 0", //print register values                    --7
                "halt" //stop the program                               --7
        };

        var p = runProgram(program);
        System.out.println("the cost of running the program 20 integers in reverse is: " + p.cur_clock_cycle);
        // Optional: Assert specific register/memory state
        // Example: assertEquals("r2:210", p.output.get(X)); // if 1+2+...+20 is in memory
    }



    @Test
    public void testLinkedListSum() {
        String[] program = {
                "copy 15 r1", //start of LL                --0
                "copy 0 r2", //sum                         --0
                "copy 0 r3", //counter                     --1
                "copy 15 r5",//used in comp                --1
                "add 5 r5", //making it 20                 --2
                "add 0 r5", //taking up space              --2
                "load r1 r4", //loading the first val      --3
                "add r4 r2", //adding it to total          --3
                "add 1 r1",//increment mem                 --4
                "load r1 r1", //making the cur memory addr --4
                "add 1 r3", //increment counter            --5
                "compare r5 r3", //compare counter to 20   --5
                "blt -3", //loop back to the first load    --6
                "syscall 0", //print registers             --6
                "halt", //stop the program                 --7
                "halt" //another halt for no reason        --7
        };

        var p = runProgramLL(program);
        System.out.println("the cost of running the linked list sum is: " + p.cur_clock_cycle);
    }


    private static Processor runProgram(String[] program) {
        var assembled = Assembler.assemble(program);
        var merged = Assembler.finalOutput(assembled);
        var m = new Memory();
        m.load(merged);
        var p = new Processor(m);
        p.run();
        return p;
    }

    private static Processor runProgramLL(String[] program) {
        var assembled = Assembler.assemble(program);
        var merged = Assembler.finalOutput(assembled);
        var m = linkedlistmem();
        m.load(merged);
        Word32 addr = new Word32();
        addr.copy(m.address);
        addr.copy(m.value);
        var p = new Processor(m);
        p.run();
        return p;
    }



}
