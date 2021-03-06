package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.CommandIdList.*;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.*;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.CommunicatorPC;

/**
 * @author Fabian & Robin
 */
public class ApplicationCommandParser
{
	private final UserInterface ui;
	private final CommunicatorPC comm;

	public ApplicationCommandParser(UserInterface ui, CommunicatorPC comm) {
		this.ui = ui;
		this.comm = comm;
	}

	/**
	 * This method checks if a String is convertible to byte.
	 * 
	 * @param arg argument in String.
	 * @return true if arg is convertible to byte.
	 */
	public static boolean byteConvertable(String arg) {
		try {
			Byte.parseByte(arg);
			return true;
		} catch (final Exception e) {
			return false;
		}
	}

	/**
	 * This method checks if a String is convertible to int.
	 * 
	 * @param arg argument in String.
	 * @return true if arg is convertible to int.
	 */
	public static boolean intConvertable(String arg) {
		try {
			Integer.parseInt(arg);
			return true;
		} catch (final Exception e) {
			return false;
		}
	}

	/**
	 * This method checks if a String is convertible to float.
	 * 
	 * @param arg argument in String.
	 * @return true if arg is convertible to float.
	 */
	public static boolean floatConvertable(String arg) {
		try {
			Float.parseFloat(arg);
			return true;
		} catch (final Exception e) {
			return false;
		}
	}

	/**
	 * This method checks if a String is convertible to double.
	 * 
	 * @param arg argument in String.
	 * @return true if arg is convertible to double.
	 */
	public static boolean doubleConvertable(String arg) {
		try {
			Double.parseDouble(arg);
			return true;
		} catch (final Exception e) {
			return false;
		}
	}

	/**
	 * This method checks if a String is convertible to long.
	 * 
	 * @param arg argument in String.
	 * @return true if arg is convertible to long.
	 */
	public static boolean longConvertable(String arg) {
		try {
			Long.parseLong(arg);
			return true;
		} catch (final Exception e) {
			return false;
		}
	}

	/**
	 * This method calls different methods depending on the input
	 * 
	 * @param input input in String
	 */
	public void parse(String input) {
		final String[] inputarray = input.split(" ");
		final int arraylength = inputarray.length;

		// filtering out command from array
		String inputcommand = inputarray[0];
		inputcommand = inputcommand.toLowerCase();

		// filtering out Parameters from array
		final int numberOfParams = arraylength - 1;
		final String[] paramarray = new String[numberOfParams];
		for (int i = 0; i < arraylength - 1; i++) {
			paramarray[i] = inputarray[i + 1];
		}

		// Switch case for sending commands
		final byte switchvariable = filterCommand(inputcommand);
		switch (switchvariable) {
			case COMMAND_SET:
				// set
				if (numberOfParams < 2) {
					// applicationHandler.gui.output("not enough parameters!");
				} else if (byteConvertable(paramarray[0])) {
					parseSet(paramarray, numberOfParams);
				} else {
					ui.showMessage("First Parameter is no valid Parameter ID!");
				}
				break;
			case COMMAND_GET:
				//
				if (arraylength < 2) {
					ui.showMessage("Parameter ID missing");
				} else if (byteConvertable(paramarray[0])) {
					comm.sendGet(Byte.parseByte(paramarray[0]));
				} else {
					ui.showMessage("Parameter is not correct! Should be byte");
				}
				break;
			case COMMAND_MOVE:
				// Move
				if (arraylength < 2) {
					ui.showMessage("Parameter ID missing");
				} else if (floatConvertable(paramarray[0])) {
					comm.sendMove(Float.parseFloat(paramarray[0]));
				} else {
					ui.showMessage("Parameter is not correct! Should be float");
				}
				break;
			case COMMAND_TURN:
				// Turn
				if (arraylength < 2) {
					ui.showMessage("Parameter ID missing");
				} else if (floatConvertable(paramarray[0])) {
					comm.sendTurn(Float.parseFloat(paramarray[0]));
				} else {
					ui.showMessage("Parameter is not correct! Should be float");
				}
				break;
			case COMMAND_MOVE_TO:
				// Moveto
				ui.showMessage("Navigation is available in navigation DLC releasing on the 6th of July for only $5.99!");
				// #Navigation
				break;
			case COMMAND_BALANCING:
				// TODO: balancing checkbox
				// Balancing
				if (arraylength < 2) {
					ui.showMessage("Parameter missing!");
				} else {
					if (paramarray[0].equals("true") || paramarray[0].equals("false")) {
						final boolean bvalue = paramarray[0].equals("true");
						comm.sendBalancing(bvalue);
					} else {
						ui.showMessage("Parameter is not boolean!");
					}
				}
				break;
			case COMMAND_DISCONNECT:
				// disconnect
				comm.disconnectInit();
				break;
			case COMMAND_ERROR_CODE:
			case COMMAND_LOG_INFO:
			case COMMAND_PROTOCOL_VERSION:
			case COMMAND_GET_RETURN:
				ui.showMessage("Error: those commands can only be send from NXT!");
				break;

			// case 8:
			// sendManual
			// sendManual(paramarray, numberOfParams);
			// break;
			// #NewCommand
			default:
				ui.showMessage("unknown command");
		}
	}

	@SuppressWarnings("unused")
	public void sendManual(String[] paramarray, int paramNumber) {
		ui.showMessage("This feature is implemented later.");
	}

	/**
	 * This method is called in the parse method. It handles the "set" command on its to make it more organized.
	 * 
	 * @param paramarray the array of parameters
	 * @param paramNumber the number of the parameter
	 */
	public void parseSet(String[] paramarray, int paramNumber) {
		final byte switchvariable = Byte.parseByte(paramarray[0]);
		switch (switchvariable) {
			case HEADING:
			case PID_GYRO_SPEED:
			case PID_GYRO_INTEGRAL:
			case PID_MOTOR_SPEED:
			case PID_MOTOR_DISTANCE:
			case PID_WEIGHT_5:
			case PID_WEIGHT_6:
			case PID_WEIGHT_7:
			case PID_WEIGHT_8:
			case PID_WEIGHT_9:
			case PID_WEIGHT_10:
				if (paramNumber > 2) {
					ui.showMessage("Too Many Parameters, ignoring the last ones.");
				}
				if (floatConvertable(paramarray[1])) {
					comm.sendSet(Byte.parseByte(paramarray[0]), Float.parseFloat(paramarray[1]));
				} else {
					ui.showMessage("Parameter is not correct! Should be float.");
				}

				break;
			case AUTO_STATUS_PACKET:
				if (paramNumber > 2) {
					ui.showMessage("Too Many Parameters, ignoring the last ones.");
				}

				if (paramarray[1].equals("true") || paramarray[1].equals("false")) {
					final boolean bvalue = paramarray[1].equals("true");
					comm.sendSet(Byte.parseByte(paramarray[0]), bvalue);
				} else {
					ui.showMessage("Parameter is not correct! Should be true or false.");
				}
			case POSITION:
				if (paramNumber > 3) {
					ui.showMessage("Too Many Parameters, ignoring the last ones.");
				}
				if (paramNumber < 3) {
					ui.showMessage("Position need two Parameters!");
				} else if (floatConvertable(paramarray[1]) && floatConvertable(paramarray[2])) {
					comm.sendSet(Byte.parseByte(paramarray[0]), Float.parseFloat(paramarray[1]), Float.parseFloat(paramarray[2]));
				} else {
					ui.showMessage("Parameters are not correct! Should be floats.");
				}
				break;
			case BATTERY_VOLTAGE:
			case GYRO_ANGLE:
			case TACHO_LEFT:
			case TACHO_RIGHT:
			case MOVEMENT_SPEED:
			case STATUS_PACKET:
				ui.showMessage("This Parameters cannot be changed!");
				break;
			// #NewCommand
			default:
				ui.showMessage("Parameter not (yet) occupied.");
		}
	}

	/**
	 * This method filters a command and gives the byte back, which is assigned with the command.
	 * 
	 * @param arg the command
	 * @return the byte assigned to the command
	 */
	private static byte filterCommand(String arg) {
		byte output = 0;
		if (arg.equals("set")) {
			output = COMMAND_SET;
		}
		if (arg.equals("get")) {
			output = COMMAND_GET;
		}
		if (arg.equals("move")) {
			output = COMMAND_MOVE;
		}
		if (arg.equals("turn")) {
			output = COMMAND_TURN;
		}
		if (arg.equals("moveto")) {
			output = COMMAND_MOVE_TO;
		}
		if (arg.equals("balancing")) {
			output = COMMAND_BALANCING;
		}
		if (arg.equals("disconnect")) {
			output = COMMAND_DISCONNECT;
		}
		// #NewCommand
		return output;
	}
}
