import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class TileMap{
	
	private File mapFile;
	private char[][] maze;
	private BufferedImage[] image;
	
	private int width, height;
	private int xCoord, yCoord;
	
	private File sound1;
	private AudioInputStream AStream1;
	Clip clip1;
	
	Ghost g;
	
	public void loadSounds() throws IOException, UnsupportedAudioFileException, LineUnavailableException
	{
		sound1 = new File("src/res/pacman_chomp1.wav");
		AStream1 = AudioSystem.getAudioInputStream(sound1);
		clip1 = AudioSystem.getClip();
		clip1.open(AStream1);
		clip1.loop(1);
	}
	//return frame width after TileMap load()
	public int getWidth(){
		return width;
	}
	
	//return frame height after TileMap load()
	public int getHeight(){
		return height;
	}
	
	
	
	//return character in maze[][]
	public char getCharAtXY(int x, int y){
		char c;
		x/=24;
		y/=24;
		
		c = maze[y][x];
		return c;
	}
	
	//set character at x and y position
	public void setAtXY(int y, int x, char c){
		
		y /= 24;
		x /= 24;
		
		maze[y][x] = c;
	}
	
	public void eatDotAtXY(int y, int x){
		
		//if dot
		if(maze[y][x] == '.')
		{
			maze[y][x] = ' ';
			
			try{
				if (clip1 == null || !clip1.isRunning()) {
					loadSounds();
				}
			} catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
				e.printStackTrace();
			}
			Pac.dotCounter--;
			Pac.score += 10;
			
			if(Pac.score % 1000 == 0)
				Pac.lives++;
			
			g.pDotCount++;
		}
		//if power dot
		if(maze[y][x] == 'o')
		{
			maze[y][x] = ' ';
			g.scared = true;
			
			Pac.dotCounter--;
			Pac.score += 50;		
		}
		
		//Ghost.reset() resets these values to 0 everytime
		//pacman dies to ensure ghost do not move until pacman
		//has eaten required amount of dots.
		if(g.pDotCount >= 5)
		{
			g.iDotCount++;
			
			if(g.iDotCount >= 5)
			{
				g.cDotCount++;
			}
		}
	}

	public int getPacAtX(){
		
		for(int i = 0; i < maze.length; i++){
			for(int j = 0; j < maze[i].length; j++)
				if(maze[i][j]=='O'){
					xCoord = j;
					return xCoord;
				}
		}
		return 0;
	}
	
	public int getPacAtY(){
		
		for(int i = 0; i < maze.length; i++){
			for(int j = 0; j < maze[i].length; j++)
				if(maze[i][j]=='O'){
					yCoord = i;
					return yCoord;
				}
		}
		return 0;
	}
	
	//constructor
	public TileMap(){
		
		load("maze.txt");
		loadGraphics();
		try {
			loadSounds();
		} catch (IOException | UnsupportedAudioFileException
				| LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	//main	
	public static void main(String[] args){
			
		System.out.println("Working Directory = " +
					System.getProperty("user.dir"));
		
		TileMap m = new TileMap();
		m.load("maze.txt");
	}
	
	//loads given fileName, reads txt file and stores in maze[][]
	//also determines dimensions for jframe
	public void load(String fileName){
		
		try{	
			mapFile = new File(fileName);
	
			BufferedReader reader = new BufferedReader(new FileReader("src/res/"+mapFile));
			ArrayList<char[]> map = new ArrayList<char[]>();
			String line = null;
		
			while((line = reader.readLine()) != null){
				char[] row = line.toCharArray();
				map.add(row);
			}
			reader.close();
			maze = new char[map.size()][];
			map.toArray(maze);
			
			height = (maze.length-1)*24;
			width = (maze[0].length)*24;
					
		} catch (IOException e) {
			System.out.println("INVALID FILE");
		}
	}
	
	//load all images into image[] 
	public void loadGraphics(){
		
		image = new BufferedImage[10];
		String res = "src/res/";
		
		try{
			image[0] = ImageIO.read(new File(res+"tile_bot_left.png"));
			image[1] = ImageIO.read(new File(res+"tile_bot_right.png"));
			image[2] = ImageIO.read(new File(res+"tile_top_left.png"));
			image[3] = ImageIO.read(new File(res+"tile_top_right.png"));
			image[4] = ImageIO.read(new File(res+"tile_horizontal.png"));
			image[5] = ImageIO.read(new File(res+"tile_empty.png"));
			image[6] = ImageIO.read(new File(res+"tile_vertical.png"));
			image[7] = ImageIO.read(new File(res+"tile_dot.png"));
			image[8] = ImageIO.read(new File(res+"tile_power_dot.png"));
			image[9] = ImageIO.read(new File(res+"door.png"));
			
		} catch (IOException e) {}
		
	}
	
	public BufferedImage loadImage(int i){
		return image[i];
	}
	//draws maze images according to maze[][] positions
	public void draw(Graphics g){
		
		int x = 0, y = 0;
		int rows = 0;
		
		//wall tiles
		while(rows < maze.length){
			for(int cols = 0; cols < maze[rows].length; cols++){
				
				//vertical tile
				if(maze[rows][cols]=='|')
					g.drawImage(image[6], x, y, null);
				
				//horizontal tile
				if(maze[rows][cols]=='-')
					g.drawImage(image[4], x, y, null);
				
				//top left tile
				if(maze[rows][cols]=='(')
					g.drawImage(image[2], x, y, null);
				
				//top right tile
				if(maze[rows][cols]==')')
					g.drawImage(image[3], x, y, null);
				
				//bottom left tile
				if(maze[rows][cols]=='[')
					g.drawImage(image[0], x, y, null);
				
				//bottom right tile
				if(maze[rows][cols]==']')
					g.drawImage(image[1], x, y, null);
				
				
				
				x+=24;
			}
			rows++;
			x = 0;
			y+=24;
		}
		y = 0;
		rows = 0;
		
		//empty space, dots and power dots
		while(rows < maze.length){
			for(int cols = 0; cols < maze[rows].length; cols++){
				
				//dot
				if(maze[rows][cols]=='.')
				{
					g.drawImage(image[7], x, y, null);
					Pac.dotCounter++;
				}
				
				//power dot
				if(maze[rows][cols]=='o')
				{
					g.drawImage(image[8], x, y, null);
					Pac.dotCounter++;
				}
				
				//empty space
				if(maze[rows][cols]==' ')
				{
					g.drawImage(image[5], x, y, null);
				}
				
				//ghost house door
				if(maze[rows][cols]=='=')
					g.drawImage(image[9], x, y, null);
							
				x+=24;
			}
			rows++;
			x = 0;
			y+=24;
		}
	}
	
	//prints maze as rows of strings
	public void print(){
		
		int rows = 0;
		
		while(rows < maze.length){
			for(int cols = 0; cols < maze[rows].length; cols++)
			System.out.print(maze[rows][cols]);
			System.out.print(maze[rows].length);
			System.out.println("");
			rows++;
		}
	}
}
