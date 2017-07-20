package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo;

/**
 * Stores a evo test result.
 *
 * @author Robin
 */
public final class Measurements
{
	public double time, averageVoltage, averageDistanceDifference, averageHeadingDifference;
	int numberOfMeasurements;

	public Measurements(double time, double averageVoltage, double averageDistanceDifference, double averageHeadingDifference) {
		this.time = time;
		this.averageVoltage = averageVoltage;
		this.averageDistanceDifference = averageDistanceDifference;
		this.averageHeadingDifference = averageHeadingDifference;
		this.numberOfMeasurements = 1;
	}
	
	public Measurements(double time, double averageVoltage, double averageDistanceDifference, double averageHeadingDifference, int numberOfMeasurements) {
		this.time = time;
		this.averageVoltage = averageVoltage;
		this.averageDistanceDifference = averageDistanceDifference;
		this.averageHeadingDifference = averageHeadingDifference;
		this.numberOfMeasurements = numberOfMeasurements;
	}
	
	public void addMeasurement(double time, double averageVoltage, double averageDistanceDifference, double averageHeadingDifference) {
		this.time *= numberOfMeasurements;
		this.averageVoltage *= numberOfMeasurements;
		this.averageDistanceDifference *= numberOfMeasurements;
		this.averageHeadingDifference *= numberOfMeasurements;
		this.time += time;
		this.averageVoltage += averageVoltage;
		this.averageDistanceDifference += averageDistanceDifference;
		this.averageHeadingDifference += averageHeadingDifference;
		this.numberOfMeasurements += 1;
		this.time /= numberOfMeasurements;
		this.averageVoltage /= numberOfMeasurements;
		this.averageDistanceDifference /= numberOfMeasurements;
		this.averageHeadingDifference /= numberOfMeasurements;
	}
	
	public void addMeasurement(Measurements measurement) {
		this.time *= numberOfMeasurements;
		this.averageVoltage *= numberOfMeasurements;
		this.averageDistanceDifference *= numberOfMeasurements;
		this.averageHeadingDifference *= numberOfMeasurements;
		this.time += measurement.time;
		this.averageVoltage += measurement.averageVoltage;
		this.averageDistanceDifference += measurement.averageDistanceDifference;
		this.averageHeadingDifference += measurement.averageHeadingDifference;
		this.numberOfMeasurements += measurement.numberOfMeasurements;
		this.time /= numberOfMeasurements;
		this.averageVoltage /= numberOfMeasurements;
		this.averageDistanceDifference /= numberOfMeasurements;
		this.averageHeadingDifference /= numberOfMeasurements;
	}
	
	/**
	 * @return whether the NXT has fallen during the test.
	 */
	public boolean hasFallen() {
		return time < 25.0;
	}
}
