package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.swt.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Composite;
import java.awt.Frame;
import org.eclipse.swt.awt.SWT_AWT;
import java.awt.Panel;
import java.awt.event.WindowEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Event;

import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;

import javax.swing.JRootPane;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Combo;

public class application {

	protected Shell shlNxtControl;
	private static Button btnVor;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	static Text txtNewText;
	static Text text;
	static Text text_1;
	static Text text_2;
	static Text sgyrospeedt;
	static Text sgyrointegralt;
	static Text smotorspeedt;
	static Text smotordistancet;
	static Text sdistancetargett;
	static Text srotationtargett;
	static Text akkuspannungt;
	static Text neigungt;
	static Text motorat;
	static Text motorbt;
	static Text drivedistancet;
	static Text turnabsolutet;
	static Text turnrelativet;
	static Text driveToXt;
	static Text driveToYt;
	static Text text_3;
	private static String ConnectionType;
	private static String ComboWheelDiameter;
	private static String ComboTrack;
	static Button connectb;
	static Button btnSenden;
	static Button btnLinks;
	static Button btnRechts;
	static Button btnZ;
	static Button drivedistanceb;
	static Button turnabsoluteb;
	static Button turnrelativeb;
	static Button driveTob;
	static Button sgyrospeedb;
	static Button sgyrointegralb;
	static Button smotorspeedb;
	static Button smotordistanceb;
	static Button sreifengroesseb;
	static Button sdistancetargetb;
	static Button srotationtargetb;
	static Button sspurb;
	static Button paramsendall;
	static Label label_4; 
	static Label label_6;
	static Label lblCrashWarning;
	static String currentTime="";
	static Button btnAutostatuspacket;
	static Button btnBalancieren;
	private static Text getgyrospeedt;
	private static Text getgyrointegralt;
	private static Text getmotorspeedt;
	private static Text getmotordistancet;
	private static Text getconstantrotationt;
	private static Text getconstantspeedt;
	private static Text getwheeldiametert;
	private static Text gettrackt;
	public static long motorA=0;
	public static long motorB=0;



	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			application window = new application();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static void setGyroIntegralt(double paramValue){
		getgyrointegralt.setText(""+paramValue);
	}
	
	public static void setMotorSpeedt(double paramValue){
		getmotorspeedt.setText(""+paramValue);
	}
	
	public static void setMotorDistancet(double paramValue){
		getmotordistancet.setText(""+paramValue);
	}
	
	public static void setGyroSpeedt(double paramValue){
		getgyrospeedt.setText(""+paramValue);
	}
	
	
	public static void output(String text){
		txtNewText.append(text + "\n");
	}
	
	private static void setConnectionType(String type)
	{
		ConnectionType = type;
	}
	
	public static String getConnectionType()
	{
		return ConnectionType;
	}
	
	public static String getWheelDiameter()
	{
		return ComboWheelDiameter;
	}
	
	public static String getTrack()
	{
		return ComboTrack;
	}
	
	static void setConnectionButtonText(String type)
	{
		connectb.setText(type);
	}
	
	static void setCurrentPositionLabel(long x, long y){
		text.setText(""+x);
		text_1.setText(""+y);
	}
	
	public static void setBatteryLabel(int paramValue){
		akkuspannungt.setText(""+paramValue + " V");
		if(paramValue>0){
			setBatteryLabel(true); //TODO Werte anpassen
		}
		else{
			setBatteryLabel(false);
		}
	}
	
	public static void setTiltLabel(float paramValue){
		neigungt.setText(""+paramValue + " °");
	}
	
	public static void setSpeedometerLabel(float paramValue){
		motorat.setText(""+paramValue + " cm/s");
	}
	
	public static void setRotationLabel(float paramValue){
		motorbt.setText(""+paramValue + " °");
	}
	
	public static void setPositionLabel(float paramValue1, float paramValue2){
		text.setText(""+paramValue1);
		text_1.setText(""+paramValue2);
	}
	
	static void setTimeText(String time){
		text_3.setText(time);
	}
	
	
	
	static void disableButtons(){
		btnVor.setEnabled(false);
		btnLinks.setEnabled(false);
		btnRechts.setEnabled(false);
		btnZ.setEnabled(false);
		drivedistanceb.setEnabled(false);
		turnabsoluteb.setEnabled(false);
		turnrelativeb.setEnabled(false);
		driveTob.setEnabled(false);
		sgyrospeedb.setEnabled(false);
		sgyrointegralb.setEnabled(false);
		smotorspeedb.setEnabled(false);
		smotordistanceb.setEnabled(false);
		sreifengroesseb.setEnabled(false);
		sdistancetargetb.setEnabled(false);
		srotationtargetb.setEnabled(false);
		sspurb.setEnabled(false);
		paramsendall.setEnabled(false);
		btnSenden.setEnabled(false);
		btnAutostatuspacket.setEnabled(false);
		btnBalancieren.setEnabled(false);
	}
	
	static void enableButtons(){
		btnVor.setEnabled(true);
		btnLinks.setEnabled(true);
		btnRechts.setEnabled(true);
		btnZ.setEnabled(true);
		drivedistanceb.setEnabled(true);
		turnabsoluteb.setEnabled(true);
		turnrelativeb.setEnabled(true);
		driveTob.setEnabled(true);
		sgyrospeedb.setEnabled(true);
		sgyrointegralb.setEnabled(true);
		smotorspeedb.setEnabled(true);
		smotordistanceb.setEnabled(true);
		sreifengroesseb.setEnabled(true);
		sdistancetargetb.setEnabled(true);
		srotationtargetb.setEnabled(true);
		sspurb.setEnabled(true);
		paramsendall.setEnabled(true);
		btnSenden.setEnabled(true);
		btnAutostatuspacket.setEnabled(true);
		btnBalancieren.setEnabled(true);
	}
	
	static void setConnectionLabel(boolean status){
		if(status){
			label_4.setBackground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
		}
		else{
			label_4.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
		}
	}
	
	static void setBatteryLabel(boolean status){
		if(status){
			label_6.setBackground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
		}
		else{
			label_6.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
		}
	}
	
	static void setCrashWarningLabel(boolean status){
		lblCrashWarning.setVisible(status);
	}
	
	public static void setAutoStatusPacket(boolean status){
		btnAutostatuspacket.setSelection(status);	
		}
	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		disableButtons();
		btnVor.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e)
			{
				if(e.keyCode ==119)
				{
					applicationHandler.goForwardButton();
				}
				else if(e.keyCode ==97)
				{
					applicationHandler.goLeftButton();
				}
				else if(e.keyCode ==115)
				{
					applicationHandler.goBackButton();
				}
				else if(e.keyCode ==100)
				{
					applicationHandler.goRightButton();
				}
				else{
					
				}
			}
		});
		
		shlNxtControl.addListener(SWT.Close, new Listener()
	    {
			@Override
			public void handleEvent(org.eclipse.swt.widgets.Event arg0) {
				//include disconnect
				
				if(!applicationHandler.getConnectionStatus()){
					applicationHandler.disconnect();
					System.out.print("disconnected & ");
					//TODO: let it wait some seconds before closing, so the NXT can receive the message.
					/*
					Display.getDefault().syncExec(new Runnable() {public void run() {
					    try {
							wait(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}});
					*/
				}
				
				System.out.println("closed");
			}
	    });   
		
		
		shlNxtControl.open();
		shlNxtControl.layout();
		while (!shlNxtControl.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		
		
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlNxtControl = new Shell();
		shlNxtControl.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		shlNxtControl.setSize(1000, 580);
		shlNxtControl.setText("NXT Control");
		shlNxtControl.setLayout(null);
		
	
		
		
		Label lblKommunikation = new Label(shlNxtControl, SWT.NONE);
		lblKommunikation.setBounds(772, 130, 92, 15);
		formToolkit.adapt(lblKommunikation, true, true);
		lblKommunikation.setText("Communication");
		lblKommunikation.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		
		txtNewText = new Text(shlNxtControl, SWT.BORDER | SWT.V_SCROLL);
        txtNewText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
		txtNewText.setEnabled(false);
		txtNewText.setEditable(false);
		txtNewText.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtNewText.setBounds(665, 151, 317, 359);
		
		Label lblNewLabel = new Label(shlNxtControl, SWT.NONE);
		lblNewLabel.setBounds(10, 10, 63, 15);
		formToolkit.adapt(lblNewLabel, true, true);
		lblNewLabel.setText("Connection");
		lblNewLabel.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		text_2 = new Text(shlNxtControl, SWT.BORDER);
		text_2.setBounds(665, 516, 261, 26);
		formToolkit.adapt(text_2, true, true);
		
		btnSenden = new Button(shlNxtControl, SWT.NONE);
		btnSenden.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
                 applicationHandler.sendCommandButton();
			}
		});
		btnSenden.setBounds(932, 516, 50, 26);
		formToolkit.adapt(btnSenden, true, true);
		btnSenden.setText("Send");
		btnSenden.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		label_4 = new Label(shlNxtControl, SWT.NONE);
		label_4.setBounds(79, 10, 17, 15);
		formToolkit.adapt(label_4, true, true);
		label_4.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
		
		/*
		Button btnBluetooth = new Button(shlNxtControl, SWT.RADIO);
		btnBluetooth.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setConnectionType("Bluetooth");
			}
		});
		btnBluetooth.setBounds(10, 31, 83, 16);
		formToolkit.adapt(btnBluetooth, true, true);
		btnBluetooth.setText("Bluetooth");
		btnBluetooth.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		Button btnKabel = new Button(shlNxtControl, SWT.RADIO);
		btnKabel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setConnectionType("USB");
			}
		});
		btnKabel.setBounds(99, 31, 50, 16);
		formToolkit.adapt(btnKabel, true, true);
		btnKabel.setText("USB");
		btnKabel.setBackground(SWTResourceManager.getColor(199, 221, 242));
		*/
		connectb = new Button(shlNxtControl, SWT.NONE);
		connectb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applicationHandler.connectButton();
			}
		});
		connectb.setText("Connect");
		connectb.setBounds(10, 30, 139, 26);//Wenn Verbindungsart gesetzt werden soll, muss zweiter Wert auf 53 gesetzt werden
		formToolkit.adapt(connectb, true, true);
		connectb.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		
		
		Label lblNeigung = new Label(shlNxtControl, SWT.NONE);
		lblNeigung.setText("Tilt");
		lblNeigung.setBounds(788, 12, 49, 26);
		formToolkit.adapt(lblNeigung, true, true);
		lblNeigung.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		Label lblMotorA = new Label(shlNxtControl, SWT.NONE);
		lblMotorA.setBounds(516, 55, 83, 24);
		formToolkit.adapt(lblMotorA, true, true);
		lblMotorA.setText("Speedometer");
		lblMotorA.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		Label lblDrehung = new Label(shlNxtControl, SWT.NONE);
		lblDrehung.setBounds(788, 55, 49, 15);
		formToolkit.adapt(lblDrehung, true, true);
		lblDrehung.setText("Rotation");
		lblDrehung.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		Label lblAkkuspannung = new Label(shlNxtControl, SWT.NONE);
		lblAkkuspannung.setBounds(516, 10, 92, 22);
		formToolkit.adapt(lblAkkuspannung, true, true);
		lblAkkuspannung.setText("Battery voltage");
		lblAkkuspannung.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		akkuspannungt = new Text(shlNxtControl, SWT.BORDER);
		akkuspannungt.setEnabled(false);
		akkuspannungt.setEditable(false);
		akkuspannungt.setBounds(635, 9, 139, 25);
		formToolkit.adapt(akkuspannungt, true, true);
		
		neigungt = new Text(shlNxtControl, SWT.BORDER);
		neigungt.setEnabled(false);
		neigungt.setEditable(false);
		neigungt.setBounds(843, 9, 139, 25);
		formToolkit.adapt(neigungt, true, true);
		
		motorat = new Text(shlNxtControl, SWT.BORDER);
		motorat.setEnabled(false);
		motorat.setEditable(false);
		motorat.setBounds(635, 50, 139, 25);
		formToolkit.adapt(motorat, true, true);
		
		motorbt = new Text(shlNxtControl, SWT.BORDER);
		motorbt.setEnabled(false);
		motorbt.setEditable(false);
		motorbt.setBounds(843, 52, 139, 25);
		formToolkit.adapt(motorbt, true, true);
		
		label_6 = new Label(shlNxtControl, SWT.NONE);
		label_6.setBounds(612, 12, 17, 15);
		formToolkit.adapt(label_6, true, true);
		label_6.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
		
		TabFolder tabFolder = new TabFolder(shlNxtControl, SWT.NONE);
		tabFolder.setBounds(0, 135, 655, 407);
		formToolkit.adapt(tabFolder);
		formToolkit.paintBordersFor(tabFolder);
		tabFolder.setBackground(SWTResourceManager.getColor(199, 221, 242));
		tabFolder.setForeground(SWTResourceManager.getColor(199, 221, 242));
		
		
		TabItem tbtmPosition = new TabItem(tabFolder, SWT.NONE);
		tbtmPosition.setText("Position");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmPosition.setControl(composite);
		formToolkit.paintBordersFor(composite);
		composite.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		btnVor = new Button(composite, SWT.NONE);
		btnVor.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applicationHandler.goForwardButton();
			}
		});
		btnVor.setBounds(456, 22, 50, 50);
		btnVor.setText("Forward");
		btnVor.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		
		btnLinks = new Button(composite, SWT.NONE);
		btnLinks.setBounds(401, 77, 50, 50);
		btnLinks.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applicationHandler.goLeftButton();
			}
		});
		btnLinks.setText("Left");
		btnLinks.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		btnRechts = new Button(composite, SWT.NONE);
		btnRechts.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applicationHandler.goRightButton();
			}
		});
		btnRechts.setBounds(511, 77, 50, 50);
		btnRechts.setText("Right");
		btnRechts.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		btnZ = new Button(composite, SWT.NONE);
		btnZ.setBounds(456, 77, 50, 50);
		btnZ.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applicationHandler.goBackButton();
			}
		});
		btnZ.setText("Back");
		btnZ.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		drivedistancet = new Text(composite, SWT.BORDER);
		drivedistancet.setBounds(34, 9, 139, 25);
		formToolkit.adapt(drivedistancet, true, true);
		
		turnabsolutet = new Text(composite, SWT.BORDER);
		turnabsolutet.setBounds(34, 40, 139, 25);
		formToolkit.adapt(turnabsolutet, true, true);
		
		turnrelativet = new Text(composite, SWT.BORDER);
		turnrelativet.setBounds(34, 71, 139, 25);
		formToolkit.adapt(turnrelativet, true, true);
		
		driveToXt = new Text(composite, SWT.BORDER);
		driveToXt.setBounds(34, 102, 50, 25);
		formToolkit.adapt(driveToXt, true, true);
		
		driveToYt = new Text(composite, SWT.BORDER);
		driveToYt.setBounds(123, 102, 50, 25);
		formToolkit.adapt(driveToYt, true, true);
		
		drivedistanceb = new Button(composite, SWT.NONE);
		drivedistanceb.setBounds(179, 9, 139, 25);
		drivedistanceb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applicationHandler.driveDistanceButton();
			}
		});
		formToolkit.adapt(drivedistanceb, true, true);
		drivedistanceb.setText("drive distance (cm)");
		drivedistanceb.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		turnabsoluteb = new Button(composite, SWT.NONE);
		turnabsoluteb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applicationHandler.turnAbsoluteButton();
			}
		});
		turnabsoluteb.setBounds(179, 40, 139, 25);
		turnabsoluteb.setText("turn absolute");
		formToolkit.adapt(turnabsoluteb, true, true);
		turnabsoluteb.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		turnrelativeb = new Button(composite, SWT.NONE);
		turnrelativeb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applicationHandler.turnRelativeButton();
			}
		});
		turnrelativeb.setBounds(179, 71, 139, 25);
		turnrelativeb.setText("turn relative");
		formToolkit.adapt(turnrelativeb, true, true);
		turnrelativeb.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		driveTob = new Button(composite, SWT.NONE);
		driveTob.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applicationHandler.driveToButton();
			}
		});
		driveTob.setBounds(179, 102, 139, 25);
		driveTob.setText("drive to");
		formToolkit.adapt(driveTob, true, true);
		driveTob.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		Label lblX_1 = new Label(composite, SWT.NONE);
		lblX_1.setBounds(10, 108, 17, 13);
		lblX_1.setText("x:");
		formToolkit.adapt(lblX_1, true, true);
		lblX_1.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		Label lblY = new Label(composite, SWT.NONE);
		lblY.setBounds(99, 108, 17, 13);
		lblY.setText("y:");
		formToolkit.adapt(lblY, true, true);
		lblY.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		TabItem tbtmParameter = new TabItem(tabFolder, SWT.NONE);
		tbtmParameter.setText("Parameter");
		
		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tbtmParameter.setControl(composite_1);
		formToolkit.paintBordersFor(composite_1);
		composite_1.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		sgyrospeedb = new Button(composite_1, SWT.NONE);
		sgyrospeedb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applicationHandler.sendGyroSpeedButton();
			}
		});
		sgyrospeedb.setBounds(169, 22, 139, 25);
		sgyrospeedb.setText("send gyro speed");
		formToolkit.adapt(sgyrospeedb, true, true);
		sgyrospeedb.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		sgyrospeedt = new Text(composite_1, SWT.BORDER);
		sgyrospeedt.setBounds(24, 22, 139, 25);
		formToolkit.adapt(sgyrospeedt, true, true);
		
		sgyrointegralb = new Button(composite_1, SWT.NONE);
		sgyrointegralb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applicationHandler.sendGyroIntegralButton();
			}
		});
		sgyrointegralb.setBounds(169, 53, 139, 25);
		sgyrointegralb.setText("send gyrointegral");
		formToolkit.adapt(sgyrointegralb, true, true);
		sgyrointegralb.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		sgyrointegralt = new Text(composite_1, SWT.BORDER);
		sgyrointegralt.setBounds(24, 53, 139, 25);
		formToolkit.adapt(sgyrointegralt, true, true);
		
		smotorspeedb = new Button(composite_1, SWT.NONE);
		smotorspeedb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applicationHandler.sendMotorSpeedButton();
			}
		});
		smotorspeedb.setBounds(169, 84, 139, 25);
		smotorspeedb.setText("send motorspeed");
		formToolkit.adapt(smotorspeedb, true, true);
		smotorspeedb.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		smotorspeedt = new Text(composite_1, SWT.BORDER);
		smotorspeedt.setBounds(24, 84, 139, 25);
		formToolkit.adapt(smotorspeedt, true, true);
		
		smotordistanceb = new Button(composite_1, SWT.NONE);
		smotordistanceb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applicationHandler.sendMotorDistanceButton();
			}
		});
		smotordistanceb.setBounds(169, 115, 139, 25);
		smotordistanceb.setText("send motordistance");
		formToolkit.adapt(smotordistanceb, true, true);
		smotordistanceb.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		smotordistancet = new Text(composite_1, SWT.BORDER);
		smotordistancet.setBounds(24, 115, 139, 25);
		formToolkit.adapt(smotordistancet, true, true);
		
		sreifengroesseb = new Button(composite_1, SWT.NONE);
		sreifengroesseb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applicationHandler.sendWheeldiameterButton();
			}
		});
		sreifengroesseb.setBounds(169, 208, 139, 25);
		sreifengroesseb.setText("send wheeldiameter cm");
		formToolkit.adapt(sreifengroesseb, true, true);
		sreifengroesseb.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		sdistancetargetb = new Button(composite_1, SWT.NONE);
		sdistancetargetb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applicationHandler.sendConstantSpeedButton();
			}
		});
		sdistancetargetb.setBounds(169, 177, 139, 25);
		sdistancetargetb.setText("send constant speed");
		formToolkit.adapt(sdistancetargetb, true, true);
		sdistancetargetb.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		sdistancetargett = new Text(composite_1, SWT.BORDER);
		sdistancetargett.setBounds(24, 177, 139, 25);
		formToolkit.adapt(sdistancetargett, true, true);
		
		srotationtargetb = new Button(composite_1, SWT.NONE);
		srotationtargetb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applicationHandler.sendConstantRotationButton();
			}
		});
		srotationtargetb.setBounds(169, 146, 139, 25);
		srotationtargetb.setText("send constant rotation");
		formToolkit.adapt(srotationtargetb, true, true);
		srotationtargetb.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		srotationtargett = new Text(composite_1, SWT.BORDER);
		srotationtargett.setBounds(24, 146, 139, 25);
		formToolkit.adapt(srotationtargett, true, true);
		
		sspurb = new Button(composite_1, SWT.NONE);
		sspurb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applicationHandler.sendTrackButton();
			}
		});
		sspurb.setBounds(169, 241, 139, 25);
		sspurb.setText("send track");
		formToolkit.adapt(sspurb, true, true);
		sspurb.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		paramsendall = new Button(composite_1, SWT.NONE);
		paramsendall.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applicationHandler.sendAllButton();
			}
		});
		paramsendall.setBounds(24, 272, 284, 50);
		formToolkit.adapt(paramsendall, true, true);
		paramsendall.setText("send all parameter");
		paramsendall.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		Combo combo = new Combo(composite_1, SWT.NONE);
		combo.setBounds(24, 208, 139, 23);
		formToolkit.adapt(combo);
		formToolkit.paintBordersFor(combo);
		combo.setItems("5.6","12");
		combo.setText("5.6");
		ComboWheelDiameter = combo.getText();
		combo.addModifyListener(new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent arg0) {
				ComboWheelDiameter = combo.getText();
			}
		});
		
		
		Combo combo_1 = new Combo(composite_1, SWT.NONE);
		combo_1.setBounds(24, 239, 139, 23);
		formToolkit.adapt(combo_1);
		formToolkit.paintBordersFor(combo_1);
		combo_1.setItems("inside", "outside");
		combo_1.setText("inside");
		ComboTrack = combo_1.getText();
		combo_1.addModifyListener(new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent arg0) {
				ComboTrack = combo_1.getText();
			}
		});
		
		TabItem tbtmKarte = new TabItem(tabFolder, SWT.NONE);
		tbtmKarte.setText("Map");
		
		Composite composite_2 = new Composite(tabFolder, SWT.NONE);
		tbtmKarte.setControl(composite_2);
		formToolkit.paintBordersFor(composite_2);
		composite_2.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		Label lblPosition = new Label(shlNxtControl, SWT.NONE);
		lblPosition.setBounds(339, 10, 100, 15);
		formToolkit.adapt(lblPosition, true, true);
		lblPosition.setText("Current position");
		lblPosition.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		text = new Text(shlNxtControl, SWT.BORDER);
		text.setEnabled(false);
		text.setEditable(false);
		text.setBounds(338, 26, 50, 25);
		formToolkit.adapt(text, true, true);
		
		text_1 = new Text(shlNxtControl, SWT.BORDER);
		text_1.setEnabled(false);
		text_1.setEditable(false);
		text_1.setBounds(427, 26, 50, 25);
		formToolkit.adapt(text_1, true, true);
		
		Label lblYaktuell = new Label(shlNxtControl, SWT.NONE);
		lblYaktuell.setBounds(410, 32, 20, 20);
		lblYaktuell.setText("y:");
		formToolkit.adapt(lblYaktuell, true, true);
		lblYaktuell.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		Label lblXaktuell = new Label(shlNxtControl, SWT.NONE);
		lblXaktuell.setBounds(321, 32, 17, 13);
		lblXaktuell.setText("x:");
		formToolkit.adapt(lblXaktuell, true, true);
		lblXaktuell.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		lblCrashWarning = new Label(shlNxtControl, SWT.NONE);
		lblCrashWarning.setBounds(341, 68, 139, 25);
		lblCrashWarning.setFont(SWTResourceManager.getFont("Tahoma", 16, SWT.NORMAL));
		formToolkit.adapt(lblCrashWarning, true, true);
		lblCrashWarning.setText("blocked way!");
		lblCrashWarning.setVisible(false);
		lblCrashWarning.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		lblCrashWarning.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		text_3 = new Text(shlNxtControl, SWT.BORDER);
		text_3.setText("00:00:00");
		text_3.setEnabled(false);
		text_3.setEditable(false);
		text_3.setBounds(174, 26, 76, 21);
		formToolkit.adapt(text_3, true, true);
		
		
		Label lblNewLabel_1 = new Label(shlNxtControl, SWT.NONE);
		lblNewLabel_1.setBounds(174, 10, 92, 15);
		formToolkit.adapt(lblNewLabel_1, true, true);
		lblNewLabel_1.setText("Connection time");
		lblNewLabel_1.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		btnAutostatuspacket = new Button(shlNxtControl, SWT.CHECK);
		btnAutostatuspacket.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applicationHandler.sendAutostatuspacket(btnAutostatuspacket.getSelection());
			}
		});
		btnAutostatuspacket.setBounds(174, 53, 123, 16);
		formToolkit.adapt(btnAutostatuspacket, true, true);
		btnAutostatuspacket.setText("AutoStatusPacket");
		btnAutostatuspacket.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		getgyrospeedt = new Text(composite_1, SWT.BORDER);
		getgyrospeedt.setEditable(false);
		getgyrospeedt.setEnabled(false);
		getgyrospeedt.setBounds(314, 22, 139, 25);
		formToolkit.adapt(getgyrospeedt, true, true);
		
		getgyrointegralt = new Text(composite_1, SWT.BORDER);
		getgyrointegralt.setEnabled(false);
		getgyrointegralt.setEditable(false);
		getgyrointegralt.setBounds(314, 53, 139, 25);
		formToolkit.adapt(getgyrointegralt, true, true);
		
		getmotorspeedt = new Text(composite_1, SWT.BORDER);
		getmotorspeedt.setEnabled(false);
		getmotorspeedt.setEditable(false);
		getmotorspeedt.setBounds(314, 84, 139, 25);
		formToolkit.adapt(getmotorspeedt, true, true);
		
		getmotordistancet = new Text(composite_1, SWT.BORDER);
		getmotordistancet.setEnabled(false);
		getmotordistancet.setEditable(false);
		getmotordistancet.setBounds(314, 115, 139, 25);
		formToolkit.adapt(getmotordistancet, true, true);
		
		getconstantrotationt = new Text(composite_1, SWT.BORDER);
		getconstantrotationt.setEnabled(false);
		getconstantrotationt.setEditable(false);
		getconstantrotationt.setBounds(314, 146, 139, 25);
		formToolkit.adapt(getconstantrotationt, true, true);
		
		getconstantspeedt = new Text(composite_1, SWT.BORDER);
		getconstantspeedt.setEnabled(false);
		getconstantspeedt.setEditable(false);
		getconstantspeedt.setBounds(314, 177, 139, 25);
		formToolkit.adapt(getconstantspeedt, true, true);
		
		getwheeldiametert = new Text(composite_1, SWT.BORDER);
		getwheeldiametert.setEnabled(false);
		getwheeldiametert.setEditable(false);
		getwheeldiametert.setBounds(314, 208, 139, 25);
		formToolkit.adapt(getwheeldiametert, true, true);
		
		gettrackt = new Text(composite_1, SWT.BORDER);
		gettrackt.setEnabled(false);
		gettrackt.setEditable(false);
		gettrackt.setBounds(314, 241, 139, 25);
		formToolkit.adapt(gettrackt, true, true);

		btnBalancieren = new Button(shlNxtControl, SWT.CHECK);
		btnBalancieren.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applicationHandler.sendBalancieren(btnBalancieren.getSelection());
			}
		});
		btnBalancieren.setBounds(174, 73, 123, 16);
		formToolkit.adapt(btnBalancieren, true, true);
		btnBalancieren.setText("Balancing");
		btnBalancieren.setBackground(SWTResourceManager.getColor(199, 221, 242));
		btnBalancieren.setSelection(false);
		
		
	}
}
