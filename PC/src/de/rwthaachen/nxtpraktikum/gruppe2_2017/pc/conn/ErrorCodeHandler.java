package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn;
/**
 * This class handles the ERROR_CODE messages
 * @author Gregor & Justus
 *
 */

import java.io.DataInputStream;
import java.io.IOException;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.CommandHandler;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.*;

public class ErrorCodeHandler implements CommandHandler {

	@Override
	public void handle(DataInputStream is) throws IOException {
		System.out.println("Error code handler...");
	}

}
