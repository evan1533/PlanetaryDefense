import java.util.*;
import java.awt.*;

public class Kamiko
extends Unit
{
    public Kamiko(int level, int px, int py, int[][] nodGraph, int pEnd, boolean friend)
    {
        //            HP   Armor
        super(level , 8  , 0,   px, py, 2, nodGraph, pEnd, friend, 21,35, 8);
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



    public void render(Graphics g)
    {
        Color c = g.getColor();
        if(!FRIENDLY)
            g.setColor(Color.RED);
        else
            g.setColor(Color.blue);
        g.fillRect((int)x,(int)y,width,height);
        g.setColor(c);
        g.fillRect((int)x+10,(int)y,5,height);
    }

    public void upgradeUnit()
    {

    }
}