package de.rwthaachen.nxtpraktikum.gruppe2_2017;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.Bot.LEFT_MOTOR;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.Bot.RIGHT_MOTOR;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.Bot.WHEEL_DIAMETER;
import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static lejos.nxt.BasicMotorPort.BACKWARD;
import static lejos.nxt.BasicMotorPort.FORWARD;
import lejos.nxt.Button;

public class MotorController
{
	private static final int SLEEP_TIME = 5;

	// Weights for PID taken from Segoway. //TODO Adjust properly
	public static final double WEIGHT_GYRO_SPEED = -7.5;
	public static final double WEIGHT_GYRO_INTEGRAL = -1.15;
	public static final double WEIGHT_MOTOR_DISTANCE = -0.07 * 360 / Math.PI / WHEEL_DIAMETER * 2;
	public static final double WEIGHT_MOTOR_SPEED = -0.1 * 360 / Math.PI / WHEEL_DIAMETER * 2;

	/**
	 * Tries to hold the segway upright. Stops when ESC is pressed.
	 */
	public static void run() throws InterruptedException {
		final long startTime = System.nanoTime();
		long cycles = 0;

		while (Button.ESCAPE.isUp()) {
			Thread.sleep(SLEEP_TIME); // Sleep before action to wait for sensor data
			cycles++;

			final double deltaTime = (System.nanoTime() - startTime) / cycles / 1000000000.0;
			SensorData.update(deltaTime);

			final double rawPower = WEIGHT_GYRO_SPEED * SensorData.gyroSpeed +
					WEIGHT_GYRO_INTEGRAL * SensorData.gyroIntegral +
					WEIGHT_MOTOR_DISTANCE * SensorData.motorDistance +
					WEIGHT_MOTOR_SPEED * SensorData.motorSpeed;

			// Clamp power to range [-100, 100]
			final int power = max(min((int)rawPower, 100), -100);

			LEFT_MOTOR.controlMotor(abs(power), power > 0 ? BACKWARD : FORWARD);
			RIGHT_MOTOR.controlMotor(abs(power), power > 0 ? BACKWARD : FORWARD);
		}
	}
}
