package de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm.CommunicatorNXT;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.sensors.SensorData;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.nxt.motorcontrol.MotorController;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;

/**
 * @author DiddiZ
 */
public class NXT
{
	// Bot design constants
	public static final MotorPort LEFT_MOTOR = MotorPort.A, RIGHT_MOTOR = MotorPort.B;
	public static final SensorPort GYRO_PORT = SensorPort.S2;
	public static final double WHEEL_DIAMETER = 5.6, WHEEL_GAUGE = 5.5;

	public static final CommunicatorNXT COMMUNICATOR = new CommunicatorNXT();

	public static void main(String[] args) throws InterruptedException {
		// TODO Start Communication-Thread here

		SensorData.init();

		Audio.playBeeps(3);

		MotorController.run();
	}
}