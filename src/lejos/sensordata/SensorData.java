package lejos.sensordata;

import static java.lang.Math.PI;
import lejos.nxt.MotorPort;
import lejos.nxt.addon.GyroSensor;

public final class SensorData
{
	private static MotorPort leftMotor, rightMotor;
	private static GyroSensor gyro;
	private static double wheelSize, wheelGauge;

	/** Current angular velocity in degrees/second. Positive when tilting forward */ // TODO verify tilting
	public static double gyroSpeed;
	/** Current angle relative to initial tilting in degrees */
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
		gyro.recalibrateOffset();

		leftMotor.resetTachoCount();
		rightMotor.resetTachoCount();
	}

	// Variables for caching data from previous cycles
	private static long motorSumDeltaP1, motorSumDeltaP2, motorSumDeltaP3, motorSumPrev;

	/**
	 * @param deltaTime Time since last call of {@link #update(double)} in seconds.
	 */
	public static void update(double deltaTime) {
		// Read gyro data
		gyroSpeed = gyro.getAngularVelocity();
		if (gyroSpeed >= 1) // Values < 1 should can ignored according to GyroSensor.getAngularVelocity()
			gyroAngle += gyro.getAngularVelocity() * deltaTime;
		// TODO Figure out, if we need to handle drift ourselves or if GyroSensor.getAngularVelocity() does this properly

		// Read motor data. Standard tacho gives 160 ticks/turn.
		final long tachoLeft = leftMotor.getTachoCount(), tachoRight = rightMotor.getTachoCount();
		final long motorSum = tachoLeft + tachoRight;
		final long motorDiff = tachoLeft - tachoRight; // Current difference between motors in degrees
		final long motorSumDelta = motorSum - motorSumPrev; //

		// Calculate heading
		heading = motorDiff * wheelSize / wheelGauge / 2;

		// Calculate speed. Use the average of the four latest deltas. Half, as we motorSum is the sum of both motors. Convert to cm/s.
		motorSpeed = (motorSumDelta + motorSumDeltaP1 + motorSumDeltaP2 + motorSumDeltaP3) / (4 * deltaTime) / 2 * wheelSize * PI / 360; // I'm pretty confident the compiler will precalculate all the static factors
		// Shift deltas
		motorSumDeltaP3 = motorSumDeltaP2;
		motorSumDeltaP2 = motorSumDeltaP1;
		motorSumDeltaP1 = motorSumDelta;

		// Caclulate traveled distance
		motorDistance += motorSpeed * deltaTime;
	}
}
