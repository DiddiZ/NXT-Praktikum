import lejos.Segoway;
import lejos.nxt.Button;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.GyroSensor;

public class Main
{
	private static final double WHEEL_DIAMETER = 5.6;

	public static void main(String[] args) {
		final Segoway segoway = new Segoway(MotorPort.A, MotorPort.B, new GyroSensor(SensorPort.S2), WHEEL_DIAMETER);
		segoway.start();

		while (Button.ESCAPE.isUp())
			if (Button.ENTER.isDown()) {
				while (Button.ENTER.isDown()) {
					// idle
				}
				segoway.move(50); // Move 50 cm // Hopeully
			}
	}
}