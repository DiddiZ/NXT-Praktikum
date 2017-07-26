package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;

/**
 * This class independently updates the map without reducing the speed of the communication.
 * Implements the {@link Runnable} interface to possibly start it in a separate thread.
 * A static boolean prevents simultaneous starting of more than one MapUpdater
 * 
 * @author Christian
 */

public class MapUpdater implements Runnable
{
	public static boolean running = true;
	private final UserInterface ui;
	
	/**
	 * The constructor of a MapUpdater. Assigns the used UI.
	 * @param gui: The UI the class uses to draw the map.
	 */
	public MapUpdater(UserInterface gui) {
		ui = gui;
	}

	/**
	 * A simple get-method
	 * @return true if starting a new MapUpdater is allowed.
	 */
	public static synchronized boolean canRun() {
		return running;
	}

	/**
	 * A set-method. Set to false to disable starting a new MapUpdater.
	 * @param run: The boolean overwriting the current running value
	 */
	public static synchronized void setRun(boolean run) {
		running = run;
	}

	@Override
	/**
	 * This method first disables starting any other MapUpdater,
	 * then calls a method of the UI to draw the current map
	 * and finally enables starting other MapUpdaters again.
	 */
	public void run() {
		setRun(false);
		ui.drawPosition();
		setRun(true);
	}
}
