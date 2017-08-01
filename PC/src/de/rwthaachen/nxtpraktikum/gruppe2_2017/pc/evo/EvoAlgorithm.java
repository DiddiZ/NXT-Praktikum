package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.EVO_COLLECT_TEST_DATA;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.PID_WEIGHT_ALL;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.CommunicatorPC;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.data.NXTData;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo.database.CSVDatabase;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo.database.EvoDatabase;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo.metrics.FitnessMetric;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo.metrics.FitnessMetrics;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui.UI;
import javafx.util.Pair;

/**
 * This class provides functionalities for automated calibration of PID weights for the NXT. 
 * It provides a test, that forces the NXT to balance and make the same movements each run, while it measures control information and sends it after the test to the PC.
 * This class provides two functions to choose next PID-values that have to be testet, a binary search for the optimum and and evolutionary algorithm. 
 * 
 * @author Gregor, Robin
 */
public class EvoAlgorithm extends Thread
{
	private final EvoDatabase db = new CSVDatabase(new File("evodb_heading6.csv"));

	private final UI ui;
	private final CommunicatorPC comm;
	private final NXTData data;
	static boolean running = false;

	// This values should allow the NXT to balance on its own and are set to the NXT before each test, to provide a clean start.
	private static final PIDWeights STANDARD_PID_WEIGHTS = new PIDWeights(-2.92236328, -16.25, 0.21320325, 0.2671875);

	/**
	 * class constructor.
	 * @param ui reference to the user interface
	 * @param comm reference to the PC communicator 
	 * @param data NXTData which model the state of the NXT on the PC side
	 */
	public EvoAlgorithm(UI ui, CommunicatorPC comm, NXTData data) {
		setDaemon(true);

		this.ui = ui;
		this.comm = comm;
		this.data = data;
	}

	@Override
	public void run() {
		if (running) {
			return;
		}
		running = true;
		try {
			// linearSearch(STANDARD_PID_WEIGHTS, 3, FitnessMetrics.LINEAR2, 0.005, 3);
			evolutionSearch(FitnessMetrics.LINEAR2, 2, 15, 0.01);
		} catch (InterruptedException | IOException ex) {
			ex.printStackTrace();
		}
		running = false;
	}

	/**
	 * Provides a binary selection of the PID-values. The PID value defined by {@link weightIdx} will be optimized.
	 * The algorithm searches first for the upper an lower limit, till it it falls down with the values. Starting with this boundaries it tests
	 * the selected PID-Weights. In each iteration the algorithm selects the limit with the best fitness value defined by {@link metric} to continue with and halves the
	 * limits of the boundaries. The algorithm terminates after an size of the boundaries defined by {@link delta} is reached. 
	 * The algorithm checks the current database for already measured values to provide faster termination. A minimal size of measurements
	 * defined by {@link minGroupSize} has to be reached within the current limits of the current iteration. If the number is not provided by the 
	 * database, the needed amount of tests will be performed. Each test will start with {@link initial} PIDWeights to allow a clean start.
	 * 
	 * @param initial PIDvalues used for a clean start
	 * @param weightIdx index of PIDvalue that will be optimized
	 * @param metric defines the fitness value which chooses the best values
	 * @param delta minimal size of the upper and lower limit difference before the algorithm terminates
	 * @param minGroupSize required number of measurements for every range of values to evaluate next iteration step
	 * @throws InterruptedException
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
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

	/**
	 * Computes the fitness value of measurements of {@link weights} in range of {@link epsilon} by given {@link metric}.
	 * If the database does not provide enough measurements, it performs additional measurements to reach the minimal
	 * amount of measurements defined by {@link minGroupSize}.
	 * 
	 * @param weights current PID weights that are measured
	 * @param weightIdx current index of the PID weights that is optimised
	 * @param epsilon current range of upper and lower bound.
	 * @param metric current metric to determine the fitness value
	 * @param minGroupSize minimal group size needed to compute the fitness value
	 * @return fitness value based on values in the database and the given range of selected PID values.
	 * @throws IOException
	 * @throws InterruptedException
	 */
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

	/**
	 * Provides an evolutionary selection of PID values that should be tested. 
	 * 
	 * The values are selected based the provided {@link metric}. The number of iterations is determined by {@link iterations}.
	 * In each iteration we create a pool of {@linkplain sizeOfPool} values defined by the following rules.
	 * {@link epsilon} determines the range of the mutations in percent. This range is halved each iteration.
	 * 
	 * In the first Iteration 20% of best values in the current database are selected. The remaining 80% are filled with random mutations.
	 * In the following Iteration we discard 10% of the current population and select 20% of best individuums from the remaining, last iteration.
	 * The next 40% of the population are created through linear crossing of values. The remaining 40% are created by randomly mutating the values of the current pool.
	 * 
	 * @param metric
	 * @param iterations
	 * @param sizeOfPool
	 * @param epsilon
	 * @throws InterruptedException
	 * @throws IOException
	 */
	private void evolutionSearch(FitnessMetric metric, int iterations, int sizeOfPool, double epsilon) throws InterruptedException, IOException {

		final List<Pair<PIDWeights, Measurements>> PIDpool = db.getBestPIDWeights(metric, sizeOfPool / 5);
		for (int i = 0; PIDpool.size() < sizeOfPool; i++) {
			PIDpool.add(newRandomizedPIDvalue(PIDpool.get(i % PIDpool.size()), epsilon));
		}

		for (int iterationNum = 0; iterationNum < iterations + 1; iterationNum++) {
			for (int i = 0; i < PIDpool.size(); i++) {
				final Pair<PIDWeights, Measurements> currentValue = PIDpool.get(i);
				final Measurements measurement = currentValue.getValue();
				measurement.addMeasurement(performTest(currentValue.getKey()));
			}

			{
				// ### CREATE NEW GENERATION ###

				// retain 1/5 of best individuums
				Collections.sort(PIDpool, (a, b) -> -Double.compare(metric.getFitness(a.getValue()), metric.getFitness(b.getValue())));
				final double threshold = metric.getFitness(PIDpool.get(sizeOfPool / 5).getValue());
				PIDpool.removeIf(a -> metric.getFitness(a.getValue()) < threshold);

				// cross 2/5 of population
				for (int i = 0; i < 2 * sizeOfPool / 5; i++) {
					final Pair<PIDWeights, Measurements> crossedPID = crossPIDvalues(PIDpool.get((int)(PIDpool.size() * Math.random())), PIDpool.get((int)(PIDpool.size() * Math.random())));
					PIDpool.add(crossedPID);
				}

				// kill 1/10 of population
				for (int i = 0; i < sizeOfPool / 10; i++) {
					PIDpool.remove(Math.random() * PIDpool.size());
				}

				// mutate 2/5 of population
				epsilon /= 2;
				for (int i = 0; PIDpool.size() < sizeOfPool; i++) {
					PIDpool.add(randomizePIDvalue(PIDpool.get(i % PIDpool.size()), epsilon));
				}
			}

			ui.showMessage("" + iterationNum + ". gen pool done.");

			final PIDWeights bestWeights = PIDpool.get(0).getKey();
			ui.showMessage("Best values: (" +
					bestWeights.weightGyroSpeed + ", " +
					bestWeights.weightGyroIntegral + ", " +
					bestWeights.weightMotorDistance + ", " +
					bestWeights.weightMotorSpeed + ", ");

		}
		ui.showMessage("Evolution search finished.");

	}

	/**
	 * Randomizes the given {@link value} by the percent range defined by {@link epsilon}
	 * @param value reference to PID values.
	 * @param epsilon mutation range in percent.
	 * @return randomized PID valeus
	 */
	private static Pair<PIDWeights, Measurements> randomizePIDvalue(Pair<PIDWeights, Measurements> value, double epsilon) {
		final PIDWeights weights = value.getKey();
		weights.weightGyroIntegral += (2 * Math.random() - 1.0) * epsilon * weights.weightGyroIntegral;
		weights.weightGyroSpeed += (2 * Math.random() - 1.0) * epsilon * weights.weightGyroSpeed;
		weights.weightMotorSpeed += (2 * Math.random() - 1.0) * epsilon * weights.weightMotorSpeed;
		weights.weightMotorDistance += (2 * Math.random() - 1.0) * epsilon * weights.weightMotorDistance;
		return value;

	}

	/**
	 * Creates new PIDValues based on the given {@link value}, randomized by the percent range defined by {@link epsilon}.
	 * @param value reference to PID values.
	 * @param epsilon mutation range in percent.
	 * @return randomized PID valeus
	 */
	private static Pair<PIDWeights, Measurements> newRandomizedPIDvalue(Pair<PIDWeights, Measurements> value, double epsilon) {
		final Measurements measurement = new Measurements(0, 0, 0, 0, 0);
		final PIDWeights weights = value.getKey().clone();
		weights.weightGyroIntegral += (2 * Math.random() - 1.0) * epsilon * weights.weightGyroIntegral;
		weights.weightGyroSpeed += (2 * Math.random() - 1.0) * epsilon * weights.weightGyroSpeed;
		weights.weightMotorSpeed += (2 * Math.random() - 1.0) * epsilon * weights.weightMotorSpeed;
		weights.weightMotorDistance += (2 * Math.random() - 1.0) * epsilon * weights.weightMotorDistance;
		return new Pair<>(weights, measurement);
	}

	/**
	 * Performs a linear addition of both provided values to create a new PID value.
	 * @param value1 PIDValue of first weights
	 * @param value2 PIDValue of second weights
	 * @return
	 */
	private static Pair<PIDWeights, Measurements> crossPIDvalues(Pair<PIDWeights, Measurements> value1, Pair<PIDWeights, Measurements> value2) {
		final Measurements measurement = new Measurements(0, 0, 0, 0, 0);
		final PIDWeights weight = new PIDWeights(0, 0, 0, 0);
		weight.weightGyroIntegral = (value1.getKey().weightGyroIntegral + value2.getKey().weightGyroIntegral) / 2.0;
		weight.weightGyroSpeed = (value1.getKey().weightGyroSpeed + value2.getKey().weightGyroSpeed) / 2.0;
		weight.weightMotorDistance = (value1.getKey().weightMotorDistance + value2.getKey().weightMotorDistance) / 2.0;
		weight.weightMotorSpeed = (value1.getKey().weightMotorSpeed + value2.getKey().weightMotorSpeed) / 2.0;

		return new Pair<>(weight, measurement);
	}

	/**
	 * Performs a test for the given {@link pidValues}. The test is structured into 10 different phases which are performed each time in the same order.
	 * The method checks first that the NXT is balancing, then it initializes a clean start with standard Pid values and the continues with
	 * the given {@link pidValues}. The sequenze of movement, turning and balancing at a spot is hard coded. After each movement command to the NXT
	 * we check if the NXT is still balancing and break the test in case the NXT has fallen to speed up the processing.
	 * After an finished test the method requests the measurements from the NXT and returns them.
	 * @param pidValues values that should be tested
	 * @return returns the measurements received from the NXT
	 * @throws InterruptedException
	 * @throws IOException
	 */
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

	/**
	 * Sends the {@link weights} given as parameter to the NXT and sets them.
	 * @param weights
	 */
	private void sendPIDWeights(PIDWeights weights) {
		comm.sendSet(PID_WEIGHT_ALL, weights.weightGyroSpeed, weights.weightGyroIntegral, weights.weightMotorDistance, weights.weightMotorSpeed);
	}
}
