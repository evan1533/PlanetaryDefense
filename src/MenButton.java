import java.awt.*;

public class MenButton
{
	int x, y, w, h;
	String action;

	public MenButton(PlanetaryDefense game, int px, int py, int pw, int ph, String command)
	{
		x = px;
		y = py;
		w = pw;
		h = ph;
		action = command;
	}


	public boolean isPressed(int mx, int my)
	{
		if(mx>=x&&mx<=x+w&&my>=y&&my<=y+h)
			return true;

		return false;
	}

	public String getAction()
	{
		return this.action;
	}
}