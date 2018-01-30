import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;

public class Tokamok extends AOETower
{
    static BufferedImage myImage;
    /*
    BASE STATS:
    -
    */
    private int animRad = 255;
    public Tokamok(int level,int px, int py, int pi, int pj, boolean friend, PlanetaryDefense gm)
    {
        //                                      at. rad   cool  cost  dmg
        super(level,px, py, pi, pj, friend, 'T',  250   ,  95,   20,   5 , gm);

    }

    public void render(Graphics g)
    {

        Color c = g.getColor();
        //g.setColor(new Color(127,127,127,100));
        //g.fillOval(centerX-(attackRadius/2), centerY-(attackRadius/2), (attackRadius),(attackRadius));
        if(FRIENDLY)
        {
            g.setColor(Color.BLUE);
            g.fillOval(x,y,42,42);
            g.setColor(Color.MAGENTA);
            g.fillOval(x+11,y+11,20,20);
            g.setColor(Color.BLUE);
            g.fillOval(x+14,y+14,14,14);
        }
        else
        {
            g.setColor(Color.red);
            g.fillOval(x,y,42,42);
            g.setColor(Color.MAGENTA);
            g.fillOval(x+11,y+11,20,20);
            g.setColor(Color.red);
            g.fillOval(x+14,y+14,14,14);
        }
        
        if(this.hasEnemies()||(!this.hasEnemies()&&animRad<255))
        {
        	g.setColor(new Color(138,212,255,255-animRad));
            Graphics2D g2 = (Graphics2D) g;
            Stroke OG_Stroke = g2.getStroke();
            g2.setStroke(new BasicStroke(8+(int)(animRad*0.15)));
            g2.drawOval(this.x-(animRad/2), this.y-(animRad/2),42+animRad, 42+animRad);
            animRad+=5;

            if(animRad>=255&&this.hasEnemies())
            {
            	animRad = 0;
            }
            g2.setStroke(OG_Stroke);
        }
        g.setColor(c);
        

    }

    public static BufferedImage getImage(boolean isFriend)
    {
        myImage = new BufferedImage(42,42,BufferedImage.TYPE_INT_ARGB);
        Graphics img = myImage.getGraphics();
        if(isFriend)
        {
            img.setColor(Color.BLUE);
            img.fillOval(0,0,42,42);
            img.setColor(Color.MAGENTA);
            img.fillOval(11,11,20,20);
            img.setColor(Color.BLUE);
            img.fillOval(14,14,14,14);
        }
        else
        {
            img.setColor(Color.red);
            img.fillOval(0,0,42,42);
            img.setColor(Color.MAGENTA);
            img.fillOval(11,11,20,20);
            img.setColor(Color.red);
            img.fillOval(14,14,14,14);
        }

        return myImage;
    }

    public void upgradeWeapon(int lvl)
    {
        if(lvl == 2)
        {
            attackRadius = 850;
        }
    }

	@Override
	public void upgradeAOE(int lev)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dealAOE()
	{
		// TODO Auto-generated method stub
		ArrayList<Unit> targets = this.getTargets();
		for(int i = 0;i<targets.size();i++)
		{
			if(targets.get(i).checkSpeed())
				targets.get(i).reduceSpeed();
		}
		
	}

	@Override
	public void removeAOE() 
	{
		// TODO Auto-generated method stub
		ArrayList<Unit> targets = this.getTargets();
		for(int i = 0;i<targets.size();i++)
		{
			targets.get(i).restoreSpeed();
		}
	}

	@Override
	public void dealAOE(Unit u) 
	{
		// TODO Auto-generated method stub
		if(u.checkSpeed())
			u.reduceSpeed();
	}

	@Override
	public void removeAOE(Unit u)
	{
		// TODO Auto-generated method stub
		u.restoreSpeed();
	}
}