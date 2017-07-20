package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.EVO_COLLECT_TEST_DATA;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.PID_WEIGHT_ALL;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.CommunicatorPC;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.NXTData;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo.database.CSVDatabase;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo.database.EvoDatabase;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo.metrics.FitnessMetric;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo.metrics.FitnessMetrics;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui.UI;
import javafx.util.Pair;

/**
 * @author Gregor, Robin
 */
public class EvoAlgorithm extends Thread
{
	private final EvoDatabase db = new CSVDatabase(new File("evodb_heading6.csv"));

	private final UI ui;
	private final CommunicatorPC comm;
	private final NXTData data;
																		//-2.87,       -16.25, 0.1199268
	private static final PIDWeights STANDARD_PID_WEIGHTS = new PIDWeights(-2.92236328, -16.25, 0.21320325, 0.2671875);

	public EvoAlgorithm(UI ui, CommunicatorPC comm, NXTData data) {
		setDaemon(true);

		this.ui = ui;
		this.comm = comm;
		this.data = data;
	}

	@Override
	public void run() {
		try {
			linearSearch(STANDARD_PID_WEIGHTS, 3, FitnessMetrics.LINEAR2, 0.005, 3);
		} catch (InterruptedException | IOException ex) {
			ex.printStackTrace();
		}
	}

	private void linearSearch(PIDWeights initial, int weightIdx, FitnessMetric metric, double delta, int minGroupSize) throws InterruptedException, IOException {
		double epsilon = Math.abs(STANDARD_PID_WEIGHTS.get(weightIdx));

		// perform initial tests
		PIDWeights bestPIDWeights = initial;
		double bestFitness = getFitness(initial, weightIdx, epsilon, metric, minGroupSize);

		// Find lower or higher optimum
		for (PIDWeights lower = bestPIDWeights, upper = bestPIDWeights; lower == bestPIDWeights || upper == bestPIDWeights; epsilon *= 2) {
			// Prepare candidates
			lower = bestPIDWeights.clone();
			lower.set(weightIdx, STANDARD_PID_WEIGHTS.get(weightIdx) - epsilon);

			upper = bestPIDWeights.clone();
			upper.set(weightIdx, STANDARD_PID_WEIGHTS.get(weightIdx) + epsilon);

			// Evaluate candidates
			final double lowerFitness = getFitness(lower, weightIdx, epsilon, metric, minGroupSize);
			if (lowerFitness > bestFitness) {
				bestFitness = lowerFitness;
				bestPIDWeights = lower;
			}

			final double upperFitness = getFitness(upper, weightIdx, epsilon, metric, minGroupSize);
			if (upperFitness > bestFitness) {
				bestFitness = upperFitness;
				bestPIDWeights = upper;
			}

			ui.showMessage("Best: " + bestPIDWeights.get(weightIdx) + " (" + bestFitness + ")");
			System.out.println("Best: " + bestPIDWeights.get(weightIdx) + " (" + bestFitness + ")");
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
			final double lowerFitness = getFitness(lower, weightIdx, epsilon, metric, minGroupSize);
			if (lowerFitness > bestFitness) {
				bestFitness = lowerFitness;
				bestPIDWeights = lower;
			}

			final double upperFitness = getFitness(upper, weightIdx, epsilon, metric, minGroupSize);
			if (upperFitness > bestFitness) {
				bestFitness = upperFitness;
				bestPIDWeights = upper;
			}

			ui.showMessage("Best: " + bestPIDWeights.get(weightIdx) + " (" + bestFitness + ")");
			System.out.println("Best: " + bestPIDWeights.get(weightIdx) + " (" + bestFitness + ")");
		}

		ui.showMessage("Finished linear optimization for " + (weightIdx + 1) + ". PID value.");
	}

	private double getFitness(PIDWeights weights, int weightIdx, double epsilon, FitnessMetric metric, int minGroupSize) throws IOException, InterruptedException {
		final PIDWeights lowerBound = weights.clone();
		lowerBound.set(weightIdx, weights.get(weightIdx) - epsilon / 2);
		final PIDWeights upperBound = weights.clone();
		upperBound.set(weightIdx, weights.get(weightIdx) + epsilon / 2);

		final List<Measurements> measurements = db.getMeasurements(lowerBound, upperBound);
		System.out.println(measurements.size() + " measurements found, performing " + Math.max(0, minGroupSize - measurements.size()) + " additions tests.");

		for (int i = measurements.size(); i < minGroupSize; i++) {
			measurements.add(performTest(weights));
		}

		final double fitness = FitnessMetric.getFitness(measurements, metric);
		System.out.println("Fitness: " + fitness);
		return fitness;
	}
	
	private void evolutionSearch(PIDWeights initial, FitnessMetric metric, int iterations, int sizeOfPool, double epsilon) throws InterruptedException, IOException {
		
		List<Pair<PIDWeights,Measurements>> PIDpool  = db.getBestPIDWeights(metric, sizeOfPool / 5);
		for (int i = 0; PIDpool.size() < sizeOfPool;i++) {
			PIDpool.add(newRandomizedPIDvalue(PIDpool.get(i % PIDpool.size()),epsilon));
		}

		
		for (int iterationNum = 0; iterationNum < iterations; iterationNum++) {
			for (int i = 0; i < PIDpool.size(); i++) {
				Pair<PIDWeights,Measurements> currentValue = PIDpool.get(i);
				Measurements measurement = currentValue.getValue();
				measurement.addMeasurement(performTest(currentValue.getKey()));
			}
			
			
			// sort values by metric
			SortedMap<Double,Pair<PIDWeights,Measurements>> metrics = new TreeMap<Double,Pair<PIDWeights,Measurements>>();
			for (int i = 0; i < PIDpool.size(); i++) {
				Pair<PIDWeights,Measurements> currentValue = PIDpool.get(i);
				metrics.put(metric.getFitness(currentValue.getValue()), currentValue);
			}
			PIDpool.clear();
			
			// get 1/5 of best results
			for (int i = 0; i< sizeOfPool / 5; i++) {
				Double index = metrics.lastKey();
				PIDpool.add(metrics.get(index));
				metrics.remove(index);
			}
			// cross 2/5 of values
			for (int i = 0; i < 2 * sizeOfPool / 5; i++) {
				Pair<PIDWeights,Measurements> crossedPID = crossPIDvalues(PIDpool.get((int) ((PIDpool.size() - 1) * Math.random())), PIDpool.get((int) ((PIDpool.size() - 1)* Math.random())));
				PIDpool.add(crossedPID);
			}
			// mutate 2/5 of pool
			epsilon /= 2;
			for (int i = 0; PIDpool.size() < sizeOfPool;i++) {
				PIDpool.add(randomizePIDvalue(PIDpool.get(i % PIDpool.size()),epsilon));
			}
			
		}
		
		
	}
	
	private Pair<PIDWeights,Measurements> randomizePIDvalue (Pair<PIDWeights,Measurements> value, double epsilon) {
		PIDWeights weights = value.getKey();
		weights.weightGyroIntegral += (2*Math.random() - 1.0) * epsilon * weights.weightGyroIntegral; 
		weights.weightGyroSpeed += (2*Math.random() - 1.0) * epsilon * weights.weightGyroSpeed;
		weights.weightMotorSpeed += (2*Math.random() - 1.0) * epsilon * weights.weightMotorSpeed;
		weights.weightMotorDistance += (2*Math.random() - 1.0) * epsilon * weights.weightMotorDistance;
		return value;
	
	}
	
	private Pair<PIDWeights,Measurements> newRandomizedPIDvalue (Pair<PIDWeights,Measurements> value, double epsilon) {
		Measurements measurement = new Measurements(0,0,0,0,0);
		PIDWeights weights = value.getKey().clone();
		weights.weightGyroIntegral += (2*Math.random() - 1.0) * epsilon * weights.weightGyroIntegral; 
		weights.weightGyroSpeed += (2*Math.random() - 1.0) * epsilon * weights.weightGyroSpeed;
		weights.weightMotorSpeed += (2*Math.random() - 1.0) * epsilon * weights.weightMotorSpeed;
		weights.weightMotorDistance += (2*Math.random() - 1.0) * epsilon * weights.weightMotorDistance;
		return new Pair<PIDWeights, Measurements>(weights, measurement);
	}
	
	private Pair<PIDWeights,Measurements> crossPIDvalues(Pair<PIDWeights,Measurements> value1, Pair<PIDWeights,Measurements> value2) {
		Measurements measurement = new Measurements(0,0,0,0,0);
		PIDWeights weight = new PIDWeights(0,0,0,0);
		weight.weightGyroIntegral = (value1.getKey().weightGyroIntegral + value2.getKey().weightGyroIntegral) / 2.0;
		weight.weightGyroSpeed = (value1.getKey().weightGyroSpeed + value2.getKey().weightGyroSpeed) / 2.0;
		weight.weightMotorDistance = (value1.getKey().weightMotorDistance + value2.getKey().weightMotorDistance) / 2.0;
		weight.weightMotorSpeed = (value1.getKey().weightMotorSpeed + value2.getKey().weightMotorSpeed) / 2.0;
		
		return new Pair<PIDWeights, Measurements>(weight,measurement);
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

		Thread.sleep(5000); // Balance for 5s without measuring to let the NXT stabilize itself

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
		ui.showMessage("Heading: " + measurements.averageHeadingDifference);

		db.addData(pidValues, measurements); // Store test result

		return measurements;
	}

	private void sendPIDWeights(PIDWeights weights) {
		comm.sendSet(PID_WEIGHT_ALL, weights.weightGyroSpeed, weights.weightGyroIntegral, weights.weightMotorDistance, weights.weightMotorSpeed);
	}
}
