/* Pipe
 * Desc: Pipe class
 * @Daniel Ho ICS3U
 * Date : June 21 2022
 */

//Import java libraries 
import java.awt.*;

public class Pipe {
    public int pipeX;
    public int pipeY;
    public int height;
    public int width;
    public Rectangle pipeRect;
    public boolean addedScore = false;

    /**
     * Pipe Default Constructor
     * @param pipeX x location of the pipe
     * @param pipeY y location of the pipe
     * @param height height location of the pipe
     */
    public Pipe(int pipeX, int pipeY, int height){
        this.pipeX = pipeX;
        this.pipeY = pipeY;
        this.height = height;
        this.width = 60;
        pipeRect= new Rectangle(pipeX, pipeY, width, height);
    }

    /**
     * Method to draw the Pipes
     * @param g grapichs panel
     */
    public void drawPipe (Graphics g){

    }
    /**
     * Method to move the Pipes
     * @param xDistance distance to move the pipes
     */
    public void movePipe(int xDistance){
        this.pipeX += xDistance;
    }

    // Method to update the Pipes location
    public void updateRect(){
        pipeRect = new Rectangle(pipeX, pipeY, width, height);
    }

    /**
     * getX
     * @return this.pipeX the x coordinate of the pipe
     */
    public int getX(){
        return this.pipeX;
    }

    /**
     * setX
     * @param pipeX set the x coordinate of the pipe
     */
    public void setX(int pipeX){
        this.pipeX = pipeX;
    }

    /**
     * getY
     * @return this.pipeY the y coordinate of the pipe
     */
    public int getY(){
        return this.pipeY;
    }
    
    /**
     * setY
     * @param pipeY set the y coordinate of the pipe
     */
    public void setY(int pipeY){
        this.pipeY = pipeY;
    } 

    /**
     * getHeight
     * @return this.height the height of the pipe
     */
    public int getHeight(){
        return this.height;
    }

    /**
     * setHeight
     * @param h set the height of the pipe
     */
    public void setHeight(int h) {
        this.height = h;
    }

    /**
     * getWidth
     * @return this.width the width of the pipe
     */
    public int getWidth(){
        return this.width;
    }

    /**
     * setWidth
     * @param w set the width of the pipe
     */
    public void setWidth(int w) {
        this.width = w;
    }

    /**
     * getRect
     * @return pipeRect the rectangle of the pipe
     */
    public Rectangle getRect(){
        return pipeRect;
    }
}