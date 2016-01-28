import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

public class MenuMain extends JFrame{
	
	TileMap m = new TileMap();
	Image image1, image2;
	
	File sound1;
	AudioInputStream AStream1;
	Clip clip1;
	
	public void loadSounds() throws IOException, UnsupportedAudioFileException, LineUnavailableException
	{
		sound1 = new File("src/res/pacman_beginning.wav");
		
		AStream1 =  AudioSystem.getAudioInputStream(sound1);
		clip1 = AudioSystem.getClip();
		clip1.open(AStream1);
		clip1.start();

	}

public class AL extends KeyAdapter{
		
		public void keyPressed(KeyEvent e){
			
			int keyCode = e.getKeyCode();
			
			if(keyCode == KeyEvent.VK_SPACE)
			{	
				driver.main(null);
				dispose();
				
			}
			if(keyCode == KeyEvent.VK_ESCAPE)
			{
				System.exit(0);
			}
			
			if(keyCode == KeyEvent.VK_H)
			{
				HighScore.main(null);
				dispose();
			}
			
		}
	}
	
	
	MenuMain(){
		
		setTitle ("Pac-Man Start Screen");
		
		setSize(1000,700);
		setVisible(true);
		addKeyListener(new AL());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		try {
			loadSounds();
			} catch (IOException | UnsupportedAudioFileException
				| LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args)
	{
		MenuMain m = new MenuMain();
		
	}
	
	public void paint(Graphics g)
	{
		Font a= new Font("Tahoma",Font.BOLD+Font.ITALIC,45);
		Font b= new Font("Tahoma",Font.BOLD+Font.ITALIC,30);
		
		g.fillRect(0, 0, 1000, 700);
		
		
		 ImageIcon i2 = new ImageIcon("src/res/pac-man-logo.gif");
		 image2 = i2.getImage();
		 ImageIcon i1 = new ImageIcon("src/res/Pac_Man_640x960_3155.jpg");
		 image1 = i1.getImage();
		 
		 g.drawImage(image2, 250, 50, null);
		 g.drawImage(image1, 0, 400,1000,400,null);
		 
		 g.setColor(Color.YELLOW);
		 g.setFont(a);
	     g.drawString("To start a new game hit 'SPACE'",130, 400);
	     g.setFont(b);
	     g.setColor(Color.RED);
	     g.drawString("To exit hit 'ESCAPE'",350, 450);
	     g.drawString("For High Scores hit 'H'",330, 500);
	     
	}
}
