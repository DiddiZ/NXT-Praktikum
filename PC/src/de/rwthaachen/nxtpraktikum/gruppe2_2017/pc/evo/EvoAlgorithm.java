package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo;

import java.util.List;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui.Send;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui.UserInterface;

public class EvoAlgorithm implements EvoInterface {

	private UserInterface ui;
	private Send send;
	private int currentState = 0;
	
	private double[] currentValues;
	private List<double[]> unprocessedPool;
	private List<double[]> processedPool;
	
	public EvoAlgorithm(UserInterface ui, Send send) {
		this.ui = ui;
		this.send = send;
	}
	
	@Override
	public void setDataSet(double paramMotorPower, double paramBatteryVoltage, double paramDistanceDifference, double paramHeadingDifference) {
		ui.showMessage("Iteration finished.");
		if (paramMotorPower == Double.MAX_VALUE) {
			ui.showMessage("Please restart the balancing thread to continue.");
		}
		currentValues[4] = paramMotorPower;
		currentValues[5] = paramBatteryVoltage;
		currentValues[6] = paramDistanceDifference;
		currentValues[7] = paramHeadingDifference;
		processedPool.add(currentValues);
	}

	@Override
	public void setState(int paramState) {
		currentState = paramState;
	}
	
	protected void chooseNextDataSet() {
		if (!unprocessedPool.isEmpty()) {
			currentValues = unprocessedPool.get(0);
			if (currentValues.length != 6) {
				System.out.println("Error in Evo algorithm. Number of values != 6");
				ui.showMessage("Error in Evo algorithm occured");
			}
			unprocessedPool.remove(0);
			startNextIteration(currentValues[0],currentValues[1],currentValues[2],currentValues[3]);
		}
	}
	
	protected void startNextIteration(double paramPID1, double paramPID2, double paramPID3, double paramPID4) {
		int valuesInPool = processedPool.size() + unprocessedPool.size();
		int valuesProcessed = processedPool.size() + 1;
		ui.showMessage("Start iteration: " + valuesProcessed + "/" + valuesInPool);
		send.sendEvoStart(paramPID1, paramPID2, paramPID3, paramPID4);
	}
	
	

}
