package de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.sensors;

import lejos.nxt.I2CPort;
import lejos.nxt.I2CSensor;

/**
 * Reduced version of {@link lejos.nxt.UltrasonicSensor}.
 *
 * @author Robin
 */
final class UltrasonicSensor extends I2CSensor
{
	/* Device modes */
	private static final byte MODE_CONTINUOUS = 0x02;
	/* Device control locations */
	private static final byte REG_MODE = 0x41, REG_DISTANCE = 0x42;
	/* Device timing */
	private static final int DELAY_DATA = 30;

	private long dataAvailableTime = 0;
	private final byte[] byteBuff = new byte[1];
	private byte distance = (byte)0xFF;

	UltrasonicSensor(I2CPort port) {
		// Set correct sensor type, default is TYPE_LOWSPEED
		super(port, DEFAULT_I2C_ADDRESS, I2CPort.LEGO_MODE, TYPE_LOWSPEED_9V);

		// Switch to continuous mode.
		byteBuff[0] = MODE_CONTINUOUS;
		if (sendData(REG_MODE, byteBuff, 1) < 0)
			throw new IllegalStateException();

		dataAvailableTime = System.currentTimeMillis() + DELAY_DATA;
	}

	/**
	 * Return distance to an object. To ensure that the data returned is valid
	 * this method may have to wait a short while for the distance data to
	 * become available.
	 *
	 * @return distance or 255 if no object in range or an error occurred
	 */
	byte getDistance() {
		if (System.currentTimeMillis() < dataAvailableTime)
			return distance;

		// Read data
		if (getData(REG_DISTANCE, byteBuff, 1) < 0)
			return (byte)0xFF;

		// Make a note of when new data should be available.
		dataAvailableTime = System.currentTimeMillis() + DELAY_DATA;

		return distance = byteBuff[0]; // Store sensor data
	}
}