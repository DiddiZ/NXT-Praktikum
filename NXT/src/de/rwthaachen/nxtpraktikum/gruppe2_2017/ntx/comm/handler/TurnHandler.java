package de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm.handler;

import java.io.DataInputStream;
import java.io.IOException;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.CommandHandler;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.nxt.motorcontrol.MotorController;

/**
 * This class handles data marked as COMMAND_TURN.
 * This command is sent by the PC to let the NXT turn by a certain angle.
 * Implements the interface {@link CommandHandler} and defines the callback-method handle().
 *
 * @author Gregor & Justus & Robin
 */
public class TurnHandler implements CommandHandler
{
	@Override
	/**
	 * This method reads a float and
	 * calls a method of the {@link MotorController} to let the NXT turn by the angle.
	 * Displays the received command and angle on the NXT.
	 * 
	 * @param is: The DataInputStream the handler uses to receive data.
	 */
	public void handle(DataInputStream is) throws IOException {
		final float angle = is.readFloat();
		MotorController.turn(angle);
		System.out.println("Turning by " + angle + "°");
	}
}
