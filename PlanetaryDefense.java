import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.applet.*;
import java.util.*;
import java.io.*;
import java.awt.image.*;


//The enemy targeting error occurs when the enemy is target by a weapon and then crashes into the base
public class PlanetaryDefense
extends Applet
implements MouseListener, Runnable, MouseMotionListener
{
	CustomWindow window;
	Controller c;
	GameMap map;
	Thread th;
	int bw = 42;
	int counter = 0;
	int hoverX = 0, hoverY = 0;
	TowerMenu myMenu;
	GameTimer timer;
	Color userColor;
	String levelFile;
	int deployCounter = 0;
	Bank market;
	ArrayList<MenButton> buttons = new ArrayList<>();
	boolean pressed = false;
	int myHP;
	int enemHP;

	public PlanetaryDefense(String lev)
	{
		levelFile = lev;
	}

	public void init()
	{
		market = new Bank(100, this);
		c = new Controller(this);
		map = new GameMap(this);
		loadLevel(levelFile);
		userColor = map.getUserColor();
		myMenu = new TowerMenu(userColor, this);

		setSize(750,1000);
		addMouseListener(this);
		addMouseMotionListener(this);
		window = new CustomWindow(750,1000,this);
		window.center();


		map.createNeighbors();
		map.findPath();
		map.findFriendlyPath();
		myHP = map.getPlayerHP();
		enemHP = map.getEnemyHP();
		timer = new GameTimer();

		c.updatePath(map.getPathArray());
		//c.addEnemy(new Scout(1,100,0,375,525,map.getNodeGraph(),map.getFriendlyBase(),false));

		th = new Thread(this);
		th.start();
		timer.start();
	}

	public void update(Graphics g) {
		Image im = null;
		Graphics dubG = null;
	    if (im == null)
	    {
	        im = createImage(this.getWidth(), this.getHeight());
	        dubG = im.getGraphics();
	    }
	    paint(dubG);
	    g.drawImage(im, 0, 0, this);
	}

	public void paint(Graphics g)
	{
		Color col = g.getColor();
		g.setColor(col);
		//g.setColor(new Color(128,128,128));
		//g.fillRect(281,11,this.getWidth()-322,this.getHeight()-49);
		map.renderMap(g);
		g.drawRect(281,11,this.getWidth()-322,this.getHeight()-49);
		//map.renderPath(g);
		try{
			c.render(g);
		}catch(NullPointerException e){}

		if(hoverX>=281&&hoverY>=11)
			g.drawRect(hoverX, hoverY, 42,42);

		myMenu.render(g);
			try{timer.render(g);}catch(NullPointerException e){}

		g.drawString("Units: "+c.getUnitCount(),50,200);

		g.drawString("$"+market.getBalance(),50,250);

		renderButtons(g);

		renderHealthBars(g);

	}

	public void renderButtons(Graphics g)
	{
		g.setColor(Color.BLUE);
		int bx = 50, by = 300, bw = 100, bh = 40;
		g.fillRect(bx, by, bw, bh);
		buttons.add(new MenButton(this,bx, by, bw, bh, "Deploy"));
	}

	public void renderHealthBars(Graphics g)
	{
		Color c = new Color(127,127,127,127);
		g.setColor(c);
		g.fillRect(30,400,200,10);

		c = new Color(0,0,255);
		g.setColor(c);
		g.fillRect(30,400,map.getPlayerHP()*2,10);

		c = new Color(127,127,127,127);
		g.setColor(c);
		g.fillRect(30,430,200,10);

		c = new Color(255,0,0);
		g.setColor(c);
		g.fillRect(30,430,map.getEnemyHP()*2,10);
	}
	public boolean checkEnviroCollision()
	{
		return true;
	}

	public void damagePlayerBase(int dmg)
	{
		map.damagePlayerBase(dmg);
	}

	public void damageEnemyBase(int dmg)
	{
		map.damageEnemyBase(dmg);
	}

	//Takes the action string from a button and executes command
	public void completeTask(String com)
	{
		if(com.equals("Deploy"))
		{
			c.deployFriendlyUnits(5);
		}
	}

	public void updateMap()
	{
		map.createNeighbors();
		map.findPath();
		map.findFriendlyPath();
	}

	public void addMoney(int mon)
	{
		market.deposit(mon);
	}
	public void run()
	{
		while(true)
		{
			deployCounter++;
			c.findEnemyPath();
			c.tickEnemies();
			c.checkWaves(timer.getSeconds(), map.myBase,map.enemyBase, map.nodeGraph, deployCounter);
			c.scanForUnits();
			c.checkBullets();
			c.checkUnits();
			c.checkDeployment(deployCounter);
			repaint();
			////System.out.println(map.getPlayerHP());
			////System.out.println(deployCounter);

			try
			{
				Thread.sleep(17);
			}catch(InterruptedException e){}
		}
	}

	public void loadLevel(String levelFile)
	{
		try{
			FileReader fr = new FileReader(levelFile);
			BufferedReader br = new BufferedReader(fr);
			String str = "";
				while((str = br.readLine()) != null)
				{
					String[] line = str.split("[-]");
					if(line[0].equals("Map"))
					{
						map.loadMapFile(line[1]);
					}
					else if(line[0].equals("Setup"))
					{
						c.coordinateTroops(line[1]);
					}
				}
			br.close();
		}catch(IOException e){}
	//	map.loadMapFile
	}

	public void addEnemyTower(Tower t)
	{
		try{
		c.addEnemyTower(t);}catch(NullPointerException e){}
	}

	public void addTower(Tower t)
	{
		try{
		c.addTower(t);}catch(NullPointerException e){}
	}

	public void setTile(Tower t)
	{
		map.removeTower(t.x,t.y);
	}

	public int getEnemyBase()
	{
		return map.enemyBase;
	}

	public int getMyBase()
	{
		return map.myBase;
	}

	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseClicked(MouseEvent e){}
	public void mouseReleased(MouseEvent e)
	{
		pressed = false;
	}
	public void mousePressed(MouseEvent e)
	{
		if(SwingUtilities.isLeftMouseButton(e))
		{
			////System.out.println(e.getPoint());
			int mouseX = e.getX();
			int mouseY = e.getY();

			boolean friendly = map.checkFriendly(mouseX, mouseY);

				String towerDetails = "";
				if(myMenu.hidden&&mouseX>281&&mouseY>11)
				{
					boolean isOpen = map.checkOpen(mouseX, mouseY);
					boolean hasRock = map.checkRock(mouseX, mouseY);
					boolean hasBase = map.checkBase(mouseX, mouseY);
					boolean canPlace = map.checkPlace(mouseX, mouseY);
					boolean hasEnemy = c.hasEnemy(mouseX, mouseY);
					boolean onPath = map.checkOnPath(mouseX, mouseY);
					boolean onMyPath = map.checkOnMyPath(mouseX, mouseY);
					towerDetails = myMenu.click(mouseX,mouseY,true, friendly);

					////System.out.println(canPlace);
					if(!hasRock&&!hasBase)
					{
						myMenu.updateCoord(GameMap.iFromY(mouseY)*42+11,GameMap.jFromX(mouseX)*42+281);
						myMenu.show('l', isOpen, map.getTower(mouseX, mouseY));
					}
				}
				else if(!myMenu.hidden)
				{
					towerDetails = myMenu.click(mouseX,mouseY,true, friendly);

					if(!towerDetails.equals("null")&&!towerDetails.substring(0,4).equals("sell"))
					{
						String[] deets = towerDetails.split("[ ]");
						int tx = Integer.parseInt(deets[2]);
						int ty = Integer.parseInt(deets[3]);
						boolean isOpen = map.checkOpen(tx, ty);
						boolean hasBase = map.checkBase(tx, ty);
						boolean canPlace = map.checkPlace(tx, ty);
						boolean hasEnemy = c.hasEnemy(tx, ty);
						boolean onPath = map.checkOnPath(tx,ty);
						boolean onMyPath = map.checkOnMyPath(tx, ty);

						if(isOpen&&!hasBase&&canPlace)
						{
							if(deets[0].charAt(0)=='F')
							{
								Factory fac = null;
								if(deets[1].charAt(0)=='S')
								{
									fac = new ScoutFactory(tx,ty,GameMap.iFromY(ty),GameMap.jFromX(tx),true,map.nodeGraph,map.myBase,map.enemyBase);
								}

								if(deets[1].charAt(0)=='T')
								{
									fac = new TankFactory(tx,ty,GameMap.iFromY(ty),GameMap.jFromX(tx),true,map.nodeGraph,map.myBase,map.enemyBase);
								}
                                if(deets[1].charAt(0)=='K')
                                {
                                    fac = new KamikoFactory(tx,ty,GameMap.iFromY(ty),GameMap.jFromX(tx),true,map.nodeGraph,map.myBase,map.enemyBase);
                                }
								if(fac!=null&&market.canBuy(fac))
								{
									buildTower(fac,tx,ty,onPath, onMyPath);
								}
							}
							if(deets[0].charAt(0)=='W')
							{
								if(deets[1].charAt(0)=='T')
								{
									Turret tur = new Turret(1,tx,ty,GameMap.iFromY(ty),GameMap.jFromX(tx),true, this);
									if(market.canBuy(tur))
									{
										buildTower(tur,tx,ty,onPath, onMyPath);
									}
								}
								if(deets[1].charAt(0)=='C')
								{
									Canon tur = new Canon(1,tx,ty,GameMap.iFromY(ty),GameMap.jFromX(tx),true, this);
									if(market.canBuy(tur))
									{
										buildTower(tur,tx,ty,onPath, onMyPath);
									}
								}
							}
						}
					}
					else if(towerDetails.substring(0,4).equals("sell"))
					{
						String[] deets = towerDetails.split("[ ]");
						int tx = Integer.parseInt(deets[2]);
						int ty = Integer.parseInt(deets[3]);
						sellTower(map.getTower(tx,ty),tx,ty);
					}
				}
			else if(mouseX<281&&!pressed)
			{
				pressed = true;
				for(int i = 0;i<buttons.size();i++)
				{
					MenButton temp = buttons.get(i);
					if(temp.isPressed(mouseX, mouseY))
					{
						completeTask(temp.getAction());
						break;
					}
				}
			}
		}
		if(SwingUtilities.isRightMouseButton(e))
		{
			////System.out.println(e.getPoint());
			int mouseX = e.getX();
			int mouseY = e.getY();

			if(mouseX>281&&mouseY>11)
			{
				boolean isOpen = map.checkOpen(mouseX, mouseY);
				boolean hasRock = map.checkRock(mouseX, mouseY);
				boolean hasBase = map.checkBase(mouseX, mouseY);
				String towerDetails = "";
				boolean canPlace = map.checkPlace(mouseX, mouseY);
				boolean hasEnemy = c.hasEnemy(mouseX, mouseY);


				////System.out.println(canPlace);
				if(myMenu.hidden&&!hasRock&&!hasBase)
				{
					myMenu.updateCoord(GameMap.iFromY(mouseY)*42+11,GameMap.jFromX(mouseX)*42+281);
					myMenu.show('r', isOpen, map.getTower(mouseX, mouseY));
				}
			}
		}


		/*if(canPlace&&!hasEnemy)
		{
			Rock tempRock = map.setRock(mouseX,mouseY);
			if(tempRock!=null)
			{
				int[][] graf = map.updatePath(tempRock,mouseX,mouseY);
				c.updateEnemyPath(graf);
			}
		}*/
	}

	public void sellTower(Tower t, int px, int py)
	{
		if(t.FRIENDLY)
		{
			market.sell(t);
			c.removeTower(t);
			map.removeTower(px, py);
			//int[][] graf = map.updatePath(px,py);
			c.updateEnemyPath(map.nodeGraph);
			c.updateMyPath(map.nodeGraph);
		}
	}
	public void buildTower(Tower t, int px, int py, boolean path, boolean myPath)
	{
		market.buy(t);
		c.addTower(t);
		map.setObject(t,px,py);
		int[][] graf = map.updatePath(t,px,py);
		if(path)
			c.updateEnemyPath(graf);
		if(myPath)
			c.updateMyPath(graf);
	}
	public static void main(String[] args)
	{
		(new PlanetaryDefense("LevelOne.txt")).init();
	}

	public ArrayList<Unit> getEnemyUnits()
	{
		return c.enemies;
	}

	public ArrayList<Unit> getFriendlyUnits()
	{
		return c.myUnits;
	}

	public void mouseMoved(MouseEvent e)
	{
		int mouseX = e.getX();
		int mouseY = e.getY();
		if(myMenu.hidden)
		{
			hoverX = GameMap.jFromX(mouseX)*42+281;
			hoverY = GameMap.iFromY(mouseY)*42+11;
		}

		if(!myMenu.hidden)
		{
			myMenu.checkMouse(mouseX,mouseY);
		}
	}
	public void mouseDragged(MouseEvent e)
	{

	}

	/*issue with whether or not 'enemyBase' should be relative to the object or always refer to the user's enemy
	make one units array list
	better establish factories and how they produce ships
	establish economy
	For animating the units leaving the base, remove them from the units array list and add them to an
		independent render array list that ticks based off the clock and not as fast as it can
	*/
}
