package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;

/**
 * Abstraction of the user interface for the communicator.
 *
 * @author DiddiZ
 */
public interface UserInterface
{
	public void setTiltLabel(float angle);

	public void setRotationLabel(float heading);

	public void setBatteryLabel(int voltage);

	public void setSpeedometerLabel(float speed);

	public void setCurrentPositionLabel(float x, float y);

	public void setGyroSpeedt(double gyroSpeed);

	public void setAutoStatusPacket(boolean enabled);

	public void setMotorSpeedt(double speed);

	public void setMotorDistancet(double distance);

	public void setGyroIntegralt(double gyroIntegral);

	public void setTachoLeft(long tacho);

	public void setTachoRight(long tacho);

	public void output(String text);

	public void setTimeText(String time);
}
