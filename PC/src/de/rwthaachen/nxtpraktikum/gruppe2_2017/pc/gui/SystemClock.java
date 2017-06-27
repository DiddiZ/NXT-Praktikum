package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;

import java.text.DateFormat;
import java.util.Date;

/**
 * @author Christian & Robin
 */
public class SystemClock extends UI implements Runnable
{
	
	private final DateFormat df = DateFormat.getTimeInstance();

	@Override
	public void run() {
		final Date nd = new Date();
		final long zero = System.currentTimeMillis();
		while (applicationHandler.ClockStarter) {
			nd.setTime(System.currentTimeMillis() - zero);

			applicationHandler.gui.currentTime = df.format(nd);
			//Display.getDefault().syncExec(() -> applicationHandler.gui.setTimeText(currentTime));
			applicationHandler.gui.setTimeText(applicationHandler.gui.currentTime);
			try {
				Thread.sleep(1000);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
