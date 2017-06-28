package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;

/**
 * @author Christian & Robin
 */
public class SystemClock extends Thread
{
	private final UserInterface ui;

	public SystemClock(UserInterface ui) {
		this.ui = ui;

	}

	@Override
	public void run() {
		final long zero = System.currentTimeMillis();
		while (ApplicationHandler.ClockStarter) {
			ui.showConnectionTime(System.currentTimeMillis() - zero);
			try {
				Thread.sleep(1000);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
