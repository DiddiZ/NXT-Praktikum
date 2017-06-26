package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;

import java.text.DateFormat;
import java.util.Date;
import org.eclipse.swt.widgets.Display;

/**
 * @author ??? & Robin
 */
public class SystemClock extends application implements Runnable
{
	private final DateFormat df = DateFormat.getTimeInstance();

	@Override
	public void run() {
		final Date nd = new Date();
		final long zero = System.currentTimeMillis();
		while (applicationHandler.ClockStarter) {
			nd.setTime(System.currentTimeMillis() - zero);

			application.currentTime = df.format(nd);

			Display.getDefault().syncExec(() -> application.setTimeText(currentTime));
			try {
				Thread.sleep(1000);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
