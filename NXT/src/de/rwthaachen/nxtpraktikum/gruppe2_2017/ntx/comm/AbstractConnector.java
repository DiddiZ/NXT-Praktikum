package de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm;

import lejos.nxt.comm.NXTConnection;

/**
 * This is an abstract class to establish a connection between a nxt and a pc.
 *
 * @author Gregor & Justus
 */
public abstract class AbstractConnector extends Thread
{
	public boolean isConnecting = false;
	public boolean connectionEstablished = false;
	protected NXTConnection connection = null;

	/**
	 * returns an established connection, otherwise null.
	 */
	public abstract NXTConnection getConnection();
}
