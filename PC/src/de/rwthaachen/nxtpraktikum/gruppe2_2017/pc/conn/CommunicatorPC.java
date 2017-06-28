package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.CommandIdList.*;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.AUTO_STATUS_PACKET;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.ByteBuffer;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.AbstractCommunicator;
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
	protected static PipedOutputStream pipedDataOut = null;
	private boolean connected;
	public byte nxtProtocol = 0;

	public CommunicatorPC(UserInterface ui) {
		this.ui = ui;
		registerHandler(new GetReturnHandler(ui), COMMAND_GET_RETURN);
		registerHandler(new LogInfoHandler(), COMMAND_LOG_INFO);
		registerHandler(new ErrorCodeHandler(), COMMAND_ERROR_CODE);
		registerHandler(new ProtocolVersionHandler(this), COMMAND_PROTOCOL_VERSION);
		System.out.println("Registered all handlers.");
	}

	@Override
	public void connect() {
		if (!isConnected()) {
			ui.output("Trying to connect");
			try {
				link.close();
			} catch (final IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			if (link.connectTo()) {
				dataOut = new DataOutputStream(link.getOutputStream());
				dataIn = new DataInputStream(link.getInputStream());
				pipedDataOut = new PipedOutputStream();
				try {
					pipedDataIn = new PipedInputStream(pipedDataOut);
				} catch (final IOException e1) {
					System.out.println("Could not create a piped input stream. Disconnecting.");
					ui.output("Could not create a piped input stream. Disconnecting.");
					disconnect();
				}
				connected = true;
				System.out.println("NXT is connected");
				ui.output("NXT is connected");

				System.out.println("Set automatic status package: on");
				try {
					pipedDataOut.write(COMMAND_SET);
					pipedDataOut.write(AUTO_STATUS_PACKET);
					pipedDataOut.write((byte)1);
				} catch (final IOException e) {
					System.out.println("Could not set automatic status package. Disconnecting.");
					disconnect();
				}

				new CommandListener(true).start();

			} else {
				System.out.println("No NXT found");
				ui.output("No NXT found");
			}
		}
	}

	@Override
	public void disconnect() {
		try {
			link.close();
			System.out.println("Connection closed cleanly.");
		} catch (final IOException e) {
			System.out.println("Could not close the connection cleanly.");
		}
	}

	public void disconnectInit() {
		if (isConnected()) {
			try {
				System.out.println("Closing connection");
				ui.output("Closing connection");
				sendDisconnect();
			} catch (final IOException ex) {
				logException(ex);
			}
			connected = false;
			nxtProtocol = -1;
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

	/**
	 * Sets the protocol version for the GUI.
	 *
	 * @param version Received protocol version
	 */
	public void setProtocolVersion(byte version) {
		nxtProtocol = version;
	}

	public void sendSet(byte param, float value) throws IOException {
		if (nxtProtocol == 2) {
			System.out.println("Sending SET " + param + " " + value);

			pipedDataOut.write(COMMAND_SET);
			pipedDataOut.write(param);
			pipedDataOut.write(ByteBuffer.allocate(4).putFloat(value).array());
		} else if (nxtProtocol >= 0 && nxtProtocol <= 4) {
			if (param > 9 || param < 1) {
				System.out.println("The protocol version cannot handle non standard commands.");
			} else {
				System.out.println("Sending SET " + param + " " + value);
				pipedDataOut.write(COMMAND_SET);
				pipedDataOut.write(param);
				pipedDataOut.write(ByteBuffer.allocate(4).putFloat(value).array());
			}
		} else {
			System.out.println("This protocol version is invalid.");
		}
	}

	public void sendSet(byte param, float value1, float value2) throws IOException {
		if (nxtProtocol == 2) {
			System.out.println("Sending SET " + param + " " + value1 + ", " + value2);
			pipedDataOut.write(COMMAND_SET);
			pipedDataOut.write(param);
			pipedDataOut.write(ByteBuffer.allocate(4).putFloat(value1).array());
			pipedDataOut.write(ByteBuffer.allocate(4).putFloat(value2).array());
		} else if (nxtProtocol >= 0 && nxtProtocol <= 4) {
			if (param > 9 || param < 1) {
				System.out.println("The protocol version cannot handle non standard commands.");
			} else {
				System.out.println("Sending SET " + param + " " + value1 + ", " + value2);
				pipedDataOut.write(COMMAND_SET);
				pipedDataOut.write(param);
				pipedDataOut.write(ByteBuffer.allocate(4).putFloat(value1).array());
				pipedDataOut.write(ByteBuffer.allocate(4).putFloat(value2).array());
			}
		} else {
			System.out.println("This protocol version is invalid.");
		}
	}

	public void sendSet(byte param, double value) throws IOException {
		if (nxtProtocol == 2) {
			System.out.println("Sending SET " + param + " " + value);
			pipedDataOut.write(COMMAND_SET);
			pipedDataOut.write(param);
			pipedDataOut.write(ByteBuffer.allocate(8).putDouble(value).array());
		} else if (nxtProtocol >= 0 && nxtProtocol <= 4) {
			if (param > 9 || param < 1) {
				System.out.println("The protocol version cannot handle non standard commands.");
			} else {
				System.out.println("Sending SET " + param + " " + value);
				pipedDataOut.write(COMMAND_SET);
				pipedDataOut.write(param);
				pipedDataOut.write(ByteBuffer.allocate(8).putDouble(value).array());
			}
		} else {
			System.out.println("This protocol version is invalid.");
		}
	}

	public void sendSet(byte param, boolean value) throws IOException {
		if (nxtProtocol == 2) {
			System.out.println("Sending SET " + param + " " + value);
			pipedDataOut.write(COMMAND_SET);
			pipedDataOut.write(param);
			pipedDataOut.write((byte)(value ? 1 : 0));
		} else if (nxtProtocol >= 0 && nxtProtocol <= 4) {
			if (param > 9 || param < 1) {
				System.out.println("The protocol version cannot handle non standard commands.");
			} else {
				System.out.println("Sending SET " + param + " " + value);
				pipedDataOut.write(COMMAND_SET);
				pipedDataOut.write(param);
				pipedDataOut.write((byte)(value ? 1 : 0));
			}
		} else {
			System.out.println("This protocol version is invalid.");
		}
	}

	public void sendGet(byte param) throws IOException {
		if (nxtProtocol == 2) {
			System.out.println("Sending GET " + param);
			pipedDataOut.write(COMMAND_GET);
			pipedDataOut.write(param);
		} else if (nxtProtocol >= 0 && nxtProtocol <= 4) {
			if (param > 9 || param < 1) {
				System.out.println("The protocol version cannot handle non standard commands.");
			} else {
				System.out.println("Sending GET " + param);
				pipedDataOut.write(COMMAND_GET);
				pipedDataOut.write(param);
			}
		} else {
			System.out.println("This protocol version is invalid.");
		}
	}

	public void sendGetQuiet(byte param) throws IOException {
		if (nxtProtocol == 2) {
			// quiet, no output in console - System.out.println("Sending GET " + param);
			pipedDataOut.write(COMMAND_GET);
			pipedDataOut.write(param);
		} else if (nxtProtocol >= 0 && nxtProtocol <= 4) {
			if (param > 9 || param < 1) {
				System.out.println("The protocol version cannot handle non standard commands.");
			} else {
				System.out.println("Sending GET " + param);
				pipedDataOut.write(COMMAND_GET);
				pipedDataOut.write(param);
			}
		} else {
			System.out.println("This protocol version is invalid.");
		}
	}

	@SuppressWarnings("static-method")
	public void sendMove(float distance) throws IOException {
		System.out.println("Sending MOVE " + distance);
		pipedDataOut.write(COMMAND_MOVE);
		pipedDataOut.write(ByteBuffer.allocate(4).putFloat(distance).array());
	}

	@SuppressWarnings("static-method")
	public void sendTurn(float angle) throws IOException {
		System.out.println("Sending TURN " + angle);
		pipedDataOut.write(COMMAND_TURN);
		pipedDataOut.write(ByteBuffer.allocate(4).putFloat(angle).array());
	}

	@SuppressWarnings("static-method")
	public void sendBalancing(boolean enable) throws IOException {
		System.out.println("Sending BALANCING " + enable);
		pipedDataOut.write(COMMAND_BALANCING);
		pipedDataOut.write((byte)(enable ? 1 : 0));
	}

	@SuppressWarnings("static-method")
	public void sendDisconnect() throws IOException {
		System.out.println("Sending DISCONNECT");
		pipedDataOut.write(COMMAND_DISCONNECT);
		pipedDataOut.flush();
	}
}