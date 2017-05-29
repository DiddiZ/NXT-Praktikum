package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.default_package;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.swt.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Composite;
import java.awt.Frame;
import org.eclipse.swt.awt.SWT_AWT;
import java.awt.Panel;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JRootPane;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Combo;

public class application {

	protected Shell shlNxtControl;
	private Button btnVor;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	static Text txtNewText;
	static Text text;
	static Text text_1;
	static Text text_2;
	private Text sgyrospeedt;
	private Text sgyrointegralt;
	private Text smotorspeedt;
	private Text smotordistancet;
	private Text sdistancetargett;
	private Text srotationtargett;
	static Text akkuspannungt;
	static Text neigungt;
	static Text motorat;
	private Text motorbt;
	private Text drivedistancet;
	private Text turnabsolutet;
	private Text turnrelativet;
	private Text driveToXt;
	private Text driveToYt;
	private Text text_3;

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
	/*
	private void MyKeyActionForButton(java.awt.event.KeyEvent evt) {
        if(evt.getKeyCode()==10){
           
            //HIER DIE ACTION AUFRUFEN DIE DU HABEN MOECHTEST
            //10 steht uebrigens fuer enter. 27 waere z.B. Esc :-)
           
        }
    }
    */
	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
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
		
		txtNewText = formToolkit.createText(shlNxtControl, "", SWT.NONE);
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
		
		Button btnSenden = new Button(shlNxtControl, SWT.NONE);
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
		
		Label label_4 = new Label(shlNxtControl, SWT.NONE);
		label_4.setBounds(79, 10, 17, 15);
		formToolkit.adapt(label_4, true, true);
		label_4.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
		
		Button btnBluetooth = new Button(shlNxtControl, SWT.RADIO);
		btnBluetooth.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnBluetooth.setBounds(10, 31, 83, 16);
		formToolkit.adapt(btnBluetooth, true, true);
		btnBluetooth.setText("Bluetooth");
		btnBluetooth.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		Button btnKabel = new Button(shlNxtControl, SWT.RADIO);
		btnKabel.setBounds(99, 31, 50, 16);
		formToolkit.adapt(btnKabel, true, true);
		btnKabel.setText("USB");
		btnKabel.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		Button connectb = new Button(shlNxtControl, SWT.NONE);
		connectb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applicationHandler.connectButton();
			}
		});
		connectb.setText("Connect");
		connectb.setBounds(10, 53, 139, 26);
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
		
		Label label_6 = new Label(shlNxtControl, SWT.NONE);
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
		
		
		Button btnLinks = new Button(composite, SWT.NONE);
		btnLinks.setBounds(401, 77, 50, 50);
		btnLinks.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applicationHandler.goLeftButton();
			}
		});
		btnLinks.setText("Left");
		btnLinks.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		Button btnRechts = new Button(composite, SWT.NONE);
		btnRechts.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applicationHandler.goRightButton();
			}
		});
		btnRechts.setBounds(511, 77, 50, 50);
		btnRechts.setText("Right");
		btnRechts.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		Button btnZ = new Button(composite, SWT.NONE);
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
		
		Button drivedistanceb = new Button(composite, SWT.NONE);
		drivedistanceb.setBounds(179, 9, 139, 25);
		drivedistanceb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applicationHandler.driveDistanceButton();
			}
		});
		formToolkit.adapt(drivedistanceb, true, true);
		drivedistanceb.setText("drive distance (cm)");
		
		Button turnabsoluteb = new Button(composite, SWT.NONE);
		turnabsoluteb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applicationHandler.turnAbsoluteButton();
			}
		});
		turnabsoluteb.setBounds(179, 40, 139, 25);
		turnabsoluteb.setText("turn absolute");
		formToolkit.adapt(turnabsoluteb, true, true);
		
		Button turnrelativeb = new Button(composite, SWT.NONE);
		turnrelativeb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applicationHandler.turnRelativeButton();
			}
		});
		turnrelativeb.setBounds(179, 71, 139, 25);
		turnrelativeb.setText("turn relative");
		formToolkit.adapt(turnrelativeb, true, true);
		
		Button driveTob = new Button(composite, SWT.NONE);
		driveTob.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applicationHandler.driveToButton();
			}
		});
		driveTob.setBounds(179, 102, 139, 25);
		driveTob.setText("drive to");
		formToolkit.adapt(driveTob, true, true);
		
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
		
		Button sgyrospeedb = new Button(composite_1, SWT.NONE);
		sgyrospeedb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applicationHandler.sendGyroSpeedButton();
			}
		});
		sgyrospeedb.setBounds(169, 22, 139, 25);
		sgyrospeedb.setText("send gyro speed");
		formToolkit.adapt(sgyrospeedb, true, true);
		
		sgyrospeedt = new Text(composite_1, SWT.BORDER);
		sgyrospeedt.setBounds(24, 22, 139, 25);
		formToolkit.adapt(sgyrospeedt, true, true);
		
		Button sgyrointegralb = new Button(composite_1, SWT.NONE);
		sgyrointegralb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applicationHandler.sendGyroIntegralButton();
			}
		});
		sgyrointegralb.setBounds(169, 53, 139, 25);
		sgyrointegralb.setText("send gyrointegral");
		formToolkit.adapt(sgyrointegralb, true, true);
		
		sgyrointegralt = new Text(composite_1, SWT.BORDER);
		sgyrointegralt.setBounds(24, 53, 139, 25);
		formToolkit.adapt(sgyrointegralt, true, true);
		
		Button smotorspeedb = new Button(composite_1, SWT.NONE);
		smotorspeedb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applicationHandler.sendMotorSpeedButton();
			}
		});
		smotorspeedb.setBounds(169, 84, 139, 25);
		smotorspeedb.setText("send motorspeed");
		formToolkit.adapt(smotorspeedb, true, true);
		
		smotorspeedt = new Text(composite_1, SWT.BORDER);
		smotorspeedt.setBounds(24, 84, 139, 25);
		formToolkit.adapt(smotorspeedt, true, true);
		
		Button smotordistanceb = new Button(composite_1, SWT.NONE);
		smotordistanceb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applicationHandler.sendMotorDistanceButton();
			}
		});
		smotordistanceb.setBounds(169, 115, 139, 25);
		smotordistanceb.setText("send motordistance");
		formToolkit.adapt(smotordistanceb, true, true);
		
		smotordistancet = new Text(composite_1, SWT.BORDER);
		smotordistancet.setBounds(24, 115, 139, 25);
		formToolkit.adapt(smotordistancet, true, true);
		
		Button sreifengroesseb = new Button(composite_1, SWT.NONE);
		sreifengroesseb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applicationHandler.sendWheeldiameterButton();
			}
		});
		sreifengroesseb.setBounds(169, 146, 139, 25);
		sreifengroesseb.setText("send wheeldiameter");
		formToolkit.adapt(sreifengroesseb, true, true);
		
		Button sdistancetargetb = new Button(composite_1, SWT.NONE);
		sdistancetargetb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applicationHandler.sendConstantSpeedButton();
			}
		});
		sdistancetargetb.setBounds(169, 177, 139, 25);
		sdistancetargetb.setText("send constant speed");
		formToolkit.adapt(sdistancetargetb, true, true);
		
		sdistancetargett = new Text(composite_1, SWT.BORDER);
		sdistancetargett.setBounds(24, 177, 139, 25);
		formToolkit.adapt(sdistancetargett, true, true);
		
		Button srotationtargetb = new Button(composite_1, SWT.NONE);
		srotationtargetb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applicationHandler.sendConstantRotationButton();
			}
		});
		srotationtargetb.setBounds(169, 208, 139, 25);
		srotationtargetb.setText("send constant rotation");
		formToolkit.adapt(srotationtargetb, true, true);
		
		srotationtargett = new Text(composite_1, SWT.BORDER);
		srotationtargett.setBounds(24, 208, 139, 25);
		formToolkit.adapt(srotationtargett, true, true);
		
		Button sspurb = new Button(composite_1, SWT.NONE);
		sspurb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applicationHandler.sendTrackButton();
			}
		});
		sspurb.setBounds(169, 241, 139, 25);
		sspurb.setText("send track");
		formToolkit.adapt(sspurb, true, true);
		
		Button paramsendall = new Button(composite_1, SWT.NONE);
		paramsendall.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applicationHandler.sendAllButton();
			}
		});
		paramsendall.setBounds(24, 272, 284, 50);
		formToolkit.adapt(paramsendall, true, true);
		paramsendall.setText("send all parameter");
		
		Combo combo = new Combo(composite_1, SWT.NONE);
		combo.setBounds(24, 146, 139, 23);
		formToolkit.adapt(combo);
		formToolkit.paintBordersFor(combo);
		combo.setItems("5,6 cm","12 cm");
		combo.setText("5,6 cm");
		
		Combo combo_1 = new Combo(composite_1, SWT.NONE);
		combo_1.setBounds(24, 239, 139, 23);
		formToolkit.adapt(combo_1);
		formToolkit.paintBordersFor(combo_1);
		combo_1.setItems("inside", "outside");
		combo_1.setText("inside");
		
		
		TabItem tbtmKarte = new TabItem(tabFolder, SWT.NONE);
		tbtmKarte.setText("Map");
		
		Composite composite_2 = new Composite(tabFolder, SWT.NONE);
		tbtmKarte.setControl(composite_2);
		formToolkit.paintBordersFor(composite_2);
		composite_2.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		Label lblPosition = new Label(shlNxtControl, SWT.NONE);
		lblPosition.setBounds(341, 10, 100, 15);
		formToolkit.adapt(lblPosition, true, true);
		lblPosition.setText("current position");
		lblPosition.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		text = new Text(shlNxtControl, SWT.BORDER);
		text.setEnabled(false);
		text.setEditable(false);
		text.setBounds(338, 37, 50, 25);
		formToolkit.adapt(text, true, true);
		
		text_1 = new Text(shlNxtControl, SWT.BORDER);
		text_1.setEnabled(false);
		text_1.setEditable(false);
		text_1.setBounds(427, 37, 50, 25);
		formToolkit.adapt(text_1, true, true);
		
		Label lblYaktuell = new Label(shlNxtControl, SWT.NONE);
		lblYaktuell.setBounds(403, 44, 17, 13);
		lblYaktuell.setText("y:");
		formToolkit.adapt(lblYaktuell, true, true);
		lblYaktuell.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		Label lblXaktuell = new Label(shlNxtControl, SWT.NONE);
		lblXaktuell.setBounds(312, 46, 17, 13);
		lblXaktuell.setText("x:");
		formToolkit.adapt(lblXaktuell, true, true);
		lblXaktuell.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		Label lblCrashWarning = new Label(shlNxtControl, SWT.NONE);
		lblCrashWarning.setBounds(341, 68, 139, 25);
		lblCrashWarning.setFont(SWTResourceManager.getFont("Tahoma", 16, SWT.NORMAL));
		formToolkit.adapt(lblCrashWarning, true, true);
		lblCrashWarning.setText("blocked way!");
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

	}
}
