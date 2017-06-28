package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.CommunicatorPC;

/**
 * @author Christian & Robin
 */
public class SystemClock extends Thread
{
	private final UserInterface ui;
	private final CommunicatorPC communicator;

	public SystemClock(UserInterface ui, CommunicatorPC communicator) {
		this.ui = ui;
		this.communicator = communicator;
	}

	@Override
	public void run() {
		final long zero = System.currentTimeMillis();
		while (communicator.isConnected()) {
			ui.showConnectionTime(System.currentTimeMillis() - zero);
			try {
				Thread.sleep(1000);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
