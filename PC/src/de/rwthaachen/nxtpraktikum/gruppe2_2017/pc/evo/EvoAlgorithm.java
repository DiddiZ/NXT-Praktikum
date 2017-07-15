package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.EVO_COLLECT_TEST_DATA;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.EVO_MEASUREMENTS;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.PID_GYRO_INTEGRAL;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.PID_GYRO_SPEED;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.PID_MOTOR_DISTANCE;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.PID_MOTOR_SPEED;
import java.io.File;
import java.io.IOException;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.NXTData;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo.metrics.FitnessMetric;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo.metrics.FitnessMetrics;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui.Send;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui.UI;

/**
 * @author Gregor, Robin
 */
public class EvoAlgorithm extends Thread
{
	private final EvoDatabase db = new CSVDatabase(new File("test.csv"));

	private final UI ui;
	private final Send send;
	private final NXTData data;

	private static final PIDWeights STANDARD_PID_WEIGHTS = new PIDWeights(-2.8, -13.0, 0.15, 0.225);

	public EvoAlgorithm(UI ui, Send send, NXTData data) {
		setDaemon(true);

		this.ui = ui;
		this.send = send;
		this.data = data;
	}

	@Override
	public void run() {
		try {
			linearSearch(STANDARD_PID_WEIGHTS, 0, FitnessMetrics.LINEAR, 0.001);
		} catch (InterruptedException | IOException ex) {
			ex.printStackTrace();
		}
	}

	private void linearSearch(PIDWeights initial, int weightIdx, FitnessMetric metric, double delta) throws InterruptedException, IOException {
		// perform initial tests
		PIDWeights bestPIDWeights = initial;
		double bestFitness = metric.getFitness(performTest(initial));

		double epsilon = STANDARD_PID_WEIGHTS.get(weightIdx);

		// Find lower or higher optimum
		for (PIDWeights lower = bestPIDWeights, upper = bestPIDWeights; lower == bestPIDWeights || upper == bestPIDWeights; epsilon *= 2) {
			// Prepare candidates
			lower = bestPIDWeights.clone();
			lower.set(weightIdx, STANDARD_PID_WEIGHTS.get(weightIdx) - epsilon);

			upper = bestPIDWeights.clone();
			upper.set(weightIdx, STANDARD_PID_WEIGHTS.get(weightIdx) + epsilon);

			// Evaluate candidates
			final double lowerFitness = metric.getFitness(performTest(lower));
			if (lowerFitness > bestFitness) {
				bestFitness = lowerFitness;
				bestPIDWeights = lower;
			}

			final double upperFitness = metric.getFitness(performTest(upper));
			if (upperFitness > bestFitness) {
				bestFitness = upperFitness;
				bestPIDWeights = upper;
			}
		}

		// Iterate to find best value
		while (epsilon > delta) {
			epsilon /= 2; // Half epsilon

			// Prepare candidates
			final PIDWeights lower = bestPIDWeights.clone();
			lower.set(weightIdx, STANDARD_PID_WEIGHTS.get(weightIdx) - epsilon);

			final PIDWeights upper = bestPIDWeights.clone();
			upper.set(weightIdx, STANDARD_PID_WEIGHTS.get(weightIdx) + epsilon);

			// Evaluate candidates
			final double lowerFitness = metric.getFitness(performTest(lower));
			if (lowerFitness > bestFitness) {
				bestFitness = lowerFitness;
				bestPIDWeights = lower;
			}

			final double upperFitness = metric.getFitness(performTest(upper));
			if (upperFitness > bestFitness) {
				bestFitness = upperFitness;
				bestPIDWeights = upper;
			}
		}

		ui.showMessage("Finished linear optimization for " + (weightIdx + 1) + ". PID value.");
	}

	private Measurements performTest(PIDWeights pidValues) throws InterruptedException, IOException {
		ui.showMessage("Start test.");

		ui.setEvoAlgGS(pidValues.weightGyroSpeed);
		ui.setEvoAlgGI(pidValues.weightGyroIntegral);
		ui.setEvoAlgMS(pidValues.weightMotorSpeed);
		ui.setEvoAlgMD(pidValues.weightMotorDistance);

		// set standard pid weights
		send.sendSetDouble(PID_GYRO_SPEED, STANDARD_PID_WEIGHTS.weightGyroSpeed);
		send.sendSetDouble(PID_GYRO_INTEGRAL, STANDARD_PID_WEIGHTS.weightGyroIntegral);
		send.sendSetDouble(PID_MOTOR_DISTANCE, STANDARD_PID_WEIGHTS.weightMotorDistance);
		send.sendSetDouble(PID_MOTOR_SPEED, STANDARD_PID_WEIGHTS.weightMotorSpeed);

		while (!data.getBalancing()) {
			ui.showMessage("Start balancing thread to continue.");
			Thread.sleep(2000);
		}

		int stateNo = 0;
		while (data.getBalancing() && stateNo < 10) {
			switch (stateNo) {
				case 0:
					ui.setEvoAlgProcessing("00/10");
					Thread.sleep(5000);
					break;
				case 1:
					send.sendSetDouble(PID_GYRO_SPEED, pidValues.weightGyroSpeed);
					send.sendSetDouble(PID_GYRO_INTEGRAL, pidValues.weightGyroIntegral);
					send.sendSetDouble(PID_MOTOR_DISTANCE, pidValues.weightMotorDistance);
					send.sendSetDouble(PID_MOTOR_SPEED, pidValues.weightMotorSpeed);

					send.sendSetBoolean(EVO_COLLECT_TEST_DATA, true);

					ui.setEvoAlgProcessing("01/10");
					Thread.sleep(1000);
					break;
				case 2:
					send.sendMove(20);
					ui.setEvoAlgProcessing("02/10");
					Thread.sleep(5000);
					break;
				case 3:
					send.sendTurn(180);
					ui.setEvoAlgProcessing("03/10");
					Thread.sleep(2000);
					break;
				case 4:
					send.sendMove(20);
					ui.setEvoAlgProcessing("04/10");
					Thread.sleep(5000);
					break;
				case 5:
					send.sendTurn(-180);
					ui.setEvoAlgProcessing("05/10");
					Thread.sleep(2000);
					break;
				case 6:
					send.sendMove(10);
					ui.setEvoAlgProcessing("06/10");
					Thread.sleep(3000);
					break;
				case 7:
					send.sendTurn(-180);
					ui.setEvoAlgProcessing("07/10");
					Thread.sleep(2000);
					break;
				case 8:
					send.sendMove(10);
					ui.setEvoAlgProcessing("08/10");
					Thread.sleep(3000);
					break;
				case 9:
					send.sendTurn(180);
					ui.setEvoAlgProcessing("09/10");
					Thread.sleep(2000);
					break;
			}
			stateNo++;
		}

		data.setMeasurements(null);
		send.sendSetBoolean(EVO_COLLECT_TEST_DATA, false);
		send.sendGetByteQuiet(EVO_MEASUREMENTS);

		ui.setEvoAlgProcessing("10/10");

		// Wait for measurements
		while (data.getMeasurements() == null) {
			data.wait();
		}
		final Measurements measurements = data.getMeasurements();

		ui.showMessage("Passed test time: " + measurements.time);
		ui.showMessage("Battery: " + measurements.averageVoltage);
		ui.showMessage("Distance: " + measurements.averageDistanceDifference);
		ui.showMessage("Heading: " + measurements.averageDistanceDifference);

		db.addData(pidValues, measurements); // Store test result

		return measurements;
	}
}
