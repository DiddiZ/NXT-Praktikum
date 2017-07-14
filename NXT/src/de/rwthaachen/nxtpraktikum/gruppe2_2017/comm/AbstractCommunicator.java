package de.rwthaachen.nxtpraktikum.gruppe2_2017.comm;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.STATUS_PACKET;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.NXT;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm.CommunicatorNXT;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.sensors.SensorData;

/**
 * Contains common code for both {@link CommunicatorNXT} and {@link CommunicatorPC}.
 *
 * @author DiddiZ
 */
public abstract class AbstractCommunicator
{
	protected static final int NUMBER_OF_HANDLERS = 32;

	protected static final CommandHandler[] handlers = new CommandHandler[NUMBER_OF_HANDLERS];

	protected static DataOutputStream dataOut = null;
	protected static DataInputStream dataIn = null;
	protected static InputStream pipedDataIn = null;

	/**
	 * Tries to connect this communicator. Success will be reflected by {@link #isConnected()}.
	 */
	public abstract void connect();

	/**
	 * Disconnects this communicator.
	 */
	public abstract void disconnect();

	/**
	 * @return whether this communicator is connected.
	 */
	public abstract boolean isConnected();

	/**
	 * Logs an exception during command processing in a platform dependent fashion.
	 */
	protected abstract void logException(IOException ex);

	/**
	 * Listens for, and handles incoming commands
	 */
	protected final class CommandListener extends Thread
	{
		private boolean isPCListener = false;

		public CommandListener(boolean p_isPCListener) {
			setPriority(5);
			setDaemon(!p_isPCListener);

			isPCListener = p_isPCListener;
		}

		@Override
		public void run() {
			long nextTime = 0;
			while (isConnected()) {
				try {
					if (dataIn.available() > 0 || isPCListener) { // Avoid blocking so sending commands is still possible

						final byte commandId = dataIn.readByte();

						if (commandId == -1) {
							System.out.println("Connection lost...");
							break;
						}

						if (commandId == -2) {
							System.out.println("Command lost...");
							break;
						}

						if (commandId >= NUMBER_OF_HANDLERS || handlers[commandId] == null) {
							System.out.print("No handler" + commandId);
							continue;
						}

						// Handle command
						handlers[commandId].handle(dataIn);

						if (isPCListener) {
							int availableDataLen = 0;
							while ((availableDataLen = pipedDataIn.available()) > 0) {
								final byte[] data = new byte[availableDataLen];
								pipedDataIn.read(data, 0, availableDataLen);
								dataOut.write(data);
								dataOut.flush();
							}
						}
					}
				} catch (final IOException ex) {
					logException(ex);
					break;
				}

				if (nextTime < System.currentTimeMillis() && !isPCListener && isConnected()) {
					try {
						NXT.COMMUNICATOR.sendGetReturn(STATUS_PACKET,
								(float)SensorData.positionX, (float)SensorData.positionY, (float)SensorData.motorSpeed, (float)SensorData.heading);
						NXT.COMMUNICATOR.sendGetReturnUltraSensor(SensorData.getUltrasonicSensorDistance());
					} catch (final IOException e) {
						System.out.println("Could not sent AutoStatusPacket");
					}
					nextTime = System.currentTimeMillis() + 100;
				}
			}
			disconnect();
		}
	}

	/**
	 * Registers a handler to handle commands with the specified command id.
	 * The position of the registered function must match the interface method.
	 * <P>
	 * Registering will fail if there is already a handler registered for the same command id.
	 *
	 * @param commandId Must be positive and lesser than {@link #NUMBER_OF_HANDLERS}.
	 * @return true if handler was registered successfully.
	 */
	public static boolean registerHandler(CommandHandler handler, byte commandId) {
		if (commandId >= NUMBER_OF_HANDLERS || commandId < 0) // Invalid commandID
			return false;
		if (handlers[commandId] != null) // There is already a handler listening on the same command id
			return false;

		// Register handler
		handlers[commandId] = handler;
		return true;
	}

	/**
	 * Unregisters a handler.
	 *
	 * @param commandId Must be positive and lesser than {@link #NUMBER_OF_HANDLERS}.
	 * @return whether the handler was unregistered successfully.
	 */
	public static boolean unregisterHandler(byte commandId) {
		if (handlers[commandId] != null) {
			handlers[commandId] = null;
			return true;
		}
		return false;
	}
}
