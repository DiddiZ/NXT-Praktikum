package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.MapData;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.NXTData;

public class Navigator extends Thread{
	
	private NXTData data;
	private ApplicationHandler appHandler;
	private MapData map;
	private UI gui;
	
	public static final int MAP_SQUARE_LENGTH = 5;
	private static final float NAVIGATION_DISTANCE_EPSILON = 5f;
	private static final float NAVIGATION_HEADING_EPSILON = 10f;
	
	public Navigator(NXTData data, ApplicationHandler appHandler, UI gui){
		this.data = data;
		this.appHandler = appHandler;
		this.map = new MapData(0,0, false);
		this.gui = gui;
		generateRandomMap();
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
