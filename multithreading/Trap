
package hello-world.multithreading;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Trap extends JPanel {

    private static final int HEIGHT = 400;
    private static final int WIDTH = 600;

    private Bridge bridge;
    private Rect rect;
    private Button button;
    private JFrame frame;

    Thread shipThread;
    Thread bridgeThread;
    Thread buttonThread;

    public Trap() {
        
        rect = new Rect();
        bridge = new Bridge();
        button = new Button(-5, 175);

        frame = new JFrame("Ship and bridge");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocation(400, 150);
        frame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        frame.addKeyListener(new Move());
        frame.setContentPane(this);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(12, 161, 8));
        g.fillRect(rect.x, rect.y, rect.height, rect.width);
        bridge.drawBridge(g);
        button.drawButton(g);
    }

    public void action() throws InterruptedException {
        buttonThread = new Thread(button);
        shipThread = new Thread(rect);
        bridgeThread = new Thread(bridge);

        buttonThread.start();
        shipThread.start();
        bridgeThread.start();

    }

    public class Button implements Runnable {
        
        private int x;
        private int y;

        public Button(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void drawButton(Graphics g) {
            g.setColor(new Color(40, 60, 80));
            g.fillOval(x, y, 10, 10);
        }

        public void pushToUpBridge() {
            if (rect.getX() <= (x + 15)) {
                button.notify();
            }
        }

        @Override
        public void run() {
            try {
                synchronized (button) {
                    System.out.println("button waiting");
                    button.wait();
                }
                pushToUpBridge();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class Bridge implements Runnable {

        private int bridgeHeight = 350;
        private Color color = Color.RED;
        boolean bridgeUpped = false;

        public Bridge() {
        }

        public void drawBridge(Graphics g) {
            g.setColor(color);
            g.fillRect(0, 200, 200, 200);
            g.fillRect(400, 200, 200, 200);
            g.setColor(new Color(2, 100, 20));
            g.fillRect(200, bridgeHeight, 200, 50);
        }

        public void upBridge() throws InterruptedException {

            while (bridgeHeight != 200) {
                bridgeHeight -= 5;
                frame.repaint();
                Thread.sleep(100);
            }
            bridgeUpped = true;
            System.out.println("bridge upped");
        }

        public void downBridge() throws InterruptedException {
            bridgeUpped = false;
            while (bridgeHeight != 350) {
                bridgeHeight += 5;
                frame.repaint();
                Thread.sleep(100);
            }
            System.out.println("bridge downed");
        }

        public boolean isBridgeUpped() {
            return bridgeUpped;
        }

        @Override
        public void run() {
            try {
                synchronized (bridge) {
                    System.out.println("bridge waiting");
                    bridge.wait();
                }
                upBridge();
                synchronized (bridge) {
                    bridge.wait();
                }
                downBridge();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public class Rect implements Runnable {
        private int x = 100;
        private int y = 160;
        private int height = 40;
        private int width = 40;

        private boolean onAnotherSide = false;

        public Rect() {
        }

        public void left() {
            if (onAnotherSide && !bridge.isBridgeUpped()) {
                if (x > 400) {
                    x -= 3;
                }
            } else if (x > 0) {
                x -= 3;
            }
        }

        public void right() {
            if (x == 400) {
                onAnotherSide = true;
            }
            if (bridge.isBridgeUpped()) {
                if (x < 550) {
                    x += 3;
                }
            } else if (onAnotherSide && !bridge.isBridgeUpped()) {
                if (x >= 397 && x < 555) {
                    x += 3;
                }
            } else if (x < 160) {
                x += 3;
            }

        }

        public int getX() {
            return x;
        }

        @Override
        public void run() {

        }
    }

    public class Move implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {

            if (e.getKeyCode() == e.VK_LEFT) {
                System.out.println("left [" + rect.x + "_" + rect.y + "]");
                rect.left();
                if (rect.getX() <= 10) {
                    try {
                        synchronized (bridge) {
                            bridge.notify();
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
                frame.repaint();
            }

            if (e.getKeyCode() == e.VK_RIGHT) {
                System.out.println("right [" + rect.x + "_" + rect.y + "]");
                rect.right();

                if (rect.getX() >= 400) {
                    try {
                        synchronized (bridge) {
                            bridge.notify();
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
                frame.repaint();
            }

        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }

    public static void main(String[] args) throws InterruptedException {

        Trap myWorld = new Trap();
        myWorld.action();

    }
}
