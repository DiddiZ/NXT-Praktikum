/**
 * This class provides an interface to connect the NXT brick with a PC through Bluetooth.
 * It offers a functionality to register callback methods. Up to 32 methods can be registered.
 *
 * @author Gregor
 */

package de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm;

import java.io.IOException;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.AbstractCommunicator;
import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.USB;

public final class CommunicatorNXT extends AbstractCommunicator
{
	private NXTConnection conn = null;

	private boolean connecting = false;

	public CommunicatorNXT() {
		registerHandler(new SetHandler(), 1);
	}

	/**
	 * @return whether the NXT is trying to establish a connection
	 */
	public boolean isConnecting() {
		return connecting;
	}

	/**
	 * @return whether the NXT is connected to a PC.
	 */
	@Override
	public boolean isConnected() {
		return conn != null;
	}

	/**
	 * connect tries to establish a connection with the PC.
	 */
	@Override
	public void connect() {
		connecting = true;
		System.out.println("Awaiting connection.");

		conn = USB.waitForConnection();
		// while (true) { // Alternate connection method until connection established
		// conn = USB.waitForConnection(1000, NXTConnection.PACKET);
		// if (conn != null)
		// break;
		// conn = Bluetooth.waitForConnection(1000, NXTConnection.PACKET);
		// if (conn != null)
		// break;
		// }
		dataOut = conn.openDataOutputStream();
		dataIn = conn.openDataInputStream();
		System.out.println("Ready for input.");
		connecting = false;
		new PacketListener().start();
	}

	/**
	 * This function disconnects any connection of the NXT.
	 */
	@Override
	public void disconnect() {
		if (conn != null) {
			conn.close();
			conn = null;
			System.out.println("Disconnected");
		}
	}

	/**
	 * Registers a handler to handle packets with the specified packet id.
	 * The position of the registered function must match the interface method.
	 * <P>
	 * Registering will fail if there is already a handler registered for the same packet id.
	 *
	 * @param packetId Must be positive and lesser than {@link CommunicatorNXT#NUMBER_OF_HANDLERS}.
	 * @return true if the handler was registered successfully.
	 */
	public boolean registerHandler(PacketHandler handler, int packetId) {
		if (packetId >= NUMBER_OF_HANDLERS || packetId < 0) // Invalid packetID
			return false;
		if (handlers[packetId] != null) // There is already a handler listening on the smae packet id
			return false;

		// Register handler
		handlers[packetId] = handler;
		return true;
	}

	/**
	 * Unregisters a handler.
	 *
	 * @param packetId Must be positive and lesser than {@link CommunicatorNXT#NUMBER_OF_HANDLERS}.
	 * @return Returns true if a method was unregistered, otherwise false.
	 */
	public boolean unregisterHandler(int packetId) {
		if (handlers[packetId] != null) {
			handlers[packetId] = null;
			return true;
		}
		return false;
	}

	/**
	 * Listens for, and handle incoming packets
	 */
	private class PacketListener extends Thread
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
					System.out.println("IO Exception read");
					break;
				}
			disconnect();
		}
	}
}
