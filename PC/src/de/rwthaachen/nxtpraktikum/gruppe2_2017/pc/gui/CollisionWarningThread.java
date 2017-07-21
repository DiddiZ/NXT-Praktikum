package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.NXTData;

public class CollisionWarningThread implements Runnable
{
	private final UI ui;
	private final NXTData data;
	private final Navigator navi;

	public CollisionWarningThread(UI gui, NXTData data, Navigator navi) {
		ui = gui;
		this.data = data;
		this.navi = navi;
	}

	@Override
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
