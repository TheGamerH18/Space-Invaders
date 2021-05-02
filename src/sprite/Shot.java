package sprite;

import javax.swing.*;
import java.net.URL;

public class Shot extends Sprite {

    public Shot() {

    }

    public Shot(int x, int y){
        initShot(x, y);
    }

    // Hidden Init Setting Position of Shot. Movement in Board
    private void initShot(int x, int y){
        String loc = "/images/bomb.png";
        URL url = getClass().getResource(loc);
        ImageIcon ii = new ImageIcon(url);
        setImage(ii.getImage());

        // Positioniere Schuss
        int H_SPACE = 6;
        setX(x + H_SPACE);

        int V_SPACE = 1;
        setY(y - V_SPACE);
    }
}
