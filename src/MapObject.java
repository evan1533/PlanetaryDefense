import java.awt.*;

public abstract class MapObject
{
	int x, y, width = 42, height = 42, myI, myJ;
	int TYPE;
	int vertIndex = 0;
	String  stringIndex = "";
	int[] verts = new int[256];
	/*TYPE:
	0 - Empty Tile
	1 - Environment/Rock
	2 - Base
	3 - Tower*/

	public MapObject(int typ,int px, int py, int pi, int pj)
	{
		TYPE = typ;
		x = px;
		y = py;
		myI = pi;
		myJ = pj;
		stringIndex = myI+" "+myJ;
		vertIndex = getLinearCoord(myI,myJ);
	}

	abstract public void render(Graphics g);

	public int getType()
	{
		return this.TYPE;
	}


	public void setNeighbors(MapObject[][] map)
	{
		int leftN = getLinearCoord(myI,myJ-1);
		int rightN = getLinearCoord(myI,myJ+1);
		int upN = getLinearCoord(myI-1,myJ);
		int downN = getLinearCoord(myI+1,myJ);
		////System.out.print("LEFT: "+downN+" ");
		for(int i = 0; i < 256; i++)
		{
			verts[i] = 999;
		}
		try{
			//if the space is a rock make the path impossible
			if(map[myI][myJ].getType()==1||map[myI][myJ].getType()==3)
			{
				////System.out.println("NO SIR");
				verts[leftN] = -1;
				verts[rightN] = -1;
				verts[downN] = -1;
				verts[upN] = -1;
			}
			else
			{
				if(vertIndex!=0)
				{
					if(vertIndex%16==0||(map[leftN/16][leftN%16].getType()==1))
						verts[leftN] = 999;
					else
						verts[leftN] = 1;
				}
				if(vertIndex!=255||(map[rightN/16][rightN%16].getType()==1))
				{
					if((vertIndex+1)%16==0)
						verts[rightN] = 999;
					else
						verts[rightN] = 1;
				}

				if(upN>0&&(map[upN/16][upN%16].getType()==0||map[upN/16][upN%16].getType()==2))
					verts[upN] = 1;////System.out.println(map[upN/16][upN%16].getType());
				if(myI<15&&(map[downN/16][downN%16].getType()==0||map[downN/16][downN%16].getType()==2))
					verts[downN] = 1;
			}
			if(map[myI][myJ].getType()==1)
			{
				////System.out.println("NO SIR");
				verts[leftN] = -1;
				verts[rightN] = -1;
				verts[downN] = -1;
				verts[upN] = -1;
			}
		}catch(ArrayIndexOutOfBoundsException e){}//e.printStackTrace();}

		////System.out.print(stringIndex+": ");
		for(int i = 0; i < 256; i++)
		{
			if(verts[i] == 1&&this.TYPE==1)
			{
				verts[i] = -1;
				/////System.out.print(i+" ");
			}
		}
		////System.out.println();
	}

	public static int getLinearCoord(int pi, int pj)
	{
		return pj+16*pi;
	}

	public static String getGridCoord(int linear)
	{
		int tj = linear%16;
		int ti = linear/16;
		return ti+" "+tj;
	}
	public int getVert(int pj)
	{
		return verts[pj];
	}
	public void setNodes(int[][] nodeGraph)
	{
		for(int j = 0;j<256;j++)
		{
			nodeGraph[j][vertIndex] = verts[j];
			//if(verts[j]==1)
			////System.out.println(j+" "+vertIndex+" "+nodeGraph[j][vertIndex]);
		}
	}
}