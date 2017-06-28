package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn;

/**
 * Contains current state of NXT.
 * Data gets updated every time it is received.
 *
 * @author Robin
 */
public final class NXTData
{
	private float heading;

	public float getHeading() {
		return heading;
	}

	void setHeading(float heading) {
		this.heading = heading;
	}
}
