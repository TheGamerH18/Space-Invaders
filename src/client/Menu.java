package client;

import res.MenuButton;
import res.MenuTitle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Menu extends JPanel {

    private final MenuButton startgame;
    private final MenuButton ExitGame;

    private final Space_Invaders ex;

    Menu(Space_Invaders ex) {
        this.ex = ex;

        // Remove Layout for Absolut positioning
        setLayout(null);
        setBackground(Color.BLACK);

        // Creating Title
        String GameName = "Space Invaders";
        MenuTitle title = new MenuTitle(GameName);
        FontMetrics titlesize = getFontMetrics(title.getFont());
        title.setBounds(Commons.BOARD_WIDTH / 2 - titlesize.stringWidth(GameName) / 2,50, titlesize.stringWidth(GameName), titlesize.getHeight());
        add(title);

        // Creating Multiplayer Button
        MultiplayerGame = new MenuButton("Multiplayer");
        Dimension size = MultiplayerGame.getPreferredSize();
        MultiplayerGame.setBounds((Commons.BOARD_WIDTH / 2) - size.width / 2, Commons.BOARD_HEIGHT / 2 - size.height / 2, size.width, size.height);
        MultiplayerGame.addActionListener(new MenuCycle());
        add(MultiplayerGame);

        // Creating start button
        startgame = new MenuButton("Start Game");
        startgame.setBounds((Commons.BOARD_WIDTH / 2) - size.width / 2, Commons.BOARD_HEIGHT / 2 - size.height * 2, size.width, size.height);
        startgame.addActionListener(new MenuCycle());
        add(startgame);


        // Creating exit button
        ExitGame = new MenuButton("Exit Game");
        ExitGame.setBounds((Commons.BOARD_WIDTH / 2) - size.width / 2, Commons.BOARD_HEIGHT / 2 + size.height * 2, size.width, size.height);
        ExitGame.addActionListener(new MenuCycle());
        add(ExitGame);
    }

    public void setVisibleVar(boolean visibleVar){
        setVisible(visibleVar);
    }

    private class MenuCycle implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == startgame){
                setVisibleVar(false);
                ex.initGame();
                ex.remove(ex.menu);
            }
            if(e.getSource() == ExitGame) {
                System.exit(1);
            }
        }
    }
}
