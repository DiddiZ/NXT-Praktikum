package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn;

import java.io.DataInputStream;
import java.io.IOException;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.CommandHandler;

/**
 * This class handles the ERROR_CODE messages
 *
 * @author Gregor & Justus
 */
public class LogInfoHandler implements CommandHandler
{
	@Override
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
