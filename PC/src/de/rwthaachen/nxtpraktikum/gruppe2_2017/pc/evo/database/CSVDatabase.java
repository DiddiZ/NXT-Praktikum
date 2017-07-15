package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo.Measurements;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo.PIDWeights;

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
}
