package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.data.NXTData;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.nav.Navigator;

/**
 * This class is designed to independently detect potential collisions
 * and show a corresponding warning in the UI.
 * Implements the {@link Runnable} interface to possibly start it in a separate Thread.
 * 
 * @author Christian
 */

public class CollisionWarningThread implements Runnable
{
	private final UI ui;
	private final NXTData data;
	private final Navigator navi;

	/**
	 * The constructor for a CollisionWarningThread. Assigns attributes that this class uses.
	 * @param gui: The UI on which a warning will be shown.
	 * @param data: The NXTData that is needed for calculation.
	 * @param navi: The Navigator which provides methods to identify nearby obstacles
	 */
	public CollisionWarningThread(UI gui, NXTData data, Navigator navi) {
		ui = gui;
		this.data = data;
		this.navi = navi;
	}

	@Override
	/**
	 * This method runs as long as the NXT is balancing (the value is taken of the {@link NXTData}).
	 * It periodically calls a method of the {@link Navigator} to determine whether
	 * the NXT is currently blocked or not.
	 * Calls a method of the {@link UI} with this information to show a warning sign
	 * if needed.
	 * This method sleeps 100 ms.
	 */
	public void run() {
		while (data.getBalancing()) {
			ui.showBlockedWay(navi.isBlocked());
			try {
				Thread.sleep(100);
			} catch (final InterruptedException e) {
				ui.showMessage("Something at CollisionWarningThread went wrong");
				e.printStackTrace();
			}
		}
	}
}
