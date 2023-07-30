/* TopPipe
 * Desc: TopPipe class
 * @Daniel Ho ICS3U
 * Date : June 21 2022
 */

//Import java libraries 
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

// subclass for the Pipe class
public class TopPipe extends Pipe{
    public BufferedImage topPipe;

    public TopPipe(int pipeX, int pipeY, int height){
        super(pipeX, pipeY, height);

        // Read image from file
        try {                
            topPipe = ImageIO.read(new File("TopPipe.png"));
        } catch (IOException ex){}     
    }
    @Override // There are 2 drawPipe methods so we call both methods in each sub class to the Pipe class
    public void drawPipe (Graphics g){  // Draw the top Pipe
        g.drawImage(topPipe, pipeX-25, pipeY, this.width+50, height, null);   
    }
}
