package lejos.sensordata;

import static java.lang.Math.PI;
import static lejos.nxt.BasicMotorPort.FLOAT;
import lejos.nxt.MotorPort;
import lejos.nxt.addon.GyroSensor;

public final class SensorData
{
	private static MotorPort leftMotor, rightMotor;
	private static GyroSensor gyro;
	private static double wheelSize, wheelGauge;

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
	@SuppressWarnings("hiding")
	public static void init(MotorPort leftMotor, MotorPort rightMotor, GyroSensor gyro, double wheelSize, double wheelGauge) {
		SensorData.leftMotor = leftMotor;
		SensorData.rightMotor = rightMotor;
		SensorData.gyro = gyro;
		SensorData.wheelSize = wheelSize;
		SensorData.wheelGauge = wheelGauge;

		System.out.println("Calibrating gyro ...");
		// gyro.recalibrateOffset();
		recalibrateOffsetAlt(); // recalibrateOffsetAlt is much faster than recalibrateOffset and actually more reliable, as it rejects the sample if jitter is too large

		leftMotor.resetTachoCount();
		rightMotor.resetTachoCount();
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
		final long tachoLeft = -leftMotor.getTachoCount(), tachoRight = -rightMotor.getTachoCount();
		final long motorSum = tachoLeft + tachoRight;
		final long motorDiff = tachoLeft - tachoRight; // Current difference between motors in degrees
		final double motorSumDelta = (motorSum - motorSumPrev) / deltaTime;
		motorSumPrev = motorSum;

		// Calculate heading
		heading = motorDiff * wheelSize / wheelGauge / 2;

		// Calculate speed. Use the average of the four latest deltas. Half, as we motorSum is the sum of both motors. Convert to cm/s.
		motorSpeed = (motorSumDelta + motorSumDeltaP1 + motorSumDeltaP2 + motorSumDeltaP3) / 4 / 2 * wheelSize * PI / 360; // I'm pretty confident the compiler will precalculate all the static factors
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
		double gSum;
		int i, gMin, gMax, g;

		// Bit of a hack here. Ensure that the motor controller is active since this affects the gyro values for HiTechnic.
		leftMotor.controlMotor(0, FLOAT);
		rightMotor.controlMotor(0, FLOAT);

		do {
			gSum = 0.0;
			gMin = 1000;
			gMax = -1000;
			for (i = 0; i < OFFSET_SAMPLES; i++) {
				g = gyro.readValue();
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
		gyro.setOffset((int)(gSum / OFFSET_SAMPLES));// TODO: Used to have +1, which was mainly for stopping Segway wandering.
	}
}
