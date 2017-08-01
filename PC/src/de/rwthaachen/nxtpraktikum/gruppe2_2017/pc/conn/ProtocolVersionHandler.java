package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn;

import java.io.DataInputStream;
import java.io.IOException;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.CommandHandler;

/**
 * This class handles data marked as PROTOCOL_VERSION.
 * This command is initially sent by the NXT to share the version of its communication protocol.
 * Implements the interface {@link CommandHandler} and defines the callback-method handle().
 *
 * @author Justus
 */
public class ProtocolVersionHandler implements CommandHandler
{
	private final CommunicatorPC communicator;
	
	/**
	 * The constructor for a ProtocolVersionHandler. 
	 * Assigns a CommunicatorPC to the private attribute.
	 * 
	 * @param communicator: The communicator in which the protocol version will be saved.
	 */
	public ProtocolVersionHandler(CommunicatorPC communicator) {
		this.communicator = communicator;
	}

	@Override
	/**
	 * This method reads the protocol-version of the NXT.
	 * If the version equals 2, the UI will use the extended command list,
	 * in other cases only the standard commands.
	 * A corresponding message is printed and the handler calls a method of the
	 * {@link CommunicatorPC} to set the protocol version.
	 * 
	 * @param is: The DataInputStream the handler uses to receive data.
	 */
	public void handle(DataInputStream is) throws IOException {
		byte protocolVersion = is.readByte();
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
