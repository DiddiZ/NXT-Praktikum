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

	public GetReturnHandler(UserInterface ui) {
		this.ui = ui;
	}

	@Override
	public void handle(DataInputStream is) throws IOException {
		final byte param = is.readByte();
		switch (param) {
			case BATTERY_VOLTAGE:
				final int voltage = is.readInt();
				ui.setBatteryLabel(voltage);
				break;
			case GYRO_ANGLE:
				final float angle = is.readFloat();
				ui.setTiltLabel(angle);
				break;
			case TACHO_LEFT:
				final long tachoLeft = is.readLong();

				ui.setTachoLeft(tachoLeft);
				break;
			case TACHO_RIGHT:
				final long tachoRight = is.readLong();

				ui.setTachoRight(tachoRight);
				break;
			case HEADING:
				final float heading = is.readFloat();
				ui.setRotationLabel(heading);
				break;
			case POSITION:
				final float posX = is.readFloat();
				final float posY = is.readFloat();

				ui.setCurrentPositionLabel(posX, posY);
				break;
			case MOVEMENT_SPEED:
				final float movementSpeed = is.readFloat();
				ui.setSpeedometerLabel(movementSpeed);
				break;
			case STATUS_PACKET:
				final float posX_all = is.readFloat();
				final float posY_all = is.readFloat();
				final float movementSpeed_all = is.readFloat();
				final float heading_all = is.readFloat();

				ui.setSpeedometerLabel(movementSpeed_all);
				ui.setCurrentPositionLabel(posX_all, posY_all);
				ui.setRotationLabel(heading_all);
				break;
			case AUTO_STATUS_PACKET:
				final boolean enabled = is.readBoolean();

				ui.setAutoStatusPacket(enabled);
				break;
			case PID_GYRO_SPEED:
				final double gyroSpeed = is.readDouble();

				ui.setGyroSpeedt(gyroSpeed);
				break;
			case PID_GYRO_INTEGRAL:
				final double gyroIntegral = is.readDouble();

				ui.setGyroIntegralt(gyroIntegral);
				break;
			case PID_MOTOR_DISTANCE:
				final double motorDistance = is.readDouble();

				ui.setMotorDistancet(motorDistance);
				break;
			case PID_MOTOR_SPEED:
				final double motorSpeed = is.readDouble();

				ui.setMotorSpeedt(motorSpeed);
				break;
			case PARAM_CONSTANT_ROTATION:
				final double constantRotation = is.readDouble();
				// TODO: Print the value in the GUI
				System.out.println("Received constant rotation: " + constantRotation);
				break;
			case PARAM_CONSTANT_SPEED:
				final double constantSpeed = is.readDouble();
				// TODO: Print the value in the GUI
				System.out.println("Received constant speed: " + constantSpeed);
				break;
			case PARAM_WHEEL_DIAMETER:
				final double wheelDiameter = is.readDouble();
				// TODO: Print the value in the GUI
				System.out.println("Received wheel diameter: " + wheelDiameter);
				break;
			case PARAM_TRACK:
				final double track = is.readDouble();
				// TODO: Print the value in the GUI
				System.out.println("Received track: " + track);
				break;
			case PID_WEIGHT_ALL:
				final double gyroSpeed_all = is.readDouble();
				final double gyroIntegral_all = is.readDouble();
				final double motorDistance_all = is.readDouble();
				final double motorSpeed_all = is.readDouble();

				ui.setGyroSpeedt(gyroSpeed_all);
				ui.setGyroIntegralt(gyroIntegral_all);
				ui.setMotorDistancet(motorDistance_all);
				ui.setMotorSpeedt(motorSpeed_all);
				break;
			default:
				System.out.println("Unrecognized GetReturn command with " + param);
		}
	}
}
