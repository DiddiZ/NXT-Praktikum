package de.rwthaachen.nxtpraktikum.gruppe2_2017.comm;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm.CommunicatorNXT;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm.PacketHandler;

/**
 * Contains common code for both {@link CommunicatorNXT} and {@link CommunicatorPC}.
 *
 * @author DiddiZ
 */
public abstract class AbstractCommunicator
{
	protected static final int NUMBER_OF_HANDLERS = 32;

	/** Documented packet Ids. Each must be unique. */
	public static final byte PACKET_ID_SET = 1;

	protected final PacketHandler[] handlers = new PacketHandler[NUMBER_OF_HANDLERS];

	protected DataOutputStream dataOut = null;
	protected DataInputStream dataIn = null;

	/**
	 * Tries to connect this communicator. Success will be reflected by {@link #isConnected()}.
	 */
	public abstract void connect();

	/**
	 * Disconnects this communicator.
	 */
	public abstract void disconnect();

	/**
	 * @return whether this communicator is connected.
	 */
	public abstract boolean isConnected();
}
