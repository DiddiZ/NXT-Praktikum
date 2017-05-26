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
import lejos.nxt.comm.Bluetooth;

public final class CommunicatorNXT extends AbstractCommunicator
{
	private NXTConnection conn = null;

	private boolean connecting = false;

	public CommunicatorNXT() {
		registerHandler(new SetHandler(), COMMAND_SET);
		registerHandler(new GetHandler(), COMMAND_GET);
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

		// conn = USB.waitForConnection();
		conn = Bluetooth.waitForConnection();
		dataOut = conn.openDataOutputStream();
		dataIn = conn.openDataInputStream();
		System.out.println("Ready for input.");
		connecting = false;
		new CommandListener().start();
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

	@Override
	protected void logException(IOException ex) {
		System.out.println(ex.getMessage());
	}

	public void sendGetReturn(short param, double value) throws IOException {
		dataOut.writeByte(COMMAND_GET_RETURN);
		dataOut.writeShort(param);
		dataOut.writeDouble(value);
		dataOut.flush();
	}
}
