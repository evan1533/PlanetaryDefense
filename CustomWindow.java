import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.image.*;
import java.text.*;
import java.math.*;
import java.applet.*;

public class CustomWindow extends JFrame
{
	int w, h;
	boolean resizable = false;

	public CustomWindow(int pw, int ph)
	{
		w = pw;
		h = ph;
		setResizable(resizable);
		setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		setVisible(true);
		setSize(h,w);
		center();
	}

	public CustomWindow(int pw, int ph, Applet ap)
	{
		w = pw;
		h = ph;
		add(ap);
		setResizable(resizable);
		setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		setVisible(true);
		setSize(h,w);
		center();
	}

	public void center()
	{
		setLocationRelativeTo(null);
	}
}