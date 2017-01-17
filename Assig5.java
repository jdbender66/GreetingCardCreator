
//Joe Bender
//CS 401
//Ramirez Assignment #5
//Tues/Thurs 9:30-10:45

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.awt.print.*;
import javax.swing.JOptionPane;
import java.util.ArrayList;

// Create enum types 
enum Figures {TREE,SNOWFLAKE,GREETING,CABIN,CLOUD,PRESENT};
enum Mode {NONE,DRAW,SELECTED,MOVING};


class thePrintPanel implements Printable
{
	JPanel panelToPrint;
	
	public int print(Graphics g, PageFormat pf, int page) throws
                                                        PrinterException
    {
        if (page > 0) { /* We have only one page, and 'page' is zero-based */
            return NO_SUCH_PAGE;
        }

        /* User (0,0) is typically outside the imageable area, so we must
         * translate by the X and Y values in the PageFormat to avoid clipping
         */
         
        Graphics2D g2d = (Graphics2D)g;
        
        g2d.translate(pf.getImageableX(), pf.getImageableY());

        /* Now print the window and its visible contents */
        panelToPrint.printAll(g);

        /* tell the caller that this page is part of the printed document */
        return PAGE_EXISTS;
    }
    
    public thePrintPanel(JPanel p)
    {
    	panelToPrint = p;
    }
}

public class Assig5
{	
	private ShapePanel drawPanel;
	private JPanel buttonPanel;
	private JButton makeShape;
	private JRadioButton makeTree, makeFlake, makeGreet,makeCabin,makeCloud,makePresent;
	private ButtonGroup shapeGroup;
	private Figures currShape;
	private JLabel msg;
	
	private JMenuBar theBar;
	private JMenu fileMenu;
	private JMenu editMenu;
	private JMenuItem endProgram, printScene, cutItem, copyItem, pasteItem, newOption, openFile, saveCard, saveAs;
	
	
	private JPopupMenu popper;
	private JMenuItem delete;
	private JMenuItem resize;
	private JFrame theWindow;
	public String filename;
	public int openfilename;
	public String filenamewindow = "";
	public int response;
	public String filename2 = "";

	
	public JFileChooser fileChooser = new JFileChooser();
	public ArrayList<MyShape> shapeList;	
	private ArrayList<String> inputShapes;	
	private MyShape newShape;

	public Assig5()
	{
		drawPanel = new ShapePanel(640, 480);
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(3, 2));

		makeShape = new JButton("Click to Draw");

		ButtonHandler bhandler = new ButtonHandler();
		makeShape.addActionListener(bhandler);

		buttonPanel.add(makeShape);
		msg = new JLabel("");
		buttonPanel.add(msg);

		makeTree = new JRadioButton("Tree", false);
		makeFlake = new JRadioButton("Snowflake", true);
		makeGreet = new JRadioButton("Greeting", false);
		makeCabin = new JRadioButton("Cabin",false);
		makeCloud = new JRadioButton("Cloud",false);
		makePresent = new JRadioButton("Present",false);
		
		

		RadioHandler rhandler = new RadioHandler();
		makeTree.addItemListener(rhandler);
		makeFlake.addItemListener(rhandler);
		makeGreet.addItemListener(rhandler);
		makeCabin.addItemListener(rhandler);
		makeCloud.addItemListener(rhandler);
		makePresent.addItemListener(rhandler);

		buttonPanel.add(makeFlake);
		buttonPanel.add(makeTree);
		buttonPanel.add(makeGreet);
		buttonPanel.add(makeCabin);
		buttonPanel.add(makeCloud);
		buttonPanel.add(makePresent);

		// A ButtonGroup allows a set of JRadioButtons to be associated
		// together such that only one can be selected at a time
		shapeGroup = new ButtonGroup();
		shapeGroup.add(makeFlake);
		shapeGroup.add(makeTree);
		shapeGroup.add(makeGreet);
		shapeGroup.add(makeCabin);
		shapeGroup.add(makeCloud);
		shapeGroup.add(makePresent);

		currShape = Figures.SNOWFLAKE;
		drawPanel.setMode(Mode.NONE);

		theWindow = new JFrame("Greeting Card Creator");
		Container c = theWindow.getContentPane();
		drawPanel.setBackground(Color.lightGray);
		c.add(drawPanel, BorderLayout.NORTH);
		c.add(buttonPanel, BorderLayout.SOUTH);

		// I make a JMenuBar, then
		// I put a JMenu in it, then I put JMenuItems in the JMenu.  I
		// can have multiple JMenus if I'd like.  JMenuItems generate
		// ActionEvents, just like JButtons, so I just have to link an
		// ActionListener to them.
		
		//the file menu
		theBar = new JMenuBar();
		theWindow.setJMenuBar(theBar);
		fileMenu = new JMenu("File");
		theBar.add(fileMenu);
		printScene = new JMenuItem("Print");
		endProgram = new JMenuItem("Exit");
		newOption = new JMenuItem("New");
		openFile = new JMenuItem("Open");
		saveCard = new JMenuItem("Save");
		saveAs = new JMenuItem("Save As");
		
		printScene.addActionListener(bhandler);
		endProgram.addActionListener(bhandler);
		newOption.addActionListener(bhandler);
		openFile.addActionListener(bhandler);
		saveCard.addActionListener(bhandler);
		saveAs.addActionListener(bhandler); 
		
		
		
		fileMenu.add(newOption);
		fileMenu.add(openFile);
		fileMenu.add(saveCard);
		fileMenu.add(saveAs);
		fileMenu.add(printScene);
		fileMenu.add(endProgram);
		
		
		saveCard.setEnabled(false);
		
		
		//the edit menu
		editMenu = new JMenu("Edit");
		theBar.add(editMenu);
		cutItem = new JMenuItem("Cut");
		copyItem = new JMenuItem("Copy");
		pasteItem = new JMenuItem("Paste");
	
		
		editMenu.add(cutItem);
		editMenu.add(copyItem);
		editMenu.add(pasteItem);
		
		
		
		//cutItem.addActionListener(bhandler);
		copyItem.addActionListener(bhandler);
		//pasteItem.addActionListener(bhandler);
		
		// JPopupMenu() also holds JMenuItems.  To see how it is actually
		// brought out, see the mouseReleased() method in the ShapePanel class
		// below.
		popper = new JPopupMenu();
		delete = new JMenuItem("Delete");
		resize = new JMenuItem("Resize");
		
		delete.addActionListener(bhandler);
		resize.addActionListener(bhandler);
		popper.add(delete);
		popper.add(resize);
		
		theWindow.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		theWindow.pack();
		theWindow.setVisible(true);
	}

	public static void main(String [] args)
	{
		new Assig5();
	}


	private class RadioHandler implements ItemListener
	{
		public void itemStateChanged(ItemEvent e)
		{
			if (e.getSource() == makeTree)
				currShape = Figures.TREE;
			else if (e.getSource() == makeFlake)
				currShape = Figures.SNOWFLAKE;
			else if (e.getSource() == makeGreet)
				currShape = Figures.GREETING;
			else if (e.getSource() == makeCabin)
				currShape = Figures.CABIN;
			else if (e.getSource() == makeCloud)
				currShape = Figures.CLOUD;
			else if (e.getSource() == makePresent)
				currShape = Figures.PRESENT;
		}
	}


	public class ButtonHandler implements ActionListener 
	{
		public void actionPerformed(ActionEvent e) 
		{
			if (e.getSource() == makeShape)
			{
				if (makeShape.getText().equals("Click to Draw"))
				{
					drawPanel.setMode(Mode.DRAW);
					msg.setText("Position new shapes with mouse");
					makeShape.setText("Click to Edit");
				}
				else
				{
					drawPanel.setMode(Mode.NONE);
					msg.setText("Edit shapes with mouse");
					makeShape.setText("Click to Draw");
				}
			}
			//deletes a shape
			else if (e.getSource() == delete)
			{
				boolean ans = drawPanel.deleteSelected();
				if (ans)
				{
					msg.setText("Shape deleted");
					drawPanel.repaint();
				}
			}
			//resizes a shape, did not finish
			else if (e.getSource() == resize)
			{
				
				
					msg.setText("Resizing Unavailable");
					drawPanel.repaint();
				
			}
			
			
			
			//prints scene
			else if (e.getSource() == printScene)
			{
				Printable thePPanel = new thePrintPanel(drawPanel); 
				PrinterJob job = PrinterJob.getPrinterJob();
				job.setPrintable(thePPanel);
				boolean ok = job.printDialog();
				if (ok) 
				{
					try
					{
						job.print();
					} 
					catch (PrinterException ex)
					{
					/* The job did not successfully complete */
					}
				}
             		}
             		
             		//ends program
			else if (e.getSource() == endProgram)
			{
				System.exit(0);
			}
			
			
			//saves as, brings up save dialog box
			else if (e.getSource() == saveAs)
			{
				
				openfilename = fileChooser.showSaveDialog(null);
				
				
				
				
				if (openfilename == JFileChooser.APPROVE_OPTION)
				{
					filename2 = fileChooser.getSelectedFile().getName();
				
				}
				
				filename2 = filename2 + ".txt";
				
				try
				{
				File myFile = new File(filename2);
				
				PrintWriter outputFile = new PrintWriter(myFile);
			
			
				for (int x = 0; x < shapeList.size(); x++)
				{
					outputFile.println(shapeList.get(x).saveData());
					
					
					
				}
				outputFile.close();
				saveCard.setEnabled(true);
				}
				catch (FileNotFoundException a)
				{
					System.out.println("File Not found");
				}
					
				
			}
			
			//just saves based on filename2, no dialog box
			else if (e.getSource() == saveCard)
			{
				
				
				try
				{
				File myFile = new File(filename2);
				
				PrintWriter outputFile = new PrintWriter(myFile);
			
			
				for (int x = 0; x < shapeList.size(); x++)
				{
					outputFile.println(shapeList.get(x).saveData());
					
					
					
				}
				outputFile.close();
				JOptionPane.showMessageDialog(null,"File Saved as "+filename2);
	
				}
				catch (FileNotFoundException a)
				{
					System.out.println("File Not found");
				}
					
				
			}
			
			//creates new document
			else if (e.getSource() == newOption)
			{
				
			response = JOptionPane.showConfirmDialog(null,"Are you sure you want to create a new card? All current data will be lost");
			
			 if (response == JOptionPane.YES_OPTION )
			    {
				
			    	  shapeList.clear();
			    	  drawPanel.repaint();
			    }
				
					
				
			}
			//opens an existing file with a dialog box
			else if (e.getSource() == openFile)
			{
			try
			{
				openfilename = fileChooser.showOpenDialog(null);
				
				File selectedFile = null;
				
				if (openfilename == JFileChooser.APPROVE_OPTION)
				{
					selectedFile = fileChooser.getSelectedFile();
					filenamewindow = selectedFile.getPath();
					JOptionPane.showMessageDialog(null,"You selected "+filenamewindow);
				}
				
				
				
				
				
				
				
				FileInputStream fstream = new FileInputStream(selectedFile);
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				
				
				String check = br.readLine();
				
				while(check != null)
				{
				  	String[] inputfromfile = check.split(":");
				  	int x = Integer.parseInt(inputfromfile[1]);
				  	int y = Integer.parseInt(inputfromfile[2]);
				  	int s = Integer.parseInt(inputfromfile[3]);
				  	
				  	
				  	
				  	if (inputfromfile[0].equals("Tree"))
					{
						newShape = new Tree(x,y,s);
					}
					else if (inputfromfile[0].equals("Snowflake"))
					{
						newShape = new Snowflake(x,y,s);
					}
					else if (inputfromfile[0].equals("Greeting"))
					{
						newShape = new Greeting(x,y,s);
					}
					else if (inputfromfile[0].equals("Cabin"))
					{
						newShape = new Cabin(x,y,s);  
					}
					else if (inputfromfile[0].equals("Cloud"))
					{
						newShape = new Cloud(x,y,s);  
					}
					else if (inputfromfile[0].equals("Present"))
					{
						newShape = new Present(x,y,s); 
					}

					drawPanel.addShape(newShape);
					
						
					check = br.readLine();
				}	
				
				
			
			
			}
			catch (Exception p) 
			{
				JOptionPane.showMessageDialog(null,"Error!"+p);
			}
			
			filename2 = filenamewindow;
			System.out.print(filenamewindow);
			saveCard.setEnabled(true);
			}
			
			
			else if (e.getSource() == cutItem)
			{	
				
				boolean ans = drawPanel.cutSelected();
				if (ans)
				{
					msg.setText("Shape cut");
					drawPanel.repaint();
				}
				
				
			}
			
			
			else if (e.getSource() == copyItem)
			{
				
				boolean ans = drawPanel.copySelected();
				if (ans)
				{
					msg.setText("Shape copied");
					drawPanel.repaint();
				}
				
			}
			
			/*
			
			else if (e.getSource() == pasteItem)
			{
				
				
				
			}
			*/
			
		}
	}
	
	

	
	private class ShapePanel extends JPanel
	{
		
		private int prefwid, prefht;

		public int selindex;

		public int x1, y1, x2, y2; 
		
		private boolean popped; // has popup menu been activated?

		private Mode mode;   // Keep track of the current Mode

		public ShapePanel (int pwid, int pht)
		{
			shapeList = new ArrayList<MyShape>(); // create empty ArrayList
			selindex = -1;
		
			prefwid = pwid;	// values used by getPreferredSize method below
			prefht = pht;   // (which is called implicitly).  This enables
					// the JPanel to request the room that it needs.
					// However, the JFrame is not required to honor
					// that request.

			setOpaque(true);// Paint all pixels here (See API)

			setBackground(Color.lightGray);

			addMouseListener(new MyMouseListener());
			addMouseMotionListener(new MyMover());
			popped = false;
		}  // end of constructor

		// This class is extending MouseAdapter.  MouseAdapter is a predefined
		// class that implements MouseListener in a trivial way (i.e. none of
		// the methods actually do anything).  Extending MouseAdapter allows
		// a programmer to implement only the MouseListener methods that
		// he/she needs but still satisfy the interface (recall that to
		// implement an interface one must implement ALL of the methods in the
		// interface -- in this case I do not need 3 of the 5 MouseListener
		// methods)
		private class MyMouseListener extends MouseAdapter
		{
			public void mousePressed(MouseEvent e)
			{
				x1 = e.getX();  // store where mouse is when clicked
				y1 = e.getY();

				if (!e.isPopupTrigger() && (mode == Mode.NONE ||
										    mode == Mode.SELECTED)) // left click and
				{												    // either NONE or
					if (selindex >= 0)								// SELECTED mode
					{
						unSelect();			// unselect previous shape
						mode = Mode.NONE;
					}
					selindex = getSelected(x1, y1);  // find shape mouse is
													 // clicked on
					if (selindex >= 0)
					{
						mode = Mode.SELECTED;  	// Now in SELECTED mode for shape
						
						// Check for double-click.  If so, show dialog to update text of
						// the current text shape (will do nothing if shape is not a MyText)
						MyShape curr = shapeList.get(selindex);
						if (curr instanceof MyText && e.getClickCount() == 2)
						{
							String newText = JOptionPane.showInputDialog(theWindow, "Enter new text [Cancel for no change]");
							if (newText != null)
								((MyText) curr).setText(newText);
						}
					}
					repaint();	//  Make sure updates are redrawn
				}
				else if (e.isPopupTrigger() && selindex >= 0)  // if button is
				{								               // the popup menu
					popper.show(ShapePanel.this, x1, y1);      // trigger, show
					popped = true;							   // popup menu
				}											  
			}
			
			public void mouseReleased(MouseEvent e)
			{
				if (mode == Mode.DRAW) // in DRAW mode, create the new Shape
				{					   // and add it to the list of Shapes.  In this
									   // case we need to distinguish between the
									   // shapes since we are calling constructors
					if (currShape == Figures.TREE)
					{
						newShape = new Tree(x1,y1,50);
					}
					else if (currShape == Figures.SNOWFLAKE)
					{
						newShape = new Snowflake(x1,y1,10);
					}
					else if (currShape == Figures.GREETING)
					{
						newShape = new Greeting(x1,y1,30);
					}
					else if (currShape == Figures.CABIN)
					{
						newShape = new Cabin(x1,y1,30);  
					}
					else if (currShape == Figures.CLOUD)
					{
						newShape = new Cloud(x1,y1,30);  
					}
					else if (currShape == Figures.PRESENT)
					{
						newShape = new Present(x1,y1,30); 
					}

					
					addShape(newShape);
					
					
					
					
				}
				// In MOVING mode, set mode back to NONE and unselect shape (since 
				// the move is finished when we release the mouse).
				else if (mode == Mode.MOVING) 
				{
					mode = Mode.NONE;
					unSelect();  
					makeShape.setEnabled(true);
					msg.setText("");
					repaint();
				}
				else if (e.isPopupTrigger() && selindex >= 0) // if button is
				{							// the popup menu trigger, show the
					popper.show(ShapePanel.this, x1, y1); // popup menu
				}
				popped = false;  // unset popped since mouse is being released
			}
		}

		// the MouseMotionAdapter has the same idea as the MouseAdapter
		// above, but with only 2 methods.  The method not implemented
		// here is mouseMoved.  The difference between the two is whether or
		// not the mouse is pressed at the time.  Since we require a "click and
		// hold" to move our objects, we are using mouseDragged and not
		// mouseMoved.
		private class MyMover extends MouseMotionAdapter
		{
			public void mouseDragged(MouseEvent e)
			{
				x2 = e.getX();   // store where mouse is now
				y2 = e.getY();

				// Note how easy moving the shapes is, since the "work"
				// is done within the various shape classes.  All we do
				// here is call the appropriate method.  However, we don't 
				// want to accidentally move the selected shape with a right click
				// so we make sure the popup menu has not been activated.
				if ((mode == Mode.SELECTED || mode == Mode.MOVING) && !popped)
				{
					MyShape s = shapeList.get(selindex);
					mode = Mode.MOVING;
					s.move(x2, y2);
				}
				repaint();	// Repaint screen to show updates
			}
		}

		// Check to see if point (x,y) is within any of the shapes.  If
		// so, select that shape and highlight it so user can see.  Again,
		// note that we do not care which shape we are selecting here --
		// it only matters that it has the MyShape interface methods.
		// This version of getSelected() always considers the shapes from
		// beginning to end of the ArrayList.  Thus, if a shape is "under"
		// or "within" a shape that was previously created, it will not
		// be possible to select the "inner" shape.  In your assignment you
		// must redo this method so that it allows all shapes to be selected.
		// Think about how you would do this.
		public int getSelected(double x, double y)
		{                                             
			for (int i = 0; i < shapeList.size(); i++)
			{
				if (shapeList.get(i).contains(x, y))
				{
					shapeList.get(i).highlight(true); 
					return i;
				}
			}
			return -1;
		}

		public void unSelect()
		{
			if (selindex >= 0)
			{
				shapeList.get(selindex).highlight(false);
				selindex = -1;
			}
		}
		
		public boolean deleteSelected()
		{
			if (selindex >= 0)
			{
				shapeList.remove(selindex);
				selindex = -1;
				return true;
			}
			else return false;
		}
		
		
		public boolean cutSelected()
		{
			int shapeselected;
			
			if (selindex >= 0)
			{
				
				shapeselected = getSelected(x1, y1); 
				
				
		
				
				/*
				drawPanel.repaint();
				*/
				shapeList.remove(selindex);
				
				selindex = -1;
				
				return true;
				
			}
			else return false;
		}
		
		public boolean copySelected()
		{
			int shapeselected;
			
			if (selindex >= 0)
			{
				
				shapeselected = getSelected(x1, y1); 
				
				
				shapeList.add(shapeList.get(shapeselected));
				
				/*
				drawPanel.repaint();
				
				shapeList.remove(selindex);
				*/
				selindex = -1;
				
				return true;
				
			}
			else return false;
		}
		
		/*
		public boolean pasteSelected()
		{
			int shapeselected;
			
			if (selindex >= 0)
			{
				
				
				
			}
			else return false;
		}
		
		
		public boolean resizeSelected()
		{
			String resizesize;
			int resizesize2;
			String resizestring;
			myShape[] resizearray;
			String newResized;
			
			if (selindex >= 0)
			{
				resizesize = JOptionPane.showInputDialog(null,"What would you like the new size to be?:");
				resizesize2 = Integer.parseInt(resizesize);
				
				
				resizestring = shapeList.get(selindex).saveData();
				resizearray = resizestring.split(":");
				resizearray[3]=resizesize2;
				
				for(int b = 0; b < 4; b++)
				{
					newResized =+ (resizearray[b]+":");
					
				}
				
				shapeList.set(selindex,newResized);
				
				
				
				selindex = -1;
				return true;
			}
			else return false;
		}
		*/
		
		public void setMode(Mode newMode)            // set Mode
		{
			mode = newMode;
		}

		private void addShape(MyShape newshape)      // Add shape
		{
			shapeList.add(newshape);
			repaint();	// repaint so we can see new shape
		}

		// Method called implicitly by the JFrame to determine how much
		// space this JPanel wants.  Be sure to include this method in
		// your program so that your panel will be sized correctly.
		public Dimension getPreferredSize()
		{
			return new Dimension(prefwid, prefht);
		}

		// This method enables the shapes to be seen.  Note the parameter,
		// which is implicitly passed.  To draw the shapes, we in turn
		// call the draw() method for each shape.  The real work is in the draw()
		// method for each MyShape
		public void paintComponent (Graphics g)    
		{
			super.paintComponent(g);         // don't forget this line!
			Graphics2D g2d = (Graphics2D) g;
			for (int i = 0; i < shapeList.size(); i++)
			{
				shapeList.get(i).draw(g2d);
			}
		}
	} // end of ShapePanel
}