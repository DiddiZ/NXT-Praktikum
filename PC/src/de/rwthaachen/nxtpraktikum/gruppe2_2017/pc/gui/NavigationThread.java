package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;

import java.awt.Point;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.NXTData;


public class NavigationThread extends Thread{
	private Navigator navi;
	private ApplicationHandler appHandler;
	private final float xTarget, yTarget;
	private double xNext, yNext;
	private boolean running;
	
	public NavigationThread(Navigator navi_init, ApplicationHandler app_init, float x_init, float y_init){
		this.navi = navi_init;
		this.appHandler = app_init;
		this.xTarget = x_init;
		this.yTarget = y_init;
		xNext = Double.MAX_VALUE;
		yNext = Double.MAX_VALUE;
		running = true;
	}
	
	public void setRunning(boolean runSet){
		this.running = runSet;
	}
	
	public void run(){
		if(xTarget == Float.MAX_VALUE && yTarget == Float.MAX_VALUE){
			while(running){
				appHandler.showBlockedSign(!navi.isFree(navi.getNXTData().getPositionX(), navi.getNXTData().getPositionX()));
				appHandler.stopMoving();
				
				try {
					Thread.sleep(200);
				} catch (final InterruptedException e) {
					System.out.println("NavigationThread was interrupted while sleeping.");
				}
			}
		}
		else{
			while(running && !navi.reachedPosition(xTarget, yTarget)){
				if((xNext==Double.MAX_VALUE && yNext==Double.MAX_VALUE)|| (navi.reachedPosition((float)xNext, (float)yNext))){
					Point nextMove = navi.getNextPoint(xTarget, yTarget);
					xNext = (float) nextMove.getX();
					yNext = (float) nextMove.getY();
					appHandler.driveToMethod(xNext, yNext);
				}
				appHandler.showBlockedSign(!navi.isFree(navi.getNXTData().getPositionX(), navi.getNXTData().getPositionX()));
				
				try {
					Thread.sleep(200);
				} catch (final InterruptedException e) {
					System.out.println("NavigationThread was interrupted while sleeping.");
				}
			}
		}	
	}
}
