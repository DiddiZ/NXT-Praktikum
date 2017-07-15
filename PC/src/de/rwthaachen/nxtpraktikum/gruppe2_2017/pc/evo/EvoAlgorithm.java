package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.EVO_COLLECT_TEST_DATA;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.EVO_MEASUREMENTS;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.PID_WEIGHT_ALL;
import java.io.File;
import java.io.IOException;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.CommunicatorPC;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.NXTData;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo.metrics.FitnessMetric;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo.metrics.FitnessMetrics;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui.UI;

/**
 * @author Gregor, Robin
 */
public class EvoAlgorithm extends Thread
{
	private final EvoDatabase db = new CSVDatabase(new File("evodb.csv"));

	private final UI ui;
	private final CommunicatorPC comm;
	private final NXTData data;

	private static final PIDWeights STANDARD_PID_WEIGHTS = new PIDWeights(-2.8, -13.0, 0.15, 0.225);

	public EvoAlgorithm(UI ui, CommunicatorPC comm, NXTData data) {
		setDaemon(true);

		this.ui = ui;
		this.comm = comm;
		this.data = data;
	}

	@Override
	public void run() {
		try {
			linearSearch(STANDARD_PID_WEIGHTS, 0, FitnessMetrics.LINEAR, 0.002);
		} catch (InterruptedException | IOException ex) {
			ex.printStackTrace();
		}
	}
	
	private void linearSearch(PIDWeights initial, int weightIdx, FitnessMetric metric, double delta) throws InterruptedException, IOException {
		// perform initial tests
		PIDWeights bestPIDWeights = initial;
		double bestFitness = metric.getFitness(performTest(initial));

		double epsilon = Math.abs(STANDARD_PID_WEIGHTS.get(weightIdx));

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
			
			ui.showMessage("Best: " + bestPIDWeights.get(weightIdx) + " ("+bestFitness+")");
			System.out.println("Best: " + bestPIDWeights.get(weightIdx) + " ("+bestFitness+")");
		}
		epsilon /= 2; // Compensate

		// Iterate to find best value
		while (epsilon > delta) {
			epsilon /= 2; // Half epsilon

			// Prepare candidates
			final PIDWeights lower = bestPIDWeights.clone();
			lower.set(weightIdx, bestPIDWeights.get(weightIdx) - epsilon);

			final PIDWeights upper = bestPIDWeights.clone();
			upper.set(weightIdx, bestPIDWeights.get(weightIdx) + epsilon);

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
			
			ui.showMessage("Best: " + bestPIDWeights.get(weightIdx) + " ("+bestFitness+")");
			System.out.println("Best: " + bestPIDWeights.get(weightIdx) + " ("+bestFitness+")");
		}

		ui.showMessage("Finished linear optimization for " + (weightIdx + 1) + ". PID value.");
	}

	private Measurements performTest(PIDWeights pidValues) throws InterruptedException, IOException {
		ui.showMessage("Testing " + pidValues.weightGyroSpeed + ", " + pidValues.weightGyroIntegral + ", " + pidValues.weightMotorDistance + ", " + pidValues.weightMotorSpeed);

		ui.setEvoWeights(pidValues);
		data.setMeasurements(null); // Clear measurement

		// set standard pid weights
		sendPIDWeights(STANDARD_PID_WEIGHTS);

		ui.setEvoAlgProcessing("00/10");
		do { // Balance for 5s without measuring to let the NXT stabilize itself
			if (!data.getBalancing()) {
				ui.showMessage("Start balancing to continue.");
				do {
					Thread.sleep(100);
				} while (!data.getBalancing());
			}
			Thread.sleep(5000);
		} while (!data.getBalancing()); // Check NXT hasn't fallen again
		
		Thread.sleep(5000);  // Balance for 5s without measuring to let the NXT stabilize itself
		
		int stateNo = 1;
		while (data.getBalancing() && stateNo < 10) {
			switch (stateNo) {
				case 1: // Start measurement
					
					sendPIDWeights(pidValues);
					comm.sendSet(EVO_COLLECT_TEST_DATA, true);

					ui.setEvoAlgProcessing("01/10");
					Thread.sleep(1000);
					break;
				case 2:
					comm.sendMove(20, true);
					ui.setEvoAlgProcessing("02/10");
					Thread.sleep(5000);
					break;
				case 3:
					comm.sendTurn(180, true);
					ui.setEvoAlgProcessing("03/10");
					Thread.sleep(2000);
					break;
				case 4:
					comm.sendMove(20, true);
					ui.setEvoAlgProcessing("04/10");
					Thread.sleep(5000);
					break;
				case 5:
					comm.sendTurn(-180, true);
					ui.setEvoAlgProcessing("05/10");
					Thread.sleep(2000);
					break;
				case 6:
					comm.sendMove(10, true);
					ui.setEvoAlgProcessing("06/10");
					Thread.sleep(3000);
					break;
				case 7:
					comm.sendTurn(-180, true);
					ui.setEvoAlgProcessing("07/10");
					Thread.sleep(2000);
					break;
				case 8:
					comm.sendMove(10, true);
					ui.setEvoAlgProcessing("08/10");
					Thread.sleep(3000);
					break;
				case 9:
					comm.sendTurn(180, true);
					ui.setEvoAlgProcessing("09/10");
					Thread.sleep(2000);
					break;
			}
			stateNo++;
		}

		ui.setEvoAlgProcessing("10/10");

		// Wait for measurements
		while (data.getMeasurements() == null) {
			Thread.sleep(100);
		}
		final Measurements measurements = data.getMeasurements();

		ui.showMessage("Passed test time: " + measurements.time);
		ui.showMessage("Battery: " + measurements.averageVoltage);
		ui.showMessage("Distance: " + measurements.averageDistanceDifference);
		ui.showMessage("Heading: " + measurements.averageDistanceDifference);

		db.addData(pidValues, measurements); // Store test result

		return measurements;
	}

	private void sendPIDWeights(PIDWeights weights) {
		comm.sendSet(PID_WEIGHT_ALL, weights.weightGyroSpeed, weights.weightGyroIntegral, weights.weightMotorDistance, weights.weightMotorSpeed);
	}
}
