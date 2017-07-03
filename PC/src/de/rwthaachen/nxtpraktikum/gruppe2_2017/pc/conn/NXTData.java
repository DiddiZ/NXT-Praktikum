package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn;

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

	public float getHeading() {
		return heading;
	}

	public float getPositionX(){
		return positionX;
	}
	
	public float getPositionY(){
		return positionY;
	}
	
	void setPosition(float x, float y){
		this.positionX = x;
		this.positionY = y;
	}
	void setHeading(float heading) {
		this.heading = heading;
	}
}
