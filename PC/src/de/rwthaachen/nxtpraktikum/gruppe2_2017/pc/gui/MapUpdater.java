package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;

public class MapUpdater implements Runnable
{
	/**
	 * Every time the GetReturnHandler receives an AutoStatusPaket, an instance of this thread is started to repaint the map by calling drawPosition
	 */
	
	public static boolean running = true;
	private final UserInterface ui;

	public static synchronized boolean canRun() {
		return running;
	}

	public static synchronized void setRun(boolean run) {
		running = run;
	}

	@Override
	public void run() {
		setRun(false);
		ui.drawPosition();
		setRun(true);
	}

	public MapUpdater(UserInterface gui) {
		ui = gui;
	}
}
