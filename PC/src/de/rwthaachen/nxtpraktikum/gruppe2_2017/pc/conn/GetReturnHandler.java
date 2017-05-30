package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn;

import java.io.DataInputStream;
import java.io.IOException;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.CommandHandler;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.*;

public final class GetReturnHandler implements CommandHandler
{
	@Override
	public void handle(DataInputStream is) throws IOException {
		final byte param = is.readByte();
		switch (param) {
			case BATTERY_VOLTAGE:
				final int voltage = is.readInt();
				System.out.println("Voltage: " + voltage + "V");
				break;
			case GYRO_ANGLE:
				final float angle = is.readFloat();
				System.out.println("Gyro angle: " + angle + "°");
				break;
			case TACHO_LEFT:
				final long tachoLeft = is.readLong();
				System.out.println("Tacho left: " + tachoLeft + "°");
				break;
			case TACHO_RIGHT:
				final long tachoRight = is.readLong();
				System.out.println("Tacho right: " + tachoRight + "°");
				break;
			case HEADING:
				final float heading = is.readFloat();
				System.out.println("Heading: " + heading + "°");
				break;
			case POSITION:
				final float posX = is.readFloat();
				final float posY = is.readFloat();
				System.out.println("Position: (" + posX + ", " + posY + ")");
				break;
			case MOVEMENT_SPEED:
				final float movementSpeed = is.readFloat();
				System.out.println("Movement speed: " + movementSpeed + "cm/s");
				break;
			case STATUS_PACKAGE:
				final float posX_all = is.readFloat();
				final float posY_all = is.readFloat();
				final float movementSpeed_all = is.readFloat();
				final float heading_all = is.readFloat();
				System.out.println("Position: (" + posX_all + ", " + posY_all + ")");
				System.out.println("Movement speed: " + movementSpeed_all + "cm/s");
				System.out.println("Heading: " + heading_all + "°");
				break;
			case AUTO_STATUS_PACKAGE:
				final boolean isOn = is.readBoolean();
				System.out.println("Auto status package: " + isOn);
				break;
			case PID_GYRO_SPEED:
				final double gyroSpeed = is.readDouble();
				System.out.println("Gyro speed weight: " + gyroSpeed);
				break;
			case PID_GYRO_INTEGRAL:
				final double gyroIntegral = is.readDouble();
				System.out.println("Gyro integral weight: " + gyroIntegral);
				break;
			case PID_MOTOR_DISTANCE:
				final double motorDistance = is.readDouble();
				System.out.println("Motor distance weight: " + motorDistance);
				break;
			case PID_MOTOR_SPEED:
				final double motorSpeed = is.readDouble();
				System.out.println("Motor speed weight: " + motorSpeed);
				break;
			case PID_WEIGHT_ALL:
				final double gyroSpeed_all = is.readDouble();
				final double gyroIntegral_all = is.readDouble();
				final double motorDistance_all = is.readDouble();
				final double motorSpeed_all = is.readDouble();
				System.out.println("Gyro speed weight: " + gyroSpeed_all);
				System.out.println("Gyro integral weight: " + gyroIntegral_all);
				System.out.println("Motor distance weight: " + motorDistance_all);
				System.out.println("Motor speed weight: " + motorSpeed_all);
			default:
				System.out.println("Unrecognized GetReturn command with " + param);
		}
	}
}
