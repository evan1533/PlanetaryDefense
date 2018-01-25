import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

public class Rock extends MapObject
{
	Image image = new ImageIcon(this.getClass().getResource("Images/PD_Rock2.PNG")).getImage();
	static BufferedImage myImage;
	public Rock(int px, int py, int pi, int pj)
	{
		super(1, px, py, pi, pj);
	}

	public void render(Graphics g)
	{
		////System.out.println("AR");
		g.fillRect(x,y,42,42);
	}

	public void renderRockImage(Graphics g)
	{
		g.drawImage(image,x,y,42,42,null);
	}
	
	public static BufferedImage getImage()
	{
		myImage = new BufferedImage(42,42,BufferedImage.TYPE_INT_ARGB);
		Graphics img = myImage.getGraphics();
		img.setColor(new Color(50,50,50));
		img.fillRect(0,0,42,42);

		return myImage;
	}
}