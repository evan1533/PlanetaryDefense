import java.awt.*;
import java.io.*;
import java.util.*;

public class GameMap
{
	MapObject[][] map = new MapObject[16][16];
	String[] stringPath = {};
	String[] stringFriendlyPath = {};
	int[][] nodeGraph = new int[256][256];
	ArrayList<String> pathArray = new ArrayList<>();
	ArrayList<String> friendlyPathArray = new ArrayList<>();
	Color userCol;
	PlanetaryDefense myGame;
	PlanetaryMapEditor myEdit;
	int source = -1, end = -1;
	static final int width = 16;
	static final int height = 16;
	static final int mapX = 281;
	static final int mapY = 11;
	static final int bw = 42;
	int myBase;
	int enemyBase;

	Base playerBase;
	Base badBase;

	//input level in constructor and get Map from level txt file
	public GameMap(PlanetaryDefense gam)
	{
		for(int i = 0; i<height;i++)
		{
			for(int j = 0;j<width;j++)
			{
				map[i][j] = new Tile(281+(j*14),11+(i*14),i,j);
			}
		}
		this.myGame = gam;
	}

	public GameMap()
	{
		for(int i = 0; i<height;i++)
		{
			for(int j = 0;j<width;j++)
			{
				map[i][j] = new Tile(281+(j*14),11+(i*14),i,j);
			}
		}
	}

	public MapObject[][] getMap()
	{
		return this.map;
	}

	public int getPlayerHP()
	{
		return playerBase.HP;
	}

	public int getEnemyHP()
	{
		return badBase.HP;
	}

	public void damagePlayerBase(int dmg)
	{
		playerBase.hit(dmg);
	}

	public void damageEnemyBase(int dmg)
	{
		badBase.hit(dmg);
	}

	public void renderMap(Graphics g)
	{
		for(int i = 0; i<height;i++)
		{
			for(int j = 0;j<width;j++)
			{
				map[i][j].render(g);
				//if(map[i][j]
			}
		}
	}

	public void renderPath(Graphics g)
	{
		Color c = g.getColor();
		g.setColor(Color.GREEN);
		if(pathArray.size()>0)
		{
			for(int k = 0;k<pathArray.size();k++)
			{
				String elem = pathArray.get(k);
				String[] coords = elem.split("[ ]");
				int tempI = Integer.parseInt(coords[0]);
				int tempJ = Integer.parseInt(coords[1]);
				int tempX = 281+(bw*tempJ);
				int tempY = 11+(bw*tempI);
				g.fillRect(tempX+10, tempY+10, 21, 21);
			}
		}
		g.setColor(c);
	}
	public Rock setRock(int mx, int my)
	{
		Rock tempRock;
		if(mx<281||my<11)
			return null;
		int rockJ = jFromX(mx);
		int rockI = iFromY(my);

		if(map[rockI][rockJ].TYPE != 1)
		{
			tempRock = new Rock(281+(rockJ*bw),11+(rockI*bw),rockI,rockJ);
			map[rockI][rockJ] = tempRock;
			return tempRock;
		}
		else
		{
			//map[rockI][rockJ] = new Tile(281+(rockJ*bw),11+(rockI*bw),rockI,rockJ);
			return null;
		}


	}

	public void setObject(MapObject obj, int mx, int my)
	{
		if(mx<281||my<11)
			return;
		int rockJ = jFromX(mx);
		int rockI = iFromY(my);

		if(map[rockI][rockJ].TYPE == 0)
		{
			map[rockI][rockJ] = obj;
		}
	}

	public void removeObject(int mx, int my)
	{
		if(mx<281||my<11)
			return;
		int rockJ = jFromX(mx);
		int rockI = iFromY(my);

		if(map[rockI][rockJ].TYPE >= 0)
		{
			map[rockI][rockJ] = new Tile(281+42*rockJ,11+42*rockI,rockI,rockJ);
		}
	}

	public void removeTower(int mx, int my)
	{
		int rockJ = jFromX(mx);
		int rockI = iFromY(my);
		map[rockI][rockJ] = new Tile(281+42*rockJ,11+42*rockI,rockI,rockJ);
		createNeighbors();
		findPath();
		findFriendlyPath();
	}
	public boolean checkOnPath(int px, int py)
	{
		return pathArray.contains(iFromY(py)+" "+jFromX(px));
	}

	public boolean checkFriendly(int mx, int my)
	{
		if(mx<281||my<11)
		{
			return false;
		}
		int tempJ = ((mx-mapX)/42);
		int tempI = ((my-mapY)/42);

		if(map[tempI][tempJ].TYPE == 3)
		{
			Tower temp = (Tower)(map[tempI][tempJ]);
			System.out.println(temp.FRIENDLY);
			return temp.FRIENDLY;
		}
		return false;
	}
	public boolean checkOnMyPath(int px, int py)
	{
		return friendlyPathArray.contains(iFromY(py)+" "+jFromX(px));
	}
	public boolean checkBase(int mx, int my)
	{
		int tempJ = ((mx-mapX)/42);
		int tempI = ((my-mapY)/42);

		int linearClick = MapObject.getLinearCoord(tempI,tempJ);
		if(linearClick==source||linearClick==end)
		{
			return true;
		}
		else return false;
	}
	public int getFriendlyBase()
	{
		return source;
	}
	public boolean checkPlace(int mx, int my)
	{
		int tempJ = ((mx-mapX)/42);
		int tempI = ((my-mapY)/42);

		if(map[tempI][tempJ].getType()!=0)
		{
			return false;
		}
		int[][] copyNode = new int[256][256];
		int linearClick = MapObject.getLinearCoord(tempI,tempJ);
		for(int i = 0;i<256;i++)
			for(int j = 0; j < 256; j++)
				copyNode[i][j] = nodeGraph[i][j];
		if(linearClick==source||linearClick==end)
		{
			return false;
		}
		if(!pathArray.contains(tempI+" "+tempJ)||!friendlyPathArray.contains(tempI+" "+tempJ))
		{
			return true;
		}
		else
		{
			int lin = MapObject.getLinearCoord(tempI, tempJ);
			for(int i = 0;i<256;i++)
			{
				copyNode[lin][i] = 999;
				copyNode[i][lin] = 999;
			}
			BFS.init(copyNode,end);
			String path = BFS.getPath(source, end);
			if(path.equals("null"))
			{
				//System.out.println("Impossible");
				return false;
			}
			else
			{
				return true;
			}
		}
	}

	public Color getUserColor()
	{
		return userCol;
	}

	public boolean checkRock(int mx, int my)
	{
		int tempJ = ((mx-mapX)/42);
		int tempI = ((my-mapY)/42);

		if(map[tempI][tempJ].getType()==1)
		{
			return true;
		}
		else
			return false;
	}

	public boolean checkOpen(int mx, int my)
	{
		int tempJ = ((mx-mapX)/42);
		int tempI = ((my-mapY)/42);


		if(map[tempI][tempJ].getType()==0)
		{
			return true;
		}
		else
			return false;
	}

	public static int jFromX(int px)
	{
		for(int j = 0; j<width;j++)
		{
			////System.out.println("X "+j);
			if(px>=281+(j*bw)&&px<281+((j+1)*bw))
			{
				return j;
			}
		}
		return -1;
	}

	public static int iFromY(int py)
	{
		for(int j = 0; j<width;j++)
		{
			//347
			////System.out.println("Y "+j);
			if(py>=11+(j*bw)&&py<11+((j+1)*bw))
			{
				return j;
			}
		}
		return -1;
	}

	public void setComponentBase(int pi, int pj, Boolean friend)
	{
		Color c;
		Base temp;
		if(friend)
		{
			c = new Color(0,0,255);
			playerBase = new Base(281+(pj*bw),11+(pi*bw), pi, pj, c,true);
			temp = playerBase;
		}
		else
		{
			c = new Color(255,0,0);
			badBase = new Base(281+(pj*bw),11+(pi*bw), pi, pj, c,true);
			temp = badBase;
		}

		map[pi][pj] = temp;

	}

	public void setComponentRock(int pi, int pj)
	{
		map[pi][pj] = new Rock(281+(pj*bw),11+(pi*bw), pi, pj);
	}

	public void setEnemyTower(int pi, int pj, char typ1, char typ2)
	{
		Tower temp = null;
		if(typ1 == 'F')
		{
			if(typ2 == 'S')
			{
				//System.out.println("ASDF");
				temp = new ScoutFactory(281+(pj*bw),11+(pi*bw), pi, pj, false, nodeGraph, enemyBase, myBase);
				//addEnemyFactory(tempFactory);
				map[pi][pj] = temp;
			}
			if(typ2 == 'T')
			{
				//System.out.println("ASDF");
				temp = new TankFactory(281+(pj*bw),11+(pi*bw), pi, pj, false, nodeGraph, enemyBase, myBase);
				//addEnemyFactory(tempFactory);
				map[pi][pj] = temp;
			}
			if(typ2 == 'K')
			{
				//System.out.println("ASDF");
				temp = new KamikoFactory(281+(pj*bw),11+(pi*bw), pi, pj, false, nodeGraph, enemyBase, myBase);
				//addEnemyFactory(tempFactory);
				map[pi][pj] = temp;
			}
		}
		if(typ1 == 'W')
		{
			if(typ2 == 'T')
			{
				//System.out.println("ASDF");
				temp = new Turret(1,281+(pj*bw),11+(pi*bw), pi, pj, false, myGame);
				//addEnemyFactory(tempFactory);
				map[pi][pj] = temp;
			}
			if(typ2 == 'C')
			{
				//System.out.println("ASDF");
				temp = new Canon(1,281+(pj*bw),11+(pi*bw), pi, pj, false, myGame);
				//addEnemyFactory(tempFactory);
				map[pi][pj] = temp;
			}
		}
		//System.out.println(temp.SUBCLASS);
		try{myGame.addEnemyTower(temp);}catch(NullPointerException e){}
	}

	public void setTower(int pi, int pj, char typ1, char typ2)
	{
		Tower temp = null;
		if(typ1 == 'F')
		{
			if(typ2 == 'S')
			{
				//System.out.println("ASDF");
				temp = new ScoutFactory(281+(pj*bw),11+(pi*bw), pi, pj, true, nodeGraph, myBase, enemyBase);
				//addEnemyFactory(tempFactory);
				map[pi][pj] = temp;
			}
			if(typ2 == 'T')
			{
				//System.out.println("ASDF");
				temp = new TankFactory(281+(pj*bw),11+(pi*bw), pi, pj, true, nodeGraph, myBase, enemyBase);
				//addEnemyFactory(tempFactory);
				map[pi][pj] = temp;
			}
		}
		//System.out.println(temp.SUBCLASS);
			myGame.addTower(temp);
	}

	public Tower getTower(int px, int py)
	{
		int tpi = iFromY(py);
		int tpj = jFromX(px);

		MapObject temp = getObject(tpi,tpj);
		if(temp.TYPE == 3)
		{
			return (Tower)temp;
		}
		return null;
	}

	public void createNeighbors()
	{
		for(int i = 0;i<map.length;i++)
		{
			for(int j = 0;j<map[i].length;j++)
			{
				map[i][j].setNeighbors(map);
			}
		}
		for(int i = 0;i<map.length;i++)
		{
			for(int j = 0;j<map[i].length;j++)
			{
				map[i][j].setNodes(nodeGraph);
			}
		}
	}

	public int[][] updatePath(MapObject obj, int mx, int my)
	{
		int tempJ = ((mx-mapX)/42);
		int tempI = ((my-mapY)/42);

		int lin = MapObject.getLinearCoord(tempI, tempJ);
		for(int i = 0;i<256;i++)
		{
			nodeGraph[lin][i] = 999;
			nodeGraph[i][lin] = 999;
		}
		findPath();
		findFriendlyPath();

		return nodeGraph;
	}

	public int[][] updatePath(int mx, int my)
	{
		int tempJ = ((mx-mapX)/42);
		int tempI = ((my-mapY)/42);

		int lin = MapObject.getLinearCoord(tempI, tempJ);
		for(int i = 0;i<256;i++)
		{
			nodeGraph[lin][i] = 1;
			nodeGraph[i][lin] = 1;
		}
		findPath();
		findFriendlyPath();

		return nodeGraph;
	}

	public int[][] getNodeGraph()
	{
		return nodeGraph;
	}

	public void findPath()
	{
		//System.out.println(end);
		BFS.init(nodeGraph,end);
		//System.out.println(source+" "+end);
		String path = BFS.getPath(source, end);
		pathArray.clear();
		//System.out.println("soss: "+source);
		if(path.equals("null"))
		{
			//System.out.println("Impossible");
		}
		else
		{
			stringPath = path.split("[-]");

			for(int i = 0;i < stringPath.length;i++)
			{
				int linCoord = Integer.parseInt(stringPath[i]);
				pathArray.add(0,MapObject.getGridCoord(linCoord));
			}
			//System.out.println(pathArray);
		}
	}

	public void findFriendlyPath()
	{
		//System.out.println(end);
		BFS.init(nodeGraph,source);
		String path = BFS.getPath(end, source);
		friendlyPathArray.clear();
		if(path.equals("null"))
		{
			//System.out.println("Impossible");
		}
		else
		{
			stringFriendlyPath = path.split("[-]");

			for(int i = 0;i < stringFriendlyPath.length;i++)
			{
				int linCoord = Integer.parseInt(stringFriendlyPath[i]);
				friendlyPathArray.add(0,MapObject.getGridCoord(linCoord));
			}
		}
	}

	public MapObject getObject(int pi, int pj)
	{
		return map[pi][pj];
	}
	public ArrayList<String> getPathArray()
	{
		return pathArray;
	}
	public ArrayList<String> getFriendlyPath()
	{
		return friendlyPathArray;
	}

	public void writeMapFile(String fileName, boolean append)
	{
	    /* Order to write:
	     * ---------------
	     * Rocks
	     * MyBase
	     * EnemyBase
	     * EnemyTowers
	     */
		String[] headers = {"Rocks","MyBase","EnemyBase","EnemyTower"};
		try{
		BufferedWriter pw = new BufferedWriter(new FileWriter(fileName+".txt",append));
		for(int q = 0;q<headers.length;q++)
		{
			pw.newLine();
			pw.append(headers[q]);
			pw.newLine();
			pw.flush();
			for(int i = 0; i<this.height;i++)
			{
				for(int j = 0;j<this.width;j++)
				{
					MapObject tempObj = this.getObject(i, j);
					
					if(tempObj.TYPE == 1 && q == 0)
					{
						pw.append(i+","+j);
						pw.newLine();
						pw.flush();
					}
					else if(tempObj.TYPE == 2 && (q==1 || q==2))
					{
						if( ((Base)tempObj).friendly && q==1)
						{
							pw.append(i+","+j);
							pw.newLine();
							pw.flush();
						}
						else if( ((Base)tempObj).friendly && q==2)
						{
							pw.append(i+","+j);
							pw.newLine();
							pw.flush();
						}
					}
					else if(tempObj.TYPE == 3 && q==3)
					{
						pw.append(i+","+j+" "+((Tower)tempObj).TOWER_TYPE+" "+((Tower)tempObj).SUBCLASS);
						pw.newLine();
						pw.flush();
					}
				}
			}
		}
		}catch(IOException e){}
	}

	public void writeMapFile(String fileName)
	{
	    /* Order to write:
	     * ---------------
	     * Rocks
	     * MyBase
	     * EnemyBase
	     * EnemyTowers
	     */
		String[] headers = {"Rocks","MyBase","EnemyBase","EnemyTower"};
		try{
		BufferedWriter pw = new BufferedWriter(new FileWriter(fileName+".txt",true));
		for(int q = 0;q<headers.length;q++)
		{
			pw.newLine();
			pw.append(headers[q]);
			pw.newLine();
			pw.flush();
			for(int i = 0; i<this.height;i++)
			{
				for(int j = 0;j<this.width;j++)
				{
					MapObject tempObj = this.getObject(i, j);
					
					if(tempObj.TYPE == 1 && q == 0)
					{
						pw.append(i+","+j);
						pw.newLine();
						pw.flush();
					}
					else if(tempObj.TYPE == 2 && (q==1 || q==2))
					{
						if( ((Base)tempObj).friendly && q==1)
						{
							pw.append(i+","+j);
							pw.newLine();
							pw.flush();
						}
						else if( ((Base)tempObj).friendly && q==2)
						{
							pw.append(i+","+j);
							pw.newLine();
							pw.flush();
						}
					}
					else if(tempObj.TYPE == 3 && q==3)
					{
						pw.append(i+","+j+" "+((Tower)tempObj).TOWER_TYPE+" "+((Tower)tempObj).SUBCLASS);
						pw.newLine();
						pw.flush();
					}
				}
			}
		}
		}catch(IOException e){}
	}

	public void loadMapFile(String fileName)
	{
		try
		{
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			String str = "";
				while((str = br.readLine()) != null)
				{
					if(str.equals("Rocks"))
					{
						while(!(str = br.readLine()).equals(""))
						{
							String[] line = str.split("[,]");
							int tempI = Integer.parseInt(line[0]);
							int tempJ = Integer.parseInt(line[1]);
							this.setComponentRock(tempI,tempJ);
						}
					}
					else if(str.equals("MyBase"))
					{
						str = br.readLine();
						String[] line = str.split("[,]");
						int tempI = Integer.parseInt(line[0]);
						int tempJ = Integer.parseInt(line[1]);
						this.source = MapObject.getLinearCoord(tempI,tempJ);
						myBase = source;
						//System.out.println("soss: "+this.source);
						this.userCol = new Color(0,0,255);
						this.setComponentBase(tempI,tempJ, true);
					}
					else if(str.equals("EnemyBase"))
					{
						str = br.readLine();
						String[] line = str.split("[,]");
						int tempI = Integer.parseInt(line[0]);
						int tempJ = Integer.parseInt(line[1]);
						this.end = MapObject.getLinearCoord(tempI,tempJ);
						enemyBase = end;
						//System.out.println("N: "+this.end);
						this.setComponentBase(tempI,tempJ, false);
					}
					else if(str.equals("EnemyTower"))
					{
						while(!(str = br.readLine()).equals(""))
						{
							//System.out.println("THIS IS THE STRING: "+str);
							String[] line = str.split("[ ]");
							String[] cord = line[0].split("[,]");
							int tempI = Integer.parseInt(cord[0]);
							int tempJ = Integer.parseInt(cord[1]);
							//System.out.println("REAL WOMEN "+tempI+" "+tempJ);
							if(line[1].charAt(0)=='F')
							{
								this.setEnemyTower(tempI,tempJ,line[1].charAt(0),line[2].charAt(0));
							}
							if(line[1].charAt(0)=='W')
							{
								this.setEnemyTower(tempI,tempJ,line[1].charAt(0),line[2].charAt(0));
							}
						}
					}
					else if(str.equals("FriendlyTower"))
					{
						str = br.readLine();
						String[] line = str.split("[ ]");
						String[] cord = line[0].split("[,]");
						int tempI = Integer.parseInt(cord[0]);
						int tempJ = Integer.parseInt(cord[1]);
						if(line[1].charAt(0)=='F')
						{
							this.setTower(tempI,tempJ,line[1].charAt(0),line[2].charAt(0));
						}
					}
					//System.out.println(str);
				}
		}catch(IOException e){}
	}

}