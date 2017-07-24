package de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm.handler;

import java.io.DataInputStream;
import java.io.IOException;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.CommandHandler;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.NXT;

/**
 * This class handles incoming data marked as COMMAND_BALANCING.
 * The balancing-command is sent by the PC to either start or stop the balancing of the NXT.
 * Implements the interface {@link CommandHandler} and defines the callback-method handle().
 *
 * @author Gregor & Justus & Robin
 */
public class BalancingHandler implements CommandHandler
{
	@Override
	/**
	 * This method reads a boolean of the DataInputStream and
	 * either starts or stops the balancing of the NXT depending on the value.
	 * The value is displayed on the NXT.
	 * 
	 * @param is: The DataInputStream the handler uses to receive data.
	 */
	public void handle(DataInputStream is) throws IOException {
		final boolean enabled = is.readBoolean();
		if (enabled)
			NXT.startBalancing();
		else
			NXT.stopBalancing();
		System.out.println("balancing=" + enabled);
	}
}
