package de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm.handler;

import java.io.DataInputStream;
import java.io.IOException;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.CommandHandler;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm.CommunicatorNXT;

/**
 * This class handles the COMMAND_DISCONNECT calls.
 *
 * @author Gregor & Justus
 */
public class DisconnectHandler implements CommandHandler
{
	@Override
	public void handle(DataInputStream is) throws IOException {
		CommunicatorNXT.staticDisconnect();
	}
}
