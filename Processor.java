/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package console;

/**
 *
 * @author Jocelyn
 */
public class Processor {
    private int[] reg;
    private int PC;
    private int IR;
    private Memory memory;

    public Processor() {
        reg = new int [8];
        setMemory(memory);
    }
    
    public void setMemory(Memory mem) {
        memory = mem;
    }

    public void setPC(int i) {
        PC = i;
    }

    public void dump() {
        //printing out registers, PC, IR
        for(int i = 0; i<reg.length;i++){
            System.out.println("Register " + i+": "+reg[i]);
        }
        System.out.println("PC: "+PC);
        System.out.println("IR: "+IR);
    }

    public boolean step() {
        //incrmenting PC by one and setting new instruction
        //each instruction is then decoded, it's in hex right now..
        
        int bitmask = 0xF;  //15 in hex, 15 in binary = 1111
        IR = memory.read(PC++);//using PC to set IR by referencing memory
        if(IR==0)return true;
    
            //some register index b
            int b = IR&bitmask;
            //some register index a
            int a = (IR>>4)&bitmask;
            //instruction p
            //int p = IR>>8&bitmask;
            int p = IR>>8;
            
            
            switch(p){
                //Data Control
                case 1://load a b
                    reg[a] = memory.read(reg[b]);
                    break;
                case 2: //load a
                    reg[a] = memory.read(PC++);
                    break;
                case 3://store a b
                    memory.write(reg[a], reg[b]);
                    break;
                
                //Arithmetic
                case 4://add a b
                    reg[a] = reg[a] + reg[b];
                    break;
                case 5://mul a b
                    reg[a] = reg[a]*reg[b];
                    break;
                case 6://sub a b
                    reg[a] = reg[a] - reg[b];
                    break;
                case 7://div a b
                    if(b==0){
                        System.out.println("Error: b cannot equal 0");
                        break;
                    }
                    reg[a] = reg[a] / reg[b];
                    break;
                
                //Logic
                case 8://and a b
                    if(reg[a]!=0&&reg[b]!=0)reg[a]=1;
                    else{
                        reg[a]=0;
                    }
                    break;
                case 9://or a b
                    if(reg[a]!=0||reg[b]!=0)reg[a]=1;
                    else{
                        reg[a] = 0;
                    }
                    break;
                case 10://not a b
                    if(reg[b]!=0)reg[a]=0;
                    else{
                        reg[a]=1;
                    }
                    break;
                
                //Bitwise
                case 11://lshift a b
                    reg[a] = reg[b]<<1;
                    break;           
                case 12://rshift a b
                    reg[a] = reg[b]>>1;
                    break;            
                case 13://bwc a b
                    reg[a] = reg[a] & reg[b];
                    break;      
                case 14://bwd a b
                    reg[a] = reg[a]|reg[b];
                    break;        
                
                //Sequence Control
                case 15:
                    if(reg[a]!=0)PC = reg[b];
                    break;
//                case 0:
//                    System.out.println("Program has been halted");
//                    break; //halt, set up some kind of flag/indicator that the program has ended, even if
//                //you try to step, the data will stay the same, you can't add to the PC anymore
//                //add in breaks after each case
            }
            
        
        return false;
    }
    //For each step... in step...so each instruction, move on, then next instruction
    //shifting and masking used when fetching pab
    //treat numbers as all binary numbers
    //b can directly be masked with 15 = 1111, a>>4 bits &1111, p>>8bit
    //IR>>4 & 1111
    //0000ppppaaaabbbb
    
}
