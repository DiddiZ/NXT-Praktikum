package de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.NXT.COMMUNICATOR;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.sensors.SensorData;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.nxt.motorcontrol.MotorController;
import lejos.nxt.Button;

/**
 * This class is the main menu on the NXT. It allows to start the communication and the balancing threads.
 *
 * @author Gregor & Robin
 */
public class SegwayMain
{
	static boolean startBalancing = false;

	public static void main(String[] args) throws InterruptedException {
		System.out.println("Segway Controls:");
		System.out.println("");
		System.out.println("Balance    RIGHT");
		System.out.println("");
		System.out.println("Bluetooth   LEFT");
		System.out.println("");
		System.out.println("Exit      ESCAPE");

		while (Button.ESCAPE.isUp()) {

			if (startBalancing) {
				startBalancing = false;
				SensorData.init();
				Audio.playBeeps(3);
				MotorController.run();
			}

			if (Button.RIGHT.isDown()) {
				startBalancing = true;
				while (Button.RIGHT.isDown())
					; // NOP
			}

			if (Button.LEFT.isDown())
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
