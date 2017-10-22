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
 * This class handles data marked as COMMAND_GET.
 * This command is sent by the PC to let the NXT return the internal value of a certain parameter.
 * Implements the interface {@link CommandHandler} and defines the callback-method handle().
 *
 * @author Gregor & Justus
 */
public final class GetHandler implements CommandHandler
{
	/**
	 * This method reads a parameter and switches it.
	 * Depending on the parameter, the number and type of the values to be send varies.
	 * In each case, the handler calls a method of the {@link CommunicatorNXT}
	 * to send the requested data.
	 * AUTO_STATUS_PACKET will always return true, as it is mandatory for our communication.
	 * The default case displays an error message on the NXT that the parameter is unknown.
	 * 
	 * @param is: The DataInputStream the handler uses to receive data
	 */
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
				NXT.COMMUNICATOR.sendGetReturn(POSITION, (float)SensorData.positionX, (float)SensorData.positionY);
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
			case PARAM_CONSTANT_ROTATION:
				NXT.COMMUNICATOR.sendGetReturn(PARAM_CONSTANT_ROTATION, (float)MotorController.CONST_ROTATION);
				break;
			case PARAM_CONSTANT_SPEED:
				NXT.COMMUNICATOR.sendGetReturn(PARAM_CONSTANT_SPEED, (float)MotorController.CONST_SPEED);
				break;
			case PARAM_WHEEL_DIAMETER:
				NXT.COMMUNICATOR.sendGetReturn(PARAM_WHEEL_DIAMETER, (float)NXT.WHEEL_DIAMETER);
				break;
			case PARAM_TRACK:
				NXT.COMMUNICATOR.sendGetReturn(PARAM_TRACK, (float)NXT.WHEEL_GAUGE);
				break;
			case PID_WEIGHT_ALL:
				NXT.COMMUNICATOR.sendGetReturn(PID_WEIGHT_ALL,
						MotorController.WEIGHT_GYRO_SPEED, MotorController.WEIGHT_GYRO_INTEGRAL,
						MotorController.WEIGHT_MOTOR_DISTANCE, MotorController.WEIGHT_MOTOR_SPEED);
				break;
			default:
				System.out.println("Undefined parameter.");
				System.out.println("Please reconnect.");
				// TODO send error code back to PC.
		}
	}
}
