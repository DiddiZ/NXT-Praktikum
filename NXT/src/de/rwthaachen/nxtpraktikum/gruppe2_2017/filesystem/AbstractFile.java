/**
 * This is an abstract class to allow to create an easy accesible file system on the NXT.
 * It can use any generic type that is defined by java.
 */

package de.rwthaachen.nxtpraktikum.gruppe2_2017.filesystem;
import java.io.*;
import java.io.IOException;

public abstract class AbstractFile<T> {
	
	protected File data;
	protected FileOutputStream out;
	protected DataOutputStream dataOut;
	protected InputStream in;
	protected DataInputStream dataIn;
	
	protected boolean isInitialised = false;

	/**
	 * saves the values into the corresponding file.
	 * @return true, if file was successfully saved.
	 * @throws IOException if file could not be saved.
	 */
	public abstract boolean save() throws IOException;
	/**
	 * loads values from the corresponding file
	 * @return true, if file was successfully loaded.
	 * @throws IOException
	 */
	public abstract boolean load() throws IOException;
	/**
	 * Changes a single value specified by p_name to p_value. Does not save the value into the file.
	 * @param p_name Identifier to the value.
	 * @param p_value Generic object of the value type.
	 * @return true, if could assign the value to the parameter.
	 */
	public abstract boolean changeValue(int p_name, T p_value);
	/**
	 * Changes a single value specified by p_name to p_value. Does save the file.
	 * @param p_name Identifier to the value.
	 * @param p_value Generic object of the value type.
	 * @return true, if could assign the value to the parameter and save the file.
	 * @throws IOException if could not save the file.
	 */
	public abstract boolean changeValueAndSave(int p_name, T p_value) throws IOException;
	/**
	 * Returns a generic type value specified by the class. Otherwise throws an IOException.
	 * @param p_name Identifier to the wanted value.
	 * @return Generic type of the value.
	 * @throws IOException if the class was not initialised yet.
	 */
	public abstract T getValue(int p_name) throws IOException; //maybe other Exception would be better
	
}


