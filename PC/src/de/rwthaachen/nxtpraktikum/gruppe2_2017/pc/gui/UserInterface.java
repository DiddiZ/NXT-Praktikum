package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;

/**
 * Abstraction of the user interface for the communicator.
 *
 * @author Robin, Christian
 */
public interface UserInterface
{
	/**
	 * Displays the user interface to the user.
	 */
	public void show();

	/**
	 * Displays the tilt of the NXT to the user.
	 *
	 * @param tilt angle in °
	 */
	public void showTilt(float tilt);

	/**
	 * Displays the heading of the NXT to the user.
	 *
	 * @param heading angle in °
	 */
	public void showHeading(float heading);

	/**
	 * Displays the battery voltage of the NXT to the user.
	 *
	 * @param voltage in mV
	 */
	public void showBatteryVoltage(int voltage);

	/**
	 * Displays the current speed of the NXT to the user.
	 *
	 * @param speed in cm/s
	 */
	public void showSpeed(float speed);

	/**
	 * Displays the current position of the NXT to the user.
	 *
	 * @param x in cm
	 * @param y in cm
	 */
	public void showPosition(float x, float y);

	/**
	 * Displays the gyro speed pid weight of the NXT to the user.
	 */
	public void showGyroSpeedWeight(double gyroSpeedWeight);

	/**
	 * Displays whether the NXT sends automatic status packets to the user.
	 */
	public void showAutoStatusPacketEnabled(boolean enabled);

	/**
	 * Displays the motor speed pid weight of the NXT to the user.
	 */
	public void showMotorSpeedWeight(double speedWeight);

	/**
	 * Displays the motor distance pid weight of the NXT to the user.
	 */
	public void showMotorDistanceWeight(double distanceWeight);

	/**
	 * Displays the gyro integral pid weight of the NXT to the user.
	 */
	public void showGyroIntegralWeight(double gyroIntegralWeight);

	/**
	 * Displays the left tacho of the NXT to the user.
	 *
	 * @param leftTacho in °
	 */
	public void showTachoLeft(long leftTacho);

	/**
	 * Displays the right tacho of the NXT to the user.
	 *
	 * @param rightTacho in °
	 */
	public void showTachoRight(long rightTacho);

	/**
	 * Displays a log message to the user.
	 */
	public void showMessage(String text);

	/**
	 * Displays the duration of the current connection to the user.
	 *
	 * @param time in ms
	 */
	public void showConnectionTime(long time);

	/**
	 * Displays the current state of NXT balancing to the user.
	 */
	public void showBalancingEnabled(boolean enabled);

	/**
	 * Displays the state of the connection to the user.
	 */
	public void showConnected(boolean connected);
	
	public void drawPosition(int x, int y);
	/**
	 * Draws the given position on the map
	 */
	
	public void drawPosition(int x, int y, float heading);
	/**
	 * Draws the given position on the map as an arrow
	 */
}
