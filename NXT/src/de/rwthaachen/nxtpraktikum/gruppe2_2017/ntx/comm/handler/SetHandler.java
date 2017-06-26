package de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm.handler;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.*;
import java.io.DataInputStream;
import java.io.IOException;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.CommandHandler;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.NXT;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.sensors.SensorData;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.nxt.motorcontrol.MotorController;

/**
 * This class handles the COMMAND_SET calls.
 *
 * @author Gregor & Justus
 */
public final class SetHandler implements CommandHandler
{
	@Override
	public void handle(DataInputStream is) throws IOException {
		final byte param = is.readByte();
		// System.out.println("Received Set " + param);
		switch (param) {
			case HEADING:
				SensorData.heading = is.readFloat();
				break;
			case POSITION:
				final float posX = is.readFloat();
				final float posY = is.readFloat();
				System.out.println("Pos(" + posX + "," + posY + ")");
				// TODO set the positions to a class.
				break;
			case AUTO_STATUS_PACKET:
				final boolean isOn = is.readBoolean();
				System.out.println("Auto Status: " + isOn);
				NXT.COMMUNICATOR.setAutoStatusThread(isOn);
				break;
			case PID_GYRO_SPEED:
				MotorController.WEIGHT_GYRO_SPEED = is.readDouble();
				break;
			case PID_GYRO_INTEGRAL:
				MotorController.WEIGHT_GYRO_INTEGRAL = is.readDouble();
				break;
			case PID_MOTOR_DISTANCE:
				MotorController.WEIGHT_MOTOR_DISTANCE = is.readDouble() * 360 / Math.PI / NXT.WHEEL_DIAMETER * 2;
				break;
			case PID_MOTOR_SPEED:
				MotorController.WEIGHT_MOTOR_SPEED = is.readDouble() * 360 / Math.PI / NXT.WHEEL_DIAMETER * 2;
				break;
			case CONSTANT_ROTATION:
				// TODO: Save the value (double)
				break;
			case CONSTANT_SPEED:
				// TODO: Save the value (double)
				break;
			case WHEEL_DIAMETER:
				// TODO: Save the value (double)
				break;
			case TRACK:
				// TODO: Save the value (boolean will be true if the wheels are inside)
				break;
			case PID_WEIGHT_ALL:
				MotorController.WEIGHT_GYRO_SPEED = is.readDouble();
				MotorController.WEIGHT_GYRO_INTEGRAL = is.readDouble();
				MotorController.WEIGHT_MOTOR_SPEED = is.readDouble() * 360 / Math.PI / NXT.WHEEL_DIAMETER * 2;
				MotorController.WEIGHT_MOTOR_SPEED = is.readDouble() * 360 / Math.PI / NXT.WHEEL_DIAMETER * 2;
				break;
			default:
				System.out.println("Undefinde parameter.");
				System.out.println("Please reconnect.");
				// TODO send error code back to PC.
		}
	}
}
