package client;

import javax.swing.*;
import java.awt.EventQueue;

public class Space_Invaders extends JFrame {

    Space_Invaders(){
        initUI();
    }

    private void initUI() {
        add(new Board());

        setTitle("Space-Invaders");
        setSize(Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            Space_Invaders ex = new Space_Invaders();
            ex.setVisible(true);
        });
    }

}
