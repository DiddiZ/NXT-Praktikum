package de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm.handler;

import java.io.DataInputStream;
import java.io.IOException;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.CommandHandler;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm.CommunicatorNXT;

/**
 * This class handles incoming data marked as COMMAND_DISCONNECT.
 * This command is sent by the PC to initialize a disconnect.
 * Implements the interface {@link CommandHandler} and defines the callback-method handle().
 *
 * @author Gregor & Justus
 */
public class DisconnectHandler implements CommandHandler
{
	@Override
	/**
	 * This method calls a static method of the {@link CommunicatorNXT} to disconnect safely.
	 * @param is: The DataInputStream the handler could use to receive data. This handler does not use the input-stream. 
	 */
	public void handle(DataInputStream is) throws IOException {
		CommunicatorNXT.staticDisconnect();
	}
}
