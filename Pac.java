import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Pac implements ImageObserver {
	
	public static int score = 0;
	public static int lives = 3;
	public static int dotCounter = 0;
	
	//x: x coordinates
	//y: y coordinates
	//xVel: x velocity after wall collision detection
	//yVel: y velocity after wall collision detection
	//prefxVel: preferred x velocity set from keys pressed
	//prefyVel: preferred y velocity set from keys pressed
	//wall, if collision set to true, else false
	public int x, y;
	public int xVel, yVel, prefxVel, prefyVel;
	boolean wall = true;
	
	public BufferedImage[] pacImage;
	
	public TileMap map;
	

	public Pac(){
		
		loadPacGraphics();
	}
	
	public void loadPacGraphics(){
		
		pacImage = new BufferedImage[14];
		
		String res = "src/res/";
		
		try{
			pacImage[0] = ImageIO.read(new File(res+"pac_right_1.png"));
			pacImage[1] = ImageIO.read(new File(res+"pac_right_2.png"));
			pacImage[2] = ImageIO.read(new File(res+"pac_right_3.png"));
			pacImage[3] = ImageIO.read(new File(res+"pac_left_1.png"));
			pacImage[4] = ImageIO.read(new File(res+"pac_left_2.png"));
			pacImage[5] = ImageIO.read(new File(res+"pac_left_3.png"));
			pacImage[6] = ImageIO.read(new File(res+"pac_up_1.png"));
			pacImage[7] = ImageIO.read(new File(res+"pac_up_2.png"));
			pacImage[8] = ImageIO.read(new File(res+"pac_up_3.png"));
			pacImage[9] = ImageIO.read(new File(res+"pac_down_1.png"));
			pacImage[10] = ImageIO.read(new File(res+"pac_down_2.png"));
			pacImage[11] = ImageIO.read(new File(res+"pac_down_3.png"));
			pacImage[12] = ImageIO.read(new File(res+"pac_full.png"));
			//pacImage[13] = ImageIO.read(new File(res+"pac-man-logo.gif"));
			
			
		} catch (IOException e) {System.out.println("INVALID IMAGE READ");}
	}
	
	int gframe = 0;
	
	public void paintPac(Graphics g, boolean w, int xv, int yv, int x, int y){
		
		//drawing strings for score, lives and esc
		Font b = new Font("Tahoma",Font.BOLD,40);
		Font a = new Font("Tahoma",Font.BOLD,30);
		
		
		g.drawImage(pacImage[13], 720, 50, 400, 150, this);
		g.setColor(Color.white);

		g.setFont(b);
		g.drawString("Score: " + score, 725, 100);
		g.drawString("Lives: " + lives, 735, 170);
		
		g.setFont(a);
		g.setColor(Color.RED);
		g.drawString("To exit hit 'ESC'",685, 650);

		//pacman not moving
			if(xv == 0 && yv == 0)
				g.drawImage(pacImage[0], x, y, 32, 32, this);
		
		//pacman moving right, if w is true, he's at wall
		//so stop move animation
			else if(xv == 1)
			{
				if(w == true)
				{
					g.drawImage(pacImage[1], x, y, 32, 32, this);
					return;
				}
				
				int frame = gframe/4;
			
				if(frame > 2)
					frame = 12;
				if(frame > 12)
					frame = 0;
				g.drawImage(pacImage[frame], x, y, 32, 32, this);
				frame++;
			}
		
			//pacman moving left, if w is true, he's at wall
			//so stop move animation
			else if(xv == -1)
			{
				if(w == true)
				{
					g.drawImage(pacImage[4], x, y, 32, 32, this);
					return;
				}
				
				
				int frame = gframe/4+3;
			
				if(frame > 5)
					frame = 12;
				if(frame > 12)
					frame = 3;
				g.drawImage(pacImage[frame], x, y, 32, 32, this);
				frame++;
			}
		
			//pacman moving up, if w is true, he's at wall
			//so stop move animation
			else if(yv == -1)
			{
				if(w == true)
				{
					g.drawImage(pacImage[7], x, y, 32, 32, this);
					return;
				}
				
				
				int frame = gframe/4+6;
			
				if(frame > 8)
					frame = 12;
				if(frame > 12)
					frame = 6;
				g.drawImage(pacImage[frame], x, y, 32, 32, this);
				frame++;
			}
			
			//pacman moving down, if w is true, he's at wall
			//so stop move animation
			else if(yv == 1)
			{
				if(w == true)
				{
					g.drawImage(pacImage[10], x, y, 32, 32, this);
					return;
				}
				
				
				int frame = gframe/4+9;
			
				if(frame > 12)
					frame = 9;
				g.drawImage(pacImage[frame], x, y, 32, 32, this);
				frame++;
			}
			
			gframe++;
			if(gframe == 12)
				gframe = 0;
	}
	

	public void move(){
	
		char c;
		
		//pcx: pacmans center x
		//pcy: pacmans center y
		int pcx = x + 12;
		int pcy = y + 12;
		
		centerPac();
	
		//depending on what direction pacman is set to determines
		//where to check for wall collision
		c = map.getCharAtXY(pcx + prefxVel*14, pcy + prefyVel*14);
	
		//if no wall then prefxVel sets xVel and
		//prefyVel sets yVel
		if((c == ' ' || c == '.' || c == 'o' || c == 'O'))
		{
			xVel = prefxVel;
			yVel = prefyVel;
		
			wall = false;
		}
	
		else
		{
		
			//if wall, continue previous xVel and yVel
			char c2 = map.getCharAtXY(pcx + xVel*12, pcy + yVel*12);
		
			if((c2 == ' ' || c2 == '.' || c2 == 'o' || c2 == 'O'))
			{
				wall = false;
			}
		
			//else wall collision
			else
			{
				xVel = 0;
				yVel = 0;
			
				wall = true;
			}
		}
		
		
		x += xVel;
		y += yVel;
			
		map.eatDotAtXY(pcy/24, pcx/24);
			
		//bounds for maze and
		//pacman teleportation
		if(x < 23)
			x = map.getWidth() - 47;
			
		if(x > map.getWidth() - 47)
			x = 23;
			
		if(pcy + yVel*12 < 0)
			y = pcy + yVel*12;
				
		if(pcy + yVel*12 > map.getHeight())
			y = map.getHeight() - (pcy + yVel*12);
		
		//determines move animation for corresponding
		//direction
		if(wall == true)
		{
			xVel = prefxVel;
			yVel = prefyVel;
		}
	
	}

	public void centerPac(){
	
		//if going left or right
		if(xVel != 0)
		{	
			y = (y + 12)/24;
			y *= 24;
		}
	
		//if going up or down
		if(yVel != 0)
		{
			x = (x + 12)/24;
			x *= 24;
		}
		
	}		

	
	public void setXVelocity(int xv){
		prefxVel = xv;
		
	}
	
	public void setYVelocity(int yv){
		prefyVel = yv;
	}
	
	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y,
			int width, int height) {
		// TODO Auto-generated method stub
		return false;
	}
}
