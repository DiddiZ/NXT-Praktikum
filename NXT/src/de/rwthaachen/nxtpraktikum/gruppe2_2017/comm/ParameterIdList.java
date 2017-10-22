package de.rwthaachen.nxtpraktikum.gruppe2_2017.comm;

/**
 * This class inherits type definitions for parameters.
 *
 * @author Gregor & Justus
 */
public final class ParameterIdList
{
	/*
	 * This parameter list belongs to the communication protocol number 0.
	 */
	public final static byte BATTERY_VOLTAGE = 1,
			GYRO_ANGLE = 2,
			TACHO_LEFT = 3,
			TACHO_RIGHT = 4,
			HEADING = 5,
			POSITION = 6,
			MOVEMENT_SPEED = 7,
			STATUS_PACKET = 8,
			AUTO_STATUS_PACKET = 9,

			/*
			 * This parameter list belongs to the communication protocol number 2.
			 */
			PID_WEIGHT_1 = 21,
			PID_WEIGHT_2 = 22,
			PID_WEIGHT_3 = 23,
			PID_WEIGHT_4 = 24,
			PID_WEIGHT_5 = 25,
			PID_WEIGHT_6 = 26,
			PID_WEIGHT_7 = 27,
			PID_WEIGHT_8 = 28,
			PID_WEIGHT_9 = 29,
			PID_WEIGHT_10 = 30,

			PID_GYRO_SPEED = 21,
			PID_GYRO_INTEGRAL = 22,
			PID_MOTOR_DISTANCE = 23,
			PID_MOTOR_SPEED = 24,

			PID_WEIGHT_ALL = (byte)128,

			PARAM_CONSTANT_ROTATION = (byte)131,
			PARAM_CONSTANT_SPEED = (byte)132,
			PARAM_WHEEL_DIAMETER = (byte)133,
			PARAM_TRACK = (byte)134,
			PARAM_ULTRASENSOR = (byte)135,

			EVO_COLLECT_TEST_DATA = (byte)140,
			EVO_MEASUREMENTS = (byte)141;
}
