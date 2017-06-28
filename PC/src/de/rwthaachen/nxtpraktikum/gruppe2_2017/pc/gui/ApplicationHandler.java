package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.AUTO_STATUS_PACKET;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.PARAM_CONSTANT_ROTATION;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.PARAM_CONSTANT_SPEED;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.PID_GYRO_INTEGRAL;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.PID_GYRO_SPEED;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.PID_MOTOR_DISTANCE;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.PID_MOTOR_SPEED;

public class ApplicationHandler
{
	// Connect Area
	private final UI gui;
	private final Send send;

	public ApplicationHandler(UI gui, Send send) {
		this.gui = gui;
		this.send = send;
	}

	public void connectButton() {
		if (!send.com.isConnected()) {
			connect();
		} else {
			disconnect();
		}
	}

	public void connect() {
		send.com.connect();
		if (send.com.isConnected()) {
			gui.showConnected(true);
			new SystemClock(gui, send.com).start();
			new SendGetThread(gui, send).start();
		} else {
			gui.showMessage("Unable to connect");
		}
	}

	public void disconnect() {
		send.sendDisconnect();
	}

	// Parameter Area

	// Command Area
	public void sendCommandButton() {
		final String input = gui.getInput();
		gui.showMessage("input: " + input);

		new ApplicationCommandParser(gui, send).parse(input);
	}

	// PositionTab
	public void goForwardButton() {
		gui.showMessage("Forward");
		// gui.output("Is not implemented yet");
		send.sendSetDouble(PARAM_CONSTANT_SPEED, 11);
	}

	public void goBackButton() {
		gui.showMessage("Back");
		// gui.output("Is not implemented yet");
		send.sendSetDouble(PARAM_CONSTANT_SPEED, -11);
	}

	public void goLeftButton() {
		gui.showMessage("Left");
		// gui.output("Is not implemented yet");
		send.sendSetDouble(PARAM_CONSTANT_ROTATION, 15);
	}

	public void goRightButton() {
		gui.showMessage("Right");
		// gui.output("Is not implemented yet");
		send.sendSetDouble(PARAM_CONSTANT_ROTATION, -15);
	}

	public void stopForwardButton() {
		gui.showMessage("Stop Forward");
		// gui.output("Is not implemented yet");
		send.sendSetDouble(PARAM_CONSTANT_SPEED, 0);
	}

	public void stopBackButton() {
		gui.showMessage("Stop Back");
		// gui.output("Is not implemented yet");
		send.sendSetDouble(PARAM_CONSTANT_SPEED, 0);
	}

	public void stopLeftButton() {
		gui.showMessage("Stop Left");
		// gui.output("Is not implemented yet");
		send.sendSetDouble(PARAM_CONSTANT_ROTATION, 0);
	}

	public void stopRightButton() {
		gui.showMessage("Stop Right");
		// gui.output("Is not implemented yet");
		send.sendSetDouble(PARAM_CONSTANT_ROTATION, 0);
	}

	public void driveDistanceButton() {
		// gui.output("driveDistance: "+gui.drivedistancet.getText());
		// gui.output("Is not implemented yet");
		final String arg = gui.getDriveDistance();
		if (ApplicationCommandParser.floatConvertable(arg)) {
			final float paramValue = Float.parseFloat(arg);
			send.sendMove(paramValue);
		} else {
			gui.showMessage("Parameter not convertable!");
		}
	}

	public void turnAbsoluteButton() {
		gui.showMessage("turnAbsolut: " + gui.getTurnAbsolute());

	}

	public void turnRelativeButton() {
		final String arg = gui.getTurnRelative();
		if (ApplicationCommandParser.floatConvertable(arg)) {
			final float paramValue = Float.parseFloat(arg);
			send.sendTurn(paramValue);
		} else {
			gui.showMessage("Parameter not convertable!");
		}
	}

	public void driveToButton() {
		gui.showMessage("drive to: " + gui.getDriveToX() + ", " + gui.getDriveToY());
		gui.showMessage("Is not implemented yet");
	}

	public void sendAutostatuspacket(boolean status) {
		send.sendSetBoolean(AUTO_STATUS_PACKET, status);
	}

	public void sendBalancieren(boolean status) {
		send.sendBalancieren(status);
	}

	// ParameterTab
	// assuming paramID for parameter ranges from 21-
	public void sendGyroSpeedButton() {
		final String arg = gui.getGyroSpeedt();
		if (ApplicationCommandParser.doubleConvertable(arg)) {
			final double paramValue = Double.parseDouble(arg);
			send.sendSetDouble(PID_GYRO_SPEED, paramValue);
			send.sendGetByte(PID_GYRO_SPEED);
		} else {
			gui.showMessage("Parameter not convertable!");
		}
	}

	public void sendGyroIntegralButton() {
		final String arg = gui.getGyroIntegralt();
		if (ApplicationCommandParser.doubleConvertable(arg)) {
			final double paramValue = Double.parseDouble(arg);
			send.sendSetDouble(PID_GYRO_INTEGRAL, paramValue);
			send.sendGetByte(PID_GYRO_INTEGRAL);
		} else {
			gui.showMessage("Parameter not convertable!");
		}
	}

	public void sendMotorDistanceButton() {
		final String arg = gui.getMotorDistancet();
		if (ApplicationCommandParser.doubleConvertable(arg)) {
			final double paramValue = Double.parseDouble(arg);
			send.sendSetDouble(PID_MOTOR_DISTANCE, paramValue);
			send.sendGetByte(PID_MOTOR_DISTANCE);
		} else {
			gui.showMessage("Parameter not convertable!");
		}
	}

	public void sendMotorSpeedButton() {
		final String arg = gui.getMotorSpeed();
		if (ApplicationCommandParser.doubleConvertable(arg)) {
			final double paramValue = Double.parseDouble(arg);
			send.sendSetDouble(PID_MOTOR_SPEED, paramValue);
			send.sendGetByte(PID_MOTOR_SPEED);
		} else {
			gui.showMessage("Parameter not convertable!");
		}
	}

	public void sendConstantSpeedButton() {
		final String arg = gui.getConstantSpeed();
		if (ApplicationCommandParser.floatConvertable(arg)) {
			final float paramValue = Float.parseFloat(arg);
			send.sendSetFloat((byte)128, paramValue);
		} else {
			gui.showMessage("Parameter not convertable!");
		}
	}

	public void sendConstantRotationButton() {
		final String arg = gui.getConstantSpeed();
		if (ApplicationCommandParser.floatConvertable(arg)) {
			final float paramValue = Float.parseFloat(arg);
			send.sendSetFloat((byte)129, paramValue);
		} else {
			gui.showMessage("Parameter not convertable!");
		}
	}

	public void sendWheeldiameterButton() {
		final String arg = gui.getWheelDiameter();
		if (ApplicationCommandParser.floatConvertable(arg)) {
			final float paramValue = Float.parseFloat(arg);
			send.sendSetFloat((byte)130, paramValue);
		} else {
			gui.showMessage("Parameter not convertable!");
		}
	}

	public void sendTrackButton() {
		final String arg = gui.getTrack();
		if (ApplicationCommandParser.floatConvertable(arg)) {
			final float paramValue = Float.parseFloat(arg);
			send.sendSetFloat((byte)131, paramValue);
		} else {
			gui.showMessage("Parameter not convertable!");
		}
	}

	public void sendAllButton() {
		gui.showMessage("SendAllParameter");
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
