
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Star extends GameObj {
	public static final String s1 = "star1.png";
	public static final String s2 = "star2.png";
	public static final String s3 = "star3.png";
	public static BufferedImage star1;
	public static BufferedImage star2;
	public static BufferedImage star3;
	
	public static final int SIZE = 20;
	public static final int INIT_X = 0;
	public static final int INIT_VEL_X = 1;
	
	public static BufferedImage img;
	

	

    public Star(int courtWidth, int courtHeight, int y0, int vy0){
        super(INIT_VEL_X, vy0, INIT_X, y0, 
        		SIZE, SIZE, courtWidth, courtHeight);
        
		try {
			if (star1 == null) {
				star1 = ImageIO.read(new File(s1));
				star2 = ImageIO.read(new File(s2));
				star3 = ImageIO.read(new File(s3));
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
    }
    
    public void starChange(int i) {	
		if (i == 0) img = star1;
    	else if (i == 1 || i == 3) img = star2;
    	else if (i == 2) img = star3;
    }
    
    
    
    @Override
    public void draw(Graphics g) {
		g.drawImage(img, pos_x, pos_y, width, height, null); 
    }

}