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
        this.username = username;
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
        while(!getObjects()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {}
        }
    }

    // Pull Objects from network
    private boolean getObjects() {
        if(network.getShots() == null || network.getAliens() == null || network.getPlayers() == null) {
            return false;
        } else {
            players = network.getPlayers();
            shots = network.getShots();
            aliens = network.getAliens();
            return true;
        }
    }
}
