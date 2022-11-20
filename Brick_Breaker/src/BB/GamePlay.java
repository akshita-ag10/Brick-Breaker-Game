package BB;

import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import javax.swing.Timer;
import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.Font;

public class GamePlay extends JPanel implements ActionListener, KeyListener {
    private boolean play = false;
    private int scores = 0;
    private int totalBricks = 21;

    private Timer timer;
    private int delay = 8;
    private int playerX = 310;
    private int ballposX = 120;
    private int ballposY = 350;
    private int ballXdir = -1;
    private int ballYdir = -2;

    private MapGenerator map;


    public GamePlay(){
        map = new MapGenerator(3, 7); //i forgot this line
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics g){

        g.setColor(Color.white);
        g.fillRect(1, 1, 692, 592);

        map.draw((Graphics2D)g);

        g.setColor(Color.yellow);
        g.fillRect( 0, 0, 3, 592);
        g.fillRect(0, 0, 692 , 3);
        g.fillRect(683, 0, 3, 592);

        g.setColor(Color.blue);
        g.fillRect(playerX, 550,100, 8);

        g.setColor(Color.green);
        g.fillOval(ballposX, ballposY,20, 20);

        g.setColor(Color.black);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString ("" + scores, 590, 30);

        if(totalBricks <= 0){
            play = false;
            ballXdir = 0;
            ballYdir = 0; // why we did ballXdir and ballYdir to zero?

            g.setColor(Color.green);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("You WON, Scores: " + scores, 190, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart!", 230, 350);
        }

        if(ballposY > 570){
            play = false;
            ballXdir = 0;
            ballYdir = 0;

            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Over, Scores: " + scores, 190, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart!", 230, 350);
        }

        g.dispose();
    }

    public void keyPressed(KeyEvent arg0){

        if(arg0.getKeyCode() == KeyEvent.VK_RIGHT){
            if(playerX >= 600){
                playerX = 600;
            }else{
                moveRight();
            }
        }

        if(arg0.getKeyCode() == KeyEvent.VK_LEFT){
            if(playerX <= 10){
                playerX = 10;
            }else{
                moveLeft();
            }
        }

        if(arg0.getKeyCode() == KeyEvent.VK_ENTER){
            if(!play){
                play = true;
                totalBricks = 21;
                ballposX = 120;
                ballposY = 350;
                ballXdir = -1;
                ballYdir = -2;
                scores = 0;
                map = new MapGenerator(3, 7);

                repaint(); // what if we do not write repaint()?
            }
        }

    }

    public void moveRight(){
        play = true;
        playerX += 20;
    }

    public void moveLeft(){
        play = true;
        playerX -= 20;
    }


    public void keyReleased(KeyEvent arg0){
    }
    public void keyTyped(KeyEvent arg0){
    }

    public void actionPerformed(ActionEvent arg0){

        timer.start();

        if(play){
            ballposX += ballXdir;
            ballposY += ballYdir;

            if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))){
                ballYdir = -ballYdir;
            }
            if(ballposX < 0){
                ballXdir = - ballXdir;
            }
            if(ballposY < 0){
                ballYdir = -ballYdir;
            }
            if(ballposX > 670){
                ballXdir = -ballXdir;
            }

            for(int i = 0; i<map.map.length; i++){ // first map is obj of class MapGenerator and second map is the 2d array
                for(int j =0; j<map.map[0].length; j++){
                    if(map.map[i][j] >0){
                        Rectangle rect = new Rectangle(j*map.brickWidth + 80, i*map.brickHeight + 50, map.brickWidth, map.brickHeight);
                        Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
                        Rectangle brickRect = rect;

                        if(brickRect.intersects(ballRect)){ //ulta bhi krke dekhna ki (brickRect.intersects(ballRect))
                            map.setBrickValue(0, i, j);
                            scores += 5;
                            totalBricks --;

                            if(ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width){
                                ballXdir = -ballXdir;
                            }else{
                                ballYdir = -ballYdir;
                            }
                        }
                    }
                }
            }
        }

        repaint();

    }
}
