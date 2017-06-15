package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;

public class applicationCommandParser {
	
	public static boolean byteConvertable(String arg){
		try{
			byte a=Byte.parseByte(arg);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public static boolean intConvertable(String arg){
		try{
			int a=Integer.parseInt(arg);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public static boolean floatConvertable(String arg){
		try{
			float a=Float.parseFloat(arg);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public static boolean doubleConvertable(String arg){
		try{
			double a=Double.parseDouble(arg);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public static boolean longConvertable(String arg){
		try{
			long a=Long.parseLong(arg);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	/*
	 * This method is the commandline's handler. It parses a string and calls another method if it finds one.
	 */
	public static void parse(String input){
		String[] inputarray=input.split(" ");
		int arraylength=inputarray.length;
		
		//filtering out command from array
		String inputcommand=inputarray[0];
		inputcommand=inputcommand.toLowerCase();
		
		//filtering out Parameters from array
		int numberOfParams=arraylength-1;
		String[] paramarray=new String[numberOfParams];
		for(int i=0; i<arraylength-1;i++){
			paramarray[i]=inputarray[i+1];
		}
		
		
		// Switch case for sending commands
		int switchvariable=filterCommand(inputcommand);
		switch(switchvariable){
		case 1: 
			//set
			if(numberOfParams<2){
			        //output: not enough Parameters
		            }else{
			         if(byteConvertable(paramarray[0])){
			        	 parseSet(paramarray, numberOfParams);
			         }else{
			        	 application.output("First Parameter is no valid Parameter ID!");
			         }
		            }
			break;
		case 2:
			//
			if(arraylength<2){
				application.output("Parameter ID missing");
			}else{
				if(byteConvertable(paramarray[0])){
					Send.sendGetByte(Byte.parseByte(paramarray[0]));
				}else{
					application.output("Parameter is not correct! Should be byte");
				}
			}
			break;
		case 3:
			//Move
			if(arraylength<2){
				application.output("Parameter ID missing");
			}else{
				if(floatConvertable(paramarray[0])){
					Send.sendMove(Float.parseFloat(paramarray[0]));
				}else{
					application.output("Parameter is not correct! Should be float");
				}
			}
			break;
		case 4:
			//Turn
			if(arraylength<2){
				application.output("Parameter ID missing");
			}else{
				if(floatConvertable(paramarray[0])){
					Send.sendTurn(Float.parseFloat(paramarray[0]));
				}else{
					application.output("Parameter is not correct! Should be float");
				}
			}
			break;
		case 5:
			//Moveto
			application.output("Navigation is available in navigation DLC releasing on the 6th of July for only $5.99!");
			//#Navigation
			break;
		case 6:
			//Balancing
			if(arraylength<2){
				application.output("Parameter missing!");
			}else{
				boolean bvalue;
				if(paramarray[0]=="true"||paramarray[0]=="false"){
					if(paramarray[0]=="true"){
						bvalue=true;
					}else{
						bvalue=false;
					}
					Send.sendBalancing(bvalue);
				}else{
					application.output("Parameter is not boolean!");
				}
			}
			break;
		case 7:
			//disconnect
			Send.sendDisconnect();
			break;
		case 8:
			//sendManual
			sendManual(paramarray, numberOfParams);
			break;
		//#NewCommand
		default: application.output("unknown command");
		}
		
	
	}
	
	/*
	 * This method is called in the parse method. Its function is to send data to the NXT manually.
	 */
	public static void sendManual(String[] paramarray, int paramNumber){
		application.output("This feature is implemented later.");
	}
	
	
	/*
	 * This method is called in the parse method. It handles the "set" command on its to make it more organized.
	 */
	public static void parseSet(String[] paramarray, int paramNumber){
		byte switchvariable=Byte.parseByte(paramarray[0]);
		switch(switchvariable){
		case (byte)5:
		case (byte)21:
		case (byte)22:
		case (byte)23:
		case (byte)24:
			if(paramNumber>2){
				application.output("Too Many Parameters, ignoring the last ones.");
			}
			if(floatConvertable(paramarray[1])){
				Send.sendSetFloat(Byte.parseByte(paramarray[0]), Float.parseFloat(paramarray[1]));
			}else{
				application.output("Parameter is not correct! Should be float.");
			}
			
			break;
		case (byte)9:
			if(paramNumber>2){
				application.output("Too Many Parameters, ignoring the last ones.");
			}
			boolean bvalue;
			if(paramarray[1]=="true"||paramarray[1]=="false"){
				if(paramarray[1]=="true"){
					bvalue=true;
				}else{
					bvalue=false;
				}
				Send.sendSetBoolean(Byte.parseByte(paramarray[0]), bvalue);
			}else{
				application.output("Parameter is not correct! Should be true or false.");
			}
		case (byte)6:
			if(paramNumber>3){
				application.output("Too Many Parameters, ignoring the last ones.");
			}
			if(paramNumber<3){
			application.output("Position need two Parameters!");	
			}else{
			  if(floatConvertable(paramarray[1])&&floatConvertable(paramarray[2])){
				Send.sendSetFloatFloat(Byte.parseByte(paramarray[0]), Float.parseFloat(paramarray[1]), Float.parseFloat(paramarray[2]));
			  }else{
				application.output("Parameters are not correct! Should be floats.");
			  }
			}
			break;
		case (byte)1:
		case (byte)2:
		case (byte)3:
		case (byte)4:
		case (byte)7:
		case (byte)8:
			application.output("This Parameters cannot be changed!");
			break;
		//#NewCommand
		default: 
			application.output("Parameter not (yet) occupied.");
		}
	}
	
	private static int filterCommand(String arg){
		int output=0;
		if(arg=="set")output=1;
		if(arg=="get")output=2;
		if(arg=="move")output=3;
		if(arg=="turn")output=4;
		if(arg=="moveto")output=5;
		if(arg=="balancing")output=6;
		if(arg=="disconnect")output=7;
		if(arg=="send")output=8;
		//#NewCommand
		return output;
	}

}