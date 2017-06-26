package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;

import java.io.IOException;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.CommunicatorPC;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.SyncExec;

public class Send 
{
	static CommunicatorPC com = new CommunicatorPC();
	//Set
	public static void sendSetInt(byte paramID, int paramValue)
	{
		//unused
		// byte commandID=1;
		application.output("Set send " + paramID + ": " + paramValue);
	}
	
	public static void sendSetFloat(byte paramID, float paramValue)
	{
		try {
			com.sendSet(paramID, paramValue);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//byte commandID=1;
		application.output("Set send " + paramID + ": " + paramValue);
	}
	
	public static void sendSetDouble(byte paramID, double paramValue)
	{
		try {
			com.sendSet(paramID, paramValue);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//byte commandID=1;
		application.output("Set send " + paramID + ": " + paramValue);
	}
	
	public static void sendSetFloatFloat(byte paramID, float paramValue1, float paramValue2)
	{
		try {
			com.sendSet(paramID, paramValue1, paramValue2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//byte commandID=1;
		application.output("Set send " + paramID + ": " + paramValue1 + ", " + paramValue2);
	}
	
	public static void sendSetLong(byte paramID, long paramValue)
	{
		//unused
		//byte commandID=1;
		application.output("Set send " + paramID + ": " + paramValue);
	}
	
	public static void sendSetBoolean(byte paramID, boolean paramValue)
	{
		try {
			com.sendSet(paramID, paramValue);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//byte commandID=1;
		application.output("Set send " + paramID + ": " + paramValue);
	}
	
	public static void sendBalancieren(boolean status){
		try {
			com.sendBalancing(status);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		application.output("Set send: Balancing "+status);
	}
	
	//Get
	public static void sendGetByte(byte paramID)
	{
		// byte commandID=2;
		SyncExec.syncoutput("Get send " + paramID);
		try {
			com.sendGet(paramID);
		} catch (IOException e) {
			System.out.println("Could not request: " + paramID + " in Send." + e.getMessage());
		}
	}
	
	//Get without notifying the console
	public static void sendGetByteQuiet(byte paramID)
	{
		// byte commandID=2;
		try {
			com.sendGetQuiet(paramID);
		} catch (IOException e) {
			System.out.println("Could not request: " + paramID + " in Send." + e.getMessage());
		}
	}
	
	
	
	//Move
	public static void sendMove(float paramValue){
		// byte commandID=4;
		//application.output("Move send " + paramValue);
		try {
			com.sendMove(paramValue);
		} catch (IOException e) {
			System.out.println("Could not request: " + paramValue + " in Send." + e.getMessage());
		}
	}
	
	//Turn
	public static void sendTurn(float paramValue){
		// byte commandID=5;
		//application.output("Turn send " + paramValue);
		try {
			com.sendTurn(paramValue);
		} catch (IOException e) {
			System.out.println("Could not request: " + paramValue + " in Send." + e.getMessage());
		}
	}
	
	//Balancing
	public static void sendBalancing(boolean paramValue){
		// byte commandID=7;
		application.output("Balancing send " + paramValue);
	}
	
	//Disconnect
	public static void sendDisconnect(){
		//byte commandID=10;
		com.disconnectInit();
		application.output("Disconnect requested");
	}
	
}
