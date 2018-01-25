import java.awt.*;
import java.awt.image.*;

public abstract class Tower extends MapObject
{
	boolean FRIENDLY;
	char TOWER_TYPE;
	char SUBCLASS;
	static BufferedImage myImage;
	int cost;
	int sellValue;
	int HP = 20;

	public Tower(int px, int py, int pi, int pj, boolean friendly, char typ, char subclass, int pcost)
	{
		super(3,px,py,pi,pj);
		FRIENDLY = friendly;
		TOWER_TYPE = typ;
		SUBCLASS = subclass;
		cost = pcost;
		sellValue = (int)(pcost*.8);
	}

	abstract public void render(Graphics g);

	public String toString()
	{
		return this.TOWER_TYPE+" "+this.SUBCLASS+" "+this.FRIENDLY;
	}

	public boolean checkAlive()
	{
		if(HP<=0)
			return false;
		return true;
	}

	public void damage(int dmg)
	{
		HP-=dmg;
		//System.out.println(HP);
	}
	
	public static BufferedImage getImage(boolean isFriendly)
	{
	    return myImage;
	}
	
	public static BufferedImage getImage(char towType, char sub, boolean isFriendly)
	{
		if(towType=='F')
		{
			if(sub=='S')
			{
				return ScoutFactory.getImage(isFriendly);
			}
			if(sub=='T')
			{
				return TankFactory.getImage(isFriendly);
			}
			if(sub=='K')
			{
				return KamikoFactory.getImage(isFriendly);
			}
		}
		if(towType=='W')
		{
			if(sub=='T')
			{
				return Turret.getImage(isFriendly);
			}
			if(sub=='C')
			{
				return Canon.getImage(isFriendly);
			}
		}
	    return myImage;
	}
}