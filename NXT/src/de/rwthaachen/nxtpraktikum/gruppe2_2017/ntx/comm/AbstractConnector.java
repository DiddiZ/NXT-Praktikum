package de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm;

import lejos.nxt.comm.NXTConnection;

/**
 * This is an abstract class to establish a connection between a nxt and a pc.
 *
 * @author Gregor & Justus
 */
abstract class AbstractConnector extends Thread
{
	public AbstractConnector() {
		setDaemon(true);
	}

	protected boolean isConnecting = true;
	protected NXTConnection connection = null;

	public final boolean connectionEstablished() {
		return connection != null;
	}

	/**
	 * returns an established connection, otherwise null.
	 */
	public final NXTConnection getConnection() {
		return connection;
	}
}
