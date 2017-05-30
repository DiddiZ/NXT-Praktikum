package de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm.handler;
/**
 * @author Gregor & Justus
 * 
 * This class handles the COMMAND_GET calls.
 * 
 */
import java.io.DataInputStream;
import java.io.IOException;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.CommandHandler;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.*;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.NXT;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.sensors.SensorData;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.nxt.motorcontrol.MotorController;
import lejos.nxt.Battery;

public final class GetHandler implements CommandHandler
{

	@Override
	public void handle(DataInputStream is) throws IOException {
		final byte param = is.readByte();
		System.out.println("Received Get:" + param);
		switch (param) {
			case BATTERY_VOLTAGE:
				System.out.println("MAX_VOLTAGE");
				NXT.COMMUNICATOR.sendGetReturn(BATTERY_VOLTAGE, (int) Battery.getVoltage());
				break;
			case GYRO_ANGLE:
				NXT.COMMUNICATOR.sendGetReturn(GYRO_ANGLE, (float) SensorData.gyroIntegral);
				break;
			case TACHO_LEFT:
				NXT.COMMUNICATOR.sendGetReturn(TACHO_LEFT, (long) SensorData.tachoLeft);
				break;
			case TACHO_RIGHT:
				NXT.COMMUNICATOR.sendGetReturn(TACHO_RIGHT, (long) SensorData.tachoRight);
				break;
			case HEADING:
				NXT.COMMUNICATOR.sendGetReturn(HEADING, (float) SensorData.heading);
				break;
			case POSITION:
				// TODO get the position from corresponding class.
				NXT.COMMUNICATOR.sendGetReturn(POSITION, (float) 0, (float) 0);
				break;
			case MOVEMENT_SPEED:
				NXT.COMMUNICATOR.sendGetReturn(MOVEMENT_SPEED, (float) SensorData.motorSpeed);
				break;
			case STATUS_PACKAGE:
				// TODO get the position from corresponding class.
				NXT.COMMUNICATOR.sendGetReturn(STATUS_PACKAGE, 
						(float) 0, (float) 0, (float) SensorData.motorSpeed, (float) SensorData.heading);
				break;
			case AUTO_STATUS_PACKAGE:
				// TODO get the auto status package status from corresponding class.
				NXT.COMMUNICATOR.sendGetReturn(AUTO_STATUS_PACKAGE, false);
				break;
			case PID_GYRO_SPEED:
				NXT.COMMUNICATOR.sendGetReturn(PID_GYRO_SPEED, MotorController.WEIGHT_GYRO_SPEED);
				break;
			case PID_GYRO_INTEGRAL:
				NXT.COMMUNICATOR.sendGetReturn(PID_GYRO_INTEGRAL, MotorController.WEIGHT_GYRO_INTEGRAL);
				break;
			case PID_MOTOR_DISTANCE:
				NXT.COMMUNICATOR.sendGetReturn(PID_MOTOR_DISTANCE, MotorController.WEIGHT_MOTOR_DISTANCE);
				break;
			case PID_MOTOR_SPEED:
				NXT.COMMUNICATOR.sendGetReturn(PID_MOTOR_SPEED, MotorController.WEIGHT_MOTOR_SPEED);
				break;
			case PID_WEIGHT_ALL:
				NXT.COMMUNICATOR.sendGetReturn(PID_WEIGHT_ALL, 
						MotorController.WEIGHT_GYRO_SPEED,		MotorController.WEIGHT_GYRO_INTEGRAL,
						MotorController.WEIGHT_MOTOR_DISTANCE, 	MotorController.WEIGHT_MOTOR_SPEED);
				break;
			default:
				System.out.println("Undefinde parameter.");
				System.out.println("Please reconnect.");
				//TODO send error code back to PC.
		}
	}
}
