package de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm.handler;

import java.io.DataInputStream;
import java.io.IOException;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.CommandHandler;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.NXT;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.nxt.motorcontrol.MotorController;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.evo.*;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.*;

public class EvoHandler implements CommandHandler
{

	@Override
	public void handle(DataInputStream is) throws IOException {
		final byte param = is.readByte();
		switch (param) {			
		case EVO_START_TEST:
			new EvoRunner(is.readDouble(),is.readDouble(),is.readDouble(),is.readDouble()).start();
			break;
		}
	}
	
	

}
