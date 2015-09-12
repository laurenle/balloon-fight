import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;



public class Instructions extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -766788626596376850L;
	private BufferedImage instructpic;
	
	public Instructions() {
        File instructions = new File("instructions.png");
        try {
			instructpic = ImageIO.read(instructions);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void paintComponent(Graphics g) {
	    g.drawImage(instructpic, 0, 0, null); 
	}

}