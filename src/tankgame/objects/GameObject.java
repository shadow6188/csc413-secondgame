package tankgame.objects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;

public abstract class GameObject {

    protected float x;
    protected float y;
    protected BufferedImage image;
    protected int width;
    protected int height;
    protected Rectangle rect;

    GameObject(float x,float y, BufferedImage image){
        this.x = x;
        this.y = y;
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.rect = new Rectangle(Math.round(x),Math.round(y),width,height);
    }
    public void applyGravity(){
        this.y++;
    }

    public Rectangle getBoundary(){
        return rect;
    }

    public abstract void drawImage(Graphics g);

}
