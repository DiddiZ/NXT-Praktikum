package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.*;

import java.io.FileWriter;
import java.io.IOException;

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
	
	public EvoAlgorithm(UI ui, Send send){
		this.setDaemon(true);
		
		this.ui = ui;
		this.send = send;
		
		threadRunTime = 5000;
		threadStartTime = System.currentTimeMillis();
		threadStopTime = System.currentTimeMillis() + threadRunTime;

	}
	
	@Override
	public void run() {
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
	
	private void saveCurrentDataToCSV() {
		StringBuilder sb = new StringBuilder();
		sb.append(passedTestTime);
		sb.append(',');
		sb.append((collectedBatteryIntegral / passedTestTime));
		sb.append(',');
		sb.append((collectedDistanceIntegral / passedTestTime));
		sb.append(',');
		sb.append((collectedHeadingIntegral / passedTestTime));
		
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
