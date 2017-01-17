

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;
import java.lang.Object;

class Cloud implements MyShape
{
	
	public Ellipse2D.Double cloud1;
	public Ellipse2D.Double cloud2;
	public Ellipse2D.Double cloud3;
	public Ellipse2D.Double cloud4;
	public Ellipse2D.Double cloud5;

	private int X, Y;
	private int size;

	private boolean isHighlighted;
	

	public Cloud(int startX, int startY, int sz)
	{
		X = startX;
		Y = startY;
		size = sz;

		setUp();
	}
	
	
	private void setUp()
	{
	
		cloud1 = new Ellipse2D.Double(X,Y,30,40);
		cloud2 = new Ellipse2D.Double(X+3,Y+3,40,30);
		cloud3 = new Ellipse2D.Double(X-3,Y-5,20,30);
		cloud4 = new Ellipse2D.Double(X+7,Y+4,40,20);
		cloud5 = new Ellipse2D.Double(X+6,Y-2,30,20);
		
	
	}

	public void highlight(boolean b)
	{
		isHighlighted = b;
	}

	
	public void draw(Graphics2D g)
	{

		g.setColor(new Color(255,255,255));
		if (isHighlighted)
			g.draw(cloud1);
		else
			g.fill(cloud1);
		
		g.setColor(new Color(255,255,255));
		if (isHighlighted)
			g.draw(cloud2);
		else
			g.fill(cloud2);
		
		g.setColor(new Color(255,255,255));
		if (isHighlighted)
			g.draw(cloud3);
		else
			g.fill(cloud3);
		
		g.setColor(new Color(255,255,255));
		if (isHighlighted)
			g.draw(cloud4);
		else
			g.fill(cloud4);
		
		g.setColor(new Color(255,255,255));
		if (isHighlighted)
			g.draw(cloud5);
		else
			g.fill(cloud5);
	}

	
	public void move(int x, int y)
	{
		int deltaX = x - X;
		int deltaY = y - Y;
		
		
		cloud1.setFrame(X,Y,30,40);
		cloud2.setFrame(X+3,Y+3,40,30);
		cloud3.setFrame(X-3,Y-5,20,30);
		cloud4.setFrame(X+5,Y+4,40,30);
		cloud5.setFrame(X+6,Y-2,30,20);
		
		X = x;
		Y = y;
	}

	
	public boolean contains(double x, double y)
	{
		
		if (cloud1.contains(x,y))
			return true;
		if (cloud2.contains(x,y))
			return true;
		if (cloud3.contains(x,y))
			return true;
		if (cloud4.contains(x,y))
			return true;
		if (cloud5.contains(x,y))
			return true;
		return false;
	}

	
	public void resize(int newsize)
	{
		size = newsize;
		setUp();
	}

	
	public String saveData()
	{
		return ("Cloud:" + X + ":" + Y + ":" + size);
	}
}
