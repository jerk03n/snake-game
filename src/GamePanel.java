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
    boolean withEnemy;
    Timer timer;
    Random random;
    Image background;

    final int x[] = new int[gameUnits];
    final int y[] = new int[gameUnits];
    int bodyParts = 3;
    int applesEaten = 0;
    char direction = 'R';
    Image head;

    final int enemyX[] = new int[gameUnits];
    final int enemyY[] = new int[gameUnits];
    int enemyParts = 3;
    char enemyDirection = 'L';
    boolean isAlive = true;
    Image enemyHead;

    int mouseX;
    int mouseY;
    Image mouse;

    GamePanel(boolean withEnemy){
        this.withEnemy = withEnemy;
        random = new Random();
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        head = new ImageIcon(getClass().getResource("/images/head.png")).getImage();
        enemyHead = new ImageIcon(getClass().getResource("/images/enemyHead.png")).getImage();
        mouse = new ImageIcon(getClass().getResource("/images/mouse.png")).getImage();
        background = new ImageIcon(getClass().getResource("/images/background.png")).getImage();
        startGame();
    }

    public void startGame(){
        newMouse();
        running = true;
        isAlive = true;
        if(withEnemy){
            enemyX[0] = screenWidth - unitSize;
            enemyY[0] = 0;
        }
        timer = new Timer(delay, this);
        timer.start();
    }

    public void restartGame(){
        if(timer != null){
            timer.stop();
        }

        bodyParts = 3;
        applesEaten = 0;
        direction = 'R';

        for(int i = 0; i < gameUnits; i++){
            x[i] = 0;;
            y[i] = 0;
        }

        if(withEnemy){
            respawnEnemy();
        }

        repaint();
        startGame();
    }

    public void respawnEnemy(){
        enemyParts = 3;
        enemyDirection = 'L';
        isAlive = true;

        for(int i = 0; i < gameUnits; i++){
            enemyX[i] = 0;;
            enemyY[i] = 0;
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if (background != null){
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
        draw(g);
    }

    public void draw(Graphics g){
       if(running){
            g.drawImage(mouse, mouseX, mouseY, unitSize, unitSize, this);;

            for(int i = 0; i < bodyParts; i++){
                if(i == 0){
                    g.drawImage(head, x[0], y[0], unitSize, unitSize, this);
                }else{
                    g.setColor(new Color(181, 230, 29));
                    g.fillRect(x[i], y[i], unitSize, unitSize);
                }
            }

            if(isAlive && withEnemy){
                for(int i = 0; i < enemyParts; i++){
                    if(i == 0){
                        g.drawImage(enemyHead, enemyX[0], enemyY[0], unitSize, unitSize, this);
                    }else{
                        g.setColor(new Color(255, 242, 0));
                        g.fillRect(enemyX[i], enemyY[i], unitSize, unitSize);
                    }
                }
            }

            g.setColor(Color.yellow);
			g.setFont( new Font("Comic Sans",Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: "+applesEaten, (screenWidth - metrics.stringWidth("Score: "+applesEaten)) / 2, 
                                                 g.getFont().getSize());
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

    public void moveEnemy(){
        if(!withEnemy){
            return;
        }

        for(int i = enemyParts; i > 0; i--){
            enemyX[i] = enemyX[i-1];
            enemyY[i] = enemyY[i-1];
        }

        chooseDirection();

        switch(enemyDirection){
            case 'U':
                enemyY[0] = enemyY[0] - unitSize;
                break;
            case 'D':
                enemyY[0] = enemyY[0] + unitSize;
                break;
            case 'L':
                enemyX[0] = enemyX[0] - unitSize;
                break;
            case 'R':
                enemyX[0] = enemyX[0] + unitSize;
                break;
        }
    }

    public boolean isDirectionSafe(int x, int y){
        for(int i = 1; i < enemyParts; i++){
            if(enemyX[i] == x && enemyY[i] == y){
                return false;
            }
        }

        for(int i = 0; i < bodyParts; i++){
            if(this.x[i] == x && this.y[i] == y){
                return false;
            }
        }
    
        if(x < 0 || x >= screenWidth || y < 0 || y >= screenHeight){
            return false;
        }
    
        return true;
    }

    public void chooseDirection(){
        char[] directions = {'U', 'D', 'L', 'R'};
        int bestDistance = Integer.MAX_VALUE;
        char bestDirection = enemyDirection;

        for(char dir: directions){
            int newX = enemyX[0];
            int newY = enemyY[0];

            switch(dir){
                case 'U': 
                    newY -= unitSize; 
                    break;
                case 'D': 
                    newY += unitSize; 
                    break;
                case 'L': 
                    newX -= unitSize; 
                    break;
                case 'R': 
                    newX += unitSize; 
                    break;
            }

            if(isDirectionSafe(newX, newY)){
                int dist = Math.abs(mouseX - newX) + Math.abs(mouseY - newY);
                if(dist < bestDistance){
                    bestDistance = dist;
                    bestDirection = dir;
                }
            }
        }

        enemyDirection = bestDirection;
    }

    public void newMouse(){
        boolean validPos = false;
        
        while(!validPos){
            mouseX = random.nextInt((int)(screenWidth / unitSize)) * unitSize;
            mouseY = random.nextInt((int)(screenHeight / unitSize)) * unitSize;

            validPos = true;

            for (int i = 0; i < bodyParts; i++) {
                if (x[i] == mouseX && y[i] == mouseY) {
                    validPos = false;
                    break;
                }
            }

            for (int i = 0; i < enemyParts; i++) {
                if (enemyX[i] == mouseX && enemyY[i] == mouseY) {
                    validPos = false;
                    break;
                }
            }
        }
    }

    public void checkMouse(){
        if(x[0] == mouseX && y[0] == mouseY) {
			bodyParts++;
			applesEaten++;
			newMouse();
		}else if(enemyX[0] == mouseX && enemyY[0] == mouseY){
            enemyParts++;
            newMouse();
        }
    }

    public void checkCollisions(){
		for(int i = bodyParts; i > 0; i--) {
			if(x[0] == x[i] && y[0] == y[i]) {
				running = false;
			}
		}
		
		if(x[0] < 0) {
			running = false;
		}
	
		if(x[0] > screenWidth) {
			running = false;
		}
		
		if(y[0] < 0) {
			running = false;
		}
		
		if(y[0] > screenHeight) {
			running = false;
		}

        for(int i = 0; i < enemyParts; i++){
            if(x[0] == enemyX[i] && y[0] == enemyY[i]){
                running = false;
            }
        }
		
		if(!running) {
			timer.stop();
		}
    }

    public void checkEnemyCollisions(){
		for(int i = enemyParts; i > 0; i--) {
			if(enemyX[0] == enemyX[i] && enemyY[0] == enemyY[i]) {
				isAlive = false;
			}
		}
		
		if(enemyX[0] < 0) {
			isAlive = false;
		}
	
		if(enemyX[0] > screenWidth) {
			isAlive = false;
		}
		
		if(enemyY[0] < 0) {
			isAlive = false;
		}
		
		if(enemyY[0] > screenHeight) {
			isAlive = false;
		}

        for(int i = 0; i < bodyParts; i++){
            if(enemyX[0] == x[i] && enemyY[0] == y[i]){
                isAlive = false;
            }
        }
    }

    public void gameOver(Graphics g){
		g.setColor(Color.yellow);
		g.setFont( new Font("Comic Sans",Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: "+applesEaten, (screenWidth - metrics1.stringWidth("Score: "+applesEaten)) / 2, 
                                             g.getFont().getSize());
		
		g.setColor(Color.yellow);
		g.setFont( new Font("Ink Free",Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (screenWidth - metrics2.stringWidth("Game Over")) / 2, screenHeight/2);
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        if(running){
            move();
            moveEnemy();
            checkMouse();
            checkCollisions();
            checkEnemyCollisions();
        }

        if(!isAlive){
            respawnEnemy();
        }

        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if(direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U') {
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    if(!running) {
                       restartGame();
                    }
                    break;
                }
        }
    }
}
