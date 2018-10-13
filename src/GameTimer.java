import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.text.*;

public class GameTimer implements Runnable
{
	static int min, sec, mil;
	Thread th;
	boolean running = false;
	Font digital;
	NumberFormat format;
	int refSecond = 0;

	public GameTimer()
	{
		min = 0;
		sec = 0;
		mil = 0;
		th = new Thread(this);
		format = NumberFormat.getInstance();
		try {
			digital = Font.createFont(Font.TRUETYPE_FONT, new File("Font//DS-DIGI.ttf")).deriveFont(50f);
			} catch (IOException|FontFormatException e) {}
		format.setMaximumIntegerDigits(2);
		format.setMinimumIntegerDigits(2);
		//digital = new Font(
	}


//***************************************************************************
	public static int getSeconds()
	{
		int seconds = (min*60)+sec;
		return seconds;
	}

	public void start()
	{
		running = true;
		th.start();
	}

	public void run()
	{
		while(running)
		{
			if(sec>=60)
			{
				min++;
				sec = 0;
			}
			Calendar calendar = Calendar.getInstance();

			if(calendar.get(Calendar.SECOND)!=refSecond)
			{
				refSecond = calendar.get(Calendar.SECOND);
				sec++;
			}
			try{Thread.sleep(1);}catch(InterruptedException e){}
		}
	}
	public void stop()
	{
		running = false;
	}

	public Font getFont()
	{
		return digital;
	}
	public String toString()
	{
			return "00:"+format.format(min)+":"+format.format(sec);
	}

	public void render(Graphics g)
	{
		try{
			Color c = g.getColor();
		g.setColor(new Color(127,127,127,200));
		g.fillRect(40,15,200,100);
		g.setColor(Color.GREEN);
		g.setFont(digital);
		g.drawString(this.toString(),55,75);
		g.setColor(c);
		}catch(NullPointerException e){}
	}
}