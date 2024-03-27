import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Flappy extends JPanel implements ActionListener, KeyListener{

    int boardHeight = 640;
    int boardWidth = 360;

//Images

Image backgroundImg;
Image birdImg;
Image topPipeImg;
Image bottomPipeImg;

//The Bird

int birdX = boardWidth/8;
int birdY = boardHeight/2;
int birdWidth = 34;
int birdHeight = 24;

class Bird {

    int x = birdX;
    int y = birdY;
    int width = birdWidth;
    int height = birdHeight;
    Image img;

    Bird (Image img){

        this.img = img;
    }
}

//pipes

int pipeX = boardWidth;
int pipeY = 0;
int pipeWidth = 64;   //scaled by 1/6
int pipeHeight = 512;


class Pipe {
  
    int x = pipeX;
    int y = pipeY;
    int width = pipeWidth;
    int height = pipeHeight;
    Image img;
    boolean passed = false;

    Pipe(Image img) {
        this.img = img;
    }
}



    // game logic 

    Bird bird;
    int velocityX = -4;   //moves the pipes to the left speed
    int velocityY = 0;   //the speed of the bird up/down
    int gravity = 1;

    Timer gameLoop;
    
    Timer placePipeTimer;

    ArrayList<Pipe> pipes;

    Random random = new Random();

    boolean gameOver = false;

    double score = 0;

    Flappy()  {

        setPreferredSize(new Dimension(boardWidth,boardHeight));
           

       setFocusable(true);  
       addKeyListener(this);

        //Loading the img

        backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();


        //the bird

        bird = new Bird(birdImg);
        pipes = new ArrayList<Pipe>();

        //place pipes timer

        placePipeTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              // Code to be executed
              placePipes();
            }
        });

        placePipeTimer.start();

        //game timer, for 60 fps

        gameLoop = new Timer (1000/60,this); 
        gameLoop.start();

    }


    public void placePipes(){

        // (0-1) * pipeHeight/2 -> (0-256)
        //128
        //0 -128 -(0-256)  --> pipeHeight/4 -> 3/4 pipeHeight

        int randomPipeY = (int)(pipeY - pipeHeight/4 - Math.random()*pipeHeight/2);

        int openingSpace = boardHeight/4;

        Pipe toPipe = new Pipe(topPipeImg);
        
        toPipe.y = randomPipeY;

        pipes.add(toPipe);

        Pipe bottonPipe = new Pipe(bottomPipeImg);
        bottonPipe.y = toPipe.y + pipeHeight + openingSpace;
        pipes.add(bottonPipe);


    }

    public void paintComponent(Graphics g) {

		super.paintComponent(g);
        draw(g);
	
    }


    public void draw(Graphics g){
   
   
        // background

    g.drawImage(backgroundImg, 0, 0 , boardWidth, boardHeight, null);
    
    //bird 

    g.drawImage(birdImg, bird.x, bird.y, bird.width, bird.height, null);


    for(int i=0 ; i< pipes.size(); i++){

        Pipe pipe = pipes.get(i);
        g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);

    }

    g.setColor(Color.white);
    g.setFont(new Font("Calibri",Font.BOLD, 32));

    if (gameOver){

        g.drawString("Game over: " + String.valueOf((int) score), 8, 30);

    }else{

        g.drawString(String.valueOf((int) score), 8,30);
    }

    }

    public void move(){

        //bird
        velocityY += gravity;

        bird.y +=velocityY;
        bird.y =Math.max(bird.y,0);

        //pipes

        for (int i=0; i< pipes.size() ; i++){

            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            if(!pipe.passed && bird.x > pipe.x + pipe.width){

                pipe.passed = true;
                score += 0.5;  //because there are 2 pipes , so 1 in total for each set of pipes passed

            }

            if (collision(bird, pipe)){

                gameOver = true;

            }

        }


        if (bird.y > boardHeight){

            gameOver = true;
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        move();
        repaint();
        if(gameOver){

            placePipeTimer.stop();
            gameLoop.stop();
        }
      
    }


    public boolean collision(Bird a, Pipe b){

        return a.x < b.x + b.width &&   //a's top left corner doesn't reach b's top right corner
        a.x + a.width > b.x &&   //a's top right corner passes b's top left corner
        a.y < b.y + b.height &&  //a's top left corner doesn't reach b's bottom left corner
        a.y + a.height > b.y;    //a's bottom left corner passes b's top left corner

    }
   
    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_SPACE){

            velocityY = -9;

             if (gameOver) {
                //restart game by resetting conditions
                bird.y = birdY;
                velocityY = 0;
                pipes.clear();
                gameOver = false;
                score = 0;
                gameLoop.start();
                placePipeTimer.start();
            }

        }
       
    }
   
    @Override
    public void keyTyped(KeyEvent e) {
     
    }


    @Override
    public void keyReleased(KeyEvent e) {
       
    }
    
}
