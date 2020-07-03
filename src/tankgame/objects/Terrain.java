package tankgame.objects;


import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Terrain extends GameObject implements Collidable {
    private int state;

    public Terrain(int x, int y, BufferedImage terrain){
        super(x,y, terrain);
        state = 1;
    }
    public boolean intact(){
        return state > 0;
    }

    public void hit(){
        state--;
    }

    public void drawImage(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.image,AffineTransform.getTranslateInstance(x,y), null);
    }
}