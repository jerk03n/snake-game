import java.awt.*;
import java.awt.event.*;

import java.util.*;

import javax.swing.*;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener{
    static final int screenWidth = 600;
    static final int screenHeight = 600;
    static final int unitSize = 25;
    static final int gameUnits = (screenHeight * screenWidth) / unitSize;
    static final int delay = 75;
    boolean running = false;
    Timer timer;
    Random random;

    final int x[] = new int[gameUnits];
    final int y[] = new int[gameUnits];
    int bodyParts = 3;
    int applesEaten = 0;
    char direction = 'R';

    int mouseX;
    int mouseY;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame(){
        newMouse();
        running = true;
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
       if(running){
            g.setColor(Color.GRAY);
            g.fillRect(mouseX, mouseY, unitSize, unitSize);

            for(int i = 0; i < bodyParts; i++){
                if(i == 0){
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], unitSize, unitSize);
                }else{
                    g.setColor(new Color(181, 230, 29));
                    g.fillRect(x[i], y[i], unitSize, unitSize);
                }
            }
       }else{
            gameOver(g);
       }
    }

    public void move(){
        for(int i = bodyParts; i > 0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direction){
            case 'U':
                y[0] = y[0] - unitSize;
                break;
            case 'D':
                y[0] = y[0] + unitSize;
                break;
            case 'L':
                x[0] = x[0] - unitSize;
                break;
            case 'R':
                x[0] = x[0] + unitSize;
                break;
        }
    }

    public void newMouse(){
        mouseX = random.nextInt((int)(screenWidth / unitSize)) * unitSize;
        mouseY = random.nextInt((int)(screenHeight / unitSize)) * unitSize;
    }

    public void checkMouse(){

    }

    public void checkCollisions(){

    }

    public void gameOver(Graphics g){

    }

    
    @Override
    public void actionPerformed(ActionEvent e){
        if(running){
            move();
            checkMouse();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){

        }
    }
}
