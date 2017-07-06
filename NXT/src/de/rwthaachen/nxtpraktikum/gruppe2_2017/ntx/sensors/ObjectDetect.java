package de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.sensors;

import lejos.robotics.objectdetection.Feature;
import lejos.robotics.objectdetection.FeatureDetector;
import lejos.robotics.objectdetection.FeatureListener;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.NXT;
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.ParameterIdList.PARAM_ULTRASENSOR;

import java.io.IOException;

public class ObjectDetect implements FeatureListener{
	public ObjectDetect(){
		
	}
	
	@Override
	public void featureDetected(Feature feature, FeatureDetector detector) {
		// TODO Auto-generated method stub
		int length = feature.getRangeReadings().getNumReadings();
		for (int i = 0; i<length; i++){
			float range = feature.getRangeReadings().getRange(i);
			float angle = feature.getRangeReadings().getAngle(i);
			
			try {
				NXT.COMMUNICATOR.sendGetReturn(PARAM_ULTRASENSOR, range, angle);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

}
