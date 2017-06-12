package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.default_package;

public class applicationHandler {
	//Connect Area
	private static boolean ConnectionStatus=true;
	public static void connectButton(){
		if(application.getConnectionType()!=null){
			if(ConnectionStatus){
				application.output("Connect via "+application.getConnectionType());
				application.enableButtons();
				application.setConnectionButtonText("Disconnect");
				ConnectionStatus=false;
			}
			else
			{
				application.output("Disconnected");
				application.disableButtons();
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
	}
	
	public static void goBackButton(){
		application.output("Back");
	}
	
	public static void goLeftButton(){
		application.output("Left");
	}
	
	public static void goRightButton(){
		application.output("Right");
	}
	
	public static void driveDistanceButton(){
		application.output("driveDistance: "+application.drivedistancet.getText());
	}
	
	public static void turnAbsoluteButton(){
		application.output("turnAbsolut: "+application.turnabsolutet.getText());
	}
	
	public static void turnRelativeButton(){
		application.output("turnRelative: "+application.turnrelativet.getText());
	}
	
	public static void driveToButton(){
		application.output("drive to: "+application.driveToXt.getText()+", "+application.driveToYt.getText());
	}
	
	//ParameterTab
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
		application.output("SendGyroIntegral: "+application.sgyrointegralt.getText());
	}
	
	public static void sendMotorDistanceButton(){
		application.output("SendMotorDistance: "+application.smotordistancet.getText());
	}
	
	public static void sendMotorSpeedButton(){
		application.output("SendMotorSpeed: "+application.smotorspeedt.getText());
	}
	
	public static void sendConstantSpeedButton(){
		application.output("SendConstantSpeed: "+application.sdistancetargett.getText());
	}
	
	public static void sendConstantRotationButton(){
		application.output("SendConstantRotation: "+application.srotationtargett.getText());
	}
	
	public static void sendWheeldiameterButton(){
		application.output("SendWheelDiameter: "+application.getWheelDiameter());
	}
	
	public static void sendTrackButton(){
		application.output("SendTrack: "+application.getTrack());
	}
	
	public static void sendAllButton(){
		application.output("SendAllParameter");
	}
	//MapTab
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
