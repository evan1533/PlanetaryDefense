import java.util.*;
import java.awt.*;

public abstract class Weapon extends Tower
implements Runnable
{
	int attackRadius;
	int centerX, centerY;
	ArrayList<Unit> targetedEnemies;
	ArrayList<Tower> targetedTowers;
	ArrayList<Bullet> bullets = new ArrayList<>();
	ArrayList<Bullet> animationBullets = new ArrayList<>();
	ArrayList<Unit> currentEnemies = new ArrayList<>();
	int level;
	int cooldown;
	Thread th;
	int coolTime = 0;
	PlanetaryDefense game;
	int damage = 0;
	GameMap gameMap;
	double bulletSpeed = 0.5;

	public Weapon(int lvl, int px, int py, int pi, int pj, boolean friend, char subclass, int attackRad, int coold, int cost, int dmg, PlanetaryDefense pd)
	{
		super(px, py, pi, pj, friend,'W',subclass, cost);
		game = pd;
		attackRadius = attackRad;
		centerX = px+21;
		centerY = py+21;
		targetedEnemies = new ArrayList<>();
		targetedTowers = new ArrayList<>();
		level = lvl;
		cooldown = coold;
		damage = dmg;
		upgradeWeapon(level);
		this.gameMap = pd.map;
		th = new Thread(this);
		th.start();
	}

	//Scans for enemy ships within its firing radius
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
		if(!enem.checkAlive())
		{
			////System.out.println("YOU WEAKLING");
			return;
		}
		if(enemLoc<(attackRadius/2)&&!targetedEnemies.contains(enem))
		{
			targetedEnemies.add(enem);
		}
		else if(targetedEnemies.contains(enem)&&enemLoc>(attackRadius/2))
		{
			targetedEnemies.remove(enem);
		}

		if(coolTime>cooldown&&targetedEnemies.size()>0)
		{
			fire(targetedEnemies.get(0));
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
			fire(targetedTowers.get(0));
			coolTime = 0;
		}
	}

	public void fire(Unit u)
	{
		bullets.add(new Bullet(centerX, centerY, bulletSpeed, u.x, u.y, u.speed));
	}
	public void fire(Tower u)
	{
		bullets.add(new Bullet(centerX, centerY, bulletSpeed, u.x+21, u.y+21, 0));
	}

	public void drawBullets(Graphics g)
	{
		for(int i = 0;i<bullets.size();i++)
		{
			bullets.get(i).render(g);
		}
		for(int i = 0;i<animationBullets.size();i++)
		{
			Bullet tempBullet = animationBullets.get(i);
			tempBullet.render(g);
			if(tempBullet.animating == false)
			{
				animationBullets.remove(tempBullet);
			}
		}
	}

	public ArrayList<Unit> getTargets()
	{
		return targetedEnemies;
	}

	public void checkMapCollision()
	{
		this.gameMap = game.map;
		MapObject[][] map = gameMap.getMap();

		for(int i = 0;i<map.length;i++)
		{
			for(int j = 0;j<map[i].length;j++)
			{
				for(int k = 0;k<bullets.size();k++)
				{
					Bullet tempBullet = bullets.get(k);
					if(tempBullet.checkCollide(map[i][j]))
					{
						if(SUBCLASS == 'C')
						{
							dealRadialDamage(tempBullet);
						}
						bullets.remove(tempBullet);
					}
				}
			}
		}
	}
	public void checkCollision(ArrayList<Unit> enemy)
	{
		for(int i = 0;i<enemy.size();i++)
		{
			Unit tempEnemy = enemy.get(i);
			if(tempEnemy.HP<=0)
				targetedEnemies.remove(tempEnemy);
			for(int j = 0;j<bullets.size();j++)
			{
				Bullet tempBullet = bullets.get(j);
				if(tempBullet.checkCollide(tempEnemy))
				{
					if(SUBCLASS == 'C')
					{
						dealRadialDamage(tempBullet);
						animationBullets.add(tempBullet);

					}
					else
					{
						tempEnemy.damage(damage);
						if(tempEnemy.HP-damage<=0)
						{
							targetedEnemies.remove(tempEnemy);
							if(FRIENDLY)
								game.addMoney(tempEnemy.getValue());
						}
					}
					bullets.remove(tempBullet);
				}
			}
		}
	}

	public void checkTowerCollision(ArrayList<Weapon> towers)
	{
		for(int i = 0;i<towers.size();i++)
		{
			Tower tempEnemy = towers.get(i);
			if(tempEnemy.HP<=0)
				targetedTowers.remove(tempEnemy);
			for(int j = 0;j<bullets.size();j++)
			{
				Bullet tempBullet = bullets.get(j);
				if(tempBullet.checkTowCollide(tempEnemy))
				{
					if(tempEnemy.HP-damage<=0)
					{
						targetedTowers.remove(tempEnemy);
						if(FRIENDLY)
							game.addMoney(20);
						//game.updateMap();
					}
					bullets.remove(tempBullet);
					tempEnemy.damage(damage);
				}
			}
		}
	}

	public void tickBullets()
	{
		for(int i = 0;i<bullets.size();i++)
		{
			Bullet temp = bullets.get(i);
			bullets.get(i).tick();
			if(temp.x<281||temp.x>953||temp.y<11||temp.y>(11+16*42))
			{
				bullets.remove(temp);
			}
		}
	}

	public void run()
	{
		while(true)
		{
			coolTime++;
			if(bullets.size()>0)
			{
				tickBullets();
				checkMapCollision();
			}
			////System.out.println(coolTime);
			try{Thread.sleep(10);}catch(InterruptedException e){}
		}
	}

	public void dealRadialDamage(Bullet tempBullet)
	{
		for(int i = 0;i<currentEnemies.size();i++)
		{
			Unit tempEnemy = currentEnemies.get(i);
			int bulletX = tempBullet.x;
			int bulletY = tempBullet.y;
			double enemX = tempEnemy.x;
			double enemY = tempEnemy.y;

			double distance = Math.sqrt( Math.pow( (bulletX - enemX) , 2) + Math.pow( (bulletY - enemY) , 2));
			double dmg = 10 * Math.pow( Math.E, -distance*0.023);
			if(distance<100)
			{
				//System.out.println("\n\n"+distance+" "+dmg+"\n\n");
				tempEnemy.damage((int)dmg);
			}
			if(tempEnemy.HP-(int)dmg<=0)
			{
				currentEnemies.remove(tempEnemy);
				targetedEnemies.remove(tempEnemy);
				if(FRIENDLY)
					game.addMoney(tempEnemy.getValue());
			}
		}
	}
	abstract void upgradeWeapon(int lvl);

	class Bullet
	{
		int x, y;
		double dx, dy;
		double targetX, targetY;
		double velocityX, velocityY;
		double blastRadius = 100;
		double animRad = 0;
		boolean collided = false;
		boolean animating = false;

		public Bullet(int px, int py, double spd, double tX, double tY, double vx)
		{
			x = px;
			y = py;
			dx = spd;
			dy = spd;
			targetX = tX;
			targetY = tY;
			velocityX = vx;
			velocityY = vx;
			dx *= (((double)(targetX-x))/3.0);
			dy *= (((double)(targetY-y))/3.0);
			//System.out.println((dx)+" "+(dy));
		}

		public void render(Graphics g)
		{
			Color c = g.getColor();
			if(!animating)
			{
				g.setColor(Color.black);
				g.fillOval(x-5, y-5, 10, 10);
				g.setColor(c);
			}

			if(animRad<blastRadius&&collided)
			{
				animating = true;
				g.setColor(new Color(255,0,0,(int)( 255-(animRad*1.2) )));
				g.fillOval((int)(x-(animRad/2.0)),(int)(y-(animRad/2.0)),(int)animRad,(int)animRad);
				animRad+=10;
			}
			else
				animating = false;
			g.setColor(c);
		}

		public void tick()
		{
			x+=dx;
			y+=dy;
		}

		//If one tower destroys while another tower has it stored in its own arraylist/has it targeted
		public boolean checkCollide(Unit enem)
		{
			////System.out.println((x+dx)+" "+(y+dy)+" "+enem.x+" "+enem.y);
			/*if((x)>=enem.x&&(x)<=enem.x+enem.width&&(y)>=enem.y&&(y)<=enem.y+enem.width)
				{
				return true;
			}*/

			double di = 0;
			if(dx!=0)
				di = (dx/Math.abs(dx));

			double dj = 0;
			if(dy!=0)
				dj = (dy/Math.abs(dy));

			for(int i = 0; Math.abs(i)<Math.abs(dx);i+=di)
			{
				for(int j = 0; Math.abs(j)<Math.abs(dy);j+=dj)
				{
					if(x+10>=enem.x&&x<=enem.x+enem.width&&y+10>=enem.y&&y<=enem.y+enem.width)
					{
						collided = true;
						return true;
					}
				}
			}
			return false;
		}

		public boolean checkCollide(MapObject obj)
		{
			if(x>=obj.x &&x <=obj.x+obj.width && y>=obj.y && y<=obj.y+obj.width && obj.TYPE==1)
			{
				collided = true;
				return true;
			}
			return false;
		}

		public boolean checkTowCollide(Tower obj)
		{
			if(x>=obj.x &&x <=obj.x+obj.width && y>=obj.y && y<=obj.y+obj.width)
			{
				collided = true;
				return true;
			}
			return false;
		}
	}

}