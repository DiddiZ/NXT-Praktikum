package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.*;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.NXT.WHEEL_DIAMETER;

import java.io.FileWriter;
import java.io.IOException;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.NXTData;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui.Send;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui.UI;



public class EvoAlgorithm extends Thread{

	private UI ui;
	private Send send;
	
	private final double threadRunTime;
	private final double threadStartTime;
	private final double threadStopTime;
	
	public static double collectedBatteryIntegral;
	public static double collectedDistanceIntegral;
	public static double collectedHeadingIntegral;
	public static double passedTestTime;
	
	private final double standardPidValues[] = new double[4];
	
	public EvoAlgorithm(UI ui, Send send){
		this.setDaemon(true);
		
		this.ui = ui;
		this.send = send;
		
		threadRunTime = 5000;
		threadStartTime = System.currentTimeMillis();
		threadStopTime = System.currentTimeMillis() + threadRunTime;
		
		standardPidValues[0] 	= -2.8; //WEIGHT_GYRO_SPEED
		standardPidValues[1]	= -13; // WEIGHT_GYRO_INTEGRAL
		standardPidValues[2]	= 0.15 * 360 / Math.PI / WHEEL_DIAMETER * 2; //WEIGHT_MOTOR_DISTANCE
		standardPidValues[3]	= 0.225 * 360 / Math.PI / WHEEL_DIAMETER * 2; //WEIGHT_MOTOR_SPEED

	}
	
	@Override
	public void run() {
		//linearSearch();
		
		send.sendSetBoolean(EVO_COLLECT_TEST_DATA, true);
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		send.sendSetBoolean(EVO_COLLECT_TEST_DATA, false);
		
		send.sendGetByteQuiet(EVO_BATTERY);
		send.sendGetByteQuiet(EVO_DISTANCE);
		send.sendGetByteQuiet(EVO_HEADING);
		send.sendGetByteQuiet(EVO_TIME);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		ui.showMessage("Passed test time: " + passedTestTime);
		ui.showMessage("Battery: " + (collectedBatteryIntegral / passedTestTime));
		ui.showMessage("Distance: " + (collectedDistanceIntegral / passedTestTime));
		ui.showMessage("Heading: " + (collectedHeadingIntegral / passedTestTime));
		
		saveCurrentDataToCSV();

	}
	
	
	private void linearSearch() {
		
		performTest(standardPidValues);
				
		
	}
	
	private void performTest(double pidValues[]) {
		ui.setEvoAlgGS(pidValues[0]);
		ui.setEvoAlgGI(pidValues[1]);
		ui.setEvoAlgMS(pidValues[2]);
		ui.setEvoAlgMS(pidValues[3]);
		
		send.sendSetDouble(PID_GYRO_SPEED, 		standardPidValues[0]);
		send.sendSetDouble(PID_GYRO_INTEGRAL, 	standardPidValues[1]);
		send.sendSetDouble(PID_MOTOR_DISTANCE, 	standardPidValues[2]);
		send.sendSetDouble(PID_MOTOR_SPEED, 	standardPidValues[3]);
			
		try {
			
			while (!NXTData.getBalancing()) {
				ui.showMessage("Start balancing thread to continue.");
				Thread.sleep(1000);
			}
			
			Thread.sleep(5000);
			
			send.sendSetDouble(PID_GYRO_SPEED, 		pidValues[0]);
			send.sendSetDouble(PID_GYRO_INTEGRAL, 	pidValues[1]);
			send.sendSetDouble(PID_MOTOR_DISTANCE, 	pidValues[2]);
			send.sendSetDouble(PID_MOTOR_SPEED, 	pidValues[3]);
			send.sendSetBoolean(EVO_COLLECT_TEST_DATA, true);
			
			
			Thread.sleep(1000);
			
			send.sendMove(30);
			Thread.sleep(5000);
			
			send.sendTurn(180);
			Thread.sleep(2000);
			
			send.sendMove(30);
			Thread.sleep(5000);
			
			send.sendTurn(-180);
			Thread.sleep(2000);
			
			send.sendMove(15);
			Thread.sleep(3000);
			
			send.sendMove(-30);
			Thread.sleep(5000);
			
			send.sendMove(15);
			Thread.sleep(3000);
			
			send.sendSetBoolean(EVO_COLLECT_TEST_DATA, false);
			
			send.sendGetByteQuiet(EVO_BATTERY);
			send.sendGetByteQuiet(EVO_DISTANCE);
			send.sendGetByteQuiet(EVO_HEADING);
			send.sendGetByteQuiet(EVO_TIME);
			
			Thread.sleep(1000);
			
				
		} catch (InterruptedException e) {
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
}
