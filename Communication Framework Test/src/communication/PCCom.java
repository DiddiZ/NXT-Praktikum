//author: Gregor

package communication;

import java.io.*;
import lejos.nxt.*;
import lejos.nxt.comm.*;

public class PCCom extends Thread{
	
	public static DataOutputStream dataOut; 
	public static DataInputStream dataIn;
	public static USBConnection USBLink;
	public static BTConnection BTLink;
	public static BTConnection btLink;
	public static int transmitReceived;
	private boolean isConnected = false, isNewData = false;	
	
	public PCCom() {
		connect();
		this.start();
	}
	
	
	public void run() {
		while (isConnected)
			checkCommand();
	}
	
	
	private void checkCommand() {
		try {
		       transmitReceived = dataIn.read();
		       System.out.print((char) transmitReceived);
		       isNewData = true;

		       }
		    
		    catch (IOException ioe) {
		       System.out.println("IO Exception read");
		       }
		
		if (transmitReceived == -1) {
	    	   System.out.println("Connection lost...");
	    	   isConnected = false;
		}
	}
	
	public boolean getConnected() {
		return isConnected;
	}
	
	public int getNewData() {
		if (isNewData) {
			isNewData = false;
			return transmitReceived;
		} else {
			return -1;
		}
	}
	
	public void connect() {
	    System.out.println("Listening");
	    BTLink = Bluetooth.waitForConnection();
	    isConnected = true;
	    dataOut = BTLink.openDataOutputStream();
	    dataIn = BTLink.openDataInputStream();
	    System.out.println("Waiting for input.");
	   //USBLink = USB.waitForConnection();
	   //dataOut = USBLink.openDataOutputStream();
	   //dataIn = USBLink.openDataInputStream();
	}
	
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
	
}