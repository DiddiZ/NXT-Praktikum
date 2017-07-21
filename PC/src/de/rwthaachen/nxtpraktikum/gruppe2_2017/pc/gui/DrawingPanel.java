package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui.Navigator.MAP_SQUARE_LENGTH;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Map.Entry;
import javax.swing.JPanel;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.MapData;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.NXTData;

/**
 * @author Christian
 */
class DrawingPanel extends JPanel implements MouseListener
{
	private final int width = 550;
	private final int height = 270;
	final int ticNumber = 10; // axes marking number; higher equals more
	final int ticSize = 150; // axes marking size; higher equals smaller
	final int pointSize = 6; // point diameter in pixel
	final int barrierLength = 20;
	private final MapData map;
	private final NXTData data;
	private final ApplicationHandler applicationHandler;

	public DrawingPanel(MapData map, NXTData data, ApplicationHandler applicationHandler) {
		this.map = map;
		this.data = data;
		this.applicationHandler = applicationHandler;

		addMouseListener(this);
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

	@Override
	public void mouseClicked(MouseEvent e) {
		if (applicationHandler.isConnected()) {
			final Point p = e.getPoint();
			final double x = p.getX() - width / 2;
			final double y = -1 * (p.getY() - height / 2);
			applicationHandler.driveToMethod("" + x, "" + y);
		}

	}

	private void drawArrowPosition(Graphics g) {
		g.setColor(new Color(255, 0, 0));
		final int adjacentSide = (int)(Math.cos(Math.toRadians(180 - 45 + data.getHeading())) * 8);
		final int oppositeSide = (int)(Math.sin(Math.toRadians(180 - 45 + data.getHeading())) * 8);

		final int posX = (int)data.getPositionX(), posY = (int)data.getPositionY();
		g.drawLine(posX, -posY, posX - oppositeSide, -posY - adjacentSide);
		g.drawLine(posX, -posY, posX - adjacentSide, -posY + oppositeSide);

		g.drawLine(posX, -posY, posX - (int)(Math.cos(Math.toRadians(180 - 90 + data.getHeading())) * 12),
				-posY + (int)(Math.sin(Math.toRadians(180 - 90 + data.getHeading())) * 12));

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
		synchronized (map) {
			for (final Entry<Point, Float> e : map.entrySet()) {
				final Point p = e.getKey();
				final boolean isObstacle = e.getValue() > 0.5f;

				g.setColor(isObstacle ? new Color(230, 150, 121) : new Color(184, 214, 152));
				g.fillRect(p.x, -p.y, MAP_SQUARE_LENGTH, MAP_SQUARE_LENGTH);
			}
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

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}
