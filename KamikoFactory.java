import java.awt.*;
import java.awt.image.*;

public class KamikoFactory extends Factory
{
    public KamikoFactory(int px, int py, int pi, int pj, boolean friendly,int[][] nodeGraf, int myBase, int enemBase)
    {                           //Prod rate                               res cost
        super(px,py,pi,pj,friendly, 500,  'K', nodeGraf, myBase, enemBase, 25, 50);
    }

    public void upgradeFactory()
    {

    }

    public void developUnit()
    {
        int x = 281+(myBase%16)*42;
        int y = 11+(myBase/16)*42;

        units.add(new Kamiko(1,x,y,nodeGraph,enemBase,friendly));
    }

    public void developUnit(int num)
    {
        for(int i = 0;i<num;i++)
        {
            units.add(new Kamiko(1,0,0,nodeGraph,end,friendly));
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
        g.setColor(Color.ORANGE);
        g.fillOval(x+10,y+10,21,21);
        g.setColor(c);
    }

    public static BufferedImage getImage()
    {
        myImage = new BufferedImage(42,42,BufferedImage.TYPE_INT_ARGB);
        Graphics img = myImage.getGraphics();
        //if(FRIENDLY)
        //{
            img.setColor(Color.blue);
        //}
        //else
        //{
        //  img.setColor(Color.red);
        //}

        img.fillOval(0,0,42,42);
        img.setColor(Color.MAGENTA);
        img.fillOval(10,10,11,11);
        return myImage;
    }


}
