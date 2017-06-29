package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.*;

public class SendGetThread extends Thread
{
	private final UserInterface ui;
	private final Send send;

	public SendGetThread(UserInterface ui, Send send) {
		this.ui = ui;
		this.send = send;
	}

	@Override
	public void run() {
		int i = 0;
		while (send.com.isConnected()) {
			switch (i) {
				case 0:
					ui.showMessage("Automatic update of values started.");
					send.sendGetByteQuiet(PID_GYRO_INTEGRAL);
					send.sendGetByteQuiet(PID_GYRO_SPEED);
					send.sendGetByteQuiet(PID_MOTOR_DISTANCE);
					send.sendGetByteQuiet(PID_MOTOR_SPEED);
					send.sendGetByteQuiet(BATTERY_VOLTAGE);
					send.sendGetByteQuiet(GYRO_ANGLE);
					send.sendGetByteQuiet(TACHO_LEFT);
					send.sendGetByteQuiet(TACHO_RIGHT);
					send.sendGetByteQuiet(HEADING);
					send.sendGetByteQuiet(POSITION);
					send.sendGetByteQuiet(MOVEMENT_SPEED);
					send.sendGetByteQuiet(PID_GYRO_INTEGRAL);
					send.sendGetByteQuiet(PARAM_CONSTANT_ROTATION);
					send.sendGetByteQuiet(PARAM_CONSTANT_SPEED);
					send.sendGetByteQuiet(PARAM_WHEEL_DIAMETER);
					send.sendGetByteQuiet(PARAM_TRACK);
					break;
				case 1:
					send.sendGetByteQuiet(PID_GYRO_SPEED);
					break;
				case 2:
					send.sendGetByteQuiet(PID_MOTOR_DISTANCE);
					break;
				case 3:
					send.sendGetByteQuiet(PID_MOTOR_SPEED);
					break;
				case 4:
					send.sendGetByteQuiet(BATTERY_VOLTAGE);
					break;
				case 5:
					send.sendGetByteQuiet(GYRO_ANGLE);
					break;
				case 6:
					send.sendGetByteQuiet(TACHO_LEFT);
					break;
				case 7:
					send.sendGetByteQuiet(TACHO_RIGHT);
					break;
				case 8:
					send.sendGetByteQuiet(PID_GYRO_INTEGRAL);
					break;
				case 9:
					send.sendGetByteQuiet(PARAM_CONSTANT_ROTATION);
					break;
				case 10:
					send.sendGetByteQuiet(PARAM_CONSTANT_SPEED);
					break;
				// case 14:
				// send.sendGetByteQuiet(PARAM_WHEEL_DIAMETER);
				// break;
				// case 15:
				// send.sendGetByteQuiet(PARAM_TRACK);
				// break;
				default:
					i = 0;
			}
			i++;
			try {
				Thread.sleep(100);
			} catch (final InterruptedException e) {
				System.out.println("SendGetThread was interrupted while sleeping.");
			}
		}
	}
}
