package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo;

/**
 * Stores a evo test result.
 *
 * @author Robin, Gregor
 */
public final class Measurements
{
	public double time, averageVoltage, averageDistanceDifference, averageHeadingDifference;
	int numberOfMeasurements;

	/**
	 * constructor
	 * @param time initial average up time
	 * @param averageVoltage initial average voltage
	 * @param averageDistanceDifference initial average distance difference
	 * @param averageHeadingDifference initial average heading difference
	 */
	public Measurements(double time, double averageVoltage, double averageDistanceDifference, double averageHeadingDifference) {
		this.time = time;
		this.averageVoltage = averageVoltage;
		this.averageDistanceDifference = averageDistanceDifference;
		this.averageHeadingDifference = averageHeadingDifference;
		numberOfMeasurements = 1;
	}

	public Measurements(double time, double averageVoltage, double averageDistanceDifference, double averageHeadingDifference, int numberOfMeasurements) {
		this.time = time;
		this.averageVoltage = averageVoltage;
		this.averageDistanceDifference = averageDistanceDifference;
		this.averageHeadingDifference = averageHeadingDifference;
		this.numberOfMeasurements = numberOfMeasurements;
	}

	/**
	 * Performs a linear combination of given measurement values and the current one.
	 * @param time
	 * @param averageVoltage
	 * @param averageDistanceDifference
	 * @param averageHeadingDifference
	 */
	public void addMeasurement(double time, double averageVoltage, double averageDistanceDifference, double averageHeadingDifference) {
		this.time *= numberOfMeasurements;
		this.averageVoltage *= numberOfMeasurements;
		this.averageDistanceDifference *= numberOfMeasurements;
		this.averageHeadingDifference *= numberOfMeasurements;
		this.time += time;
		this.averageVoltage += averageVoltage;
		this.averageDistanceDifference += averageDistanceDifference;
		this.averageHeadingDifference += averageHeadingDifference;
		numberOfMeasurements += 1;
		this.time /= numberOfMeasurements;
		this.averageVoltage /= numberOfMeasurements;
		this.averageDistanceDifference /= numberOfMeasurements;
		this.averageHeadingDifference /= numberOfMeasurements;
	}

	/**
	 * Performs linear combination of the current Measurement values and {@link measurement}.
	 * @param measurement
	 */
	public void addMeasurement(Measurements measurement) {
		time *= numberOfMeasurements;
		averageVoltage *= numberOfMeasurements;
		averageDistanceDifference *= numberOfMeasurements;
		averageHeadingDifference *= numberOfMeasurements;
		time += measurement.time;
		averageVoltage += measurement.averageVoltage;
		averageDistanceDifference += measurement.averageDistanceDifference;
		averageHeadingDifference += measurement.averageHeadingDifference;
		numberOfMeasurements += measurement.numberOfMeasurements;
		time /= numberOfMeasurements;
		averageVoltage /= numberOfMeasurements;
		averageDistanceDifference /= numberOfMeasurements;
		averageHeadingDifference /= numberOfMeasurements;
	}

	/**
	 * @return whether the NXT has fallen during the test.
	 */
	public boolean hasFallen() {
		return time < 25.0;
	}
}
