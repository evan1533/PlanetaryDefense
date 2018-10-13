import java.awt.*;
import java.awt.image.*;

public class ScoutFactory extends Factory
{
	public ScoutFactory(int px, int py, int pi, int pj, boolean friendly,int[][] nodeGraf, int myBase, int enemBase)
	{							//Prod rate								  res cost
		super(px,py,pi,pj,friendly, 500,  'S', nodeGraf, myBase, enemBase, 25, 30);
	}

	public void upgradeFactory()
	{
	}

	public void developUnit()
	{
		int x = 281+(myBase%16)*42;
		int y = 11+(myBase/16)*42;

		units.add(new Scout(1,x,y,nodeGraph,enemBase,friendly));
		////System.out.println("WE MADE IT!");
	}

	public void developUnit(int num)
	{
		for(int i = 0;i<num;i++)
		{
			units.add(new Scout(1,0,0,nodeGraph,end,friendly));
		}
	}

	public void render(Graphics g)
	{

		Color c = g.getColor();
		if(FRIENDLY)
		{
			g.setColor(Color.blue);
		}
		else
		{
			g.setColor(Color.red);
		}
		g.fillOval(x,y,42,42);
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

		img.fillOval(0,0,42,42);
		return myImage;
	}


}
