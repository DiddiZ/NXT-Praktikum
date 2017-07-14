package de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.CommandIdList.*;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.PARAM_ULTRASENSOR;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.STATUS_PACKET;
import java.io.IOException;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.AbstractCommunicator;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.NXT;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm.handler.BalancingHandler;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm.handler.DisconnectHandler;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm.handler.GetHandler;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm.handler.MoveHandler;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm.handler.SetHandler;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm.handler.TurnHandler;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.sensors.SensorData;
import lejos.nxt.comm.NXTConnection;
import lejos.util.Delay;

/**
 * This class provides an interface to connect the NXT brick with a PC through Bluetooth.
 * It offers a functionality to register callback methods. Up to 32 methods can be registered.
 *
 * @author Gregor & Robin & Justus
 */
public final class CommunicatorNXT extends AbstractCommunicator
{
	private static NXTConnection conn = null;
	private static boolean isConnecting, isDisconnecting = false;
	protected boolean autoStatusThreadActivated = false;
	final private byte protocolVersion = 2;

	public CommunicatorNXT() {
		// Register handlers
		registerHandler(new SetHandler(), COMMAND_SET);
		registerHandler(new GetHandler(), COMMAND_GET);
		registerHandler(new MoveHandler(), COMMAND_MOVE);
		registerHandler(new TurnHandler(), COMMAND_TURN);
		registerHandler(new BalancingHandler(), COMMAND_BALANCING);
		registerHandler(new DisconnectHandler(), COMMAND_DISCONNECT);
	}

	/**
	 * @return whether the NXT is trying to establish a connection
	 */
	public boolean isConnecting() {
		return isConnecting;
	}

	/**
	 * @return whether the NXT is connected to a PC.
	 */
	@Override
	public boolean isConnected() {
		return conn != null && !isDisconnecting;
	}

	/**
	 * connect tries to establish a connection with the PC.
	 */
	@Override
	public void connect() {
		isConnecting = true;
		System.out.println("Awaiting connection.");

		// create bouth, usb and bluetooth connectors
		USBConnector usbConn = new USBConnector();
		BluetoothConnector bluetoothConn = new BluetoothConnector();

		// try to establish a connection with either USB or Bluetooth device
		bluetoothConn.start();
		usbConn.start();

		// wait for a thread to establish a connection or timeout.
		while (!(usbConn.connectionEstablished() || bluetoothConn.connectionEstablished()) && (usbConn.isConnecting || bluetoothConn.isConnecting))
			Delay.msDelay(10);

		// get the connection
		if (usbConn.connectionEstablished())
			conn = usbConn.getConnection();
		else if (bluetoothConn.connectionEstablished())
			conn = bluetoothConn.getConnection();
		else
			conn = null;

		if (conn != null) {
			usbConn = null;
			bluetoothConn = null;

			dataIn = conn.openDataInputStream();
			dataOut = conn.openDataOutputStream();
			System.out.println("Ready for input.");
			isConnecting = false;
			new NXTCommandListener().start();

			// send protocol version of NXT to the PC GUI
			try {
				sendProtocolVersion();
			} catch (final IOException exc) {
				System.out.println("Could not send protocol version. Disconnecting.");
				disconnect();
			}

		} else
			System.out.println("Connection timeout.");
	}

	/**
	 * This function disconnects any connection of the NXT.
	 */
	@Override
	public void disconnect() {
		if (conn != null) {
			conn.close();
			conn = null;
			System.out.println("Disconnected");
		}
	}

	private final class NXTCommandListener extends CommandListener
	{
		private static final int AUTO_STATUS_PACKET_DELAY = 100;
		long nextTime = 0;

		public NXTCommandListener() {
			super(false);
		}

		@Override
		protected void additionalAction() throws IOException {
			// Send auto status packet
			if (nextTime < System.currentTimeMillis() && isConnected()) {
				NXT.COMMUNICATOR.sendGetReturn(STATUS_PACKET,
						(float)SensorData.positionX, (float)SensorData.positionY, (float)SensorData.motorSpeed, (float)SensorData.heading);
				NXT.COMMUNICATOR.sendGetReturnUltraSensor(SensorData.getUltrasonicSensorDistance());
				nextTime = System.currentTimeMillis() + AUTO_STATUS_PACKET_DELAY;
			}
		}
	}

	public static void staticDisconnect() {
		if (conn != null) {
			isDisconnecting = true;
			conn.close();
			conn = null;
			System.out.println("Disconnected");
			isDisconnecting = false;
		}
	}

	@Override
	protected void logException(IOException ex) {
		System.out.println("Got exception in CommunicatorNXT on read.");
		System.out.println(ex.getMessage());
	}

	public void sendProtocolVersion() throws IOException {
		dataOut.writeByte(COMMAND_PROTOCOL_VERSION);
		dataOut.writeByte(protocolVersion);
		dataOut.flush();
	}

	public void sendGetReturn(byte param, int value) throws IOException {
		dataOut.writeByte(COMMAND_GET_RETURN);
		dataOut.writeByte(param);
		dataOut.writeInt(value);
		dataOut.flush();
	}

	/**
	 * Sends a GetReturn for ultrasonic sensor distance.
	 */
	public void sendGetReturnUltraSensor(byte value) throws IOException {
		dataOut.writeByte(COMMAND_GET_RETURN);
		dataOut.writeByte(PARAM_ULTRASENSOR);
		dataOut.writeByte(value);
		dataOut.flush();
	}

	public void sendGetReturn(byte param, float value) throws IOException {
		dataOut.writeByte(COMMAND_GET_RETURN);
		dataOut.writeByte(param);
		dataOut.writeFloat(value);
		dataOut.flush();
	}

	public void sendGetReturn(byte param, float value1, float value2) throws IOException {
		dataOut.writeByte(COMMAND_GET_RETURN);
		dataOut.writeByte(param);
		dataOut.writeFloat(value1);
		dataOut.writeFloat(value2);
		dataOut.flush();
	}

	public void sendGetReturn(byte param, float value1, float value2, float value3, float value4) throws IOException {
		dataOut.writeByte(COMMAND_GET_RETURN);
		dataOut.writeByte(param);
		dataOut.writeFloat(value1);
		dataOut.writeFloat(value2);
		dataOut.writeFloat(value3);
		dataOut.writeFloat(value4);
		dataOut.flush();
	}

	public void sendGetReturn(byte param, double value) throws IOException {
		dataOut.writeByte(COMMAND_GET_RETURN);
		dataOut.writeByte(param);
		dataOut.writeDouble(value);
		dataOut.flush();
	}

	public void sendGetReturn(byte param, double value1, double value2, double value3, double value4) throws IOException {
		dataOut.writeByte(COMMAND_GET_RETURN);
		dataOut.writeByte(param);
		dataOut.writeDouble(value1);
		dataOut.writeDouble(value2);
		dataOut.writeDouble(value3);
		dataOut.writeDouble(value4);
		dataOut.flush();
	}

	public void sendGetReturn(byte param, long value) throws IOException {
		dataOut.writeByte(COMMAND_GET_RETURN);
		dataOut.writeByte(param);
		dataOut.writeLong(value);
		dataOut.flush();
	}

	public void sendGetReturn(byte param, boolean value) throws IOException {
		dataOut.writeByte(COMMAND_GET_RETURN);
		dataOut.writeByte(param);
		dataOut.writeBoolean(value);
		dataOut.flush();
	}

	public void sendLogInfo(byte length, byte[] infoMessage) throws IOException {
		dataOut.writeByte(COMMAND_LOG_INFO);
		dataOut.writeByte(length);
		dataOut.write(infoMessage);
		dataOut.flush();
	}

	public void setAutoStatusThread(boolean isOn) {
		if (isOn)
			autoStatusThreadActivated = true;
		else
			autoStatusThreadActivated = false;
	}

	public static void sendErrorCode(byte errorCode) {
		try {
			dataOut.writeByte(COMMAND_ERROR_CODE);
			dataOut.writeByte(errorCode);
			dataOut.flush();
		} catch (final IOException ex) {
			System.out.println("Failed to send error code");
		}
	}
}
