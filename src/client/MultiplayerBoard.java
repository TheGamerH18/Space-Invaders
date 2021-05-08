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
import java.util.List;

public class MultiplayerBoard extends JPanel {

    // TODO: Username Input
    String username = "user";
    private MultiplayerNetwork network;
    private Space_Invaders ex;

    private Dimension d;
    private List<Alien> aliens;
    private Player[] players = new Player[2];
    private List<Shot> shots;
    private int myplayerid;

    private boolean inGame = true;
    private final String explImg = "/src/images/explosion.png";
    private String message = "Game Over";

    private Timer timer;

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

    // KeyAdapter
    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            players[myplayerid].keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            players[myplayerid].keyPressed(e);
        }
    }


    // Constructor
    public MultiplayerBoard(String username) {
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

        timer = new Timer(Commons.DELAY, new GameCycle());
        timer.start();

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
            } if(player.isDying()) {
                player.die();
            }
        }
    }

    private void doDrawing(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, d.width, d.height);
        g.setColor(Color.GREEN);

        if(inGame) {
            g.drawLine(0, Commons.GROUND, Commons.BOARD_WIDTH, Commons.GROUND);
            drawPlayer(g);
        }
    }

    private void update() {
        int lasty = players[myplayerid].getY();
        int lastx = players[myplayerid].getX();
        if(network.getGameinfo() == 2) {
            ex.mpboard = null;
            setVisible(false);
            ex.initMenu();
            ex.remove(ex.mpboard);
        }
        players[myplayerid].act();
        if(lastx != players[myplayerid].getX() || lasty != players[myplayerid].getY()) {
            network.sendMessage("POS", myplayerid, players[myplayerid].getX(), players[myplayerid].getY());
        }
        int[][] playerpos = network.getPlayerpos();
        players[0].setX(playerpos[0][0]);
        players[0].setY(playerpos[0][1]);
        players[1].setX(playerpos[1][0]);
        players[1].setY(playerpos[1][1]);
    }
}
