package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.*;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.CommunicatorPC;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.data.NXTData;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo.EvoAlgorithm;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui.gamepad.Gamepad;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.nav.NavigationThread;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.nav.Navigator;

/**
 * This Method is the handler for everything registered in the UI class. Every Button pushed will call a method in this class.
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

	/**
	 * This method changes the method the connect button uses when pushed, if the NXT is already connected, it becomes a disconnect button.
	 */
	public void connectButton() {
		if (!isConnected()) {
			connect();
		} else {
			disconnect();
		}
	}

	/**
	 * This method connects PC and NXT and starts all necessary handlers on PC side.
	 */
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

	/**
	 * This method stops all running threads which try to communicate with the NXT and changes some labels in the UI to show the NXT is not connected anymore.
	 */
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
	
	/**
	 * This method is called when the "send" button in the bottom right corner is pushed.
	 * The input of the text field next to it is then handled by the Parser.
	 */
	public void sendCommandButton() {
		final String input = gui.getInput();
		gui.showMessage("input: " + input);

		new ApplicationCommandParser(gui, comm).parse(input);
	}

	// PositionTab

	// For spamming control
	private boolean forward, left, backward, right;

	/**
	 * This method makes the NXT move forward
	 */
	public void moveForward() {
		if (!forward) {
			gui.showMessage(String.format("Move forward (Speed=%.1fcm/s)", DEFAULT_MOVE_SPEED));
			comm.sendSet(PARAM_CONSTANT_SPEED, DEFAULT_MOVE_SPEED);
			forward = true;
		}
	}

	/**
	 * This method makes the NXT move backwards
	 */
	public void moveBackward() {
		if (!backward) {
			gui.showMessage(String.format("Move backward (Speed=%.1fcm/s)", DEFAULT_MOVE_SPEED));
			comm.sendSet(PARAM_CONSTANT_SPEED, -DEFAULT_MOVE_SPEED);
			backward = true;
		}
	}

	/**
	 * This method makes the NXT turn left
	 */
	public void turnLeft() {
		if (!left) {
			gui.showMessage(String.format("Turn left (Speed=%.1f°/s)", DEFAULT_TURN_SPEED));
			comm.sendSet(PARAM_CONSTANT_ROTATION, DEFAULT_TURN_SPEED);
			left = true;
		}
	}

	/**
	 * This method makes the NXT turn right
	 */
	public void turnRight() {
		if (!right) {
			gui.showMessage(String.format("Turn right (Speed=%.1f°/s)", DEFAULT_TURN_SPEED));
			comm.sendSet(PARAM_CONSTANT_ROTATION, -DEFAULT_TURN_SPEED);
			right = true;
		}
	}

	/**
	 * This method makes the NXT moving forward
	 */
	public void startMoving() {
		if (!forward) {
			comm.sendSet(PARAM_CONSTANT_SPEED, DEFAULT_MOVE_SPEED);
			forward = true;
		}
	}

	/**
	 * This method makes the NXT stop moving
	 */
	public void stopMoving() {
		gui.showMessage("Stop moving");
		comm.sendSet(PARAM_CONSTANT_SPEED, 0f);
		forward = false;
		backward = false;
	}

	/**
	 * This method makes the NXT stop turning
	 */
	public void stopTurning() {
		gui.showMessage("Stop turning");
		comm.sendSet(PARAM_CONSTANT_ROTATION, 0f);
		left = false;
		right = false;
	}

	/**
	 * This method is called when the move button in the UI is pushed. The method reads the input from the text field next to the button and sends a move command, if the input is valid.
	 * 
	 */
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

	/**
	 * This method is called when the "turn absolute" button is pushed in the UI. This method reads the input from the text field next to the button and calls turnAbsolteMethod(float f), if the input is valid.
	 * 
	 */
	public void turnAbsoluteButton() {
		final String arg = gui.getTurnAbsolute();
		if (ApplicationCommandParser.floatConvertable(arg)) {
			final float targetHeading = Float.parseFloat(arg);
			turnAbsoluteMethod(targetHeading);
		} else {
			gui.showMessage("Parameter not convertable!");
		}
	}

	/**
	 * This method reads the NXT's current heading and calculates the angle necessary to turn to the absolute angle.
	 * @param targetHeading the absolute heading the NXT has to turn to.
	 */
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

	/**
	 * This method is called when the "turn relative" Button is pushed. It reads the input of the text field next to the button and sends a turn command.
	 */
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

	/**
	 * This method is an alternative version of a turn command. This method separates a large turn command into several smaller turn commands, to increase stability and accuracy of the sensors.
	 * 
	 * @param turnDegree the turning angle of the turn
	 */
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

	/**
	 * This method is called when the "drive to" Button is pushed in the UI. It reads the data from the text fields next to the button and calls "driveToMethod" with these parameters.
	 */
	public void driveToButton() {
		final String posXText = gui.getDriveToX();
		final String posYText = gui.getDriveToY();
		driveToMethod(posXText, posYText);
	}

	/**
	 * This method tries to parse the Parameters to numbers. If this is successful a new NavigationThread is started with the parsed Parameters.
	 * @param posXText Position x
	 * @param posYText Position y
	 */
	public void driveToMethod(String posXText, String posYText) {
		float diffX, diffY; 

		if (ApplicationCommandParser.floatConvertable(posXText) && ApplicationCommandParser.floatConvertable(posYText)) {
			diffX = Float.parseFloat(posXText); // - posX
			diffY = Float.parseFloat(posYText); // - posY
			if (naviThread != null) {
				naviThread.setRunning(false);
			}
			naviThread = new NavigationThread(navi, this, diffX, diffY);
			naviThread.start();

		} else {
			gui.showMessage("Something went wrong with parsing parameters");
		}
	}

	/**
	 * This method calculates angle and distance from NXT's Position to the given coordinates. Then it calls methods to send commands
	 * @param posX x coordinate of the destination
	 * @param posY y coordinate of the destination
	 */
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

	/**
	 * This method makes the NXT face a given Point. It does so by calculating the angle and calling a send method.
	 * @param xTarget x coordinate of the destination
	 * @param yTarget y coordinate of the destination
	 */
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
		//waiting because of following movement commands, which could have a negative impact on the balancing
		try {
			Thread.sleep(DEFAULT_NAVIGATION_SLEEP_TIME);
		} catch (final Exception e) {

		}
	}

	/**
	 * This method is called when the "set Position" Button is called. 
	 * It reads the input from the text fields next to the button, then tries to parse it. 
	 * If this is successful, the method sends the NXT the new Position and clears the map
	 */
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

	/**
	 * resets the map.
	 */
	public void resetMap() {
		navi.resetMapData();
	}

	/**
	 * This method is called when the "set Heading" Button is called. 
	 * It reads the input from the text field next to the button, then tries to parse it. 
	 * If this is successful, the method sends the NXT the new Heading and clears the map
	 */
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

	/**
	 * This method sets the AutostatusPacket Parameter to the given Parameter. It does so by calling a send command
	 * @param status the new status of the autoStatusPacket Parameter
	 */
	public void sendAutostatuspacket(boolean status) {
		comm.sendSet(AUTO_STATUS_PACKET, status);
	}

	/**
	 * This method changes the balancing Parameter to the given Parameter. It does so by calling a send command.
	 * @param status the new Value of the balancing Parameter
	 */
	public void sendBalancieren(boolean status) {
		comm.sendBalancing(status);
		data.setBalancing(status);
		new Thread(collision).start();
	}

	// ParameterTab
	/**
	 * This method reads the input from a text field in the UI, tries to parse it and then sends it to the NXT as the GyroSpeed Parameter.
	 */
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

	/**
	 * This method reads the input from a text field in the UI, tries to parse it and then sends it to the NXT as the GyroIntegral Parameter.
	 */
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

	/**
	 * This method reads the input from a text field in the UI, tries to parse it and then sends it to the NXT as the MotorDistance Parameter.
	 */
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

	/**
	 * This method reads the input from a text field in the UI, tries to parse it and then sends it to the NXT as the MotorSpeed Parameter.
	 */
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

	/**
	 * This method reads the input from a text field in the UI, tries to parse it and then sends it to the NXT as the ConstantSpeed Parameter.
	 */
	public void sendConstantSpeedButton() {
		final String arg = gui.getConstantSpeed();
		if (ApplicationCommandParser.floatConvertable(arg)) {
			final float paramValue = Float.parseFloat(arg);
			comm.sendSet(PARAM_CONSTANT_SPEED, paramValue);
		} else {
			gui.showMessage("Parameter not convertable!");
		}
	}

	/**
	 * This method reads the input from a text field in the UI, tries to parse it and then sends it to the NXT as the ConstantRotation Parameter.
	 */
	public void sendConstantRotationButton() {
		final String arg = gui.getConstantSpeed();
		if (ApplicationCommandParser.floatConvertable(arg)) {
			final float paramValue = Float.parseFloat(arg);
			comm.sendSet(PARAM_CONSTANT_ROTATION, paramValue);
		} else {
			gui.showMessage("Parameter not convertable!");
		}
	}

	/**
	 * This method reads the input from a text field in the UI, tries to parse it and then sends it to the NXT as the WheelDiameter Parameter.
	 */
	public void sendWheeldiameterButton() {
		final String arg = gui.getWheelDiameter();
		if (ApplicationCommandParser.floatConvertable(arg)) {
			final float paramValue = Float.parseFloat(arg);
			comm.sendSet(PARAM_WHEEL_DIAMETER, paramValue);
		} else {
			gui.showMessage("Parameter not convertable!");
		}
	}

	/**
	 * This method reads the input from a text field in the UI, tries to parse it and then sends it to the NXT as the Track Parameter.
	 */
	public void sendTrackButton() {
		final String arg = gui.getTrack();
		if (ApplicationCommandParser.floatConvertable(arg)) {
			final float paramValue = Float.parseFloat(arg);
			comm.sendSet(PARAM_TRACK, paramValue);
		} else {
			gui.showMessage("Parameter not convertable!");
		}
	}

	/**
	 * This method sends all Parameters at once.
	 */
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

	/**
	 * This method starts the Evo Algorithm
	 */
	public void startEvoAlgButton() {
		new EvoAlgorithm(gui, comm, data).start();
	}

	private Gamepad gamepad;

	/**
	 * This method changes the visibility of the "blocked way" sign to the given parameter.
	 * @param isBlocked visibility of the "blocked way" sign
	 */
	public void showBlockedSign(boolean isBlocked) {
		gui.showBlockedWay(isBlocked);
	}

	/**
	 * Changes the gamepad support
	 * @param enabled true if the gamepad needs to be enabled, false if it is needed to be disabled.
	 */
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
					float moveSpeed = -gamepad.zAxis * DEFAULT_MOVE_SPEED;
					if (Math.abs(moveSpeed) < 0.1) {
						moveSpeed = 0;
					}
					if (lastMoveSpeed != moveSpeed) {
						comm.sendSet(PARAM_CONSTANT_SPEED, moveSpeed);
						lastMoveSpeed = moveSpeed;
					}

					float turnSpeed = -gamepad.xAxis * DEFAULT_TURN_SPEED * 2;
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
