/**
 * @author Gregor
 * 
 * sends the status package every 100ms to the PC while activated.
 */

package de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.STATUS_PACKAGE;

import java.io.IOException;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.NXT;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.sensors.SensorData;

public class AutoStatusThread extends Thread {
	
	protected boolean isActivated = false;
	
	public AutoStatusThread() {
		this.setDaemon(true);
	}
	
	public void activateAutoStatusThread(){
		isActivated = true;
		run();
	}
	
	public void deactivateAutoStatusThread() {
		isActivated = false;
	}
	
	@Override
	public void run() {
		while (isActivated) {
			// TODO get the position from corresponding class.
			try {
				NXT.COMMUNICATOR.sendGetReturn(STATUS_PACKAGE, 
						(float) 0, (float) 0, (float) SensorData.motorSpeed, (float) SensorData.heading);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				this.wait(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		//isActivated = false;
	}
}