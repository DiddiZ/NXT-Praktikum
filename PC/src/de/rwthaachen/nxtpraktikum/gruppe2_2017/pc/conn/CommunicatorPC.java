package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn;
/**
 * @author Gregor & Justus & Robin
 * 
 * This class handles the communication on the PC side.
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.ByteBuffer;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.AbstractCommunicator;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui.application;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.CommandIdList.*;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.*;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTConnector;

public final class CommunicatorPC extends AbstractCommunicator
{
	private NXTConnector link = new NXTConnector();
	protected static PipedOutputStream pipedDataOut = null;
	NXTComm nxtComm;
	private boolean connected;
	public static byte nxtProtocol = -1;
	

	public CommunicatorPC() {		
		
		registerHandler(new GetReturnHandler(), 		COMMAND_GET_RETURN);
		registerHandler(new LogInfoHandler(),			COMMAND_LOG_INFO);
		registerHandler(new ErrorCodeHandler(), 		COMMAND_ERROR_CODE);
		registerHandler(new ProtocolVersionHandler(), 	COMMAND_PROTOCOL_VERSION);
		System.out.println("Registered all handlers.");
	}

	@Override
	public void connect() {
		if (!isConnected()){
			application.output("Trying to connect");
			if (link.connectTo()) {
				dataOut = new DataOutputStream(link.getOutputStream());
				dataIn = new DataInputStream(link.getInputStream());
				pipedDataOut = new PipedOutputStream();
				try {
					pipedDataIn = new PipedInputStream(pipedDataOut);
				} catch (IOException e1) {
					System.out.println("Could not create a piped input stream. Disconnecting.");
					application.output("Could not create a piped input stream. Disconnecting.");
					disconnect();
				}
				connected = true;
				System.out.println("NXT is connected");
				application.output("NXT is connected");
				
				System.out.println("Set automatic status package: on");
				try {
					pipedDataOut.write(COMMAND_SET);
					pipedDataOut.write(AUTO_STATUS_PACKAGE);
					pipedDataOut.write((byte) 1);
				} catch (IOException e) {
					System.out.println("Could not set automatic status package. Disconnecting.");
					disconnect();
				}
				
				new CommandListener(true).start();
				
			} else{
				System.out.println("No NXT found");
				application.output("No NXT found");
			}
		}
	}

	@Override
	public void disconnect() {		
		if (isConnected()) {
			try {				
				System.out.println("Closing connection");
				application.output("Closing connection");
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
	
	public void sendSet(byte param, float value) throws IOException {
		if (nxtProtocol != -1){
			
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
		} else {
			System.out.println("No protocol version received yet. Cannot send command.");
		}
	}
	
	public void sendSet(byte param, float value1, float value2) throws IOException {
		if (nxtProtocol != -1){
			
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
		} else {
			System.out.println("No protocol version received yet. Cannot send command.");
		}		
	}
	
	public void sendSet(byte param, double value) throws IOException {
		if (nxtProtocol != -1){
			
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
		} else {
			System.out.println("No protocol version received yet. Cannot send command.");
		}		
	}
	
	public void sendSet(byte param, boolean value) throws IOException {
		if (nxtProtocol != -1){
			
			if (nxtProtocol == 2) {
				System.out.println("Sending SET " + param + " " + value);
				pipedDataOut.write(COMMAND_SET);
				pipedDataOut.write(param);
				pipedDataOut.write((byte) (value?1:0));
			} else if (nxtProtocol >= 0 && nxtProtocol <= 4) {
				if (param > 9 || param < 1) {
					System.out.println("The protocol version cannot handle non standard commands.");
				} else {
					System.out.println("Sending SET " + param + " " + value);
					pipedDataOut.write(COMMAND_SET);
					pipedDataOut.write(param);
					pipedDataOut.write((byte) (value?1:0));
				}	
			} else {
				System.out.println("This protocol version is invalid.");
			}					
		} else {
			System.out.println("No protocol version received yet. Cannot send command.");
		}		
	}
	
	

	public void sendGet(byte param) throws IOException {
		if (nxtProtocol != -1){
						
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
		} else {
			System.out.println("No protocol version received yet. Cannot send command.");
		}
	}
	
	public void sendMove(float distance) throws IOException {
		if (nxtProtocol != -1){
			System.out.println("Sending MOVE " + distance);
			pipedDataOut.write(COMMAND_MOVE);
			pipedDataOut.write(ByteBuffer.allocate(4).putFloat(distance).array());
		} else {
			System.out.println("No protocol version received yet. Cannot send command.");
		}		
	}
	
	public void sendTurn(float angle) throws IOException {
		if (nxtProtocol != -1){
			System.out.println("Sending TURN " + angle);
			pipedDataOut.write(COMMAND_TURN);
			pipedDataOut.write(ByteBuffer.allocate(4).putFloat(angle).array());
		} else {
			System.out.println("No protocol version received yet. Cannot send command.");
		}	
	}
	
	public void sendBalancing(boolean enable) throws IOException {
		if (nxtProtocol != -1){
			System.out.println("Sending BALANCING " + enable);
			pipedDataOut.write(COMMAND_BALANCING);
			pipedDataOut.write((byte) (enable?1:0));
		} else {
			System.out.println("No protocol version received yet. Cannot send command.");
		}
	}
	
	public void sendDisconnect() throws IOException {
		if (nxtProtocol != -1){
			System.out.println("Sending DISCONNECT");
			pipedDataOut.write(COMMAND_DISCONNECT);
		} else {
			System.out.println("No protocol version received yet. Cannot send command.");
		}	
	}
	
	
	public static void writeBatteryVoltage(int value) {
		application.setBatteryLabel(value);
	}
	
}