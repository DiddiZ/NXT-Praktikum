package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui.Navigator.MAP_SQUARE_LENGTH;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import javax.swing.JPanel;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.MapData;

/**
 * @author Christian
 */
class DrawingPanel extends JPanel
{
	private final int width = 550;
	private final int height = 270;
	private int posX = 0;
	private int posY = 0;
	private float head = 0;
	final int ticNumber = 10; // axes marking number; higher equals more
	final int ticSize = 150; // axes marking size; higher equals smaller
	final int pointSize = 6; // point diameter in pixel
	final int barrierLength = 20;
	private final List<Integer[]> obstacles = new ArrayList<>();
	private final List<Integer[]> obstaclesPoints = new ArrayList<>();
	private final MapData map;

	public DrawingPanel(MapData map) {
		this.map = map;
	}

	public void setXY(int x, int y) {
		posX = x;
		posY = y;
	}

	public void setHeading(float heading) {
		head = -heading;
	}

	public void newObstacle(float heading, float distance) {
		final Integer[] obstacle = new Integer[4];
		final int hypotenuse = (int)Math.sqrt(distance * distance + barrierLength / 2 * (barrierLength / 2));
		final int adjacentSide = (int)Math.cos(heading * hypotenuse);
		final int oppositeSide = (int)Math.sin(heading * hypotenuse);

		obstacle[0] = posX + oppositeSide;
		obstacle[1] = posY + adjacentSide;
		obstacle[2] = posX + adjacentSide;
		obstacle[3] = posY + oppositeSide;

		obstacles.add(obstacle);
	}

	public void newObstaclePoint(float heading, float distance) {
		final Integer[] obstacle = new Integer[2];

		final int hypotenuse = (int)distance;
		final int adjacentSide = (int)Math.cos(heading * hypotenuse);
		final int oppositeSide = (int)Math.sin(heading * hypotenuse);

		obstacle[0] = posX + oppositeSide;
		obstacle[1] = posY + adjacentSide;

		obstaclesPoints.add(obstacle);

	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.translate(width / 2, height / 2);

		// draw grid
		drawGrid(g);

		// draw the x and y axes
		drawXYAxes(g);

		// draw position
		drawArrowPosition(g);

		/*
		 * for(int i=0; i<obstacles.size(); i++){
		 * drawObstacleLine(g, obstacles.get(i));
		 * }
		 * for(int i=0; i<obstacles.size(); i++){
		 * drawObstaclePoint(g, obstacles.get(i));
		 * }
		 */
	}

	private void drawArrowPosition(Graphics g) {
		g.setColor(new Color(255, 0, 0));
		final int adjacentSide = (int)(Math.cos(Math.toRadians(180 - 45 - head)) * 8);
		final int oppositeSide = (int)(Math.sin(Math.toRadians(180 - 45 - head)) * 8);

		g.drawLine(posX, -posY, posX - oppositeSide, -posY - adjacentSide);
		g.drawLine(posX, -posY, posX - adjacentSide, -posY + oppositeSide);

		g.drawLine(posX, -posY, posX - (int)(Math.cos(Math.toRadians(180 - 90 - head)) * 12),
				-posY + (int)(Math.sin(Math.toRadians(180 - 90 - head)) * 12));

		/*
		 * map.append(new MapData(navi.discrete(posX), navi.discrete(posY), false));
		 * float range = OBSTACLE_DETECTION_RANGE;
		 * float width = OBSTACLE_DETECTION_WIDTH;
		 * while(!(width<0)){
		 * float angleModifier = (float)Math.atan((double)(width/range));
		 * while(!(range<=0)){
		 * System.out.println("draw");
		 * map.append(navi.calcSquare(navi.getNXTData().getPositionX(), navi.getNXTData().getPositionY(), navi.getNXTData().getHeading() + angleModifier, range, false));
		 * map.append(navi.calcSquare(navi.getNXTData().getPositionX(), navi.getNXTData().getPositionY(), navi.getNXTData().getHeading() - angleModifier, range, false));
		 * range -= MAP_SQUARE_LENGTH;
		 * }
		 * range = OBSTACLE_DETECTION_RANGE;
		 * width -= MAP_SQUARE_LENGTH;
		 * }
		 */
	}

	/*
	 * private void drawObstaclePoint(Graphics g, Integer[] point){
	 * g.setColor(new Color(0,0,255));
	 * g.fillOval(point[0]-(pointSize/2), -point[1]-(pointSize/2), pointSize, pointSize);
	 * }
	 * private void drawObstacleLine(Graphics g, Integer[] line){
	 * g.drawLine(line[0], -line[1], line[2], -line[3]);
	 * }
	 */
	private void drawGrid(Graphics g) {
		for (final Entry<Point, Boolean> e : map.entrySet()) {
			final Point p = e.getKey();
			final boolean isObstacle = e.getValue();

			g.setColor(isObstacle ? new Color(230, 150, 121) : new Color(184, 214, 152));
			g.fillRect(p.x, -p.y, MAP_SQUARE_LENGTH, MAP_SQUARE_LENGTH);
		}
	}

	private void drawXYAxes(Graphics g) {
		// Dimension size = this.getSize();

		g.setColor(Color.BLACK);
		final int hBound = width / 2;
		final int vBound = height / 2;
		final int tic = width / ticSize;

		// draw the x-axis
		g.drawLine(-hBound, 0, hBound, 0);

		// draw the tic marks along the x axis
		for (int k = 0; k <= hBound; k += ticNumber) {
			if (k % 100 == 0) {
				g.drawLine(k, tic * 2, k, -tic * 2);
			} else {
				g.drawLine(k, tic, k, -tic);
			}
		}

		for (int k = 0; k >= -hBound; k -= ticNumber) {
			if (k % 100 == 0) {
				g.drawLine(k, tic * 2, k, -tic * 2);
			} else {
				g.drawLine(k, tic, k, -tic);
			}
		}

		// draw the y-axis
		g.drawLine(0, vBound, 0, -vBound);

		// draw the tic marks along the y axis
		for (int k = 0; k <= vBound; k += ticNumber) {
			if (k % 100 == 0) {
				g.drawLine(-tic * 2, k, tic * 2, k);
			} else {
				g.drawLine(-tic, k, tic, k);
			}
		}

		for (int k = 0; k >= -vBound; k -= ticNumber) {
			if (k % 100 == 0) {
				g.drawLine(-tic * 2, k, tic * 2, k);
			} else {
				g.drawLine(-tic, k, tic, k);
			}
		}

		g.drawString("x", 265, 15);
		g.drawString("y", 7, -125);
		g.drawString("1m", 91, 15);
		g.drawString("1m", 7, -95);
	}
}
