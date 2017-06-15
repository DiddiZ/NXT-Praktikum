package de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm;
/**
 * @author Gregor & Justus
 * 
 * This class extends the abstract connector and establishes the connection via bluetooth
 */

import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.Bluetooth;

public final class BluetoothConnector extends AbstractConnector{

	public BluetoothConnector() {
		this.setDaemon(true);
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
		//connection = Bluetooth.waitForConnection();
		isConnecting = false;
		if (connection != null)
			connectionEstablished = true;
	}

}
