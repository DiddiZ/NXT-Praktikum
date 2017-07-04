package de.rwthaachen.nxtpraktikum.gruppe2_2017.evo;

import java.io.IOException;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.NXT;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.sensors.SensorData;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.nxt.motorcontrol.MotorController;

public class EvoRunner extends Thread {

	static int numberOfActiveRunners = 0;
	protected boolean isRunning;
	
	public EvoRunner() {
		this.setDaemon(true);
		isRunning = true;
		numberOfActiveRunners++;
		this.run();
	}
	
	@Override
	public void run() {
		final long execTime = 30000; // execution time in miliseconds
		final long prepTime = 5000;
		long startTime = System.currentTimeMillis();
		long runTime = 0; // runtime of this thread
		int state = 0;
		while (runTime < execTime && isRunning) {
			runTime = System.currentTimeMillis() - startTime;
			
			// only one Runner is allowed to be active at one time.
			if (numberOfActiveRunners > 1) {
				isRunning = false;
			}
			
			// if NXT fallen, then break and return collected values
			if (SensorData.motorPowerIntegral == Double.MAX_VALUE) {
				break;
			}
			
			if (runTime > 0 && state == 0) { //no movement at begin
				MotorController.stopMoving();
				state = incrementAndSendState(state);
			}
			if (runTime > prepTime && state == 1) { //reset integrals after 5 seconds
				SensorData.batteryVoltageIntegral = 0;
				SensorData.motorPowerIntegral = 0;
				state = incrementAndSendState(state);
			}
			if (runTime > 5000 && state == 2) { // move 30cm forward after 0 seconds
				MotorController.move(30);
				state = incrementAndSendState(state);
			}
			if (runTime > 10000 && state == 3) { //turn for 180° after 5 seconds
				MotorController.turn(180);
				state = incrementAndSendState(state);
			}
			if (runTime > 11000 && state == 4) { // move 30cm forward after 1 second
				MotorController.move(30);
				state = incrementAndSendState(state);
			}
			if (runTime > 16000 && state == 5) { // turn 180° after 5 seconds
				MotorController.turn(180);
				state = incrementAndSendState(state);
			}
			if (runTime > 17000 && state == 6) { // move 15cm forward after 1 second
				MotorController.move(15);
				state = incrementAndSendState(state);
			}
			if (runTime > 20000 && state == 7) { // move 30cm backward after 3 seconds
				MotorController.move(-30);
				state = incrementAndSendState(state);
			}
			if (runTime > 25000 && state == 8) { // move 15cm forward after 5 seconds
				MotorController.move(15);
				state = incrementAndSendState(state);
			}

		}
		MotorController.stopMoving();
		numberOfActiveRunners--;
		double averageBatteryVoltage = SensorData.batteryVoltageIntegral / (runTime - prepTime);
		double averageMotorIntegral;
		if (SensorData.motorPowerIntegral != Double.MAX_VALUE)
			averageMotorIntegral = SensorData.motorPowerIntegral / (runTime - prepTime);
		else
			averageMotorIntegral = Double.MAX_VALUE;
		try {
			NXT.COMMUNICATOR.sendReturnEvoTest(averageMotorIntegral, averageBatteryVoltage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * This method increments the given number and sends it to the PC as a state of the runner. Hack to save code.
	 * @param paramState
	 * @return paramState + 1;
	 */
	private int incrementAndSendState(int paramState) {
		paramState++;
		
		try {
			NXT.COMMUNICATOR.sendReturnEvoTestState(paramState);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return paramState;
	}
}
