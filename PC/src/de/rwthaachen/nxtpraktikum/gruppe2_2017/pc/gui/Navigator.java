package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.MapData;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.NXTData;
/**
 * 
 * @author Fabian
 *
 */
public class Navigator{
	
	private NXTData data;
	private MapData map;
	@SuppressWarnings("unused")
	private UI gui;
	
	public static final int MAP_SQUARE_LENGTH = 5;
	public static final float NAVIGATION_DISTANCE_EPSILON = 5f;
	public static final float NAVIGATION_HEADING_EPSILON = 10f;
	// The following two Parameters are for automatic detection of free squares in front of NXT
	public static final float OBSTACLE_DETECTION_RANGE = 30f; 
	public static final float OBSTACLE_DETECTION_WIDTH = 10f;
	
	/**
	 * This is the constructor for the Navigator class. 
	 * @param data An instance of NXTData
	 * @param appHandler An instance of ApplicationHandler
	 * @param gui An instance of UI
	 */
	public Navigator(NXTData data, UI gui){
		this.data = data;
		this.map = new MapData(0,0, false);
		this.gui = gui;
		//generateRandomMap(); //use for testing purposes
	}
	
	//set and get Methods
	/**
	 * Getter method for map
	 * @return returns the MapData map stored in Navigator
	 */
	public MapData getMapData(){
		return this.map;
	}
	/**
	 * Getter method for data
	 * @return returns the NXTData data stored in Navigator
	 */
	public NXTData getNXTData(){
		return this.data;
	}
	/**
	 * This method returns the discrete identification of the square the coordinate is in.
	 * @param param the coordinate to make discrete
	 * @return the identification of the square the coordinate was in
	 */
	public int  discrete(double param){
		return (int) param / MAP_SQUARE_LENGTH * MAP_SQUARE_LENGTH;
	}
	
	/**
	 * This Method calculates a new Object of MapData which is p_distance cm from a current Position away, looking in direction p_angle.
	 * @param current_posX Position X of current Position
	 * @param current_posY Position Y of current Position
	 * @param p_angle direction of new MapData
	 * @param p_distance distance of new MapData to current Position
	 * @param obstacle true if newMapData is an obstacle, else false
	 * @return a new MapData object which is p_distance cm away from the current Position, looking in direction p_angle, and which has the property of parameter obstacle.
	 */
	public MapData calcSquare(float current_posX, float current_posY, float p_angle, float p_distance, boolean obstacle){
		double distX = -Math.cos(((double)(p_angle)-90.0)/180.0*Math.PI)*p_distance;
		double distY = -Math.sin(((double)(p_angle)-90.0)/180.0*Math.PI)*p_distance;
		return new MapData(discrete((double)current_posX + distX), discrete((double)current_posY + distY), obstacle);
	}
	
	
	/**
	 * This method is for testing purposes and draws squares randomly distributed on the map within a set range.
	 */
	private void generateRandomMap(){
		for(int i= 0; i<300; i++){
			map.append(new MapData((int)(Math.random()*300)-150,(int)(Math.random()*300)-150,i%2==0));
		}
		
	}
	
	//renewed Navigation Methods:
	/*
	 * TODO: Renew moveTo() method.
	 * By now there is no detection if the NXT finished moving/turning or not. Right now there is just a set wait time between moving and turning, which is unusable for more than one command in a row.
	 * Idea: check if NXT has fulfilled its Task. If it hasn't done this after a timeout time, send the Navigation command again. Only if the target reaches its destination, the next moveTo command can be sent.
	 * 
	 */
	
	/**
	 * This method calculates the hypotenuse for two cathetes.
	 * @param diffX cathetus number one
	 * @param diffY cathetus number two
	 * @return the length of the hypotenuse in cm
	 */
	public float calcDistance(float diffX, float diffY){
		return (float)Math.sqrt((double)((diffY)*(diffY)+(diffX)*(diffX)));
	}
	
	/**
	 * This method checks if the NXT has reached a certain position within an epsilon stored in NAVIGATION_DISTANCE_EPSILON
	 * @param posX x coordinate for destination to check
	 * @param posY y coordinate for destination to check
	 * @return true if NXT is within epsilon of the destination
	 */
	public boolean reachedPosition(float posX, float posY){
		float diffX = data.getPositionX() - posX;
		float diffY = data.getPositionY() - posY;
		if ( calcDistance(diffX, diffY) < NAVIGATION_DISTANCE_EPSILON){
			return true;
		}else{
			return false;
		}
	}
	
	//TODO: Implement something useful below here
		public boolean reachedHeading(float targetHeading){
			return true;
		}
	
	public void navigateTo(float posX, float posY){
		
	}
	
	

}
