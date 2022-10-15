package miriSweeper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PreGame implements ActionListener {

    static int SIZE = 10;
    // Edit SIZE number for a bigger board (CLICK DEFAULT)
    //WARNING: Setting the SIZE too low (less than 4) WILL create bugs.

    static JButton[] buttons = new JButton[3];

    static JFrame frame;

    public static void main(String[] args) {

        PreGame instance = new PreGame();

        frame = new JFrame();
        frame.setSize(310,89);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setTitle("Select Difficulty");

        String[] buttonNames = {
                "EASY",
                "DEFAULT",
                "HARD"
        };

        for (int i = 0; i < 3; i++) {
            buttons[i] = new JButton(buttonNames[i]);
            buttons[i].setBounds(i*100,0,100,50);
            buttons[i].addActionListener(instance);
            frame.add(buttons[i]);
        }
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttons[0]) {
            System.out.println("EASY");
            MiriSweeper.SIZE = 6;
        }
        else if (e.getSource() == buttons[1]) {
            System.out.println("DEFAULT");
            frame.dispose();
        }
        else if (e.getSource() == buttons[2]) {
            System.out.println("HARD");
            MiriSweeper.SIZE = 18;
        }
        new MiriSweeper();
        frame.dispose();
    }
}
