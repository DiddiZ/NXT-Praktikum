package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.ERROR_CODE_FALLEN;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.ERROR_CODE_PACKAGE_LOSS;
import java.io.DataInputStream;
import java.io.IOException;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.CommandHandler;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui.UserInterface;

/**
 * @author Gregor & Justus
 * 
 * This class handles incoming data marked as ERROR_CODES.
 * An ERROR_CODE is sent by an NXT in case of emergency.
 * Implements the interface CommandHandler and defines the callback-method handle().
 */

public class ErrorCodeHandler implements CommandHandler
{
	private final UserInterface ui;
	
	/**
	 * The constructor for an ErrorCodeHandler
	 * @param ui: The UI on which the handler will print its messages and will call methods to handle the error.
	 */
	
	public ErrorCodeHandler(UserInterface ui) {
		this.ui = ui;
	}

	
	@Override
	/**
	 * This method reads the incoming ERROR_CODE and switches it.
	 * Currently recognized ERROR_CODES:
	 * ERROR_CODE_FALLEN: Display the message, that the NXT has fallen, in the UI and let the UI handle the error.
	 * ERROR_CODE_PACKAGE_LOSS: Currently just prints a message that this error has been recognized
	 * Default: Prints a message, that this error is unknown
	 * 
	 * @param is: The DataInputStream the handler uses to receive the ERROR_CODE
	 */
	public void handle(DataInputStream is) throws IOException {
		final byte param = is.readByte();
		switch (param) {
			case ERROR_CODE_FALLEN:
				System.out.println("NXT has fallen.");
				ui.showMessage("NXT has fallen.");
				ui.showBalancingEnabled(false);
				break;
			case ERROR_CODE_PACKAGE_LOSS:
				System.out.println("Lost a packet.");
				// TODO handle the error
				break;
			default:
				System.out.println("Unrecognized Errorcode with " + param);
		}
	}
}
