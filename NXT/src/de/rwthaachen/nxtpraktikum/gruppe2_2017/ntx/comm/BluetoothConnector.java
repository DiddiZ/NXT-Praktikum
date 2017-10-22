package de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm;

import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;

/**
 * This class extends the abstract connector and establishes the connection via bluetooth
 *
 * @author Gregor & Justus & Robin
 */
final class BluetoothConnector extends AbstractConnector
{
	/**
	 * Performs the connection. after 20 seconds it breaks the attempt and sets connection to null.
	 * This method is blocking.
	 */
	@Override
	public void run() {
		connection = Bluetooth.waitForConnection(20000, NXTConnection.PACKET);
		isConnecting = false;
	}
}
