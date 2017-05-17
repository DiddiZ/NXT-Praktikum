package de.rwthaachen.nxtpraktikum.gruppe2_2017.nxt.motorcontrol;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.NXT.LEFT_MOTOR;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.NXT.RIGHT_MOTOR;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.NXT.WHEEL_DIAMETER;
import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static lejos.nxt.BasicMotorPort.BACKWARD;
import static lejos.nxt.BasicMotorPort.FORWARD;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.sensors.SensorData;
import lejos.nxt.BasicMotorPort;
import lejos.nxt.Button;

/**
 * Contains the motor control loop.
 *
 * @author Robin
 */
public final class MotorController
{
	private static final int SLEEP_TIME = 4;
	/** Assume bot is fallen if power is on full speed for ASSUMED_FALLEN_TICKS ticks. */
	private static final int ASSUMED_FALLEN_TICKS = 70;

	// Weights for PID taken from Segoway. //TODO Adjust properly
	public static double WEIGHT_GYRO_SPEED 		= -2.8;
	public static double WEIGHT_GYRO_INTEGRAL 	= -8.2;
	public static double WEIGHT_MOTOR_DISTANCE 	=  0.042 * 360 / Math.PI / WHEEL_DIAMETER * 2;
	public static double WEIGHT_MOTOR_SPEED 	=  0.25 * 360 / Math.PI / WHEEL_DIAMETER * 2;

	/**
	 * Tries to hold the segway upright. Stops when ESC is pressed.
	 */
	public static void run() throws InterruptedException {
		final long startTime = System.nanoTime();
		long cycles = 0;
		int fallenTicks = 0;

		while (Button.ENTER.isUp()) {
			Thread.sleep(SLEEP_TIME); // Sleep before action to wait for sensor data
			cycles++;

			final double deltaTime = (System.nanoTime() - startTime) / cycles / 1000000000.0;
			SensorData.update(deltaTime);

			final double rawPower = WEIGHT_GYRO_SPEED 		* SensorData.gyroSpeed +
									WEIGHT_GYRO_INTEGRAL 	* SensorData.gyroIntegral +
									WEIGHT_MOTOR_DISTANCE 	* SensorData.motorDistance +
									WEIGHT_MOTOR_SPEED 		* SensorData.motorSpeed;

			// Fall detection logic. Assume fallen if power is on full speed for several ticks
			if (abs(rawPower) > 100) {
				fallenTicks++;
				if (fallenTicks >= ASSUMED_FALLEN_TICKS) {
					System.out.println("Ups, I fell...");
					break; // I've fallen and I can't get up!
				}
			} else
				fallenTicks = 0;

			// Clamp power to range [-100, 100]
			final int power = max(min((int)rawPower, 100), -100);

			LEFT_MOTOR.controlMotor(abs(power), power > 0 ? BACKWARD : FORWARD);
			RIGHT_MOTOR.controlMotor(abs(power), power > 0 ? BACKWARD : FORWARD);
		}
		System.out.println("Balancing stoped.");
		LEFT_MOTOR.controlMotor(0, BasicMotorPort.FLOAT);
		RIGHT_MOTOR.controlMotor(0, BasicMotorPort.FLOAT);
	}
}