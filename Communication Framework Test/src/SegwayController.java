import communication.callbackMethods;
import segoway.Segoway;

public class SegwayController implements callbackMethods{

	private Segoway segWay;
	private int speed = 50;
	
	SegwayController(Segoway p_segway) {
		segWay = p_segway;
	}
	
 	public void callback0 (int p_parameter) {
 		int turnSpeed = (int) (0.25 * speed);
 		switch (p_parameter) {
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
 	public void callback1 (int p_parameter) {}
 	public void callback2 (int p_parameter) {}
 	public void callback3 (int p_parameter) {}
 	public void callback4 (int p_parameter) {}
 	public void callback5 (int p_parameter) {}
 	public void callback6 (int p_parameter) {}
 	public void callback7 (int p_parameter) {}
 	public void callback8 (int p_parameter) {}
 	public void callback9 (int p_parameter) {}
 	public void callback10 (int p_parameter) {}
 	public void callback11 (int p_parameter) {}
 	public void callback12 (int p_parameter) {}
 	public void callback13 (int p_parameter) {}
 	public void callback14 (int p_parameter) {}
 	public void callback15 (int p_parameter) {}
 	public void callback16 (int p_parameter) {}
 	public void callback17 (int p_parameter) {}
 	public void callback18 (int p_parameter) {}
 	public void callback19 (int p_parameter) {}
 	public void callback20 (int p_parameter) {}
 	public void callback21 (int p_parameter) {}
 	public void callback22 (int p_parameter) {}
 	public void callback23 (int p_parameter) {}
 	public void callback24 (int p_parameter) {}
 	public void callback25 (int p_parameter) {}
 	public void callback26 (int p_parameter) {}
 	public void callback27 (int p_parameter) {}
 	public void callback28 (int p_parameter) {}
 	public void callback29 (int p_parameter) {}
 	public void callback30 (int p_parameter) {}
	public void callback31 (int p_parameter) {}
	
}
