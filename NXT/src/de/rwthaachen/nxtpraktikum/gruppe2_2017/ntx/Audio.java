package de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx;

import lejos.nxt.Sound;

/**
 * Utility methods for audio.
 * 
 * @author Robin
 */

public final class Audio
{
	/**
	 * Plays a number of beeps and counts down. Each beep is one second.
	 */
	public static void playBeeps(int number) {
		System.out.println("About to start");
		System.out.println("Put me up");
		for (int c = number; c > 0; c--) {
			System.out.print(c + " ");
			Sound.playTone(440, 100);
			try {
				Thread.sleep(1000);
			} catch (final InterruptedException e) {}
		}
		System.out.println("GO");
	}
}
