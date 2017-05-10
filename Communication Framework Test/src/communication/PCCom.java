/**
 * This class provides an interface to connect the NXT brick with a PC through Bluetooth.
 * It offers a functionality to register callback methods. Up to 32 methods can be registered.
 * 
 * @author Gregor
 * 
 */

package communication;

import java.io.*;
import lejos.nxt.comm.*;

import communication.callbackMethods;

public class PCCom extends Thread{
	
	public DataOutputStream dataOut; 
	public DataInputStream dataIn;
	public BTConnection BTLink;
	private boolean isConnected, isConnecting;	
	
	//hold up to 32 callback methods
	private callbackMethods[] callbackArray = new callbackMethods[32];
	
	/**
	 * constructor of the PCCom(munication) class.
	 */
	public PCCom() {		
		isConnected = isConnecting = false;
	}
	
	/**
	 * This is the main function of the thread. It is polling for transmitted data, while the NXT is connected to a PC.
	 */
	public void run() {
		while (isConnected) {
			checkCommand();
			Thread.yield();
		}
	}
	
	/**
	 * @return returns if the NXT is trying to establish a connection
	 */
	public boolean getConnecting() {
		return isConnecting;
	}
	
	/**
	 * @return returns if the NXT is connected to a PC
	 */
	public boolean getConnected() {
		return isConnected;
	}
	
	/**
	 * This function tries to establish a connection with the PC.
	 */
	public void connect() {
		isConnecting = true;
	    System.out.println("Awaiting connection.");
	    BTLink = Bluetooth.waitForConnection();
	    isConnected = true;
	    dataOut = BTLink.openDataOutputStream();
	    dataIn = BTLink.openDataInputStream();
	    System.out.println("Ready for input.");
	    isConnecting = false;
	    this.start();
	}
	
	/**
	 * This function disconnects any connection of the NXT.
	 */
	public void disconnect() {
		if (BTLink != null)
			BTLink.close();
		try {
			if (dataOut != null)
				dataOut.close();
			if (dataIn != null)
				dataIn.close();
		} catch (IOException ioExc) {
			
		}
		isConnected = false;
		System.out.println("Disconnected");
	}
	
	/**
	 * This function tries to send data to an open connection.
	 * @param p_data data that should be transmitted
	 * @return returns if the data transfer was successful
	 */
	public boolean sendInteger(int p_data) {
		if (isConnected && dataOut != null) {
			try {
				dataOut.writeInt(p_data);
			} catch (IOException ioExc) {
				System.out.println("Sending data failed.");
				return false;
			}
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * This function registers a callback function to a position via the callbackMethods interface.
	 * The position of the registered function must match the interface method. 
	 * @param p_interface The callbackMethods interface of the class.
	 * @param p_pos The position at which the callback method will be registered. 
	 * @return Returns true if the position was unused and the callback method could be registered, otherwise false.
	 */
	public boolean registerCallback(callbackMethods p_interface, int p_pos) {
		if (p_pos > 31 || p_pos < 0) {
			return false;
		}
		if (callbackArray[p_pos] != null) {
			return false;
		}
		callbackArray[p_pos] = p_interface;		
		return true;
	}
	
	/**
	 * This function unregisters a function in the callBack array.
	 * @param p_pos The position that should be freed.
	 * @return Returns true if a method was unregistered, otherwise false.
	 */
	public boolean unregisterCallback(int p_pos) {
		if (callbackArray[p_pos] != null) {
			callbackArray[p_pos] = null;
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * This function tries to read an incoming data and call the callback method according the the message.
	 */
	private void checkCommand() {
		int transmitReceived;
		float parameterReceived;
		try {
		       transmitReceived = dataIn.read();
		       parameterReceived = dataIn.readFloat();  
		} catch (IOException ioe) {
		       System.out.println("IO Exception read");
		       disconnect();
		       return;
		}
		
		if (transmitReceived == -1 || transmitReceived == -2) {
	    	System.out.println("Connection lost...");
	    	disconnect();
		} else {
			//call the callback method
			callBack(transmitReceived, parameterReceived);
		}
	}
	
	/**
	 * This function calls a registered callBack method according to the input parameters.
	 * @param p_pos The position of the registered callback method in the callback array.
	 * @param p_parameter The parameters that will be used to call the function.
	 */
	private void callBack(int p_pos, float p_parameter) {
		if (p_pos < 0 || p_pos > 255) {
			System.out.println("Tried to callBack with invalid position.");
			return;
		}
		if (callbackArray[p_pos] != null) {
			switch (p_pos) {
			case 0:
				callbackArray[p_pos].callback0(p_parameter);
				break;
			case 1:
				callbackArray[p_pos].callback1(p_parameter);
				break;				
			} //end switch
		} else {
			System.out.println("No callback method registered at position:");
			System.out.print(p_pos);
		} //end if
	}
	
}