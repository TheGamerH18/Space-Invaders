package sprite;

import javax.swing.*;

public class Shot extends Sprite {

    public Shot() {

    }
    public Shot(int x, int y){
        initShot(x, y);
    }

    private void initShot(int x, int y){
        ImageIcon ii = new ImageIcon("src/images/shot.png");
        setImage(ii.getImage());

        // Positioniere Schuss
        int H_SPACE = 6;
        setX(x + H_SPACE);

        int V_SPACE = 1;
        setY(y - V_SPACE);
    }
}
