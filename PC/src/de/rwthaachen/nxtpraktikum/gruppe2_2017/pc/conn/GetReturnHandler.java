package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn;
/**
 * @author Gregor & Justus
 * 
 * This class handles the GET_RETURN command.
 */
import java.io.DataInputStream;
import java.io.IOException;

import org.eclipse.swt.widgets.Display;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.CommandHandler;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui.*;

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
				Display.getDefault().syncExec(new Runnable() {public void run() {
					    application.output(""+voltage);
				}});
				//application.output(""+voltage);
				//application.setBatteryLabel(voltage);
				break;
			case GYRO_ANGLE:
				final float angle = is.readFloat();
				//System.out.println("Gyro angle: " + angle + "°");
				application.setTiltLabel(angle);
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
				//System.out.println("Heading: " + heading + "°");
				application.setRotationLabel(heading);
				break;
			case POSITION:
				final float posX = is.readFloat();
				final float posY = is.readFloat();
				//System.out.println("Position: (" + posX + ", " + posY + ")");
				application.setPositionLabel(posX, posY);
				break;
			case MOVEMENT_SPEED:
				final float movementSpeed = is.readFloat();
				//System.out.println("Movement speed: " + movementSpeed + "cm/s");
				application.setSpeedometerLabel(movementSpeed);
				break;
			case STATUS_PACKAGE:
				final float posX_all = is.readFloat();
				final float posY_all = is.readFloat();
				final float movementSpeed_all = is.readFloat();
				final float heading_all = is.readFloat();
				System.out.println("Position: (" + posX_all + ", " + posY_all + ")");
				System.out.println("Movement speed: " + movementSpeed_all + "cm/s");
				System.out.println("Heading: " + heading_all + "°");
				//application.setPositionLabel(posX_all, posY_all);
				//application.setSpeedometerLabel(movementSpeed_all);
				//application.setRotationLabel(heading_all);
				break;
			case AUTO_STATUS_PACKAGE:
				final boolean isOn = is.readBoolean();
				//System.out.println("Auto status package: " + isOn);
				application.setAutoStatusPacket(isOn);
				break;
			case PID_GYRO_SPEED:
				final double gyroSpeed = is.readDouble();
				//System.out.println("Gyro speed weight: " + gyroSpeed);
				application.setGyroSpeedt(gyroSpeed);
				break;
			case PID_GYRO_INTEGRAL:
				final double gyroIntegral = is.readDouble();
				//System.out.println("Gyro integral weight: " + gyroIntegral);
				application.setGyroIntegralt(gyroIntegral);
				break;
			case PID_MOTOR_DISTANCE:
				final double motorDistance = is.readDouble();
				//System.out.println("Motor distance weight: " + motorDistance);
				application.setMotorDistancet(motorDistance);
				break;
			case PID_MOTOR_SPEED:
				final double motorSpeed = is.readDouble();
				//System.out.println("Motor speed weight: " + motorSpeed);
				application.setMotorSpeedt(motorSpeed);
				break;
			case PID_WEIGHT_ALL:
				final double gyroSpeed_all = is.readDouble();
				final double gyroIntegral_all = is.readDouble();
				final double motorDistance_all = is.readDouble();
				final double motorSpeed_all = is.readDouble();
				//System.out.println("Gyro speed weight: " + gyroSpeed_all);
				//System.out.println("Gyro integral weight: " + gyroIntegral_all);
				//System.out.println("Motor distance weight: " + motorDistance_all);
				//System.out.println("Motor speed weight: " + motorSpeed_all);
				application.setGyroSpeedt(gyroSpeed_all);
				application.setGyroIntegralt(gyroIntegral_all);
				application.setMotorDistancet(motorDistance_all);
				application.setMotorSpeedt(motorSpeed_all);
				break;
			default:
				System.out.println("Unrecognized GetReturn command with " + param);
		}
	}
}
