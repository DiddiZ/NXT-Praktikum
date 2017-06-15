package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.default_package;

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

}
