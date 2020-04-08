package bsu.rfe.java.group10.lab6.Lebedevskiy.varC;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
//import java.io.File;
//import java.io.IOException;
//import javax.imageio.ImageIO;
import javax.swing.*;

public class MyFrame extends JFrame {
    private static final int WIDTH = 700;
    private static final int HEIGHT = 500;
    private JMenuItem pauseMenuItem;
    private JMenuItem resumeMenuItem;
    private Field field = new Field();
    public MyFrame(){
        super("Balls");
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH)/2,
                (kit.getScreenSize().height - HEIGHT)/2);
        setIconImage(kit.getImage("src/bsu/rfe/java/group10/lab6/Lebedevskiy/varC/Ball.png"));//new ImageIcon(ImageIO.read(new File("Ball.png"))));
        setExtendedState(MAXIMIZED_BOTH);
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu ballMenu = new JMenu("Balls");
        Action addBallAction = new AbstractAction("Add ball") {
            public void actionPerformed(ActionEvent event) {
                field.addBall();
                if (!pauseMenuItem.isEnabled() &&
                        !resumeMenuItem.isEnabled()) {
                    pauseMenuItem.setEnabled(true);
                }
            }
        };
        menuBar.add(ballMenu);
        ballMenu.add(addBallAction);
        JMenu controlMenu = new JMenu("Control");
        menuBar.add(controlMenu);
        Action pauseAction = new AbstractAction("Pause"){
            public void actionPerformed(ActionEvent event) {
                field.pause();
                pauseMenuItem.setEnabled(false);
                resumeMenuItem.setEnabled(true);
            }
        };
        pauseMenuItem = controlMenu.add(pauseAction);
        pauseMenuItem.setEnabled(false);
        Action resumeAction = new AbstractAction("Continue") {
            public void actionPerformed(ActionEvent event) {
                field.resume();
                pauseMenuItem.setEnabled(true);
                resumeMenuItem.setEnabled(false);
            }
        };
        resumeMenuItem = controlMenu.add(resumeAction);
        resumeMenuItem.setEnabled(false);
        getContentPane().add(field, BorderLayout.CENTER);
    }
    public void Add_Balls(int n)
    {
        pauseMenuItem.setEnabled(true);
        for (int i = 0; i < n; ++i)
            field.addBall();
    }
    public static void main(String[] args) {
        MyFrame frame = new MyFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
//        try {
            frame.Add_Balls(8);//Integer.parseInt((String) JOptionPane.showInputDialog(frame, "Count of balls", "Balls", JOptionPane.QUESTION_MESSAGE, new ImageIcon(ImageIO.read(new File("src/bsu/rfe/java/group10/lab6/Lebedevskiy/varC/Ball.png"))), /*(Object[])*/ null, 8)));
//        }
//        catch (IOException ex)
//        {
//            System.out.println("Cannot find picture src/bsu/rfe/java/group10/lab6/Lebedevskiy/varC/Ball.png");
//        }
    }
}