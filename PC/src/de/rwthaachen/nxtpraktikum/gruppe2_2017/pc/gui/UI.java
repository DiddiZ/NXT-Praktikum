package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JScrollBar;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import javax.swing.DropMode;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;

public class UI {

	private JFrame NXTControl;
	private JTextField tConnectionTime;
	private JTextField tCurrentPositionX;
	private JTextField tCurrentPostionY;
	private JTextField tBatteryValtage;
	private JTextField tSpeedometer;
	private JTextField tTilt;
	private JTextField tRotation;
	private JTextField ConsoleInput;
	private JTextField tDriveDistance;
	private JTextField tTurnAbsolute;
	private JTextField tTurnRelative;
	private JTextField tDriveToX;
	private JTextField tDriveToY;
	private JTextField tgyrospeeds;
	private JTextField tgyrointegrals;
	private JTextField tmotorspeeds;
	private JTextField tmotordistances;
	private JTextField tconstantrotations;
	private JTextField tconstantspeeds;
	private JTextField tgyrospeedg;
	private JTextField tgyrointegralg;
	private JTextField tmotorspeedg;
	private JTextField tmotordistanceg;
	private JComboBox twheeldiameters;
	private JComboBox ttracks;
	private JButton btnConnect;
	private JLabel lblBatteryVoltageStatus;
	private JLabel lblConnectionStatus;
	private JButton btnForward;
	private JButton btnLeft;
	private JButton btnBack;
	private JButton btnRight;
	private JButton btnDriveDistancecm;
	private JButton btnTurnAbsolute;
	private JButton btnTurnRelative;
	private JButton btnSendGyrospeed;
	private JButton btnSendGyrointegral;
	private JButton btnSendMotorspeed;
	private JButton btnSendMotordistance;
	private JButton btnSendConstantRotation;
	private JButton btnSendConstantSpeed;
	private JButton btnSendWheeldiameter;
	private JButton btnSendTrack;
	private JButton btnSendAllParameter;
	private JButton btnSend;
	private JButton btnDriveTo;
	private JCheckBox chckbxAutostatuspacket;
	private JCheckBox chckbxBalancing;
	private boolean wdown=false;
	private boolean adown=false;
	private boolean sdown=false;
	private boolean ddown=false;
	String currentTime="";
	public long motorA=0;
	public long motorB=0;
	private JScrollPane scrollPane;
	private JTextArea Console;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					UI window = new UI();
					applicationHandler.gui = window;
					window.NXTControl.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UI() {
		initialize();
		startMyKeyListener();
		disableButtons();
	}
	
	private void startMyKeyListener(){
		btnForward.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(java.awt.event.KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(java.awt.event.KeyEvent e) {
				if(e.getKeyChar() =='w')
				{
					applicationHandler.stopForwardButton();
					wdown=false;
				}
				else if(e.getKeyChar() =='a')
				{
					applicationHandler.stopLeftButton();
					adown=false;
				}
				else if(e.getKeyChar() =='s')
				{
					applicationHandler.stopBackButton();
					sdown=false;
				}
				else if(e.getKeyChar() =='d')
				{
					applicationHandler.stopRightButton();
					ddown=false;
				}
				else{
					
				}
				
			}
			
			@Override
			public void keyPressed(java.awt.event.KeyEvent e) {
				if(e.getKeyChar() =='w' && wdown ==false)
				{
					applicationHandler.goForwardButton();
					wdown=true;
				}
				else if(e.getKeyChar() =='a' && adown ==false)
				{
					applicationHandler.goLeftButton();
					adown=true;
				}
				else if(e.getKeyChar() =='s' && sdown ==false)
				{
					applicationHandler.goBackButton();
					sdown=true;
				}
				else if(e.getKeyChar() =='d'  && ddown ==false)
				{
					applicationHandler.goRightButton();
					ddown=true;
				}
				else{
					
				}
				
				
			}
		});
	}
	
	public void output(String text){
		Console.append(text+"\n");
	}
	
	public String getGyroSpeedt(){
		return tgyrospeeds.getText();
	}
	
	public String getMotorSpeed(){
		return tmotorspeeds.getText();
	}
	
	public String getMotorDistancet(){
		return tmotordistances.getText();
	}
	
	public String getGyroIntegralt(){
		return tgyrointegrals.getText();
	}
	
	public String getInput(){
		return ConsoleInput.getText();
	}
	
	public String getDriveDistance(){
		return tDriveDistance.getText();
	}
	
	public String getTurnRelative(){
		return tTurnRelative.getText();
	}
	
	public String getTurnAbsolute(){
		return tTurnAbsolute.getText();
	}
	
	public String getConstantSpeed(){
		return tconstantspeeds.getText();
	}
	
	public String getConstantRotation(){
		return tconstantrotations.getText();
	}
	
	public String getDriveToX(){
		return tDriveToX.getText();
	}
	
	public String getDriveToY(){
		return tDriveToY.getText();
	}
	
	
	public void setGyroIntegralt(double paramValue){
		tgyrointegralg.setText(""+paramValue);
	}
	
	public void setMotorSpeedt(double paramValue){
		tmotorspeedg.setText(""+paramValue);
	}
	
	public void setMotorDistancet(double paramValue){
		tmotordistanceg.setText(""+paramValue);
	}
	
	public void setGyroSpeedt(double paramValue){
		tgyrospeedg.setText(""+paramValue);
	}
	
	public void setCurrentPositionLabel(float paramValue1, float paramValue2){
		tCurrentPositionX.setText(""+paramValue1);
		tCurrentPostionY.setText(""+paramValue2);
	}
	
	public String getWheelDiameter()
	{
		return twheeldiameters.toString();
	}
	
	public String getTrack()
	{
		return ttracks.toString();
	}
	
	void setConnectionButtonText(String type)
	{
		btnConnect.setText(type);
	}
	
	
	public void setBatteryLabel(int paramValue){
		tBatteryValtage.setText(""+paramValue + " mV");
		if(paramValue>6000){
			setBatteryLabel(true); 
		}
		else{
			setBatteryLabel(false);
		}
	}
	
	public void setTiltLabel(float paramValue){
		tTilt.setText(""+paramValue + " �");
	}
	
	public void setSpeedometerLabel(float paramValue){
		tSpeedometer.setText(""+paramValue + " cm/s");
	}
	
	public void setRotationLabel(float paramValue){
		tRotation.setText(""+paramValue + " �");
	}
	
	public void setTimeText(String time){
		tConnectionTime.setText(time);
	}
	
	
	
	public void disableButtons(){
		btnForward.setEnabled(false);
		btnLeft.setEnabled(false);
		btnRight.setEnabled(false);
		btnBack.setEnabled(false);
		btnDriveDistancecm.setEnabled(false);
		btnTurnAbsolute.setEnabled(false);
		btnTurnRelative.setEnabled(false);
		btnDriveTo.setEnabled(false);
		btnSendGyrospeed.setEnabled(false);
		btnSendGyrointegral.setEnabled(false);
		btnSendMotorspeed.setEnabled(false);
		btnSendMotordistance.setEnabled(false);
		btnSendWheeldiameter.setEnabled(false);
		btnSendConstantSpeed.setEnabled(false);
		btnSendConstantRotation.setEnabled(false);
		btnSendTrack.setEnabled(false);
		btnSendAllParameter.setEnabled(false);
		btnSend.setEnabled(false);
		chckbxAutostatuspacket.setEnabled(false);
		chckbxBalancing.setEnabled(false);
	}
	
	public void enableButtons(){
		btnForward.setEnabled(true);
		btnLeft.setEnabled(true);
		btnRight.setEnabled(true);
		btnBack.setEnabled(true);
		btnDriveDistancecm.setEnabled(true);
		btnTurnAbsolute.setEnabled(true);
		btnTurnRelative.setEnabled(true);
		btnDriveTo.setEnabled(true);
		btnSendGyrospeed.setEnabled(true);
		btnSendGyrointegral.setEnabled(true);
		btnSendMotorspeed.setEnabled(true);
		btnSendMotordistance.setEnabled(true);
		btnSendWheeldiameter.setEnabled(true);
		btnSendConstantSpeed.setEnabled(true);
		btnSendConstantRotation.setEnabled(true);
		btnSendTrack.setEnabled(true);
		btnSendAllParameter.setEnabled(true);
		btnSend.setEnabled(true);
		chckbxAutostatuspacket.setEnabled(true);
		chckbxBalancing.setEnabled(true);
	}
	
	public void setConnectionLabel(boolean status){
		if(status){
			lblConnectionStatus.setBackground(new Color(0,255,0));
		}
		else{
			lblConnectionStatus.setBackground(new Color(255,0,0));
		}
	}
	
	public void setBatteryLabel(boolean status){
		if(status){
			lblBatteryVoltageStatus.setBackground(new Color(0,255,0));
		}
		else{
			lblBatteryVoltageStatus.setBackground(new Color(255,0,0));
		}
	}
	/*
	static void setCrashWarningLabel(boolean status){
		lblCrashWarning.setVisible(status);
	}
	*/
	public void setAutoStatusPacket(boolean status){
		chckbxAutostatuspacket.setSelected(status);	
		}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		
		
		NXTControl = new JFrame();
		NXTControl.setTitle("NXT Control");
		NXTControl.setResizable(false);
		NXTControl.setBounds(100, 100, 1000, 580);
		NXTControl.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		NXTControl.getContentPane().setBackground(new Color(199, 221, 242));
		NXTControl.getContentPane().setLayout(null);
		
		JLabel lblConnection = new JLabel("Connection");
		lblConnection.setBounds(10, 11, 77, 14);
		NXTControl.getContentPane().add(lblConnection);
		
		
		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				applicationHandler.connectButton();
			}
		});
		btnConnect.setBounds(10, 27, 97, 36);
		NXTControl.getContentPane().add(btnConnect);
		btnConnect.setBackground(new Color(199, 221, 242));
		
		JLabel lblConnectionTime = new JLabel("Connection Time");
		lblConnectionTime.setBounds(132, 11, 121, 14);
		NXTControl.getContentPane().add(lblConnectionTime);
		
		tConnectionTime = new JTextField();
		tConnectionTime.setEnabled(false);
		tConnectionTime.setEditable(false);
		tConnectionTime.setBounds(132, 28, 89, 20);
		NXTControl.getContentPane().add(tConnectionTime);
		tConnectionTime.setColumns(10);
		
		JLabel lblCurrentPosition = new JLabel("Current Position");
		lblCurrentPosition.setBounds(281, 11, 111, 14);
		NXTControl.getContentPane().add(lblCurrentPosition);
		
		JLabel lblX = new JLabel("x:");
		lblX.setBounds(281, 31, 17, 14);
		NXTControl.getContentPane().add(lblX);
		
		JLabel lblY = new JLabel("y:");
		lblY.setBounds(326, 31, 32, 14);
		NXTControl.getContentPane().add(lblY);
		
		tCurrentPositionX = new JTextField();
		tCurrentPositionX.setEnabled(false);
		tCurrentPositionX.setEditable(false);
		tCurrentPositionX.setBounds(291, 28, 32, 20);
		NXTControl.getContentPane().add(tCurrentPositionX);
		tCurrentPositionX.setColumns(10);
		
		tCurrentPostionY = new JTextField();
		tCurrentPostionY.setEnabled(false);
		tCurrentPostionY.setEditable(false);
		tCurrentPostionY.setBounds(336, 28, 32, 20);
		NXTControl.getContentPane().add(tCurrentPostionY);
		tCurrentPostionY.setColumns(10);
		
		chckbxAutostatuspacket = new JCheckBox("AutoStatusPacket");
		chckbxAutostatuspacket.setBounds(132, 55, 155, 23);
		NXTControl.getContentPane().add(chckbxAutostatuspacket);
		chckbxAutostatuspacket.setBackground(new Color(199, 221, 242));
		chckbxAutostatuspacket.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applicationHandler.sendAutostatuspacket(chckbxAutostatuspacket.isSelected());
			}
		});
		
		chckbxBalancing = new JCheckBox("Balancing");
		chckbxBalancing.setBounds(132, 81, 97, 23);
		NXTControl.getContentPane().add(chckbxBalancing);
		chckbxBalancing.setBackground(new Color(199, 221, 242));
		chckbxBalancing.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applicationHandler.sendBalancieren(chckbxBalancing.isSelected());
			}
		});
		
		JLabel lblBatteryVoltage = new JLabel("Battery Voltage");
		lblBatteryVoltage.setBounds(464, 11, 97, 14);
		NXTControl.getContentPane().add(lblBatteryVoltage);
		
		JLabel lblSpeedometer = new JLabel("Speedometer");
		lblSpeedometer.setBounds(464, 38, 97, 14);
		NXTControl.getContentPane().add(lblSpeedometer);
		
		JLabel lblTilt = new JLabel("Tilt");
		lblTilt.setBounds(777, 11, 46, 14);
		NXTControl.getContentPane().add(lblTilt);
		
		JLabel lblRotation = new JLabel("Rotation");
		lblRotation.setBounds(777, 38, 61, 14);
		NXTControl.getContentPane().add(lblRotation);
		
		tBatteryValtage = new JTextField();
		tBatteryValtage.setEnabled(false);
		tBatteryValtage.setEditable(false);
		tBatteryValtage.setBounds(607, 8, 140, 20);
		NXTControl.getContentPane().add(tBatteryValtage);
		tBatteryValtage.setColumns(10);
		
		tSpeedometer = new JTextField();
		tSpeedometer.setEnabled(false);
		tSpeedometer.setEditable(false);
		tSpeedometer.setBounds(607, 33, 140, 20);
		NXTControl.getContentPane().add(tSpeedometer);
		tSpeedometer.setColumns(10);
		
		tTilt = new JTextField();
		tTilt.setEnabled(false);
		tTilt.setEditable(false);
		tTilt.setBounds(844, 8, 140, 20);
		NXTControl.getContentPane().add(tTilt);
		tTilt.setColumns(10);
		
		tRotation = new JTextField();
		tRotation.setEnabled(false);
		tRotation.setEditable(false);
		tRotation.setBounds(844, 33, 140, 20);
		NXTControl.getContentPane().add(tRotation);
		tRotation.setColumns(10);
		
		JLabel lblCommunication = new JLabel("Communication");
		lblCommunication.setBounds(748, 119, 97, 14);
		NXTControl.getContentPane().add(lblCommunication);
		
		ConsoleInput = new JTextField();
		ConsoleInput.setBounds(615, 524, 279, 20);
		NXTControl.getContentPane().add(ConsoleInput);
		ConsoleInput.setColumns(10);
		
		btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applicationHandler.sendCommandButton();
				//output(ConsoleInput.getText());
			}
		});
		btnSend.setBounds(904, 523, 80, 23);
		NXTControl.getContentPane().add(btnSend);
		btnSend.setBackground(new Color(199, 221, 242));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 119, 594, 422);
		NXTControl.getContentPane().add(tabbedPane);
		tabbedPane.setBackground(new Color(199, 221, 242));
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Position", null, panel, null);
		panel.setLayout(null);
		panel.setBackground(new Color(199, 221, 242));
		
		tDriveDistance = new JTextField();
		tDriveDistance.setBounds(28, 11, 120, 20);
		panel.add(tDriveDistance);
		tDriveDistance.setColumns(10);
		
		tTurnAbsolute = new JTextField();
		tTurnAbsolute.setColumns(10);
		tTurnAbsolute.setBounds(28, 42, 120, 20);
		panel.add(tTurnAbsolute);
		
		tTurnRelative = new JTextField();
		tTurnRelative.setColumns(10);
		tTurnRelative.setBounds(28, 73, 120, 20);
		panel.add(tTurnRelative);
		
		btnDriveDistancecm = new JButton("drive distance (cm)");
		btnDriveDistancecm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applicationHandler.driveDistanceButton();
			}
		});
		btnDriveDistancecm.setBounds(158, 10, 162, 23);
		panel.add(btnDriveDistancecm);
		btnDriveDistancecm.setBackground(new Color(199, 221, 242));
		
		btnTurnAbsolute = new JButton("turn absolute");
		btnTurnAbsolute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applicationHandler.turnAbsoluteButton();
			}
		});
		btnTurnAbsolute.setBounds(158, 41, 162, 23);
		panel.add(btnTurnAbsolute);
		btnTurnAbsolute.setBackground(new Color(199, 221, 242));
		
		btnTurnRelative = new JButton("turn relative");
		btnTurnRelative.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applicationHandler.turnRelativeButton();
			}
		});
		btnTurnRelative.setBounds(158, 72, 162, 23);
		panel.add(btnTurnRelative);
		btnTurnRelative.setBackground(new Color(199, 221, 242));
		
		btnForward = new JButton("Forward");
		btnForward.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applicationHandler.goForwardButton();
			}
		});
		btnForward.setBounds(120, 133, 82, 82);
		panel.add(btnForward);
		btnForward.setBackground(new Color(199, 221, 242));
		
		btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applicationHandler.goBackButton();
			}
		});
		btnBack.setBounds(120, 226, 82, 82);
		panel.add(btnBack);
		btnBack.setBackground(new Color(199, 221, 242));
		
		btnLeft = new JButton("Left");
		btnLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applicationHandler.goLeftButton();
			}
		});
		btnLeft.setBounds(28, 226, 82, 82);
		panel.add(btnLeft);
		btnLeft.setBackground(new Color(199, 221, 242));
		
		btnRight = new JButton("Right");
		btnRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applicationHandler.goRightButton();
			}
		});
		btnRight.setBounds(212, 226, 82, 82);
		panel.add(btnRight);
		btnRight.setBackground(new Color(199, 221, 242));
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Parameter", null, panel_1, null);
		panel_1.setLayout(null);
		panel_1.setBackground(new Color(199, 221, 242));
		
		tgyrospeeds = new JTextField();
		tgyrospeeds.setColumns(10);
		tgyrospeeds.setBounds(10, 11, 120, 20);
		panel_1.add(tgyrospeeds);
		
		btnSendGyrospeed = new JButton("send gyrospeed");
		btnSendGyrospeed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applicationHandler.sendGyroSpeedButton();
			}
		});
		btnSendGyrospeed.setBounds(140, 10, 165, 23);
		panel_1.add(btnSendGyrospeed);
		btnSendGyrospeed.setBackground(new Color(199, 221, 242));
		
		tgyrointegrals = new JTextField();
		tgyrointegrals.setColumns(10);
		tgyrointegrals.setBounds(10, 42, 120, 20);
		panel_1.add(tgyrointegrals);
		
		tmotorspeeds = new JTextField();
		tmotorspeeds.setColumns(10);
		tmotorspeeds.setBounds(10, 73, 120, 20);
		panel_1.add(tmotorspeeds);
		
		tmotordistances = new JTextField();
		tmotordistances.setColumns(10);
		tmotordistances.setBounds(10, 104, 120, 20);
		panel_1.add(tmotordistances);
		
		tconstantrotations = new JTextField();
		tconstantrotations.setColumns(10);
		tconstantrotations.setBounds(10, 135, 120, 20);
		panel_1.add(tconstantrotations);
		
		tconstantspeeds = new JTextField();
		tconstantspeeds.setColumns(10);
		tconstantspeeds.setBounds(10, 166, 120, 20);
		panel_1.add(tconstantspeeds);
		
		twheeldiameters = new JComboBox();
		twheeldiameters.setBounds(10, 197, 120, 20);
		panel_1.add(twheeldiameters);
		twheeldiameters.addItem("5.6");
		twheeldiameters.addItem("12");
		
		ttracks = new JComboBox();
		ttracks.setBounds(10, 228, 120, 20);
		panel_1.add(ttracks);
		ttracks.addItem("inside");
		ttracks.addItem("outside");
		
		btnSendGyrointegral = new JButton("send gyrointegral");
		btnSendGyrointegral.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applicationHandler.sendGyroIntegralButton();
			}
		});
		btnSendGyrointegral.setBounds(140, 41, 165, 23);
		panel_1.add(btnSendGyrointegral);
		btnSendGyrointegral.setBackground(new Color(199, 221, 242));
		
		btnSendMotorspeed = new JButton("send motorspeed");
		btnSendMotorspeed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applicationHandler.sendMotorSpeedButton();
			}
		});
		btnSendMotorspeed.setBounds(140, 72, 165, 23);
		panel_1.add(btnSendMotorspeed);
		btnSendMotorspeed.setBackground(new Color(199, 221, 242));
		
		btnSendMotordistance = new JButton("send motordistance");
		btnSendMotordistance.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applicationHandler.sendMotorDistanceButton();
			}
		});
		btnSendMotordistance.setBounds(140, 103, 165, 23);
		panel_1.add(btnSendMotordistance);
		btnSendMotordistance.setBackground(new Color(199, 221, 242));
		
		btnSendConstantRotation = new JButton("send constant rotation");
		btnSendConstantRotation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applicationHandler.sendConstantRotationButton();
			}
		});
		btnSendConstantRotation.setBounds(140, 134, 165, 23);
		panel_1.add(btnSendConstantRotation);
		btnSendConstantRotation.setBackground(new Color(199, 221, 242));
		
		btnSendConstantSpeed = new JButton("send constant speed");
		btnSendConstantSpeed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applicationHandler.sendConstantSpeedButton();
			}
		});
		btnSendConstantSpeed.setBounds(140, 165, 165, 23);
		panel_1.add(btnSendConstantSpeed);
		btnSendConstantSpeed.setBackground(new Color(199, 221, 242));
		
		btnSendWheeldiameter = new JButton("send wheeldiameter");
		btnSendWheeldiameter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applicationHandler.sendWheeldiameterButton();
			}
		});
		btnSendWheeldiameter.setBounds(140, 196, 165, 23);
		panel_1.add(btnSendWheeldiameter);
		btnSendWheeldiameter.setBackground(new Color(199, 221, 242));
		
		btnSendTrack = new JButton("send track");
		btnSendTrack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applicationHandler.sendTrackButton();
			}
		});
		btnSendTrack.setBounds(140, 227, 165, 23);
		panel_1.add(btnSendTrack);
		btnSendTrack.setBackground(new Color(199, 221, 242));
		
		btnSendAllParameter = new JButton("send all parameter");
		btnSendAllParameter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applicationHandler.sendAllButton();
			}
		});
		btnSendAllParameter.setBounds(10, 259, 269, 43);
		panel_1.add(btnSendAllParameter);
		btnSendAllParameter.setBackground(new Color(199, 221, 242));
		
		tgyrospeedg = new JTextField();
		tgyrospeedg.setEnabled(false);
		tgyrospeedg.setEditable(false);
		tgyrospeedg.setColumns(10);
		tgyrospeedg.setBounds(315, 11, 120, 20);
		panel_1.add(tgyrospeedg);
		
		tgyrointegralg = new JTextField();
		tgyrointegralg.setEnabled(false);
		tgyrointegralg.setEditable(false);
		tgyrointegralg.setColumns(10);
		tgyrointegralg.setBounds(315, 42, 120, 20);
		panel_1.add(tgyrointegralg);
		
		tmotorspeedg = new JTextField();
		tmotorspeedg.setEnabled(false);
		tmotorspeedg.setEditable(false);
		tmotorspeedg.setColumns(10);
		tmotorspeedg.setBounds(315, 73, 120, 20);
		panel_1.add(tmotorspeedg);
		
		tmotordistanceg = new JTextField();
		tmotordistanceg.setEnabled(false);
		tmotordistanceg.setEditable(false);
		tmotordistanceg.setColumns(10);
		tmotordistanceg.setBounds(315, 104, 120, 20);
		panel_1.add(tmotordistanceg);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Navigation", null, panel_2, null);
		panel_2.setLayout(null);
		panel_2.setBackground(new Color(199, 221, 242));
		
		JLabel label = new JLabel("x:");
		label.setBounds(10, 14, 16, 14);
		panel_2.add(label);
		
		JLabel label_1 = new JLabel("y:");
		label_1.setBounds(84, 14, 16, 14);
		panel_2.add(label_1);
		
		tDriveToX = new JTextField();
		tDriveToX.setColumns(10);
		tDriveToX.setBounds(28, 11, 50, 20);
		panel_2.add(tDriveToX);
		
		tDriveToY = new JTextField();
		tDriveToY.setColumns(10);
		tDriveToY.setBounds(98, 11, 50, 20);
		panel_2.add(tDriveToY);
		
		btnDriveTo = new JButton("drive to");
		btnDriveTo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applicationHandler.driveToButton();
			}
		});
		btnDriveTo.setBounds(158, 10, 89, 23);
		panel_2.add(btnDriveTo);
		btnDriveTo.setBackground(new Color(199, 221, 242));
		
		lblConnectionStatus = new JLabel("");
		lblConnectionStatus.setBackground(new Color(255, 0, 0));
		lblConnectionStatus.setBounds(81, 11, 14, 14);
		NXTControl.getContentPane().add(lblConnectionStatus);
		lblConnectionStatus.setOpaque(true);
		
		lblBatteryVoltageStatus = new JLabel("");
		lblBatteryVoltageStatus.setOpaque(true);
		lblBatteryVoltageStatus.setBackground(Color.RED);
		lblBatteryVoltageStatus.setBounds(568, 11, 14, 14);
		NXTControl.getContentPane().add(lblBatteryVoltageStatus);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(615, 140, 369, 377);
		NXTControl.getContentPane().add(scrollPane);
		
		Console = new JTextArea();
		Console.setEnabled(false);
		Console.setEditable(false);
		scrollPane.setViewportView(Console);
		

	
	}
}
