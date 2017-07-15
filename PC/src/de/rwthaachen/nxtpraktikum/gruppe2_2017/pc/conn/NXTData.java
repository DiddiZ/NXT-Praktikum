package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo.Measurements;

/**
 * Contains current state of NXT.
 * Data gets updated every time it is received.
 *
 * @author Robin & Fabian
 */
public final class NXTData
{
	private float heading;
	private float positionX;
	private float positionY;
	private boolean isBalancing;
	private Measurements measurements;

	public float getHeading() {
		return heading;
	}

	public float getPositionX() {
		return positionX;
	}

	public float getPositionY() {
		return positionY;
	}

	public boolean getBalancing() {
		return isBalancing;
	}

	public void setPosition(float x, float y) {
		positionX = x;
		positionY = y;
	}

	public void setHeading(float heading) {
		this.heading = heading;
	}

	public void setBalancing(boolean enabled) {
		isBalancing = enabled;
	}

	public Measurements getMeasurements() {
		return measurements;
	}

	public void setMeasurements(Measurements measurements) {
		this.measurements = measurements;
	}
}
