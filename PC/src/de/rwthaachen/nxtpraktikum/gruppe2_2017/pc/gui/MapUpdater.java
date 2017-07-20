package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;

public class MapUpdater implements Runnable{

	public static boolean running = false;
	private UserInterface ui;
	@Override
	public void run() {
		running = true;
		ui.drawPosition();
		running = false;
	}
	
	public MapUpdater(UserInterface gui){
		ui = gui;
	}

}
