/**
 * Interface for the EvoAlgorithm class
 * 
 * @author Gregor 
 */

package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo;

public interface EvoInterface {
	
	public void setDataSet(double paramMotorPower, double paramBatteryVoltage, double paramDistanceDifference, double paramHeadingDifference);
	public void setState(int paramState);
	
}
