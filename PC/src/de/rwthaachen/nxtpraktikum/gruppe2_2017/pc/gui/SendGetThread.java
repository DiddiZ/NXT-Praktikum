package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.*;

import org.eclipse.swt.widgets.Display;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.SyncExec;

public class SendGetThread extends application implements Runnable{	
	public void run(){
		int i = 0;
		while(Send.com.isConnected()){
			switch (i) {
			case 0:
				SyncExec.syncoutput("Automatic update of values started.");
				Send.sendGetByteQuiet(PID_GYRO_INTEGRAL);
				Send.sendGetByteQuiet(PID_GYRO_SPEED);
				Send.sendGetByteQuiet(PID_MOTOR_DISTANCE);
				Send.sendGetByteQuiet(PID_MOTOR_SPEED);
				Send.sendGetByteQuiet(BATTERY_VOLTAGE);
				Send.sendGetByteQuiet(GYRO_ANGLE);
				Send.sendGetByteQuiet(TACHO_LEFT);
				Send.sendGetByteQuiet(TACHO_RIGHT);
				Send.sendGetByteQuiet(HEADING);
				Send.sendGetByteQuiet(POSITION);
				Send.sendGetByteQuiet(MOVEMENT_SPEED);
				Send.sendGetByteQuiet(PID_GYRO_INTEGRAL);
				break;
			case 1:
				Send.sendGetByteQuiet(PID_GYRO_SPEED);
				break;
			case 2:
				Send.sendGetByteQuiet(PID_MOTOR_DISTANCE);
				break;
			case 3:
				Send.sendGetByteQuiet(PID_MOTOR_SPEED);
				break;
			case 4:
				Send.sendGetByteQuiet(BATTERY_VOLTAGE);
				break;
			case 5:
				Send.sendGetByteQuiet(GYRO_ANGLE);
				break;
			case 6:
				Send.sendGetByteQuiet(TACHO_LEFT);
				break;
			case 7:
				Send.sendGetByteQuiet(TACHO_LEFT);
				break;
			case 8:
				Send.sendGetByteQuiet(TACHO_RIGHT);
				break;
			case 9:
				Send.sendGetByteQuiet(HEADING);
				break;
			case 10:
				Send.sendGetByteQuiet(POSITION);
				break;
			case 11:
				Send.sendGetByteQuiet(MOVEMENT_SPEED);
				break;
			case 12:
				Send.sendGetByteQuiet(PID_GYRO_INTEGRAL);
				break;
			default:
				i = 1;
			}			
			i++;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				System.out.println("SendGetThread was interrupted while sleeping.");
			}
		}
	}
}
