package de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm.handler;

import java.io.DataInputStream;
import java.io.IOException;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.CommandHandler;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.nxt.motorcontrol.MotorController;

/**
 * This class handles data marked as COMMAND_MOVE.
 * This command is sent by the PC to let the NXT drive a certain distance.
 * Implements the interface {@link CommandHandler} and defines the callback-method handle().
 *
 * @author Gregor & Justus & Robin
 */
public class MoveHandler implements CommandHandler
{
	/**
	 * This method reads a float of the input-stream
	 * and calls a method of the {@link MotorController} to let the NXT move the distance.
	 * Displays the received command and the distance on the NXT.
	 * 
	 * @param is: The DataInputStream the handler uses to receive data.
	 */
	@Override
	public void handle(DataInputStream is) throws IOException {
		final float distance = is.readFloat();
		MotorController.move(distance);
		System.out.println("Moving " + distance + "cm");
	}
}
