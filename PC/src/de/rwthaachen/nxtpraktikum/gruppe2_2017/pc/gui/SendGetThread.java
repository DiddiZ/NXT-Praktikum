package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.*;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.CommunicatorPC;

/**
 * This class is designed to periodically request data of different
 * parameters of the NXT to keep the PC updated.
 * Extends the class {@link Thread} to run it in a separate thread.  
 *   
 * @author Gregor
 */
public class SendGetThread extends Thread
{
	private final UserInterface ui;
	private final CommunicatorPC comm;

	/**
	 * The constructor for a SendGetThread. Assigns the attributes.
	 * @param ui: The UI this class uses to display messages.
	 * @param comm: The communicator this class uses to send the requests.
	 */
	public SendGetThread(UserInterface ui, CommunicatorPC comm) {
		this.ui = ui;
		this.comm = comm;
	}

	@Override
	/**
	 * This method uses a counter and runs as long as the connection endures.
	 * 
	 * Initially requests every important parameter of the NXT
	 * to display the values without manual request.
	 * Proceeds by sending one request in each iteration parameters that
	 * can be changed.
	 * 
	 * This thread sleeps for 100 ms.
	 */
	public void run() {
		int i = 0;
		while (comm.isConnected()) {
			switch (i) {
				case 0:
					ui.showMessage("Automatic update of values started.");
					comm.sendGet(BATTERY_VOLTAGE, true);
					comm.sendGet(GYRO_ANGLE, true);
					comm.sendGet(TACHO_LEFT, true);
					comm.sendGet(TACHO_RIGHT, true);
					if (comm.nxtProtocol == 2) {
						comm.sendGet(PID_WEIGHT_ALL, true);
						comm.sendGet(PARAM_CONSTANT_ROTATION, true);
						comm.sendGet(PARAM_CONSTANT_SPEED, true);
						comm.sendGet(PARAM_WHEEL_DIAMETER, true);
						comm.sendGet(PARAM_TRACK, true);
					}
					break;
				case 1:
					if (comm.nxtProtocol == 2) {
						comm.sendGet(PID_WEIGHT_ALL, true);
					}
					break;
				case 2:
					comm.sendGet(BATTERY_VOLTAGE, true);
					break;
				case 3:
					comm.sendGet(GYRO_ANGLE, true);
					break;
				case 4:
					comm.sendGet(TACHO_LEFT, true);
					break;
				case 5:
					comm.sendGet(TACHO_RIGHT, true);
					break;
				case 6:
					if (comm.nxtProtocol == 2) {
						comm.sendGet(PARAM_CONSTANT_ROTATION, true);
					}
					break;
				case 7:
					if (comm.nxtProtocol == 2) {
						comm.sendGet(PARAM_CONSTANT_SPEED, true);
					}
					break;
				default:
					i = 0;
			}
			i++;
			try {
				Thread.sleep(100);
			} catch (final InterruptedException e) {
				System.out.println("SendGetThread was interrupted while sleeping.");
			}
		}
	}
}
