// CS 401 Fall 2012

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.ArrayList;


class Present implements MyShape
{
	public Rectangle2D.Double presbackground;
	public Rectangle2D.Double vertribbon;
	public Rectangle2D.Double horzribbon;
	public Ellipse2D.Double bow1;
	public Ellipse2D.Double bow2;
	

	private int X, Y;
	private int size;
	
	private boolean isHighlighted;
	public ArrayList<Rectangle2D.Double> presarray  = new ArrayList<Rectangle2D.Double>();
	
	

	public Present(int startX, int startY, int sz)
	{
		X = startX;
		Y = startY;
		size = sz;

		setUp();
	}
	
	private void setUp()
	{
		
		
		
		
		presbackground = new Rectangle2D.Double(X,Y,60,60);
			
		
		vertribbon = new Rectangle2D.Double(X+25,Y,10,60);
		horzribbon = new Rectangle2D.Double(X,Y+25,60,10);
		
		bow1 = new Ellipse2D.Double(X+15,Y-15,15,15);
		bow2 = new Ellipse2D.Double(X+30,Y-15,15,15);
		
		
	}

	public void highlight(boolean b)
	{
		isHighlighted = b;
	}
	public void draw(Graphics2D g)
	{
	
		g.setColor(new Color(68,189,72));
		
			if (isHighlighted)
				g.draw(presbackground);
			else
				g.fill(presbackground);
		
		
		
		g.setColor(new Color(224,49,49));
		
			if (isHighlighted)
				g.draw(vertribbon);
			else
				g.fill(vertribbon);
			
		g.setColor(new Color(224,49,49));
		if (isHighlighted)
				g.draw(horzribbon);
			else
				g.fill(horzribbon);
			
			
		g.setColor(new Color(255,255,100));
		if (isHighlighted)
			g.draw(bow1);
		else
			g.fill(bow1);
		
		g.setColor(new Color(255,255,100));
		if (isHighlighted)
			g.draw(bow2);
		else
			g.fill(bow2);	
		
		
		
		
	}

	public void move(int x, int y)
	{
		int deltaX = x - X;
		int deltaY = y - Y;
		
		
		presbackground.setRect(X,Y,60,60);
		
		
		
		vertribbon.setRect(X+25,Y,10,60);
		horzribbon.setRect(X,Y+25,60,10);
		
		bow1.setFrame(X+15,Y-15,15,15);
		bow2.setFrame(X+30,Y-15,15,15);
	
		
		
		X = x;
		Y = y;
	}

	public boolean contains(double x, double y)
	{
		
		if (presbackground.contains(x,y))
			return true;
		
		
	
		
		if(vertribbon.contains(x,y))
			return true;
		if(horzribbon.contains(x,y))
			return true;
		
		
		if (bow1.contains(x,y))
			return true;
		if (bow2.contains(x,y))
			return true;
		
		return false;
		
	}

	public void resize(int newsize)
	{
		size = newsize;
		setUp();
	}

	// Note again the format
	public String saveData()
	{
		return ("Present:" + X + ":" + Y + ":" + size);
	}
}
