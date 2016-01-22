package hello-world.multithreading;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Goalkeeper extends JPanel {

    private static final int HEIGHT = 500;
    private static final int WIDTH = 400;


    private int cached = 9;
    private int counter;
    private Lock locker;
    private Rect rect;

    private Ball[] balls;
    private Color[] colors = new Color[]{
            Color.yellow, Color.black, Color.red, Color.green, Color.orange,
            Color.cyan, Color.LIGHT_GRAY, Color.blue, Color.magenta, Color.white};

    public CountDownLatch countDownLatch = new CountDownLatch(2);
    JLabel labelCounter;
    JFrame frame;

    public Goalkeeper() {

        balls = getBallsArray();
        rect = new Rect();
        locker = new ReentrantLock();
        labelCounter = new JLabel("score: " + counter);
        Font font = new Font("Cooper", Font.PLAIN, 20);
        labelCounter.setFont(font);

        frame = new JFrame("Goalkeeper");
        this.add(labelCounter);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocation(600, 150);
        frame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        frame.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if ((e.getKeyCode() == e.VK_A) && (rect.x > 0)) {
                    rect.x -= 5;
                } else if ((e.getKeyCode() == e.VK_D) && (rect.x < 365)) {
                    rect.x += 5;
                } else if ((e.getKeyCode() == e.VK_W) && (rect.y > 0)) {
                    rect.y -= 5;
                } else if ((e.getKeyCode() == e.VK_S) && (rect.y < 430)) {
                    rect.y += 5;
                }
                frame.repaint();
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        frame.setContentPane(this);
        frame.pack();
        frame.setVisible(true);
    }

    public void action() throws InterruptedException {

        for (Ball b : balls) {
            Thread t = new Thread(b);
            t.start();
            countDownLatch.countDown();
            System.out.println(countDownLatch.getCount());
            Thread.sleep(100);
        }

    }

    private Ball[] getBallsArray() {
        Ball[] ballz = new Ball[9];
        for (int i = 0; i < ballz.length; i++) {
            ballz[i] = new Ball((i * 45), 0, howManySleep(), giveMeColor());
        }
        return ballz;
    }

    private Color giveMeColor() {
        return colors[(int) (Math.random() * colors.length)];
    }

    private long howManySleep() {
        return (long) (Math.random() * 30 + 5);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Ball b : balls) {
            g.setColor(b.color);
            g.fillOval(b.x, b.y, b.width, b.height);
        }
        rect.drawRect(g);

    }

    public class Rect implements Runnable {
        int x = 200;
        int y = 430;
        int height = 16;
        int width = 16;

        public Rect() {
        }

        public void drawRect(Graphics g) {
            g.setColor(new Color(255, 1, 0));
            g.fillRect(x, y, width, height);
        }

        @Override
        public void run() {

        }
    }

    public class Ball implements Runnable {

        int x;
        int y;
        int width = 16;
        int height = 16;
        long sleep = 50;
        Color color;

        volatile boolean stopped;

        public Ball(int x, int y, long sleep, Color color) {

            this.x = x;
            this.y = y;
            this.sleep = sleep;
            this.color = color;

        }

        @Override
        public void run() {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (!stopped) {
                down();
                if (!stopped) {
                    up();
                }
            }
        }

        public void stop() {
            this.stopped = true;
            cached--;
            if (cached == 0) {
                System.out.println("GAME OVER");
                JOptionPane remark;
                remark = new JOptionPane();
                remark.showMessageDialog(frame, "GAME OVER! Your score: " + counter);
                frame.dispose();
                try {
                    new Goalkeeper().action();
                } catch (InterruptedException e) {

                }
            }
        }

        private void down() {
            while (x != (400 - 25) && y != 450) {
                y++;
                if (y == 449) {
                    locker.lock();
                    counter++;
                    System.out.println("COAL! " + counter + " " + Thread.currentThread().getName());
                    labelCounter.setText("score: " + counter);
                    locker.unlock();
                }
                if ((x == rect.x) && (y == rect.y)) {
                    System.out.println("You catch " + Thread.currentThread().getName());
                    this.stop();
                    break;
                }

                repaint();
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void up() {
            while (x >= 0 && y >= 0) {
                y--;
                if ((x == rect.x) && (y == rect.y)) {
                    System.out.println("You catch " + Thread.currentThread().getName());
                    frame.repaint();
                    this.stop();
                    break;
                }
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

        Goalkeeper goalkeeper = new Goalkeeper();
        goalkeeper.action();

    }
}
