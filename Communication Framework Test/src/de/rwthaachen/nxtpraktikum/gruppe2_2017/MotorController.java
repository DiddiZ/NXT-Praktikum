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

import communication.callbackMethods;

public class MotorController implements callbackMethods
{
	private static final int SLEEP_TIME = 5;

	// Weights for PID taken from Segoway. //TODO Adjust properly
	private static double WEIGHT_GYRO_SPEED = 0;
	private static double WEIGHT_GYRO_ANGLE = 0;
	private static double WEIGHT_MOTOR_DISTANCE = 0 * 360 / Math.PI / WHEEL_DIAMETER * 2;
	private static double WEIGHT_MOTOR_SPEED = 0 * 360 / Math.PI / WHEEL_DIAMETER * 2;
	private static boolean isRunning = false;
	
	
	/**
	 * Tries to hold the segway upright. Stops when ESC is pressed.
	 */
	public static void run() throws InterruptedException {
		final long startTime = System.nanoTime();
		long cycles = 0;
		isRunning = true;
		
		
		while (Button.ENTER.isUp()) {
			Thread.sleep(SLEEP_TIME); // Sleep before action to wait for sensor data
			cycles++;

			final double deltaTime = (System.nanoTime() - startTime) / cycles / 1000000000.0;
			SensorData.update(deltaTime);

			final double rawPower = 
					WEIGHT_GYRO_SPEED * SensorData.gyroSpeed +
					WEIGHT_GYRO_ANGLE * SensorData.gyroAngle +
					WEIGHT_MOTOR_DISTANCE * SensorData.motorDistance +
					WEIGHT_MOTOR_SPEED * SensorData.motorSpeed;

			// Clamp power to range [-100, 100]
			final int power = max(min((int) rawPower, 100), -100);

			LEFT_MOTOR.controlMotor(abs(power), power > 0 ? BACKWARD : FORWARD);
			RIGHT_MOTOR.controlMotor(abs(power), power > 0 ? BACKWARD : FORWARD);
		}
		LEFT_MOTOR.controlMotor(0, FORWARD);
		RIGHT_MOTOR.controlMotor(0, FORWARD);
		System.out.println("Stop balancing.");
		isRunning = false;
	}
	
	public static boolean running() {
		return isRunning;
	}

	@Override
	public void callback0(float p_parameter) {
		// TODO Auto-generated method stub
		WEIGHT_GYRO_SPEED = p_parameter;
		System.out.println();
		System.out.print("GyroSpeed:");
		System.out.print(p_parameter);
	}

	@Override
	public void callback1(float p_parameter) {
		// TODO Auto-generated method stub
		WEIGHT_GYRO_ANGLE = p_parameter;
		System.out.println();
		System.out.print("GyroAngle:");
		System.out.print(p_parameter);
	}

	@Override
	public void callback2(float p_parameter) {
		// TODO Auto-generated method stub
		WEIGHT_MOTOR_DISTANCE = p_parameter * 360 / Math.PI / WHEEL_DIAMETER * 2;
		System.out.println();
		System.out.print("MotorDist:");
		System.out.print(p_parameter);
	}

	@Override
	public void callback3(float p_parameter) {
		// TODO Auto-generated method stub
		WEIGHT_MOTOR_SPEED = p_parameter * 360 / Math.PI / WHEEL_DIAMETER * 2;
		System.out.println();
		System.out.print("MotorSped:");
		System.out.print(p_parameter);
	}
}
