package de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm.handler;

/**
 * This class handles the COMMAND_TURN calls.
 *
 * @author Gregor & Justus & Robin
 */
import java.io.DataInputStream;
import java.io.IOException;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.CommandHandler;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.nxt.motorcontrol.MotorController;

public class TurnHandler implements CommandHandler
{
	@Override
	public void handle(DataInputStream is) throws IOException {
		final float angle = is.readFloat();
		MotorController.turn(angle);
		System.out.println("Turning by " + angle + "°");
	}
}
