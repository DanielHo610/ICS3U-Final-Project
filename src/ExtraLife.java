/* ExtraLife
 * Desc: ExtraLife class
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

public class ExtraLife {
    private BufferedImage extraLife;
    private int lifeX;
    private int lifeY;
    private int width;
    private Rectangle lifeRect;

    /*
     * ExtraLife Default Constructor
     * @param lifeX x cooridinate of the life
     * @param lifeY y coordinate of the life 
     * @param width Width of the hitbox
     */
    public ExtraLife(int lifeX, int lifeY, int width){
        this.lifeX = lifeX;
        this.lifeY = lifeY;
        this.width = 50;
        lifeRect= new Rectangle(lifeX, lifeY, width, width);

        // Read the image from the file
        try {                
            extraLife = ImageIO.read(new File("ExtraLife.png"));
        } catch (IOException ex){}
    }

    /**
     * Method to draw the extra life
     * @param g graphics panel
     */
    public void drawExtraLife(Graphics g){
        g.drawImage(extraLife, lifeX, lifeY, width, width, null);
    } 

    /**
     * Method to move the extra life
     * @param xDistance distance to move the extra life
     */
    public void moveExtraLife(int xDistance){
        this.lifeX += xDistance;
    }

    /**
     * Method to update the rectangle of the extra life
     */
    public void updateRect(){
        lifeRect = new Rectangle(lifeX, lifeY, width, width);
    }

    /**
     * getX
     * @return lifeX x coordinates of the extra life
     */
    public int getX(){
        return lifeX;
    }

    /**
     * setX
     * @param lifeY set x coordinates of the extra life
     */
    public void setX(int lifeX){
        this.lifeX = lifeX;
    }

    /**
     * getY
     * @return lifeY y coordinates of the extra life
     */
    public int getY(){
        return lifeY;
    }

    /**
     * setY
     * @param lifeY set y coordinates of the extra life
     */
    public void setY(int lifeY){
        this.lifeY = lifeY;
    }

    /**
     * getWidth
     * @return this.width width of the extra life
     */
    public int getWidth(){
        return this.width;
    }

    /**
     * getRect
     * @return lifeRect rectangle of the extra life
     */
    public Rectangle getRect(){
        return lifeRect;
    }
}
