import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;



public class Menu extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -766788626596376850L;
	private BufferedImage menupic;
	
	public Menu() {
        File loadscreen = new File("loadscreen.png");
        try {
			menupic = ImageIO.read(loadscreen);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void paintComponent(Graphics g) {
	    g.drawImage(menupic, 0, 0, null); 
	}

}