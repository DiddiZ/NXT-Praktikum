/**
 * This Runner runs one evolution test by performind movement operations and measuring values.
 * 
 * @author Gregor
 */

package de.rwthaachen.nxtpraktikum.gruppe2_2017.evo;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.NXT.WHEEL_DIAMETER;

import java.io.IOException;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.NXT;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.sensors.SensorData;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.nxt.motorcontrol.MotorController;
import lejos.nxt.Button;

public class EvoRunner extends Thread {

	static int numberOfActiveRunners = 0;
	protected boolean isRunning;
	
	final double currentPidVal1, currentPidVal2,currentPidVal3,currentPidVal4;
	
	public EvoRunner(double pidValue1, double pidValue2, double pidValue3, double pidValue4) {
		this.setDaemon(true);
		isRunning = true;
		numberOfActiveRunners++;
		this.run();
		
		currentPidVal1 = pidValue1;
		currentPidVal2 = pidValue2;
		currentPidVal3 = pidValue3;
		currentPidVal4 = pidValue4;
	}
	
	@Override
	public void run() {
		while (!MotorController.isRunning || Button.ESCAPE.isDown()) {
			// busy waiting, until NXT is balancing.
		}
		
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
				setStandardPidValues();
				state = incrementAndSendState(state);
			}
			if (runTime > prepTime && state == 1) { //set test values after 5 seconds
				initTest();
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
			if (runTime > 16000 && state == 5) { // turn -180° after 5 seconds
				MotorController.turn(-180);
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
		final double passedTestTime = (runTime - prepTime);
		final double averageBatteryVoltage = SensorData.batteryVoltageIntegral / passedTestTime;
		final double averageMotorPower;
		if (SensorData.motorPowerIntegral != Double.MAX_VALUE)
			averageMotorPower = SensorData.motorPowerIntegral / passedTestTime;
		else
			averageMotorPower = Double.MAX_VALUE;
		final double avarageDistanceDifference = SensorData.distanceDifferenceIntegral / passedTestTime;
		final double avarageHeadingDifference = SensorData.headingDifferenceIntegral / passedTestTime;
		try {
			NXT.COMMUNICATOR.sendReturnEvoTest(averageMotorPower, averageBatteryVoltage,avarageDistanceDifference, avarageHeadingDifference);
		} catch (IOException e) {
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
	
	private void setStandardPidValues() {
		MotorController.WEIGHT_GYRO_SPEED = -2.8;
		MotorController.WEIGHT_GYRO_INTEGRAL = -13;
		MotorController.WEIGHT_MOTOR_DISTANCE = 0.15 * 360 / Math.PI / WHEEL_DIAMETER * 2;
		MotorController.WEIGHT_MOTOR_SPEED = 0.225 * 360 / Math.PI / WHEEL_DIAMETER * 2;
	}
	
	private void initTest() {
		MotorController.WEIGHT_GYRO_SPEED = currentPidVal1;
		MotorController.WEIGHT_GYRO_INTEGRAL = currentPidVal2;
		MotorController.WEIGHT_MOTOR_SPEED = currentPidVal3 * 360 / Math.PI / NXT.WHEEL_DIAMETER * 2;
		MotorController.WEIGHT_MOTOR_SPEED = currentPidVal4 * 360 / Math.PI / NXT.WHEEL_DIAMETER * 2;
		
		SensorData.batteryVoltageIntegral = 0;
		SensorData.motorPowerIntegral = 0;
		SensorData.distanceDifferenceIntegral = 0;
		SensorData.headingDifferenceIntegral = 0;
	}
	
}
