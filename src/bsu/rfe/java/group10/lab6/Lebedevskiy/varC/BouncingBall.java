package bsu.rfe.java.group10.lab6.Lebedevskiy.varC;

//import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class BouncingBall implements Runnable {
    private static final int MAX_RADIUS = 40;
    private static final int MIN_RADIUS = 3;
    private static final int MAX_SPEED = 15;
    private Field field;
    private int radius;
    private Color color;
    private double x;
    private double y;
    private double speed;
    private double speedX;
    private double speedY;
    private double angle;
    private boolean P;
    public BouncingBall(Field field) {
        this.field = field;
        radius = (int)(Math.random()*(MAX_RADIUS - MIN_RADIUS)) + MIN_RADIUS;
        speed = Math.round(5*MAX_SPEED / radius);///////////////////////////////////////////////////////////////////////////////Pay attention TODO
        if (speed>MAX_SPEED) {
            speed = MAX_SPEED;
        }
        angle = Math.random() * 2 * Math.PI - Math.PI;
        if (angle == -Math.PI)
            angle = Math.PI;
        speedX = speed*Math.cos(angle);
        speedY = speed*Math.sin(angle);
        color = new Color((float)Math.random(), (float)Math.random(),
                (float)Math.random());
        x = Math.random()*(field.getSize().getWidth()-2*radius) + radius;
        y = Math.random()*(field.getSize().getHeight()-2*radius) + radius;
        Thread thisThread = new Thread(this);
        thisThread.start();
    }
    public void run() {
        try {
            while(true) {
                field.canMove(this);
                if (x + speedX <= radius) {
                    speedX = -speedX;
                    x = radius;
                } else
                if (x + speedX >= field.getWidth() - radius) {
                    speedX = -speedX;
                    x=field.getWidth()-radius;
                } else
                if (y + speedY <= radius) {
                    speedY = -speedY;
                    y = radius;
                } else
                if (y + speedY >= field.getHeight() - radius) {
                    speedY = -speedY;
                    y=field.getHeight()-radius;
                } else {
                    x += speedX;
                    y += speedY;
                }
                Thread.sleep(16);
            }
        } catch (InterruptedException ex) {
            System.out.print("InterruptedException in Ball with x ");
            System.out.print(x);
            System.out.print(" and y ");
            System.out.print(y);
            System.out.print(" and radius ");
            System.out.println(radius);
        }
    }
    public void paint(Graphics2D canvas) {
        canvas.setColor(color);
        canvas.setPaint(color);
        Ellipse2D.Double ball = new Ellipse2D.Double(x-radius, y-radius,
                2*radius, 2*radius);
        canvas.draw(ball);
        canvas.fill(ball);
        canvas.setColor(new Color(255-color.getRed(), 255-color.getGreen(), 255-color.getBlue()));
        canvas.drawString(Double.toString(speed), (int) x, (int) y);
        canvas.drawString(Double.toString(speedX), (int) x+20, (int) y);
        canvas.drawString(Double.toString(speedY), (int) x+160, (int) y);
        if (P) {
            //canvas.drawString("Punch", (int) x, (int) y);
        }
    }
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    public int getR() {
        return radius;
    }
    /**
     * @implSpec
     * Angle_From_dx_dy in (-pi; pi]
     */
    private double Angle_From_dx_dy (double dy, double dx)
    {
        double alpha = dx != 0 ? Math.atan(dy/dx) : dy > 0 ? Math.PI/2 : -Math.PI;
//       alpha = alpha + (dx < 0) ? dy >= 0 ? Math.PI :  -Math.PI : 0
        if (dx < 0) {
            if (dy >= 0)
                alpha += Math.PI;
            else
                alpha -= Math.PI;
        }
        return alpha;
    }
    public synchronized void Punch (BouncingBall Ball)
    {
        P=true;
        Ball.P = true;
        assert false;
//        if (speed...)
        double dx = Ball.x - x;
        double dy = Ball.y - y;
        double a3 = Angle_From_dx_dy(dy, dx);
        double a1 = angle;
        double a2 = Ball.angle;
        double Y1NSpeed = speed*Math.sin(a3-a1);
        double Y2NSpeed = Ball.speed*Math.sin(a3-a2);
        double X1NSpeed = speed*Math.cos(a3-a1);
        double X2NSpeed = Ball.speed*Math.cos(a3-a2);
        double m1 = radius*radius;
        double m2 = Ball.radius*Ball.radius;
        double u1 = (2*m2*X2NSpeed+X1NSpeed*(m1-m2))/(m1+m2);
        double u2 = (2*m1*X1NSpeed+X2NSpeed*(m2-m1))/(m1+m2);
        double speed1 = Math.sqrt(u1*u1+Y1NSpeed*Y1NSpeed);
        double speed2 = Math.sqrt(u2*u2+Y2NSpeed*Y2NSpeed);
        a1 = a3 - Angle_From_dx_dy(Y1NSpeed, u1);
        a2 = a3 - Angle_From_dx_dy(Y2NSpeed, u2);
        speed =/* (int) */speed1;
        speedX = speed1*Math.cos(a1);
        speedY = speed1*Math.sin(a1);
        Ball.speed =/* (int) */speed2;
        Ball.speedX = speed2*Math.cos(a2);
        Ball.speedY = speed2*Math.sin(a2);
    }
}