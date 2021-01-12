package bsu.rfe.java.group10.lab6.Lebedevskiy.varC;

import javafx.util.Pair;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.concurrent.Semaphore;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Field extends JPanel {
    private boolean paused;
    private final ArrayList<Pair<BouncingBall, BouncingBall>> PairPunches;
    //    public HashMap<BouncingBall, BouncingBall> nMap;
//    static int k=0;
    private final ArrayList<BouncingBall> balls = new ArrayList<>(10);
    private final Semaphore SemForBalls;
    private Timer repaintTimer = new Timer(20, new ActionListener() {
        public void actionPerformed(ActionEvent ev) {
            repaint();
        }
    });
    Field() {
//        nMap = new HashMap<>();
        SemForBalls = new Semaphore(1);
        PairPunches = new ArrayList<>(10);
        setBackground(Color.WHITE);
        repaintTimer.start();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D canvas = (Graphics2D) g;
        try {
            SemForBalls.acquire();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < balls.size(); ++i)
            balls.get(i).paint(canvas);
        Handle_Punches();
        SemForBalls.release();
    }
    void addBall() {
        try {
            SemForBalls.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        balls.add(new BouncingBall(this));
        SemForBalls.release();
    }

    private void Handle_Punches()
    {
        BouncingBall Temp;
        BouncingBall Temp1;
         synchronized (PairPunches){
             for (int i = 0; i < balls.size() - 1; ++i)
                for (int j = i + 1; j < balls.size(); ++j) {
                    Temp = balls.get(i);
                    Temp1 = balls.get(j);
                    Pair<BouncingBall, BouncingBall> P = new Pair<>(Temp, Temp1);//because in the next iteration it can recalculate punch already bounced balls
                    /*nMap.get(Temp1) == Temp*/
                    if (((Temp.getY()-Temp1.getY())*(Temp.getY()-Temp1.getY())+(Temp.getX()-Temp1.getX())*(Temp.getX()-Temp1.getX()) <= (Temp.getR()+Temp1.getR())*(Temp.getR()+Temp1.getR())))// && ((Temp.getY()-Temp1.getY())*(Temp.getY()-Temp1.getY())+(Temp.getX()-Temp1.getX())*(Temp.getX()-Temp1.getX()) > Temp.getR()*Temp.getR()) && ((Temp.getY()-Temp1.getY())*(Temp.getY()-Temp1.getY())+(Temp.getX()-Temp1.getX())*(Temp.getX()-Temp1.getX()) > Temp1.getR()*Temp1.getR()))
                    {
                        if (!PairPunches.contains(P)/*nMap.get(Temp1) != Temp*/){// || (((Temp.getY()-Temp1.getY())*(Temp.getY()-Temp1.getY())+(Temp.getX()-Temp1.getX())*(Temp.getX()-Temp1.getX()) > Temp.getR()*Temp.getR()) && ((Temp.getY()-Temp1.getY())*(Temp.getY()-Temp1.getY())+(Temp.getX()-Temp1.getX())*(Temp.getX()-Temp1.getX()) > Temp1.getR()*Temp1.getR()))) {
//                        System.out.println("Here"+k++);
                            //Temp.Punch(Temp1);
//                      Temp1.Punch(Temp);
                            PairPunches.add(P);
                            double dx = Temp1.getX() - Temp.getX();
                            double dy = Temp1.getY() - Temp.getY();
                            double a3 = BouncingBall.Angle_From_dx_dy(dy, dx);
//                          double a33 = a3*180/Math.PI;
                            double a1 = Temp.getAngle();
//                          double a11 = a1*180/Math.PI;
                            double a2 = Temp1.getAngle();
//                          double a22 = a2*180/Math.PI;
                            double Y1NSpeed = Temp.getSpeed() * Math.sin(a3 - a1);
                            double Y2NSpeed = Temp1.getSpeed() * Math.sin(a3 - a2);
                            double X1NSpeed = Temp.getSpeed() * Math.cos(a3 - a1);
                            double X2NSpeed = Temp1.getSpeed() * Math.cos(a3 - a2);
                            double m1 = Temp.getR() * Temp.getR();
                            double m2 = Temp1.getR() * Temp1.getR();
                            double u1 = (2 * m2 * X2NSpeed + X1NSpeed * (m1 - m2)) / (m1 + m2);
                            double u2 = (2 * m1 * X1NSpeed + X2NSpeed * (m2 - m1)) / (m1 + m2);
                            double speed1 = Math.sqrt(u1 * u1 + Y1NSpeed * Y1NSpeed);
                            double speed2 = Math.sqrt(u2 * u2 + Y2NSpeed * Y2NSpeed);
                            a1 = a3 - BouncingBall.Angle_From_dx_dy(Y1NSpeed, u1);
                            a2 = a3 - BouncingBall.Angle_From_dx_dy(Y2NSpeed, u2);
                            Temp.setSpeed(speed1);// < BouncingBall.MAX_SPEED ? speed1 : BouncingBall.MAX_SPEED;
                            Temp.setSpeedX(speed1 * Math.cos(a1));
                            Temp.setSpeedY(speed1 *Math.sin(a1));
                            Temp1.setSpeed(speed2);// <BouncingBall.MAX_SPEED ? speed2 : BouncingBall.MAX_SPEED;
                            Temp1.setSpeedX(speed2 * Math.cos(a2));
                            Temp1.setSpeedY(speed2 * Math.sin(a2));
//                      nMap.put(Temp1, Temp);
                        }
                    } else
                        PairPunches.remove(P);
//                  nMap.remove(Temp1);
                }

         }
    }

    synchronized void pause() {
        paused = true;
    }
    synchronized void resume() {
        paused = false;
        notifyAll();
    }
    synchronized void canMove() throws InterruptedException {
        if (paused) {
            wait();
        }
    }
}