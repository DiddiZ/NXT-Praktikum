package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;

import javax.swing.JPanel;
/**
 * @author Christian
 */
class DrawingPanel extends JPanel
{
	private int width = 550;
    private int height = 270;
    private int posX = 0;
    private int posY = 0;
    
    public void setXY(int x, int y){
    	posX = x;
    	posY = y;
    }
    
	public void paintComponent ( Graphics g )
	  {
	    super.paintComponent ( g );
	    g.translate (width / 2, height / 2);

	    // draw the x and y axes
	    drawXYAxes (g);
	    
		g.drawString("x", 265, 15);
		g.drawString("y", 7, -125);
		g.drawString("1m", 97, 17);
		g.drawString("1m", 7, -100);
		drawXYPoint(g);

	  }
	
	  private void drawXYPoint (Graphics g){
		  g.setColor(new Color(255,0,0));
		  g.fillOval(posX-3, -posY-3, 6, 6);
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
		
}