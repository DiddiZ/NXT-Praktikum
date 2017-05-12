package de.rwthaachen.nxtpraktikum.gruppe2_2017;

import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;

/**
 * Contains design constants.
 */
public class Bot
{
	public static final MotorPort LEFT_MOTOR = MotorPort.A, RIGHT_MOTOR = MotorPort.B;
	public static final SensorPort GYRO_PORT = SensorPort.S2;

	public static final double WHEEL_DIAMETER = 5.6, WHEEL_GAUGE = 5.5;
}
