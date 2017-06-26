package de.rwthaachen.nxtpraktikum.gruppe2_2017.nxt.motorcontrol;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.NXT.LEFT_MOTOR;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.NXT.RIGHT_MOTOR;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.NXT.WHEEL_DIAMETER;
import static java.lang.Math.abs;
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
	private static final double MAX_DISTANCE_INFLUENCE = 10;
	private static final double MAX_HEADING_INFLUENCE = 3;

	// Weights for PID taken from Segoway. //TODO Adjust properly
	public static double WEIGHT_GYRO_SPEED 		= -2.8;
	public static double WEIGHT_GYRO_INTEGRAL 	= -13;
	public static double WEIGHT_MOTOR_DISTANCE 	=  0.15 * 360 / Math.PI / WHEEL_DIAMETER * 2;
	public static double WEIGHT_MOTOR_SPEED 	=  0.225 * 360 / Math.PI / WHEEL_DIAMETER * 2;
	
	public static double CONST_SPEED = 0;
	public static double CONST_ROTATION = 0;

	public static boolean isRunning = false;

	private static double distanceTarget, headingTarget;

	/**
	 * Tries to hold the segway upright. Stops when ESC is pressed.
	 */
	public static void run() throws InterruptedException {
		CONST_SPEED = 0;
		CONST_ROTATION = 0;
		isRunning = true;
		final long startTime = System.nanoTime();
		long cycles = 0;
		int fallenTicks = 0;

		while (Button.ENTER.isUp() && isRunning) {
			Thread.sleep(SLEEP_TIME); // Sleep before action to wait for sensor data
			cycles++;

			final double deltaTime = (System.nanoTime() - startTime) / cycles / 1000000000.0;
			SensorData.update(deltaTime);

			distanceTarget += CONST_SPEED * deltaTime;
			headingTarget += CONST_ROTATION * deltaTime;
			
			final double rawPower = WEIGHT_GYRO_SPEED     * SensorData.gyroSpeed +
									WEIGHT_GYRO_INTEGRAL  * SensorData.gyroIntegral +
									// Clamp motorDistance
									WEIGHT_MOTOR_DISTANCE * clamp(SensorData.motorDistance - distanceTarget, -MAX_DISTANCE_INFLUENCE, MAX_DISTANCE_INFLUENCE) +
									WEIGHT_MOTOR_SPEED    * SensorData.motorSpeed;

			// Fall detection logic. Assume fallen if power is on full speed for several ticks
			if (abs(rawPower) > 100) {
				fallenTicks++;
				if (fallenTicks >= ASSUMED_FALLEN_TICKS) {
					System.out.println("Ups, I fell...");
					break; // I've fallen and I can't get up!
				}
			} else
				fallenTicks = 0;

			// remove turning
			final double rawPowerLeft = rawPower - clamp(SensorData.heading - headingTarget, -MAX_HEADING_INFLUENCE, MAX_HEADING_INFLUENCE) * WEIGHT_MOTOR_DISTANCE;
			final double rawPowerRight = rawPower + clamp(SensorData.heading - headingTarget, -MAX_HEADING_INFLUENCE, MAX_HEADING_INFLUENCE) * WEIGHT_MOTOR_DISTANCE;

			// Clamp power to range [-100, 100]
			final int powerLeft = clamp((int) rawPowerLeft, -100, 100);
			final int powerRight = clamp((int) rawPowerRight, -100, 100);

			LEFT_MOTOR.controlMotor(abs(powerLeft), powerLeft > 0 ? BACKWARD : FORWARD);
			RIGHT_MOTOR.controlMotor(abs(powerRight), powerRight > 0 ? BACKWARD : FORWARD);
		}
		System.out.println("Balancing stoped.");
		LEFT_MOTOR.controlMotor(0, BasicMotorPort.FLOAT);
		RIGHT_MOTOR.controlMotor(0, BasicMotorPort.FLOAT);
	}

	/**
	 * Clamps a double value between min and max.
	 */
	private static double clamp(double value, double min, double max) {
		if (value < min)
			return min;
		if (value > max)
			return max;
		return value;
	}

	/**
	 * Clamps a int value between min and max.
	 */
	private static int clamp(int value, int min, int max) {
		if (value < min)
			return min;
		if (value > max)
			return max;
		return value;
	}

	/**
	 * Makes the NXT move a certain distance by influencing the PID.
	 *
	 * @param distance in cm
	 */
	public static void move(double distance) {
		distanceTarget += distance;
	}

	/**
	 * Makes the NXT turn by a certain angle by influencing the PID.
	 *
	 * @param angle in °
	 */
	public static void turn(double angle) {
		headingTarget += angle;
	}

	/**
	 * Stops all current movement.
	 */
	public static void stopMoving() {
		distanceTarget = SensorData.motorDistance;
		headingTarget = SensorData.heading;
	}
}
