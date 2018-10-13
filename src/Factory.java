import java.util.*;
import java.awt.*;

public abstract class Factory extends Tower
implements Runnable
{
	int[][] nodeGraph;
	int end;
	boolean friendly;
	int myBase;
	int enemBase;
	Thread productionThread;
	int RESOURCES_NEEDED;
	int currentResources = 0;
	int PROD_RATE;
	int SECONDS = 0;
	int productionCount = 0;
	boolean building = false;

	ArrayList<Unit> units = new ArrayList<Unit>();

	public Factory(int px, int py, int pi, int pj, boolean friend,int prodRate, char subclass, int[][] nodeGraf, int myBase, int enemBase, int Resources, int pcost)
	{
		super(px, py, pi, pj, friend,'F',subclass, pcost);
		friendly = friend;
		nodeGraph = nodeGraf;
		this.myBase = myBase;
		this.enemBase = enemBase;
		this.end = enemBase;
		this.RESOURCES_NEEDED = Resources;
		this.PROD_RATE = prodRate;

		if(friendly)
		{
			productionThread = new Thread(this);
			productionThread.start();
			building = true;
		}
	}

	public int getSize()
	{
		return units.size();
	}
/*	public ArrayList<Unit> deploy(int size)
	{
		ArrayList<Unit> temp = new ArrayList<>();
		if(units.size()>=size)
		{
			for(int i = 0;i<size;i++)
			{
				temp.add(units.get(i));
			}
		}

		units.removeAll(temp);
		return temp;
	}*/

	public void setUnitArray(ArrayList<Unit> pUnits)
	{
		units = pUnits;
	}

	abstract public void upgradeFactory();


//***************************************************************************
	public void run()
	{
		while(building)
		{
			////System.out.println(currentResources);
			if(productionCount-SECONDS>PROD_RATE)
			{
				SECONDS = productionCount;
				developUnit();
				/*if(currentResources<RESOURCES_NEEDED)
				{
					currentResources+=(PROD_RATE*1);
				}
				else if(currentResources>=RESOURCES_NEEDED)
				{
					currentResources-=RESOURCES_NEEDED;
					developUnit();
				}*/
			}
			productionCount++;

			try{Thread.sleep(5);}catch(InterruptedException e){}
		}
	}

	public void destroy()
	{
		building = false;
		try{
		productionThread.join();}catch(InterruptedException e){}
	}

	abstract public void developUnit();
	abstract public void developUnit(int num);
	abstract public void render(Graphics g);

}