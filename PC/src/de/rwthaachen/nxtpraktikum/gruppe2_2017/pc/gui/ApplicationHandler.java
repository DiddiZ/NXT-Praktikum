package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.*;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.NXTData;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui.gamepad.Gamepad;

/**
 * @author Christian, Fabian, Robin
 */

public class ApplicationHandler
{
	private static final float DEFAULT_MOVE_SPEED = 10f;
	private static final float DEFAULT_TURN_SPEED = 45f;
	private static final long DEFAULT_NAVIGATION_SLEEP_TIME = 2000;
	private static final long DEFAULT_SLOWTURN_SLEEP_TIME = 500;
	private static final float MAXIMUM_SLOWTURN_STEPLENGTH = 45.5f; // <--- should not be 0

	// Connect Area
	private final UI gui;
	private final Send send;
	private final Navigator navi;

	public ApplicationHandler(UI gui, Send send, Navigator navi) {
		this.gui = gui;
		this.send = send;
		this.navi = navi;
	}

	public Navigator getNavigator() {
		return navi;
	}

	public boolean isConnected() {
		return send.com.isConnected();
	}

	public void connectButton() {
		if (!isConnected()) {
			connect();
		} else {
			disconnect();
		}
	}

	public void connect() {
		send.com.connect();
		if (isConnected()) {
			gui.showConnected(true);
			new SystemClock(gui, send.com).start();
			new SendGetThread(gui, send).start();
		} else {
			gui.showMessage("Unable to connect");
		}
	}

	public void disconnect() {
		if (isConnected()) {
			send.sendDisconnect();
		}
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

	public void moveForward() {
		if (!forward) {
			gui.showMessage(String.format("Move forward (Speed=%.1fcm/s)", DEFAULT_MOVE_SPEED));
			send.sendSetFloat(PARAM_CONSTANT_SPEED, DEFAULT_MOVE_SPEED);
			forward = true;
		}
	}

	public void moveBackward() {
		if (!backward) {
			gui.showMessage(String.format("Move backward (Speed=%.1fcm/s)", DEFAULT_MOVE_SPEED));
			send.sendSetFloat(PARAM_CONSTANT_SPEED, -DEFAULT_MOVE_SPEED);
			backward = true;
		}
	}

	public void turnLeft() {
		if (!left) {
			gui.showMessage(String.format("Turn left (Speed=%.1f°/s)", DEFAULT_TURN_SPEED));
			send.sendSetFloat(PARAM_CONSTANT_ROTATION, DEFAULT_TURN_SPEED);
			left = true;
		}
	}

	public void turnRight() {
		if (!right) {
			gui.showMessage(String.format("Turn right (Speed=%.1f°/s)", DEFAULT_TURN_SPEED));
			send.sendSetFloat(PARAM_CONSTANT_ROTATION, -DEFAULT_TURN_SPEED);
			right = true;
		}
	}

	public void stopMoving() {
		gui.showMessage("Stop moving");
		send.sendSetFloat(PARAM_CONSTANT_SPEED, 0);
		forward = false;
		backward = false;
	}

	public void stopTurning() {
		gui.showMessage("Stop turning");
		send.sendSetFloat(PARAM_CONSTANT_ROTATION, 0);
		left = false;
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
		final String arg = gui.getTurnAbsolute();
		if (ApplicationCommandParser.floatConvertable(arg)) {
			final float targetHeading = Float.parseFloat(arg);
			turnAbsoluteMethod(targetHeading);
		} else {
			gui.showMessage("Parameter not convertable!");
		}
	}

	public void turnAbsoluteMethod(float targetHeading) {
		final float currHeading = send.com.getData().getHeading();

		System.out.println("targetHeading: " + targetHeading);
		System.out.println("currHeading: " + currHeading);

		float diff = (targetHeading - currHeading) % 360;
		if (diff < -180) {
			diff += 360;
		}
		if (diff > 180) {
			diff -= 360;
		}

		send.sendTurn(diff < 180 ? diff : 180 - diff);
	}

	public void turnRelativeButton() {
		final String arg = gui.getTurnRelative();
		if (ApplicationCommandParser.floatConvertable(arg)) {
			final float angle = Float.parseFloat(arg);
			send.sendTurn(angle);
			// turnSlow(angle); //TODO Doesn't work for negative angles
		} else {
			gui.showMessage("Parameter not convertable!");
		}
	}

	public void turnSlow(float turnDegree) {

		final float numberOfSteps = turnDegree / MAXIMUM_SLOWTURN_STEPLENGTH;
		final int roundNumberOfSteps = (int)(numberOfSteps + 1f);

		send.sendTurn(turnDegree / roundNumberOfSteps);
		for (int i = 1; i < roundNumberOfSteps; i++) {
			try {
				Thread.sleep(DEFAULT_SLOWTURN_SLEEP_TIME);
			} catch (final Exception e) {

			}
			send.sendTurn(turnDegree / roundNumberOfSteps);
		}

	}

	public void driveToButton() {
		final String posXText = gui.getDriveToX();
		final String posYText = gui.getDriveToY();
		// System.out.println("X: "+send.com.getData().getPositionX()+"\nY:"+send.com.getData().getPositionY());
		driveToMethod(posXText, posYText);
	}

	public void driveToMethod(String posXText, String posYText) {
		final float posX = send.com.getData().getPositionX();
		final float posY = send.com.getData().getPositionY();
		float diffX, diffY, newHeading, drivingLength;

		if (ApplicationCommandParser.floatConvertable(posXText) && ApplicationCommandParser.floatConvertable(posYText)) {
			diffX = Float.parseFloat(posXText) - posX;
			diffY = Float.parseFloat(posYText) - posY;
			drivingLength = (float)Math.sqrt(diffY * diffY + diffX * diffX);

			if (diffY == 0f) {
				if (diffX < 0) {
					newHeading = 90f;
				} else {
					if (diffX > 0) {
						newHeading = -90f;
					} else {
						newHeading = 0f;
					}
				}
			} else {
				newHeading = (float)(Math.atan(diffX / diffY) / Math.PI * 180.0 * -1.0);
				if (diffY < 0f) {
					newHeading += 180f;
				}
			}
			// System.out.println("X: "+posX+"\n Y:"+posY);

			gui.showMessage("drive to: " + posXText + ", " + posYText);

			turnAbsoluteMethod(newHeading);
			try {
				// TODO: make wait time dependent on turning time
				Thread.sleep(DEFAULT_NAVIGATION_SLEEP_TIME);
			} catch (final Exception e) {

			}
			send.sendMove(drivingLength);

		} else {
			gui.showMessage("Something went wrong with parsing parameters");
		}
	}

	public void setPositionButton() {
		final String argX = gui.getSetPositionX();
		final String argY = gui.getSetPositionY();
		if (ApplicationCommandParser.floatConvertable(argX) && ApplicationCommandParser.floatConvertable(argY)) {
			final float paramValue1 = Float.parseFloat(argX);
			final float paramValue2 = Float.parseFloat(argY);
			send.sendSetFloatFloat(POSITION, paramValue1, paramValue2);
		} else {
			gui.showMessage("Parameter not convertable!");
		}
	}

	public void setHeadingButton() {
		final String arg = gui.getSetHeading();
		if (ApplicationCommandParser.floatConvertable(arg)) {
			final float paramValue = Float.parseFloat(arg);
			send.sendSetFloat(HEADING, paramValue);
		} else {
			gui.showMessage("Parameter not convertable!");
		}

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

	public void startEvoAlgButton() {
		// TODO implement EvoAlgStart
		// UI has getter/setter: getEvoAlgGI(), getEvoAlgGS(), getEvoAlgMD(), getEvoAlgMS(), setEvoAlgProcessing(String text)
	}

	private Gamepad gamepad;

	public void gamepadControl(boolean enabled) {
		if (enabled) {
			gamepad = Gamepad.findGamepad();
			if (gamepad == null) {
				gui.showMessage("No Gamepad found");
				gui.chkGamepad.setSelected(false);
			}

			final Thread t = new Thread(() -> {
				double lastMoveSpeed = 0, lastTurnSpeed = 0; // Cache last speeds in order to not spam the NXT with meaningless updates

				while (gamepad != null && gamepad.isActive() && send.com.isConnected()) {
					float moveSpeed = -gamepad.zAxis * DEFAULT_MOVE_SPEED;
					if (Math.abs(moveSpeed) < 0.1) {
						moveSpeed = 0;
					}
					if (lastMoveSpeed != moveSpeed) {
						send.sendSetFloat(PARAM_CONSTANT_SPEED, moveSpeed);
						lastMoveSpeed = moveSpeed;
					}

					float turnSpeed = -gamepad.xAxis * DEFAULT_TURN_SPEED * 2;
					if (Math.abs(turnSpeed) < 0.1) {
						turnSpeed = 0;
					}

					if (lastTurnSpeed != turnSpeed) {
						send.sendSetFloat(PARAM_CONSTANT_ROTATION, turnSpeed);
						lastTurnSpeed = turnSpeed;
					}

					try {
						Thread.sleep(100);
					} catch (final InterruptedException ex) {
						return;
					}
				}
				gui.chkGamepad.setSelected(false);
			});
			t.setDaemon(true);
			t.start();
			gui.showMessage("Started gamepad control");
		} else {
			if (gamepad != null) {
				gamepad.close();
				stopMoving();
				stopTurning();
			}
			gamepad = null;
		}
	}

	public NXTData getData() {
		return send.com.getData();
	}
}
