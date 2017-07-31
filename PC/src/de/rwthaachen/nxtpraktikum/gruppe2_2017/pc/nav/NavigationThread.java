package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.nav;

import java.awt.Point;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui.ApplicationHandler;

/**
 * This class is used to navigate the NXT without influencing other calculations and communication.
 * The class is designed to navigate the NXT based on the MapData to a certain target
 * and avoid collisions with obstacles.
 * Extends the class {@link Thread} to run it in an own Thread.
 * 
 * @author Justus, Fabian
 */

public class NavigationThread extends Thread
{
	private final Navigator navi;
	private final ApplicationHandler appHandler;
	private final float xTarget, yTarget;
	private double xNext, yNext, xOld, yOld;
	private boolean running;
	private boolean finalMove;
	private int idle;
	private static final int STANDARD_IDLE_TIME = 10000000;
	
	/**
	 * The constructor of the NavigationThread. Assigns important attributes and
	 * sets the Thread to Daemon to let the JVM exit if this should be the only thread running.
	 * Sets the coordinates of the next calculation-step initially to MAX_VALUE
	 * and the idle timer to 0.
	 * 
	 * @param navi_init: The Navigator to be assigned to this NavigationThread
	 * @param app_init: The ApplicationHandler to be assigned to this NavigationThread
	 * @param x_init: The x-coordinate of the target this class navigates to
	 * @param y_init: The y-coordinate of the target this class navigates to
	 */
	public NavigationThread(Navigator navi_init, ApplicationHandler app_init, float x_init, float y_init) {
		setDaemon(true);
		navi = navi_init;
		appHandler = app_init;
		xTarget = x_init;
		yTarget = y_init;
		xNext = Double.MAX_VALUE;
		yNext = Double.MAX_VALUE;
		xOld = navi.getNXTData().getPositionX();
		yOld = navi.getNXTData().getPositionY();
		running = true;
		finalMove = true;
		idle = 0;
	}

	/**
	 * Sets the running boolean and eventually updates the finalMove boolean
	 * @param runSet: The value the running boolean will be set to
	 */
	public void setRunning(boolean runSet) {
		running = runSet;
		if (!runSet) {
			finalMove = false;
		}

	}

	@Override
	/**
	 * This method is responsible for the navigation of the NXT.
	 * Once the Thread is started, a corresponding message is printed.
	 * The Thread is running as long as its boolean running equals true,
	 * the target has not been reached and the NXT is balancing.
	 * 
	 * Every time the idle timer equals 0, the method calculates the next destination.
	 * For this, a method of the {@link Navigator} is called and the destination is saved.
	 * The method catches cases in which the target cannot be reached.
	 * After the calculation, another method is called to handle the new destination
	 * and the idle timer is set to its maximum value.
	 * 
	 * The method proceeds to handle blocked positions and reached calculated targets
	 * by setting the idle timer to 0 and stopping the movement.
	 * Updates the idle timer and sleeps for 100 ms.
	 * 
	 * Finally stops the movement and, if the thread was not stopped by an external
	 * command, send a moveTo-command to the NXT to correct inaccuracies. 
	 */
	public void run() {
		System.out.println("NaviThread started with " + xTarget + ", " + yTarget);
		while (navi.getNXTData().getBalancing() && running && !navi.reachedPosition(xTarget, yTarget)) {
			if (idle == 0) {
				System.out.println("Calculating next Step...");
				final Point nextMove = navi.getNextPoint(xTarget, yTarget);
				xNext = nextMove.getX();
				yNext = nextMove.getY();
				xOld = navi.getNXTData().getPositionX();
				yOld = navi.getNXTData().getPositionY();
				System.out.println("Drive to: " + nextMove);
				if ((int)nextMove.getX() == Integer.MIN_VALUE && (int)nextMove.getY() == Integer.MIN_VALUE) {
					running = false;
					System.out.println("Target can not be reached.");
					finalMove = false;
					break;
				}
				System.out.println("Calculation successful");
				handleNewTarget((float)nextMove.getX(), (float)nextMove.getY());
				idle = STANDARD_IDLE_TIME;
			}
			
			// Collision detection: instantly stop the movement and wait for recalculation
			if (navi.isBlocked()) {
				idle = 0;
				System.out.println("Recalculating because the way is blocked");
				appHandler.stopMoving();
			}
			// Destination detection: If the next destination is reached, instantly stop the movement and wait for recalculation
			if (Math.hypot(xOld - navi.getNXTData().getPositionX(), yOld - navi.getNXTData().getPositionY()) >= Math.hypot(xOld - xNext, yOld - yNext)) {
				idle = 0;
				appHandler.stopMoving();
			}

			// decreasing idle timer:
			if (idle > 0) {
				idle--;
			} else {
				idle = 0;
			}
			try {
				Thread.sleep(100);
			} catch (final InterruptedException e) {
				System.out.println("NavigationThread was interrupted while sleeping.");
			}
		}
		System.out.print("############final move###########");
		// stopping movement if thread is finished:
		appHandler.stopMoving();
		// sending a final, correcting moveTo command if the thread has not been stopped
		if (finalMove) {
			// waiting a little bit of idle time, so NXT will not fall
			try {
				Thread.sleep(500);
			} catch (final InterruptedException e) {
				System.out.println("NavigationThread was interrupted while sleeping.");
			}
			// sending final movement command:
			final float currentHeading = navi.getNXTData().getHeading();
			appHandler.driveToMethod(xTarget, yTarget);
			appHandler.turnAbsoluteMethod(currentHeading);
		}
		System.out.println("############ no more movement messages ##########");
	}

	/**
	 * This method handles a next destination for the navigation
	 * by updating the corresponding attributes and
	 * calling methods of the {@link ApplicationHandler} to turn the NXT to the target
	 * and start a constant movement.
	 * 
	 * @param nextX: The x-coordinate of the next destination the NXT is supposed to drive to
	 * @param nextY: The y-coordinate of the next destination the NXT is supposed to drive to
	 */
	public void handleNewTarget(float nextX, float nextY) {
		xNext = nextX;
		yNext = nextY;
		appHandler.turnToCoordinate(nextX, nextY);
		appHandler.startMoving();
	}
}
