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
	public void connect() {
		if (!isConnected()) {
			ui.showMessage("Trying to connect");
			try {
				link.close();
			} catch (final IOException e2) {
				// TODO Auto-generated catch block
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
	public void disconnect() {
		logMessage(connected ? "Connection closed unexpectedly" : "Connection closed cleanly", false);
		connected = false; // In case connection was closed unexpectedly
		ui.showConnected(false);
		try {
			link.close();
		} catch (final IOException ex) {
			logException("Could not close the connection.", ex);
		}
	}

	public void disconnectInit() {
		if (isConnected()) {
			logMessage("Closing connection", false);
			sendDisconnect();
			connected = false;
			ui.showConnected(false);
		}
	}

	@Override
	public boolean isConnected() {
		return connected;
	}

	@Override
	protected void logException(IOException ex) {
		System.out.println("Exception in CommunicatorPC on read.");
		ex.printStackTrace();
	}

	private final class PCCommandListener extends CommandListener
	{
		public PCCommandListener() {
			super(true);
		}

		@Override
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
	 *
	 * @param version Received protocol version
	 */
	public void setProtocolVersion(byte version) {
		nxtProtocol = version;
	}

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
	 * @return
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
	 * @return
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
	 * @return true if sent successfully.
	 */
	public boolean sendGet(byte param) {
		return sendGet(param, false);
	}

	/**
	 * @param quiet if true, will not print "Sending ..."
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
	 * @return true if sent successfully.
	 */
	public boolean sendMove(float distance) {
		return sendMove(distance, false);
	}

	/**
	 * @return true if sent successfully.
	 */
	public synchronized boolean sendMove(float distance, boolean quite) {
		logMessage("Sending MOVE " + distance, quite);
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
	 * @return true if sent successfully.
	 */
	public boolean sendTurn(float distance) {
		return sendTurn(distance, false);
	}

	/**
	 * @return true if sent successfully.
	 */
	public synchronized boolean sendTurn(float angle, boolean quite) {
		logMessage("Sending TURN " + angle, quite);
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

	private void logMessage(String msg, boolean quiet) {
		if (!quiet) {
			ui.showMessage(msg);
			System.out.println(msg);
		}
	}

	private void logException(String msg) {
		ui.showMessage(msg);
		System.out.println(msg);
	}

	private void logException(String msg, Exception ex) {
		ui.showMessage(msg);
		System.out.println(msg);
		ex.printStackTrace();
	}
}