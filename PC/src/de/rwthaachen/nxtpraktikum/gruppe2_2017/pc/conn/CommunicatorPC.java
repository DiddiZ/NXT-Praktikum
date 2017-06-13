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
	public static byte nxtProtocol = -1;

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
			nxtProtocol = -1;
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
	
	/**
	 * Sets the protocol version for the GUI.
	 * @param p_version
	 */
	public static void setProtocolVersion(byte p_version) {
		CommunicatorPC.nxtProtocol = p_version;
	}
	
	public void sendSet(byte param, double value) throws IOException {
		if (nxtProtocol != -1){
			
			if (nxtProtocol == 2) {
				System.out.println("Sending SET " + param + " " + value);
				dataOut.writeByte(COMMAND_SET);
				dataOut.writeByte(param);
				dataOut.writeDouble(value);
				dataOut.flush();
			} else if (nxtProtocol >= 0 && nxtProtocol <= 4) {
				if (param > 9 || param < 1) {
					System.out.println("The protocol version cannot handle non standard commands.");
				} else {
					System.out.println("Sending SET " + param + " " + value);
					dataOut.writeByte(COMMAND_SET);
					dataOut.writeByte(param);
					dataOut.writeDouble(value);
					dataOut.flush();
				}	
			} else {
				System.out.println("This protocol version is invalid.");
			}		
			
		}
		
	}

	public void sendGet(byte param) throws IOException {
		if (nxtProtocol != -1){
						
			if (nxtProtocol == 2) {
				System.out.println("Sending GET " + param);
				dataOut.writeByte(COMMAND_GET);
				dataOut.writeByte(param);
				dataOut.flush();
			} else if (nxtProtocol >= 0 && nxtProtocol <= 4) {
				if (param > 9 || param < 1) {
					System.out.println("The protocol version cannot handle non standard commands.");
				} else {
					System.out.println("Sending GET " + param);
					dataOut.writeByte(COMMAND_GET);
					dataOut.writeByte(param);
					dataOut.flush();
				}	
			} else {
				System.out.println("This protocol version is invalid.");
			}	
		}
	}
	
	public void sendMove(float distance) throws IOException {
		if (nxtProtocol != -1){
			System.out.println("Sending MOVE " + distance);
			dataOut.writeByte(COMMAND_MOVE);
			dataOut.writeFloat(distance);
			dataOut.flush();
		}		
	}
	
	public void sendTurn(float angle) throws IOException {
		if (nxtProtocol != -1){
			System.out.println("Sending TURN " + angle);
			dataOut.writeByte(COMMAND_TURN);
			dataOut.writeFloat(angle);
			dataOut.flush();
		}	
	}
	
	public void sendBalancing(boolean enable) throws IOException {
		if (nxtProtocol != -1){
			System.out.println("Sending BALANCING " + enable);
			dataOut.writeByte(COMMAND_BALANCING);
			dataOut.writeBoolean(enable);
			dataOut.flush();
		}
	}
	
	public void sendDisconnect() throws IOException {
		if (nxtProtocol != -1){
			System.out.println("Sending DISCONNECT");
			dataOut.writeByte(COMMAND_DISCONNECT);
			dataOut.flush();
		}	
	}
	
}