package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn;

/**
 * This class handles the LOG_INFO messages
 * @author Justus
 * 
 */

import java.io.DataInputStream;
import java.io.IOException;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.CommandHandler;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.*;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.CommunicatorPC;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui.application;

public class ProtocolVersionHandler implements CommandHandler{
	@Override
	public void handle(DataInputStream is) throws IOException {
		final byte protocolVersion = is.readByte();
		//application.output("Connected with protocol version " + protocolVersion + ".");
		System.out.println("Connected with protocol version " + protocolVersion + ".");
		if (protocolVersion != 2) {
			System.out.println("GUI will use only standard commands.");
		} else {
			System.out.println("GUI will use extended command list.");
		}
		
		CommunicatorPC.setProtocolVersion(protocolVersion);
		
		
	}
}