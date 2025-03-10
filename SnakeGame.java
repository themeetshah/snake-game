import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {
    private final int TILE_SIZE = 25;
    private final int SCREEN_WIDTH = 600;
    private final int SCREEN_HEIGHT = 600;
    private final int TOTAL_TILES = (SCREEN_WIDTH * SCREEN_HEIGHT) / (TILE_SIZE * TILE_SIZE);

    private final int x[] = new int[TOTAL_TILES];
    private final int y[] = new int[TOTAL_TILES];
    private int bodyParts = 5;
    private int foodX, foodY;
    private char direction = 'R';
    private boolean running = false;
    private Timer timer;

    public SnakeGame() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new KeyHandler());
        startGame();
    }

    public void startGame() {
        spawnFood();
        running = true;
        timer = new Timer(200, this);
        timer.start();
    }

    public void spawnFood() {
        Random random = new Random();
        foodX = random.nextInt(SCREEN_WIDTH / TILE_SIZE) * TILE_SIZE;
        foodY = random.nextInt(SCREEN_HEIGHT / TILE_SIZE) * TILE_SIZE;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            g.setColor(Color.RED);
            g.fillOval(foodX, foodY, TILE_SIZE, TILE_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                } else {
                    g.setColor(new Color(45, 180, 0));
                }
                g.fillRect(x[i], y[i], TILE_SIZE, TILE_SIZE);
            }
        } else {
            gameOver(g);
        }
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U' -> y[0] -= TILE_SIZE;
            case 'D' -> y[0] += TILE_SIZE;
            case 'L' -> x[0] -= TILE_SIZE;
            case 'R' -> x[0] += TILE_SIZE;
        }
    }

    public void checkFoodCollision() {
        if (x[0] == foodX && y[0] == foodY) {
            bodyParts++;
            spawnFood();
        }
    }

    public void checkCollisions() {
        for (int i = bodyParts; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
            }
        }
        if (x[0] < 0 || x[0] >= SCREEN_WIDTH || y[0] < 0 || y[0] >= SCREEN_HEIGHT) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
        g.setFont(new Font("Arial", Font.PLAIN, 30));
        g.drawString("Score: " + (bodyParts - 5),
                (SCREEN_WIDTH - metrics.stringWidth("Score: " + (bodyParts - 5)) + TILE_SIZE) / 2,
                (SCREEN_HEIGHT / 2) + (2 * TILE_SIZE));

        System.out.println("Game Over! Your score: " + (bodyParts - 5));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkFoodCollision();
            checkCollisions();
        }
        repaint();
    }

    private class KeyHandler extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT -> {
                    if (direction != 'R')
                        direction = 'L';
                }
                case KeyEvent.VK_RIGHT -> {
                    if (direction != 'L')
                        direction = 'R';
                }
                case KeyEvent.VK_UP -> {
                    if (direction != 'D')
                        direction = 'U';
                }
                case KeyEvent.VK_DOWN -> {
                    if (direction != 'U')
                        direction = 'D';
                }
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
