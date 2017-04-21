/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package console;

import java.awt.event.ActionEvent;
import java.util.*;
import java.io.*;
import javax.swing.*;

/**
 * Instances of this class represent console user interfaces to a simulated
 * computer equipped with a Micro-1 processor.
 */
public class Console {

    /**
     * Representation of the keyboard
     */
    private Scanner kbd = new Scanner(System.in);
    /**
     * Main memory of the simulated computer
     */
    Memory memory;
    /**
     * Processor of the simulated computer
     */
    Processor cpu;

    /**
     * Constructs a memory with specified number of cells, and constructs an
     * associated processor.
     *
     * @param cap the specified amount of memory
     */
    public Console(int cap) {
        memory = new Memory(cap);
        cpu = new Processor();
        cpu.setMemory(memory);
    }

    /**
     * Constructs a processor and a memory with 256 cells
     */
    public Console() {
        this(256);
    }

    /**
     * Loads hexadecimal numbers stored in fName into memory starting at address
     * 0. Resets PC to 0.
     *
     * @param fName the name of a file containing hex numbers
     */
    public boolean load(String fName) {
        try {
            File f = new File(fName);
            Scanner scan = new Scanner(f);
            int address = 0;
            while (scan.hasNext()) {
                memory.write(address++, scan.nextInt(16));
            }
            cpu.setPC(0);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    /**
     * Displays synopsis of all commands in the console window
     */
    public void help() {
        System.out.println("load fileName \t loads hex memory image into memory");
        System.out.println("memory \t\t dumps memory to console");
        System.out.println("registers \t dumps registers to console");
        System.out.println("step N \t\t executes next N instructions or until halt");
        System.out.println("help \t\t displays this message");
        System.out.println("quit \t\t terminate console");
    }

    /**
     * This is the read-execute-print loop for the console. It perpetually
     *
     * 1) displays a prompt 2) reads a command from the keyboard 3) executes the
     * command 4) displays the result Commands include quit, help, load (a
     * program from a file), memory (display contents of memory), registers
     * (display contents of registers), and step N (execute the next N
     * instructions.
     */
    public void controlLoop() {

        //System.out.println("type \"help\" for commands");
        while (true) {
            //System.out.print("-> ");
            String cmmd = kbd.next();
            if (cmmd.equals("quit")) {
                break;
            } else if (cmmd.equals("help")) {
                help();
            } else if (cmmd.equals("load")) {
                load(kbd.next());
                System.out.println("done");
            } else if (cmmd.equals("memory")) {
                memory.dump();
            } else if (cmmd.equals("registers")) {
                cpu.dump();
            } else if (cmmd.equals("step")) {
                int num;
                if (!kbd.hasNextInt()) {
                    num = 0;
                    kbd.nextLine();
                    System.out.println("invalid number of steps");
                } else {
                    num = kbd.nextInt();
                    boolean halt = false;
                    for (int i = 0; i < num && !halt; i++) {
                        if (!halt) {
                            halt = cpu.step();
                        }
                        if (halt) {
                            System.out.println("program terminated");
                            break;
                        }
                    }
                    System.out.println("done");
                }
            } else {
                System.out.println("unrecognized command: " + cmmd);
                if (kbd.hasNext()) {
                    kbd.nextLine();
                }
            }
        }
        System.out.println("bye");
    }

    /**
     * Creates a console (with memory and CPU), then starts the console's
     * control loop.
     *
     * @param args
     */
    public static void main(String[] args) {
        //first function, the start, typically "the main"
        Micro1Viewer gui = new Micro1Viewer();
        gui.BuildMainWindow();
        //Console console = new Console();
        //console.controlLoop();
    }

    /**
     *
     * @author Jocelyn
     */
    public static class Micro1Viewer extends JFrame {

        /**
         * Replace Console.java with Micro1Viewer.java. The viewer contains a
         * control panel (JPanel) with text fields (JTextField) showing the
         * current content of each register, and buttons (JButton) for the
         * Console commands: step, load, memory (dumps to the command console),
         * and registers (updates the text fields.)
         *
         */
        //Everything will be added to this panel
        public static JFrame MainWindow = new JFrame();

        //command buttons
        private static JButton B_Step = new JButton("Step");
        private static JButton B_Registers = new JButton("Registers");
        private static JButton B_Memory = new JButton("Memory");
        private static JButton B_Load = new JButton("Load");
        private static JButton B_Quit = new JButton("Quit");

        //register labels
        private static JLabel L_r0 = new JLabel("Register 0:");
        private static JLabel L_r1 = new JLabel("Register 1:");
        private static JLabel L_r2 = new JLabel("Register 2:");
        private static JLabel L_r3 = new JLabel("Register 3:");
        private static JLabel L_r4 = new JLabel("Register 4:");
        private static JLabel L_r5 = new JLabel("Register 5:");
        private static JLabel L_r6 = new JLabel("Register 6:");
        private static JLabel L_r7 = new JLabel("Register 7:");

        //register fields to be updated with each step
        private static JTextField TF_r0 = new JTextField(10);
        private static JTextField TF_r1 = new JTextField(10);
        private static JTextField TF_r2 = new JTextField(10);
        private static JTextField TF_r3 = new JTextField(10);
        private static JTextField TF_r4 = new JTextField(10);
        private static JTextField TF_r5 = new JTextField(10);
        private static JTextField TF_r6 = new JTextField(10);
        private static JTextField TF_r7 = new JTextField(10);

        //IR and PC components
        private static JLabel L_PC = new JLabel("PC:");
        private static JTextField TF_PC = new JTextField(10);
        private static JLabel L_IR = new JLabel("IR:");
        private static JTextField TF_IR = new JTextField(10);
        
        //user input
        private static JLabel L_Input = new JLabel("Input: ");
        public static JTextField TF_Input = new JTextField(20);

        //Memory text area and label
        private static JLabel L_Memory = new JLabel("Memory");
        public static JTextArea TA_Memory = new JTextArea();
        private static JScrollPane SP_Memory = new JScrollPane(TA_Memory);

        public static Console console = new Console();

        public void BuildMainWindow() {
            MainWindow.setTitle("Micro1Viewer");
            MainWindow.setSize(450, 500);
            MainWindow.setLocation(220, 180);
            MainWindow.setResizable(false);
            ConfigureMainWindow();
            //MainWindow_Action();
            MainWindow.setVisible(true);

        }//end BuildMainWindow

        public static void ConfigureMainWindow() {
            //Setting up Main window
            MainWindow.setBackground(new java.awt.Color(255, 255, 255));
            MainWindow.setSize(500, 320);
            MainWindow.getContentPane().setLayout(null);

            //Step Button
            B_Step.setBackground(new java.awt.Color(0, 0, 255));
            B_Step.setForeground(new java.awt.Color(255, 255, 255));
            B_Step.setEnabled(false);
            B_Step.setToolTipText("Executes next N instruction or until halt");
            MainWindow.getContentPane().add(B_Step);
            B_Step.setBounds(295, 40, 75, 25);

            //Register Button
            B_Registers.setBackground(new java.awt.Color(0, 0, 255));
            B_Registers.setForeground(new java.awt.Color(255, 255, 255));
            B_Registers.setEnabled(false);
            B_Registers.setToolTipText("Print out register contents");
            MainWindow.getContentPane().add(B_Registers);
            B_Registers.setBounds(10, 40, 100, 25);

            //Load button
            B_Load.setBackground(new java.awt.Color(0, 0, 255));
            B_Load.setForeground(new java.awt.Color(255, 255, 255));
            B_Load.setToolTipText("Upload a text file of hex instructions");
            MainWindow.getContentPane().add(B_Load);
            B_Load.setBounds(120, 40, 75, 25);

            //quit button
            B_Quit.setBackground(new java.awt.Color(0, 0, 255));
            B_Quit.setForeground(new java.awt.Color(255, 255, 255));
            B_Quit.setToolTipText("Press to exit");
            MainWindow.getContentPane().add(B_Quit);
            B_Quit.setBounds(208, 40, 75, 25);

            //memory button
            B_Memory.setBackground(new java.awt.Color(0, 0, 255));
            B_Memory.setForeground(new java.awt.Color(255, 255, 255));
            B_Memory.setEnabled(false);
            B_Memory.setToolTipText("Prints out memory contents to console");
            MainWindow.getContentPane().add(B_Memory);
            B_Memory.setBounds(380, 40, 100, 25);

            //Input Label
            MainWindow.getContentPane().add(L_Input);
            L_Input.setBounds(10, 10, 60, 20);

            //Input text field
            TF_Input.setForeground(new java.awt.Color(0, 0, 255));
            TF_Input.requestFocus();
            MainWindow.getContentPane().add(TF_Input);
            TF_Input.setBounds(70, 4, 260, 30);

            //Register 0 Label
            L_r0.setHorizontalAlignment(SwingConstants.CENTER);
            MainWindow.getContentPane().add(L_r0);
            L_r0.setBounds(0, 70, 100, 25);

            //Register 1 Label
            L_r1.setHorizontalAlignment(SwingConstants.CENTER);
            MainWindow.getContentPane().add(L_r1);
            L_r1.setBounds(0, 95, 100, 25);

            //Register 2 Label
            L_r2.setHorizontalAlignment(SwingConstants.CENTER);
            MainWindow.getContentPane().add(L_r2);
            L_r2.setBounds(0, 120, 100, 25);

            //Register 3 Label
            L_r3.setHorizontalAlignment(SwingConstants.CENTER);
            MainWindow.getContentPane().add(L_r3);
            L_r3.setBounds(0, 145, 100, 25);

            //Register 4 Label
            L_r4.setHorizontalAlignment(SwingConstants.CENTER);
            MainWindow.getContentPane().add(L_r4);
            L_r4.setBounds(0, 170, 100, 25);

            //Register 5 Label
            L_r5.setHorizontalAlignment(SwingConstants.CENTER);
            MainWindow.getContentPane().add(L_r5);
            L_r5.setBounds(0, 195, 100, 25);

            //Register 6 Label
            L_r6.setHorizontalAlignment(SwingConstants.CENTER);
            MainWindow.getContentPane().add(L_r6);
            L_r6.setBounds(0, 220, 100, 25);

            //Register 7 Label
            L_r7.setHorizontalAlignment(SwingConstants.CENTER);
            MainWindow.getContentPane().add(L_r7);
            L_r7.setBounds(0, 245, 100, 25);

            //PC Label
            L_PC.setHorizontalAlignment(SwingConstants.CENTER);
            MainWindow.getContentPane().add(L_PC);
            L_PC.setBounds(170, 100, 100, 25);

            //IR Label
            L_IR.setHorizontalAlignment(SwingConstants.CENTER);
            MainWindow.getContentPane().add(L_IR);
            L_IR.setBounds(170, 150, 100, 25);

            //Register 0 Text Field
            TF_r0.setForeground(new java.awt.Color(0, 0, 255));
            TF_r0.requestFocus();
            MainWindow.getContentPane().add(TF_r0);
            TF_r0.setBounds(100, 70, 100, 25);

            //Register 1 Text Field
            TF_r1.setForeground(new java.awt.Color(0, 0, 255));
            TF_r1.requestFocus();
            MainWindow.getContentPane().add(TF_r1);
            TF_r1.setBounds(100, 95, 100, 25);

            //Register 2 Text Field
            TF_r2.setForeground(new java.awt.Color(0, 0, 255));
            TF_r2.requestFocus();
            MainWindow.getContentPane().add(TF_r2);
            TF_r2.setBounds(100, 120, 100, 25);

            //Register 3 Text Field
            TF_r3.setForeground(new java.awt.Color(0, 0, 255));
            TF_r3.requestFocus();
            MainWindow.getContentPane().add(TF_r3);
            TF_r3.setBounds(100, 145, 100, 25);

            //Register 4 Text Field
            TF_r4.setForeground(new java.awt.Color(0, 0, 255));
            TF_r4.requestFocus();
            MainWindow.getContentPane().add(TF_r4);
            TF_r4.setBounds(100, 170, 100, 25);

            //Register 5 Text Field
            TF_r5.setForeground(new java.awt.Color(0, 0, 255));
            TF_r5.requestFocus();
            MainWindow.getContentPane().add(TF_r5);
            TF_r5.setBounds(100, 195, 100, 25);

            //Register 6 Text Field
            TF_r6.setForeground(new java.awt.Color(0, 0, 255));
            TF_r6.requestFocus();
            MainWindow.getContentPane().add(TF_r6);
            TF_r6.setBounds(100, 220, 100, 25);

            //Register 7 Text Field
            TF_r7.setForeground(new java.awt.Color(0, 0, 255));
            TF_r7.requestFocus();
            MainWindow.getContentPane().add(TF_r7);
            TF_r7.setBounds(100, 245, 100, 25);

            //PC Text Field
            TF_PC.setForeground(new java.awt.Color(0, 0, 255));
            TF_PC.requestFocus();
            MainWindow.getContentPane().add(TF_PC);
            TF_PC.setBounds(240, 100, 100, 25);

            //IR Text Field
            TF_IR.setForeground(new java.awt.Color(0, 0, 255));
            TF_IR.requestFocus();
            MainWindow.getContentPane().add(TF_IR);
            TF_IR.setBounds(240, 150, 100, 25);

            L_Memory.setHorizontalAlignment(SwingConstants.CENTER);
            MainWindow.getContentPane().add(L_Memory);
            L_Memory.setBounds(350, 70, 130, 16);

            TA_Memory.setColumns(15);
            TA_Memory.setFont(new java.awt.Font("Tahoma", 0, 12));
            TA_Memory.setForeground(new java.awt.Color(0, 0, 255));
            TA_Memory.setLineWrap(true);
            TA_Memory.setRows(5);
            TA_Memory.setEditable(false);

            SP_Memory.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            SP_Memory.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            SP_Memory.setViewportView(TA_Memory);
            MainWindow.getContentPane().add(SP_Memory);
            SP_Memory.setBounds(350, 90, 130, 180);

//--------------------------Load Button Listener------------------------------------------------------------------        
            //if user tries to press load w/no file name in the textfield to load
            B_Load.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (TF_Input.equals("")) {
                        JOptionPane.showMessageDialog(null, "Please type in a valid file name");
                    }

                    //read in file
                    String fname = TF_Input.getText();
                    if (console.load(fname) == true) {
                        JOptionPane.showMessageDialog(null, "Done loading instructions");
                        B_Step.setEnabled(true);
                        B_Registers.setEnabled(true);
                        B_Memory.setEnabled(true);
                        TF_Input.setText("");
                    } else {
                        JOptionPane.showMessageDialog(null, "Could not load file");
                        TF_Input.setText("");
                    }
                }
            });

//-------------------Quit Button-------------------------------------------------------------        
            B_Quit.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });

//-----------------------Memory Button--------------------------------------------------
//prints out memory when pressed
            B_Memory.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    /**
                     * it only prints out the last cell, need to figure out why
                     * and fix it...
                     */
                    String[] cells = console.memory.GUIdump();
                    for (int i = 0; i < cells.length; i++) {
                        TA_Memory.append(cells[i]);
                    }
                }
            });
//-----------------------------Registers Button--------------------------------------
//Updates register fields when pressed
            B_Registers.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    TF_r0.setText(console.cpu.getReg0Data());
                    TF_r1.setText(console.cpu.getReg1Data());
                    TF_r2.setText(console.cpu.getReg2Data());
                    TF_r3.setText(console.cpu.getReg3Data());
                    TF_r4.setText(console.cpu.getReg4Data());
                    TF_r5.setText(console.cpu.getReg5Data());
                    TF_r6.setText(console.cpu.getReg6Data());
                    TF_r7.setText(console.cpu.getReg7Data());
                    TF_PC.setText(console.cpu.getPCData());
                    TF_IR.setText(console.cpu.getIRData());
                }
            });

//----------------------------Step Button-------------------------------------------
//When the step button is pressed, it will go to next instruction
            B_Step.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int step;
                    try {
                        step = Integer.parseInt(TF_Input.getText());
                        if (step % 1 == 0) {
                            boolean halt = false;
                            for (int i = 0; i < step && !halt; i++) {
                                if (!halt) {
                                    halt = console.cpu.step();
                                }
                                if (halt) {
                                    System.out.println("program terminated");
                                    break;
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Please type in a whole number");
                        }
                    } catch (NumberFormatException n) {
                        JOptionPane.showMessageDialog(null, "Please type a valid whold number");
                    }
                    TF_Input.setText("");
                }
            });

        }//end
    }
}
