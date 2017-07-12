package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.util.TimeZone;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


/**
 * @author Christian, Fabian, Robin
 */

public class UI implements UserInterface
{
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
	private JTextField tSetPosX;
	private JTextField tSetPosY;
	private JTextField tSetHeading;
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
	private JComboBox<String> twheeldiameters;
	private JComboBox<String> ttracks;
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
	private JButton btnSetPos;
	private JButton btnSetHeading;
	private JCheckBox chckbxAutostatuspacket;
	private JCheckBox chckbxBalancing;
	JCheckBox chkGamepad;
	private JScrollPane scrollPane;
	private JTextArea Console;
	private final DateFormat timeFormat = DateFormat.getTimeInstance();

	private final ApplicationHandler applicationHandler;
	private JTextField tEvoAlgGI;
	private JTextField tEvoAlgGS;
	private JTextField tEvoAlgMS;
	private JTextField tEvoAlgMD;
	private JTextField tEvoAlgProcessing;
	private DrawingPanel panel_4;
	

	static { // Set look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
			throw new Error(ex);
		}
	}

	/**
	 * Create the application.
	 */
	public UI() {
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		applicationHandler = new ApplicationHandler(this, new Send(this));
		initialize();
		showConnected(false);

		
	}
	


	@Override
	public void showMessage(String text) {
		Console.append(text + "\n");
	}

	public String getGyroSpeedt() {
		return tgyrospeeds.getText();
	}

	public String getMotorSpeed() {
		return tmotorspeeds.getText();
	}

	public String getMotorDistancet() {
		return tmotordistances.getText();
	}

	public String getGyroIntegralt() {
		return tgyrointegrals.getText();
	}

	public String getInput() {
		return ConsoleInput.getText();
	}

	public String getDriveDistance() {
		return tDriveDistance.getText();
	}

	public String getTurnRelative() {
		return tTurnRelative.getText();
	}

	public String getTurnAbsolute() {
		return tTurnAbsolute.getText();
	}

	public String getConstantSpeed() {
		return tconstantspeeds.getText();
	}

	public String getConstantRotation() {
		return tconstantrotations.getText();
	}

	public String getDriveToX() {
		return tDriveToX.getText();
	}

	public String getDriveToY() {
		return tDriveToY.getText();
	}
	
	public void setEvoAlgGI(float paramValue){
		tEvoAlgGI.setText(""+paramValue);
	}
	
	public void setEvoAlgGS(float paramValue){
		tEvoAlgGS.setText(""+paramValue);
	}
	
	public void setEvoAlgMD(float paramValue){
		 tEvoAlgMD.setText(""+paramValue);
	}

	public void setEvoAlgMS(float paramValue){
		tEvoAlgMS.setText(""+paramValue);
	}
	
	public void setEvoAlgProcessing(String text){
		tEvoAlgProcessing.setText(text);
	}
	
	@Override
	public void showGyroIntegralWeight(double paramValue) {
		tgyrointegralg.setText("" + paramValue);
	}

	@Override
	public void showMotorSpeedWeight(double speed) {
		tmotorspeedg.setText("" + speed);
	}

	@Override
	public void showMotorDistanceWeight(double distance) {
		tmotordistanceg.setText("" + distance);
	}

	@Override
	public void showGyroSpeedWeight(double gyroSpeed) {
		tgyrospeedg.setText("" + gyroSpeed);
	}

	@Override
	public void showPosition(float x, float y) {
		tCurrentPositionX.setText("" + x);
		tCurrentPostionY.setText("" + y);
	}

	public String getWheelDiameter() {
		return twheeldiameters.toString();
	}

	public String getTrack() {
		return ttracks.toString();
	}
	
	public String getSetHeading(){
		return tSetHeading.getText();
	}
	
	public String getSetPositionX(){
		return tSetPosX.getText();
	}
	
	public String getSetPositionY(){
		return tSetPosY.getText();
	}
	
	public void drawPosition(int x, int y){
		panel_4.setXY(x,y);
		panel_4.repaint();
	}
	
	public void drawPosition(int x, int y, float heading){
		panel_4.setXY(x,y);
		panel_4.setHeading(heading);
		panel_4.repaint();
	}
	
	public void drawNewBarrier(float heading, float distance){
		panel_4.newObstacle(heading, distance);
		panel_4.repaint();
		
	}

	public void drawNewBarrierPoint(float heading, float distance){
		panel_4.newObstaclePoint(heading, distance);
		panel_4.repaint();
		
	}
	
	@Override
	public void showBatteryVoltage(int mV) {
		tBatteryValtage.setText("" + mV + " mV");
		if (mV > 6000) {
			setBatteryLabel(true);
		} else {
			setBatteryLabel(false);
		}
	}

	@Override
	public void showTilt(float angle) {
		tTilt.setText(String.format("%.3f°", angle));
	}

	@Override
	public void showSpeed(float speed) {
		tSpeedometer.setText(String.format("%.3f cm/s", speed));
	}

	@Override
	public void showHeading(float heading) {
		tRotation.setText(String.format("%.3f°", heading));
	}

	@Override
	public void showConnectionTime(long time) {
		tConnectionTime.setText(timeFormat.format(time));
	}

	private void setButtonsEnabled(boolean enabled) {
		btnForward.setEnabled(enabled);
		btnLeft.setEnabled(enabled);
		btnRight.setEnabled(enabled);
		btnBack.setEnabled(enabled);
		btnDriveDistancecm.setEnabled(enabled);
		btnTurnAbsolute.setEnabled(enabled);
		btnTurnRelative.setEnabled(enabled);
		btnDriveTo.setEnabled(enabled);
		btnSetPos.setEnabled(enabled);
		btnSetHeading.setEnabled(enabled);
		btnSendGyrospeed.setEnabled(enabled);
		btnSendGyrointegral.setEnabled(enabled);
		btnSendMotorspeed.setEnabled(enabled);
		btnSendMotordistance.setEnabled(enabled);
		btnSendWheeldiameter.setEnabled(enabled);
		btnSendConstantSpeed.setEnabled(enabled);
		btnSendConstantRotation.setEnabled(enabled);
		btnSendTrack.setEnabled(enabled);
		btnSendAllParameter.setEnabled(enabled);
		btnSend.setEnabled(enabled);
		chckbxAutostatuspacket.setEnabled(enabled);
		chckbxBalancing.setEnabled(enabled);
		chkGamepad.setEnabled(enabled);
	}

	private void clearLabels() {
		tBatteryValtage.setText("");
		tConnectionTime.setText("");
		tCurrentPositionX.setText("");
		tCurrentPostionY.setText("");
		tSpeedometer.setText("");
		tTilt.setText("");
		tRotation.setText("");
	}

	@Override
	public void showConnected(boolean connected) {
		setButtonsEnabled(connected);
		lblConnectionStatus.setBackground(connected ? new Color(0, 255, 0) : new Color(255, 0, 0));
		btnConnect.setText(connected ? "Disconnect" : "Connect");
		showAutoStatusPacketEnabled(connected);
		if (!connected) {
			clearLabels();
		}
	}

	public void setBatteryLabel(boolean status) {
		if (status) {
			lblBatteryVoltageStatus.setBackground(new Color(0, 255, 0));
		} else {
			lblBatteryVoltageStatus.setBackground(new Color(255, 0, 0));
		}
	}

	@Override
	public void showAutoStatusPacketEnabled(boolean status) {
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

		final JLabel lblConnection = new JLabel("Connection");
		lblConnection.setBounds(10, 11, 77, 14);
		NXTControl.getContentPane().add(lblConnection);

		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(e -> applicationHandler.connectButton());
		btnConnect.setBounds(10, 27, 97, 36);
		NXTControl.getContentPane().add(btnConnect);
		btnConnect.setBackground(new Color(199, 221, 242));

		final JLabel lblConnectionTime = new JLabel("Connection Time");
		lblConnectionTime.setBounds(132, 11, 121, 14);
		NXTControl.getContentPane().add(lblConnectionTime);

		tConnectionTime = new JTextField();
		tConnectionTime.setEnabled(false);
		tConnectionTime.setEditable(false);
		tConnectionTime.setBounds(132, 28, 89, 20);
		NXTControl.getContentPane().add(tConnectionTime);
		tConnectionTime.setColumns(10);

		final JLabel lblCurrentPosition = new JLabel("Current Position");
		lblCurrentPosition.setBounds(281, 11, 111, 14);
		NXTControl.getContentPane().add(lblCurrentPosition);

		final JLabel lblX = new JLabel("x:");
		lblX.setBounds(281, 31, 17, 14);
		NXTControl.getContentPane().add(lblX);

		final JLabel lblY = new JLabel("y:");
		lblY.setBounds(281, 59, 32, 14);
		NXTControl.getContentPane().add(lblY);

		tCurrentPositionX = new JTextField();
		tCurrentPositionX.setEnabled(false);
		tCurrentPositionX.setEditable(false);
		tCurrentPositionX.setBounds(291, 27, 77, 20);
		NXTControl.getContentPane().add(tCurrentPositionX);
		tCurrentPositionX.setColumns(10);

		tCurrentPostionY = new JTextField();
		tCurrentPostionY.setEnabled(false);
		tCurrentPostionY.setEditable(false);
		tCurrentPostionY.setBounds(291, 56, 77, 20);
		NXTControl.getContentPane().add(tCurrentPostionY);
		tCurrentPostionY.setColumns(10);

		chckbxAutostatuspacket = new JCheckBox("AutoStatusPacket");
		chckbxAutostatuspacket.setBounds(132, 55, 155, 23);
		NXTControl.getContentPane().add(chckbxAutostatuspacket);
		chckbxAutostatuspacket.setBackground(new Color(199, 221, 242));
		chckbxAutostatuspacket.addActionListener(e -> applicationHandler.sendAutostatuspacket(chckbxAutostatuspacket.isSelected()));

		chckbxBalancing = new JCheckBox("Balancing");
		chckbxBalancing.setBounds(132, 81, 97, 23);
		NXTControl.getContentPane().add(chckbxBalancing);
		chckbxBalancing.setBackground(new Color(199, 221, 242));
		chckbxBalancing.addActionListener(e -> applicationHandler.sendBalancieren(chckbxBalancing.isSelected()));

		final JLabel lblBatteryVoltage = new JLabel("Battery Voltage");
		lblBatteryVoltage.setBounds(464, 11, 97, 14);
		NXTControl.getContentPane().add(lblBatteryVoltage);

		final JLabel lblSpeedometer = new JLabel("Speedometer");
		lblSpeedometer.setBounds(464, 38, 97, 14);
		NXTControl.getContentPane().add(lblSpeedometer);

		final JLabel lblTilt = new JLabel("Tilt");
		lblTilt.setBounds(777, 11, 46, 14);
		NXTControl.getContentPane().add(lblTilt);

		final JLabel lblRotation = new JLabel("Rotation");
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

		final JLabel lblCommunication = new JLabel("Communication");
		lblCommunication.setBounds(748, 119, 97, 14);
		NXTControl.getContentPane().add(lblCommunication);

		ConsoleInput = new JTextField();
		ConsoleInput.setBounds(615, 524, 279, 20);
		NXTControl.getContentPane().add(ConsoleInput);
		ConsoleInput.setColumns(10);

		btnSend = new JButton("Send");
		btnSend.addActionListener(e -> applicationHandler.sendCommandButton());
		btnSend.setBounds(904, 523, 80, 23);
		NXTControl.getContentPane().add(btnSend);
		btnSend.setBackground(new Color(199, 221, 242));

		final JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
		tabbedPane.setBounds(10, 119, 594, 422);
		NXTControl.getContentPane().add(tabbedPane);
		tabbedPane.setBackground(new Color(199, 221, 242));

		final JPanel panel = new JPanel();
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
		btnDriveDistancecm.addActionListener(e -> applicationHandler.driveDistanceButton());
		btnDriveDistancecm.setBounds(158, 10, 162, 23);
		panel.add(btnDriveDistancecm);
		btnDriveDistancecm.setBackground(new Color(199, 221, 242));

		btnTurnAbsolute = new JButton("turn absolute");
		btnTurnAbsolute.addActionListener(e -> applicationHandler.turnAbsoluteButton());
		btnTurnAbsolute.setBounds(158, 41, 162, 23);
		panel.add(btnTurnAbsolute);
		btnTurnAbsolute.setBackground(new Color(199, 221, 242));

		btnTurnRelative = new JButton("turn relative");
		btnTurnRelative.addActionListener(e -> applicationHandler.turnRelativeButton());
		btnTurnRelative.setBounds(158, 72, 162, 23);
		panel.add(btnTurnRelative);
		btnTurnRelative.setBackground(new Color(199, 221, 242));

		btnForward = new JButton("Forward");
		btnForward.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				applicationHandler.moveForward();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				applicationHandler.stopMoving();
			}
		});
		btnForward.setBounds(120, 133, 82, 82);
		panel.add(btnForward);
		btnForward.setBackground(new Color(199, 221, 242));

		btnBack = new JButton("Back");
		btnBack.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				applicationHandler.moveBackward();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				applicationHandler.stopMoving();
			}
		});
		btnBack.setBounds(120, 226, 82, 82);
		panel.add(btnBack);
		btnBack.setBackground(new Color(199, 221, 242));

		btnLeft = new JButton("Left");
		btnLeft.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				applicationHandler.turnLeft();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				applicationHandler.stopTurning();
			}
		});
		btnLeft.setBounds(28, 226, 82, 82);
		panel.add(btnLeft);
		btnLeft.setBackground(new Color(199, 221, 242));

		btnRight = new JButton("Right");
		btnRight.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				applicationHandler.turnRight();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				applicationHandler.stopTurning();
			}
		});
		btnRight.setBounds(212, 226, 82, 82);
		panel.add(btnRight);
		btnRight.setBackground(new Color(199, 221, 242));

		chkGamepad = new JCheckBox("Gamepad");
		chkGamepad.setBounds(480, 360, 90, 20);
		chkGamepad.addActionListener(a -> applicationHandler.gamepadControl(chkGamepad.isSelected()));
		panel.add(chkGamepad);
		chkGamepad.setBackground(new Color(199, 221, 242));

		{// Navigation by keys
			final InputMap inputMap = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
			final ActionMap actionMap = panel.getActionMap();

			inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), "forward_go");
			inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), "moving_stop");
			inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "forward_go");
			inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true), "moving_stop");
			inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), "left_go");
			inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), "turning_stop");
			inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "left_go");
			inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), "turning_stop");
			inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "backward_go");
			inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), "moving_stop");
			inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), "backward_go");
			inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "moving_stop");
			inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), "right_go");
			inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), "turning_stop");
			inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "right_go");
			inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), "turning_stop");

			actionMap.put("forward_go", new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (!(NXTControl.getFocusOwner() instanceof JTextArea || NXTControl.getFocusOwner() instanceof JTextField)) {
						applicationHandler.moveForward();
					}
				}
			});

			actionMap.put("moving_stop", new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (!(NXTControl.getFocusOwner() instanceof JTextArea || NXTControl.getFocusOwner() instanceof JTextField)) {
						applicationHandler.stopMoving();
					}
				}
			});

			actionMap.put("left_go", new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (!(NXTControl.getFocusOwner() instanceof JTextArea || NXTControl.getFocusOwner() instanceof JTextField)) {
						applicationHandler.turnLeft();
					}
				}
			});

			actionMap.put("turning_stop", new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (!(NXTControl.getFocusOwner() instanceof JTextArea || NXTControl.getFocusOwner() instanceof JTextField)) {
						applicationHandler.stopTurning();
					}
				}
			});

			actionMap.put("backward_go", new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (!(NXTControl.getFocusOwner() instanceof JTextArea || NXTControl.getFocusOwner() instanceof JTextField)) {
						applicationHandler.moveBackward();
					}
				}
			});

			actionMap.put("right_go", new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (!(NXTControl.getFocusOwner() instanceof JTextArea || NXTControl.getFocusOwner() instanceof JTextField)) {
						applicationHandler.turnRight();
					}
				}
			});
		}

		final JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Parameter", null, panel_1, null);
		panel_1.setLayout(null);
		panel_1.setBackground(new Color(199, 221, 242));

		tgyrospeeds = new JTextField();
		tgyrospeeds.setColumns(10);
		tgyrospeeds.setBounds(10, 11, 120, 20);
		panel_1.add(tgyrospeeds);

		btnSendGyrospeed = new JButton("send gyrospeed");
		btnSendGyrospeed.addActionListener(e -> applicationHandler.sendGyroSpeedButton());
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

		twheeldiameters = new JComboBox<>();
		twheeldiameters.setBounds(10, 197, 120, 20);
		panel_1.add(twheeldiameters);
		twheeldiameters.addItem("5.6");
		twheeldiameters.addItem("12");

		ttracks = new JComboBox<>();
		ttracks.setBounds(10, 228, 120, 20);
		panel_1.add(ttracks);
		ttracks.addItem("inside");
		ttracks.addItem("outside");

		btnSendGyrointegral = new JButton("send gyrointegral");
		btnSendGyrointegral.addActionListener(e -> applicationHandler.sendGyroIntegralButton());
		btnSendGyrointegral.setBounds(140, 41, 165, 23);
		panel_1.add(btnSendGyrointegral);
		btnSendGyrointegral.setBackground(new Color(199, 221, 242));

		btnSendMotorspeed = new JButton("send motorspeed");
		btnSendMotorspeed.addActionListener(e -> applicationHandler.sendMotorSpeedButton());
		btnSendMotorspeed.setBounds(140, 72, 165, 23);
		panel_1.add(btnSendMotorspeed);
		btnSendMotorspeed.setBackground(new Color(199, 221, 242));

		btnSendMotordistance = new JButton("send motordistance");
		btnSendMotordistance.addActionListener(e -> applicationHandler.sendMotorDistanceButton());
		btnSendMotordistance.setBounds(140, 103, 165, 23);
		panel_1.add(btnSendMotordistance);
		btnSendMotordistance.setBackground(new Color(199, 221, 242));

		btnSendConstantRotation = new JButton("send constant rotation");
		btnSendConstantRotation.addActionListener(e -> applicationHandler.sendConstantRotationButton());
		btnSendConstantRotation.setBounds(140, 134, 165, 23);
		panel_1.add(btnSendConstantRotation);
		btnSendConstantRotation.setBackground(new Color(199, 221, 242));

		btnSendConstantSpeed = new JButton("send constant speed");
		btnSendConstantSpeed.addActionListener(e -> applicationHandler.sendConstantSpeedButton());
		btnSendConstantSpeed.setBounds(140, 165, 165, 23);
		panel_1.add(btnSendConstantSpeed);
		btnSendConstantSpeed.setBackground(new Color(199, 221, 242));

		btnSendWheeldiameter = new JButton("send wheeldiameter");
		btnSendWheeldiameter.addActionListener(e -> applicationHandler.sendWheeldiameterButton());
		btnSendWheeldiameter.setBounds(140, 196, 165, 23);
		panel_1.add(btnSendWheeldiameter);
		btnSendWheeldiameter.setBackground(new Color(199, 221, 242));

		btnSendTrack = new JButton("send track");
		btnSendTrack.addActionListener(e -> applicationHandler.sendTrackButton());
		btnSendTrack.setBounds(140, 227, 165, 23);
		panel_1.add(btnSendTrack);
		btnSendTrack.setBackground(new Color(199, 221, 242));

		btnSendAllParameter = new JButton("send all parameter");
		btnSendAllParameter.addActionListener(e -> applicationHandler.sendAllButton());
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

		final JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Navigation", null, panel_2, null);
		panel_2.setLayout(null);
		panel_2.setBackground(new Color(199, 221, 242));

		final JLabel label = new JLabel("x:");
		label.setBounds(10, 14, 16, 14);
		panel_2.add(label);

		final JLabel label_1 = new JLabel("y:");
		label_1.setBounds(84, 14, 16, 14);
		panel_2.add(label_1);
		
		final JLabel lblSetPosX = new JLabel("x:");
		lblSetPosX.setBounds(10, 43, 16, 14);
		panel_2.add(lblSetPosX);

		final JLabel lblSetPosY = new JLabel("y:");
		lblSetPosY.setBounds(84, 43, 16, 14);
		panel_2.add(lblSetPosY);

		tDriveToX = new JTextField();
		tDriveToX.setColumns(10);
		tDriveToX.setBounds(28, 11, 50, 20);
		panel_2.add(tDriveToX);

		tDriveToY = new JTextField();
		tDriveToY.setColumns(10);
		tDriveToY.setBounds(98, 11, 50, 20);
		panel_2.add(tDriveToY);

		btnDriveTo = new JButton("drive to");
		btnDriveTo.addActionListener(e -> applicationHandler.driveToButton());
		btnDriveTo.setBounds(158, 10, 89, 23);
		panel_2.add(btnDriveTo);
		btnDriveTo.setBackground(new Color(199, 221, 242));
		
		tSetPosX = new JTextField();
		tSetPosX.setColumns(10);
		tSetPosX.setBounds(28, 40, 50, 20);
		panel_2.add(tSetPosX);

		tSetPosY = new JTextField();
		tSetPosY.setColumns(10);
		tSetPosY.setBounds(98, 40, 50, 20);
		panel_2.add(tSetPosY);
		
		tSetHeading = new JTextField();
		tSetHeading.setColumns(10);
		tSetHeading.setBounds(28, 70, 50, 20);
		panel_2.add(tSetHeading);
		
		btnSetHeading = new JButton("set heading");
		btnSetHeading.addActionListener(e -> applicationHandler.setHeadingButton());
		btnSetHeading.setBounds(158, 70, 89, 23);
		panel_2.add(btnSetHeading);
		btnSetHeading.setBackground(new Color(199, 221, 242));

		btnSetPos= new JButton("set position");
		btnSetPos.addActionListener(e -> applicationHandler.setPositionButton());
		btnSetPos.setBounds(158, 40, 89, 23);
		panel_2.add(btnSetPos);
		btnSetPos.setBackground(new Color(199, 221, 242));
		
		panel_4 = new DrawingPanel(applicationHandler.getNavigator());
		panel_4.setBounds(10, 99, 550, 270);
		panel_2.add(panel_4);
		panel_4.setBackground(new Color(142,186,229));
		
		
		final JPanel panel_3 = new JPanel();
		tabbedPane.addTab("EvoAlg.", null, panel_3, null);
		panel_3.setLayout(null);
		panel_3.setBackground(new Color(199, 221, 242));
		
		JButton btnStartEvoAlg = new JButton("Start");
		btnStartEvoAlg.setBounds(17, 6, 97, 29);
		panel_3.add(btnStartEvoAlg);
		btnStartEvoAlg.addActionListener(e -> applicationHandler.startEvoAlgButton());
		
		
		JLabel lblGyrointegral = new JLabel("GyroIntegral:");
		lblGyrointegral.setBounds(26, 40, 88, 16);
		panel_3.add(lblGyrointegral);
		
		tEvoAlgGI = new JTextField();
		tEvoAlgGI.setBounds(120, 36, 130, 26);
		panel_3.add(tEvoAlgGI);
		tEvoAlgGI.setColumns(10);
		
		JLabel lblGyrospeed = new JLabel("GyroSpeed:");
		lblGyrospeed.setBounds(26, 70, 88, 16);
		panel_3.add(lblGyrospeed);
		
		tEvoAlgGS = new JTextField();
		tEvoAlgGS.setColumns(10);
		tEvoAlgGS.setBounds(120, 66, 130, 26);
		panel_3.add(tEvoAlgGS);
		
		JLabel lblMotorspeed = new JLabel("MotorSpeed:");
		lblMotorspeed.setBounds(26, 100, 88, 16);
		panel_3.add(lblMotorspeed);
		
		tEvoAlgMS = new JTextField();
		tEvoAlgMS.setColumns(10);
		tEvoAlgMS.setBounds(120, 96, 130, 26);
		panel_3.add(tEvoAlgMS);
		
		JLabel lblMotordistance = new JLabel("MotorDistance:");
		lblMotordistance.setBounds(26, 130, 97, 16);
		panel_3.add(lblMotordistance);
		
		tEvoAlgMD = new JTextField();
		tEvoAlgMD.setColumns(10);
		tEvoAlgMD.setBounds(120, 126, 130, 26);
		panel_3.add(tEvoAlgMD);
		
		JLabel lblProcessing = new JLabel("Processing:");
		lblProcessing.setBounds(26, 200, 88, 16);
		panel_3.add(lblProcessing);
		
		tEvoAlgProcessing = new JTextField();
		tEvoAlgProcessing.setBounds(120, 196, 130, 26);
		panel_3.add(tEvoAlgProcessing);
		tEvoAlgProcessing.setColumns(10);
		
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

		// Listen for window close and close connection
		NXTControl.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (applicationHandler.isConnected()) {
					applicationHandler.sendBalancieren(false);
					applicationHandler.disconnect();
				}
			}
		});
	}

	@Override
	public void showTachoLeft(long tacho) {
		// Ignore
	}

	@Override
	public void showTachoRight(long tacho) {
		// Ignore
	}

	@Override
	public void showBalancingEnabled(boolean enabled) {
		chckbxBalancing.setSelected(enabled);
	}

	@Override
	public void show() {
		NXTControl.setVisible(true);
	}

	
}
