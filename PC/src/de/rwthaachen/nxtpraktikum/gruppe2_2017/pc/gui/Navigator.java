package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.NXTData;

public class Navigator extends Thread{
	
	private NXTData data;
	private ApplicationHandler appHandler;
	
	public static final int MAP_SQUARE_LENGTH = 5;
	
	public Navigator(NXTData data, ApplicationHandler appHandler){
		this.data = data;
		this.appHandler = appHandler;
	}
	
	public int  discrete(double param){
		return (int) param / MAP_SQUARE_LENGTH * MAP_SQUARE_LENGTH;
	}
	
	
	

}
