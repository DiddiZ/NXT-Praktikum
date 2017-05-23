/**
 * This class extends the abstract file to save pid values.
 * 
 * @author Gregor
 */

package de.rwthaachen.nxtpraktikum.gruppe2_2017.filesystem;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public final class PidValuesFile extends AbstractFile<Float> {
	
	//identifier for variables they should be changed later to default values that are used by the communicator
	final int GYRO_SPEED = 0;
	final int GYRO_INTEGRAL = 1;
	final int MOTOR_SPEED = 2;
	final int MOTOR_DISTANCE = 3;
	final int MOTOR_HEADING = 4;

	private float gyroSpeed;
	private float gyroIntegral;
	private float motorSpeed;
	private float motorDistance;
	private float motorHeading;
	
	/**
	 * constructor of this class
	 * @param p_filePath path to the file which contains the data
	 */
	public PidValuesFile(String p_filePath) {
		this.data = new File(p_filePath);
		isInitialised = false;
	}
	
	/**
	 * saves the stored values to the corresponding file
	 */
	@Override
	public boolean save() throws IOException{
		try {
			out = new FileOutputStream(data);
		} catch (IOException exc) {
			throw exc;
		}
		
		this.dataOut = new DataOutputStream(this.out);
		
		try {
			dataOut.writeFloat(gyroSpeed);
			dataOut.writeFloat(gyroIntegral);
			dataOut.writeFloat(motorSpeed);
			dataOut.writeFloat(motorDistance);
			dataOut.writeFloat(motorHeading);
		} catch (IOException exc) {
			throw exc;
		}
		
		isInitialised = true;
		return true;
	}

	/**
	 * loads the data from file
	 */
	@Override
	public boolean load() throws IOException{
		
		try {
			in = new FileInputStream(data);
			dataIn = new DataInputStream(in);
			
			if (in.available() > 3)
				gyroSpeed = dataIn.readFloat();
			else
				throw new IOException("Could not read gyro speed value.");
			
			if (in.available() > 3)
				gyroIntegral = dataIn.readFloat();
			else
				throw new IOException("Could not read gyro integral value.");
			
			if (in.available() > 3)
				motorSpeed = dataIn.readFloat();
			else
				throw new IOException("Could not read motor speed value.");
			
			if (in.available() > 3)
				motorDistance = dataIn.readFloat();
			else
				throw new IOException("Could not read motor distance value.");
			
			if (in.available() > 3)
				motorHeading = dataIn.readFloat();
			else
				throw new IOException("Could not read motor heading value.");
			
			in.close();
			
		} catch (IOException exc) {
			throw exc;
		}
		
		this.isInitialised = true;
		return true;
	}

	/**
	 * Changes a single value given by {@link #p_name} to {@linkplain p_value}. Does not save the changed values.
	 */
	@Override
	public boolean changeValue(int p_name, Float p_value) {
		switch (p_name) {
		case GYRO_SPEED:
			gyroSpeed = p_value.floatValue();
			break;
		case GYRO_INTEGRAL:
			gyroIntegral = p_value.floatValue();
			break;
		case MOTOR_DISTANCE:
			motorDistance = p_value.floatValue();
			break;
		case MOTOR_SPEED:
			motorSpeed = p_value.floatValue();
			break;
		case MOTOR_HEADING:
			motorHeading = p_value.floatValue();
			break;
		default:
			System.out.println("Unrecognized value in PID file class."); //debug message
			return false;
		}
		return true;
		
	}

	/**
	 * Changes a single value given by {@link #p_name} to {@linkplain p_value}. Does save the changed value directly into the file.
	 */
	@Override
	public boolean changeValueAndSave(int p_name, Float p_value)  throws IOException{
		boolean returnValue = changeValue(p_name, p_value);
		if (returnValue) {
			try {
				returnValue = save();
			} catch (IOException exc) {
				returnValue = false;
				throw exc;
			}
		}
		return returnValue;
	}

	/**
	 * returns a single initialised value. throws an IOException is no values were loaded.
	 */
	@Override
	public Float getValue(int p_name) throws IOException {
		
		if (!isInitialised) 
			throw new IOException("PID values file was not initialized yet.");
		
		switch (p_name) {
		case GYRO_SPEED:
			return gyroSpeed;
		case GYRO_INTEGRAL:
			return gyroIntegral;
		case MOTOR_DISTANCE:
			return motorDistance;
		case MOTOR_SPEED:
			return motorSpeed;
		case MOTOR_HEADING:
			return motorHeading;
		default:
			throw new IOException("No method with this identifier found");
		}
	}
	

}
