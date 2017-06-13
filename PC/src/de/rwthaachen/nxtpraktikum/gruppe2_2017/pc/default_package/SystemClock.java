package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.default_package;

import java.util.Date;

import org.eclipse.swt.widgets.Display;

public class SystemClock extends application implements Runnable
{

	public void run(){
		
		Date nd= new Date();
		long zero = System.currentTimeMillis();
		while(applicationHandler.ClockStarter){
			nd.setTime(System.currentTimeMillis()-zero);
			
			application.currentTime=nd.getHours()-1+":"+nd.getMinutes()+":"+nd.getSeconds();
			
			Display.getDefault().syncExec(new Runnable(){public void run(){
					application.setTimeText(currentTime);
			}});
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
 