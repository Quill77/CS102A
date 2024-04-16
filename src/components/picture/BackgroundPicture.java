package components.picture;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class BackgroundPicture {

    public static ImageIcon getImageIcon(int i) throws IOException{
        return new ImageIcon(ImageIO.read(new File("src/assets/images/bgp"+i+".png")));

    }
}
