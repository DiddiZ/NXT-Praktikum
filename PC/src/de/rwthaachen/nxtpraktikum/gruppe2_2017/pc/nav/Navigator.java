package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.nav;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.MapData;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.NXTData;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.nav.aStarAlg.AStar;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.nav.aStarAlg.PointNode;

/**
 * This class is designed to calculate routes based on a {@link MapData}.
 * It provides several methods concerning the navigation.
 * The class currently calculates based on an A*-algorithm.
 * 
 * @author Fabian, Robin, Justus
 */
public final class Navigator
{
	private final NXTData data;
	private final MapData map;
	public static final int MAP_SQUARE_LENGTH = 5;
	public static final float NAVIGATION_DISTANCE_EPSILON = 15f;
	public static final float NAVIGATION_HEADING_EPSILON = 10f;
	// The following two Parameters are for automatic detection of free squares in front of NXT
	public static final float OBSTACLE_DETECTION_RANGE = 30f;
	public static final float OBSTACLE_DETECTION_WIDTH = 10f;
	private final AStar alg;

	private static final int SAFETY_DISTANCE = 10;
	private static final float US_SPREAD = 15;

	/**
	 * This is the constructor for the Navigator class.
	 * Assigns important attributes and creates a {@link MapData} and an {@link AStar}.
	 *
	 * @param data: The NXTData the calculation is based on
	 */
	public Navigator(NXTData data) {
		this.data = data;
		map = new MapData();
		alg = new AStar(map, data);
	}

	// set and get Methods
	public MapData getMapData() {
		return map;
	}

	public void resetMapData() {
		map.clear();
	}

	public NXTData getNXTData() {
		return data;
	}

	/**
	 * This method returns the discrete identification of the square the coordinate is in.
	 *
	 * @param param: the coordinate to be discretized
	 * @return the identification of the square the coordinate is in
	 */
	public static int discrete(double param) {
		return (int)param / MAP_SQUARE_LENGTH * MAP_SQUARE_LENGTH;
	}

	/**
	 * This method calculates a discrete square in which the given coordinates are located
	 * @param x: the x-coordinate to be discretized
	 * @param y: the y-coordinate to be discretized
	 * @return a Point with discretized coordinates fitting to the given values.
	 */
	public static Point discretize(double x, double y) {
		return new Point(discrete(x), discrete(y));
	}

	/**
	 * This method calculates a new square of the map based on the
	 * current position and a given distance and absolute angle.
	 *
	 * @param current_posX: the x-coordinate of the current position
	 * @param current_posY: the y-coordinate of the current position
	 * @param p_angle: the absolute angle the new square is ahead
	 * @param p_distance: the distance between the new square and the current position
	 * @return a new discretized Point which is p_distance cm away from the current Position, looking in direction p_angle
	 */
	public static Point calcSquare(float current_posX, float current_posY, float p_angle, float p_distance) {
		final double distX = -Math.cos((p_angle - 90.0) / 180.0 * Math.PI) * p_distance;
		final double distY = -Math.sin((p_angle - 90.0) / 180.0 * Math.PI) * p_distance;
		return discretize(current_posX + distX, current_posY + distY);
	}

	// renewed Navigation Methods:
	/*
	 * TODO: Renew moveTo() method.
	 * By now there is no detection if the NXT finished moving/turning or not. Right now there is just a set wait time between moving and turning, which is unusable for more than one command in a row.
	 * Idea: check if NXT has fulfilled its Task. If it hasn't done this after a timeout time, send the Navigation command again. Only if the target reaches its destination, the next moveTo command can be sent.
	 */

	/**
	 * This method checks if the NXT has reached a certain position within an epsilon stored in NAVIGATION_DISTANCE_EPSILON
	 *
	 * @param posX x coordinate for destination to check
	 * @param posY y coordinate for destination to check
	 * @return true if NXT is within epsilon of the destination
	 */
	public boolean reachedPosition(float posX, float posY) {
		final float diffX = data.getPositionX() - posX;
		final float diffY = data.getPositionY() - posY;
		if (Math.hypot(diffX, diffY) < NAVIGATION_DISTANCE_EPSILON) {
			return true;
		}
		return false;
	}

	public void addSensorData(int distance) {
		final Arc2D arc = new Arc2D.Double();

		arc.setArcByCenter(data.getPositionX(), data.getPositionY(), distance - SAFETY_DISTANCE, -data.getHeading() - US_SPREAD - 90, US_SPREAD * 2, Arc2D.PIE);
		addAllTiles(arc, false, distance == 255);

		if (distance < 255) { // Obstacle sensed
			final Arc2D arc2 = new Arc2D.Double();
			arc2.setArcByCenter(data.getPositionX(), data.getPositionY(), distance + SAFETY_DISTANCE, -data.getHeading() - US_SPREAD - 90, US_SPREAD * 2, Arc2D.PIE);

			final Area area = new Area(arc2);
			area.subtract(new Area(arc));

			addAllTiles(area, true, false);
		}
	}

	/**
	 * This method calculates the next target for the NXT
	 * The method discretizes the current position and the target
	 * and calls a method of the {@link AStar} to calculate a route in the MapData.
	 * Then iterates through the returned route to find a Point
	 * the NXT can drive to.
	 * 
	 * @param xTarget: The x-coordinate of the final target
	 * @param yTarget: The y-coordinate of the final target
	 * @return the next Point the NXT can drive to by using only one command; Integer.MIN_VALUE if the calculation failed
	 */
	public Point getNextPoint(float xTarget, float yTarget) {
		final Point destination = new Point(discrete(xTarget), discrete(yTarget));
		final Point position = new Point(discrete(data.getPositionX()), discrete(data.getPositionY()));

		final PointNode chain = alg.aStarAlgorithm(position, destination, 1000);
		if (chain != null) {
			boolean isReachable = true;
			int indexTest = chain.getChainLength();
			while (isReachable && indexTest >= 0) {
				indexTest--;
				isReachable = isReachable2((int)position.getX(), (int)position.getY(), (int)chain.getPred(indexTest).getPoint().getX(), (int)chain.getPred(indexTest).getPoint().getY());
			}
			indexTest++;
			return chain.getPred(indexTest).getPoint();
		}
		System.out.println("Calculation failed.");
		return new Point(Integer.MIN_VALUE, Integer.MIN_VALUE);
	}

	/**
	 * Checks if facing an obstacle in a 15 cm, 90° cone.
	 * Ignores obstacles nearer than 7.5cm.
	 * 
	 * @return true, if an obstacle is marked within the area
	 */
	public boolean isBlocked() {
		// Construct cone
		final Arc2D arc = new Arc2D.Double();
		arc.setArcByCenter(data.getPositionX(), data.getPositionY(), 25, -data.getHeading() - 45, 90, Arc2D.PIE);
		final Arc2D arc2 = new Arc2D.Double();
		arc2.setArcByCenter(data.getPositionX(), data.getPositionY(), 7.5f, -data.getHeading() - 45, 90, Arc2D.PIE);

		final Area area = new Area(arc2);
		area.subtract(new Area(arc));
		return map.isObstacled(area);
	}

	/**
	 * Calls a method of the {@link AStar} do determine whether the given coordinates
	 * are marked as free.
	 * Does not discretize the coordinates.
	 * 
	 * @param x: the x-coordinate to be checked
	 * @param y: the y-coordinate to be checked
	 * @return true, if the point is marked as free in the MapData
	 */
	public boolean isFree(float x, float y) {
		return alg.isFree((int)x, (int)y);
	}

	public boolean isReachable2(int xStart, int yStart, int xTarget, int yTarget) {
		Point p1, p2, p3, p4, p5, p6, p7, p8;
		p1 = Navigator.calcSquare(xStart, yStart, data.getHeading() + 90, MAP_SQUARE_LENGTH);
		p2 = Navigator.calcSquare(xStart, yStart, data.getHeading() + 90, 2 * MAP_SQUARE_LENGTH);
		p3 = Navigator.calcSquare(xStart, yStart, data.getHeading() - 90, MAP_SQUARE_LENGTH);
		p4 = Navigator.calcSquare(xStart, yStart, data.getHeading() - 90, 2 * MAP_SQUARE_LENGTH);
		p5 = Navigator.calcSquare(xTarget, yTarget, data.getHeading() + 90, MAP_SQUARE_LENGTH);
		p6 = Navigator.calcSquare(xTarget, yTarget, data.getHeading() + 90, 2 * MAP_SQUARE_LENGTH);
		p7 = Navigator.calcSquare(xTarget, yTarget, data.getHeading() - 90, MAP_SQUARE_LENGTH);
		p8 = Navigator.calcSquare(xTarget, yTarget, data.getHeading() - 90, 2 * MAP_SQUARE_LENGTH);

		if (!isReachableLine(xStart, yStart, xTarget, yTarget)) {
			return false;
		}
		if (!isReachableLine((int)p1.getX(), (int)p1.getY(), (int)p5.getX(), (int)p5.getY())) {
			return false;
		}
		if (!isReachableLine((int)p2.getX(), (int)p2.getY(), (int)p6.getX(), (int)p6.getY())) {
			return false;
		}
		if (!isReachableLine((int)p3.getX(), (int)p3.getY(), (int)p7.getX(), (int)p7.getY())) {
			return false;
		}
		if (!isReachableLine((int)p4.getX(), (int)p4.getY(), (int)p8.getX(), (int)p8.getY())) {
			return false;
		}
		return true;

	}

	private boolean isReachableLine(int xStart, int yStart, int xTarget, int yTarget) {
		int xCorr, yCorr;
		if (xTarget - xStart > 0) {
			xCorr = MAP_SQUARE_LENGTH;
		} else {
			xCorr = -MAP_SQUARE_LENGTH;
		}
		if (yTarget - yStart > 0) {
			yCorr = MAP_SQUARE_LENGTH;
		} else {
			yCorr = -MAP_SQUARE_LENGTH;
		}
		// asking if only one tile is to check:
		if (xStart == xTarget && yStart == yTarget) {
			return !map.isObstacle(xStart, yStart);
		}
		// checking if x or y coordinate doesn't change
		if (xStart == xTarget || yStart == yTarget) {
			if (xStart == xTarget) {
				// x doesn't change:

				while (yStart != yTarget + yCorr) {
					if (map.isObstacle(xStart, yStart)) {
						return false;
					}
					yStart += yCorr;
				}
				return true;
			}
			// y doesn't change:
			while (xStart != xTarget + xCorr) {
				if (map.isObstacle(xStart, yStart)) {
					return false;
				}
				xStart += xCorr;
			}
			return true;
		}
		// checking everything else:
		double m, b;
		m = (yTarget - yStart) / (xTarget - xStart);
		b = yStart - m * xStart;
		while (xStart != xTarget + xCorr) {
			final int yHeight = discrete(m * (xStart + xCorr) + b) - yStart;
			final int savedY = yStart;
			while (yStart != savedY + yHeight) {
				if (map.isObstacle(xStart, yStart)) {
					return false;
				}
				yStart += yCorr;
			}
			if (map.isObstacle(xStart, yStart)) {
				return false;
			}
			xStart += xCorr;
		}
		return true;
	}

	/**
	 * Adds all tiles contained in a shape to the map.
	 */
	private void addAllTiles(Shape shape, boolean isObstacle, boolean defective) {
		final Rectangle bounds = shape.getBounds();

		final int minX = discrete(bounds.getMinX()), maxX = discrete(bounds.getMaxX()), minY = discrete(bounds.getMinY()), maxY = discrete(bounds.getMaxY());

		for (int x = minX; x <= maxX; x += MAP_SQUARE_LENGTH) {
			for (int y = minY; y <= maxY; y += MAP_SQUARE_LENGTH) {
				if (shape.contains(x, y)) {
					map.append(x, y, isObstacle, defective);
				}
			}
		}
	}
}
