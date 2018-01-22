import java.util.*;

public class BFS
{
	static int[] vert;
	static int[] par;
	static int[][] graph;
	static final int INF = 999;

	public static void init(int[][] gr, int end)
	{
		graph = gr;
		vert = new int[graph.length];
		par = new int[graph.length];
		for(int i = 0;i<vert.length;i++)
		{
			vert[i] = INF;
			par[i] = -1;
		}
		////System.out.println("END "+ end);
		vert[end]=0;
		BFS(graph, vert, par, end);
	}

	public static String getPath(int source, int end)
	{
		int parent = source;
		String path = (source)+"";
		int sum = vert[parent];

		if(sum==1000)
		{
			return "null";
		}
		while(parent!=end)
		{
			//path+="-";
			parent = par[parent];
			path=path+"-"+parent;
			//sum+=v[parent];
		}

		////System.out.println(vert[end]+" "+par[end]);
		////System.out.println(path);
		return path;
	}

	public static void BFS(int[][] graph, int[] v, int[] p,int end)
	{
		int[] verts = v;
		int[] parents = p;
        MyQueue Q = new MyQueue();
        Q.enqueue(end);
        Integer current;
    	while(!Q.isEmpty())
    	{
        	current = Q.dequeue();
        	////System.out.println(current+" "+Q.isEmpty());

        	for(int j = 0;j < graph[current].length;j++)
        	{
            	if(graph[current][j]>0)
            	{
					if(verts[j]==INF)
					{
						////System.out.println("Farms");
                		verts[j] = verts[current] + graph[current][j];
                		parents[j] = current;
                		Q.enqueue(j);
					}
					else if(graph[current][j]+verts[current]<verts[j])
					{
						////System.out.println(MapObject.getGridCoord(current)+","+MapObject.getGridCoord(j)+" "+graph[current][j]);
						verts[j] = graph[current][j]+verts[current];
                		parents[j] = current;
                		Q.enqueue(j);
					}
				//	//System.out.println("");
				}
			}
		}
		////System.out.println("HERE");
	}

	static class MyQueue
	{
		private ArrayList<Integer> list = new ArrayList<Integer>();

		public void enqueue(Integer num)
		{
			list.add(num);
		}

		public Integer dequeue()
		{
			return list.remove(0);
		}

		public boolean isEmpty()
		{
			return list.isEmpty();
		}
	}
}