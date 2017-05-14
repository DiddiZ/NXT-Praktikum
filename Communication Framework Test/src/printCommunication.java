
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import communication.callbackMethods;

public class printCommunication implements callbackMethods {

	@Override
	public void callback0(float p_parameter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void callback1(float p_parameter) {
		char transmitedChar = (char) p_parameter;
		System.out.print(transmitedChar);
	}

	@Override
	public void callback2(float p_parameter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void callback3(float p_parameter) {
		// TODO Auto-generated method stub
		
	}


}
