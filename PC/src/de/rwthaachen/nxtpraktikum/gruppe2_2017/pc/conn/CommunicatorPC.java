package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn;
/**
 * @author Gregor & Justus
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.AbstractCommunicator;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.CommandIdList.*;
import lejos.pc.comm.NXTConnector;

public final class CommunicatorPC extends AbstractCommunicator
{
	private final NXTConnector link = new NXTConnector();
	private boolean connected;

	public CommunicatorPC() {
		registerHandler(new GetReturnHandler(), COMMAND_GET_RETURN);
	}

	@Override
	public void connect() {
		if (!isConnected())
			if (link.connectTo()) {
				dataOut = new DataOutputStream(link.getOutputStream());
				dataIn = new DataInputStream(link.getInputStream());
				connected = true;
				new CommandListener().start();
				System.out.println("NXT is connected");
			} else
				System.out.println("No NXT found");
	}

	@Override
	public void disconnect() {
		if (isConnected()) {
			try {				
				System.out.println("Closing connection");
				this.sendDisconnect();
				link.close();				
			} catch (final IOException ex) {
				logException(ex);
			}
			connected = false;
		}
	}

	@Override
	public boolean isConnected() {
		return connected;
	}

	@Override
	protected void logException(IOException ex) {
		ex.printStackTrace();
	}
	
	public void sendSet(byte param, double value) throws IOException {
		System.out.println("Sending SET " + param + " " + value);
		dataOut.writeByte(COMMAND_SET);
		dataOut.writeByte(param);
		dataOut.writeDouble(value);
		dataOut.flush();
	}

	public void sendGet(byte param) throws IOException {
		System.out.println("Sending GET " + param);
		dataOut.writeByte(COMMAND_GET);
		dataOut.writeByte(param);
		dataOut.flush();
	}
	
	public void sendDisconnect() throws IOException {
		System.out.println("Sending DISCONNECT");
		dataOut.writeByte(COMMAND_DISCONNECT);
		dataOut.flush();
	}
}
