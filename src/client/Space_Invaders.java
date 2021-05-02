package client;

import javax.swing.*;
import java.awt.EventQueue;

public class Space_Invaders extends JFrame {

    public Board board;
    public Menu menu;
    public MultiplayerBoard mpboard;

    Space_Invaders(){
        initMenu();
        init();
    }

    private void init() {
        setTitle("Space-Invaders");
        setSize(Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private void initMenu() {
        menu = new Menu(this);
        add(menu);
    }

    public void initGame() {
        // Initialize Window
        if(board == null) {
            board = new Board();
            add(board);
        }
        board.grabFocus();
        board.restart();
    }

    public void initMultiplayer() {
        if(mpboard == null) {
            mpboard = new MultiplayerBoard();
            add(mpboard);
        }
        mpboard.grabFocus();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Space_Invaders ex = new Space_Invaders();
            ex.setVisible(true);
        });
    }
}