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
	public int transmitReceived;
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
		try {
		       transmitReceived = dataIn.read();
		       int callbackPosition = translateInputToCallbackPosition(transmitReceived);
		       int callbackParameter = translateInputToCallbackParameter(transmitReceived);
		       callBack(callbackPosition,callbackParameter);
		       System.out.print((char) transmitReceived);

		       }
		    
		    catch (IOException ioe) {
		       System.out.println("IO Exception read");
		       }
		
		if (transmitReceived == -1 || transmitReceived == -2) {
	    	   System.out.println("Connection lost...");
	    	   disconnect();
		}
	}
	
	/**
	 * This function extracts the callback method position from an integer input
	 * 0bXXXXXX__________________________ (6bit|26bit)
	 */
	private int translateInputToCallbackPosition(int p_input) {
		p_input = p_input >> 26;
		p_input &= 0b111111;
		return p_input;
	}
	
	/**
	 * This function extracts the callback parameter from an integer input
	 * 0b______XXXXXXXXXXXXXXXXXXXXXXXXXX (6bit|26bit)
	 */
	private int translateInputToCallbackParameter(int p_input) {
		p_input &= 0b00000011111111111111111111111111;
		return p_input;
	}
	
	/**
	 * This function calls a registered callBack method according to the input parameters.
	 * @param p_pos The position of the registered callback method in the callback array.
	 * @param p_parameter The parameters that will be used to call the function.
	 */
	private void callBack(int p_pos, int p_parameter) {
		if (p_pos < 0 || p_pos > 31) {
			System.out.println("Tried to callBack with invalid position.");
			return;
		}
		if (p_parameter < 0 || p_parameter > (Math.pow(2, 26)-1) ) {
			System.out.println("Tried to callBack with invalid parameter.");
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
			case 2:
				callbackArray[p_pos].callback2(p_parameter);
				break;
			case 3:
				callbackArray[p_pos].callback3(p_parameter);
				break;
			case 4:
				callbackArray[p_pos].callback4(p_parameter);
				break;
			case 5:
				callbackArray[p_pos].callback5(p_parameter);
				break;
			case 6:
				callbackArray[p_pos].callback6(p_parameter);
				break;
			case 7:
				callbackArray[p_pos].callback7(p_parameter);
				break;
			case 8:
				callbackArray[p_pos].callback8(p_parameter);
				break;
			case 9:
				callbackArray[p_pos].callback9(p_parameter);
				break;
			case 10:
				callbackArray[p_pos].callback10(p_parameter);
				break;
			case 11:
				callbackArray[p_pos].callback11(p_parameter);
				break;
			case 12:
				callbackArray[p_pos].callback12(p_parameter);
				break;
			case 13:
				callbackArray[p_pos].callback13(p_parameter);
				break;
			case 14:
				callbackArray[p_pos].callback14(p_parameter);
				break;
			case 15:
				callbackArray[p_pos].callback15(p_parameter);
				break;
			case 16:
				callbackArray[p_pos].callback16(p_parameter);
				break;
			case 17:
				callbackArray[p_pos].callback17(p_parameter);
				break;
			case 18:
				callbackArray[p_pos].callback18(p_parameter);
				break;
			case 19:
				callbackArray[p_pos].callback19(p_parameter);
				break;
			case 20:
				callbackArray[p_pos].callback20(p_parameter);
				break;
			case 21:
				callbackArray[p_pos].callback21(p_parameter);
				break;
			case 22:
				callbackArray[p_pos].callback22(p_parameter);
				break;
			case 23:
				callbackArray[p_pos].callback23(p_parameter);
				break;
			case 24:
				callbackArray[p_pos].callback24(p_parameter);
				break;
			case 25:
				callbackArray[p_pos].callback25(p_parameter);
				break;
			case 26:
				callbackArray[p_pos].callback26(p_parameter);
				break;
			case 27:
				callbackArray[p_pos].callback27(p_parameter);
				break;
			case 28:
				callbackArray[p_pos].callback28(p_parameter);
				break;
			case 29:
				callbackArray[p_pos].callback29(p_parameter);
				break;
			case 30:
				callbackArray[p_pos].callback30(p_parameter);
				break;
			case 31:
				callbackArray[p_pos].callback31(p_parameter);
				break;				
			} //end switch
		} else {
			System.out.println("No callback method registered at position:");
			System.out.print(p_pos);
			System.out.print(" parameter: ");
			System.out.print(p_parameter);
		} //end if
	}
	
}