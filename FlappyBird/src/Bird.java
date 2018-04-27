import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JDialog;
import javax.swing.JOptionPane;


public class Bird {
	private static final int SIZE=25;
	private Game match;
	private double y,yspeed;
	private double x;
	private double grav,lift;
	private BufferedImage img;
	private Clip hoop,death;
	
	public Bird(Game match) {
		this.match=match;
		y=match.getHeight()/2;
		x=SIZE;
		yspeed=0;
		grav=0.1;
		lift=7;
		
		try {
			img=ImageIO.read(getClass().getResource("cippy.png"));
		} catch (Exception e) {
		}
		
		try {
			AudioInputStream instream=AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream("funnysound.wav")));
			AudioInputStream ins=AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream("morte.wav")));
			hoop=AudioSystem.getClip();
			hoop.open(instream);
			death=AudioSystem.getClip();
			death.open(ins);
		} catch (Exception e) {
		} 
		
	}
	
	public void pressed(int keyCode) {
		if(keyCode==' ') {
			yspeed-=lift;
			y+=yspeed;
			hoop.start();
			hoop.setFramePosition(0);
		}
	}
	public void released(int keyCode) {
		if(keyCode==' ') 
			yspeed=0;
		
	}
	public void update()  {
		yspeed+=grav;
		yspeed*=0.98;
		y+=yspeed;
		if(y>match.getHeight()-2.25*SIZE) { // ho toccato terra quindi sono morto
			match.getGamePanel().stopFunnyMusic();
			death.start(); 	
			death.setFramePosition(0);
			Object[] op= {"Riproviamo","Basta Cos�"};
			JOptionPane pane=new JOptionPane("ops purtroppo sei morto :(((( che facciamo ora?",JOptionPane.INFORMATION_MESSAGE);
			pane.setOptions(op);
			JDialog dialog=pane.createDialog(match.getComponent(0), "Partita Conclusa");
			dialog.setVisible(true);
			if(pane.getValue()==null)
				System.exit(0);
			if(pane.getValue().equals(op[1]))
				System.exit(0);
			if(pane.getValue().equals(op[0]))
				match.getGamePanel().reset();
			
				
		}
		if(y<0) {
			y=0;
			yspeed=0;
		}
			
	}
	
	public void reset() {
		this.x=SIZE;
		y=match.getHeight()/2;
		yspeed=0;
	
	}
	public double getXPos() {
		return this.x;
	}
	
	public Rectangle getBounds() {
		return (new Rectangle((int)x,(int)y,SIZE,SIZE));
	}
	public void paint(Graphics g) {
		g.drawImage(img, (int)x, (int)y, SIZE, SIZE, null);
	}
	
}
