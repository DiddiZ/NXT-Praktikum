package de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm.handler;

import java.io.DataInputStream;
import java.io.IOException;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.CommandHandler;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.NXT;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.nxt.motorcontrol.MotorController;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.evo.*;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.*;

public class EvoHandler implements CommandHandler
{

	@Override
	public void handle(DataInputStream is) throws IOException {
		final byte param = is.readByte();
		switch (param) {
		case EVO_SET_NEW_VALUES:
			MotorController.WEIGHT_GYRO_SPEED = is.readDouble();
			MotorController.WEIGHT_GYRO_INTEGRAL = is.readDouble();
			MotorController.WEIGHT_MOTOR_SPEED = is.readDouble() * 360 / Math.PI / NXT.WHEEL_DIAMETER * 2;
			MotorController.WEIGHT_MOTOR_SPEED = is.readDouble() * 360 / Math.PI / NXT.WHEEL_DIAMETER * 2;
			break;
		case EVO_START_TEST:
			new EvoRunner().start();
			break;
		}
	}
	
	

}
