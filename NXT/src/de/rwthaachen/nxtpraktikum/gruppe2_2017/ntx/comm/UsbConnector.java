package de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm;
/**
 * @author Gregor & Justus
 * 
 * This class extends the abstract connector and establishes the connection via usb
 */

import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.USB;

public class UsbConnector extends AbstractConnector {

	public UsbConnector() {
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
	 * Performs the connection. after 5 seconds it breaks the attempt and sets connection to null.
	 * This method is blocking.
	 */
	public void run() {	
		isConnecting = true;
		connectionEstablished = false;
		if (connection != null)
			connection.close();
		connection = USB.waitForConnection(5000, NXTConnection.PACKET);
		//connection = USB.waitForConnection();
		isConnecting = false;
		if (connection != null)
			connectionEstablished = true;
	}
	
}
