/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package galaga;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Computer
 */
class OpeningOptionPanel {

    private Container contentPane;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JLabel label1;
    public static JFrame openingOptionPanel = new JFrame("Options");

    public OpeningOptionPanel() {
        openingOptionPanel.setSize(400, 100);
        openingOptionPanel.setResizable(false);
        openingOptionPanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contentPane = openingOptionPanel.getContentPane();
        contentPane.setLayout(new FlowLayout());
        button1 = new JButton("Oldy, but Goody!");
        button1.addActionListener(new Original());
        button2 = new JButton("Panic Mode 1");
        button2.addActionListener(new PanicMode1());
        button3 = new JButton("Panic Mode 2");
        button3.addActionListener(new PanicMode2());
        label1 = new JLabel("Please select an interface:                                                           ");
        contentPane.add(label1);
        contentPane.add(button1);
        contentPane.add(button2);
        contentPane.add(button3);
        openingOptionPanel.setVisible(true);
    }

    private static class Original implements ActionListener {

        public Original() {
            //Performs no action, other than exiting out of the window
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            openingOptionPanel.setVisible(false);
            openingOptionPanel.dispose();
        }
    }

    private static class PanicMode1 implements ActionListener {

        public PanicMode1() {
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            openingOptionPanel.setVisible(false);
            openingOptionPanel.dispose();
            Galaga.panicMode1Active = true;
        }
    }

    private static class PanicMode2 implements ActionListener {

        public PanicMode2() {
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            openingOptionPanel.setVisible(false);
            openingOptionPanel.dispose();
            Galaga.panicMode2Active = true;
        }
    }
}
