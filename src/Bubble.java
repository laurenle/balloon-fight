import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Bubble extends GameObj {

	public static final String s1 = "bubble.png";
	public BufferedImage img;
	public static final int SIZE = 20;
	private boolean popped = false;
	

    public Bubble(int courtWidth, int courtHeight){
        super(1, -1, 0, 320, 
        		SIZE, SIZE, courtWidth, courtHeight);
        
		try {
			if (img == null) {
				img = ImageIO.read(new File(s1));
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
    }
    
    public void pop() {
    	popped = true;
    	img = null;
    }
    
    public boolean isPopped() {
    	return popped;
    }
    
    
    @Override
    public void draw(Graphics g) {
		g.drawImage(img, pos_x, pos_y, width, height, null); 
    }
}
