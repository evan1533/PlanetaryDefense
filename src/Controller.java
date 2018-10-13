import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.image.*;
import java.text.*;
import java.math.*;
import java.applet.*;
import java.io.*;

public class Controller
{
	PlanetaryDefense currentGame;
	ArrayList<String> pathArray;
	ArrayList<Factory> enemyFactories = new ArrayList<>();
	ArrayList<Weapon> enemyWeapons = new ArrayList<>();
	ArrayList<Unit> enemies = new ArrayList<>();
	ArrayList<Wave> waves = new ArrayList<>();
	ArrayList<AOETower> enemy_aoe = new ArrayList<>();


	ArrayList<Factory> factories = new ArrayList<>();
	ArrayList<Unit> myUnits = new ArrayList<>();
	ArrayList<Unit> activeUnits = new ArrayList<>();
	ArrayList<Weapon> weapons = new ArrayList<>();
	ArrayList<AOETower> aoe_tows = new ArrayList<>();

	int bw = 42;
	int offsetX = 281;
	int offsetY = 11;
	int deployNum = 0;
	int myLastDeployment = -999;
	int offX = 0;
	int offY = 20;

	public Controller(PlanetaryDefense gam)
	{
		currentGame = gam;
	}

	public void addEnemy(Unit enem)
	{
		enemies.add(enem);
	}

	public void render(Graphics g)
	{
		for(int i = 0; i<enemies.size();i++)
		{
			(enemies.get(i)).render(g);
		}
		for(int i = 0; i<activeUnits.size();i++)
		{
			(activeUnits.get(i)).render(g);
		}
		for(int i = 0; i<factories.size();i++)
		{
			(factories.get(i)).render(g);
		}
		for(int i = 0; i<weapons.size();i++)
		{
			(weapons.get(i)).drawBullets(g);
		}
		for(int i = 0; i<enemyWeapons.size();i++)
		{
			(enemyWeapons.get(i)).drawBullets(g);
		}
	}

	public void updatePath(ArrayList<String> pPath)
	{
		pathArray = pPath;
	}

	public int getUnitCount()
	{
		if(myUnits.size()-deployNum>0)
			return myUnits.size()-deployNum;
		else
			return 0;
	}

	public void findEnemyPath()
	{
		for(int i = 0;i<enemies.size();i++)
		{
			Unit temp = enemies.get(i);
			if(!temp.hasPath)
			{
				temp.findPath();
			}
		}

		for(int i = 0;i<myUnits.size();i++)
		{
			Unit temp = myUnits.get(i);
			if(!temp.hasPath)
			{
				temp.findPath();
			}
		}
	}

	public void addEnemyTower(Tower t)
	{
		char typ1 = t.TOWER_TYPE;
		char typ2 = t.SUBCLASS;

		if(typ1 == 'F')
		{
			Factory tempFactory = (Factory)(t);
			enemyFactories.add(tempFactory);
			//System.out.println("ASSHAT");
		}
		if(typ1 == 'W')
		{
			Weapon tempWeapon = (Weapon)(t);
			enemyWeapons.add(tempWeapon);
			//System.out.println("TURRET TIME");
		}
		if(typ1 == 'A')
		{
			AOETower tempAOE = (AOETower)(t);
			enemy_aoe.add(tempAOE);
			//System.out.println("TURRET TIME");
		}
	}

	public void removeTower(Tower t)
	{
		char typ1 = t.TOWER_TYPE;
		char typ2 = t.SUBCLASS;

		if(typ1 == 'F')
		{
			Factory tempFactory = (Factory)(t);
			tempFactory.destroy();
			factories.remove(tempFactory);
			tempFactory = null;
			//System.out.println("ASSHAT");
		}
		if(typ1 == 'W')
		{
			Weapon tempTurret = (Weapon)(t);
			weapons.remove(tempTurret);
			//System.out.println("ASSHAT");
		}
		if(typ1 == 'A')
		{
			AOETower tempAOE = (AOETower)(t);
			enemy_aoe.remove(tempAOE);
			//System.out.println("TURRET TIME");
		}
	}

	public void addTower(Tower t)
	{
		char typ1 = t.TOWER_TYPE;
		char typ2 = t.SUBCLASS;

		if(typ1 == 'F')
		{
			Factory tempFactory = (Factory)(t);
			tempFactory.setUnitArray(myUnits);
			factories.add(tempFactory);
			//System.out.println("ASSHAT");
		}
		if(typ1 == 'W')
		{
			weapons.add((Weapon)t);
		}
		if(typ1 == 'A')
		{
			aoe_tows.add((AOETower)t);
		}
	}

	public ArrayList<Factory> getFactories()
	{
		return enemyFactories;
	}
	
	public boolean hasEnemy(int mx, int my)
	{
		for(int i = 0;i<enemies.size();i++)
		{
			Unit temp = enemies.get(i);
			int enemI = GameMap.iFromY((int)temp.y);
			int enemJ = GameMap.jFromX((int)temp.x);
			int clickI = GameMap.iFromY(my);
			int clickJ = GameMap.jFromX(mx);

			if(enemI == clickI && enemJ == clickJ)
			{
				return true;
			}
		}

		return false;

	}

	public void checkBullets()
	{
		for(int i = 0; i<weapons.size();i++)
		{
			weapons.get(i).checkCollision(enemies);
			weapons.get(i).checkTowerCollision(enemyWeapons);
		}
		for(int i = 0; i<enemyWeapons.size();i++)
		{
			enemyWeapons.get(i).checkCollision(activeUnits);
			enemyWeapons.get(i).checkTowerCollision(weapons);
		}
	}
	public void updateEnemyPath(int[][] graph)
	{
		for(int i = 0; i<enemies.size();i++)
		{
			Unit temp = enemies.get(i);
			temp.setNodeGraph(graph);
			temp.findPath();
		}
	}

	public void updateMyPath(int[][] graph)
	{
		for(int i = 0; i<myUnits.size();i++)
		{
			Unit temp = myUnits.get(i);
			temp.setNodeGraph(graph);
			temp.findPath();
		}
		for(int i = 0; i<activeUnits.size();i++)
		{
			Unit temp = activeUnits.get(i);
			temp.setNodeGraph(graph);
			temp.findPath();
		}
	}

	//ANIMATION HERE PLEASE LOOK HERE
	public void checkWaves(int gameTime,int myBase, int enemBase, int[][] node, int count)
	{
		for(int i = 0;i<waves.size();i++)
		{
			Wave tempWave = waves.get(i);
			if(gameTime==tempWave.DEPLOY_TIME&&!tempWave.deployed)
			{
				tempWave.deploy();
			}
			if(tempWave.deployed&&tempWave.SIZE>0&&count-tempWave.lastDeployment>25)
			{
				int x = 281+(enemBase%16)*42;
				int y = 11+(enemBase/16)*42;

				//neeed a way to add all types of enemies, not just scout
				if(tempWave.TYPE=='S')
				{
					enemies.add(new Scout(1,x,y,node,myBase,false));
				}
                else if(tempWave.TYPE=='K')
                {
                    enemies.add(new Kamiko(1,x,y,node,myBase,false));
                }
				else if(tempWave.TYPE=='T')
				{
					enemies.add(new Tank(1,x,y,node,myBase,false));
				}
				tempWave.lastDeployment = count;
				tempWave.SIZE-=1;
			}
		}

	}
//***************************************************************************
	public void deployFriendlyUnits(int num)
	{
		if(myUnits.size()-num>0)
			deployNum += num;
		else
			deployNum += num+(myUnits.size()-deployNum);
	}

	public void checkDeployment(int count)
	{
		if(deployNum>0&&count-myLastDeployment>25)
		{
			if(myUnits.size()>=1)
			{
				Unit temp = myUnits.remove(0);
				temp.setOffset(offX,offY);
				activeUnits.add(temp);
				myLastDeployment = count;
				deployNum-=1;
				offX+=10;
				offY-=10;

				if(offX>20)
				{
					offX = 0;
				}
				if(offY<0)
				{
					offY = 20;
				}
			}
		}
	}

	public void scanForUnits()
	{
	    //Friendly tower targeting enemy units
		for(int u = 0;u<enemies.size();u++)
		{
			Unit tempEnemy = enemies.get(u);
			for(int i = 0;i<weapons.size();i++)
			{
				(weapons.get(i)).scan(tempEnemy);
				(weapons.get(i)).updateEnemies(enemies);
			}
			for(int i = 0;i<aoe_tows.size();i++)
			{
				(aoe_tows.get(i)).scan(tempEnemy);
				(aoe_tows.get(i)).updateEnemies(enemies);
			}
		}
		
		//Enemy tower targeting friendly units
		for(int u = 0;u<activeUnits.size();u++)
		{
			Unit tempEnemy = activeUnits.get(u);
			for(int i = 0;i<enemyWeapons.size();i++)
			{
				(enemyWeapons.get(i)).scan(tempEnemy);
			}
			for(int i = 0;i<enemy_aoe.size();i++)
			{
				(enemy_aoe.get(i)).scan(tempEnemy);
			}
		}
		
		//Enemy Towers targeting friendly towers positioned too close/in range
		for(int u = 0;u<weapons.size();u++)
		{
			Weapon tempEnemy = weapons.get(u);
			for(int i = 0;i<enemyWeapons.size();i++)
			{
				(enemyWeapons.get(i)).scan(tempEnemy);
			}
		}
		
		//Friendly kamikazee units scanning how many enemies are close by
        for(int u = 0;u<enemies.size();u++)
        {
            Unit tempEnemy = enemies.get(u);
            for(int i = 0;i<activeUnits.size();i++)
            {
                if(activeUnits.get(i).ID == 'K')
                {
                    ((Kamiko)activeUnits.get(i)).scan(tempEnemy);
                    ((Kamiko)activeUnits.get(i)).updateEnemies(enemies);
                }
            }
        }
	}

	public void checkUnits()
	{
		for(int u = 0;u<enemies.size();u++)
		{
			Unit tempEnemy = enemies.get(u);
			if(!tempEnemy.checkAlive())
			{
				enemies.remove(tempEnemy);
				if(tempEnemy.pathComplete)
				{
					currentGame.damagePlayerBase(tempEnemy.getBaseDmg());
					//System.out.println("WE IN THERE BOI");
				}
			}
		}
		for(int u = 0;u<activeUnits.size();u++)
		{
			Unit tempUnit = activeUnits.get(u);
			if(!tempUnit.checkAlive())
			{
				activeUnits.remove(tempUnit);
				if(tempUnit.pathComplete)
				{
					currentGame.damageEnemyBase(tempUnit.getBaseDmg());
					//System.out.println("WE IN THERE BOI");
				}
			}
		}
		for(int u = 0;u<weapons.size();u++)
		{
			Weapon tempUnit = weapons.get(u);
			if(!tempUnit.checkAlive())
			{
				weapons.remove(tempUnit);
				currentGame.setTile(tempUnit);
				//System.out.println(tempUnit.checkAlive());
			}
		}
	}


	public void tickEnemies()
	{
		for(int i = 0; i<enemies.size();i++)
		{
			if(pathArray.size()==0)
				break;
			Unit temp = enemies.get(i);
			try{
			if(temp.pathComplete)
			{
				enemies.remove(temp);
			}
			temp.move();}catch(Exception e){e.printStackTrace();}

			/*if(temp.isDeployed())
			{
				temp.move();
			}
			else
			{
				'check the unit in front of this unit to see whether or not it should be deployed'
			}*/

		}

		for(int i = 0; i<activeUnits.size();i++)
		{
			if(pathArray.size()==0)
				break;
			Unit temp = activeUnits.get(i);
			//try{
			if(temp.pathComplete)
			{
				activeUnits.remove(temp);
				currentGame.damageEnemyBase(temp.getBaseDmg());
			}
			temp.move();//}catch(Exception e){}
		}
			/*
			String pPoint = temp.curPathPoint;
			////System.out.println("path point: "+temp.curPathPoint+" "+temp.pointIndex);
			if(pPoint.equals(""))
			{
				temp.setPathPoint(pathArray.get(0),0);
			}
			else if(pPoint.equals(pathArray.get(temp.pointIndex)))
			{
				String[] nextPoint = (pathArray.get(temp.pointIndex)).split("[ ]");
				String[] prevPoint = pPoint.split("[ ]");

				int nextX = (offsetX+bw*(Integer.parseInt(nextPoint[1])))+10;
				int nextY = (offsetY+bw*(Integer.parseInt(nextPoint[0])))+10;
				////System.out.println(temp.x+" "+nextX+","+temp.y+" "+nextY);

				if(temp.x==nextX&&temp.y==nextY)
				{
					if(temp.pointIndex+1<pathArray.size())
						temp.setPathPoint(pathArray.get(temp.pointIndex+1),temp.pointIndex+1);
				}

				if(temp.x>nextX)
				{
					temp.tickX(-1);
				}
				else if(temp.x<nextX)
				{
					temp.tickX(1);
				}

				if(temp.y>nextY)
				{
					temp.tickY(-1);
				}
				else if(temp.y<nextY)
				{
					temp.tickY(1);
				}
			}
		}*/
	}

	public void deployUnits(Wave wav, ArrayList<Factory> fac)
	{
		//fac.get(0).deploy(wav.SIZE);
	}
	public void coordinateTroops(String fileName)
	{
		try
		{
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			String str = "";
			ArrayList<String> names = new ArrayList<>();
				while(!(str = br.readLine()).equals("END"))
				{
					//System.out.println(str);
					if(!names.contains(str))
					{
						while(!(str = br.readLine()).equals(""))
						{
							String[] wave = str.split("[ ]");
							char unitType = wave[0].charAt(0);
							int unitLevel = Integer.parseInt(wave[1]);
							int unitNumber = Integer.parseInt(wave[2]);
							int unitTime = Integer.parseInt(wave[3]);

							waves.add(new Wave(unitType,unitLevel,unitNumber,unitTime));
						}
					}

				}
		}catch(IOException e){}
	}
}