package de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm;

import java.io.DataInputStream;
import java.io.IOException;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.NXT;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.nxt.motorcontrol.MotorController;

public final class SetHandler implements PacketHandler
{
	/** Documented variables **/
	public static final short PARAM_DISTANCE_TARGET = 0,
			PARAM_TACHO_DIFF_TARGET = 1;

	/** Custom variables **/
	public static final short PARAM_WEIGHT_GYRO_SPEED = 1000,
			PARAM_WEIGHT_GYRO_INTEGRAL = 1001,
			PARAM_WEIGHT_MOTOR_DISTANCE = 1002,
			PARAM_WEIGHT_MOTOR_SPEED = 1003,
			PARAM_PID_VALUES = 1004;

	@Override
	public void handle(DataInputStream is) throws IOException {
		final short param = is.readShort();
		switch (param) {
			case PARAM_WEIGHT_GYRO_SPEED:
				MotorController.WEIGHT_GYRO_SPEED = is.readDouble();
				break;
			case PARAM_WEIGHT_GYRO_INTEGRAL:
				MotorController.WEIGHT_GYRO_INTEGRAL = is.readDouble();
				break;
			case PARAM_WEIGHT_MOTOR_DISTANCE:
				MotorController.WEIGHT_MOTOR_DISTANCE = is.readDouble() * 360 / Math.PI / NXT.WHEEL_DIAMETER * 2;
				break;
			case PARAM_WEIGHT_MOTOR_SPEED:
				MotorController.WEIGHT_MOTOR_SPEED = is.readDouble() * 360 / Math.PI / NXT.WHEEL_DIAMETER * 2;
				break;
			case PARAM_PID_VALUES:
				MotorController.WEIGHT_GYRO_SPEED = is.readDouble();
				MotorController.WEIGHT_GYRO_INTEGRAL = is.readDouble();
				MotorController.WEIGHT_MOTOR_SPEED = is.readDouble() * 360 / Math.PI / NXT.WHEEL_DIAMETER * 2;
				MotorController.WEIGHT_MOTOR_SPEED = is.readDouble() * 360 / Math.PI / NXT.WHEEL_DIAMETER * 2;
				break;
			default:
				// TODO
		}
	}
}
