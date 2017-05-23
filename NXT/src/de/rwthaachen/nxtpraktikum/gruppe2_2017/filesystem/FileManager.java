/**
 * This class will can handle different files and save values inside the nxt.
 * 
 * @author Gregor
 */

package de.rwthaachen.nxtpraktikum.gruppe2_2017.filesystem;
import java.io.IOException;

public final class FileManager {

	final int PID_FILE = 0;
	
	private PidValuesFile pidValuesFile = new PidValuesFile("pidValues.txt");
	
	public FileManager() {
		
	}
	
	public boolean saveFile(int p_fileIdentifier) throws IOException {
		switch (p_fileIdentifier) {
		case PID_FILE:
			try {
				return pidValuesFile.save();
			} catch (IOException exc) {
				throw exc;
			}
		default:
			throw new IOException("No file with this identificator found.");
		}
	}
	
	public boolean loadFile(int p_fileIdentifier) throws IOException {
		
		switch (p_fileIdentifier) {
		case PID_FILE:
			try {
				return pidValuesFile.load();
			} catch (IOException exc) {
				throw exc;
			}
		default:
			throw new IOException("No file with this identificator found.");
		}
		
	}
	
	public boolean changeValue(int p_fileIdentifier, int p_name, Object p_value) {
		switch (p_fileIdentifier) {
		case PID_FILE:
			return pidValuesFile.changeValue(p_name, (Float) p_value);
		default:
			return false;
		}		
	}
	
	public boolean changeValueAndSaveFile(int p_fileIdentifier, int p_name, Object p_value) throws IOException {
		switch (p_fileIdentifier) {
		case PID_FILE:
			try {
				return pidValuesFile.changeValueAndSave(p_name, (Float) p_value);
			} catch (IOException exc) {
				throw exc;
			}
		default:
			throw new IOException("No file with this identificator found.");
		}		
	}
	
	public Object getValue(int p_fileIdentifier, int p_name) throws IOException {
		switch (p_fileIdentifier) {
		case PID_FILE:
			try {
				return pidValuesFile.getValue(p_name);
			} catch (IOException exc) {
				throw exc;
			}
		default:
			throw new IOException("No file with this identificator found.");
		}
	}
	
}
