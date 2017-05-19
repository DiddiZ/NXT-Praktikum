package de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm.CallbackMethod;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm.PCCom;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.sensors.SensorData;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.nxt.motorcontrol.MotorController;
import lejos.nxt.Button;
import lejos.nxt.Sound;

public class SegwayMain
{
	private static PCCom communicator;
	int speed = 50;

	public static void main(String[] args) throws InterruptedException {
		System.out.println("Segway Controls:");
		System.out.println("");
		System.out.println("Balance    RIGHT");
		System.out.println("");
		System.out.println("Bluetooth   LEFT");
		System.out.println("");
		System.out.println("Exit      ESCAPE");

		communicator = new PCCom();
		communicator.setPriority(Thread.NORM_PRIORITY);
		communicator.setDaemon(true);

		communicator.registerCallback(new CallbackMethod() {
			@Override
			public void callback(float p) {
				MotorController.WEIGHT_GYRO_SPEED = p;
			}
		}, 0);
		communicator.registerCallback(new CallbackMethod() {
			@Override
			public void callback(float p) {
				MotorController.WEIGHT_GYRO_INTEGRAL = p;
			}
		}, 1);
		communicator.registerCallback(new CallbackMethod() {
			@Override
			public void callback(float p) {
				MotorController.WEIGHT_MOTOR_DISTANCE = p * 360 / Math.PI / NXT.WHEEL_DIAMETER * 2;
			}
		}, 2);
		communicator.registerCallback(new CallbackMethod() {
			@Override
			public void callback(float p) {
				MotorController.WEIGHT_MOTOR_SPEED = p * 360 / Math.PI / NXT.WHEEL_DIAMETER * 2;
			}
		}, 3);

		while (Button.ESCAPE.isUp()) {

			if (Button.RIGHT.isDown()) {
				SensorData.init();
				playBeeps(3);
				MotorController.run();
			}

			if (Button.LEFT.isDown())
				if (!communicator.getConnected() && !communicator.getConnecting())
					communicator.connect();
		}
		communicator.disconnect();
	}// End main

	/**
	 * Plays a number of beeps and counts down. Each beep is one second.
	 */
	private static void playBeeps(int number) {
		System.out.println("About to start");
		System.out.println("Put me up");
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
