package res;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class MenuButton extends JButton {

    public MenuButton(String text) {
        this.setFont(new Font("Helvetica", Font.BOLD, 14));
        setForeground(Color.WHITE);
        setBackground(new Color(0, 32, 48));
        Border line = new LineBorder(Color.WHITE);
        Border margin = new EmptyBorder(5 ,15, 5, 15);
        Border compound = new CompoundBorder(line, margin);
        setBorder(compound);
        this.setText(text);
        setFocusPainted(false);
    }
}
