package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;

import javax.swing.JPanel;

class DrawingPanel extends JPanel
{
	int width = 550;
    int height = 270;
	public void paintComponent ( Graphics g )
	  {
	    super.paintComponent ( g );
		
	    // Set up a Cartesian coordinate system

	    // get the size of the drawing area		
	    //Dimension size = this.getSize();
	    

	    // place the origin at the middle
	    g.translate (width / 2, height / 2);

	    // draw the x and y axes
	    drawXYAxes (g);
		g.drawString("test", 50, 50);
	    //graphLine (g, 0.5, 25.2);
	    System.out.println("kommt bis hier");

	  }
		
	  private void drawXYAxes (Graphics g) {
	    //Dimension size = this.getSize();
	    int hBound = width / 2;
	    int vBound = height / 2;
	    int tic = width / 100;
				
	    // draw the x-axis
	    g.drawLine (-hBound, 0, hBound, 0);

	    // draw the tic marks along the x axis
	    for (int k = -hBound; k <= hBound; k += 10)
	      g.drawLine (k, tic, k, -tic);
				
	    // draw the y-axis
	    g.drawLine (0, vBound, 0, -vBound);	

	    // draw the tic marks along the y axis
	    for (int k = -vBound; k <= vBound; k += 10)
	      g.drawLine (-tic , k, +tic, k);					
	  }
		
	  private void graphLine (Graphics g, double a,  double b) {
	    Dimension size = this.getSize();
	    g.setColor (Color.red);
			
	    int x1 = width / 2;
	    int y1 = (int) ((a * x1) + b);
	    y1 = - y1;
			
	    int x2 = - x1;
	    int y2 = (int)((a * x2) + b);
	    y2 = - y2;
			
	    g.drawLine (x1, y1, x2, y2);	
	  }
	  
}