package de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm;

import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.USB;

/**
 * This class extends the abstract connector and establishes the connection via usb
 *
 * @author Gregor & Justus & Robin
 */
final class USBConnector extends AbstractConnector
{
	/**
	 * Performs the connection. after 5 seconds it breaks the attempt and sets connection to null.
	 * This method is blocking.
	 */
	@Override
	public void run() {
		connection = USB.waitForConnection(5000, NXTConnection.PACKET);
		isConnecting = false;
	}
}
