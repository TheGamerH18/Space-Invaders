package client;

import com.blogspot.debukkitsblog.net.Datapackage;
import sprite.Alien;
import sprite.Player;
import sprite.Shot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ArrayList;

public class MultiplayerBoard extends JPanel {

    // TODO: Username Input
    String username = "user";
    private final MultiplayerNetwork network;
    private final Space_Invaders ex;

    private Dimension d;
    private final Player[] players = new Player[2];
    private final ArrayList<Shot> shots = new ArrayList<>();
    private ArrayList<Alien> aliens = new ArrayList<>();
    private final int myplayerid;

    private final String explImg = "/src/images/explosion.png";

    private final TAdapter tadapter;

    // Timer Class
    private class GameCycle implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            doGameCycle();
        }
    }

    // Timer Function
    private void doGameCycle() {
        update();
        repaint();
    }

    private class Sync implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            doSync();
        }
    }

    private void doSync() {
        int[][] playerpositions = network.getPlayerpos();
        int lastx = playerpositions[myplayerid][0];
        int lasty = playerpositions[myplayerid][1];
        if(lastx != players[myplayerid].getX() || lasty != players[myplayerid].getY()) {
            network.sendMessage("POS", myplayerid, players[myplayerid].getX(), players[myplayerid].getY());
        }
        if(myplayerid == 0) {
            players[1].setX(playerpositions[1][0]);
            players[1].setY(playerpositions[1][1]);
        } else {
            players[0].setX(playerpositions[0][0]);
            players[0].setY(playerpositions[0][1]);
        }
    }

    // KeyAdapter
    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            players[myplayerid].keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            players[myplayerid].keyPressed(e);
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_SPACE) {
                int x = players[myplayerid].getX();
                int y = players[myplayerid].getY();
                network.sendMessage(new Datapackage("NEW_SHOT", x, y, myplayerid));
            }
        }
    }


    // Constructor
    public MultiplayerBoard(String username, Space_Invaders ex) {
        this.username = username;
        this.ex = ex;
        network = new MultiplayerNetwork("localhost", username);
        tadapter = new TAdapter();
        myplayerid = (int) network.sendMessage(new Datapackage("AUTH", username)).get(1);
        System.out.println(myplayerid);
        if(myplayerid == -1) {
            System.exit(2);
        }
        initBoard();
        gameInit();
    }

    // Initialize Board
    private void initBoard(){
        addKeyListener(tadapter);
        setFocusable(true);
        d = new Dimension(Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);
        setBackground(Color.BLACK);

        Timer timer = new Timer(Commons.DELAY, new GameCycle());
        timer.start();

        Timer sync = new Timer(Commons.DELAY, new Sync());
        sync.start();

        gameInit();
    }

    // Initialize and Start Game
    private void gameInit(){
        while(network.getGameinfo() == 0) {
            System.out.println("Not enough players");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        players[0] = new Player();
        players[1] = new Player();
        network.sendMessage(new Datapackage("POS", myplayerid, players[myplayerid].getX(), players[myplayerid].getY()));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    private void drawPlayer(Graphics g){
        for(Player player : players) {
            if(player.isVisible()) {
                g.drawImage(player.getImage(), player.getX(), player.getY(), this);
            }
            if(player.isDying()) {
                player.die();
            }
        }
    }

    private void drawAliens(Graphics g) {
        for(Alien alien : aliens) {
            if(alien.isVisible()) {
                g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
            }
            if(alien.isDying()) {
                alien.die();
            }
        }
    }

    private void drawShots(Graphics g) {
        for(Shot shot : shots) {
            if(shot.isVisible()) {
                g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
            }
        }
    }

    private void doDrawing(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, d.width, d.height);
        g.setColor(Color.GREEN);

        g.drawLine(0, Commons.GROUND, Commons.BOARD_WIDTH, Commons.GROUND);
        drawPlayer(g);
        drawShots(g);
        drawAliens(g);
    }

    private void update() {
        String explImg = "/images/explosion.png";
        if(network.getGameinfo() == 2) {
            ex.mpboard = null;
            setVisible(false);
            ex.initMenu();
            ex.remove(ex.mpboard);
        }
        players[myplayerid].act();
        shots.clear();
        ArrayList<int[]> shotspos = network.getShots();
        for(int[] shotpos : shotspos) {
            shots.add(new Shot(shotpos[0], shotpos[1]));
        }
        ArrayList<int[]> alienspos = network.getAliens();
        for(int i = 0; i < alienspos.size(); i++) {
            if(aliens.size() != 24) {
                aliens.add(new Alien(alienspos.get(i)[0], alienspos.get(i)[1]));
            } else {
                aliens.get(i).setX(alienspos.get(i)[0]);
                aliens.get(i).setY(alienspos.get(i)[1]);
                if(aliens.get(i).isVisible() && alienspos.get(i)[2] == 1) {
                    aliens.get(i).setDying(true);
                    URL url = getClass().getResource(explImg);
                    ImageIcon ii = new ImageIcon(url);
                    aliens.get(i).setImage(ii.getImage());
                }
            }
        }
    }
}
