/* Bird
 * Desc: Bird class
 * @Daniel Ho ICS3U
 * Date : June 21 2022
 */

//Import java libraries 
import java.awt.*;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Bird {
    private BufferedImage trollFace;
    private int birdX;
    private int birdY;
    private int yVelocity = 0;
    private int width;
    private final int GRAVITY = 1;
    private Rectangle birdRect = new Rectangle(birdX, birdY, width, width);
    
    /**
     * Bird Default Constructor
     * @param birdX the x location of the bird
     * @param birdY the y location of the bird
     * @param width the width location of the bird
     */
    public Bird (int birdX, int birdY, int width){
        this.birdX = birdX;
        this.birdY = birdY;
        this.width = width;

        // Read the image from the file
        try {                
            trollFace = ImageIO.read(new File("TrollFace.png"));
        } catch (IOException ex){}
    }

    // Method to drop the bird
    public void gravity() {
        yVelocity += GRAVITY;   
        birdY += yVelocity;     // y location of the bird increases by the y velocity
        birdRect = new Rectangle(birdX, birdY, width, width);   // updates the rectangle of the bird
    }

    // Method to fly the bird
    public void fly() {
        yVelocity = -10;    // Moves up the screen by 10
    }

    /**
     * Method to draw the bird
     * @param g grapichs panel
     */
    public void drawBird(Graphics g){
        g.drawImage(trollFace, birdX-width/2, birdY-width/2, width+20, width+20, null);
    }

    /**
     * getX
     * @return birdX the x coordinate of the bird
     */
    public int getX() {
        return birdX;
    }

    /**
     * setX
     * @param x the coordinate you want to set
     */
    public void setX(int x){
        this.birdX = x;
    }

    /**
     * getY
     * @return birdY the y coordinate of the bird
     */
    public int getY() {
        return birdY;
    }

    /**
     * setY
     * @param y the coordinate you want to set
     */
    public void setY(int y) {
        this.birdY = y;
    }

    /**
     * getWidth
     * @return width the width of the bird
     */
    public int getWidth() {
        return width;
    }

    /**
     * getRect
     * @return birdRect the rectangle of the bird
     */
    public Rectangle getRect(){
        return birdRect;
    } 
}