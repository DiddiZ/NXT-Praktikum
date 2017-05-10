import communication.callbackMethods;
import segoway.Segoway;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.DataInputStream;

public class SegwayController implements callbackMethods{

	private Segoway segWay;
	private int speed = 50;
	
	SegwayController(Segoway p_segway) {
		segWay = p_segway;
	}
	
 	public void callback0 (float p_parameter) {
 		int turnSpeed = (int) (0.25 * speed);
 		
 		char parameter = (char) p_parameter;
 		
 		switch (parameter) {
		case 'w':
			segWay.wheelDriver(speed, speed);
			break;
		case 'a':
			segWay.wheelDriver(-turnSpeed, turnSpeed);
			break;
		case 'd': 
			segWay.wheelDriver(turnSpeed, -turnSpeed);
			break;
		case 's': 
			segWay.wheelDriver(-speed, -speed);
			break;
		default:
			segWay.wheelDriver(0, 0);
		}
 	}
 	
 	
 	public void callback1 (float p_parameter) {}
	
}
