package lejos;

import lejos.nxt.BasicMotorPort;
import lejos.nxt.MotorPort;
import lejos.nxt.Sound; // TODO: Visual count-down only, no sound? Or some sort of sound interface and container for Sound class (can't implement interface on static methods)?
import lejos.nxt.addon.GyroSensor;

/**
 * <p>This class balances a two-wheeled Segway-like robot. It works with almost any construction
 * (tall or short) such as the <a href="http://www.laurensvalk.com/nxt-2_0-only/anyway">Anyway</a> or
 * the <a href="http://www.hitechnic.com/blog/gyro-sensor/htway/">HTWay</a>. Wheel diameter is the most
 * important construction variable, which is specified in the constructor.</p>
 * <p>To start the robot balancing:
 * <li>1. Run the program. You will be prompted to lay it down.
 * <li>2. Lay it down (orientation doesn't matter). When it detects it is not moving it will automatically calibrate the gyro sensor.
 * <li>3. When the beeping begins, stand it up so it is vertically balanced.
 * <li>4. When the beeping stops, let go and it will begin balancing on its own.</p>
 * <p>Alternately you can lean the robot against a wall and run the program. After the gyro
 * calibration, the robot backs up against the wall until it falls forward. When it detects the
 * forward fall, it start the balance loop.</p>
 * <p>NOTE: In order to make the robot move and navigate, use the SegowayPilot class.</p>
 * <p><i>This code is based on the <a href="http://www.hitechnic.com/blog/gyro-sensor/htway/">HTWay</a> by HiTechnic.</i></p>
 *
 * @author BB
 */
public class Segoway extends Thread
{
	private final MotorPort left_motor;
	private final MotorPort right_motor;

	/**
	 * Loop wait time. WAIT_TIME is the time in ms passed to the Wait command.
	 * NOTE: Balance control loop only takes 1.128 MS in leJOS NXJ.
	 */
	private static final int WAIT_TIME = 7; // originally 8

	/**
	 * These are the main four balance constants, only the gyro constants are relative to the wheel size. KPOS and KSPEED are self-relative to the wheel size.
	 */
	private static final double KGYROANGLE = 7.5, KGYROSPEED = 1.15, KPOS = 0.07, KSPEED = 0.1;

	/**
	 * This constant aids in drive control. When the robot starts moving because of user control,
	 * this constant helps get the robot leaning in the right direction. Similarly, it helps
	 * bring robot to a stop when stopping.
	 */
	private static final double KDRIVE = -0.02;

	/**
	 * Power differential used for steering based on difference of target steering and actual motor difference.
	 */
	private static final double KSTEER = 0.25;

	/**
	 * If robot power is saturated (over +/- 100) for over this time limit then
	 * robot must have fallen. In milliseconds.
	 */
	private static final double TIME_FALL_LIMIT = 500; // originally 1000

	/**
	 * This constant is in degrees/second for maximum speed. Note that position
	 * and speed are measured as the sum of the two motors, in other words, 600
	 * would actually be 300 degrees/second for each motor.
	 */
	private static final double CONTROL_SPEED = 600.0;

	/**
	 * motorControlDrive is the target speed for the sum of the two motors
	 * in degrees per second.
	 */
	private double motorControlDrive = 0.0; // TODO Replace by integral for PID

	/**
	 * motorControlSteer is the target change in difference for two motors
	 * in degrees per second.
	 */
	private double motorControlSteer = 0.0; // TODO Replace by integral for PID

	/**
	 * This global contains the target motor differential, essentially, which
	 * way the robot should be pointing. This value is updated every time through
	 * the balance loop based on motorControlSteer.
	 */
	private double motorDiffTarget = 0.0;

	/**
	 * Time that robot first starts to balance. Used to calculate tInterval.
	 */
	private long tCalcStart;

	/**
	 * tInterval is the time, in seconds, for each iteration of the balance loop.
	 */
	private double tInterval;

	/**
	 * ratioWheel stores the relative wheel size compared to a standard NXT 1.0 wheel.
	 * RCX 2.0 wheel has ratio of 0.7 while large RCX wheel is 1.4.
	 */
	private final double wheelDiameter, ratioWheel;

	/**
	 * Creates an instance of the Segoway, prompts the user to lay Segoway flat for gyro calibration,
	 * then begins self-balancing thread. Wheel diameter is used in balancing equations.
	 * <li>NXT 1.0 wheels = 5.6 cm
	 * <li>NXT 2.0 wheels = 4.32 cm
	 * <li>RCX "motorcycle" wheels = 8.16 cm
	 *
	 * @param left The left motor. An unregulated motor.
	 * @param right The right motor. An unregulated motor.
	 * @param gyro A HiTechnic gyro sensor
	 * @param wheelDiameter diameter of wheel, preferably use cm (printed on side of LEGO tires in mm)
	 */
	public Segoway(MotorPort left_motor, MotorPort right_motor, GyroSensor gyro, double wheelDiameter) {
		this.left_motor = left_motor;
		this.right_motor = right_motor;
		this.wheelDiameter = wheelDiameter;
		ratioWheel = wheelDiameter / 5.6; // Original algorithm was tuned for 5.6 cm NXT 1.0 wheels.

		// Calibrate gyro
		System.out.println("Calibrating gyro ...");
		gyro.recalibrateOffsetAlt();

		// Play warning beep sequence to indicate balancing is about to start
		playBeeps(5);

		// Start balance thread
		setDaemon(true);
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

	// Motor variables
	private double motorPos = 0;
	private long mrcSum = 0, mrcSumPrev;
	private long motorDiff;
	private long mrcDeltaP3 = 0, mrcDeltaP2 = 0, mrcDeltaP1 = 0;

	/**
	 * Keeps track of wheel position with both motors.
	 */
	private void updateMotorData() {
		long mrcLeft, mrcRight, mrcDelta;

		// Keep track of motor position and speed
		mrcLeft = left_motor.getTachoCount();
		mrcRight = right_motor.getTachoCount();

		// Maintain previous mrcSum so that delta can be calculated and get
		// new mrcSum and Diff values
		mrcSumPrev = mrcSum;
		mrcSum = mrcLeft + mrcRight;
		motorDiff = mrcLeft - mrcRight;

		// mrcDetla is the change int sum of the motor encoders, update
		// motorPos based on this detla
		mrcDelta = mrcSum - mrcSumPrev;
		motorPos += mrcDelta;

		// motorSpeed is based on the average of the last four delta's.
		motorSpeed = (mrcDelta + mrcDeltaP1 + mrcDeltaP2 + mrcDeltaP3) / (4 * tInterval);

		// Shift the latest mrcDelta into the previous three saved delta values
		mrcDeltaP3 = mrcDeltaP2;
		mrcDeltaP2 = mrcDeltaP1;
		mrcDeltaP1 = mrcDelta;
	}

	/**
	 * Global variables used to control the amount of power to apply to each wheel.
	 * Updated by the steerControl() method.
	 */
	private int powerLeft, powerRight; // originally local variables

	/**
	 * This function determines the left and right motor power that should
	 * be used based on the balance power and the steering control.
	 */
	private void steerControl(int power) {
		int powerSteer;

		// Update the target motor difference based on the user steering
		// control value.
		motorDiffTarget += motorControlSteer * tInterval;

		// Determine the proportionate power differential to be used based
		// on the difference between the target motor difference and the
		// actual motor difference.
		powerSteer = (int)(KSTEER * (motorDiffTarget - motorDiff));

		// Apply the power steering value with the main power value to
		// get the left and right power values.
		powerLeft = power + powerSteer;
		powerRight = power - powerSteer;

		// Limit the power to motor power range -100 to 100
		if (powerLeft > 100)
			powerLeft = 100;
		if (powerLeft < -100)
			powerLeft = -100;

		// Limit the power to motor power range -100 to 100
		if (powerRight > 100)
			powerRight = 100;
		if (powerRight < -100)
			powerRight = -100;
	}

	/**
	 * Calculate the interval time from one iteration of the loop to the next.
	 * Note that first time through, cLoop is 0, and has not gone through
	 * the body of the loop yet. Use it to save the start time.
	 * After the first iteration, take the average time and convert it to
	 * seconds for use as interval time.
	 */
	private void calcInterval(long cLoop) {
		if (cLoop == 0) {
			// First time through, set an initial tInterval time and record start time
			tInterval = 0.0055;
			tCalcStart = System.currentTimeMillis();
		} else
			// Take average of number of times through the loop and
			// use for interval time.
			tInterval = (System.currentTimeMillis() - tCalcStart) / (cLoop * 1000.0);
	}

	private double gyroSpeed, gyroAngle, motorSpeed;

	/**
	 * This is the main balance thread for the robot.
	 * Robot is assumed to start leaning on a wall. The first thing it
	 * does is take multiple samples of the gyro sensor to establish and
	 * initial gyro offset.
	 * After an initial gyro offset is established, the robot backs up
	 * against the wall until it falls forward, when it detects the
	 * forward fall, it start the balance loop.
	 * The main state variables are:
	 * gyroAngle This is the angle of the robot, it is the results of
	 * integrating on the gyro value.
	 * Units: degrees
	 * gyroSpeed The value from the Gyro Sensor after offset subtracted
	 * Units: degrees/second
	 * motorPos This is the motor position used for balancing.
	 * Note that this variable has two sources of input:
	 * Change in motor position based on the sum of
	 * MotorRotationCount of the two motors, and,
	 * forced movement based on user driving the robot.
	 * Units: degrees (sum of the two motors)
	 * motorSpeed This is the speed of the wheels of the robot based on the
	 * motor encoders.
	 * <p>
	 * Units: degrees/second (sum of the two motors)
	 * From these state variables, the power to the motors is determined by this linear equation:
	 * power = KGYROSPEED * gyro +
	 * KGYROANGLE * gyroAngle +
	 * KPOS * motorPos +
	 * KSPEED * motorSpeed;
	 */
	@Override
	public void run() {

		int power;
		long tMotorPosOK;
		long cLoop = 0;

		System.out.println("Balancing");
		System.out.println();

		tMotorPosOK = System.currentTimeMillis();

		// Reset the motors to make sure we start at a zero position
		left_motor.resetTachoCount();
		right_motor.resetTachoCount();

		// NOTE: This balance control loop only takes 1.128 MS to execute each loop in leJOS NXJ. //TODO Recalc
		while (true) {
			calcInterval(cLoop++);

			updateMotorData();

			// Apply the drive control value to the motor position to get robot to move.
			motorPos -= motorControlDrive * tInterval;

			// This is the main balancing equation
			power = (int)((KGYROSPEED * gyroSpeed + // Deg/Sec from Gyro sensor
					KGYROANGLE * gyroAngle) / ratioWheel + // Deg from integral of gyro
					KPOS * motorPos + // From MotorRotaionCount of both motors
					KDRIVE * motorControlDrive + // To improve start/stop performance
					KSPEED * motorSpeed); // Motor speed in Deg/Sec

			steerControl(power); // Movement control. Not used for balancing.

			// Apply the power values to the motors
			left_motor.controlMotor(Math.abs(powerLeft), powerLeft > 0 ? BasicMotorPort.FORWARD : BasicMotorPort.BACKWARD);
			right_motor.controlMotor(Math.abs(powerRight), powerRight > 0 ? BasicMotorPort.FORWARD : BasicMotorPort.BACKWARD);

			// Check if robot has fallen by detecting that motorPos is being limited
			// for an extended amount of time.
			if (Math.abs(power) < 100)
				tMotorPosOK = System.currentTimeMillis();
			if (System.currentTimeMillis() - tMotorPosOK > TIME_FALL_LIMIT)
				break; // I've fallen, and I can't get up!

			try {
				Thread.sleep(WAIT_TIME);
			} catch (final InterruptedException e) {
				continue; // Try to keep standing on error
			}
		}

		// Stop motors
		left_motor.controlMotor(0, BasicMotorPort.FLOAT);
		right_motor.controlMotor(0, BasicMotorPort.FLOAT);

		Sound.beepSequenceUp();
		System.out.println("Oops... I fell");
		System.out.println("tInt ms:");
		System.out.println((int)tInterval * 1000);
	}

	/**
	 * This method allows the robot to move forward/backward and make in-spot rotations as
	 * well as arcs by varying the power to each wheel. This method does not actually
	 * apply direct power to the wheels. Control is filtered through to each wheel, allowing the robot to
	 * drive forward/backward and make turns. Higher values are faster. Negative values cause the wheel
	 * to rotate backwards. Values between -200 and 200 are good. If values are too high it can make the
	 * robot balance unstable.
	 *
	 * @param left_wheel The relative control power to the left wheel. -200 to 200 are good numbers.
	 * @param right_wheel The relative control power to the right wheel. -200 to 200 are good numbers.
	 */
	public void wheelDriver(int left_wheel, int right_wheel) {
		motorControlDrive = (left_wheel + right_wheel) * CONTROL_SPEED / 200.0;
		motorControlSteer = (left_wheel - right_wheel) * CONTROL_SPEED / 200.0;
	}

	/**
	 * Move distance in cm
	 */
	public void move(double dist) {
		motorPos -= dist / wheelDiameter / Math.PI * 360;
	}
}