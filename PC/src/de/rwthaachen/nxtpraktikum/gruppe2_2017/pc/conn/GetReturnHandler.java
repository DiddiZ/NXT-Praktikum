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
				SyncExec.syncsetBatteryLabel(voltage);
				break;
			case GYRO_ANGLE:
				final float angle = is.readFloat();
				//System.out.println("Gyro angle: " + angle + "�");
				SyncExec.syncsetTiltLabel(angle);
				break;
			case TACHO_LEFT:
				final long tachoLeft = is.readLong();
				SyncExec.syncsetTachoLeft(tachoLeft);
				break;
			case TACHO_RIGHT:
				final long tachoRight = is.readLong();
				SyncExec.syncsetTachoRight(tachoRight);
				break;
			case HEADING:
				final float heading = is.readFloat();
				//System.out.println("Heading: " + heading + "�");
				SyncExec.syncsetRotationLabel(heading);
				break;
			case POSITION:
				final float posX = is.readFloat();
				final float posY = is.readFloat();
				//System.out.println("Position: (" + posX + ", " + posY + ")");
				SyncExec.syncsetPositionLabel(posX, posY);
				break;
			case MOVEMENT_SPEED:
				final float movementSpeed = is.readFloat();
				//System.out.println("Movement speed: " + movementSpeed + "cm/s");
				SyncExec.syncsetSpeedometerLabel(movementSpeed);
				break;
			case STATUS_PACKET:
				final float posX_all = is.readFloat();
				final float posY_all = is.readFloat();
				final float movementSpeed_all = is.readFloat();
				final float heading_all = is.readFloat();
				//System.out.println("Position: (" + posX_all + ", " + posY_all + ")");
				//System.out.println("Movement speed: " + movementSpeed_all + "cm/s");
				//System.out.println("Heading: " + heading_all + "�");
				SyncExec.syncsetSpeedometerLabel(movementSpeed_all);
				SyncExec.syncsetPositionLabel(posX_all, posY_all);
				SyncExec.syncsetRotationLabel(heading_all);
				break;
			case AUTO_STATUS_PACKET:
				final boolean isOn = is.readBoolean();
				//System.out.println("Auto status package: " + isOn);
				SyncExec.syncsetAutoStatusPacket(isOn);
				break;
			case PID_GYRO_SPEED:
				final double gyroSpeed = is.readDouble();
				//System.out.println("Gyro speed weight: " + gyroSpeed);
				SyncExec.syncsetGyroSpeed(gyroSpeed);
				break;
			case PID_GYRO_INTEGRAL:
				final double gyroIntegral = is.readDouble();
				//System.out.println("Gyro integral weight: " + gyroIntegral);
				SyncExec.syncsetGyroIntegral(gyroIntegral);
				break;
			case PID_MOTOR_DISTANCE:
				final double motorDistance = is.readDouble();
				//System.out.println("Motor distance weight: " + motorDistance);
				SyncExec.syncsetMotorDistance(motorDistance);
				break;
			case PID_MOTOR_SPEED:
				final double motorSpeed = is.readDouble();
				//System.out.println("Motor speed weight: " + motorSpeed);
				SyncExec.syncsetMotorSpeed(motorSpeed);
				break;
			case CONSTANT_ROTATION:
				final double constantRotation = is.readDouble();
				// TODO: Print the value in the GUI
				System.out.println("Received constant rotation: " + constantRotation);
				break;
			case CONSTANT_SPEED:
				final double constantSpeed = is.readDouble();
				// TODO: Print the value in the GUI
				System.out.println("Received constant speed: "+ constantSpeed);
				break;
			case WHEEL_DIAMETER:
				final double wheelDiameter = is.readDouble();
				// TODO: Print the value in the GUI
				System.out.println("Received wheel diameter: " + wheelDiameter);
				break;
			case TRACK:
				final boolean trackInside = is.readBoolean();
				// TODO: Print the value in the GUI
				System.out.println("Received track: ");
				if (trackInside){
					System.out.println("inside");
				}
				else{
					System.out.println("outside");
				}
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
				SyncExec.syncsetGyroSpeed(gyroSpeed_all);
				SyncExec.syncsetGyroIntegral(gyroIntegral_all);
				SyncExec.syncsetMotorDistance(motorDistance_all);
				SyncExec.syncsetMotorSpeed(motorSpeed_all);
				break;
			default:
				System.out.println("Unrecognized GetReturn command with " + param);
		}
	}
}
