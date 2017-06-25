package de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm.handler;

import java.io.DataInputStream;
import java.io.IOException;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.CommandHandler;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.nxt.motorcontrol.MotorController;

/**
 * This class handles the COMMAND_MOVE calls.
 *
 * @author Gregor & Justus & Robin
 */
public class MoveHandler implements CommandHandler
{
	@Override
	public void handle(DataInputStream is) throws IOException {
		final float distance = is.readFloat();
		MotorController.move(distance);
		System.out.println("Moving " + distance + "cm");
	}
}
