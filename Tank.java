import java.util.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Tank
extends Unit
{
	public Tank(int level, int px, int py, int[][] nodGraph, int pEnd, boolean friend)
	{
		//            HP   Armor
		super(level , 80  ,30.0,   px, py, .5, nodGraph, pEnd, friend, 21,35,8,'T');
	}

	public void tickX(int pDx)
	{
		this.x += this.speed*pDx;
	}

	public void tickY(int pDy)
	{
		this.y += this.speed*pDy;
	}

	public int getValue()
	{
		return 20;
	}
	
	public void createUnitImage()
	{
        unitImage = new BufferedImage(this.width,this.height,BufferedImage.TYPE_INT_ARGB);
        Graphics2D img = (Graphics2D)unitImage.getGraphics();
        if(!FRIENDLY)
            img.setColor(Color.RED);
        else
            img.setColor(Color.blue);
        img.fillRect(0,0,width,height);
        img.setColor(new Color(80,80,80));
        img.fillRect(10, 0,5,height);
	}


	public void upgradeUnit()
	{

	}
}