package tankgame.objects;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public abstract class Bullet extends GameObject{
    private int angle;
    private int vx = 0;
    private int vy = 0;
    private float velocityX;
    private float velocityY;

    public Bullet(float x, float y, int angle, float power, BufferedImage image){

        super(x + Math.round(image.getWidth() * Math.cos(Math.toRadians(angle))),
                y + Math.round(image.getHeight() * Math.sin(Math.toRadians(angle))), image);
        this.angle = angle;
        this.velocityX = Math.round(power* Math.cos(Math.toRadians(angle)));
        this.velocityY = Math.round(power * Math.sin(Math.toRadians(angle)));
    }

    public void move(float accel,float wind) {
        velocityY += accel;
        velocityX += wind;
        //vx = (int) Math.round(velocityX * Math.cos(Math.toRadians(angle)));
        //vy = (int) Math.round(velocityY * Math.sin(Math.toRadians(angle)));
        x += velocityX;
        y += velocityY;

        rect.setLocation(Math.round(x),Math.round(y));
    }

    @Override
    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), height/ 2.0, width / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.image, rotation, null);
    }
    public int getWidth(){
        return this.width;
    }
    public int getHeight(){
        return this.height;
    }

    @Override
    public String toString() {
        return "Bullet";
    }
}
