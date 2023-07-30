/* BotPipe
 * Desc: BotPipe class
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
public class BotPipe extends Pipe{
    public BufferedImage botPipe;

    public BotPipe(int pipeX, int pipeY, int height){
        super(pipeX, pipeY, height);

        // Read image from file
        try {                
            botPipe = ImageIO.read(new File("BotPipe.png"));
        } catch (IOException ex){} 
    }
    @Override // There are 2 drawPipe methods so we call both methods in each sub class to the Pipe class
    public void drawPipe (Graphics g){  // Draw the bottom Pipe
        g.drawImage(botPipe, pipeX-25, pipeY, this.width+50, height, null);
    }
}
