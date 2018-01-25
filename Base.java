import java.awt.*;

public class Base extends MapObject
{
	Image baseImage;
	Color baseColor;
	int HP = 100;
	boolean friendly;

	public Base(int px, int py, int pi, int pj, Color col, boolean friendly)
	{
		super(2, px, py, pi, pj);
		baseColor = col;
		this.friendly = friendly;
	}

	public void render(Graphics g)
	{
		Color c = g.getColor();
		//g.drawImage(baseImage,x,y,width,height,null);
		g.setColor(baseColor);
		g.fillRect(x,y,42,42);
		g.setColor(Color.green);
		g.fillRect(x+11,y+11,21,21);
		g.setColor(c);
	}

	public void hit(int dmg)
	{
		this.HP -= dmg;
	}
}