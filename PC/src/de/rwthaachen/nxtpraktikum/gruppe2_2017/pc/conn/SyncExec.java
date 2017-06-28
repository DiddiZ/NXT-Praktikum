package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui.applicationHandler;

public class SyncExec
{
	public static void syncoutput(String text) {
		applicationHandler.gui.output(text);
	}

	public static void syncsetTiltLabel(float paramValue) {
		applicationHandler.gui.setTiltLabel(paramValue);
	}

	public static void syncsetRotationLabel(float paramValue) {
		applicationHandler.gui.setRotationLabel(paramValue);
	}

	public static void syncsetBatteryLabel(int paramValue) {
		applicationHandler.gui.setBatteryLabel(paramValue);
	}

	public static void syncsetSpeedometerLabel(float paramValue1) {
		applicationHandler.gui.setSpeedometerLabel(paramValue1);
	}

	public static void syncsetPositionLabel(float paramValue1, float paramValue2) {
		applicationHandler.gui.setCurrentPositionLabel(paramValue1, paramValue2);
	}

	public static void syncsetAutoStatusPacket(boolean status) {
		applicationHandler.gui.setAutoStatusPacket(status);
	}

	public static void syncsetGyroSpeed(double paramValue) {
		applicationHandler.gui.setGyroSpeedt(paramValue);
	}

	public static void syncsetGyroIntegral(double paramValue) {
		applicationHandler.gui.setGyroIntegralt(paramValue);
	}

	public static void syncsetMotorSpeed(double paramValue) {
		applicationHandler.gui.setMotorSpeedt(paramValue);
	}

	public static void syncsetMotorDistance(double paramValue) {
		applicationHandler.gui.setMotorDistancet(paramValue);
	}

	@SuppressWarnings("unused")
	public static void syncsetTachoLeft(long paramValue) {}

	@SuppressWarnings("unused")
	public static void syncsetTachoRight(long paramValue) {}
}
