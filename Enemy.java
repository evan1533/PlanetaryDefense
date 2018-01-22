/*import java.util.*;
import java.awt.*;

public abstract class Enemy
{
	int HP, armor;
	int x, y, speed;
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

	public Enemy(int pHP, int pArmor, int px, int py, int pSpeed, int[][] nodGraf, int pEnd, boolean pfriendly)
	{
		HP = pHP;
		armor = pArmor;
		x = px;
		y = py;
		speed = pSpeed;
		this.nodeGraph = nodGraf;
		this.end = pEnd;
		bfs = new BFS();
		bfs.init(nodeGraph, end);
		FRIENDLY = pfriendly;
	}

//	public void setPath(ArrayList<String> path)//
	//{
		//this.myPath = path;
//	}

	public void findPath()
	{
		int tempI = GameMap.iFromY(y);
		int tempJ = GameMap.jFromX(x);
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

			int nextX = (GameMap.mapX+GameMap.bw*(Integer.parseInt(nextPoint[1])))+10;
			int nextY = (GameMap.mapY+GameMap.bw*(Integer.parseInt(nextPoint[0])))+10;
			////System.out.println(this.x+" "+nextX+","+this.y+" "+nextY);

			if(this.x==nextX&&this.y==nextY)
			{
				if(this.pointIndex+1<myPath.size())
					this.setPathPoint(myPath.get(this.pointIndex+1),this.pointIndex+1);
			}

			if(this.x>nextX)
			{
				this.tickX(-1);
			}
			else if(this.x<nextX)
			{
				this.tickX(1);
			}

			if(this.y>nextY)
			{
				this.tickY(-1);
			}
			else if(this.y<nextY)
			{
				this.tickY(1);
			}
		}
		if(this.curPathPoint.equals(this.myPath.get(myPath.size()-1)))
		{
			pathComplete = true;
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



}*/