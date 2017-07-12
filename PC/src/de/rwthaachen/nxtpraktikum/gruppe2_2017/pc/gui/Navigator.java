package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.MapData;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.NXTData;

public class Navigator{
	
	private NXTData data;
	private ApplicationHandler appHandler;
	private MapData map;
	private UI gui;
	
	public static final int MAP_SQUARE_LENGTH = 5;
	private static final float NAVIGATION_DISTANCE_EPSILON = 5f;
	private static final float NAVIGATION_HEADING_EPSILON = 10f;
	// The following two Parameters are for automatic 
	private static final float OBSTACLE_DETECTION_RANGE = 30f; 
	private static final float OBSTACLE_DETECTION_WIDTH = 10f;
	
	public Navigator(NXTData data, ApplicationHandler appHandler, UI gui){
		this.data = data;
		this.appHandler = appHandler;
		this.map = new MapData(0,0, false);
		this.gui = gui;
		//generateRandomMap();
	}
	
	public MapData getMapData(){
		return map;
	}
	
	public NXTData getNXTData(){
		return this.data;
	}
	public int  discrete(double param){
		return (int) param / MAP_SQUARE_LENGTH * MAP_SQUARE_LENGTH;
	}
	
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
	/**
	 * This Method calculates a new Object of MapData which is p_distance cm from a current Position away, looking in direction p_angle.
	 * @param current_posX Position X of current Position
	 * @param current_posY Position Y of current Position
	 * @param p_angle direction of new MapData
	 * @param p_distance distance of new MapData to current Position
	 * @param obstacle true if newMapData is an obstacle, else false
	 * @return
	 */
	public MapData calcSquare(float current_posX, float current_posY, float p_angle, float p_distance, boolean obstacle){
		double distX = -Math.cos(((double)(p_angle)-90.0)/180.0*Math.PI)*p_distance;
		double distY = -Math.sin(((double)(p_angle)-90.0)/180.0*Math.PI)*p_distance;
		return new MapData(discrete((double)current_posX + distX), discrete((double)current_posY + distY), obstacle);
	}
	
	
	
	private void generateRandomMap(){
		for(int i= 0; i<300; i++){
			map.append(new MapData((int)(Math.random()*300)-150,(int)(Math.random()*300)-150,i%2==0));
		}
		
	}
	
	public float calcDistance(float diffX, float diffY){
		return (float)Math.sqrt((double)((diffY)*(diffY)+(diffX)*(diffX)));
	}
	
	public void navigateTo(float posX, float posY){
		
	}
	
	

}
