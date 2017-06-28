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
	private static final float CONST_MOVE_SPEED = 10;
	private static final float CONST_TURN_SPEED = 15;

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

	// For spamming control
	private boolean forward, left, backward, right;

	public void goForwardButton() {
		if (!forward) {
			gui.showMessage("Move forward");
			send.sendSetDouble(PARAM_CONSTANT_SPEED, CONST_MOVE_SPEED);
			forward = true;
		}
	}

	public void goBackwardButton() {
		if (!backward) {
			gui.showMessage("Move backward");
			send.sendSetDouble(PARAM_CONSTANT_SPEED, -CONST_MOVE_SPEED);
			backward = true;
		}
	}

	public void goLeftButton() {
		if (!left) {
			gui.showMessage("Turn left");
			send.sendSetDouble(PARAM_CONSTANT_ROTATION, CONST_TURN_SPEED);
			left = true;
		}
	}

	public void goRightButton() {
		if (!right) {
			gui.showMessage("Turn right");
			send.sendSetDouble(PARAM_CONSTANT_ROTATION, -CONST_TURN_SPEED);
			right = true;
		}
	}

	public void stopForwardButton() {
		gui.showMessage("Stop moving forward");
		send.sendSetDouble(PARAM_CONSTANT_SPEED, 0);
		forward = false;
	}

	public void stopBackwardButton() {
		gui.showMessage("Stop moving backward");
		send.sendSetDouble(PARAM_CONSTANT_SPEED, 0);
		backward = false;
	}

	public void stopLeftButton() {
		gui.showMessage("Stop turning left");
		send.sendSetDouble(PARAM_CONSTANT_ROTATION, 0);
		left = false;
	}

	public void stopRightButton() {
		gui.showMessage("Stop turning right");
		send.sendSetDouble(PARAM_CONSTANT_ROTATION, 0);
		right = false;
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
