package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.*;
import java.io.DataInputStream;
import java.io.IOException;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.CommandHandler;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo.Measurements;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui.MapUpdater;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui.Navigator;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui.UserInterface;
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

	@Override
	/**
	 * This method reads the parameter from the input-stream and switches it. 
	 * The type and number of the values depends on the parameter.
	 * 
	 * Currently recognized parameters:
	 * BATTERY_VOLTAGE: 		Calls a method of the UI to update the value.
	 * GYRO_ANGLE: 				Calls a method of the UI to update the value.
	 * TACHO_LEFT: 				Calls a method of the UI to update the value.
	 * TACHO_RIGHT:				Calls a method of the UI to update the value.
	 * HEADING: 				Calls a method of the UI to update the value and overwrites the value in the NXTData.
	 * POSITION: 				Calls a method of the UI to update the value and overwrites the value in the NXTData.
	 * MOVEMENT_SPEED: 			Calls a method of the UI to update the value.
	 * STATUS_PACKET: 			Calls a method of the UI to update the values and overwrites the heading and position in NXTData.
	 * 				  			Starts a MapUpdater-thread if possible.
	 * AUTO_STATUS_PACKET: 		Calls a method of the UI to update the value.
	 * PID_GYRO_SPEED: 			Calls a method of the UI to update the value.
	 * PID_GYRO_INTEGRAL: 		Calls a method of the UI to update the value.
	 * PID_MOTOR_DISTANCE: 		Calls a method of the UI to update the value.
	 * PID_MOTOR_SPEED: 		Calls a method of the UI to update the value.
	 * PARAM_CONSTANT_ROTATION: Just reads the value to not mess up the input-stream.
	 * PARAM_CONSTANT_SPEED: 	Just reads the value to not mess up the input-stream.
	 * PARAM_WHEEL_DIAMETER: 	Just reads the value to not mess up the input-stream.
	 * PARAM_TRACK: 			Just reads the value to not mess up the input-stream.
	 * PID_WEIGHT_ALL: 			Calls a method of the UI to update the value.
	 * PARAM_ULTRA_SENSOR: 		Calls a method of the Navigator to update the map.
	 * EVO_MEASUREMENTS: 		Calls a method to update the NXTData.
	 * 
	 * Default: Print the information, that the parameter is unknown.
	 * 
	 * @param is: The DataInputStream the handler uses to receive the data.
	 */
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
