package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.CommunicatorPC;

/**
 * This class is designed to run a timer as long as the connection endures.
 * Extends the {@link Thread} class to run it in a separate Thread.
 * 
 * @author Christian & Robin
 */
public class SystemClock extends Thread
{
	private final UserInterface ui;
	private final CommunicatorPC communicator;

	/**
	 * The constructor for a SystemClock. Assigns necessary attributes.
	 * 
	 * @param ui: The UI which is used to display the connection-time
	 * @param communicator: The Communicator which is used to determine whether an NXT is connected
	 */
	public SystemClock(UserInterface ui, CommunicatorPC communicator) {
		this.ui = ui;
		this.communicator = communicator;
	}

	@Override
	/**
	 * This method creates a local variable to save the time the connection was created.
	 * As long as the connection endures (which is determined by calling a method of the {@link CommunicatorPC}),
	 * a method of the {@link UserInterface} is called to display
	 * the difference between the current system time and the local variable.
	 * <p>
	 * The Thread sleeps for 1000 ms.
	 */
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
