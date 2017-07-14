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

	public static void main(String[] args) throws InterruptedException {
		mainMenu();
	}

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

	public static void startBalancing() {
		startBalancing = true;
	}

	public static void stopBalancing() {
		MotorController.isRunning = false;
	}
}
