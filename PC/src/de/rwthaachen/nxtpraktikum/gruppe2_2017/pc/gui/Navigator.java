package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.MapData;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.NXTData;

public class Navigator extends Thread{
	
	private NXTData data;
	private ApplicationHandler appHandler;
	private MapData map;
	
	public static final int MAP_SQUARE_LENGTH = 5;
	private static final float NAVIGATION_DISTANCE_EPSILON = 5f;
	private static final float NAVIGATION_HEADING_EPSILON = 10f;
	
	public Navigator(NXTData data, ApplicationHandler appHandler){
		this.data = data;
		this.appHandler = appHandler;
		this.map = new MapData(0,0, false);
	}
	
	public MapData getMapData(){
		return map;
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
	
	//public boolean reachedHeading(float targetHeading){
		
	//}
	
	public float calcDistance(float diffX, float diffY){
		return (float)Math.sqrt((double)((diffY)*(diffY)+(diffX)*(diffX)));
	}
	
	public void navigateTo(float posX, float posY){
		
	}
	
	

}
