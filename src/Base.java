import java.awt.*;
import java.awt.image.BufferedImage;

public class Base extends MapObject
{
	Image baseImage;
	Color baseColor;
	int HP = 100;
	boolean friendly;
	static BufferedImage myImage;

	public Base(int px, int py, int pi, int pj, Color col, boolean friendly)
	{
		super(2, px, py, pi, pj);
		baseColor = col;
		this.friendly = friendly;
	}

	public void render(Graphics g)
	{
		Color c = g.getColor();
		//g.drawImage(baseImage,x,y,width,height,null);
		g.setColor(baseColor);
		g.fillRect(x,y,42,42);
		g.setColor(Color.green);
		g.fillRect(x+11,y+11,21,21);
		g.setColor(c);
	}

	public void hit(int dmg)
	{
		this.HP -= dmg;
	}
	
    public static BufferedImage getImage(boolean isFriend)
    {
        myImage = new BufferedImage(42,42,BufferedImage.TYPE_INT_ARGB);
        Graphics img = myImage.getGraphics();
        if(isFriend)
        {
            img.setColor(Color.blue);
        }
        else
        {
            img.setColor(Color.red);
        }

        img.fillRect(0,0,42,42);
        img.setColor(Color.green);
        img.fillRect(11,11,21,21);
        return myImage;
    }
}