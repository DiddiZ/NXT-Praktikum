package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;

import java.io.IOException;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.CommunicatorPC;

public class Send
{
	private final UserInterface ui;
	final CommunicatorPC com;

	public Send(UserInterface ui) {
		this.ui = ui;
		com = new CommunicatorPC(ui);
	}

	// Set
	public void sendSetFloat(byte paramID, float paramValue) {
		try {
			com.sendSet(paramID, paramValue);
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ui.showMessage("Set send " + paramID + ": " + paramValue);
	}

	public synchronized void sendSetDouble(byte paramID, double paramValue) {
		com.sendSet(paramID, paramValue);
	}

	public void sendSetFloatFloat(byte paramID, float paramValue1, float paramValue2) {
		try {
			com.sendSet(paramID, paramValue1, paramValue2);
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ui.showMessage("Set send " + paramID + ": " + paramValue1 + ", " + paramValue2);
	}

	public void sendSetBoolean(byte paramID, boolean paramValue) {
		try {
			com.sendSet(paramID, paramValue);
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ui.showMessage("Set send " + paramID + ": " + paramValue);
	}

	public void sendBalancieren(boolean status) {
		try {
			com.sendBalancing(status);
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ui.showMessage("Set send: Balancing " + status);
	}

	// Get
	public void sendGetByte(byte paramID) {
		try {
			com.sendGet(paramID);
		} catch (final IOException e) {
			System.out.println("Could not request: " + paramID + " in Send." + e.getMessage());
		}
		ui.showMessage("Get send " + paramID);
	}

	// Get without notifying the console
	public void sendGetByteQuiet(byte paramID) {
		try {
			com.sendGetQuiet(paramID);
		} catch (final IOException e) {
			System.out.println("Could not request: " + paramID + " in Send." + e.getMessage());
		}
	}

	// Move
	public void sendMove(float paramValue) {
		// applicationHandler.gui.output("Move send " + paramValue);
		try {
			com.sendMove(paramValue);
		} catch (final IOException e) {
			System.out.println("Could not request: " + paramValue + " in Send." + e.getMessage());
		}
	}

	// Turn
	public void sendTurn(float paramValue) {
		// applicationHandler.gui.output("Turn send " + paramValue);
		try {
			com.sendTurn(paramValue);
		} catch (final IOException e) {
			ui.showMessage("Could not request: " + paramValue + " in Send.");
			e.printStackTrace();
		}
	}

	// Balancing
	public void sendBalancing(boolean paramValue) {
		ui.showMessage("Balancing send " + paramValue);
	}

	// Disconnect
	public void sendDisconnect() {
		com.disconnectInit();
		ui.showMessage("Disconnect requested");
	}
}
