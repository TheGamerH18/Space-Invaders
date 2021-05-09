package sprite;

import javax.swing.*;
import java.net.URL;

public class Bomb extends Sprite{

    private boolean destroyed;

    public Bomb(int x, int y) {
        initBomb(x, y);
    }

    private void initBomb(int x, int y) {
        setDestroyed(true);

        this.x = x;
        this.y = y;

        String loc = "/images/bomb.png";
        URL url = getClass().getResource(loc);
        ImageIcon ii = new ImageIcon(url);
        setImage(ii.getImage());
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public boolean isDestroyed() {
        return destroyed;
    }
}
