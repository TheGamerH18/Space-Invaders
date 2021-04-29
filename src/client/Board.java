package client;

import sprite.Alien;
import sprite.Player;
import sprite.Shot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board extends JPanel {

    private Dimension d;
    private List<Alien> aliens;
    private Player player;
    private Shot shot;

    private int direction = -1;
    private int deaths = 0;

    private boolean inGame = true;
    private String explImg = "src/images/explosion.png";
    private String message = "Game Over";

    private Timer timer;

    private TAdapter tadapter;


    public Board() {
        tadapter = new TAdapter();
        initBoard();
        gameInit();
    }

    private void restart() {
        d = null;
        removeKeyListener(tadapter);
        timer = null;
        aliens = null;
        player = null;
        shot = null;
        inGame = true;
        direction = -1;
        deaths = 0;
        initBoard();
        gameInit();
    }

    /* Initialize Board
    Creating Basic stuff
     */
    private void initBoard() {
        addKeyListener(tadapter);
        setFocusable(true);
        d = new Dimension(Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);
        setBackground(Color.black);

        timer = new Timer(Commons.DELAY, new GameCycle());
        timer.start();

        gameInit();
    }

    /* Initialize Game
    Creating Aliens in rectangle Shape
    Creating Player and shot
     */
    private void gameInit() {
        aliens = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) {
                Alien alien = new Alien(Commons.ALIEN_INIT_X + 18 * j,
                        Commons.ALIEN_INIT_Y + 18 * i);
                aliens.add(alien);
            }
        }
        player = new Player();
        shot = new Shot();
    }

    // Draws Aliens
    private void drawAliens(Graphics g) {

        for (Alien alien : aliens) {

            if (alien.isVisible()) {

                g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
            }

            if (alien.isDying()) {

                alien.die();
            }
        }
    }

    // Draws Player
    private void drawPlayer(Graphics g) {
        if (player.isVisible()) {
            g.drawImage(player.getImage(), player.getX(), player.getY(), this);
        }
        if (player.isDying()) {
            player.die();
            inGame = false;
        }
    }

    // Draws Shot
    private void drawShot(Graphics g) {
        if (shot.isVisible()) {
            g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
        }
    }

    // Draws Bombs
    private void drawBombing(Graphics g) {

        for (Alien a : aliens) {

            Alien.Bomb b = a.getBomb();

            if (!b.isDestroyed()) {

                g.drawImage(b.getImage(), b.getX(), b.getY(), this);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    // Do Basic Drawing (Background, calling draw functions, calling gameover on game end)
    private void doDrawing(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, d.width, d.height);
        g.setColor(Color.green);

        if (inGame) {
            g.drawLine(0, Commons.GROUND, Commons.BOARD_WIDTH, Commons.GROUND);
            drawAliens(g);
            drawPlayer(g);
            drawShot(g);
            drawBombing(g);
        } else {
            if (timer.isRunning()) {
                timer.stop();
            }
            gameOver(g);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    // Game Over, showing if the player is killed
    private void gameOver(Graphics g) {

        g.setColor(Color.black);
        g.fillRect(0, 0, Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, Commons.BOARD_WIDTH / 2 - 30, Commons.BOARD_WIDTH - 100, 50);
        g.setColor(Color.white);
        g.drawRect(50, Commons.BOARD_WIDTH / 2 - 30, Commons.BOARD_WIDTH - 100, 50);

        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics fontMetrics = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message, (Commons.BOARD_WIDTH - fontMetrics.stringWidth(message)) / 2,
                Commons.BOARD_WIDTH / 2);
        restart();
    }

    private void update() {
        // Check if the Player has killed all Aliens
        if (deaths == Commons.NUMBER_OF_ALIENS_TO_DESTROY) {
            inGame = false;
            timer.stop();
            message = "Game won!";
            restart();
        }

        // player
        player.act();

        // shot
        if (shot.isVisible()) {

            int shotX = shot.getX();
            int shotY = shot.getY();

            // Check if shot is touching an Alien, resulting in Killing the Alien and removing the shot
            for (Alien alien : aliens) {

                int alienX = alien.getX();
                int alienY = alien.getY();

                if (alien.isVisible()
                        && shot.isVisible()
                        && shotX >= (alienX)
                        && shotX <= (alienX + Commons.ALIEN_WIDTH)
                        && shotY >= (alienY)
                        && shotY <= (alienY + Commons.ALIEN_HEIGHT))
                {
                    ImageIcon ii = new ImageIcon(explImg);
                    alien.setImage(ii.getImage());
                    alien.setDying(true);
                    deaths++;
                    shot.die();
                }
            }

            // Check if the shot is at the max top, otherwise move it up
            int y = shot.getY();
            y -= 4;
            if (y < 0) {
                shot.die();
            } else {
                shot.setY(y);
            }
        }

        /* The Check if the Alien needs to change to next Line and the Alien Movement HAS to be Seperate!
        Otherwise every single Alien would touch the Border before changing lane, resulting in the loss of the Shape
         */

        // aliens
        for (Alien alien : aliens) {
            // Check if the Alien needs to move in next Line
            int x = alien.getX();
            if (x >= Commons.BOARD_WIDTH - Commons.BORDER_RIGHT && direction != -1) {
                direction = -1;
                for (Alien a2 : aliens) {
                    a2.setY(a2.getY() + Commons.GO_DOWN);
                }
            }

            if (x <= Commons.BORDER_LEFT && direction != 1) {
                direction = 1;
                for (Alien a : aliens) {
                    a.setY(a.getY() + Commons.GO_DOWN);
                }
            }
        }

        // Alien Movement
        for (Alien alien : aliens) {
            if (alien.isVisible()) {
                int y = alien.getY();
                // Check if the Alien is touching the Ground, resulting in an Invasion
                if (y > Commons.GROUND - Commons.ALIEN_HEIGHT) {
                    inGame = false;
                    message = "Invasion!";
                    restart();
                }
                alien.act(direction);
            }
        }

        // bombs
        Random generator = new Random();

        for (Alien alien : aliens) {

            // Gen Random number
            int shot = generator.nextInt(80);
            Alien.Bomb bomb = alien.getBomb();

            // Spawn Bomb if a bomb could be spawned
            if (shot == Commons.CHANCE && alien.isVisible() && bomb.isDestroyed()) {
                // Sppawn Bomb
                bomb.setDestroyed(false);
                bomb.setX(alien.getX());
                bomb.setY(alien.getY());
            }

            // Get current Cords of Bomb and Player
            int bombX = bomb.getX();
            int bombY = bomb.getY();
            int playerX = player.getX();
            int playerY = player.getY();

            // Check if Bomb is destroying the Player
            if (player.isVisible()
                    && !bomb.isDestroyed()
                    && bombX >= (playerX)
                    && bombX <= (playerX + Commons.PLAYER_WIDTH)
                    && bombY >= (playerY)
                    && bombY <= (playerY + Commons.PLAYER_HEIGHT))
            {
                ImageIcon ii = new ImageIcon(explImg);
                player.setImage(ii.getImage());
                player.setDying(true);
                bomb.setDestroyed(true);
            }

            // Move Bomb Towards Ground
            if (!bomb.isDestroyed()) {
                bomb.setY(bomb.getY() + 1);
                // Destroy Bomb when touching Ground
                if (bomb.getY() >= Commons.GROUND - Commons.BOMB_HEIGHT) bomb.setDestroyed(true);
            }
        }
    }

    private void doGameCycle() {
        update();
        repaint();
    }

    private class GameCycle implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            doGameCycle();
        }
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {

            player.keyPressed(e);
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_SPACE && inGame && !shot.isVisible()) {
                int x = player.getX();
                int y = player.getY();
                shot = new Shot(x, y);
            }
        }
    }
}
