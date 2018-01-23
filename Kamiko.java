import java.util.*;
import java.awt.*;

public class Kamiko
extends Unit
{
    ArrayList<Unit> targetedEnemies= new ArrayList<>();
    ArrayList<Tower> targetedTowers= new ArrayList<>();
    ArrayList<Unit> currentEnemies = new ArrayList<>();
    int centerX, centerY;
    int attackRadius = 150;
    int animRad = 0;
    boolean exploding = false;
    //Potential upgrade would be EMP blast that temporarily disables enemy units and towers.
    public Kamiko(int level, int px, int py, int[][] nodGraph, int pEnd, boolean friend)
    {
        //            HP   Armor
        super(level , 8  , 0,   px, py, 2, nodGraph, pEnd, friend, 21,35, 8,'K');
        centerX = px+(this.width/2);
        centerY = py+(this.height/2);
    }

    public void tickX(int pDx)
    {
        this.x += this.speed*pDx;
        centerX = (int)this.x+(this.width/2);
        if(!this.exploding&&targetedEnemies.size()>3)
            this.explode();
    }

    public void tickY(int pDy)
    {
        this.y += this.speed*pDy;
        centerY = (int)this.y+(this.height/2);
        if(!this.exploding&&targetedEnemies.size()>3)
            this.explode();
    }

    public int getValue()
    {
        return 20;
    }
    
    public void explode()
    {
        System.out.println("EXPLODING");
        exploding = true;
        for(int i = 0;i<targetedEnemies.size();i++)
            targetedEnemies.get(i).damage(this.baseDamage);
    }
    
    public void updateEnemies(ArrayList<Unit> enems)
    {
        currentEnemies = enems;
    }

    public void scan(Unit enem)
    {
        int enemX = (int)(enem.getX()-centerX);
        int enemY = (int)(enem.getY()-centerY);
        int enemLoc = (int)Math.sqrt((Math.pow(enemX,2)+Math.pow(enemY,2)));
        for(int i = 0;i<targetedEnemies.size();i++)
        {
            Unit temp = targetedEnemies.get(i);
            if(temp.checkAlive()==false)
            {
                targetedEnemies.remove(temp);
            }

        }
        if(!enem.checkAlive())
        {
            targetedEnemies.remove(enem);
        }
        if(enemLoc<(attackRadius/2)&&!targetedEnemies.contains(enem))
        {
            targetedEnemies.add(enem);
        }
        else if(targetedEnemies.contains(enem)&&enemLoc>(attackRadius/2))
        {
            targetedEnemies.remove(enem);
        }

    }
    public void scan(Tower enem)
    {
        int enemX = ((enem.x+21)-centerX);
        int enemY = ((enem.y+21)-centerY);
        int enemLoc = (int)Math.sqrt((Math.pow(enemX,2)+Math.pow(enemY,2)));
        for(int i = 0;i<targetedTowers.size();i++)
        {
            Tower temp = targetedTowers.get(i);
            if(temp.checkAlive()==false)
            {
                targetedTowers.remove(temp);
            }

        }
        if(!enem.checkAlive()&&targetedEnemies.contains(enem))
        {
            targetedEnemies.remove(enem);
        }
        if(!enem.checkAlive())
        {
            ////System.out.println("YOU WEAKLING");
            return;
        }
        if(enemLoc<(attackRadius/2)&&!targetedEnemies.contains(enem))
        {
            targetedTowers.add(enem);
        }
        else if(targetedEnemies.contains(enem)&&enemLoc>(attackRadius/2))
        {
            targetedTowers.remove(enem);
        }

    }

    public void render(Graphics g)
    {
        if(!exploding)
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
        else
        {
            g.setColor(new Color(255,0,0,(int)( 255-(animRad*1.2) )));
            g.fillOval((int)(x-(animRad/2.0)),(int)(y-(animRad/2.0)),(int)animRad,(int)animRad);
            animRad+=10;
            if( animRad*1.2>=250)
                this.alive = false;
        }
    }

    public void upgradeUnit()
    {

    }
}