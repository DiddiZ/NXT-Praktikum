package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.*;
import java.io.DataInputStream;
import java.io.IOException;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.CommandHandler;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.data.NXTData;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo.Measurements;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui.MapUpdater;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui.UserInterface;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.nav.Navigator;

/**
 * This class handles incoming data marked as GET_RETURN.
 * The data is sent by an NXT either as an AUTO_STATUS_PACKET or as response to a GET command.
 * Implements the {@link CommandHandler} interface and defines the callback-method handle().
 * 
 * @author Gregor & Justus
 */

public final class GetReturnHandler implements CommandHandler
{
	private final UserInterface ui;
	public final NXTData data;
	public final Navigator navi;
	private final MapUpdater updater;

	/**
	 * The constructor for a GetReturnHandler object.
	 * Simply assigns necessary attributes to the object and creates a {@link MapUpdater}.
	 * 
	 * @param ui: The UI on which the handler displays received values for parameters.
	 * @param data: The known data of the NXT which is updated by this handler
	 * @param navi: The navigator the handler sends data of the ultrasonic-sensor to to create a map of the environment.
	 */
	public GetReturnHandler(UserInterface ui, NXTData data, Navigator navi) {
		this.ui = ui;
		this.data = data;
		this.navi = navi;
		updater = new MapUpdater(ui);
	}

	/**
	 * This method reads the parameter from the input-stream and switches it.
	 * The type and number of the values depends on the parameter.
	 * <p>
	 * Currently recognized parameters:
	 * <table>
	 * <tr><td>BATTERY_VOLTAGE:</td><td>Calls a method of the UI to update the value.</td></tr>
	 * <tr><td>GYRO_ANGLE:</td><td>Calls a method of the UI to update the value.</td></tr>
	 * <tr><td>TACHO_LEFT:</td><td>Calls a method of the UI to update the value.</td></tr>
	 * <tr><td>TACHO_RIGHT:</td><td>Calls a method of the UI to update the value.</td></tr>
	 * <tr><td>HEADING:</td><td>Calls a method of the UI to update the value and overwrites the value in the NXTData.</td></tr>
	 * <tr><td>POSITION:</td><td>Calls a method of the UI to update the value and overwrites the value in the NXTData.</td></tr>
	 * <tr><td>MOVEMENT_SPEED:</td><td>Calls a method of the UI to update the value.</td></tr>
	 * <tr><td>STATUS_PACKET:</td><td>Calls a method of the UI to update the values and overwrites the heading and position in NXTData. Starts a MapUpdater-thread if possible.</td></tr>
	 * <tr><td>AUTO_STATUS_PACKET:</td><td>Calls a method of the UI to update the value.</td></tr>
	 * <tr><td>PID_GYRO_SPEED:</td><td>Calls a method of the UI to update the value.</td></tr>
	 * <tr><td>PID_GYRO_INTEGRAL:</td><td>Calls a method of the UI to update the value.</td></tr>
	 * <tr><td>PID_MOTOR_DISTANCE:</td><td>Calls a method of the UI to update the value.</td></tr>
	 * <tr><td>PID_MOTOR_SPEED:</td><td>Calls a method of the UI to update the value.</td></tr>
	 * <tr><td>PARAM_CONSTANT_ROTATION:</td><td>Just reads the value to not mess up the input-stream.</td></tr>
	 * <tr><td>PARAM_CONSTANT_SPEED:</td><td>Just reads the value to not mess up the input-stream.</td></tr>
	 * <tr><td>PARAM_WHEEL_DIAMETER:</td><td>Just reads the value to not mess up the input-stream.</td></tr>
	 * <tr><td>PARAM_TRACK:</td><td>Just reads the value to not mess up the input-stream.</td></tr>
	 * <tr><td>PID_WEIGHT_ALL:</td><td>Calls a method of the UI to update the value.</td></tr>
	 * <tr><td>PARAM_ULTRA_SENSOR:</td><td>Calls a method of the Navigator to update the map.</td></tr>
	 * <tr><td>EVO_MEASUREMENTS:</td><td>Calls a method to update the NXTData.</td></tr>
	 * </table>
	 * Default: Print the information, that the parameter is unknown.
	 * 
	 * @param is: The DataInputStream the handler uses to receive the data.
	 */
	@Override
	public void handle(DataInputStream is) throws IOException {
		final byte param = is.readByte();
		switch (param) {
			case BATTERY_VOLTAGE:
				final int voltage = is.readInt();
				ui.showBatteryVoltage(voltage);
				break;
			case GYRO_ANGLE:
				final float angle = is.readFloat();
				ui.showTilt(angle);
				break;
			case TACHO_LEFT:
				final long tachoLeft = is.readLong();
				ui.showTachoLeft(tachoLeft);
				break;
			case TACHO_RIGHT:
				final long tachoRight = is.readLong();
				ui.showTachoRight(tachoRight);
				break;
			case HEADING:
				final float heading = is.readFloat();
				data.setHeading(heading);
				ui.showHeading(heading);
				break;
			case POSITION:
				final float posX = -is.readFloat();
				final float posY = is.readFloat();
				data.setPosition(posX, posY);
				ui.showPosition(posX, posY);
				break;
			case MOVEMENT_SPEED:
				final float movementSpeed = is.readFloat();
				ui.showSpeed(movementSpeed);
				break;
			case STATUS_PACKET:
				final float posX_all = -is.readFloat();
				final float posY_all = is.readFloat();
				final float movementSpeed_all = is.readFloat();
				final float heading_all = is.readFloat();

				data.setHeading(heading_all);
				data.setPosition(posX_all, posY_all);
				ui.showSpeed(movementSpeed_all);
				ui.showPosition(posX_all, posY_all);
				ui.showHeading(heading_all);
				if (MapUpdater.canRun()) {
					new Thread(updater).start();
				}
				break;
			case AUTO_STATUS_PACKET:
				final boolean enabled = is.readBoolean();
				ui.showAutoStatusPacketEnabled(enabled);
				break;
			case PID_GYRO_SPEED:
				final double gyroSpeed = is.readDouble();
				ui.showGyroSpeedWeight(gyroSpeed);
				break;
			case PID_GYRO_INTEGRAL:
				final double gyroIntegral = is.readDouble();
				ui.showGyroIntegralWeight(gyroIntegral);
				break;
			case PID_MOTOR_DISTANCE:
				final double motorDistance = is.readDouble();
				ui.showMotorDistanceWeight(motorDistance);
				break;
			case PID_MOTOR_SPEED:
				final double motorSpeed = is.readDouble();
				ui.showMotorSpeedWeight(motorSpeed);
				break;
			case PARAM_CONSTANT_ROTATION:
				@SuppressWarnings("unused")
				final float constantRotation = is.readFloat();
				// TODO: Print the value in the GUI
				break;
			case PARAM_CONSTANT_SPEED:
				@SuppressWarnings("unused")
				final float constantSpeed = is.readFloat();
				// TODO: Print the value in the GUI
				break;
			case PARAM_WHEEL_DIAMETER:
				@SuppressWarnings("unused")
				final float wheelDiameter = is.readFloat();
				// TODO: Print the value in the GUI
				break;
			case PARAM_TRACK:
				@SuppressWarnings("unused")
				final float track = is.readFloat();
				// TODO: Print the value in the GUI
				break;
			case PID_WEIGHT_ALL:
				final double gyroSpeed_all = is.readDouble();
				final double gyroIntegral_all = is.readDouble();
				final double motorDistance_all = is.readDouble();
				final double motorSpeed_all = is.readDouble();

				ui.showGyroSpeedWeight(gyroSpeed_all);
				ui.showGyroIntegralWeight(gyroIntegral_all);
				ui.showMotorDistanceWeight(motorDistance_all);
				ui.showMotorSpeedWeight(motorSpeed_all);
				break;
			case PARAM_ULTRASENSOR:
				final int distance = is.readByte() & 0xFF;
				navi.addSensorData(distance);
				break;
			case EVO_MEASUREMENTS:
				data.setMeasurements(new Measurements(is.readDouble(), is.readDouble(), is.readDouble(), is.readDouble()));
				break;
			default:
				System.out.println("Unrecognized GetReturn command with " + param);
		}
	}
}
