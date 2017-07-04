/**
 * Handler for evolution algorithm 
 * 
 * @author Gregor
 */

package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn;

import java.io.DataInputStream;
import java.io.IOException;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.EVO_RETURN_TEST;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.EVO_RETURN_TEST_STATE;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.CommandHandler;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo.EvoInterface;


public class EvoHandler implements CommandHandler{

	protected EvoInterface ei;
	
	public EvoHandler(EvoInterface ei) {
		this.ei = ei;
	}
	
	@Override
	public void handle(DataInputStream is) throws IOException {
		final byte param = is.readByte();
		switch (param) {
			case EVO_RETURN_TEST:
				final double motorPowerIntegral = is.readDouble();
				final double batteryVoltage = is.readDouble();
				ei.setDataSet(motorPowerIntegral, batteryVoltage);
				break;
			case EVO_RETURN_TEST_STATE:
				final int state = is.readInt();
				ei.setState(state);
			default:
				System.out.println("Unrecognized Evo command with " + param);
		}
		
	}

}
