package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.*;
/**
 * This class handles the GET_RETURN command.
 *
 * @author Gregor & Justus
 */
import java.io.DataInputStream;
import java.io.IOException;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.CommandHandler;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui.UserInterface;

public final class GetReturnHandler implements CommandHandler
{
	private final UserInterface ui;
	public final NXTData data;

	public GetReturnHandler(UserInterface ui, NXTData data) {
		this.ui = ui;
		this.data = data;
	}

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
				final float posX = (-1f)*is.readFloat();
				final float posY = is.readFloat();
				
				data.setPosition(posX, posY);
				ui.showPosition(posX, posY);
				ui.drawPosition(((int)posX), (int)posY);
				break;
			case MOVEMENT_SPEED:
				final float movementSpeed = is.readFloat();
				ui.showSpeed(movementSpeed);
				break;
			case STATUS_PACKET:
				final float posX_all = (-1f)*is.readFloat();
				final float posY_all = is.readFloat();
				final float movementSpeed_all = is.readFloat();
				final float heading_all = is.readFloat();

				data.setHeading(heading_all);
				data.setPosition(posX_all, posY_all);
				ui.showSpeed(movementSpeed_all);
				ui.showPosition(posX_all, posY_all);
				ui.showHeading(heading_all);
				ui.drawPosition(((int)posX_all), (int)posY_all);
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
				final float p_range = is.readFloat();
				final float p_angle = is.readFloat();
				System.out.println("Object distance: " + p_range + "; angle: " + p_angle);
				// TODO: handle the input
				break;
			default:
				System.out.println("Unrecognized GetReturn command with " + param);
		}
	}
}
