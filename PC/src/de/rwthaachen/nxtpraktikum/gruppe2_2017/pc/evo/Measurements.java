package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo;

/**
 * Stores a evo test result.
 *
 * @author Robin
 */
public final class Measurements
{
	public final double time, averageVoltage, averageDistanceDifference, averageHeadingDifference;

	public Measurements(double time, double averageVoltage, double averageDistanceDifference, double averageHeadingDifference) {
		this.time = time;
		this.averageVoltage = averageVoltage;
		this.averageDistanceDifference = averageDistanceDifference;
		this.averageHeadingDifference = averageHeadingDifference;
	}
}
