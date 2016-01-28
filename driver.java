import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class driver extends JFrame implements Runnable{
	
	private Image dbImage;
	private Graphics dbg;
	
	TileMap map = new TileMap();
	Pac p = new Pac();
	
    static Rectangle blinky = new Rectangle(324, 264, 32, 32);
    static Rectangle pinky = new Rectangle(325, 320, 32, 32);
    static Rectangle inky = new Rectangle(280, 320, 32, 32);
    static Rectangle clyde = new Rectangle(370, 320, 32, 32);
    static Rectangle player = new Rectangle(200, 200, 32, 32);
    
    static Ghost ghost = new Ghost(blinky, pinky, inky, clyde, player);
	
    public driver(){
    	p.map = map;
    	ghost.map = map;
    	ghost.pac = p;
    	map.g = ghost;

    }
    
    public void reset(){
    	
    	ghost.Blinky = new Rectangle(336, 264, 32, 32);
        ghost.Pinky = new Rectangle(325, 320, 32, 32);
        ghost.Inky = new Rectangle(280, 320, 32, 32);
        ghost.Clyde = new Rectangle(370, 320, 32, 32);
        ghost.ghostReset();
        p.x = map.getPacAtX()*24;
        p.y = map.getPacAtY()*24;
        p.prefxVel = 0;
        p.prefyVel = 0;
        
        ghost.pacdead = false;
    }
    
	public void run(){
		try{
			while(true){
				
				p.move();
				Thread.sleep(5);
				
				if(ghost.pacdead)
				{
					Pac.lives--;
					
					
					reset();
					
				}
				
				if(Pac.dotCounter == 0)
				{
					ghost.nextLevel = true;
					reset();
				}
				
			 }
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
	
	public class AL extends KeyAdapter{
		
		public void keyPressed(KeyEvent e){
			
			int code = e.getKeyCode();
			
			if(code == KeyEvent.VK_UP)
			{	
				p.setYVelocity(-1);
				p.setXVelocity(0);
			}
			
			if(code == KeyEvent.VK_DOWN)
			{	
				p.setYVelocity(1);
				p.setXVelocity(0);
			}
			
			if(code == KeyEvent.VK_LEFT)
			{	
				p.setXVelocity(-1);
				p.setYVelocity(0);
				
				if(ghost.freshReset)
				{
					ghost.bxDir = -1;
					ghost.byDir = 0;
					ghost.freshReset = false;
				}
			}	
			if(code == KeyEvent.VK_RIGHT)
			{	
				p.setXVelocity(1);
				p.setYVelocity(0);
				
				if(ghost.freshReset)
				{
					ghost.bxDir = 1;
					ghost.byDir = 0;
					ghost.freshReset = false;
				}
			}
			if(code == KeyEvent.VK_ESCAPE)
			{
				System.exit(0);
			}
			if(code == KeyEvent.VK_SPACE)
			{
				if(ghost.ghostSleepTime < 10)
				{
					ghost.ghostSleepTime = 2500;
				}
				else 
				{
					ghost.ghostSleepTime = 7;
				}
				
				
			}
			if(code == KeyEvent.VK_R)
			{
				reset();
			}
		}
		
	}
	
	public void createAndDisplayGUI(){
		
		addKeyListener(new AL());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(map.getWidth()+300, map.getHeight()+100));
		pack();
		setTitle("PacGame");
		setLocation(400,100);
		setVisible(true);
		invalidate();
		
		p.x = map.getPacAtX()*24;
		p.y = map.getPacAtY()*24;
	}
	
	//double buffer
	public void paint(Graphics g){
		dbImage = createImage(getWidth(), getHeight());
		dbg = dbImage.getGraphics();
		paintComponent(dbg);
		g.drawImage(dbImage, 24, 24, this);
		
	}
	
	public void paintComponent(Graphics g){
		this.setBackground(Color.BLACK);
		map.draw(g);
		p.paintPac(g, p.wall, p.xVel, p.yVel, p.x, p.y);
		ghost.Pac = new Rectangle(p.x, p.y, 32, 32);
		ghost.paintBlinky(g);
		ghost.paintPinky(g);
		ghost.paintInky(g);
		ghost.paintClyde(g);
		
		
		repaint();
	}
	
	public static void main(String[] args){
		
		SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
            	
            	driver d = new driver();
            	d.createAndDisplayGUI();
            	
            	Thread t1 = new Thread(d);
            	t1.start();
            
            		Thread t2 = new Thread(ghost);
            		//t2.start();
            }
        });
	}
}
