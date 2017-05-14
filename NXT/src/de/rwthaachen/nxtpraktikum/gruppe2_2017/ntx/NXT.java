package de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.sensors.SensorData;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.nxt.motorcontrol.MotorController;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;

/**
 * @author DiddiZ
 */
public class NXT
{
	// Bot design constants
	public static final MotorPort LEFT_MOTOR = MotorPort.A, RIGHT_MOTOR = MotorPort.B;
	public static final SensorPort GYRO_PORT = SensorPort.S2;
	public static final double WHEEL_DIAMETER = 5.6, WHEEL_GAUGE = 5.5;

	public static void main(String[] args) throws InterruptedException {
		// TODO Start Communication-Thread here

		SensorData.init();

		playBeeps(3);

		MotorController.run();
	}

	/**
	 * Plays a number of beeps and counts down. Each beep is one second.
	 */
	private static void playBeeps(int number) {
		System.out.println("About to start");
		for (int c = number; c > 0; c--) {
			System.out.print(c + " ");
			Sound.playTone(440, 100);
			try {
				Thread.sleep(1000);
			} catch (final InterruptedException e) {}
		}
		System.out.println("GO");
	}
}