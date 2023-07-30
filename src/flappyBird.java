/* flappyBird
 * Desc: flappyBird main class
 * @Daniel Ho ICS3U
 * Date : June 21 2022
 */

//Import java libraries 
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;


public class flappyBird{ 
    // Game Window properties
    static MyKeyListener keyListener = new MyKeyListener();
    static JFrame gameWindow;
    static GraphicsPanel canvas = new GraphicsPanel();
    static final int SCREEN_WIDTH = 1200;
    static final int SCREEN_HEIGHT = 800;

    // Variables for the game function
    static int gameState = 0; //0 = menu //1 = game
    static boolean gameOver = false;
    static boolean reset = true; //Used to restart the game
    static boolean gifSpawned = false;
    static Bird bird;
    static double score = 0;
    static double currentHighScore = 0;
    static double highScore = 0;
    static int lives = 1;

    // Variables for the background
    static int background1X = 0;
    static int background1Y = 0;
    static int background1W = -1000; 
    static int background2X = -1000;
    static int background2Y = 0;
    static int background2W = -1000; 
    static int backgroundStep = -5;
    static int moveSpeed = -5;

    // Images 
    static BufferedImage background2;
    static Image newImage2;
    static BufferedImage background1;
    static Image newImage1;
    static Image gif;
    static Image gif1;
    static Image gif2;
    static Image gif3;
    static Image gif4;
    static Image largeGif;

    // Location of gifs
    static int xGifLoc;
    static int yGifLoc;

    // Variables for the pipe 
    static final int PIPE_GAP = 100;
    static int xLoc = 1260;
    static ArrayList<Image> gifArray = new ArrayList<>();
    static ArrayList<Pipe> pipeArray = new ArrayList<Pipe>();
    static ArrayList<ExtraLife> extraLifesArray = new ArrayList<ExtraLife>(); 

//------------------------------------------------------------------------------    
    public static void main(String[] args) throws Exception{
        highScore = readFile(highScore);    // After reading the high score from the file, set it as the high score
        
        // Loading the gifs
        gif1 = load("gif1.gif");    
        gif2 = load("gif2.gif");
        gif3 = load("gif3.gif");
        gif4 = load("gif4.gif");
        largeGif = load("largeGif.gif");

        // Keep the game running, allows the uesr to play again
        while (true){
            Thread.sleep(100);
            if(reset) {   
                reset = false;
                initialize();
            }
        }
    } // main method end  

//------------------------------------------------------------------------------   
    public static void initialize(){
        gameState = 0;
        gameOver = false;
        if(lives == 0){ // Set the lives to be 1, when the game resets / user has 0 lives 
            lives = 1;
        }
        // Creating the game window and adding the Key listener & Graphics panel
        gameWindow = new JFrame("Troll Bird");
        gameWindow.setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        keyListener = new MyKeyListener();
        canvas.addKeyListener(keyListener);
        gameWindow.add(canvas);  
        gameWindow.setResizable(false);  
        gameWindow.requestFocusInWindow();

        // Reading the images from the file
        try {                
            background1 = ImageIO.read(new File("FlappyBirdBackground.png"));
            newImage1 = background1.getScaledInstance(SCREEN_WIDTH, SCREEN_HEIGHT, Image.SCALE_DEFAULT); // Scaling to screen size
        } catch (IOException ex){}
        try {                
            background2 = ImageIO.read(new File("FlappyBirdBackground.png"));
            newImage2 = background2.getScaledInstance(SCREEN_WIDTH, SCREEN_HEIGHT, Image.SCALE_DEFAULT); // Scaling to screen size
        } catch (IOException ex){}

        gameWindow.setVisible(true);
        runGame();
    } // initialize method end

//------------------------------------------------------------------------------   
    public static void runGame(){
        gameWindow.setVisible(true);
        bird = new Bird (500, 450, 20); // Creating a new bird 
        spawnPipes();
        spawnExtraLife();
        moveSpeed = -5;

        do {
            if(gameState == 1){
                gameWindow.repaint();
                bird.gravity();
                detectPipeCollision();
                detectLifeCollision();
                pointDetector();
                //Scrolling background
                background1X = background1X + backgroundStep; // Both backgrounds will scroll right at the same speed
                background2X = background2X + backgroundStep;
                if (background1X + backgroundStep < background1W) // If the first background is off the screen 
                    background1X = -background2W;                 // move it to the right
                if (background2X + backgroundStep < background2W) // If the second background is off the screen 
                    background2X = -background1W;                 // move it to the right

                if(bird.getY() < 15 || bird.getY() > SCREEN_HEIGHT-90) {  // If bird touches the top or bottom you lose a life   
                    if(lives > 0){
                        lives--;
                    }
                    gameOver = true;                                                                          
                }
                for(int i = 0; i < pipeArray.size(); i++){ // Move Pipes
                    pipeArray.get(i).movePipe(moveSpeed);
                }
                for(int i = 0; i < extraLifesArray.size(); i++){ // Move Extra Lifes
                    extraLifesArray.get(i).moveExtraLife(moveSpeed);
                }

                // When x location of pipe is less than 0 and its width remove it 
                for(int i = 0; i < pipeArray.size(); i++){
                    if(pipeArray.get(i).getX() < 0 - pipeArray.get(i).getWidth()){
                        pipeArray.get(i).addedScore = false;
                        pipeArray.remove(i);
                    }
                }
                // Create another pipe to the right of the screen after removing one
                if(pipeArray.size() < 8){
                    int height = pipeRand((int) ((5-1+1)*Math.random()+1));     // The height is randomized using the pipeRand switch case method
                    pipeArray.add(new TopPipe(SCREEN_WIDTH+340, 0, height));   
                    pipeArray.add(new BotPipe(SCREEN_WIDTH+340, height + PIPE_GAP, SCREEN_HEIGHT - height));
                } 

                // When x location of the heart is less than 0 and its width remove it 
                for(int i = 0; i < extraLifesArray.size(); i++){
                    if(extraLifesArray.get(i).getX() < 0 - extraLifesArray.get(i).getWidth()){
                        extraLifesArray.remove(i);
                    }
                }

                // Create another heart to the right of the screen after removing one
                if(extraLifesArray.size() < 2){
                    int extraLifeY = extraLifeRand((int) ((2)*Math.random()+1));    // The y location is randomized using the extraLifeRand switch case method
                    extraLifesArray.add(new ExtraLife(SCREEN_WIDTH + 4850, extraLifeY, 50));
                }

                // If a gif is not shown, take a random gif using the randGif switch case method
                if(gifSpawned == false){
                    gif = randGif((int) ((4)*Math.random()+1));
                    gifSpawned = true;
                    xGifLoc = xGifLoc();    // Randomly set the x and y locations of the gif 
                    yGifLoc = yGifLoc();
                }

            }
            try  {Thread.sleep(30);} catch(Exception e){}
        } while(!gameOver);
    } // runGame method end

//------------------------------------------------------------------------------ 
    static void detectPipeCollision(){
        for (Pipe pipe : pipeArray){    // For each pipe in the array check if a collision occured
            if(bird.getRect().intersects(pipe.getRect())){  // If the bird hits the pipe, you lose a life 
                lives--;                           
                gameOver = true;
            }
            pipe.updateRect();
        }
    } // detectPipeCollision method end

//------------------------------------------------------------------------------ 
    static void detectLifeCollision(){
        for (ExtraLife extraLife : extraLifesArray){   // For each heart in the array check if a collision occured
            if(bird.getRect().intersects(extraLife.getRect())){     // If the bird hits the heart, add a life to the lives counter
                lives++;
                extraLifesArray.remove(extraLife);          // Remove the object once collision occurs
            }
            extraLife.updateRect();
        }
    } // detectLifecollision method end

//------------------------------------------------------------------------------ 
    static void pointDetector(){
        for(Pipe piped : pipeArray) {
            if(piped.getX() < bird.getX() && piped.getX()+piped.getWidth() > bird.getX() && !piped.addedScore) {  // During a small frame a point can be added and then addedScore = true
                score += 0.5;
                currentHighScore += 0.5; 
                if (currentHighScore > highScore){      // If the current high score is higher than the previous
                    highScore = currentHighScore;       // Set the current high score as the new one
                    try {
                        writeFile(highScore);           // Write the high score in the text file
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }    
                piped.addedScore = true;
            }
        }
    } // pointDetector method end

//------------------------------------------------------------------------------ 
    static void spawnPipes(){
        int temp = 0;
        for(int i = 0; i < 4; i++){
            int height = pipeRand((int) ((5)*Math.random()+1));         // Gets a random height of the pipe
            pipeArray.add(new TopPipe(xLoc + temp, 0, height));     // Add the top pipe to the array list
            pipeArray.add(new BotPipe(xLoc + temp, height + PIPE_GAP, SCREEN_HEIGHT - height));   // Add the bottom pipe to the array list
            temp += 400;
        }
    } // spawnPipes method end 

//------------------------------------------------------------------------------
    static int pipeRand(int rand){
        switch(rand){   // Takes a random number from 1-5 inclusive and returns the corresponding value as the height of the pipe
            case 1:
                return 100;     // Ex : If the random number is 1, 100 would be the height of the pipe
            case 2:
                return 200;
            case 3:
                return 300;
            case 4:
                return 333;
            case 5:
                return 500;
            default:
                return 400;
        }
    } // pipeRand method end

//------------------------------------------------------------------------------ 
    static void spawnExtraLife(){
        int temp = 1805;
        for(int i = 0; i < 2; i++){     
            int extraLifeY = extraLifeRand((int) ((2)*Math.random()+1));    // Gets the random y location of the heart
            extraLifesArray.add(new ExtraLife(xLoc + temp, extraLifeY, 50));    // Adds the heart to an array list
            temp += 4000;
        }
    } // spawnExtraLife method end

//------------------------------------------------------------------------------
    static int extraLifeRand(int rand){ // Takes a number either 1 or 2 
        switch(rand){                   // and returns the corresponding value as the y location of the heart
            case 1:
                return 600;         // Ex : If the random number is 1, 600 would be the y location of the heart
            case 2:
                return 200;
            default:
                return 500;
        }
    } // extraLifeRand method end

//------------------------------------------------------------------------------ 
    static Image randGif(int rand){   // Takes a number from 1-3 inclusive 
        switch(rand){                 // and returns the corresponding value as the gif being shown
            case 1: 
                return gif1;    // Ex : If the random number is 1, gif1 would be shown
            case 2:
                return gif2;
            case 3:
                return gif3;
            case 4:
                return gif4;
            default:
                return gif1;
        }
    } // randGif method end

//------------------------------------------------------------------------------ 
    static int xGifLoc(){
        int max = SCREEN_WIDTH - 400;
        int min = SCREEN_WIDTH / 2;
        int xGifLoc = (int) ((max - min +1)*Math.random()+min); // Randomly spawn between max and min x locations 
        return xGifLoc;
    } // xGifLoc method end

//------------------------------------------------------------------------------ 
    static int yGifLoc(){
        int max = SCREEN_HEIGHT - 300;
        int min = 300;
        int yGifLoc = (int) ((max - min +1)*Math.random()+min); // Randomly spawn between max and min y locations 
        return yGifLoc;
    } // yGifLoc method end 

//------------------------------------------------------------------------------ 
    static void writeFile(double highScore) throws Exception{   // Writes the new high score in the text file 
        File file = new File("HighScore.txt");      // Create a new File object and assign it the text file
        PrintWriter output = new PrintWriter(file);           // Create a PrinterWriter and link it to the file object
        output.println(highScore);
        output.close();
    } // writeFile method end

//------------------------------------------------------------------------------ 
    static double readFile(double highScore) throws Exception{
        double num = 0;
        File showCast = new File("HighScore.txt");  // Create a new File object and assign it the text file 
        Scanner input = new Scanner(showCast);                // Create a Scanner and link it to the file object
        num = Double.parseDouble(input.next());               // Set the number to be the number read in the file
        input.close();
        return num;                                           // Return that number
    } // readFile method end
    
//------------------------------------------------------------------------------ 
    private static Image load(final String path) {  // Used to load gifs
        try {
            final Toolkit tk = Toolkit.getDefaultToolkit();
            final Image img = tk.createImage(path);
            tk.prepareImage(img, -1, -1, null); // prepares the gif to be returned 
            return img;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    } // load method end

//------------------------------------------------------------------------------ 
    static class GraphicsPanel extends JPanel{
        public GraphicsPanel(){
            setFocusable(true);
            requestFocusInWindow();
        } 
        public void paintComponent(Graphics g){ 
            super.paintComponent(g); 

            // Draw the resized background images
            g.drawImage(newImage1,background1X,background1Y,this);
            g.drawImage(newImage2,background2X,background2Y,this);
            if (bird != null && gameState == 0){ // Print Menu Screen
                bird.drawBird(g);           // Draw the bird
                g.setFont(new Font("Arial", 1, 30));    // Setting font size and style
                g.drawString("Enter Space to Start", 350, 400);
            }
            if(bird != null)
                bird.drawBird(g);

            for(int i = 0; i < pipeArray.size(); i++){ // Draw Pipes
                pipeArray.get(i).drawPipe(g);
            }
            for(int i = 0; i < extraLifesArray.size(); i++){ // Draw Extra Lifes
                extraLifesArray.get(i).drawExtraLife(g);
            }
            if(gameOver == true){ //Print Game Over, score counter, life counter and draw the gif 
                g.setColor(Color.white);
                g.drawImage(largeGif, 0, 0, this);
                g.setFont(new Font("Arial", 1, 50));    // Setting font size and style
                g.drawString("Game Over!", SCREEN_WIDTH/2-400, SCREEN_HEIGHT/2);
                g.drawString("Press Space To Play Again", SCREEN_WIDTH/2-400, SCREEN_HEIGHT/2 + 100);
                g.drawString(String.valueOf((int)score), SCREEN_WIDTH/2, 50);       // Print current score
                g.drawString(String.valueOf("H: " + (int)highScore), SCREEN_WIDTH/2 - 65, 100);     // Print high score
                g.setFont(new Font("Arial", 1, 30));
                g.drawString("lives: " + String.valueOf(lives), 0, 100);    // Print number of lives remaining
            }
            if(gameState == 0 || gameOver == false){ //Print Score counter & life counter
                g.setColor(Color.white);
                g.setFont(new Font("Arial", 1, 50));
                g.drawString(String.valueOf((int)score), SCREEN_WIDTH/2, 50);     // Print current score
                g.drawString(String.valueOf("H: " + (int)highScore), SCREEN_WIDTH/2 - 65, 100); // Print high score
                g.setFont(new Font("Arial", 1, 30));
                g.drawString("lives: " + String.valueOf(lives), 0, 100);    // Print number of lives remaining
            }
            if (!gameOver && score % 3.0 == 0 && score != 0 && gifSpawned){   // Every three points
                g.drawImage(gif, xGifLoc, yGifLoc, this);                    // Spawn a gif
            }  
            else{
                gifSpawned = false;
            }
        } // paintComponent method end    
    } // GraphicsPanel class end

//------------------------------------------------------------------------------  
    public static class MyKeyListener implements KeyListener{      
        public void keyPressed(KeyEvent e){
            int key = e.getKeyCode();
            if (gameOver && key == KeyEvent.VK_SPACE && lives == 0){  // When no more lives are left & space bar is pressed & the game is over
                gameWindow.setVisible(false);                      // set the game window false
                reset = true;
                pipeArray.clear();         // Clear both array lists and reset the scores
                extraLifesArray.clear(); 
                score = 0;
                currentHighScore = 0;
            } 
            else if (gameOver && key == KeyEvent.VK_SPACE){    // If the game is over but user still has lives  
                gameWindow.setVisible(false);               // Clear both array lists and run the game again  
                reset = true;
                pipeArray.clear();
                extraLifesArray.clear();  
            } 
            else if (key == KeyEvent.VK_SPACE){   // When space bar is pressed
                gameState = 1;                    // bird.fly() - the bird will move up by 10;
                gameOver = false;  
                bird.fly();
            }   
        }
        public void keyReleased(KeyEvent e){}    
        public void keyTyped(KeyEvent e){}           
    } // MyKeyListener class end

} // flappyBird class end