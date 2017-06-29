package de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm.handler;

import java.io.DataInputStream;
import java.io.IOException;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.CommandHandler;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.NXT;

/**
 * This class handles the COMMAND_BALANCING calls.
 *
 * @author Gregor & Justus & Robin
 */
public class BalancingHandler implements CommandHandler
{
	@Override
	public void handle(DataInputStream is) throws IOException {
		final boolean enabled = is.readBoolean();
		if (enabled)
			NXT.startBalancing();
		else
			NXT.stopBalancing();
		System.out.println("balancing=" + enabled);
	}
}
