package de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm.handler;
/**
 * @author Gregor & Justus
 * 
 * This class handles the COMMAND_MOVE calls.
 * 
 */
import java.io.DataInputStream;
import java.io.IOException;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.CommandHandler;

public class MoveHandler implements CommandHandler {

	@Override
	public void handle(DataInputStream is) throws IOException {
		System.out.println("moveHandler");
		
	}

}
