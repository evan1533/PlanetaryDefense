import java.util.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Scout
extends Unit
{
	int spd;
	public Scout(int level, int px, int py, int[][] nodGraph, int pEnd, boolean friend)
	{
		//            HP   Armor         Speed
		super(level , 14  ,0.0,   px, py, 1.0, nodGraph, pEnd, friend, 21, 1, 'S');
	}

	public void tickX(int dir)
	{
		//this.dx = pDx;
		this.x += this.speed*dir;

	}

	public void tickY(int dir)
	{
		//this.dy = pDy;
		this.y += this.speed*dir;
	}

	public int getValue()
	{
		return 20;
	}

    public void createUnitImage()
    {
        unitImage = new BufferedImage(this.width,this.height,BufferedImage.TYPE_INT_ARGB);
        Graphics img = unitImage.getGraphics();
        if(!FRIENDLY)
            img.setColor(Color.RED);
        else
            img.setColor(Color.blue);
        img.fillRect(0,0,21,21);
    }

	public void upgradeUnit()
	{

	}

}