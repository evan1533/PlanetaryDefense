import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

public class TowerMenu
{
	MenuTile[] buildTiles = new MenuTile[6];
	MenuTile[] editTiles = new MenuTile[2];
	boolean hidden = true;
	int x = -11, y = -11;
	int leftX = 0, rightX = 0;
	int topY = 0, bottomY = 0;
	Color menuColor;
	PlanetaryDefense gameInstance;
	boolean hiddenSell = true;
	boolean empty = false;
	boolean friend = false;
	Tower editTower = null;
	Image[] yesOrNo = new Image[2];

	public TowerMenu(Color c, PlanetaryDefense gm)
	{
		gameInstance = gm;
		menuColor = c;

			buildTiles[0] = new MenuTile(-52,-25, 'F', 'S');
			buildTiles[1] = new MenuTile(0,-52, 'W', 'T');
			buildTiles[2] = new MenuTile(52,-25, 'W', 'C');

			buildTiles[3] = new MenuTile(-52,25, 'F', 'T');
			buildTiles[4] = new MenuTile(0,+52, 'F', 'K');
			buildTiles[5] = new MenuTile(52,25, 'F', 'S');

			editTiles[0] = new MenuTile(52,-22, 'N', 'N');
			editTiles[1] = new MenuTile(-52, -22, 'N', 'N');

			yesOrNo[0] = new ImageIcon(this.getClass().getResource("Images/confirm.PNG")).getImage();
			yesOrNo[1] = new ImageIcon(this.getClass().getResource("Images/cancel.PNG")).getImage();
	}

	public void hide()
	{
		hidden = true;
	}

	public void show(char c,boolean pEmpty, Tower t)
	{
		empty = pEmpty;
		editTower = t;
		if(c == 'l')
			hidden = false;
		else if(c == 'r')
			hiddenSell = false;
	}

	public void updateCoord(int pi, int pj)
	{
		x = pj;
		y = pi;
		for(int i = 0;i<buildTiles.length;i++)
		{
			buildTiles[i].update(pj,pi);
		}
		for(int i = 0;i<editTiles.length;i++)
		{
			editTiles[i].update(pj,pi);
		}
	}

	public void checkMouse(int px, int py)
	{
		if(px<leftX||px>rightX||py<topY||py>bottomY)
			hidden = true;
	}

	public String click(int px, int py,boolean click, boolean friendly)
	{
		friend = friendly;
		String tow = "null";
		if(px>=x&&px<=x+42&&py>y&&py<y+42&&!hidden)
		{
			hidden = true;
		}
		if(click)
		{
			if(empty)
			{
				for(int i = 0;i<buildTiles.length;i++)
				{
					if(buildTiles[i].checkClick(px,py))
					{
						tow = buildTiles[i].createTower()+" "+x+" "+y;
					}
				}
			}
			else if(!empty)
			{
				//System.out.println("FULL");
				//Cancel Transaction probably will be update button instead
				if(editTiles[0].checkClick(px,py))
				{
					hidden = true;
					hiddenSell = true;
				}

				//Sell Tower
				else if(editTiles[1].checkClick(px,py))
				{
					hidden = true;
					hiddenSell = true;
					return "sell"+" "+"N"+" "+x+" "+y;
				}
			}
		}

		return tow;
	}

	public void render(Graphics g)
	{
		Color c = g.getColor();

		if(!hidden)
		{
			if(empty)
			{
				renderBuildMenu(g);
			}
			else if(!empty&&friend)
			{
				renderEditMenu(g);
			}
		}
		if(!hiddenSell)
		{
			//g.setColor(new Color(100,100,100,180));
			//g.fillRect(x,y,150,150);
		}

		g.setColor(c);
	}


	public void renderBuildMenu(Graphics g)
	{
		leftX = x-52;
		rightX = x+42+52;
		topY = y-52;
		bottomY = y+42+52;


		if(topY<11)
		{
			bottomY=52+42+52;
			topY=0;
		}

		int r = menuColor.getRed();
		int gr = menuColor.getGreen();
		int b = menuColor.getBlue();

		g.setColor(new Color(100,100,100,180));
		g.fillRect(leftX,topY,150,150);
		g.setColor(new Color(r,gr,b,180));
		for(int i = 0;i<buildTiles.length;i++)
		{
			buildTiles[i].render(g);
		}
	}

	public void renderEditMenu(Graphics g)
	{
		leftX = x-52;
		rightX = x+42+52;
		topY = y-52;
		bottomY = y+42+52;


		//g.setColor(new Color(87,87,87,100));
		//g.fillRect(leftX,topY,150,150);
		g.drawImage(yesOrNo[0],leftX,topY+30,42,42,null);
		g.drawImage(yesOrNo[1],rightX-42,topY+30,42,42,null);

		////System.out.println(editTower);
	}

	class MenuTile
	{
		int width = 42;
		int height = 42;
		int offsetX;
		int offsetY;
		int x, y;
		Tower myTower;
		char towerType;
		char subclass;

		public MenuTile(int px, int py, char type, char type2)
		{
			offsetX = px;
			offsetY = py;
			towerType = type;
			subclass = type2;
		}

		public void update(int px, int py)
		{
			if(py==11)
			{
				x = px+offsetX;
				y = 52+offsetY;
			}
			else
			{
				x = px+offsetX;
				y = py+offsetY;
			}
		}

		public boolean checkClick(int mx, int my)
		{
			if(mx>x&&mx<x+width&&my>y&&my<y+42)
			{
				return true;
			}

			return false;
		}

		public String createTower()
		{
			hidden = true;
			return towerType+" "+subclass;
		}

		public void render(Graphics g)
		{
		//	try{
			if(towerType=='F')
			{
				if(subclass=='S')
				{
					g.drawImage(ScoutFactory.getImage(true),x,y,42,42,null);
				}
				if(subclass=='T')
				{
					g.drawImage(TankFactory.getImage(true),x,y,42,42,null);
				}
				if(subclass=='K')
				{
					g.drawImage(KamikoFactory.getImage(true),x,y,42,42,null);
				}
			}
			if(towerType=='W')
			{
				if(subclass=='T')
				{
					g.drawImage(Turret.getImage(true),x,y,42,42,null);
				}
				if(subclass=='C')
				{
					g.drawImage(Canon.getImage(true),x,y,42,42,null);
				}
			}
			if(towerType=='N')
			{
				if(subclass=='N')
				{
					g.drawImage(Turret.getImage(true),x,y,42,42,null);
				}
			}
			//g.drawImage(myTower.getImage(),x,y,42,42,null);//}catch(NullPointerException e){}
			//g.fillRect(x,y,42,42);
		}

		public String toString()
		{
			return this.x+" "+this.y;
		}
	}
}
