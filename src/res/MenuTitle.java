package res;

import javax.swing.*;
import java.awt.*;

public class MenuTitle extends JLabel {

    public MenuTitle(String title) {
        setFont(new Font("Helvetica", Font.BOLD, 20));
        setText(title);
        setForeground(Color.YELLOW);
    }
}
