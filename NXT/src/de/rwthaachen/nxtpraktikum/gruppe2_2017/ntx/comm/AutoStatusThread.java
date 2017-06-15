/**
 * @author Gregor
 * 
 * sends the status package every 5000ms to the PC while activated.
 */

package de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.STATUS_PACKAGE;

import java.io.IOException;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.NXT;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.sensors.SensorData;

public class AutoStatusThread extends Thread {
	
	protected boolean isActivated = true;
	
	public AutoStatusThread() {
		this.setDaemon(true);
		this.setPriority(4);
	}
	
	public void activate() {
		isActivated = true;
		run();
	}
	
	public void deactivate() {
		isActivated = false;
	}
	
	@Override
	public void run() {
		long nextTime = 0;
		while (isActivated) {
			// TODO get the position from corresponding class.
			if (nextTime < System.currentTimeMillis()) {
				try {
					NXT.COMMUNICATOR.sendGetReturn(STATUS_PACKAGE, 
							(float) 0, (float) 0, (float) SensorData.motorSpeed, (float) SensorData.heading);
				} catch (IOException e) {
					System.out.println("Could not sent AutoStatusPacket");
				}
				nextTime = System.currentTimeMillis() + 5000;
			}	
			//isActivated = false;
			yield();
		}
	}
}