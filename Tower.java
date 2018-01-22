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
}