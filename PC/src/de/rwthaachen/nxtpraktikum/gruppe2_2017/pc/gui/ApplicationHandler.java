package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.*;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.CommunicatorPC;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.NXTData;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo.EvoAlgorithm;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui.gamepad.Gamepad;

/**
 * @author Christian, Fabian, Robin
 */

public class ApplicationHandler
{
	private static final float DEFAULT_MOVE_SPEED = 30f;
	private static final float DEFAULT_TURN_SPEED = 45f;
	private static final long DEFAULT_NAVIGATION_SLEEP_TIME = 2000;
	private static final long DEFAULT_SLOWTURN_SLEEP_TIME = 500;
	private static final float MAXIMUM_SLOWTURN_STEPLENGTH = 45.5f; // <--- should not be 0

	// Connect Area
	private final UI gui;
	private final CommunicatorPC comm;
	private final Navigator navi;
	private final NXTData data;
	private NavigationThread naviThread;
	private final CollisionWarningThread collision;

	public ApplicationHandler(UI gui, CommunicatorPC comm, Navigator navi, NXTData data) {
		this.gui = gui;
		this.comm = comm;
		this.navi = navi;
		this.data = data;
		collision = new CollisionWarningThread(gui, data, navi);
	}

	public Navigator getNavigator() {
		return navi;
	}

	public boolean isConnected() {
		return comm.isConnected();
	}

	public void connectButton() {
		if (!isConnected()) {
			connect();
		} else {
			disconnect();
		}
	}

	public void connect() {
		comm.connect();
		if (isConnected()) {
			gui.showConnected(true);
			new SystemClock(gui, comm).start();
			new SendGetThread(gui, comm).start();
		} else {
			gui.showMessage("Unable to connect");
		}
	}

	public void disconnect() {
		if (isConnected()) {
			if (naviThread != null) {
				naviThread.setRunning(false);
			}
			sendBalancieren(false);
			data.setBalancing(false);
			gui.showBalancingEnabled(false);
			comm.disconnectInit();
			gui.setBatteryLabel(false);
		}
	}

	// Parameter Area

	// Command Area
	public void sendCommandButton() {
		final String input = gui.getInput();
		gui.showMessage("input: " + input);

		new ApplicationCommandParser(gui, comm).parse(input);
	}

	// PositionTab

	// For spamming control
	private boolean forward, left, backward, right;

	public void moveForward() {
		if (!forward) {
			gui.showMessage(String.format("Move forward (Speed=%.1fcm/s)", DEFAULT_MOVE_SPEED));
			comm.sendSet(PARAM_CONSTANT_SPEED, DEFAULT_MOVE_SPEED);
			forward = true;
		}
	}

	public void moveBackward() {
		if (!backward) {
			gui.showMessage(String.format("Move backward (Speed=%.1fcm/s)", DEFAULT_MOVE_SPEED));
			comm.sendSet(PARAM_CONSTANT_SPEED, -DEFAULT_MOVE_SPEED);
			backward = true;
		}
	}

	public void turnLeft() {
		if (!left) {
			gui.showMessage(String.format("Turn left (Speed=%.1f°/s)", DEFAULT_TURN_SPEED));
			comm.sendSet(PARAM_CONSTANT_ROTATION, DEFAULT_TURN_SPEED);
			left = true;
		}
	}

	public void turnRight() {
		if (!right) {
			gui.showMessage(String.format("Turn right (Speed=%.1f°/s)", DEFAULT_TURN_SPEED));
			comm.sendSet(PARAM_CONSTANT_ROTATION, -DEFAULT_TURN_SPEED);
			right = true;
		}
	}

	public void startMoving() {
		if (!forward) {
			comm.sendSet(PARAM_CONSTANT_SPEED, DEFAULT_MOVE_SPEED);
			forward = true;
		}
	}

	public void stopMoving() {
		gui.showMessage("Stop moving");
		comm.sendSet(PARAM_CONSTANT_SPEED, 0f);
		forward = false;
		backward = false;
	}

	public void stopTurning() {
		gui.showMessage("Stop turning");
		comm.sendSet(PARAM_CONSTANT_ROTATION, 0f);
		left = false;
		right = false;
	}

	public void driveDistanceButton() {
		// gui.output("driveDistance: "+gui.drivedistancet.getText());
		// gui.output("Is not implemented yet");
		final String arg = gui.getDriveDistance();
		if (ApplicationCommandParser.floatConvertable(arg)) {
			final float paramValue = Float.parseFloat(arg);
			comm.sendMove(paramValue);
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
		final float currHeading = data.getHeading();

		System.out.println("targetHeading: " + targetHeading);
		System.out.println("currHeading: " + currHeading);

		float diff = (targetHeading - currHeading) % 360;
		if (diff < -180) {
			diff += 360;
		}
		if (diff > 180) {
			diff -= 360;
		}

		comm.sendTurn(diff < 180 ? diff : 180 - diff);
	}

	public void turnRelativeButton() {
		final String arg = gui.getTurnRelative();
		if (ApplicationCommandParser.floatConvertable(arg)) {
			final float angle = Float.parseFloat(arg);
			comm.sendTurn(angle);
			// turnSlow(angle); //TODO Doesn't work for negative angles
		} else {
			gui.showMessage("Parameter not convertable!");
		}
	}

	public void turnSlow(float turnDegree) { // TODO FIX for negative angles

		final float numberOfSteps = turnDegree / MAXIMUM_SLOWTURN_STEPLENGTH;
		final int roundNumberOfSteps = (int)(numberOfSteps + 1f);

		comm.sendTurn(turnDegree / roundNumberOfSteps);
		for (int i = 1; i < roundNumberOfSteps; i++) {
			try {
				Thread.sleep(DEFAULT_SLOWTURN_SLEEP_TIME);
			} catch (final Exception e) {

			}
			comm.sendTurn(turnDegree / roundNumberOfSteps);
		}
	}

	public void driveToButton() {
		final String posXText = gui.getDriveToX();
		final String posYText = gui.getDriveToY();
		// System.out.println("X: "+send.com.getData().getPositionX()+"\nY:"+send.com.getData().getPositionY());
		driveToMethod(posXText, posYText);
	}

	public void driveToMethod(String posXText, String posYText) {
		// final float posX = data.getPositionX();
		// final float posY = data.getPositionY();
		float diffX, diffY; // newHeading, drivingLength;

		if (ApplicationCommandParser.floatConvertable(posXText) && ApplicationCommandParser.floatConvertable(posYText)) {
			diffX = Float.parseFloat(posXText); // - posX
			diffY = Float.parseFloat(posYText); // - posY
			if (naviThread != null) {
				naviThread.setRunning(false);
			}
			naviThread = new NavigationThread(navi, this, diffX, diffY);
			naviThread.start();
			/*
			 * drivingLength = (float)Math.sqrt(diffY * diffY + diffX * diffX);
			 * if (diffY == 0f) {
			 * if (diffX < 0) {
			 * newHeading = 90f;
			 * } else {
			 * if (diffX > 0) {
			 * newHeading = -90f;
			 * } else {
			 * newHeading = 0f;
			 * }
			 * }
			 * } else {
			 * newHeading = (float)(Math.atan(diffX / diffY) / Math.PI * 180.0 * -1.0);
			 * if (diffY < 0f) {
			 * newHeading += 180f;
			 * }
			 * }
			 * // System.out.println("X: "+posX+"\n Y:"+posY);
			 * gui.showMessage("drive to: " + posXText + ", " + posYText);
			 * turnAbsoluteMethod(newHeading);
			 * try {
			 * // TODO: make wait time dependent on turning time
			 * Thread.sleep(DEFAULT_NAVIGATION_SLEEP_TIME);
			 * } catch (final Exception e) {
			 * }
			 * comm.sendMove(drivingLength);
			 */

		} else {
			gui.showMessage("Something went wrong with parsing parameters");
		}
	}

	public void driveToMethod(double posX, double posY) {
		final float posXcurrent = data.getPositionX();
		final float posYcurrent = data.getPositionY();
		float diffX, diffY, newHeading, drivingLength;

		diffX = (float)posX - posXcurrent;
		diffY = (float)posY - posYcurrent;
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

		gui.showMessage("drive to: " + posX + ", " + posY);

		turnAbsoluteMethod(newHeading);
		try {
			// TODO: make wait time dependent on turning time
			Thread.sleep(DEFAULT_NAVIGATION_SLEEP_TIME);
		} catch (final Exception e) {

		}
		comm.sendMove(drivingLength);

	}

	public void turnToCoordinate(float xTarget, float yTarget) {
		final float posXcurrent = data.getPositionX();
		final float posYcurrent = data.getPositionY();
		float diffX, diffY, newHeading;

		diffX = xTarget - posXcurrent;
		diffY = yTarget - posYcurrent;

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

		gui.showMessage("turn to: " + xTarget + ", " + yTarget);

		turnAbsoluteMethod(newHeading);
		try {
			// TODO: make wait time dependent on turning time
			Thread.sleep(DEFAULT_NAVIGATION_SLEEP_TIME);
		} catch (final Exception e) {

		}
	}

	public void setPositionButton() {
		final String argX = gui.getSetPositionX();
		final String argY = gui.getSetPositionY();
		if (ApplicationCommandParser.floatConvertable(argX) && ApplicationCommandParser.floatConvertable(argY)) {
			final float paramValue1 = Float.parseFloat(argX);
			final float paramValue2 = Float.parseFloat(argY);
			comm.sendSet(POSITION, paramValue1, paramValue2);
			resetMap();
		} else {
			gui.showMessage("Parameter not convertable!");
		}
	}

	public void resetMap() {
		navi.resetMapData();
	}

	public void setHeadingButton() {
		final String arg = gui.getSetHeading();
		if (ApplicationCommandParser.floatConvertable(arg)) {
			final float paramValue = Float.parseFloat(arg);
			comm.sendSet(HEADING, paramValue);
			resetMap();
		} else {
			gui.showMessage("Parameter not convertable!");
		}
	}

	public void sendAutostatuspacket(boolean status) {
		comm.sendSet(AUTO_STATUS_PACKET, status);
	}

	public void sendBalancieren(boolean status) {
		comm.sendBalancing(status);
		data.setBalancing(status);
		new Thread(collision).start();
	}

	// ParameterTab
	public void sendGyroSpeedButton() {
		final String arg = gui.getGyroSpeedt();
		if (ApplicationCommandParser.doubleConvertable(arg)) {
			final double paramValue = Double.parseDouble(arg);
			comm.sendSet(PID_GYRO_SPEED, paramValue);
			comm.sendGet(PID_GYRO_SPEED);
		} else {
			gui.showMessage("Parameter not convertable!");
		}
	}

	public void sendGyroIntegralButton() {
		final String arg = gui.getGyroIntegralt();
		if (ApplicationCommandParser.doubleConvertable(arg)) {
			final double paramValue = Double.parseDouble(arg);
			comm.sendSet(PID_GYRO_INTEGRAL, paramValue);
			comm.sendGet(PID_GYRO_INTEGRAL);
		} else {
			gui.showMessage("Parameter not convertable!");
		}
	}

	public void sendMotorDistanceButton() {
		final String arg = gui.getMotorDistancet();
		if (ApplicationCommandParser.doubleConvertable(arg)) {
			final double paramValue = Double.parseDouble(arg);
			comm.sendSet(PID_MOTOR_DISTANCE, paramValue);
			comm.sendGet(PID_MOTOR_DISTANCE);
		} else {
			gui.showMessage("Parameter not convertable!");
		}
	}

	public void sendMotorSpeedButton() {
		final String arg = gui.getMotorSpeed();
		if (ApplicationCommandParser.doubleConvertable(arg)) {
			final double paramValue = Double.parseDouble(arg);
			comm.sendSet(PID_MOTOR_SPEED, paramValue);
			comm.sendGet(PID_MOTOR_SPEED);
		} else {
			gui.showMessage("Parameter not convertable!");
		}
	}

	public void sendConstantSpeedButton() {
		final String arg = gui.getConstantSpeed();
		if (ApplicationCommandParser.floatConvertable(arg)) {
			final float paramValue = Float.parseFloat(arg);
			comm.sendSet(PARAM_CONSTANT_SPEED, paramValue);
		} else {
			gui.showMessage("Parameter not convertable!");
		}
	}

	public void sendConstantRotationButton() {
		final String arg = gui.getConstantSpeed();
		if (ApplicationCommandParser.floatConvertable(arg)) {
			final float paramValue = Float.parseFloat(arg);
			comm.sendSet(PARAM_CONSTANT_ROTATION, paramValue);
		} else {
			gui.showMessage("Parameter not convertable!");
		}
	}

	public void sendWheeldiameterButton() {
		final String arg = gui.getWheelDiameter();
		if (ApplicationCommandParser.floatConvertable(arg)) {
			final float paramValue = Float.parseFloat(arg);
			comm.sendSet(PARAM_WHEEL_DIAMETER, paramValue);
		} else {
			gui.showMessage("Parameter not convertable!");
		}
	}

	public void sendTrackButton() {
		final String arg = gui.getTrack();
		if (ApplicationCommandParser.floatConvertable(arg)) {
			final float paramValue = Float.parseFloat(arg);
			comm.sendSet(PARAM_TRACK, paramValue);
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
		new EvoAlgorithm(gui, comm, data).start();
	}

	private Gamepad gamepad;

	public void showBlockedSign(boolean isBlocked) {
		gui.showBlockedWay(isBlocked);
	}

	public void gamepadControl(boolean enabled) {
		if (enabled) {
			gamepad = Gamepad.findGamepad();
			if (gamepad == null) {
				gui.showMessage("No Gamepad found");
				gui.chkGamepad.setSelected(false);
			}

			final Thread t = new Thread(() -> {
				double lastMoveSpeed = 0, lastTurnSpeed = 0; // Cache last speeds in order to not spam the NXT with meaningless updates

				while (gamepad != null && gamepad.isActive() && comm.isConnected()) {
					float moveSpeed = Math.round(-gamepad.zAxis * DEFAULT_MOVE_SPEED * 20) / 20f;
					if (Math.abs(moveSpeed) < 0.1) {
						moveSpeed = 0;
					}
					if (lastMoveSpeed != moveSpeed) {
						comm.sendSet(PARAM_CONSTANT_SPEED, moveSpeed);
						lastMoveSpeed = moveSpeed;
					}

					float turnSpeed = Math.round(-gamepad.xAxis * DEFAULT_TURN_SPEED * 2 * 20) / 20f;
					if (Math.abs(turnSpeed) < 0.1) {
						turnSpeed = 0;
					}

					if (lastTurnSpeed != turnSpeed) {
						comm.sendSet(PARAM_CONSTANT_ROTATION, turnSpeed);
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
}
