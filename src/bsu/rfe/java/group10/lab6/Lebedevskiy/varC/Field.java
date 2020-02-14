package bsu.rfe.java.group10.lab6.Lebedevskiy.varC;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Field extends JPanel {
    private boolean paused;
    private ArrayList<BouncingBall> balls = new ArrayList<>(10);
    public HashMap<BouncingBall, BouncingBall> nmap;
    private Timer repaintTimer = new Timer(20, new ActionListener() {
        public void actionPerformed(ActionEvent ev) {
            repaint();
        }
    });
    public Field() {
        nmap = new HashMap<>();
        setBackground(Color.WHITE);
        repaintTimer.start();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D canvas = (Graphics2D) g;
        Handle_Punches();
        for (BouncingBall ball: balls) {
            ball.paint(canvas);
        }
    }
    public synchronized void addBall() {
        balls.add(new BouncingBall(this));
    }

    public synchronized void Handle_Punches ()
    {
        BouncingBall Temp;
        BouncingBall Temp1;
        for (int i=0; i < balls.size()-1;++i)
            for (int j=i+1; j < balls.size();++j)
            {
                Temp = balls.get(i);
                Temp1 = balls.get(j);
                if (((Temp.getY()-Temp1.getY())*(Temp.getY()-Temp1.getY())+(Temp.getX()-Temp1.getX())*(Temp.getX()-Temp1.getX()) <= (Temp.getR()+Temp1.getR())*(Temp.getR()+Temp1.getR())))// && ((Temp.getY()-Temp1.getY())*(Temp.getY()-Temp1.getY())+(Temp.getX()-Temp1.getX())*(Temp.getX()-Temp1.getX()) > Temp.getR()) && ((Temp.getY()-Temp1.getY())*(Temp.getY()-Temp1.getY())+(Temp.getX()-Temp1.getX())*(Temp.getX()-Temp1.getX()) > Temp1.getR()))
                {
                    if (nmap.get(Temp1) != Temp)
                    {
                        Temp.Punch(Temp1);
//                      Temp1.Punch(Temp);
                        nmap.put(Temp1, Temp);
                    }
                }
                else
                    if (nmap.get(Temp1) == Temp)
                        nmap.remove(Temp1);
            }
    }

    public synchronized void pause() {
        paused = true;
    }
    public synchronized void resume() {
        paused = false;
        notifyAll();
    }
    public synchronized void canMove(BouncingBall ball) throws InterruptedException {
        if (paused) {
            wait();
        }
    }
}