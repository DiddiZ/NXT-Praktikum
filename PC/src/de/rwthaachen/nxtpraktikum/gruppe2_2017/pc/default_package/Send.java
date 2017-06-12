package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.default_package;

public class Send 
{
	//Set
	public static void sendSetInt(byte paramID, int paramValue)
	{
		byte commandID=1;
		application.output("Set send " + paramID + ": " + paramValue);
	}
	
	public static void sendSetFloat(byte paramID, float paramValue)
	{
		byte commandID=1;
		application.output("Set send " + paramID + ": " + paramValue);
	}
	
	public static void sendSetLong(byte paramID, long paramValue)
	{
		byte commandID=1;
		application.output("Set send " + paramID + ": " + paramValue);
	}
	
	public static void sendSetBoolean(byte paramID, boolean paramValue)
	{
		byte commandID=1;
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
