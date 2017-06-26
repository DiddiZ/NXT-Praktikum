package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.ERROR_CODE_FALLEN;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.ERROR_CODE_PACKAGE_LOSS;
import java.io.DataInputStream;
import java.io.IOException;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.CommandHandler;

/**
 * This class handles the ERROR_CODE messages
 *
 * @author Gregor & Justus
 */
public class ErrorCodeHandler implements CommandHandler
{
	@Override
	public void handle(DataInputStream is) throws IOException {
		// System.out.println("Error code handler...");
		final byte param = is.readByte();
		switch (param) {
			case ERROR_CODE_FALLEN:
				System.out.println("NXT has fallen.");
				// TODO handle the error (disconnect etc)
				break;
			case ERROR_CODE_PACKAGE_LOSS:
				System.out.println("Lost a packet.");
				// TODO handle the error
				break;
			default:
				System.out.println("Unrecognized Errorcode with " + param);
		}
	}
}
