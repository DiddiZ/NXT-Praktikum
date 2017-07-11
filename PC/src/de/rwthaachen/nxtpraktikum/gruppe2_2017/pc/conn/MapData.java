package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn;

/**
 * 
 * @author Justus
 *
 * This is a dynamic data structure to save all information 
 * gained by the NXT about his environment
 *
 */


public class MapData {
	
	private long x, y;
	private boolean isObstacle, isChecked;
	public MapData next, previous;
	
	
	/**
	 * The constructor for a MapData object: 
	 * @param x_init: the x-coordinate of the MapData
	 * @param y_init: the y-coordinate of the MapData
	 * @param obstacle_init: set to true, if the MapData marks an obstacle; otherwise, it will be a free area
	 */
	public MapData(long x_init, long y_init, boolean obstacle_init){
		this.x = x_init;
		this.y = y_init;
		this.isObstacle = obstacle_init;
		this.next = null;
		this.previous = null;
		this.isChecked = false;
	}
	
	
	/**
	 * This method will add a new MapData to the existing Data (sorted)
	 * @param newData: the new MapData that has to be added
	 */
	public void append(MapData newData){
		//TODO: implement something here
	}
	
	
	/**
	 * This method searches in the Data whether the coordinates are marked as an obstacle or free area
	 * @param x_test: The x-coordinate of the Data the method searches for
	 * @param y_test: The y-coordinate of the Data the method searches for
	 * @return: true, if a MapData exists that marks the Coordinates as an obstacle
	 * 
	 * TODO: decide, whether unknown areas are counted as obstacle or free area.
	 */
	public boolean isObstacle(long x_test, long y_test){
		//TODO: implement something here
		return false;
	}
	
	
	/**
	 * This method searches for an entry in the Data with fitting coordinates
	 * @param x_test: The x-coordinate of the Data the method searches for
	 * @param y_test: The y-coordinate of the Data the method searches for
	 * @return: true, if an entry with fitting coordinates exists in the MapData
	 */
	public boolean isKnown(long x_test, long y_test){
		//TODO: implement something here
		return true;
	}
	
	/**
	 * This method returns the x-coordinate of the MapData
	 * @return the x-coordinate of the MapData
	 */
	public long getX(){
		return this.x;
	}
	
	
	/**
	 * This method returns the y-coordinate of the MapData
	 * @return the y-coordinate of the MapData
	 */
	public long getY(){
		return this.y;
	}
	
	
	/**
	 * This method returns whether the Data is marked as an obstacle
	 * @return true, if this Data marks an obstacle, otherwise returns false
	 */
	public boolean getIsObstacle(){
		return this.isObstacle;
	}
	
	/**
	 * This method returns a MapData to fitting coordinates
	 * @param x_c: the x-coordinate of the MapData searched for
	 * @param y_c: the y-coordinate of the MapData searched for
	 * @return a MapData with fitting coordinates; null if there is no fitting MapData
	 */
	public MapData getMapData(long x_c, long y_c){
		return this;
	}
}