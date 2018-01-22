import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.applet.*;
import java.util.*;
import java.io.*;
import java.awt.image.*;

public class PlanetaryMapEditor
extends Applet
implements MouseListener, Runnable
{
	CustomWindow window;
	Controller c;
	GameMap map;
	Thread th;
	JTextField txt = new JTextField("Welcome to Java");
	int bw = 42;
	int placeType = 1;

	public PlanetaryMapEditor()
	{
		//c = new Controller(this);
		map = new GameMap();
	}

	public void init()
	{
		setSize(750,1000);
		addMouseListener(this);
		window = new CustomWindow(750,1000,this);
		window.center();

		map.setComponentBase(12,2, false);
		map.setComponentBase(2,12, true);
		th = new Thread(this);
		th.start();
		loadLevel("LevelOne.txt");
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
		map.renderMap(g);
		Color c = g.getColor();
		g.drawRect(281,11,this.getWidth()-322,this.getHeight()-49);
		g.fillRect(80,400,100,40);

		g.setColor(Color.RED);
		g.fillRect(80,500,100,40);

		g.setColor(c);
		for(int i = 0; i < 16;i++)
		{
			for(int j = 0; j < 16;j++)
			{
				g.drawRect(281+(j*bw),11+(i*bw),bw,bw);
			}
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

	public void run()
	{
		while(true)
		{
			repaint();

		}
	}

	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mousePressed(MouseEvent e)
	{
		int mouseX = e.getX();
		int mouseY = e.getY();

		//System.out.println(e.getPoint());
		//System.out.println(GameMap.iFromY(mouseY)+" "+GameMap.jFromX(mouseX)+"\n\n");
		map.setRock(e.getX(),e.getY());
		repaint();
		if(mouseX>80&&mouseX<180&&mouseY>400&&mouseY<440)
		{
			String fileName = JOptionPane.showInputDialog("Input File Name");
			String myPath = (this.getClass().getResource(fileName+".txt"))+"";
			if(myPath!=null)
			{
				int answer = JOptionPane.showConfirmDialog(window, fileName+".txt Already Exists Are You Sure You Would Like To Overwrite It?");

				if (answer == JOptionPane.YES_OPTION)
				{
					map.writeMapFile(fileName,false);
				}
			}
			else
				map.writeMapFile(fileName);
		}
	}
	public void mouseReleased(MouseEvent e){}
	public void mouseClicked(MouseEvent e)
	{
	}

	public static void main(String[] args)
	{
		(new PlanetaryMapEditor()).init();
	}
}