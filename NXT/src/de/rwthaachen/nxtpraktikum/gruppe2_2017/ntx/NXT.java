package de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm.CommunicatorNXT;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.sensors.SensorData;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.nxt.motorcontrol.MotorController;
import lejos.nxt.Button;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;

/**
 * This class is the main menu on the NXT. It allows to start the communication and the balancing threads.
 *
 * @author Gregor & Robin
 */
public class NXT
{
	// Bot design constants
	public static final MotorPort LEFT_MOTOR = MotorPort.A, RIGHT_MOTOR = MotorPort.B;
	public static final SensorPort GYRO_PORT = SensorPort.S2;
	public static final SensorPort ULTRASONIC_PORT = SensorPort.S4;
	public static final int US_MAXIMUM_DISTANCE = 50;
	public static final int US_PERIOD = 200;
	public static double WHEEL_DIAMETER = 5.6, WHEEL_GAUGE = 5.5;

	public static final CommunicatorNXT COMMUNICATOR = new CommunicatorNXT();

	private static boolean startBalancing = false;

	/**
	 * Is the main method on the NXT. Calls the mainMenu method to create the visible menu and initiate the Communication and Balancing Thread.
	 * @param unused
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		mainMenu();
	}

	/**
	 * Initiates the Connection, by calling the connect method of the COMMUNICATOR. 
	 * 
	 * The method runs in a endless loop until the Escape Button is hold down during a connection timeout, 
	 * if the connection is whether established nor beeing established the NXT tries to connect with the PC.
	 * If startBalancing is set to true, then the Method initiates SensorData and starts the Balancing thread after 3 seconds.
	 * @throws InterruptedException
	 */
	public static void mainMenu() throws InterruptedException {
		while (Button.ESCAPE.isUp()) {

			if (startBalancing) {
				startBalancing = false;
				SensorData.init();
				Audio.playBeeps(3);
				MotorController.run();
			}

			if (!COMMUNICATOR.isConnected() && !COMMUNICATOR.isConnecting())
				COMMUNICATOR.connect();

		}
		COMMUNICATOR.disconnect();
	}

	/**
	 * Sets the balancing var to true, which causes the main loop to initiate all Sensors and start the Balancing thread
	 */
	public static void startBalancing() {
		startBalancing = true;
	}

	/**
	 * Stops the balancing thread.
	 */
	public static void stopBalancing() {
		MotorController.isRunning = false;
	}
}
