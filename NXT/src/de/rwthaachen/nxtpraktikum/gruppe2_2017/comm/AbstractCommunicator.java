package de.rwthaachen.nxtpraktikum.gruppe2_2017.comm;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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

	/**
	 * Logs an exception while packet processing in a platform dependent fashion.
	 */
	protected abstract void logException(IOException ex);

	/**
	 * Listens for, and handle incoming packets
	 */
	protected final class PacketListener extends Thread
	{
		public PacketListener() {
			setPriority(Thread.NORM_PRIORITY);
			setDaemon(true);
		}

		@Override
		public void run() {
			while (isConnected())
				try {
					final int packetId = dataIn.read();

					if (packetId == -1) {
						System.out.println("Connection lost...");
						break;
					}

					if (packetId == -2) {
						System.out.println("Packet lost...");
						break;
					}

					if (packetId >= NUMBER_OF_HANDLERS || handlers[packetId] == null) {
						System.out.println("Unhandled packet with id " + packetId);
						continue;
					}

					// Handle packet
					handlers[packetId].handle(dataIn);
				} catch (final IOException ex) {
					logException(ex);
					break;
				}
			disconnect();
		}
	}
}
