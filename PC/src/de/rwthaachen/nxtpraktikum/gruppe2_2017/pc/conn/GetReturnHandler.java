package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn;

import java.io.DataInputStream;
import java.io.IOException;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.CommandHandler;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm.GetHandler;

public final class GetReturnHandler implements CommandHandler
{
	@Override
	public void handle(DataInputStream is) throws IOException {
		final short param = is.readShort();
		switch (param) {
			case GetHandler.PARAM_BATTERY_VOLTAGE:
				final double voltage = is.readDouble();
				System.out.println("Voltage: " + voltage + "V");
				break;
			default:
				System.out.println("Unrecognized GetReturn command with " + param);
		}
	}
}
