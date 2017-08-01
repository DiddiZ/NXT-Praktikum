package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.data;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo.Measurements;

/**
 * Contains current state of NXT, which includes its position, heading and measurements.
 * Data gets updated every time it is received.
 * Provides get- and set-methods for the data of the NXT that is needed for calculations.
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

	public Measurements getMeasurements() {
		return measurements;
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

	public void setMeasurements(Measurements measurements) {
		this.measurements = measurements;
	}
}
