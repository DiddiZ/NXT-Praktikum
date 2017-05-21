package de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm;

import java.io.DataInputStream;
import java.io.IOException;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.CommandHandler;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.NXT;
import lejos.nxt.Battery;

public final class GetHandler implements CommandHandler
{
	/** Documented variables **/
	public static final short PARAM_BATTERY_VOLTAGE = 2000;

	@Override
	public void handle(DataInputStream is) throws IOException {
		final short param = is.readShort();
		switch (param) {
			case PARAM_BATTERY_VOLTAGE:
				System.out.println("MAX_VOLTAGE");
				NXT.COMMUNICATOR.sendGetReturn(PARAM_BATTERY_VOLTAGE, Battery.getVoltage());
				break;
			default:
				// TODO
		}
	}
}
