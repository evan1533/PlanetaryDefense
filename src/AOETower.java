import java.awt.Graphics;
import java.util.ArrayList;


public abstract class AOETower extends Tower implements Runnable
{
	int attackRadius;
	int centerX, centerY;
	ArrayList<Unit> targetedEnemies;
	ArrayList<Tower> targetedTowers;
	ArrayList<Unit> currentEnemies = new ArrayList<>();
	int level;
	int cooldown;
	Thread th;
	int coolTime = 0;
	PlanetaryDefense game;
	int damage = 0;
	GameMap gameMap;
	
	public AOETower(int lvl, int px, int py, int pi, int pj, boolean friend, char subclass, int attackRad, int coold, int cost, int dmg, PlanetaryDefense pd) 
	{
		super(px, py, pi, pj, friend, 'A', subclass, cost);
		game = pd;
		attackRadius = attackRad;
		centerX = px+21;
		centerY = py+21;
		targetedEnemies = new ArrayList<>();
		targetedTowers = new ArrayList<>();
		level = lvl;
		cooldown = coold;
		damage = dmg;
		upgradeAOE(lvl);
		this.gameMap = pd.map;
		th = new Thread(this);
		th.start();
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
				this.removeAOE(enem);
				targetedEnemies.remove(temp);
			}

		}
		if(!enem.checkAlive())
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
			this.dealAOE(enem);
			targetedEnemies.add(enem);
		}
		else if(targetedEnemies.contains(enem)&&enemLoc>(attackRadius/2))
		{
			this.removeAOE(enem);
			targetedEnemies.remove(enem);
		}

		if(coolTime>cooldown&&targetedEnemies.size()>0)
		{
			//fire(targetedEnemies.get(0));
			coolTime = 0;
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

		if(coolTime>cooldown&&targetedTowers.size()>0)
		{
			//fire(targetedTowers.get(0));
			coolTime = 0;
		}
	}
	
	public boolean hasEnemies()
	{
		return targetedEnemies.size()>0;
	}
	public ArrayList<Unit> getTargets()
	{
		return targetedEnemies;
	}
	
	public void run()
	{
		while(true)
		{
			this.dealAOE();
			////System.out.println(coolTime);
			try{Thread.sleep(10);}catch(InterruptedException e){}
		}
	}
	@Override
	public void render(Graphics g)
	{
		// TODO Auto-generated method stub

	}
	public abstract void upgradeAOE(int lev);
	
	public abstract void dealAOE();
	
	public abstract void dealAOE(Unit u);
	
	public abstract void removeAOE();
	
	public abstract void removeAOE(Unit u);

}
