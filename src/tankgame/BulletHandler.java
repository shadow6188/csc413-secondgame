package tankgame;

import tankgame.objects.*;

import java.awt.*;
import java.util.ArrayList;

import static tankgame.GameDriver.SCREEN_WIDTH;
import static tankgame.GameDriver.WORLD_HEIGHT;

public class BulletHandler {
    private float gravity = (float) .25;
    private Bullet ammo;
    //private ArrayList<Bullet> ammo;
    //private ArrayList<Bullet> collided;
    private Circle temp;
    private GameDriver game;

    public BulletHandler(GameDriver game){this.game = game;}

    public boolean occupied(){
        return ammo != null;
    }
    public void Shoot(Bullet in){
        ammo = in;
    }
    public void update(ArrayList<GameObject> check){
       //for (Bullet shot: ammo) {
       //     shot.move(gravity);
       // }
        if (ammo != null)
            ammo.move(gravity,game.getWind()/10);
        if (ammo != null)
            collisions(check);
        if (ammo != null)
            outOfBounds();
        if (ammo == null)
            game.setMove(true);

    }

    private void outOfBounds() {
        Rectangle temp = ammo.getBoundary();
        if(temp.getX()< 0 ||temp.getX() > SCREEN_WIDTH|| temp.getY() > WORLD_HEIGHT)
        {
            ammo = null;
            game.generateWind();
        }
    }

    private void collisions(ArrayList<GameObject> check){
        boolean toNull = false;
            for (GameObject in: check) {
                if(ammo.getBoundary().intersects(in.getBoundary())){
                    if (ammo instanceof BasicBullet)
                        temp = new Circle( ammo.getBoundary().getX() + (ammo.getWidth()/2.), ammo.getBoundary().getY()+ (ammo.getHeight()/2.), 5);
                    else if (ammo instanceof BigBullet)
                        temp = new Circle( ammo.getBoundary().getX() + (ammo.getWidth()/2.), ammo.getBoundary().getY()+ (ammo.getHeight()/2.), 15);
                    toNull = true;
                    break;
                }
        }
        if (temp != null) {
            for (GameObject in:check) {
                if (temp.intersects(in.getBoundary())) {
                    if (in instanceof Collidable){
                        ((Collidable) in).hit();
                        //System.out.println("hit");
                    }
                }
            }
        }
        temp = null;
        if (toNull) {
            ammo = null;
            game.generateWind();
        }
    }
    public void draw(Graphics2D gameWorld){
        if (ammo != null)
            ammo.drawImage(gameWorld);
    }


}
