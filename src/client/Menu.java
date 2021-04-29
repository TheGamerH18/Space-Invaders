package client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JPanel {

    private final Space_Invaders ex;

    Menu(Space_Invaders ex) {
        this.ex = ex;
        JButton startgame = new JButton("Start Game");
        startgame.addActionListener(new MenuCycle());
        startgame.setBounds(50,50,50,25);
        add(startgame);
    }

    public void setVisibleVar(boolean visibleVar){
        setVisible(visibleVar);
    }

    private class MenuCycle implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            setVisibleVar(false);
            ex.initGame();
            ex.remove(ex.menu);
        }
    }
}
