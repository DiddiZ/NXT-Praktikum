/**
 * AutoStatusThread sends the status package every 5000ms to the PC while activated.
 *
 * @author Gregor
 */

package de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.STATUS_PACKET;
import java.io.IOException;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.NXT;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.sensors.SensorData;

public final class AutoStatusThread extends Thread
{
	private boolean isActivated = true;

	public AutoStatusThread() {
		setDaemon(true);
		setPriority(4);
	}

	public void deactivate() {
		isActivated = false;
	}

	@Override
	public void run() {
		long nextTime = 0;
		isActivated = true;
		while (isActivated) {
			if (nextTime < System.currentTimeMillis()) {
				try {
					NXT.COMMUNICATOR.sendGetReturn(STATUS_PACKET,
							(float)SensorData.positionX, (float)SensorData.positionY, (float)SensorData.motorSpeed, (float)SensorData.heading);
				} catch (final IOException e) {
					System.out.println("Could not sent AutoStatusPacket");
				}
				nextTime = System.currentTimeMillis() + 5000;
			}
			// isActivated = false;
			yield();
		}
	}
}