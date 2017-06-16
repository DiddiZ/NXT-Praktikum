package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.*;


public class applicationHandler {
	//Connect Area
	private static boolean ConnectButtonStatus=true; //true=Connect, false=Disconnect
	public static boolean ClockStarter=true;
	public static boolean getConnectionStatus(){
		return ConnectButtonStatus;
	}
	
	public static void connectButton(){
		//if(application.getConnectionType()!=null){
			if(ConnectButtonStatus){
				connect();
			}
			else{
				disconnect();
			}
			
		//}
	}
	
	public static void connect() {
		Send.com.connect();
		if(Send.com.isConnected()){
			ClockStarter=true;
			(new Thread(new SystemClock())).start();
			application.enableButtons();
			application.setConnectionLabel(true);
			application.setConnectionButtonText("Disconnect");
			ConnectButtonStatus=false;
			(new Thread(new SendGetThread())).start();
		}
		else{
			application.output("Unable to connect");
		}
	}
	
	public static void disconnect() {
		Send.sendDisconnect();
		application.disableButtons();
		application.setConnectionLabel(false);
		application.setConnectionButtonText("Connect");
		ConnectButtonStatus=true;
		ClockStarter=false;
	}
	
	/*
	public static void connectButton(){
		if(application.getConnectionType()!=null){
			if(ConnectionStatus){
				//Add Connect here!
				ClockStarter=true;
				application.output("Connect via "+application.getConnectionType());
				application.enableButtons();
				(new Thread(new SystemClock())).start();
				application.setConnectionLabel(true);
				application.setConnectionButtonText("Disconnect");
				//(new Thread(new SendGetThread())).run();
				ConnectionStatus=false;
				
			}
			else
			{
				Send.sendDisconnect();
				application.output("Disconnected");
				application.disableButtons();
				application.setConnectionLabel(false);
				application.setConnectionButtonText("Connect");
				ConnectionStatus=true;
				ClockStarter=false;
			}
			
		}
		else{
			application.output("Please select connection type!");
		}
		
	}
	*/
	//Parameter Area
	
	//Command Area
	public static void sendCommandButton(){
		String input = application.text_2.getText();
		application.output("input: "+input);
		//applicationCommandParser.parse(input);
	}
	//PositionTab
	public static void goForwardButton(){
		application.output("Forward");
		application.output("Is not implemented yet");
	}
	
	public static void goBackButton(){
		application.output("Back");
		application.output("Is not implemented yet");
	}
	
	public static void goLeftButton(){
		application.output("Left");
		application.output("Is not implemented yet");
	}
	
	public static void goRightButton(){
		application.output("Right");
		application.output("Is not implemented yet");
	}
	
	public static void driveDistanceButton(){
		application.output("driveDistance: "+application.drivedistancet.getText());
		application.output("Is not implemented yet");
	}
	
	public static void turnAbsoluteButton(){
		application.output("turnAbsolut: "+application.turnabsolutet.getText());
		application.output("Is not implemented yet");
	}
	
	public static void turnRelativeButton(){
		application.output("turnRelative: "+application.turnrelativet.getText());
		application.output("Is not implemented yet");
	}
	
	public static void driveToButton(){
		application.output("drive to: "+application.driveToXt.getText()+", "+application.driveToYt.getText());
		application.output("Is not implemented yet");
	}
	
	public static void sendAutostatuspacket(boolean status){
		Send.sendSetBoolean(AUTO_STATUS_PACKAGE, status);
	}
	
	public static void sendBalancieren(boolean status){
		Send.sendBalancieren(status);
	}
	//ParameterTab
	//assuming paramID for parameter ranges from 21-
	public static void sendGyroSpeedButton(){
		String arg = application.sgyrospeedt.getText();
		if(applicationCommandParser.doubleConvertable(arg)){
			double paramValue=Double.parseDouble(arg);
			Send.sendSetDouble(PID_GYRO_SPEED, paramValue);
			Send.sendGetByte(PID_GYRO_SPEED);
		}
		else
		{
			application.output("Parameter not convertable!");
		}
	}
	
	public static void sendGyroIntegralButton(){
		String arg = application.sgyrointegralt.getText();
		if(applicationCommandParser.doubleConvertable(arg)){
			double paramValue=Double.parseDouble(arg);
			Send.sendSetDouble(PID_GYRO_INTEGRAL, paramValue);
			Send.sendGetByte(PID_GYRO_INTEGRAL);
		}
		else
		{
			application.output("Parameter not convertable!");
		}
	}
	
	
	public static void sendMotorDistanceButton(){
		String arg = application.smotordistancet.getText();
		if(applicationCommandParser.doubleConvertable(arg)){
			double paramValue=Double.parseDouble(arg);
			Send.sendSetDouble(PID_MOTOR_DISTANCE, paramValue);
			Send.sendGetByte(PID_MOTOR_DISTANCE);
		}
		else
		{
			application.output("Parameter not convertable!");
		}
	}
	
	public static void sendMotorSpeedButton(){
		String arg = application.smotorspeedt.getText();
		if(applicationCommandParser.doubleConvertable(arg)){
			double paramValue=Double.parseDouble(arg);
			Send.sendSetDouble(PID_MOTOR_SPEED, paramValue);
			Send.sendGetByte(PID_MOTOR_SPEED);
		}
		else
		{
			application.output("Parameter not convertable!");
		}
	}
	
	public static void sendConstantSpeedButton(){
		String arg = application.sdistancetargett.getText();
		if(applicationCommandParser.floatConvertable(arg)){
			float paramValue=Float.parseFloat(arg);
			Send.sendSetFloat((byte)128, paramValue);
		}
		else
		{
			application.output("Parameter not convertable!");
		};
	}
	
	public static void sendConstantRotationButton(){
		String arg = application.srotationtargett.getText();
		if(applicationCommandParser.floatConvertable(arg)){
			float paramValue=Float.parseFloat(arg);
			Send.sendSetFloat((byte)129, paramValue);
		}
		else
		{
			application.output("Parameter not convertable!");
		}
	}
	
	public static void sendWheeldiameterButton(){
		String arg = application.getWheelDiameter();
		if(applicationCommandParser.floatConvertable(arg)){
			float paramValue=Float.parseFloat(arg);
			Send.sendSetFloat((byte)130, paramValue);
		}
		else
		{
			application.output("Parameter not convertable!");
		}
	}
	
	public static void sendTrackButton(){
		String arg = application.getTrack();
		if(applicationCommandParser.floatConvertable(arg)){
			float paramValue=Float.parseFloat(arg);
			Send.sendSetFloat((byte)131, paramValue);
		}
		else
		{
			application.output("Parameter not convertable!");
		}
	}
	
	public static void sendAllButton(){
		application.output("SendAllParameter");
		sendGyroSpeedButton();
		sendGyroIntegralButton();
		sendMotorDistanceButton();
		sendMotorSpeedButton();
		sendConstantSpeedButton();
		sendConstantRotationButton();
		sendWheeldiameterButton();
		sendTrackButton();
	}
	//MapTab
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
