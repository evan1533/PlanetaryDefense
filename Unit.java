import java.util.*;
import java.awt.*;

public abstract class Unit
{
	int HP;
	double armor;
	double x, y;
	double speed;
	String curPathPoint = "";
	private int pointIndex;
	private ArrayList<String> myPath = new ArrayList<>();
	private int[][] nodeGraph;
	private int end;
	private String[] stringPath;
	BFS bfs;
	boolean hasPath = false;
	boolean pathComplete = false;
	boolean FRIENDLY;
	int LEVEL;
	int width;
	boolean alive;
	//int dx, dy;
	int offsetX, offsetY;
	int baseDamage;
	int height;
	char ID;
	private double storedSpeed = 0;
	private boolean searchX = false;
	private boolean searchY = false;

	public Unit(int pLevel, int pHP, double pArmor, int px, int py, double pSpeed, int[][] nodGraf, int pEnd, boolean pfriendly, int width, int dmg, char id)
	{
		HP = pHP;
		armor = pArmor;
		x = px;
		y = py;
		speed = pSpeed;
		storedSpeed = speed;
		this.nodeGraph = nodGraf;
		this.end = pEnd;
		bfs = new BFS();
		bfs.init(nodeGraph, end);
		FRIENDLY = pfriendly;
		LEVEL = pLevel;
		alive = true;
		this.upgradeUnit();
		this.width = width;
		this.height = width;
		offsetX = (int)(Math.random()*width);
		offsetY = (int)(Math.random()*height);
		baseDamage = dmg;
		this.ID = id;
	}

	public Unit(int pLevel, int pHP, double pArmor, int px, int py, double pSpeed, int[][] nodGraf, int pEnd, boolean pfriendly, int width, int height, int dmg,char id)
	{
		HP = pHP;
		armor = pArmor;
		x = px;
		y = py;
		offsetX = (int)(Math.random()*width);
		offsetY = (int)(Math.random()*height);
		speed = pSpeed;
		this.nodeGraph = nodGraf;
		this.end = pEnd;
		bfs = new BFS();
		bfs.init(nodeGraph, end);
		FRIENDLY = pfriendly;
		LEVEL = pLevel;
		alive = true;
		this.upgradeUnit();
		this.width = width;
		this.height = height;
		baseDamage = dmg;
		this.ID = id;
	}

	public int getBaseDmg()
	{
		return baseDamage;
	}

//	public void setPath(ArrayList<String> path)//
	//{
		//this.myPath = path;
//	}

	abstract public void upgradeUnit();

	public void findPath()
	{
		int tempI = GameMap.iFromY((int)y);
		int tempJ = GameMap.jFromX((int)x);
		int source = MapObject.getLinearCoord(tempI, tempJ);
		////System.out.println("SOSOSOSOSS "+source+" "+this.end+" "+x+" "+y);
		bfs.init(nodeGraph,source);
		String path = bfs.getPath(end, source);
		////System.out.println(source);
		ArrayList<String> tempPath = new ArrayList<String>();
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
				tempPath.add(0,MapObject.getGridCoord(linCoord));
			}
			////System.out.println("ASS "+myPath);
		}
		curPathPoint = "";
		myPath = tempPath;
		hasPath = true;
		//flipPath();
	}
	abstract public void tickX(int dx);
	abstract public void tickY(int dy);
	abstract public void render(Graphics g);
	abstract public int getValue();

	public void setPathPoint(String pathpoint, int ind)
	{
		this.curPathPoint = pathpoint;
		this.pointIndex = ind;
	}

	public void move()
	{

		if(this.curPathPoint.equals(""))
		{
			////System.out.println("PATHS "+myPath);
			try{
			this.setPathPoint(this.myPath.get(1),1);}catch(ArrayIndexOutOfBoundsException e){}
		}
		else if(this.curPathPoint.equals(this.myPath.get(this.pointIndex)))
		{
			String[] nextPoint = (this.myPath.get(this.pointIndex)).split("[ ]");
			String[] prevPoint = this.curPathPoint.split("[ ]");

			
			int nextX = (GameMap.mapX+GameMap.bw*(Integer.parseInt(nextPoint[1])))+offsetX;
			int nextY = (GameMap.mapY+GameMap.bw*(Integer.parseInt(nextPoint[0])))+offsetY;

			////System.out.println(this.x+" "+nextX+","+this.y+" "+nextY);

			if(this.x>=nextX-2&&this.x<=nextX+2&&this.y>=nextY-2&&this.y<=nextY+2)
			{
				if(this.pointIndex+1<myPath.size())
					this.setPathPoint(myPath.get(this.pointIndex+1),this.pointIndex+1);

			}
			
			searchX = !(this.x>=nextX-2&&this.x<=nextX+2);
            searchY = !(this.y>=nextY-2&&this.y<=nextY+2);
            
			if(this.x>nextX&&searchX)
			{
				this.tickX(-1);
				this.faceDir('W');
			}
			else if(this.x<nextX&&searchX)
			{
				this.tickX(1);
				this.faceDir('E');
			}

			if(this.y>nextY&&searchY)
			{
				this.tickY(-1);
				this.faceDir('S');
			}
			else if(this.y<nextY&&searchY)
			{
				this.tickY(1);
				this.faceDir('N');
			}
		}
		if(this.curPathPoint.equals(this.myPath.get(myPath.size()-1)))
		{
			pathComplete = true;
			HP = 0;
			alive = false;
		}
	}

	public void flipPath()
	{
		ArrayList<String> tempPath = new ArrayList<>();
		for(int i = 0;i<myPath.size();i++)
		{
			tempPath.add(0,myPath.get(i));
		}
		myPath = tempPath;
	}

	public void setNodeGraph(int[][] graf)
	{
		nodeGraph = graf;
	}

	public void setOffset(int ox, int oy)
	{
		offsetX = ox;
		offsetY = oy;
	}

	public double getX()
	{
		return this.x;
	}

	public double getY()
	{
		return this.y;
	}


	public void damage(int damage)
	{
		if(armor>0)
		{
			armor-=((double)damage)/2.0;
		}
		else
			HP-=(int)damage;
		if(HP<=0)
		{
			alive = false;
		}
		System.out.println(armor+" "+HP);
	}

	public boolean checkAlive()
	{
		return alive;
	}
	
	public void reduceSpeed()
	{
		storedSpeed = speed;
		speed = speed/2;
	}
	
	public void restoreSpeed()
	{
		speed = storedSpeed;
	}
	
	public boolean checkSpeed()
	{
		return speed == storedSpeed;
	}
	
	public abstract void faceDir(char D);

}