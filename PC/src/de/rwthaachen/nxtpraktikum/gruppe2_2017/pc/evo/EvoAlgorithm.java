/**
 * This class runs the evolution algorithm. It manages all data and computes next iteration values.
 * 
 * @author Gregor
 */
package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.nxt.motorcontrol.MotorController;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui.Send;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui.UserInterface;

public class EvoAlgorithm implements EvoInterface {

	private UserInterface ui;
	private Send send;
	private int currentState = 0;
	
	private double[] currentValues;
	private List<double[]> unprocessedPool;
	private List<double[]> processedPool;
	
	private double epsilon;
	final int processingPidNo = 0;
	
	public EvoAlgorithm(UserInterface ui, Send send) {
		this.ui = ui;
		this.send = send;
	}
	
	@Override
	public void setDataSet(double paramMotorPower, double paramBatteryVoltage, double paramDistanceDifference, double paramHeadingDifference) {
		ui.showMessage("Iteration finished.");
		if (paramMotorPower == Double.MAX_VALUE) {
			ui.showMessage("Please restart the balancing thread to continue.");
		}
		currentValues[4] = paramMotorPower;
		currentValues[5] = paramBatteryVoltage;
		currentValues[6] = paramDistanceDifference;
		currentValues[7] = paramHeadingDifference;
		processedPool.add(currentValues);
		saveCurrentValues();
		
		chooseNextDataSet();
	}

	@Override
	public void setState(int paramState) {
		currentState = paramState;
	}
	
	
	protected void chooseNextDataSet() {
		if (!unprocessedPool.isEmpty()) {
			currentValues = unprocessedPool.get(0);
			if (currentValues.length != 8) {
				System.out.println("Error in Evo algorithm. Number of values != 8");
				ui.showMessage("Error in Evo algorithm occured");
			}
			unprocessedPool.remove(0);
			startNextIteration(currentValues[0],currentValues[1],currentValues[2],currentValues[3]);
		} else {
			ui.showMessage("All data sets processed.");
			
			ui.showMessage("Create new test pool.");
			createPool();
		}
	}
	
	
	protected void startNextIteration(double paramPID1, double paramPID2, double paramPID3, double paramPID4) {
		int valuesInPool = processedPool.size() + unprocessedPool.size();
		int valuesProcessed = processedPool.size() + 1;
		ui.showMessage("Start iteration: " + valuesProcessed + "/" + valuesInPool);
		send.sendEvoStart(paramPID1, paramPID2, paramPID3, paramPID4);
		currentState = 0;
	}
	
	protected void startTest() {
		createPool();	
		
	}
	
	protected void createPool() {
		final int iterationNum = 10;
		
		if (processedPool.isEmpty()) {
			epsilon = 0.2;
			
			currentValues = new double[8];
			currentValues[0] = MotorController.WEIGHT_GYRO_SPEED;
			currentValues[1] = MotorController.WEIGHT_GYRO_INTEGRAL;
			currentValues[2] = MotorController.WEIGHT_MOTOR_SPEED;
			currentValues[3] = MotorController.WEIGHT_MOTOR_DISTANCE;

			for (int i = 0; i < iterationNum ; i++) {
				double testValues[] = currentValues.clone();
				testValues[processingPidNo] += i * epsilon;
				unprocessedPool.add(testValues);
				testValues = currentValues.clone();
				testValues[processingPidNo] -= i * epsilon;
				unprocessedPool.add(testValues);
			}
			
		} else {
			double minValues[] = processedPool.get(0);
			for (double values[] : processedPool) {
				double minValuesWeight = minValues[4]+minValues[6]+minValues[7];
				double valuesWeight = values[4] + values[5] + values[7];
				if (valuesWeight < minValuesWeight) {
					minValues = values;
				}
			}
			currentValues = minValues.clone();
			epsilon = (double) (processedPool.indexOf(minValues) + 1) / (double) iterationNum / 2.0 * epsilon;
			
			processedPool.clear();
		}		
		
		for (int i = 1; i<iterationNum; i++) {
			double testValues[] = currentValues.clone();
			testValues[processingPidNo] += i * epsilon;
			unprocessedPool.add(testValues);
			testValues = currentValues.clone();
			testValues[processingPidNo] -= i * epsilon;
			unprocessedPool.add(testValues);
		}
		
		if (epsilon > 0.001) {
			chooseNextDataSet();
		}
				
	}
	
	protected void saveCurrentValues() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i<8 ; i++)
			sb.append(currentValues[i] + ',');
		sb.append('\n');
		
	
		try {
			FileWriter writer = new FileWriter("test.csv",true);
			writer.write(sb.toString());
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	
	

}
