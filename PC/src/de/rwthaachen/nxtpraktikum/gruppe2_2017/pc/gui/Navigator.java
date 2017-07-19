package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.aStarAlg.PointNode;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.aStarAlg.aStarAlg;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.MapData;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.NXTData;

/**
 * @author Fabian, Robin
 */
public final class Navigator
{

	private final NXTData data;
	private MapData map;
	@SuppressWarnings("unused")
	private final UI gui;

	public static final int MAP_SQUARE_LENGTH = 5;
	public static final float NAVIGATION_DISTANCE_EPSILON = 5f;
	public static final float NAVIGATION_HEADING_EPSILON = 10f;
	// The following two Parameters are for automatic detection of free squares in front of NXT
	public static final float OBSTACLE_DETECTION_RANGE = 30f;
	public static final float OBSTACLE_DETECTION_WIDTH = 10f;
	private final aStarAlg alg;

	private static final int SAFETY_DISTANCE = 10;
	private static final float US_SPREAD = 15;
	
	/**
	 * This is the constructor for the Navigator class.
	 *
	 * @param data An instance of NXTData
	 * @param appHandler An instance of ApplicationHandler
	 * @param gui An instance of UI
	 */
	public Navigator(NXTData data, UI gui) {
		this.data = data;
		map = new MapData();
		this.gui = gui;
		this.alg = new aStarAlg(map);
		// generateRandomMap(); //use for testing purposes
	}

	// set and get Methods
	/**
	 * Getter method for map
	 *
	 * @return returns the MapData map stored in Navigator
	 */
	public MapData getMapData() {
		return map;
	}
	
	public void resetMapData(){
		map = new MapData();
	}

	/**
	 * Getter method for data
	 *
	 * @return returns the NXTData data stored in Navigator
	 */
	public NXTData getNXTData() {
		return data;
	}

	/**
	 * This method returns the discrete identification of the square the coordinate is in.
	 *
	 * @param param the coordinate to make discrete
	 * @return the identification of the square the coordinate was in
	 */
	public static int discrete(double param) {
		return (int)param / MAP_SQUARE_LENGTH * MAP_SQUARE_LENGTH;
	}

	/**
	 * This Method calculates a new Object of MapData which is p_distance cm from a current Position away, looking in direction p_angle.
	 *
	 * @param current_posX Position X of current Position
	 * @param current_posY Position Y of current Position
	 * @param p_angle direction of new MapData
	 * @param p_distance distance of new MapData to current Position
	 * @param obstacle true if newMapData is an obstacle, else false
	 * @return a new Point which is p_distance cm away from the current Position, looking in direction p_angle, and which has the property of parameter obstacle.
	 */
	public static Point calcSquare(float current_posX, float current_posY, float p_angle, float p_distance) {
		final double distX = -Math.cos((p_angle - 90.0) / 180.0 * Math.PI) * p_distance;
		final double distY = -Math.sin((p_angle - 90.0) / 180.0 * Math.PI) * p_distance;
		return new Point(discrete(current_posX + distX), discrete(current_posY + distY));
	}

	// renewed Navigation Methods:
	/*
	 * TODO: Renew moveTo() method.
	 * By now there is no detection if the NXT finished moving/turning or not. Right now there is just a set wait time between moving and turning, which is unusable for more than one command in a row.
	 * Idea: check if NXT has fulfilled its Task. If it hasn't done this after a timeout time, send the Navigation command again. Only if the target reaches its destination, the next moveTo command can be sent.
	 */

	/**
	 * This method calculates the hypotenuse for two cathetes.
	 *
	 * @param diffX cathetus number one
	 * @param diffY cathetus number two
	 * @return the length of the hypotenuse in cm
	 */
	public static float calcDistance(float diffX, float diffY) {
		return (float)Math.sqrt(diffY * diffY + diffX * diffX);
	}

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
		if (calcDistance(diffX, diffY) < NAVIGATION_DISTANCE_EPSILON) {
			return true;
		}
		return false;
	}

	// TODO: Implement something useful below here
	/*@SuppressWarnings({"static-method", "unused"})
	public boolean reachedHeading(float targetHeading) {
		return true;
	}

	@SuppressWarnings("unused")
	public void navigateTo(float posX, float posY) {

	}*/


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
	 * @author Justus, Fabian
	 * 
	 * this method calculates the next target for the NXT
	 * @param xTarget: The x-coordinate of the final target
	 * @param yTarget: The y-coordinate of the final target
	 * @return the next Point the NXT can drive to by using only one command
	 */
	
	public Point getNextPoint(float xTarget, float yTarget){
		Point destination = new Point(discrete(xTarget), discrete(yTarget));
		Point position = new Point(discrete(data.getPositionX()), discrete(data.getPositionY()));
		
		PointNode chain = alg.aStarAlgorithm(position, destination);
		if(chain!=null){
			boolean isReachable = true;
			int indexTest = chain.getChainLength();
			//int indexOld = indexTest+1;
			while(isReachable){
				indexTest--;
				isReachable = this.isReachable((int)position.getX(), (int)position.getY(), (int)chain.getPred(indexTest).getPoint().getX(), (int)chain.getPred(indexTest).getPoint().getY());
			}
			indexTest++;
			return chain.getPred(indexTest).getPoint();
		}
		
		return position;
	}
	
	
	private boolean isReachable(int xStart, int yStart, int xTarget, int yTarget){
		//double l = Math.sqrt(Math.pow(xTarget-xStart, 2)+Math.pow(yTarget-yStart, 2));
		double a = xTarget-xStart;
		double b = yTarget-yStart;
		double alpha = Math.toDegrees(Math.asin(a/b));
		double steigung = Math.tan(alpha)*5;
		
		int currenty = yStart;
		for(int j=yStart-10; j<=yStart+10; j=j+5){
			currenty = j; 
			for(int i = xStart; i<=xTarget+5; i = i+5){
				
				if(map.isObstacle(i, currenty)){
					return false;
				}
				currenty= (int)(currenty + steigung);
			}
		}
		
		return true;
		
	}
	

	/**
	 * Adds all tiles contained in a shape to the map.
	 */
	private void addAllTiles(Shape shape, boolean isObstacle, boolean defective) {
		final Rectangle bounds = shape.getBounds();

		final int minX = discrete(bounds.getMinX()), maxX = discrete(bounds.getMaxX()), minY = discrete(bounds.getMinY()), maxY = discrete(bounds.getMaxY());

		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				if (shape.contains(x, y)) {
					map.append(x, y, isObstacle, defective);
				}
			}
		}
	}
}
