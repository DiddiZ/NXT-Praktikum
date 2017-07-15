package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
}
