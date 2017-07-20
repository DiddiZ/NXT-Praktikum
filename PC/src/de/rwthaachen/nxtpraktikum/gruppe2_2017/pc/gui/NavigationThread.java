package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;

import java.awt.Point;



public class NavigationThread extends Thread{
	private Navigator navi;
	private ApplicationHandler appHandler;
	private final float xTarget, yTarget;
	private double xNext, yNext;
	private boolean running;
	
	public NavigationThread(Navigator navi_init, ApplicationHandler app_init, float x_init, float y_init){
		this.setDaemon(true);
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
		//System.out.println("NaviThread started with "+xTarget+", "+yTarget);
		if(xTarget == Float.MAX_VALUE && yTarget == Float.MAX_VALUE){
			while(running){
				if(navi.isFree(navi.getNXTData().getPositionX(), navi.getNXTData().getPositionX())){
					appHandler.showBlockedSign(false);
				}
				else{		
					appHandler.showBlockedSign(true);		
					appHandler.stopMoving();
					//appHandler.driveToMethod(navi.getNXTData().getPositionX(), navi.getNXTData().getPositionY());
				}
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
					//System.out.println("Calculating next Step...");
					appHandler.stopMoving();
					Point nextMove = navi.getNextPoint(xTarget, yTarget);
					//System.out.println("Drive to: "+nextMove);
					handleNewTarget((float) nextMove.getX(), (float)nextMove.getY());
				}
				if(navi.isFree(navi.getNXTData().getPositionX(), navi.getNXTData().getPositionY())){
					appHandler.showBlockedSign(false);
				}
				else{ 
					appHandler.showBlockedSign(true);
					appHandler.stopMoving();
					handleNewTarget(navi.getNXTData().getPositionX(), navi.getNXTData().getPositionY());
				}
				
				try {
					Thread.sleep(200);
				} catch (final InterruptedException e) {
					System.out.println("NavigationThread was interrupted while sleeping.");
				}
			}
		}	
	}
	
	public void handleNewTarget(float nextX, float nextY){
		xNext = nextX;
		yNext = nextY;
		appHandler.turnToCoordinate(nextX, nextY);
		appHandler.startMoving();
	}
}
