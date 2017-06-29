package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn;

import java.io.DataInputStream;
import java.io.IOException;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.CommandHandler;

/**
 * This class handles the LOG_INFO messages
 *
 * @author Justus
 */
public class ProtocolVersionHandler implements CommandHandler
{
	private final CommunicatorPC communicator;

	public ProtocolVersionHandler(CommunicatorPC communicator) {
		this.communicator = communicator;
	}

	@Override
	public void handle(DataInputStream is) throws IOException {
		byte protocolVersion = is.readByte();
		// application.output("Connected with protocol version " + protocolVersion + ".");
		System.out.println("Connected with protocol version " + protocolVersion + ".");
		if (protocolVersion != 2) {
			System.out.println("GUI will use only standard commands.");
			protocolVersion = 0;
		} else {
			System.out.println("GUI will use extended command list.");
		}

		communicator.setProtocolVersion(protocolVersion);
	}
}
