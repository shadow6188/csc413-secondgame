package tankgame;

import tankgame.objects.GameObject;
import tankgame.objects.Tank;
import tankgame.objects.Terrain;
import tankgame.resources.ResourceLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


// main driver for game
public class GameDriver extends JPanel {
    public static final int SCREEN_HEIGHT = 800;
    public static final int WORLD_HEIGHT = 600;
    public static final int SCREEN_WIDTH = 1000;

    private JFrame frame;
    private BufferedImage screen;
    private Graphics2D GameWorld;

    private Tank tankOne;
    private Tank tankTwo;

    private ArrayList<GameObject> stuff;
    private BulletHandler shoot;
    private int turn; // true means player 1 turn, false for player 2
    private float wind;
    private boolean canMove;
    private ArrayList<GameObject> Player1;
    private ArrayList<GameObject> Player2;
    private ArrayList<GameObject> collidable;
    String end;

    private boolean ongoing;


    public static void main(String[] args){
        GameDriver game = new GameDriver();
        game.setVisible(true);
        game.ongoing =true;
        game.generateWind();
        
        try {
            do {
                if(game.tankOne.isDead() || game.tankTwo.isDead()) {
                    game.ongoing = false;
                    if (game.tankOne.isDead()){
                        game.end = "P1";
                    } else game.end = "P2";
                }
                game.tankOne.update();
                game.tankTwo.update();
                game.shoot.update(game.collidable);
                updateObjects(game);
                Thread.sleep(1000 / 200);
                game.repaint();
            } while (game.ongoing);
        } catch (Exception e){/*ignore*/}
        System.out.println("game ended"); // got to switch to winner announcement somehow.
    }
    public GameDriver(){
        init();
    }
    private void init() {// initialize game
        frame = new JFrame("TankGame");
        screen = new BufferedImage(SCREEN_WIDTH,WORLD_HEIGHT,BufferedImage.TYPE_INT_RGB);

        stuff = new ArrayList<>();

        for (int i = 0; i < SCREEN_WIDTH; i = i + 2){
            for (int j = SCREEN_HEIGHT/2 ; j < WORLD_HEIGHT; j = j+2)
            stuff.add(new Terrain(i,j,(BufferedImage) ResourceLoader.getResource("terrain")));
        }
        turn  = 0;
        canMove = true;

        //stuff = SetUp.populateWorld();

        //Player1 = new ArrayList<>(stuff);
        //Player2 = new ArrayList<>(stuff);

        shoot = new BulletHandler(this);

        tankOne = new Tank(0,50,WORLD_HEIGHT/2 , (BufferedImage) ResourceLoader.getResource("tank1")
                ,stuff,shoot,this);
        tankTwo = new Tank(1,SCREEN_WIDTH-100,WORLD_HEIGHT/2,(BufferedImage)ResourceLoader.getResource("tank2")
                ,stuff,shoot,this);
        //Player1.add(tankTwo);
        //Player2.add(tankOne);

        collidable = new ArrayList<>(stuff);
        collidable.add(tankOne);
        collidable.add(tankTwo);

        {
            TankControl firstPlayer = new TankControl(tankOne,
                    KeyEvent.VK_W,
                    KeyEvent.VK_S,
                    KeyEvent.VK_A,
                    KeyEvent.VK_D,
                    KeyEvent.VK_SHIFT,
                    KeyEvent.VK_E);

            frame.addKeyListener(firstPlayer);

            TankControl secondPlayer = new TankControl(tankTwo,
                    KeyEvent.VK_UP,
                    KeyEvent.VK_DOWN,
                    KeyEvent.VK_LEFT,
                    KeyEvent.VK_RIGHT,
                    KeyEvent.VK_ENTER,
                    KeyEvent.VK_PAGE_DOWN);

            frame.addKeyListener(secondPlayer);
        }//creating listeners


        {
            frame.setVisible(true);
            frame.setSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null); // makes it spawn in middle of screen
            frame.setLayout(new BorderLayout());//not sure if this is this is needed
            frame.add(this, 0);
            frame.setResizable(false);
            frame.setBackground(Color.BLACK);
        }// frame settings
    }

    public int getTurn(){
        return turn;
    }
    public void switchTurn(){
        if (turn == 0)
            turn = 1;
        else if (turn == 1)
            turn = 0;
    }
    public boolean getMove(){
        return canMove;
    }
    public void setMove(boolean in){
        canMove = in;
    }
    public float getWind(){
        return wind;
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);

        if (ongoing) {
            GameWorld = screen.createGraphics();
            // add things to game world here
            GameWorld.drawImage(((BufferedImage) ResourceLoader.getResource("background")
            ).getScaledInstance(SCREEN_WIDTH, SCREEN_HEIGHT, Image.SCALE_FAST), 0, 0, null);

            tankOne.drawImage(GameWorld);//first player onto screen
            tankTwo.drawImage(GameWorld);//second player onto screen
            // draw game world to frame

            shoot.draw(GameWorld);
            stuff.forEach(item -> item.drawImage(GameWorld));

            tankOne.drawHud(g2, 0, WORLD_HEIGHT + 50, "Player 1");
            tankTwo.drawHud(g2, 700, WORLD_HEIGHT + 50, "Player 2");

            g2.drawString("wind value is :" + wind, SCREEN_WIDTH / 2 - 100, SCREEN_HEIGHT - 100);
            g2.drawImage(screen, 0, 0, null);
        } else {
            g2.drawImage((BufferedImage) ResourceLoader.getResource(end),0,0,null);
        }
    }
    public void generateWind(){

        this.wind = (float) (Math.random()-.5);
    }

    private static void updateObjects(GameDriver game){
        ArrayList<GameObject> temp = new ArrayList<>();
        for (GameObject item: game.stuff) {
            if(item instanceof Terrain){
                if (!((Terrain) item).intact()){
                    temp.add(item);
                }
            }
        }
        game.stuff.removeAll(temp);
        temp.clear();
    }

}
