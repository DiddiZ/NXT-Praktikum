package de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm.handler;
/**
 * @author Gregor & Justus
 * 
 * This class handles the COMMAND_BALANCING calls.
 * 
 */
import java.io.DataInputStream;
import java.io.IOException;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.CommandHandler;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.Audio;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.sensors.SensorData;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.nxt.motorcontrol.MotorController;

public class BalancingHandler implements CommandHandler{

	@Override
	public void handle(DataInputStream is) throws IOException {
		System.out.println("balancingHandler");
		final boolean value = is.readBoolean();
		if (value) {
			SensorData.init();
			Audio.playBeeps(3);
			try {
				MotorController.run();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			MotorController.isRunning = false;
		}
	}

}
