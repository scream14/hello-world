package hello-world.multithreading;

import javax.swing.*;
import java.awt.*;


public class FlyingBalls extends JPanel {

    private static final int HEIGHT = 500;
    private static final int WIDTH = 400;

    private Ball[] balls;
    private Color[] colors = new Color[]{
            Color.yellow, Color.black, Color.red, Color.green, Color.orange,
            Color.cyan, Color.LIGHT_GRAY, Color.blue, Color.magenta, Color.white};

    public FlyingBalls() {
        balls = getBallsArray();
        JFrame frame = new JFrame("Fly, Balls, fly!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(600, 150);
        frame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        frame.setContentPane(this);
        frame.pack();
        frame.setVisible(true);
    }

    public void action() throws InterruptedException {
        for (Ball b : balls) {
            Thread t = new Thread(b);
                    t.start();
            System.out.println(t.getName());

        }
    }

    private Ball[] getBallsArray() {
        Ball[] ovals = new Ball[10];
        for (int i = 0; i < ovals.length; i++) {
            ovals[i] = new Ball(0, (i * 20), howManySleep(), giveMeColor());
        }
        return ovals;
    }

    private Color giveMeColor() {
        return colors[(int) (Math.random() * colors.length)];
    }

    private long howManySleep() {
        return (long) (Math.random() * 50);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Ball b : balls) {
            g.setColor(b.color);
            g.fillOval(b.x, b.y, b.width, b.height);
        }
    }

    public class Ball implements Runnable {

        int x;
        int y;
        int width = 16;
        int height = 16;
        long sleep;
        Color color;

        public Ball(int x, int y, long sleep, Color color) {

            this.x = x;
            this.y = y;
            this.sleep = sleep;
            this.color = color;

        }

        @Override
        public void run() {
            while (true) {
                right();
                left();
            }
        }

        private void right() {
            while (x != (400 - 25) && y != 450) {
                x++;
                y++;
                repaint();
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void left() {
            while (x != 0 && y != 0) {
                x--;
                y--;
                repaint();
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void main(String[] args) throws InterruptedException {

        FlyingBalls fb = new FlyingBalls();
        fb.action();

    }
}

