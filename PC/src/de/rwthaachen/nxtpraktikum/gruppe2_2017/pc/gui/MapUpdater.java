package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;

public class MapUpdater implements Runnable{

	private UserInterface ui;
	@Override
	public void run() {
		ui.drawPosition();
	}
	
	public MapUpdater(UserInterface gui){
		ui = gui;
	}

}
