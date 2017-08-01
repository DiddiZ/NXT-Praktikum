package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn;

import java.io.DataInputStream;
import java.io.IOException;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.CommandHandler;

/**
 * This class handles incoming data marked as LOG_INFO.
 * A LOG_INFO is sent by an NXT and contains information that do not fit into regular commands.
 * Implements the interface {@link CommandHandler} and defines the callback method handle.
 *
 * @author Justus
 */
public class LogInfoHandler implements CommandHandler
{
	@Override
	/**
	 * This method handles the incoming LogInfo-data.
	 * Reads the length of the incoming LogInfo first and creates an array to save the data.
	 * Currently only reads the incoming data and prints it as our NXT does not take use of this option.
	 * To not mess up the DataInputStream the data has to be read in case another NXT takes use of this.
	 */
	public void handle(DataInputStream is) throws IOException {
		final byte length = is.readByte();
		final byte[] message = new byte[length];
		for (byte b = 0; b < length; b++) {
			message[b] = is.readByte();
		}

		// TODO convert the array to a string and print the LOG_INFO
		// TODO catch errors
		System.out.println(message);
	}
}
