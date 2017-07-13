package de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.sensors;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.NXT.GYRO_PORT;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.NXT.ULTRASONIC_PORT;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.NXT.LEFT_MOTOR;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.NXT.RIGHT_MOTOR;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.NXT.WHEEL_DIAMETER;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.NXT.WHEEL_GAUGE;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.NXT.US_MAXIMUM_DISTANCE;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.NXT.US_PERIOD;
import static java.lang.Math.PI;
import static lejos.nxt.BasicMotorPort.FLOAT;

import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.GyroSensor;
import lejos.robotics.objectdetection.FeatureDetector;
import lejos.robotics.objectdetection.FeatureListener;
import lejos.robotics.objectdetection.RangeFeatureDetector;

/**
 * SensorData retrieves data from all relevant sensors and makes them accessible as static fields.
 *
 * @author Robin
 */
public final class SensorData
{
	private static GyroSensor gyro;
	
	private static UltrasonicSensor us;
	private static FeatureDetector rd;
	private static FeatureListener listener;

	/** Current angular velocity in degrees/second. Positive when tilting backwards */
	public static double gyroSpeed;
	/** Current angle relative to initial tilting in degrees. Positive when tilted backwards. NOT clamped. Very unreliable and wanders off. */
	/** Sum of the {@link #gyroSpeed} damped each tick. */
	public static double gyroIntegral;

	/** Current heading relative to initial heading in degrees. NOT clamped */
	public static double heading; //
	/** Current speed in cm/s */
	public static double motorSpeed;
	/** Current traveled distance in cm. */
	public static double motorDistance;
	/** Current position in cm. At start, NXT is looking along the y-axis */
	public static double positionX, positionY;

	/** Current tick count of the left tacho */
	public static long tachoLeft;
	/** Current tick count of the right tacho */
	public static long tachoRight;

	/**
	 * Must be called first before calling {@link #update(double)} or using any of the public attibutes.
	 */
	public static void init() {
		SensorData.us = new UltrasonicSensor(ULTRASONIC_PORT);
		//SensorData.rd = new RangeFeatureDetector(us, US_MAXIMUM_DISTANCE, US_PERIOD);
		SensorData.listener = new ObjectDetect();
		//rd.addListener(listener);
		
		
		SensorData.gyro = new GyroSensor(GYRO_PORT);

		System.out.println("Lay me down");
		System.out.println("Calibrating gyro ...");
		// gyro.recalibrateOffset();
		recalibrateOffsetAlt(); // recalibrateOffsetAlt is much faster than recalibrateOffset and actually more reliable, as it rejects the sample if jitter is too large

		// reset all variables
		gyroSpeed = 0;
		gyroIntegral = 0;
		heading = 0;
		motorSpeed = 0;
		motorDistance = 0;
		motorSumDeltaP1 = 0;
		motorSumDeltaP2 = 0;
		motorSumDeltaP3 = 0;
		motorSumPrev = 0;

		// reset motors
		LEFT_MOTOR.resetTachoCount();
		RIGHT_MOTOR.resetTachoCount();
	}

	// Variables for caching data from previous cycles
	private static double motorSumDeltaP1, motorSumDeltaP2, motorSumDeltaP3, motorSumPrev;

	/**
	 * @param deltaTime Time since last call of {@link #update(double)} in seconds.
	 */
	public static void update(double deltaTime) {
		// Read gyro data
		gyroSpeed = gyro.getAngularVelocity();
		// Update gyroIntegral. The integral is damped and extended by the actual angular velocity.
		gyroIntegral = 0.99 * gyroIntegral + gyroSpeed * deltaTime;

		// Read motor data. Standard tacho gives 360 ticks/turn.
		tachoLeft = -LEFT_MOTOR.getTachoCount();
		tachoRight = -RIGHT_MOTOR.getTachoCount();
		final long motorSum = tachoLeft + tachoRight;
		final long motorDiff = tachoLeft - tachoRight; // Current difference between motors in degrees
		final double motorSumDelta = (motorSum - motorSumPrev) / deltaTime;
		motorSumPrev = motorSum;

		// Calculate heading
		heading = motorDiff * WHEEL_DIAMETER / WHEEL_GAUGE / 2;

		// Calculate speed. Use the average of the four latest deltas. Half, as we motorSum is the sum of both motors. Convert to cm/s.
		motorSpeed = (motorSumDelta + motorSumDeltaP1 + motorSumDeltaP2 + motorSumDeltaP3) / 4 / 2 * WHEEL_DIAMETER * PI / 360; // I'm pretty confident the compiler will precalculate all the static factors
		// Shift deltas
		motorSumDeltaP3 = motorSumDeltaP2;
		motorSumDeltaP2 = motorSumDeltaP1;
		motorSumDeltaP1 = motorSumDelta;

		// Caclulate traveled distance
		motorDistance += motorSpeed * deltaTime;

		// Caclulate new position
		positionX += Math.sin(heading / 180 * Math.PI) * motorSpeed * deltaTime;
		positionY += Math.cos(heading / 180 * Math.PI) * motorSpeed * deltaTime;
	}

	/**
	 * Actually just {@link GyroSensor#recalibrateOffsetAlt()} but calculated offset is actually applied. smh
	 */
	private static void recalibrateOffsetAlt() {
		final int OFFSET_SAMPLES = 100;
		int gSum;
		int gMin, gMax;

		// Bit of a hack here. Ensure that the motor controller is active since this affects the gyro values for HiTechnic.
		LEFT_MOTOR.controlMotor(0, FLOAT);
		RIGHT_MOTOR.controlMotor(0, FLOAT);

		do {
			gSum = 0;
			gMin = Integer.MAX_VALUE;
			gMax = Integer.MIN_VALUE;
			for (int i = 0; i < OFFSET_SAMPLES; i++) {
				final int g = gyro.readValue();
				if (g > gMax)
					gMax = g;
				if (g < gMin)
					gMin = g;

				gSum += g;
				try {
					Thread.sleep(5);
				} catch (final InterruptedException e) {}
			}
		} while (gMax - gMin > 2); // Reject and sample again if range too large

		// Average the sum of the samples.
		gyro.setOffset(gSum / OFFSET_SAMPLES);// TODO: Used to have +1, which was mainly for stopping Segway wandering.
	}
}
