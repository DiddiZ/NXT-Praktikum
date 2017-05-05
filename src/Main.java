import lejos.Segoway;
import lejos.nxt.Button;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.addon.GyroSensor;

public class Main
{
	private static final double WHEEL_DIAMETER = 5.6;

	public static void main(String[] args) {
		final Segoway segoway = new Segoway(MotorPort.A, MotorPort.B, new GyroSensor(SensorPort.S2), WHEEL_DIAMETER);
		// Play warning beep sequence to indicate balancing is about to start
		playBeeps(5);

		segoway.start();

		// Main logic loop
		while (Button.ESCAPE.isUp())
			if (Button.ENTER.isDown()) {
				while (Button.ENTER.isDown()) {
					// idle
				}
				segoway.move(50); // Move 50 cm // Hopeully
			}
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