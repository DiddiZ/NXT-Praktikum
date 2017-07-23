package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo.Measurements;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo.PIDWeights;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo.metrics.FitnessMetric;
import javafx.util.Pair;

/**
 * Basic {@link EvoDatabase} storing dtata in a single CSV file.
 *
 * @author Robin
 */
public final class CSVDatabase implements EvoDatabase
{
	private final File file;

	public CSVDatabase(File file) {
		this.file = file;
	}

	@Override
	public void addData(PIDWeights pidValues, Measurements measurements) throws IOException {
		final StringBuilder sb = new StringBuilder();
		sb.append(pidValues.weightGyroSpeed);
		sb.append(';');
		sb.append(pidValues.weightGyroIntegral);
		sb.append(';');
		sb.append(pidValues.weightMotorDistance);
		sb.append(';');
		sb.append(pidValues.weightMotorSpeed);
		sb.append(';');
		sb.append(measurements.time);
		sb.append(';');
		sb.append(measurements.averageVoltage);
		sb.append(';');
		sb.append(measurements.averageDistanceDifference);
		sb.append(';');
		sb.append(measurements.averageHeadingDifference);
		sb.append('\n');

		try (FileWriter fw = new FileWriter(file, true)) {
			fw.write(sb.toString());
		}
	}

	@Override
	public List<Measurements> getMeasurements(PIDWeights lowerBound, PIDWeights upperBound) throws IOException {
		final List<Measurements> measurements = new ArrayList<>();

		try (final FileReader fr = new FileReader(file);
				final BufferedReader reader = new BufferedReader(fr)) {
			String line;
			while ((line = reader.readLine()) != null) { // Read all lines
				final String[] split = line.split(";");
				final double weightGyroSpeed = Double.parseDouble(split[0]);
				final double weightGyroIntegral = Double.parseDouble(split[1]);
				final double weightMotorDistance = Double.parseDouble(split[2]);
				final double weightMotorSpeed = Double.parseDouble(split[3]);

				// Check bounds
				if (lowerBound.weightGyroSpeed <= weightGyroSpeed && weightGyroSpeed <= upperBound.weightGyroSpeed
						&& lowerBound.weightGyroIntegral <= weightGyroIntegral && weightGyroIntegral <= upperBound.weightGyroIntegral
						&& lowerBound.weightMotorDistance <= weightMotorDistance && weightMotorDistance <= upperBound.weightMotorDistance
						&& lowerBound.weightMotorSpeed <= weightMotorSpeed && weightMotorSpeed <= upperBound.weightMotorSpeed) {
					// Add measurement
					measurements.add(new Measurements(Double.parseDouble(split[4]), Double.parseDouble(split[5]), Double.parseDouble(split[6]), Double.parseDouble(split[7])));
				}
			}
		}
		return measurements;
	}

	@Override
	public List<Pair<PIDWeights, Measurements>> getBestPIDWeights(FitnessMetric metric, int maxSize) throws IOException {
		final List<PIDWeights> PIDWeights = new ArrayList<>();
		final ArrayList<Measurements> measurements = new ArrayList<>();
		final SortedMap<Double, Integer> metrics = new TreeMap<>();
		try (final FileReader fr = new FileReader(file);
				final BufferedReader reader = new BufferedReader(fr)) {
			String line;
			while ((line = reader.readLine()) != null) { // Read all lines
				final String[] split = line.split(";");
				if (split.length < 8) {
					System.out.println("continue");
					continue;
				}
				final double weightGyroSpeed = Double.parseDouble(split[0]);
				final double weightGyroIntegral = Double.parseDouble(split[1]);
				final double weightMotorDistance = Double.parseDouble(split[2]);
				final double weightMotorSpeed = Double.parseDouble(split[3]);
				final double measurementTime = Double.parseDouble(split[4]);
				final double measurementVoltage = Double.parseDouble(split[5]);
				final double measurementDistance = Double.parseDouble(split[6]);
				final double measurementHeading = Double.parseDouble(split[7]);

				final PIDWeights weights = new PIDWeights(weightGyroSpeed, weightGyroIntegral, weightMotorDistance, weightMotorSpeed);
				final Measurements measurement = new Measurements(measurementTime, measurementVoltage, measurementDistance, measurementHeading);

				if (PIDWeights.contains(weights)) {
					final Integer index = PIDWeights.indexOf(weights);
					final Measurements measurement2 = measurements.get(index);
					measurement2.addMeasurement(measurement);
				} else {
					PIDWeights.add(weights);
					measurements.add(measurement);
				}

			}
		}

		// sort
		for (int i = 0; i < PIDWeights.size(); i++) {
			metrics.put(metric.getFitness(measurements.get(i)), i);
		}

		final List<Pair<PIDWeights, Measurements>> bestValues = new ArrayList<>();
		for (int i = 0; i < maxSize && i < PIDWeights.size(); i++) {
			final Double lastKey = metrics.lastKey();
			final Integer valueNumber = metrics.get(lastKey);
			metrics.remove(lastKey);
			bestValues.add(new Pair<>(PIDWeights.get(valueNumber), measurements.get(valueNumber)));
		}

		return bestValues;
	}
}
