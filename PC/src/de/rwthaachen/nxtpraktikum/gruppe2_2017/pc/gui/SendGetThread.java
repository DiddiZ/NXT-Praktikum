package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.*;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.CommunicatorPC;

public class SendGetThread extends Thread
{
	private final UserInterface ui;
	private final CommunicatorPC comm;

	public SendGetThread(UserInterface ui, CommunicatorPC comm) {
		this.ui = ui;
		this.comm = comm;
	}

	@Override
	public void run() {
		int i = 0;
		while (comm.isConnected()) {
			switch (i) {
				case 0:
					ui.showMessage("Automatic update of values started.");
					comm.sendGet(PID_GYRO_INTEGRAL, true);
					comm.sendGet(PID_GYRO_SPEED, true);
					comm.sendGet(PID_MOTOR_DISTANCE, true);
					comm.sendGet(PID_MOTOR_SPEED, true);
					comm.sendGet(BATTERY_VOLTAGE, true);
					comm.sendGet(GYRO_ANGLE, true);
					comm.sendGet(TACHO_LEFT, true);
					comm.sendGet(TACHO_RIGHT, true);
					comm.sendGet(HEADING, true);
					comm.sendGet(POSITION, true);
					comm.sendGet(MOVEMENT_SPEED, true);
					comm.sendGet(PID_GYRO_INTEGRAL, true);
					comm.sendGet(PARAM_CONSTANT_ROTATION, true);
					comm.sendGet(PARAM_CONSTANT_SPEED, true);
					comm.sendGet(PARAM_WHEEL_DIAMETER, true);
					comm.sendGet(PARAM_TRACK, true);
					break;
				case 1:
					comm.sendGet(PID_GYRO_SPEED);
					break;
				case 2:
					comm.sendGet(PID_MOTOR_DISTANCE);
					break;
				case 3:
					comm.sendGet(PID_MOTOR_SPEED);
					break;
				case 4:
					comm.sendGet(BATTERY_VOLTAGE);
					break;
				case 5:
					comm.sendGet(GYRO_ANGLE);
					break;
				case 6:
					comm.sendGet(TACHO_LEFT);
					break;
				case 7:
					comm.sendGet(TACHO_RIGHT);
					break;
				case 8:
					comm.sendGet(PID_GYRO_INTEGRAL);
					break;
				case 9:
					comm.sendGet(PARAM_CONSTANT_ROTATION);
					break;
				case 10:
					comm.sendGet(PARAM_CONSTANT_SPEED);
					break;
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
