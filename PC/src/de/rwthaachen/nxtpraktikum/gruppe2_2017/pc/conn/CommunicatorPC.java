package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.CommandIdList.*;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.AUTO_STATUS_PACKET;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.AbstractCommunicator;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui.Navigator;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui.UserInterface;
import lejos.pc.comm.NXTConnector;

/**
 * This class handles the communication on the PC side.
 * Provides methods to connect and disconnect with an NXT safely,
 * send data to the NXT and safe information about the communication.
 * Extends the abstract {@link AbstractCommunicator} class and
 * therefore contains a CommandListener to read received data and handle it properly.
 *
 * @author Gregor & Justus & Robin
 */
public final class CommunicatorPC extends AbstractCommunicator
{
	private final UserInterface ui;
	private static NXTConnector link = new NXTConnector();
	private PipedInputStream pipedDataIn = null;
	private DataOutputStream pipedDataOut = null;
	private boolean connected;
	public byte nxtProtocol = 0;
	private final NXTData data;
	
	/**
	 * The constructor of a CommunicatorPC.
	 * Assigns the UI and the NXTData the communicator interacts with.
	 * Registers handlers for every command that is expected to be send by an NXT.
	 * 
	 * @param ui: The UI the communicator uses to show messages. Is needed for some handlers, too.
	 * @param data: The NXTData the communicator uses to safe and read current data of the NXT. Is needed for some handlers, too.
	 * @param navi: The Navigator the GetReturnHandler needs to interact with. 
	 */
	public CommunicatorPC(UserInterface ui, NXTData data, Navigator navi) {
		this.ui = ui;
		this.data = data;
		registerHandler(new GetReturnHandler(ui, this.data, navi), COMMAND_GET_RETURN);
		registerHandler(new LogInfoHandler(), COMMAND_LOG_INFO);
		registerHandler(new ErrorCodeHandler(ui), COMMAND_ERROR_CODE);
		registerHandler(new ProtocolVersionHandler(this), COMMAND_PROTOCOL_VERSION);
		System.out.println("Registered all handlers.");
	}

	@Override
	/**
	 * This method tries to connect the PC to an NXT in case the PC is not already connected.
	 * If an NXT is successfully linked, the method creates DataStreams for the input and output.
	 * The streams are piped and the AUTO_STATUS_PACKET will be set to true.
	 * Finally, the CommandListener is started.
	 * 
	 * If any of these steps fail, the method prints a corresponding message and disconnects.
	 */
	public void connect() {
		if (!isConnected()) {
			ui.showMessage("Trying to connect");
			try {
				link.close();
			} catch (final IOException e2) {
				System.out.println("Failed to close link.");
				e2.printStackTrace();
			}
			if (link.connectTo()) {
				dataOut = new DataOutputStream(link.getOutputStream());
				dataIn = new DataInputStream(link.getInputStream());
				final PipedOutputStream pipe = new PipedOutputStream();
				pipedDataOut = new DataOutputStream(pipe);
				try {
					pipedDataIn = new PipedInputStream(pipe);
				} catch (final IOException e1) {
					System.out.println("Could not create a piped input stream. Disconnecting.");
					ui.showMessage("Could not create a piped input stream. Disconnecting.");
					disconnect();
				}
				connected = true;
				System.out.println("NXT is connected");
				ui.showMessage("NXT is connected");

				System.out.println("Set automatic status package: on");
				try {
					pipedDataOut.write(COMMAND_SET);
					pipedDataOut.write(AUTO_STATUS_PACKET);
					pipedDataOut.write((byte)1);
				} catch (final IOException e) {
					System.out.println("Could not set automatic status package. Disconnecting.");
					disconnect();
				}

				new PCCommandListener().start();

			} else {
				System.out.println("No NXT found");
				ui.showMessage("No NXT found");
			}
		}
	}

	@Override
	/**
	 * This method disconnects from an NXT. This does not send the NXT a corresponding command
	 * to close its connection.
	 * The method prints a message whether the disconnect was expected (due to a corresponding command) or not.
	 * The connection status (saved in the boolean connected) is updated and shown in the UI.
	 * Afterwards, the method tries to close the connection.
	 */
	public void disconnect() {
		logMessage(connected ? "Connection closed unexpectedly" : "Connection closed cleanly", false);
		connected = false; // In case connection was closed unexpectedly
		ui.showConnected(connected);
		try {
			link.close();
		} catch (final IOException ex) {
			logException("Could not close the connection.", ex);
		}
	}

	/**
	 * This method initializes a disconnect.
	 * The initialization is shown in the UI and a corresponding command to close the connection is
	 * sent to the NXT. This is needed to prevent the NXT from not closing the connection safely
	 * or trying to send data through a closed connection.
	 * Updates the connection status in the boolean connected and the UI.
	 */
	public void disconnectInit() {
		if (isConnected()) {
			logMessage("Closing connection", false);
			sendDisconnect();
			connected = false;
			ui.showConnected(connected);
		}
	}

	@Override
	/**
	 * Returns the connection status. 
	 * This is needed to prevent threads from sending data through closed connections.
	 */
	public boolean isConnected() {
		return connected;
	}

	@Override
	/**
	 * Prints an exception in case the reading of the InputStream failed.
	 */
	protected void logException(IOException ex) {
		System.out.println("Exception in CommunicatorPC on read.");
		ex.printStackTrace();
	}
	
	/**
	 * The PCCommandListener extends a usual CommandListener
	 * as it synchronizes the incoming and outgoing communication.
	 */
	private final class PCCommandListener extends CommandListener
	{
		public PCCommandListener() {
			super(true);
		}

		@Override
		/**
		 * This method synchronizes the read and write process in the communication on the PC side.
		 * As long as there is data available in the InputStream, the data is read and
		 * an equivalent amount of data is written and flushed in the OutputStream.
		 */
		protected void additionalAction() throws IOException {
			// Flush piped input
			int availableDataLen = 0;
			while ((availableDataLen = pipedDataIn.available()) > 0) {
				final byte[] buffer = new byte[availableDataLen];
				pipedDataIn.read(buffer, 0, availableDataLen);
				dataOut.write(buffer);
				dataOut.flush();
			}
		}
	}

	/**
	 * Sets the protocol version for the GUI.
	 * @param version: Received protocol version
	 */
	public void setProtocolVersion(byte version) {
		nxtProtocol = version;
	}
	
	/**
	 * This method secures that a command and its parameter will only be sent
	 * if the protocol version of the NXT is valid and can handle the parameter.
	 * @param param: The parameter-ID that is to be checked
	 * @return true, if the NXT can handle the parameter concerning its protocol version.
	 */
	private boolean checkProtocolVersion(byte param) {
		if (nxtProtocol < 0 || nxtProtocol > 3) {
			logException("Protocol version '" + nxtProtocol + "' is invalid.");
			return false;
		}
		if (nxtProtocol != 2 && (param > 9 || param < 1)) {
			logException("Protocol version '" + nxtProtocol + "' cannot handle non standard commands.");
			return false;
		}
		return true;
	}

	/**
	 * This method tries to send a SET-command for a parameter and its value.
	 * Checks whether it is legal to send the command first and
	 * then tries to pipe the message to the OutputStream.
	 * 
	 * @return true if sent successfully.
	 */
	public synchronized boolean sendSet(byte param, float value) {
		logMessage("Sending SET " + param + " " + value, false);
		if (!checkProtocolVersion(param)) {
			return false;
		}

		try {
			pipedDataOut.writeByte(COMMAND_SET);
			pipedDataOut.writeByte(param);
			pipedDataOut.writeFloat(value);
			return true;
		} catch (final IOException ex) {
			logException("Failed to send SET " + param + " " + value, ex);
			return false;
		}
	}

	/**
	 * This method tries to send a SET-command for a parameter and its values.
	 * Checks whether it is legal to send the command first and
	 * then tries to pipe the message to the OutputStream.
	 * 
	 * @return true if sent successfully.
	 */
	public synchronized boolean sendSet(byte param, float value1, float value2) {
		logMessage("Sending SET " + param + " " + value1 + ", " + value2, false);
		if (!checkProtocolVersion(param)) {
			return false;
		}

		try {
			pipedDataOut.writeByte(COMMAND_SET);
			pipedDataOut.writeByte(param);
			pipedDataOut.writeFloat(value1);
			pipedDataOut.writeFloat(value2);
			return true;
		} catch (final IOException ex) {
			logException("Failed to send SET " + param + " " + value1 + ", " + value2, ex);
			return false;
		}
	}

	/**
	 * This method tries to send a SET-command for a parameter and its value.
	 * Checks whether it is legal to send the command first and
	 * then tries to pipe the message to the OutputStream.
	 * 
	 * @return true if sent successfully.
	 */
	public synchronized boolean sendSet(byte param, double value) {
		logMessage("Sending SET " + param + " " + value, false);
		if (!checkProtocolVersion(param)) {
			return false;
		}

		try {
			pipedDataOut.writeByte(COMMAND_SET);
			pipedDataOut.writeByte(param);
			pipedDataOut.writeDouble(value);
			return true;
		} catch (final IOException ex) {
			logException("Failed to send SET " + param + " " + value, ex);
			return false;
		}
	}

	/**
	 * This method tries to send a SET-command for a parameter and its value.
	 * Checks whether it is legal to send the command first and
	 * then tries to pipe the message to the OutputStream.
	 * 
	 * @return true if sent successfully.
	 */
	public synchronized boolean sendSet(byte param, double value1, double value2, double value3, double value4) {
		logMessage("Sending SET " + param + " " + value1 + ", " + value2 + ", " + value3 + ", " + value4, false);
		if (!checkProtocolVersion(param)) {
			return false;
		}

		try {
			pipedDataOut.writeByte(COMMAND_SET);
			pipedDataOut.writeByte(param);
			pipedDataOut.writeDouble(value1);
			pipedDataOut.writeDouble(value2);
			pipedDataOut.writeDouble(value3);
			pipedDataOut.writeDouble(value4);
			return true;
		} catch (final IOException ex) {
			logException("Failed to send SET " + param + " " + value1 + ", " + value2 + ", " + value3 + ", " + value4, ex);
			return false;
		}
	}

	/**
	 * This method tries to send a SET-command for a parameter and its value.
	 * Checks whether it is legal to send the command first and
	 * then tries to pipe the message to the OutputStream.
	 * 
	 * @return true if sent successfully.
	 */
	public synchronized boolean sendSet(byte param, boolean value) {
		logMessage("Sending SET " + param + " " + value, false);
		if (!checkProtocolVersion(param)) {
			return false;
		}

		try {
			pipedDataOut.writeByte(COMMAND_SET);
			pipedDataOut.writeByte(param);
			pipedDataOut.writeBoolean(value);
			return true;
		} catch (final IOException ex) {
			logException("Failed to send SET " + param + " " + value, ex);
			return false;
		}
	}

	/**
	 * This method tries to send a GET-command.
	 * Calls another method and marks the send as loud (the UI will show a "Sending" message).
	 * 
	 * @return true if sent successfully.
	 */
	public boolean sendGet(byte param) {
		return sendGet(param, false);
	}


	/**
	 * This method tries to send a GET-command for a parameter and its value.
	 * Calls a method to print a corresponding information in the UI depending on its "quiet"-value.
	 * This is needed as some threads periodically send commands and would flood the UI-output.
	 * 
	 * Checks whether it is legal to send the command first and
	 * then tries to pipe the message to the OutputStream.
	 * 
	 * @param quiet: if true, will not print "Sending ..."
	 * @return true if sent successfully.
	 */
	public synchronized boolean sendGet(byte param, boolean quiet) {
		logMessage("Sending GET " + param, quiet);
		if (!checkProtocolVersion(param)) {
			return false;
		}

		try {
			pipedDataOut.writeByte(COMMAND_GET);
			pipedDataOut.writeByte(param);
			return true;
		} catch (final IOException ex) {
			logException("Failed to send GET " + param, ex);
			return false;
		}
	}

	/**
	 * This method tries to send a MOVE-command for a certain distance.
	 * Calls another method and marks the send as loud (the UI will show a "Sending" message).
	 * 
	 * @return true if sent successfully.
	 */
	public boolean sendMove(float distance) {
		return sendMove(distance, false);
	}

	/**
	 * This method tries to send a MOVE-command for a certain distance.
	 * Calls a method to print a corresponding information in the UI depending on its "quiet"-value.
	 * This is needed as some threads periodically send commands and would flood the UI-output.
	 * 
	 * Tries to pipe the message to the OutputStream.
	 * 
	 * @param quiet: if true, will not print "Sending ..."
	 * @return true if sent successfully.
	 */
	public synchronized boolean sendMove(float distance, boolean quiet) {
		logMessage("Sending MOVE " + distance, quiet);
		try {
			pipedDataOut.writeByte(COMMAND_MOVE);
			pipedDataOut.writeFloat(distance);
			return true;
		} catch (final IOException ex) {
			logException("Failed to send MOVE " + distance, ex);
			return false;
		}
	}

	/**
	 * This method tries to send a TURN-command for a certain angle.
	 * Calls another method and marks the send as loud (the UI will show a "Sending" message).
	 * 
	 * @return true if sent successfully.
	 */
	public boolean sendTurn(float distance) {
		return sendTurn(distance, false);
	}
	
	/**
	 * This method tries to send a TURN-command for a certain angle.
	 * Calls a method to print a corresponding information in the UI depending on its "quiet"-value.
	 * This is needed as some threads periodically send commands and would flood the UI-output.
	 * 
	 * Tries to pipe the message to the OutputStream.
	 * 
	 * @param quiet: if true, will not print "Sending ..."
	 * @return true if sent successfully.
	 */
	public synchronized boolean sendTurn(float angle, boolean quiet) {
		logMessage("Sending TURN " + angle, quiet);
		try {
			pipedDataOut.writeByte(COMMAND_TURN);
			pipedDataOut.writeFloat(angle);
			return true;
		} catch (final IOException ex) {
			logException("Failed to send TURN " + angle, ex);
			return false;
		}
	}

	/**
	 * This method tries to send a BALANCING-command.
	 * Calls a method to show a corresponding message in the UI and
	 * then tries to pipe the data to the OutputStream.
	 * 
	 * @param enable: Set to true if you want the NXT to start the balancing
	 * @return true if sent successfully.
	 */
	public synchronized boolean sendBalancing(boolean enable) {
		logMessage("Sending BALANCING " + enable, false);
		try {
			pipedDataOut.writeByte(COMMAND_BALANCING);
			pipedDataOut.writeBoolean(enable);
			return true;
		} catch (final IOException ex) {
			logException("Failed to send BALANCING " + enable, ex);
			return false;
		}
	}

	/**
	 * This method tries to send a DISCONNECT-command.
	 * Calls a method to show a corresponding message in the UI and
	 * then tries to pipe the data to the OutputStream.
	 * 
	 * @return true if sent successfully.
	 */
	private synchronized boolean sendDisconnect() {
		logMessage("Sending DISCONNECT", false);
		try {
			pipedDataOut.writeByte(COMMAND_DISCONNECT);
			pipedDataOut.flush();
			return true;
		} catch (final IOException ex) {
			logException("Failed to send DISCONNECT", ex);
			return false;
		}
	}

	public NXTData getData() {
		return data;
	}

	/**
	 * A Method to eventually print a message in the UI, depending on the boolean.
	 * @param msg: The message that is to be printed
	 * @param quiet: True, if the message shall not be printed
	 */
	private void logMessage(String msg, boolean quiet) {
		if (!quiet) {
			ui.showMessage(msg);
			System.out.println(msg);
		}
	}

	/**
	 * A method to print a message in the UI.
	 * @param msg: The message to be printed
	 */
	private void logException(String msg) {
		ui.showMessage(msg);
		System.out.println(msg);
	}

	/**
	 * A method to print an exception and a corresponding message.
	 * @param msg: The message to identify the exception
	 * @param ex: The exception to be printed
	 */
	private void logException(String msg, Exception ex) {
		ui.showMessage(msg);
		System.out.println(msg);
		ex.printStackTrace();
	}
}