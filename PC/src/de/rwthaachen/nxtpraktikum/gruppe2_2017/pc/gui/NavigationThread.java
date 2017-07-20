package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;

import java.awt.Point;



public class NavigationThread extends Thread{
	private Navigator navi;
	private ApplicationHandler appHandler;
	private final float xTarget, yTarget;
	private double xNext,yNext,xOld,yOld;
	private boolean running;
	private boolean finalMove;
	private int idle;
	private static final int STANDARD_IDLE_TIME = 20;
	
	public NavigationThread(Navigator navi_init, ApplicationHandler app_init, float x_init, float y_init){
		this.setDaemon(true);
		this.navi = navi_init;
		this.appHandler = app_init;
		this.xTarget = x_init;
		this.yTarget = y_init;
		xNext = Double.MAX_VALUE;
		yNext = Double.MAX_VALUE;
		xOld = navi.getNXTData().getPositionX();
		yOld = navi.getNXTData().getPositionY();
		running = true;
		finalMove = true;
		idle = 0;
	}
	
	public void setRunning(boolean runSet){
		this.running = runSet;
		if(!runSet)this.finalMove = false;
		
	}
	
	public void run(){
		System.out.println("NaviThread started with "+xTarget+", "+yTarget);
		/*if(xTarget == Float.MAX_VALUE && yTarget == Float.MAX_VALUE){
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
		else{*/
			/*while(navi.getNXTData().getBalancing()&&running && !navi.reachedPosition(xTarget, yTarget)){
				//if((xNext==Double.MAX_VALUE && yNext==Double.MAX_VALUE)|| (navi.reachedPosition((float)xNext, (float)yNext))){
					System.out.println("Calculating next Step...");
					//appHandler.stopMoving();
					Point nextMove = navi.getNextPoint(xTarget, yTarget);
					System.out.println("Drive to: "+nextMove);
					if((int)nextMove.getX()==Integer.MIN_VALUE && (int)nextMove.getY()==Integer.MIN_VALUE){
						running = false;
						System.out.println("Target can not be reached.");
						break;
					}
					else{
						handleNewTarget((float) nextMove.getX(), (float)nextMove.getY());
					}
				//}
				if(navi.isFree(navi.getNXTData().getPositionX(), navi.getNXTData().getPositionY())){
					appHandler.showBlockedSign(false);
				}
				else{ 
					appHandler.showBlockedSign(true);
					appHandler.stopMoving();
					handleNewTarget(navi.getNXTData().getPositionX(), navi.getNXTData().getPositionY());
				}
				
				try {
					Thread.sleep(1000);
				} catch (final InterruptedException e) {
					System.out.println("NavigationThread was interrupted while sleeping.");
				}
			}*/
		
		while(navi.getNXTData().getBalancing()&&running && !navi.reachedPosition(xTarget, yTarget)){
			
			if(idle == 0){
				//recalculating next destination:
				System.out.println("Calculating next Step...");
				Point nextMove = navi.getNextPoint(xTarget, yTarget);
				xNext = nextMove.getX();
				yNext = nextMove.getY();
				xOld = navi.getNXTData().getPositionX();
				yOld = navi.getNXTData().getPositionY();
				System.out.println("Drive to: "+nextMove);
				if((int)nextMove.getX()==Integer.MIN_VALUE && (int)nextMove.getY()==Integer.MIN_VALUE){
					running = false;
					System.out.println("Target can not be reached.");
					finalMove = false;
					break;
				}else{
					System.out.println("Calculation successful");
					handleNewTarget((float) nextMove.getX(), (float)nextMove.getY());
				}
				//increasing idle timer:
				idle = STANDARD_IDLE_TIME;
			}
			//Collision detection:
			if(navi.isFree(navi.getNXTData().getPositionX(), navi.getNXTData().getPositionY())){
				appHandler.showBlockedSign(false);
			}
			else{ 
				appHandler.showBlockedSign(true);
				appHandler.stopMoving();
				handleNewTarget(navi.getNXTData().getPositionX(), navi.getNXTData().getPositionY());
			}
			//check if xNext, yNext is reached?
			if(Navigator.calcDistance((float)(xOld-navi.getNXTData().getPositionX()), (float)(yOld-navi.getNXTData().getPositionY())) >= Navigator.calcDistance((float)(xOld-xNext),  (float)(yOld-yNext))){
				idle = 0;
				appHandler.stopMoving();
			}
			
			//decreasing idle timer:
			if(idle > 0){
				idle--;
			}else{
				idle = 0;
			}
			//Thread idle time:
			try {
				Thread.sleep(100);
			} catch (final InterruptedException e) {
				System.out.println("NavigationThread was interrupted while sleeping.");
			}
			
		}
		
		//stopping movement if Thread is finished:
		appHandler.stopMoving();
		//sending a final, correcting moveTo command
		//not sending final Move command if Thread stopped
		if(finalMove){
			//little bit of idle time, so NXT wont fall
			try {
				Thread.sleep(500);
			} catch (final InterruptedException e) {
				System.out.println("NavigationThread was interrupted while sleeping.");
			}
			//sending final movement command:
			
			float currentHeading = navi.getNXTData().getHeading();
			appHandler.driveToMethod(xTarget, yTarget);
			appHandler.turnAbsoluteMethod(currentHeading);
		}
		
			
		//}	
	}
	
	
	
	public void handleNewTarget(float nextX, float nextY){
		xNext = nextX;
		yNext = nextY;
		appHandler.turnToCoordinate(nextX, nextY);
		appHandler.startMoving();
	}
}
