
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Balloon extends GameObj {

	private static BufferedImage balloon0;
	private static BufferedImage balloon1;
	private static BufferedImage balloon2;
	private static BufferedImage balloon3;
	private static BufferedImage crushed;
	
	public static final int INIT_X = 0;
	public static final int INIT_VEL_X = 1;
	public static final int INIT_VEL_Y = 0;

	private boolean popped; 
	public BufferedImage img;

    public Balloon(int courtWidth, int courtHeight, int y0){
        super(INIT_VEL_X, INIT_VEL_Y, INIT_X, y0, 
        		16, 26, courtWidth, courtHeight);
        popped = false;
		try {
			if (balloon0 == null) {
				balloon0 = ImageIO.read(new File("balloon0.png"));
				balloon1 = ImageIO.read(new File("balloon1.png"));
				balloon2 = ImageIO.read(new File("balloon2.png"));
				balloon3 = ImageIO.read(new File("balloon3.png"));
				crushed = ImageIO.read(new File("crushedballoon.png"));
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
		
    }
    
    public void BalloonChange(int i) {	
		if (!popped) {
	    	if (i == 0) img = balloon0;
	    	else if (i == 1 || i == 5) img = balloon1;
	    	else if (i == 2 || i == 4) img = balloon2;
	    	else img = balloon3;
		}
		else img = null;
    }
    
    public boolean isPopped() {
    	return popped;
    }
    
    public void pop() {
    	img = crushed;
    	popped = true;
    }
    
    
    @Override
    public void draw(Graphics g) {
		g.drawImage(img, pos_x, pos_y, width, height, null); 
    }

}