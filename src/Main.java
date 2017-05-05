import lejos.Segoway;
import lejos.nxt.Button;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.GyroSensor;

public class Main
{
	public static void main(String[] args) {
		final GyroSensor gyro = new GyroSensor(SensorPort.S2);
		final double wheelDiameter = 5.6d;

		Segoway segWay = new Segoway(MotorPort.A, MotorPort.B, gyro, wheelDiameter);

		while (Button.ESCAPE.isUp())
			if (Button.ENTER.isDown())
				segWay = new Segoway(MotorPort.A, MotorPort.B, gyro, wheelDiameter);

	}
}