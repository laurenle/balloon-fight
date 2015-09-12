/**
 * CIS 120 HW10
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */


import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.Timer;
import java.util.*;

/**
 * GameCourt
 * 
 * This class holds the primary game logic of how different objects 
 * interact with one another.  Take time to understand how the timer 
 * interacts with the different methods and how it repaints the GUI 
 * on every tick().
 *
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {
	
	private static int points;
	private static int top;
	private static int rank = 0;
	
	private static BufferedImage topbar;
	private static BufferedImage start;
	
	private Square square;          // the Black Square, keyboard control
	private BalloonField bfield1;
	private BalloonField bfield2;
	public boolean playing = false;  // whether the game is running
	public boolean gameover = false;
	public boolean started = false;
	private Direction direct;
	
	private LinkedList <HashSet<Star>> slist;
	private LinkedList <HashSet<Balloon>> blist;
	private LinkedList <HashSet<Bubble>> ulist;
	
	private static Timer timer;
	private static Timer object_generator;
	private static Timer object_remover;
	private static Timer balloon_animate;
	private static Timer star_animate;
	private static Timer animate_player;
	
	private static Clip popnoise;
	
	private int animation_pane1;
	private int animation_pane2;
	// Game constants
	public static final int COURT_WIDTH = 585;
	public static final int COURT_HEIGHT = 381;
	// Update interval for timer in milliseconds 
	public static final int INTERVAL = 35; 
	public GameCourt() {
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		
		File f = new File("instructions.png");
		try {
			start = ImageIO.read(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try
	    {
	        popnoise = AudioSystem.getClip();
	        popnoise.open(
	        		AudioSystem.getAudioInputStream(new File("popsound.wav")));
	    }
	    catch (Exception e)
	    {
	        e.printStackTrace(System.out);
	    }

		try {
			if (topbar == null) {
				topbar = ImageIO.read(new File("topbar.png"));
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
		
		
		timer = new Timer(INTERVAL, new ActionListener(){
			public void actionPerformed(ActionEvent e){
				tick();
			}
		});
		
		object_generator = new Timer (750, new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if (playing) generateNew();
			}
		});
		
		animate_player = new Timer (80, new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				square.flap(direct);
			}
		});
		
		object_remover = new Timer (880, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (slist != null && playing) slist.removeLast();
				if (blist != null && playing) blist.removeLast();
				if (ulist != null && playing) ulist.removeLast();
			}
		}
		);
		object_remover.setInitialDelay(21000);
		
		balloon_animate = new Timer (350, new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if (blist != null && playing) {
					for (HashSet<Balloon> b_set : blist) {
						for (Balloon b : b_set) {
							b.BalloonChange(animation_pane1);
							if (animation_pane1 == 5) animation_pane1 = 0;
							else animation_pane1++;
						}
					}
				}
			}
		});
		
		star_animate = new Timer (100, new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if (slist != null && playing) {
					for (HashSet<Star> s_set : slist) {
						for (Star s : s_set) {
							s.starChange(animation_pane2);
							if (animation_pane2 == 3) animation_pane2 = 0;
							else animation_pane2++;
						}
					}
				}
			}
		});
		
		
		timer.start();
		object_generator.start();
		object_remover.start();
		animation_pane1 = 0;
		animation_pane2 = 0;
		balloon_animate.start();
		star_animate.start();
		animate_player.start();
		setFocusable(true);
		
		addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				if (e.getKeyCode() == KeyEvent.VK_R &&
						gameover) {
					gameover = false;
					reset();
					timer.restart();
					object_generator.restart();
					object_remover.restart();
					balloon_animate.restart();
					star_animate.restart();
					animate_player.restart();
				}
			}
		}
		);
		
		
		addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (!started) {
						started = true;
						playing = true;
					}
				}
				
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					square.v_x = -6;
					direct = Direction.LEFT;
				}
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					square.v_y = 4;
				}
				else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					square.v_x = 6;
					direct = Direction.RIGHT;
				}
				else if (e.getKeyCode() == KeyEvent.VK_UP) {
					square.v_y = -5;
					square.flying = true;
				}
				
				else if (e.getKeyCode() == KeyEvent.VK_SPACE && !gameover)
					playing = !playing;
			
			}
			public void keyReleased(KeyEvent e){
				if (direct == Direction.LEFT) {
					square.v_x = 0;
					square.v_y = 1;
				}
				else {
					square.v_x = 2;
					square.v_y = 1;
				}
				square.flying = false;
			}
		});
		
	}

	public void reset() {
		
		timer.restart();
		object_generator.restart();
		object_remover.restart();
		
		points = 0;
		rank = 0;
		square = new Square(COURT_WIDTH, COURT_HEIGHT);
		bfield1 = new BalloonField(COURT_WIDTH, COURT_HEIGHT);
		bfield2 = new BalloonField(COURT_WIDTH, COURT_HEIGHT);
		slist = new LinkedList <HashSet<Star>> ();
		blist = new LinkedList <HashSet<Balloon>> ();
		ulist = new LinkedList <HashSet<Bubble>> ();
		playing = true;
		requestFocusInWindow();

	}

	void tick() {
		if (started && playing) {
			square.move();
			square.bounce(square.hitWall());
			for (HashSet<Star> s_set : slist) {
				for (Star s : s_set) {
					if (square.intersects(s)) { 
						playing = false;
						timer.stop();
						object_generator.stop();
						object_remover.stop();
						gameover = true;
					}
					s.move();
					s.bounce(s.hitWall());
				}
			}
			for (HashSet<Balloon> b_set : blist) {
				for (Balloon b : b_set) {
					if (square.intersects(b) && !b.isPopped()) { 
						b.pop();
						popnoise.start();
						popnoise.setFramePosition(0);
						points = points + 10;
						if (points / 100 > rank) rank++; 
						if (points > top) top = points;
					}
					b.move();
				}
			}

			for (HashSet<Bubble> u_set : ulist) {
				for (Bubble u : u_set) {
					if (square.intersects(u) && !u.isPopped()) { 
						u.pop();
						points = points + 50;
						if (points / 100 > rank) rank++; 
						if (points > top) top = points;
					}
					u.move();
				}
			}
			if (bfield1.pos_x == 1) bfield2.pos_x = -584;
			else if (bfield2.pos_x == 1) bfield1.pos_x = -584;
			bfield1.move();
			bfield2.move();
			clip();
		}
		if (playing || gameover) repaint();
	}
	
	void clip() {
		if (square.pos_x > COURT_WIDTH - 20) square.pos_x = COURT_WIDTH - 20;
		else if (square.pos_x < 0) square.pos_x = 0;
		if (square.pos_y < 0) square.pos_y = 0;
		else if (square.pos_y > COURT_HEIGHT) square.pos_y = COURT_HEIGHT;
	}
	

    
	void generateNew() {
		HashSet<Balloon> balloon_column = new HashSet<Balloon> ();
		HashSet<Star> star_column = new HashSet<Star> ();
		HashSet<Bubble> bubble_column = new HashSet<Bubble> ();

		for (int i = 0; i < 4; i++) {
			if (Math.random() > 0.85) {
				int vy0 = 3;
				if (Math.random() > 0.5) vy0 = -3;
				Star s = new Star(COURT_WIDTH, COURT_HEIGHT, 
						(int) (320 * Math.random()), vy0);
				star_column.add(s);
			}
		}
		if (Math.random() > 0.7) {
			Balloon b = new Balloon(COURT_WIDTH, COURT_HEIGHT, 
					(int) (320 * Math.random()));
			balloon_column.add(b);		
		}
		
		if (Math.random() > 0.95) {
			Bubble u = new Bubble(COURT_WIDTH, COURT_HEIGHT);
			bubble_column.add(u);
		}
		slist.addFirst(star_column);
		blist.addFirst(balloon_column);
		ulist.addFirst(bubble_column);
	}

	@Override 
	public void paintComponent(Graphics g){
			super.paintComponent(g);
			g.setColor(Color.white);
					
			bfield1.draw(g);
			bfield2.draw(g);
			if (slist != null) {
				for (HashSet<Star> s_set : slist) {
					for (Star s : s_set) {
						s.draw(g);
					}
				}
			}
			if (ulist != null) {
				for (HashSet<Bubble> u_set : ulist) {
					for (Bubble u : u_set) {
						u.draw(g);
					}
				}
			}
			if (blist != null) {
				for (HashSet<Balloon> b_set : blist) {
					for (Balloon b : b_set) {
						b.draw(g);
					}
				}
			}
			square.draw(g);
			g.drawString(Integer.toString(points), 75, 30);
			g.drawString(Integer.toString(top), 260, 30);
			g.drawString(Integer.toString(rank), 500, 30);
			g.drawImage(topbar,0,0,null);
		
		
		if (!started) {
			g.drawImage(start,0,0,null);
		}
		
		if (gameover) {
			g.drawString("GAME OVER", 250, 180);
			g.drawString("Press 'R' to Replay", 230, 200);
		}
	}
	
	@Override
	public Dimension getPreferredSize(){
		return new Dimension(COURT_WIDTH,COURT_HEIGHT);
	}
}
