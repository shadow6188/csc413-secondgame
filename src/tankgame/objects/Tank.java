package tankgame.objects;

import tankgame.BulletHandler;
import tankgame.GameDriver;
import tankgame.resources.ResourceLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;



public class Tank extends GameObject implements Collidable {


    //private final int InitialX;
    //private final int InitialY;
    //private final int InitialAngle;
    private int vx;
    private int vy;
    private int health;
    private boolean isDead;

    private static final String basic = "basic";
    private static final String big = "big";

    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;
    private boolean ShootPressed;
    private boolean switchRightPressed;
    private java.awt.Rectangle nextLocation;
    private ArrayList<GameObject> collide;
    private BulletHandler shoot;
    private Gun gun;
    private int weapon;
    private int id;
    private GameDriver game;
    float power;

public Tank(int id, int x, int y, BufferedImage img, ArrayList<GameObject> in, BulletHandler shoot, GameDriver game){
    super( x, y, img);
    collide = in;
    nextLocation = this.rect;
    gun = new Gun(x+image.getWidth(),y-image.getHeight(),-90 ,(BufferedImage) ResourceLoader.getResource("gun"));
    this.shoot = shoot;
    weapon = 0;
    this.id = id;
    this.game = game;
    power = 0;
    isDead = false;
    health = 100;
}

    public void toggleUpPressed() {
        this.UpPressed = true;
    }

    public void toggleDownPressed() {
        this.DownPressed = true;
    }

    public void toggleRightPressed() {
        this.RightPressed = true;
    }

    public void toggleLeftPressed() {
        this.LeftPressed = true;
    }

    public void toggleShootPressed() {
        this.ShootPressed = true;
    }

    public void toggleSwitchRightPressed(){this.switchRightPressed =true;}

    public void unToggleUpPressed() {
        this.UpPressed = false;
    }

    public void unToggleDownPressed() {
        this.DownPressed = false;
    }

    public void unToggleRightPressed() {
        this.RightPressed = false;
    }

    public void unToggleLeftPressed() {
        this.LeftPressed = false;
    }

    public void unToggleShootPressed() {
        this.ShootPressed = false;
    }

    public void untoggleSwitchRightPressed(){this.switchRightPressed =false;}

    public void update() {
    if (game.getMove()) {
        if (game.getTurn() == id) {
            if (this.UpPressed) {
                this.rotateGunCounterClockwise();
            }
            if (this.DownPressed) {
                this.rotateGunClockwise();
            }
            if (this.LeftPressed) {
                this.moveLeft();
            }
            if (this.RightPressed) {
                this.moveRight();
            }
            if (this.switchRightPressed) {
                this.switchWeaponRight();
            }
            if (this.ShootPressed) {
                if (power < 15)
                    power+= .1;
                else {
                    this.fire(power);
                    game.setMove(false);
                    game.switchTurn();
                    power = 0;
                }
                //timer = 40;
            }else {
                if (power > 0){
                    this.fire(power);
                    game.setMove(false);
                    game.switchTurn();
                    power= 0 ;
                }
            }
        }
    }
        onLand();
        checkBoundary();
        updateGunposition();
        //timer--;
        //if (life == 0) {
            //this.status = false;
        //}
        if (health <= 0){
            isDead = true;
        }
       //if(remove != null){
       //    collide.remove(remove);
       //}
    }
    private void switchWeaponRight(){
        weapon = (weapon + 1) % 2;
    }
    private void onLand(){
        nextLocation.setLocation(Math.round(x),Math.round(y+1));
        if (checkCollision(nextLocation)){
            y = y + 1;
        }
    }
    private void moveLeft(){
        nextLocation.setLocation(Math.round(x-1),Math.round(y));
        if (checkCollision(nextLocation)){
            x = x - 1;
        }
    }

    private void moveRight(){
        nextLocation.setLocation(Math.round(x+1),Math.round(y));
        if (checkCollision(nextLocation)){
            x = x + 1;
        }
    }
    private void rotateGunClockwise(){
        if (gun.getAngle() > -170)
            this.gun.setAngle(gun.getAngle() - 1);
    }
    private void rotateGunCounterClockwise(){
        if (gun.getAngle() < -10)
            this.gun.setAngle(gun.getAngle() + 1);
    }
    private void fire(float power){
        //int posX = x ;//+ this.width/2; // resets the position to middle of tank
        //int posY = y ;//+ this.height/2;
        //posX += (int) Math.round((this.width+15)/2.0 * Math.cos(Math.toRadians(gun.getAngle())));
        //posY += (int) Math.round((this.height +15)/2.0 * Math.sin(Math.toRadians(gun.getAngle())));
        switch (weapon){
            case 0:
                shoot.Shoot(new BasicBullet(x, y, gun.getAngle(),power ,(BufferedImage) ResourceLoader.getResource("bullet")));
                break;
            case 1:
                shoot.Shoot(new BigBullet(x, y, gun.getAngle(),power ,(BufferedImage) ResourceLoader.getResource("bigbullet")));
                break;
            default:break;
        }
    }
    public boolean isDead(){
        return this.isDead;
    }
    private boolean checkCollision(Rectangle next){ // checks if tank is gonna collide with something if it moves to said location
        if (collide.isEmpty()) {System.out.println("broken");}
        for (GameObject thing: collide) {
            if (thing.getBoundary().intersects(next.getBounds()))
            {
                return false;//collision
            }

        }
        return true;//no collision
    }
    private void updateGunposition(){
        gun.setX(Math.round(this.x + 9));
        gun.setY(Math.round(this.y + 1));
    }
    private void checkBoundary(){
        if (x < 0){
            x = 0;
        }

        if (x > 1000 - image.getWidth() -15){
            x = 1000 - image.getWidth() -15;
        }
    }
    public void drawImage(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(this.image,Math.round(x),Math.round(y), null);
        gun.drawImage(g2d);
    }
    public void drawHud(Graphics2D g,int x,int y, String player){
        String temp = switch (weapon) {
            case 0 -> basic;
            case 1 -> big;
            default -> "";
        };
        String convert = String.format(java.util.Locale.US,"%.2f", (power/15)*100);
        g.drawString(player +" weapon : " + temp ,x,y);
        g.drawString("shot Power :" + convert +" %", x,y+10);
        //g.drawString(player+" Lives :" + life,x,y);
        for (int r = -health; r <= 0;r += 20){
            Rectangle temp2 = new Rectangle(20,10);
            temp2.setLocation(x+90-r, y+20);
            g.fill(temp2);
        }
        g.drawString(player+" Health :" ,x,y+30);
    }

    @Override
    public void hit() {
        health -= 20;
    }
}
