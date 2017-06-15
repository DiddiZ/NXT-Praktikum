package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn;

import org.eclipse.swt.widgets.Display;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui.application;

public class SyncExec {

	public static void syncoutput(String text){
		Display.getDefault().syncExec(new Runnable() {public void run() {
		    application.output(text);
	}});
	}
	
	public static void syncsetTiltLabel(float paramValue){
		Display.getDefault().syncExec(new Runnable() {public void run() {
		    application.setTiltLabel(paramValue);
	}});
	}

	public static void syncsetRotationLabel(float paramValue){
		Display.getDefault().syncExec(new Runnable() {public void run() {
			application.setRotationLabel(paramValue);
	}});
	}
	
	public static void syncsetBatteryLabel(int paramValue){
		Display.getDefault().syncExec(new Runnable() {public void run() {
		    application.setBatteryLabel(paramValue);
	}});
	}
	
	public static void syncsetSpeedometerLabel(float paramValue1){
		Display.getDefault().syncExec(new Runnable() {public void run() {
		    application.setSpeedometerLabel(paramValue1);
	}});
	}
	
	public static void syncsetPositionLabel(float paramValue1, float paramValue2){
		Display.getDefault().syncExec(new Runnable() {public void run() {
		    application.setPositionLabel(paramValue1, paramValue2);
	}});
	}
	
	public static void syncsetAutoStatusPacket(boolean status){
		Display.getDefault().syncExec(new Runnable() {public void run() {
		    application.setAutoStatusPacket(status);
	}});
	}
	
	public static void syncsetGyroSpeed(double paramValue){
		Display.getDefault().syncExec(new Runnable() {public void run() {
		    application.setGyroSpeedt(paramValue);
	}});
	}
	
	public static void syncsetGyroIntegral(double paramValue){
		Display.getDefault().syncExec(new Runnable() {public void run() {
		    application.setGyroIntegralt(paramValue);
	}});
	}
	
	public static void syncsetMotorSpeed(double paramValue){
		Display.getDefault().syncExec(new Runnable() {public void run() {
		    application.setMotorSpeedt(paramValue);
	}});
	}
	
	public static void syncsetMotorDistance(double paramValue){
		Display.getDefault().syncExec(new Runnable() {public void run() {
		    application.setMotorDistancet(paramValue);
	}});
	}
	
	public static void syncsetTachoLeft(long paramValue) {
		
	}
	
	public static void syncsetTachoRight(long paramValue) {
		
	}
	
	
	
	
	
	
}
