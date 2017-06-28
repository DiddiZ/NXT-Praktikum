package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.AUTO_STATUS_PACKET;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.PARAM_CONSTANT_ROTATION;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.PARAM_CONSTANT_SPEED;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.PID_GYRO_INTEGRAL;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.PID_GYRO_SPEED;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.PID_MOTOR_DISTANCE;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.PID_MOTOR_SPEED;

public class applicationHandler
{
	// Connect Area
	static UI gui;
	static Send send;
	private static boolean ConnectButtonStatus = true; // true=Connect, false=Disconnect
	public static boolean ClockStarter = true;

	public static boolean getConnectionStatus() {
		return ConnectButtonStatus;
	}

	public static void connectButton() {
		// if(gui.getConnectionType()!=null){
		if (ConnectButtonStatus) {
			connect();
		} else {
			disconnect();
		}

		// }
	}

	public static void connect() {
		send.com.connect();
		if (send.com.isConnected()) {
			ClockStarter = true;
			new SystemClock().start();
			gui.enableButtons();
			gui.setConnectionLabel(true);
			gui.setConnectionButtonText("Disconnect");
			ConnectButtonStatus = false;
			new SendGetThread(gui, send).start();
		} else {
			gui.output("Unable to connect");
		}
	}

	public static void disconnect() {
		send.sendDisconnect();
		gui.disableButtons();
		gui.setConnectionLabel(false);
		gui.setConnectionButtonText("Connect");
		ConnectButtonStatus = true;
		ClockStarter = false;
	}

	/*
	 * public static void connectButton(){
	 * if(gui.getConnectionType()!=null){
	 * if(ConnectionStatus){
	 * //Add Connect here!
	 * ClockStarter=true;
	 * gui.output("Connect via "+gui.getConnectionType());
	 * gui.enableButtons();
	 * (new Thread(new SystemClock())).start();
	 * gui.setConnectionLabel(true);
	 * gui.setConnectionButtonText("Disconnect");
	 * //(new Thread(new SendGetThread())).run();
	 * ConnectionStatus=false;
	 * }
	 * else
	 * {
	 * Send.sendDisconnect();
	 * gui.output("Disconnected");
	 * gui.disableButtons();
	 * gui.setConnectionLabel(false);
	 * gui.setConnectionButtonText("Connect");
	 * ConnectionStatus=true;
	 * ClockStarter=false;
	 * }
	 * }
	 * else{
	 * gui.output("Please select connection type!");
	 * }
	 * }
	 */
	// Parameter Area

	// Command Area
	public static void sendCommandButton() {
		final String input = gui.getInput();
		gui.output("input: " + input);

		new applicationCommandParser(send).parse(input);
	}

	// PositionTab
	public static void goForwardButton() {
		gui.output("Forward");
		// gui.output("Is not implemented yet");
		send.sendSetDouble(PARAM_CONSTANT_SPEED, 11);
	}

	public static void goBackButton() {
		gui.output("Back");
		// gui.output("Is not implemented yet");
		send.sendSetDouble(PARAM_CONSTANT_SPEED, -11);
	}

	public static void goLeftButton() {
		gui.output("Left");
		// gui.output("Is not implemented yet");
		send.sendSetDouble(PARAM_CONSTANT_ROTATION, 15);
	}

	public static void goRightButton() {
		gui.output("Right");
		// gui.output("Is not implemented yet");
		send.sendSetDouble(PARAM_CONSTANT_ROTATION, -15);
	}

	public static void stopForwardButton() {
		gui.output("Stop Forward");
		// gui.output("Is not implemented yet");
		send.sendSetDouble(PARAM_CONSTANT_SPEED, 0);
	}

	public static void stopBackButton() {
		gui.output("Stop Back");
		// gui.output("Is not implemented yet");
		send.sendSetDouble(PARAM_CONSTANT_SPEED, 0);
	}

	public static void stopLeftButton() {
		gui.output("Stop Left");
		// gui.output("Is not implemented yet");
		send.sendSetDouble(PARAM_CONSTANT_ROTATION, 0);
	}

	public static void stopRightButton() {
		gui.output("Stop Right");
		// gui.output("Is not implemented yet");
		send.sendSetDouble(PARAM_CONSTANT_ROTATION, 0);
	}

	public static void driveDistanceButton() {
		// gui.output("driveDistance: "+gui.drivedistancet.getText());
		// gui.output("Is not implemented yet");
		final String arg = gui.getDriveDistance();
		if (applicationCommandParser.floatConvertable(arg)) {
			final float paramValue = Float.parseFloat(arg);
			send.sendMove(paramValue);
		} else {
			gui.output("Parameter not convertable!");
		}
	}

	public static void turnAbsoluteButton() {
		gui.output("turnAbsolut: " + gui.getTurnAbsolute());

	}

	public static void turnRelativeButton() {
		final String arg = gui.getTurnRelative();
		if (applicationCommandParser.floatConvertable(arg)) {
			final float paramValue = Float.parseFloat(arg);
			send.sendTurn(paramValue);
		} else {
			gui.output("Parameter not convertable!");
		}
	}

	public static void driveToButton() {
		gui.output("drive to: " + gui.getDriveToX() + ", " + gui.getDriveToY());
		gui.output("Is not implemented yet");
	}

	public static void sendAutostatuspacket(boolean status) {
		send.sendSetBoolean(AUTO_STATUS_PACKET, status);
	}

	public static void sendBalancieren(boolean status) {
		send.sendBalancieren(status);
	}

	// ParameterTab
	// assuming paramID for parameter ranges from 21-
	public static void sendGyroSpeedButton() {
		final String arg = gui.getGyroSpeedt();
		if (applicationCommandParser.doubleConvertable(arg)) {
			final double paramValue = Double.parseDouble(arg);
			send.sendSetDouble(PID_GYRO_SPEED, paramValue);
			send.sendGetByte(PID_GYRO_SPEED);
		} else {
			gui.output("Parameter not convertable!");
		}
	}

	public static void sendGyroIntegralButton() {
		final String arg = gui.getGyroIntegralt();
		if (applicationCommandParser.doubleConvertable(arg)) {
			final double paramValue = Double.parseDouble(arg);
			send.sendSetDouble(PID_GYRO_INTEGRAL, paramValue);
			send.sendGetByte(PID_GYRO_INTEGRAL);
		} else {
			gui.output("Parameter not convertable!");
		}
	}

	public static void sendMotorDistanceButton() {
		final String arg = gui.getMotorDistancet();
		if (applicationCommandParser.doubleConvertable(arg)) {
			final double paramValue = Double.parseDouble(arg);
			send.sendSetDouble(PID_MOTOR_DISTANCE, paramValue);
			send.sendGetByte(PID_MOTOR_DISTANCE);
		} else {
			gui.output("Parameter not convertable!");
		}
	}

	public static void sendMotorSpeedButton() {
		final String arg = gui.getMotorSpeed();
		if (applicationCommandParser.doubleConvertable(arg)) {
			final double paramValue = Double.parseDouble(arg);
			send.sendSetDouble(PID_MOTOR_SPEED, paramValue);
			send.sendGetByte(PID_MOTOR_SPEED);
		} else {
			gui.output("Parameter not convertable!");
		}
	}

	public static void sendConstantSpeedButton() {
		final String arg = gui.getConstantSpeed();
		if (applicationCommandParser.floatConvertable(arg)) {
			final float paramValue = Float.parseFloat(arg);
			send.sendSetFloat((byte)128, paramValue);
		} else {
			gui.output("Parameter not convertable!");
		}
	}

	public static void sendConstantRotationButton() {
		final String arg = gui.getConstantSpeed();
		if (applicationCommandParser.floatConvertable(arg)) {
			final float paramValue = Float.parseFloat(arg);
			send.sendSetFloat((byte)129, paramValue);
		} else {
			gui.output("Parameter not convertable!");
		}
	}

	public static void sendWheeldiameterButton() {
		final String arg = gui.getWheelDiameter();
		if (applicationCommandParser.floatConvertable(arg)) {
			final float paramValue = Float.parseFloat(arg);
			send.sendSetFloat((byte)130, paramValue);
		} else {
			gui.output("Parameter not convertable!");
		}
	}

	public static void sendTrackButton() {
		final String arg = gui.getTrack();
		if (applicationCommandParser.floatConvertable(arg)) {
			final float paramValue = Float.parseFloat(arg);
			send.sendSetFloat((byte)131, paramValue);
		} else {
			gui.output("Parameter not convertable!");
		}
	}

	public static void sendAllButton() {
		gui.output("SendAllParameter");
		sendGyroSpeedButton();
		sendGyroIntegralButton();
		sendMotorDistanceButton();
		sendMotorSpeedButton();
		sendConstantSpeedButton();
		sendConstantRotationButton();
		sendWheeldiameterButton();
		sendTrackButton();
	}
	// MapTab

}
