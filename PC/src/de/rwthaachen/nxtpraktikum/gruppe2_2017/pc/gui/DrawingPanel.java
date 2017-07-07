package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

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
    final int ticNumber = 10; //axes marking number; higher equals more
    final int ticSize = 150; //axes marking size; higher equals smaller
    final int pointSize = 6; //point diameter in pixel
    final int barrierLength = 20;
    private List<Integer[]> obstacles = new ArrayList<Integer[]>();
    private List<Integer[]> obstaclesPoints = new ArrayList<Integer[]>();
    
    public void setXY(int x, int y){
    	posX = x;
    	posY = y;
    }
    
    public void newObstacle(float heading, float distance){
    	Integer[] obstacle = new Integer[4];
    	int hypotenuse = (int) Math.sqrt((distance*distance+(barrierLength/2)*(barrierLength/2)));
    	int adjacentSide = (int)Math.cos(heading*hypotenuse);
    	int oppositeSide = (int)Math.sin(heading*hypotenuse);
    	
    	obstacle[0]=posX+oppositeSide;
    	obstacle[1]=posY+adjacentSide;
    	obstacle[2]=posX+adjacentSide;
    	obstacle[3]=posY+oppositeSide;
    	
    	obstacles.add(obstacle);
    }
    
    public void newObstaclePoint(float heading, float distance){
    	Integer[] obstacle = new Integer[2];
    	
    	int hypotenuse = (int)distance;
    	int adjacentSide = (int)Math.cos(heading*hypotenuse);
    	int oppositeSide = (int)Math.sin(heading*hypotenuse);
    	
    	obstacle[0]=posX+oppositeSide;
    	obstacle[1]=posY+adjacentSide;
    	
    	obstaclesPoints.add(obstacle);
    	
    }
    
	public void paintComponent ( Graphics g )
	  {
	    super.paintComponent ( g );
	    g.translate (width / 2, height / 2);

	    // draw the x and y axes
	    drawXYAxes (g);
		drawXYPoint(g);

		for(int i=0; i<obstacles.size(); i++){
			drawObstacleLine(g, obstacles.get(i));
		}
		
		for(int i=0; i<obstacles.size(); i++){
			drawObstaclePoint(g, obstacles.get(i));
		}
		
	  }
	
	  private void drawXYPoint (Graphics g){
		  g.setColor(new Color(255,0,0));
		  g.fillOval(posX-(pointSize/2), -posY-(pointSize/2), pointSize, pointSize);
	  }
	  
	  private void drawObstaclePoint(Graphics g, Integer[] point){
		  g.setColor(new Color(0,0,255));
		  g.fillOval(point[0]-(pointSize/2), -point[1]-(pointSize/2), pointSize, pointSize);
	  }
	  
	  private void drawObstacleLine(Graphics g, Integer[] line){
		  g.drawLine(line[0], -line[1], line[2], -line[3]);
	  }
		
	  private void drawXYAxes (Graphics g) {
	    //Dimension size = this.getSize();
	    int hBound = width / 2;
	    int vBound = height / 2;
	    int tic = width / ticSize;
				
	    // draw the x-axis
	    g.drawLine (-hBound, 0, hBound, 0);
	    
	    // draw the tic marks along the x axis
	    for(int k=0; k<=hBound; k+=ticNumber){
	    	if((k%100)==0)
	    	{
	    	g.drawLine (k, tic*2, k, -tic*2);
	    	}
	    	else{
	    		g.drawLine (k, tic, k, -tic);
	    	}
	    }

	    for(int k=0; k>=-hBound; k-=ticNumber){
	    	if((k%100)==0)
	    	{
	    	g.drawLine (k, tic*2, k, -tic*2);
	    	}
	    	else{
	    		g.drawLine (k, tic, k, -tic);
	    	}
	    }
	    
	    // draw the y-axis
	    g.drawLine (0, vBound, 0, -vBound);	
	    
	 // draw the tic marks along the y axis
	    for(int k=0; k<=vBound; k+=ticNumber){
	    	if((k%100)==0)
	    	{
	    	g.drawLine (-tic*2, k, tic*2, k);
	    	}
	    	else{
	    		g.drawLine (-tic, k, tic, k);
	    	}
	    }
	   
	    for(int k=0; k>=-vBound; k-=ticNumber){
	    	if((k%100)==0)
	    	{
	    	g.drawLine (-tic*2, k, tic*2, k);
	    	}
	    	else{
	    		g.drawLine (-tic, k, tic, k);
	    	}
	    }
	    
	    g.drawString("x", 265, 15);
		g.drawString("y", 7, -125);
		g.drawString("1m", 91, 15);
		g.drawString("1m", 7, -95);			
	  }
		
}