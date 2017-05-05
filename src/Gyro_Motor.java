import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.GyroSensor;

/*
Dieses Programm gibt auf dem LCD des NXT die Raw-Data des Gyrosensors in Echtzeit aus und erlaubt es ueber die Pfeiltasten
des NXT die Motoren zu steuern (linke Taste fuer linken Motor; rechts analog)
*/
public class Gyro_Motor {

	private static void motormove(){
		//Buttons zum Ansprechen der Motoren verwenden
		// Linker Button fuer linkes Rad
		if (Button.LEFT.isDown()){
			Motor.A.forward();  
		}
		else{
			Motor.A.stop();
	    }
	    // Rechter Button fuer rechtes Rad
		if (Button.RIGHT.isDown()){
			Motor.B.forward();
	    }
	    else{
	    	Motor.B.stop();
	    }
	}
	
	//Raw-Daten des Gyrosensors auslesen
	private static void gyroout(float data, GyroSensor gyro){
		data = gyro.getAngularVelocity(); //getAngularVelocity() liefert Raw-Daten als float
		String s = Float.toString(data); //Float wird zu String konvertiert fuer Ausgabe
		LCD.drawString(s, 0, 2); //Ausgabe in Zeile 2 des LCD von Raw-Daten
	}
	
	public static void main(String[] args) 
	{
		GyroSensor gyro = new GyroSensor(SensorPort.S2); //evlt. ist Sensorport ein anderer als S2
		float gyrodata=0;
		
		LCD.drawString("NXT_Gyro_Motor", 0, 0);
		
		while (Button.ESCAPE.isUp()) 
		{
			LCD.clear(2);
			gyroout(gyrodata, gyro);
			motormove();
		}

	}

}
