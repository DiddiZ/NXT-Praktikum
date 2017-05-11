package de.rwthaachen.nxtpraktikum.gruppe2_2017;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.Bot.LEFT_MOTOR;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.Bot.RIGHT_MOTOR;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.Bot.WHEEL_DIAMETER;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.Bot.WHEEL_GAUGE;
import static java.lang.Math.PI;
import static lejos.nxt.BasicMotorPort.FLOAT;
import lejos.nxt.addon.GyroSensor;

public final class SensorData
{
	private static GyroSensor gyro;

	/** Current angular velocity in degrees/second. Positive when tilting backwards */
	public static double gyroSpeed;
	/** Current angle relative to initial tilting in degrees. Positive when tilted backwards. NOT clamped. */
	public static double gyroAngle;

	/** Current heading relative to initial heading in degrees. NOT clamped */ // TODO Test
	public static double heading; //
	/** Current speed in cm/s */
	public static double motorSpeed;
	/** Current traveled distance in cm. */
	public static double motorDistance;

	// TODO Absolute position

	/**
	 * Must be called first before calling {@link #update(double)} or using any of the public attibutes.
	 */
	public static void init() {
		SensorData.gyro = new GyroSensor(Bot.GYRO_PORT);

		System.out.println("Calibrating gyro ...");
		// gyro.recalibrateOffset();
		recalibrateOffsetAlt(); // recalibrateOffsetAlt is much faster than recalibrateOffset and actually more reliable, as it rejects the sample if jitter is too large

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
		if (gyroSpeed >= 1 || gyroSpeed <= -1) // Values < 1 should can ignored according to GyroSensor.getAngularVelocity()
			gyroAngle += gyro.getAngularVelocity() * deltaTime;
		// TODO Figure out, if we need to handle drift ourselves or if GyroSensor.getAngularVelocity() does this properly

		// Read motor data. Standard tacho gives 160 ticks/turn.
		final long tachoLeft = -LEFT_MOTOR.getTachoCount(), tachoRight = -RIGHT_MOTOR.getTachoCount();
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
		} while (gMax - gMin > 1); // Reject and sample again if range too large

		// Average the sum of the samples.
		gyro.setOffset(gSum / OFFSET_SAMPLES);// TODO: Used to have +1, which was mainly for stopping Segway wandering.
	}
}
