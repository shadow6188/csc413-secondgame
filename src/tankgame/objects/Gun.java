package tankgame.objects;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Gun extends GameObject{
    private int angle;
    Gun(int x, int y, int angle,BufferedImage image) {
        super(x, y, image);
        this.angle = angle;
    }

    @Override
    public Rectangle getBoundary() {
        return super.getBoundary();
    }

    //@Override
    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), 0/ 2.0, 0 / 2.0);
        ((Graphics2D) g).drawImage(this.image,rotation,null);
    }
    public void setX(int input){
        this.x = input;
    }
    public void setY(int input){
        this.y = input;
    }
    public void setAngle(int input){
        this.angle = input;
    }
    public int getAngle(){
        return angle;
    }
}
