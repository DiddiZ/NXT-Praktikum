package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.default_package;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.*;

public class SendGetThread extends application implements Runnable{
	public void run(){
		while(applicationHandler.getConnectionStatus()){
			Send.sendGetByte(PID_GYRO_INTEGRAL);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Send.sendGetByte(PID_GYRO_SPEED);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Send.sendGetByte(PID_MOTOR_DISTANCE);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Send.sendGetByte(PID_MOTOR_SPEED);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Send.sendGetByte(BATTERY_VOLTAGE);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Send.sendGetByte(GYRO_ANGLE);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Send.sendGetByte(TACHO_LEFT);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Send.sendGetByte(TACHO_RIGHT);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Send.sendGetByte(HEADING);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Send.sendGetByte(POSITION);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Send.sendGetByte(MOVEMENT_SPEED);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
