import de.rwthaachen.nxtpraktikum.gruppe2_2017.MotorController;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.SensorData;
import lejos.nxt.Sound;

public class Balancing
{
	public static void main(String[] args) throws InterruptedException {
		SensorData.init();

		playBeeps(3);

		MotorController.run();
	}

	/**
	 * Plays a number of beeps and counts down. Each beep is one second.
	 */
	private static void playBeeps(int number) {
		System.out.println("About to start");
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