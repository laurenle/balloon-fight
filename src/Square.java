/**
 * CIS 120 HW10
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/** A basic game object displayed as a black square, starting in the 
 * upper left corner of the game court.
 *
 */
public class Square extends GameObj {
	public static final int INIT_X = 250;
	public static final int INIT_Y = 150;
	public static final int INIT_VEL_X = 1;
	public static final int INIT_VEL_Y = 0;
	
	public boolean flying = false;
	
	private BufferedImage l0;
	private BufferedImage l1;
	private BufferedImage l2;
	
	private BufferedImage r0;
	private BufferedImage r1;
	private BufferedImage r2;
	
	private BufferedImage img;
	
    /** 
     * Note that because we don't do anything special
     * when constructing a Square, we simply use the
     * superclass constructor called with the correct parameters 
     */
    public Square(int courtWidth, int courtHeight){
        super(INIT_VEL_X, INIT_VEL_Y, INIT_X, INIT_Y, 
        		27, 39, courtWidth, courtHeight);
        
        try {
			if (l0 == null) {
				l0 = ImageIO.read(new File("left1.png"));
				l1 = ImageIO.read(new File("left2.png"));
				l2 = ImageIO.read(new File("left3.png"));
				
				r0 = ImageIO.read(new File("right1.png"));
				r1 = ImageIO.read(new File("right2.png"));
				r2 = ImageIO.read(new File("right3.png"));
				
				img = l0;
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
    }
    
    public void flap(Direction d) {
    	if (d != null && d.equals(Direction.RIGHT)) {
    		if (flying) {
	    		if (img == r0) img = r1;
	    		if (img == r1) img = r2;
	    		else img = r0;
    		}
    		else img = r0;
    	}
    	
	    else {
	    	if (flying) {
	    		if (img == l0) img = l1;
	    		if (img == l1) img = l2;
	    		else img = l0;
	    	}
	    	else img = l0;
		}
    }
    
    @Override
    public void draw(Graphics g) {
		g.drawImage(img, pos_x, pos_y, 27, 39, null); 
    }

}
