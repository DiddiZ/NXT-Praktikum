package de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm;

import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;

/**
 * This class extends the abstract connector and establishes the connection via bluetooth
 *
 * @author Gregor & Justus
 */
public final class BluetoothConnector extends AbstractConnector
{

	public BluetoothConnector() {
		setDaemon(true);
	}

	@Override
	public NXTConnection getConnection() {
		if (connectionEstablished)
			return connection;
		return null;
	}

	@Override
	/**
	 * Performs the connection. after 20 seconds it breaks the attempt and sets connection to null.
	 * This method is blocking.
	 */
	public void run() {
		isConnecting = true;
		connectionEstablished = false;
		if (connection != null)
			connection.close();
		connection = Bluetooth.waitForConnection(20000, NXTConnection.PACKET);
		// connection = Bluetooth.waitForConnection();
		isConnecting = false;
		if (connection != null)
			connectionEstablished = true;
	}
}
