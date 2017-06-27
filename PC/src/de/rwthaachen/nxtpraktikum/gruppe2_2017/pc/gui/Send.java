package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;

import java.io.IOException;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.CommunicatorPC;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.SyncExec;

public class Send
{
	static CommunicatorPC com = new CommunicatorPC();

	// Set
	public static void sendSetFloat(byte paramID, float paramValue) {
		try {
			com.sendSet(paramID, paramValue);
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		applicationHandler.gui.output("Set send " + paramID + ": " + paramValue);
	}

	public static void sendSetDouble(byte paramID, double paramValue) {
		try {
			com.sendSet(paramID, paramValue);
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		applicationHandler.gui.output("Set send " + paramID + ": " + paramValue);
	}

	public static void sendSetFloatFloat(byte paramID, float paramValue1, float paramValue2) {
		try {
			com.sendSet(paramID, paramValue1, paramValue2);
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		applicationHandler.gui.output("Set send " + paramID + ": " + paramValue1 + ", " + paramValue2);
	}

	public static void sendSetBoolean(byte paramID, boolean paramValue) {
		try {
			com.sendSet(paramID, paramValue);
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		applicationHandler.gui.output("Set send " + paramID + ": " + paramValue);
	}

	public static void sendBalancieren(boolean status) {
		try {
			com.sendBalancing(status);
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		applicationHandler.gui.output("Set send: Balancing " + status);
	}

	// Get
	public static void sendGetByte(byte paramID) {
		SyncExec.syncoutput("Get send " + paramID);
		try {
			com.sendGet(paramID);
		} catch (final IOException e) {
			System.out.println("Could not request: " + paramID + " in Send." + e.getMessage());
		}
	}

	// Get without notifying the console
	public static void sendGetByteQuiet(byte paramID) {
		try {
			com.sendGetQuiet(paramID);
		} catch (final IOException e) {
			System.out.println("Could not request: " + paramID + " in Send." + e.getMessage());
		}
	}

	// Move
	public static void sendMove(float paramValue) {
		// applicationHandler.gui.output("Move send " + paramValue);
		try {
			com.sendMove(paramValue);
		} catch (final IOException e) {
			System.out.println("Could not request: " + paramValue + " in Send." + e.getMessage());
		}
	}

	// Turn
	public static void sendTurn(float paramValue) {
		// applicationHandler.gui.output("Turn send " + paramValue);
		try {
			com.sendTurn(paramValue);
		} catch (final IOException e) {
			applicationHandler.gui.output("Could not request: " + paramValue + " in Send.");
			e.printStackTrace();
		}
	}

	// Balancing
	public static void sendBalancing(boolean paramValue) {
		applicationHandler.gui.output("Balancing send " + paramValue);
	}

	// Disconnect
	public static void sendDisconnect() {
		com.disconnectInit();
		applicationHandler.gui.output("Disconnect requested");
	}
}
