// CS 401 Fall 2012
// Tree class as another implementation of the MyShape interface.
// This class also uses composition, with 2 Polygons being the primary
// components of a Tree object.  For more information on Polygons, see
// the Java API.

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.ArrayList;


class Cabin implements MyShape
{
	// Represent a Tree in two parts -- a Polygon for the top part 
	// (the branches) and another Polygon for the trunk.  Since the
	// trunk is rectangular, a Rectangle2D could have been used, but
	// to keep consistent (especially with the move() method) I used
	// Polygon objects for both.
	//private Polygon canopy;
	//private Polygon trunk;
	
	public Rectangle2D.Double brownblock;
	public Rectangle2D.Double blackoutlinerec;
	public Rectangle2D.Double door;
	public Rectangle2D.Double window1;
	public Rectangle2D.Double window2;
	public Polygon roof;
	public Rectangle2D.Double chimney;
	
	

	// X, Y and size instance variables
	private int X, Y;
	private int size;
	
	private boolean isHighlighted;
	public ArrayList<Rectangle2D.Double> brownback = new ArrayList<Rectangle2D.Double>();
	public ArrayList<Rectangle2D.Double> blackoutline = new ArrayList<Rectangle2D.Double>();
	
	
	// Create a new Tree object.  Note how the Polygons are built,
	// adding one point at a time to each.  If you plot the points out
	// on paper you will see how the shapes are formed.  This method
	// uses a setUp method as shown below to allow for the Tree to
	// be regenerated internally (i.e. outside the constructor)
	public Cabin(int startX, int startY, int sz)
	{
		X = startX;
		Y = startY;
		size = sz;

		setUp();
	}
	
	// Create the actual parts of the Tree.  For your shapes you
	// will likely use trial and error to get nice looking results
	// (I used a LOT of trial and error for mine).
	private void setUp()
	{
		
		
		
		roof = new Polygon();
		roof.addPoint(X+0,Y+2);
		roof.addPoint(X+60,Y+2);
		roof.addPoint(X+52,Y-23);
		roof.addPoint(X+9,Y-23);
		
		
		
		for (int i = 0; i<6; i++)
		{
			brownblock = new Rectangle2D.Double(X,Y+10*i,60,10);
			brownback.add(brownblock);
		}
		
		for (int i = 0; i<6; i++)
		{
			blackoutlinerec = new Rectangle2D.Double(X,Y+10*i,60,10);
			blackoutline.add(blackoutlinerec);
		}
		
		door = new Rectangle2D.Double(X+25,Y+40,10,20);
		window1 = new Rectangle2D.Double(X+8,Y+25,10,10);
		window2 = new Rectangle2D.Double(X+42,Y+25,10,10);
		chimney = new Rectangle2D.Double(X+37,Y-40,10,30);
	}

	public void highlight(boolean b)
	{
		isHighlighted = b;
	}

	// The Polygon class can also be drawn with a predefined method in
	// the Graphics2D class.  There are two versions of this method:
	// 1) draw() which only draws the outline of the shape
	// 2) fill() which draws a solid shape
	// In this class the draw() method is used when the object is
	// highlighted.
	// The colors chosen are RGB colors I looked up on the Web.  Take a
	// look and use colors you like for your shapes.
	public void draw(Graphics2D g)
	{
		
		
		
		
		
		g.setColor(new Color(61,43,31));
		
		for (int i = 0; i<6; i++)
		{
			if (isHighlighted)
				g.draw(brownback.get(i));
			else
				g.fill(brownback.get(i));
		}
		
		g.setColor(new Color(0,0,0));
		
		for (int i = 0; i<6; i++)
		{
			
				g.draw(blackoutline.get(i));
		
				
		}
		g.setColor(new Color(0,0,0));
		
			if (isHighlighted)
				g.draw(door);
			else
				g.fill(door);
			
		g.setColor(new Color(255,255,100));
		if (isHighlighted)
				g.draw(window1);
			else
				g.fill(window1);
			
			
			
		g.setColor(new Color(255,255,100));
		if (isHighlighted)
				g.draw(window2);
			else
				g.fill(window2);
			
		g.setColor(new Color(0,100,0));
		if (isHighlighted)
				g.draw(roof);
			else
				g.fill(roof);
			
		g.setColor(new Color(100,0,0));
		if (isHighlighted)
				g.draw(chimney);
			else
				g.fill(chimney);
		
		
		
	}

	// Looking at the API, we see that Polygon has a translate() method
	// which can be useful to us.  All we have to do is calculate the
	// difference of the new (x,y) and the old (X,Y) and then call
	// translate() for both parts of the tree.
	public void move(int x, int y)
	{
		int deltaX = x - X;
		int deltaY = y - Y;
		//canopy.translate(deltaX, deltaY);
		//trunk.translate(deltaX, deltaY);
		
		for (int i = 0; i < 6; i++)
		{
		
		brownback.get(i).setRect(X,Y+10*i,60,10);
		blackoutline.get(i).setRect(X,Y+10*i,60,10);
		}
		
		door.setRect(X+25,Y+40,10,20);
		window1.setRect(X+8,Y+25,10,10);
		window2.setRect(X+42,Y+25,10,10);
		roof.translate(deltaX,deltaY);
		chimney.setRect(X+37,Y-40,10,30);
		
		
		
		
		
		X = x;
		Y = y;
	}

	// Polygon also has a contains() method, so this method is also
	// simple
	public boolean contains(double x, double y)
	{
		
		for (int i = 0; i < 6; i++)
		{
		if (brownback.get(i).contains(x,y))
			return true;
		}
		
		for (int i = 0; i < 6; i++)
		{
		if (blackoutline.get(i).contains(x,y))
			return true;
		}
		
		
		if(door.contains(x,y))
			return true;
		if(window1.contains(x,y))
			return true;
		if(window2.contains(x,y))
			return true;
		if (roof.contains(x,y))
			return true;
		if (chimney.contains(x,y))
			return true;
		
		
		return false;
		
	}

	// The move() method for the Polygons that are in Tree are not
	// reconfigured like in Snowflake, so we can't use the trick used
	// there.  Instead, we just change the size and call setUp() to
	// regenerate all of the objects.
	public void resize(int newsize)
	{
		size = newsize;
		setUp();
	}

	// Note again the format
	public String saveData()
	{
		return ("Cabin:" + X + ":" + Y + ":" + size);
	}
}
