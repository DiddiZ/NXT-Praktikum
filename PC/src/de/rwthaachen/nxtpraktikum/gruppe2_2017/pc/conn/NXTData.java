package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn;

/**
 * Contains current state of NXT.
 * Data gets updated every time it is received.
 *
 * @author Robin & Fabian
 */
public final class NXTData
{
	private static float heading;
	private static float positionX;
	private static float positionY;
	private static boolean isBalancing;
	

	public static float getHeading() {
		return heading;
	}

	public static float getPositionX(){
		return positionX;
	}
	
	public static float getPositionY(){
		return positionY;
	}
	
	public static boolean getBalancing() {
		return isBalancing;
	}
	
	public static void setPosition(float x, float y){
		positionX = x;
		positionY = y;
	}
	public static void setHeading(float h) {
		heading = h;
	}
	public static void setBalancing(boolean enabled) {
		isBalancing = enabled;
	}
}
