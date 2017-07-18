package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.CommunicatorPC;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.NXTData;


public class NavigationThread extends Thread{
	private Navigator navi;
	private CommunicatorPC communicator;
	private final float xTarget, yTarget;
	
	public NavigationThread(Navigator navi_init, CommunicatorPC communicator_init, float x_init, float y_init, NXTData data_init){
		this.navi = navi_init;
		this.communicator = communicator_init;
		this.xTarget = x_init;
		this.yTarget = y_init;
	}
	
	public void run(){
		if(xTarget == Float.MAX_VALUE && yTarget == Float.MAX_VALUE){
			while(true){
				//TODO: check for obstacles in close range
				//TODO: eventually set constant speed to 0
				
				try {
					Thread.sleep(200);
				} catch (final InterruptedException e) {
					System.out.println("NavigationThread was interrupted while sleeping.");
				}
			}
		}
		else{
			while(!navi.reachedPosition(xTarget, yTarget)){
		
				//TODO: calc next move command in navi
				//TODO: send moveTo command
			
				//TODO: check for obstacles in close range
			
				try {
					Thread.sleep(200);
				} catch (final InterruptedException e) {
					System.out.println("NavigationThread was interrupted while sleeping.");
				}
			}
		}	
	}
}
