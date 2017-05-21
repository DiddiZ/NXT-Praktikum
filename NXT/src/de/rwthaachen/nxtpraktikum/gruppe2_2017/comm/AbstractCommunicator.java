package de.rwthaachen.nxtpraktikum.gruppe2_2017.comm;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm.CommunicatorNXT;

/**
 * Contains common code for both {@link CommunicatorNXT} and {@link CommunicatorPC}.
 *
 * @author DiddiZ
 */
public abstract class AbstractCommunicator
{
	protected static final int NUMBER_OF_HANDLERS = 32;

	/** Documented command Ids. Each must be unique. */
	public static final byte COMMAND_SET = 1;

	protected final CommandHandler[] handlers = new CommandHandler[NUMBER_OF_HANDLERS];

	protected DataOutputStream dataOut = null;
	protected DataInputStream dataIn = null;

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
		public CommandListener() {
			setPriority(Thread.NORM_PRIORITY);
			setDaemon(true);
		}

		@Override
		public void run() {
			while (isConnected())
				try {
					final int commandId = dataIn.read();

					if (commandId == -1) {
						System.out.println("Connection lost...");
						break;
					}

					if (commandId == -2) {
						System.out.println("Command lost...");
						break;
					}

					if (commandId >= NUMBER_OF_HANDLERS || handlers[commandId] == null) {
						System.out.println("Unhandled command with id " + commandId);
						continue;
					}

					// Handle command
					handlers[commandId].handle(dataIn);
				} catch (final IOException ex) {
					logException(ex);
					break;
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
	public boolean registerHandler(CommandHandler handler, int commandId) {
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
	public boolean unregisterHandler(int commandId) {
		if (handlers[commandId] != null) {
			handlers[commandId] = null;
			return true;
		}
		return false;
	}
}
