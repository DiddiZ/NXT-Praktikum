package de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm.handler;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.*;
import java.io.DataInputStream;
import java.io.IOException;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.CommandHandler;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.NXT;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.sensors.SensorData;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.nxt.motorcontrol.MotorController;
import lejos.nxt.Battery;

/**
 * This class handles the COMMAND_GET calls.
 *
 * @author Gregor & Justus
 */
public final class GetHandler implements CommandHandler
{
	@Override
	public void handle(DataInputStream is) throws IOException {
		final byte param = is.readByte();
		switch (param) {
			case BATTERY_VOLTAGE:
				NXT.COMMUNICATOR.sendGetReturn(BATTERY_VOLTAGE, Battery.getVoltageMilliVolt());
				break;
			case GYRO_ANGLE:
				NXT.COMMUNICATOR.sendGetReturn(GYRO_ANGLE, (float)SensorData.gyroIntegral);
				break;
			case TACHO_LEFT:
				NXT.COMMUNICATOR.sendGetReturn(TACHO_LEFT, SensorData.tachoLeft);
				break;
			case TACHO_RIGHT:
				NXT.COMMUNICATOR.sendGetReturn(TACHO_RIGHT, SensorData.tachoRight);
				break;
			case HEADING:
				NXT.COMMUNICATOR.sendGetReturn(HEADING, (float)SensorData.heading);
				break;
			case POSITION:
				// TODO get the position from corresponding class.
				NXT.COMMUNICATOR.sendGetReturn(POSITION, 0, 0);
				break;
			case MOVEMENT_SPEED:
				NXT.COMMUNICATOR.sendGetReturn(MOVEMENT_SPEED, (float)SensorData.motorSpeed);
				break;
			case STATUS_PACKET:
				NXT.COMMUNICATOR.sendGetReturn(STATUS_PACKET,
						(float)SensorData.positionX, (float)SensorData.positionY, (float)SensorData.motorSpeed, (float)SensorData.heading);
				break;
			case AUTO_STATUS_PACKET:
				// TODO get the auto status package status from corresponding class.
				NXT.COMMUNICATOR.sendGetReturn(AUTO_STATUS_PACKET, true);
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
			case CONSTANT_ROTATION:
				// TODO: return value
				NXT.COMMUNICATOR.sendGetReturn(CONSTANT_ROTATION, (double)0);
				break;
			case CONSTANT_SPEED:
				// TODO: return value
				NXT.COMMUNICATOR.sendGetReturn(CONSTANT_SPEED, (double)0);
				break;
			case WHEEL_DIAMETER:
				// TODO: return value
				NXT.COMMUNICATOR.sendGetReturn(WHEEL_DIAMETER, 5.6);
				break;
			case TRACK:
				// TODO: return value
				NXT.COMMUNICATOR.sendGetReturn(TRACK, true);
				break;
			case PID_WEIGHT_ALL:
				NXT.COMMUNICATOR.sendGetReturn(PID_WEIGHT_ALL,
						MotorController.WEIGHT_GYRO_SPEED, MotorController.WEIGHT_GYRO_INTEGRAL,
						MotorController.WEIGHT_MOTOR_DISTANCE, MotorController.WEIGHT_MOTOR_SPEED);
				break;
			default:
				System.out.println("Undefinde parameter.");
				System.out.println("Please reconnect.");
				// TODO send error code back to PC.
		}
	}
}
