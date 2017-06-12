package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.default_package;

public class applicationHandler {
	//Connect Area
	private static boolean ConnectionStatus=true;
	public static void connectButton(){
		if(application.getConnectionType()!=null){
			if(ConnectionStatus){
				//Add Connect here!
				application.output("Connect via "+application.getConnectionType());
				application.enableButtons();
				application.setConnectionLabel(true);
				application.setConnectionButtonText("Disconnect");
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
			}
			
		}
		else{
			application.output("Please select connection type!");
		}
		
	}
	//Parameter Area
	
	//Command Area
	public static void sendCommandButton(){
		application.output(application.text_2.getText());
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
	
	//ParameterTab
	//assuming paramID for parameter ranges from 21-
	public static void sendGyroSpeedButton(){
		String arg = application.sgyrospeedt.getText();
		if(applicationCommandParser.floatConvertable(arg)){
			float paramValue=Float.parseFloat(arg);
			Send.sendSetFloat((byte)21, paramValue);
		}
		else
		{
			application.output("Parameter not convertable!");
		}
	}
	
	public static void sendGyroIntegralButton(){
		String arg = application.sgyrointegralt.getText();
		if(applicationCommandParser.floatConvertable(arg)){
			float paramValue=Float.parseFloat(arg);
			Send.sendSetFloat((byte)22, paramValue);
		}
		else
		{
			application.output("Parameter not convertable!");
		}
	}
	
	
	public static void sendMotorDistanceButton(){
		String arg = application.smotordistancet.getText();
		if(applicationCommandParser.floatConvertable(arg)){
			float paramValue=Float.parseFloat(arg);
			Send.sendSetFloat((byte)23, paramValue);
		}
		else
		{
			application.output("Parameter not convertable!");
		}
	}
	
	public static void sendMotorSpeedButton(){
		String arg = application.smotorspeedt.getText();
		if(applicationCommandParser.floatConvertable(arg)){
			float paramValue=Float.parseFloat(arg);
			Send.sendSetFloat((byte)24, paramValue);
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
