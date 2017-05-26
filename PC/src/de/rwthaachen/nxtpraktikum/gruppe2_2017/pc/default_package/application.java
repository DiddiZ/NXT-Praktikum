package default_package;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;
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

public class application {

	protected Shell shlNxtControl;
	private Button btnVor;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text txtNewText;
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text reifengroesse;
	private Text sgyrospeedt;
	private Text sgyrointegralt;
	private Text smotorspeedt;
	private Text smotordistancet;
	private Text sspurt;
	private Text sdistancetargett;
	private Text srotationtargett;
	private Text akkuspannungt;
	private Text neigungt;
	private Text motorat;
	private Text motorbt;
	private Text drehungt;
	private Text drivedistancet;
	private Text turnabsolutet;
	private Text turnrelativet;
	private Text driveToXt;
	private Text driveToYt;

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
		
		shlNxtControl.setSize(1000, 520);
		shlNxtControl.setText("NXT Control");
		shlNxtControl.setLayout(null);
		
		Label label = new Label(shlNxtControl, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(10, 122, 972, 2);
		label.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		btnVor = new Button(shlNxtControl, SWT.NONE);
		btnVor.setBounds(255, 130, 50, 50);
		btnVor.setText("Vor");
		btnVor.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		
		Button btnLinks = new Button(shlNxtControl, SWT.NONE);
		btnLinks.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnLinks.setText("Links");
		btnLinks.setBounds(200, 185, 50, 50);
		btnLinks.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		Button btnRechts = new Button(shlNxtControl, SWT.NONE);
		btnRechts.setText("Rechts");
		btnRechts.setBounds(310, 185, 50, 50);
		btnRechts.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		Button btnZ = new Button(shlNxtControl, SWT.NONE);
		btnZ.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnZ.setText("Zur\u00FCck");
		btnZ.setBounds(255, 185, 50, 50);
		btnZ.setBackground(SWTResourceManager.getColor(199, 221, 242));
		/*btnConnect.addKeyListener(new java.awt.event.KeyAdapter() {
	        public void keyReleased(java.awt.event.KeyEvent evt) {
	            MyKeyActionForButton(evt);
	        }
	    });
	    */
		
		Label label_1 = new Label(shlNxtControl, SWT.SEPARATOR | SWT.VERTICAL);
		label_1.setBounds(671, 130, 2, 357);
		formToolkit.adapt(label_1, true, true);
		
		Label lblKommunikation = new Label(shlNxtControl, SWT.NONE);
		lblKommunikation.setBounds(798, 130, 92, 15);
		formToolkit.adapt(lblKommunikation, true, true);
		lblKommunikation.setText("Kommunikation");
		lblKommunikation.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		Label lblPosition = new Label(shlNxtControl, SWT.NONE);
		lblPosition.setBounds(37, 149, 100, 15);
		formToolkit.adapt(lblPosition, true, true);
		lblPosition.setText("Aktuelle Position:");
		lblPosition.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		txtNewText = formToolkit.createText(shlNxtControl, "", SWT.NONE);
		txtNewText.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtNewText.setBounds(679, 151, 303, 304);
		
		Label lblNewLabel = new Label(shlNxtControl, SWT.NONE);
		lblNewLabel.setBounds(10, 10, 63, 15);
		formToolkit.adapt(lblNewLabel, true, true);
		lblNewLabel.setText("Verbindung");
		lblNewLabel.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		text = new Text(shlNxtControl, SWT.BORDER);
		text.setBounds(34, 176, 50, 25);
		formToolkit.adapt(text, true, true);
		
		text_1 = new Text(shlNxtControl, SWT.BORDER);
		text_1.setBounds(123, 176, 50, 25);
		formToolkit.adapt(text_1, true, true);
		
		text_2 = new Text(shlNxtControl, SWT.BORDER);
		text_2.setBounds(679, 461, 247, 26);
		formToolkit.adapt(text_2, true, true);
		
		Button btnSenden = new Button(shlNxtControl, SWT.NONE);
		btnSenden.setBounds(932, 461, 50, 26);
		formToolkit.adapt(btnSenden, true, true);
		btnSenden.setText("Senden");
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
		btnKabel.setBounds(99, 31, 83, 16);
		formToolkit.adapt(btnKabel, true, true);
		btnKabel.setText("Kabel");
		btnKabel.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		reifengroesse = new Text(shlNxtControl, SWT.BORDER);
		reifengroesse.setBounds(381, 311, 139, 25);
		formToolkit.adapt(reifengroesse, true, true);
		
		Button sgyrospeedb = new Button(shlNxtControl, SWT.NONE);
		sgyrospeedb.setText("send gyro speed");
		sgyrospeedb.setBounds(526, 187, 139, 25);
		formToolkit.adapt(sgyrospeedb, true, true);
		
		sgyrospeedt = new Text(shlNxtControl, SWT.BORDER);
		sgyrospeedt.setBounds(381, 187, 139, 25);
		formToolkit.adapt(sgyrospeedt, true, true);
		
		Button sgyrointegralb = new Button(shlNxtControl, SWT.NONE);
		sgyrointegralb.setText("send gyrointegral");
		sgyrointegralb.setBounds(526, 218, 139, 25);
		formToolkit.adapt(sgyrointegralb, true, true);
		
		sgyrointegralt = new Text(shlNxtControl, SWT.BORDER);
		sgyrointegralt.setBounds(381, 218, 139, 25);
		formToolkit.adapt(sgyrointegralt, true, true);
		
		Button smotorspeedb = new Button(shlNxtControl, SWT.NONE);
		smotorspeedb.setText("send motorspeed");
		smotorspeedb.setBounds(526, 249, 139, 25);
		formToolkit.adapt(smotorspeedb, true, true);
		
		smotorspeedt = new Text(shlNxtControl, SWT.BORDER);
		smotorspeedt.setBounds(381, 249, 139, 25);
		formToolkit.adapt(smotorspeedt, true, true);
		
		Button smotordistanceb = new Button(shlNxtControl, SWT.NONE);
		smotordistanceb.setText("send motordistance");
		smotordistanceb.setBounds(526, 280, 139, 25);
		formToolkit.adapt(smotordistanceb, true, true);
		
		smotordistancet = new Text(shlNxtControl, SWT.BORDER);
		smotordistancet.setBounds(381, 280, 139, 25);
		formToolkit.adapt(smotordistancet, true, true);
		
		Button sreifengroesseb = new Button(shlNxtControl, SWT.NONE);
		sreifengroesseb.setText("send wheeldiameter");
		sreifengroesseb.setBounds(526, 311, 139, 25);
		formToolkit.adapt(sreifengroesseb, true, true);
		
		sspurt = new Text(shlNxtControl, SWT.BORDER);
		sspurt.setBounds(381, 406, 139, 25);
		formToolkit.adapt(sspurt, true, true);
		
		Button sdistancetargetb = new Button(shlNxtControl, SWT.NONE);
		sdistancetargetb.setText("send distance target");
		sdistancetargetb.setBounds(526, 342, 139, 25);
		formToolkit.adapt(sdistancetargetb, true, true);
		
		sdistancetargett = new Text(shlNxtControl, SWT.BORDER);
		sdistancetargett.setBounds(381, 342, 139, 25);
		formToolkit.adapt(sdistancetargett, true, true);
		
		Button srotationtargetb = new Button(shlNxtControl, SWT.NONE);
		srotationtargetb.setText("send rotation target");
		srotationtargetb.setBounds(526, 373, 139, 25);
		formToolkit.adapt(srotationtargetb, true, true);
		
		srotationtargett = new Text(shlNxtControl, SWT.BORDER);
		srotationtargett.setBounds(381, 373, 139, 25);
		formToolkit.adapt(srotationtargett, true, true);
		
		Button sspurb = new Button(shlNxtControl, SWT.NONE);
		sspurb.setText("send track");
		sspurb.setBounds(526, 406, 139, 25);
		formToolkit.adapt(sspurb, true, true);
		
		Button paramsendall = new Button(shlNxtControl, SWT.NONE);
		paramsendall.setBounds(381, 437, 284, 50);
		formToolkit.adapt(paramsendall, true, true);
		paramsendall.setText("send all parameter");
		
		Button disconnectb = new Button(shlNxtControl, SWT.NONE);
		disconnectb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		disconnectb.setBounds(10, 85, 172, 26);
		formToolkit.adapt(disconnectb, true, true);
		disconnectb.setText("Disconnect");
		disconnectb.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		Button connectb = new Button(shlNxtControl, SWT.NONE);
		connectb.setText("Connect");
		connectb.setBounds(10, 53, 172, 26);
		formToolkit.adapt(connectb, true, true);
		connectb.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		Label label_3 = new Label(shlNxtControl, SWT.SEPARATOR);
		label_3.setBounds(373, 130, 2, 357);
		formToolkit.adapt(label_3, true, true);
		
		Label lblNeigung = new Label(shlNxtControl, SWT.NONE);
		lblNeigung.setText("Neigung");
		lblNeigung.setBounds(788, 12, 49, 13);
		formToolkit.adapt(lblNeigung, true, true);
		lblNeigung.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		Label lblMotorA = new Label(shlNxtControl, SWT.NONE);
		lblMotorA.setBounds(530, 55, 49, 13);
		formToolkit.adapt(lblMotorA, true, true);
		lblMotorA.setText("Motor A");
		lblMotorA.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		Label lblMotorb = new Label(shlNxtControl, SWT.NONE);
		lblMotorb.setBounds(530, 94, 49, 13);
		formToolkit.adapt(lblMotorb, true, true);
		lblMotorb.setText("Motor B");
		lblMotorb.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		Label lblDrehung = new Label(shlNxtControl, SWT.NONE);
		lblDrehung.setBounds(788, 55, 49, 13);
		formToolkit.adapt(lblDrehung, true, true);
		lblDrehung.setText("Drehung");
		lblDrehung.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		Label lblAkkuspannung = new Label(shlNxtControl, SWT.NONE);
		lblAkkuspannung.setBounds(530, 12, 76, 22);
		formToolkit.adapt(lblAkkuspannung, true, true);
		lblAkkuspannung.setText("Akkuspannung");
		lblAkkuspannung.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		akkuspannungt = new Text(shlNxtControl, SWT.BORDER);
		akkuspannungt.setBounds(635, 9, 139, 25);
		formToolkit.adapt(akkuspannungt, true, true);
		
		neigungt = new Text(shlNxtControl, SWT.BORDER);
		neigungt.setBounds(843, 9, 139, 25);
		formToolkit.adapt(neigungt, true, true);
		
		motorat = new Text(shlNxtControl, SWT.BORDER);
		motorat.setBounds(635, 50, 139, 25);
		formToolkit.adapt(motorat, true, true);
		
		motorbt = new Text(shlNxtControl, SWT.BORDER);
		motorbt.setBounds(843, 52, 139, 25);
		formToolkit.adapt(motorbt, true, true);
		
		drehungt = new Text(shlNxtControl, SWT.BORDER);
		drehungt.setBounds(635, 91, 139, 25);
		formToolkit.adapt(drehungt, true, true);
		
		Label label_6 = new Label(shlNxtControl, SWT.NONE);
		label_6.setBounds(612, 12, 17, 15);
		formToolkit.adapt(label_6, true, true);
		label_6.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
		
		drivedistancet = new Text(shlNxtControl, SWT.BORDER);
		drivedistancet.setBounds(55, 342, 139, 25);
		formToolkit.adapt(drivedistancet, true, true);
		
		turnabsolutet = new Text(shlNxtControl, SWT.BORDER);
		turnabsolutet.setBounds(55, 373, 139, 25);
		formToolkit.adapt(turnabsolutet, true, true);
		
		turnrelativet = new Text(shlNxtControl, SWT.BORDER);
		turnrelativet.setBounds(55, 404, 139, 25);
		formToolkit.adapt(turnrelativet, true, true);
		
		driveToXt = new Text(shlNxtControl, SWT.BORDER);
		driveToXt.setBounds(55, 435, 50, 25);
		formToolkit.adapt(driveToXt, true, true);
		
		driveToYt = new Text(shlNxtControl, SWT.BORDER);
		driveToYt.setBounds(144, 435, 50, 25);
		formToolkit.adapt(driveToYt, true, true);
		
		Button drivedistanceb = new Button(shlNxtControl, SWT.NONE);
		drivedistanceb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		drivedistanceb.setBounds(200, 342, 139, 25);
		formToolkit.adapt(drivedistanceb, true, true);
		drivedistanceb.setText("drive distance (cm)");
		
		Button turnabsoluteb = new Button(shlNxtControl, SWT.NONE);
		turnabsoluteb.setText("turn absolute");
		turnabsoluteb.setBounds(200, 373, 139, 25);
		formToolkit.adapt(turnabsoluteb, true, true);
		
		Button turnrelativeb = new Button(shlNxtControl, SWT.NONE);
		turnrelativeb.setText("turn relative");
		turnrelativeb.setBounds(200, 404, 139, 25);
		formToolkit.adapt(turnrelativeb, true, true);
		
		Button driveTob = new Button(shlNxtControl, SWT.NONE);
		driveTob.setText("drive to");
		driveTob.setBounds(200, 435, 139, 25);
		formToolkit.adapt(driveTob, true, true);
		
		Label lblX_1 = new Label(shlNxtControl, SWT.NONE);
		lblX_1.setText("x:");
		lblX_1.setBounds(31, 441, 17, 13);
		formToolkit.adapt(lblX_1, true, true);
		lblX_1.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		Label lblY = new Label(shlNxtControl, SWT.NONE);
		lblY.setText("y:");
		lblY.setBounds(120, 441, 17, 13);
		formToolkit.adapt(lblY, true, true);
		lblY.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		Label lblYaktuell = new Label(shlNxtControl, SWT.NONE);
		lblYaktuell.setText("y:");
		lblYaktuell.setBounds(99, 183, 17, 13);
		formToolkit.adapt(lblYaktuell, true, true);
		lblYaktuell.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		Label lblXaktuell = new Label(shlNxtControl, SWT.NONE);
		lblXaktuell.setText("x:");
		lblXaktuell.setBounds(10, 183, 17, 13);
		formToolkit.adapt(lblXaktuell, true, true);
		lblXaktuell.setBackground(SWTResourceManager.getColor(199, 221, 242));
		
		Label lblCrashWarning = new Label(shlNxtControl, SWT.NONE);
		lblCrashWarning.setFont(SWTResourceManager.getFont("Tahoma", 16, SWT.NORMAL));
		lblCrashWarning.setBounds(55, 245, 139, 25);
		formToolkit.adapt(lblCrashWarning, true, true);
		lblCrashWarning.setText("Hindernis!");
		lblCrashWarning.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		lblCrashWarning.setBackground(SWTResourceManager.getColor(199, 221, 242));

	}
}
