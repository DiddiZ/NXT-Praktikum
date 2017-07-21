package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.NXTData;

public class CollisionWarningThread implements Runnable{

	private UI ui;
	private NXTData data;
	private Navigator navi;
	
	public CollisionWarningThread(UI gui, NXTData data, Navigator navi){
		this.ui = gui;
		this.data = data;
		this.navi = navi;
	}
	
	@Override
	public void run() {
		while(data.getBalancing()){
			ui.showBlockedWay(navi.isFree(data.getPositionX(), data.getPositionY()));
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				ui.showMessage("Something at CollisionWarningThread went wrong");
				e.printStackTrace();
			}
		}
	}
	
	
}
