import java.awt.*;

public class Tile extends MapObject
{
	int topX, topY;
	int botX, botY;

	public Tile(int px, int py, int pi, int pj)
	{
		super(0, px, py, pi, pj);
		topX = px;
		topY = py;
		botX = px+42;
		botY = py+42;
	}

	public void render(Graphics g)
	{
	}
}