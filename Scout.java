import java.util.*;
import java.awt.*;

public class Scout
extends Unit
{
	int spd;
	public Scout(int level, int px, int py, int[][] nodGraph, int pEnd, boolean friend)
	{
		//            HP   Armor
		super(level , 14  ,0.0,   px, py, 1.2, nodGraph, pEnd, friend, 21, 1);
	}

	public void tickX(int dir)
	{
		//this.dx = pDx;
		this.x += (int)(this.speed*dir);

	}

	public void tickY(int dir)
	{
		//this.dy = pDy;
		this.y += (int)(this.speed*dir);
	}

	public int getValue()
	{
		return 20;
	}



	public void render(Graphics g)
	{
		Color c = g.getColor();
		if(!FRIENDLY)
			g.setColor(Color.RED);
		else
			g.setColor(Color.blue);
		g.fillRect((int)x,(int)y,21,21);
		g.setColor(c);
	}

	public void upgradeUnit()
	{

	}
}