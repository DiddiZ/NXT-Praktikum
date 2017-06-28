package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;

import java.text.DateFormat;
import java.util.TimeZone;

/**
 * @author Christian & Robin
 */
public class SystemClock extends Thread
{
	private final UserInterface ui;
	private final DateFormat df = DateFormat.getTimeInstance();

	public SystemClock(UserInterface ui) {
		this.ui = ui;
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	@Override
	public void run() {
		final long zero = System.currentTimeMillis();
		while (ApplicationHandler.ClockStarter) {
			ui.setTimeText(df.format(System.currentTimeMillis() - zero));
			try {
				Thread.sleep(1000);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
