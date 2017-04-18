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
class Memory {
    private int CAP;
    private int[] cell;

    public Memory() {
        this(256);
    }

    Memory(int cap) {
        this.CAP = cap;
        cell = new int[cap];
    }
    
    public int read(int address){
        return cell[address];
    }

    public void write(int address, int data) {
        cell[address] = data; 
    }

    public void dump() {
        //print out all the cells and their contents
        for(int i = 0; i<cell.length;i++){
            System.out.println("Cell["+i+"] = " + cell[i]);
        }
    }

}
