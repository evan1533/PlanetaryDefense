import java.awt.*;
import java.awt.image.*;

public class Turret extends Weapon
{
	static BufferedImage myImage;
	/*
	BASE STATS:
	-
	*/

	public Turret(int level,int px, int py, int pi, int pj, boolean friend, PlanetaryDefense gm)
	{
		//                                      at. rad   cool  cost  dmg
		super(level,px, py, pi, pj, friend, 'T',  250   ,  85,   20,   5 , gm);
	}

	public void render(Graphics g)
	{

		Color c = g.getColor();
		//g.setColor(new Color(127,127,127,100));
		//g.fillOval(centerX-(attackRadius/2), centerY-(attackRadius/2), (attackRadius),(attackRadius));
		if(FRIENDLY)
		{
			g.setColor(Color.blue);
		}
		else
		{
			g.setColor(Color.red);
		}

		g.fillRect(x,y,42,42);
		g.setColor(Color.black);
		g.fillRect(x+11,y+11,20,20);

		g.setColor(c);


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
		img.setColor(Color.black);
		img.fillRect(11,11,20,20);

		return myImage;
	}

	public void upgradeWeapon(int lvl)
	{
		if(lvl == 2)
		{
			attackRadius = 850;
		}
	}
}