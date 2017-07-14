package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.NXTData;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui.Send;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui.UI;



public class EvoAlgorithm extends Thread{

	private UI ui;
	private Send send;
	private NXTData data;
	
	private final Double threadRunTime;
	private final Double threadStartTime;
	private final Double threadStopTime;
	
	public static Double collectedBatteryIntegral;
	public static Double collectedDistanceIntegral;
	public static Double collectedHeadingIntegral;
	public static Double passedTestTime;
	private Double currentPidValues[] = new Double[4];
	
	private Double standardPidValues[] = new Double[4];
	
	public EvoAlgorithm(UI ui, Send send, NXTData data){
		this.setDaemon(true);
		
		this.ui = ui;
		this.send = send;
		this.data = data;
		
		threadRunTime = 5000d;
		threadStartTime = Double.valueOf(System.currentTimeMillis());
		threadStopTime = System.currentTimeMillis() + threadRunTime;
		
		standardPidValues[0] 	= -2.8; //WEIGHT_GYRO_SPEED
		standardPidValues[1]	= -13.0; // WEIGHT_GYRO_INTEGRAL
		standardPidValues[2]	= 0.15; //WEIGHT_MOTOR_DISTANCE
		standardPidValues[3]	= 0.225; //WEIGHT_MOTOR_SPEED
		
	}
	
	@Override
	public void run() {
		linearSearch();
	}
	
	
	private void linearSearch() {
		
		HashMap<Double,Double[]> valueMap = new HashMap<Double,Double[]>();
		SortedSet<Double> costList = new TreeSet<Double>();
		
		
		int testedPidValue = 0;
		int iterationNo = 0;
		Double delta = 0.001;
		Double epsilon = standardPidValues[testedPidValue];
		
		Double[] lowerPidValues = standardPidValues.clone();
		Double[] upperPidValues = standardPidValues.clone();		
		
		Double upperCostValue;
		Double lowerCostValue;
		
		
		//search for lower bound
		iterationNo = 0;
		while (data.getBalancing()) {
			lowerPidValues[testedPidValue] = standardPidValues[testedPidValue] - epsilon * Math.pow(2,iterationNo);
			performTest(lowerPidValues);
			lowerCostValue = getCostValue();
			valueMap.put(lowerCostValue, lowerPidValues);
			costList.add(lowerCostValue);
			iterationNo++;
		}
		
		while (!data.getBalancing()) {
			ui.showMessage("Start balancing thread to continue.");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//search for upper bound
		iterationNo = 0;
		while (data.getBalancing()) {
			upperPidValues[testedPidValue] = standardPidValues[testedPidValue] + epsilon * Math.pow(2,iterationNo);
			performTest(upperPidValues);
			upperCostValue = getCostValue();
			valueMap.put(upperCostValue, upperPidValues);
			costList.add(upperCostValue);
			iterationNo++;
		}
		
		//perform initial tests
		performTest(standardPidValues);
		Double currentCost = getCostValue();
		valueMap.put(currentCost, standardPidValues);
		costList.add(currentCost);
		
		//not Iterate to find best value
		iterationNo = 1;
		while (iterationNo < 4) {//(epsilon.doubleValue() > delta.doubleValue()) {
			Double minimalValue = costList.first();
			Double[] minimalPidValue = valueMap.get(minimalValue);
			
			epsilon = standardPidValues[testedPidValue] * Math.pow(2, -iterationNo);
			
			upperPidValues = minimalPidValue.clone();
			upperPidValues[testedPidValue] += epsilon;
			
			lowerPidValues = minimalPidValue.clone();
			lowerPidValues[testedPidValue] -= epsilon;
			
			performTest(upperPidValues);
			currentCost = getCostValue();
			valueMap.put(currentCost, upperPidValues);
			costList.add(currentCost);
			
			performTest(upperPidValues);
			currentCost = getCostValue();
			valueMap.put(currentCost, upperPidValues);
			costList.add(currentCost);
			
			iterationNo++;
		}
				
		ui.showMessage("Finished linear optimization for " + (testedPidValue+1) +". PID value.");
		
	}
	
	private void performTest(Double pidValues[]) {
		ui.showMessage("Start test.");
		currentPidValues = pidValues.clone();
		
		ui.setEvoAlgGS(pidValues[0]);
		ui.setEvoAlgGI(pidValues[1]);
		ui.setEvoAlgMS(pidValues[2]);
		ui.setEvoAlgMD(pidValues[3]);
		
		send.sendSetDouble(PID_GYRO_SPEED, 		standardPidValues[0]);
		send.sendSetDouble(PID_GYRO_INTEGRAL, 	standardPidValues[1]);
		send.sendSetDouble(PID_MOTOR_DISTANCE, 	standardPidValues[2]);
		send.sendSetDouble(PID_MOTOR_SPEED, 	standardPidValues[3]);
		
		while (!data.getBalancing()) {
			ui.showMessage("Start balancing thread to continue.");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		int stateNo = 0;
		while (data.getBalancing() && stateNo < 10) {
			try {
						
				switch (stateNo) {
				case 0:					
					ui.setEvoAlgProcessing("00/10");
					Thread.sleep(5000);
					break;
				case 1:
					send.sendSetDouble(PID_GYRO_SPEED, 		pidValues[0]);
					send.sendSetDouble(PID_GYRO_INTEGRAL, 	pidValues[1]);
					send.sendSetDouble(PID_MOTOR_DISTANCE, 	pidValues[2]);
					send.sendSetDouble(PID_MOTOR_SPEED, 	pidValues[3]);
					
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
					
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			stateNo++;
		}
		
		send.sendSetBoolean(EVO_COLLECT_TEST_DATA, false);
		
		send.sendGetByteQuiet(EVO_BATTERY);
		send.sendGetByteQuiet(EVO_DISTANCE);
		send.sendGetByteQuiet(EVO_HEADING);
		send.sendGetByteQuiet(EVO_TIME);
		
		ui.setEvoAlgProcessing("10/10");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ui.showMessage("Passed test time: " + passedTestTime);
		ui.showMessage("Battery: " + (collectedBatteryIntegral / passedTestTime));
		ui.showMessage("Distance: " + (collectedDistanceIntegral / passedTestTime));
		ui.showMessage("Heading: " + (collectedHeadingIntegral / passedTestTime));
		
		saveCurrentDataToCSV();
		
	}
	
	private void saveCurrentDataToCSV() {
		StringBuilder sb = new StringBuilder();
		sb.append(currentPidValues[0].toString());
		sb.append(';');
		sb.append(currentPidValues[1].toString());
		sb.append(';');
		sb.append(currentPidValues[2].toString());
		sb.append(';');
		sb.append(currentPidValues[3].toString());
		sb.append(';');
		sb.append(passedTestTime);
		sb.append(';');
		sb.append((collectedBatteryIntegral / passedTestTime));
		sb.append(';');
		sb.append((collectedDistanceIntegral / passedTestTime));
		sb.append(';');
		sb.append((collectedHeadingIntegral / passedTestTime));
		sb.append('\n');
		
		try {
			FileWriter fw = new FileWriter("test.csv",true);
			fw.write(sb.toString());
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected Double getCostValue() {
		Double result;
		result = (collectedDistanceIntegral + collectedHeadingIntegral); 
		result /= passedTestTime;
		return result;
	}
}
