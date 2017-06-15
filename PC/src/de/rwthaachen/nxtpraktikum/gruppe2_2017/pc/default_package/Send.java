package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.default_package;

import java.io.IOException;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.CommunicatorPC;

public class Send 
{
	static CommunicatorPC com = new CommunicatorPC();
	//Set
	public static void sendSetInt(byte paramID, int paramValue)
	{
		//unused
		byte commandID=1;
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
	
	//Get
	public static void sendGetByte(byte paramID)
	{
		byte commandID=2;
		application.output("Get send " + paramID);
	}
	
	
	
	//Move
	public static void sendMove(float paramValue){
		byte commandID=4;
		application.output("Move send " + paramValue);
	}
	
	//Turn
	public static void sendTurn(float paramValue){
		byte commandID=5;
		application.output("Turn send " + paramValue);
	}
	
	//Balancing
	public static void sendBalancing(boolean paramValue){
		byte commandID=7;
		application.output("Balancing send " + paramValue);
	}
	
	//Disconnect
	public static void sendDisconnect(){
		byte commandID=10;
		application.output("Disconnect requested");
	}
	
}
