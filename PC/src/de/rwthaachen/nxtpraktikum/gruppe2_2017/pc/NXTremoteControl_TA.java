package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import lejos.pc.comm.NXTConnector;

public class NXTremoteControl_TA extends JFrame
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static JButton quit, connect, disconnect;
	private static JTextField J_WEIGHT_GYRO_SPEED, J_WEIGHT_GYRO_ANGLE,
			J_WEIGHT_MOTOR_DISTANCE, J_WEIGHT_MOTOR_SPEED;
	private static JButton JButton_WEIGHT_GYRO_SPEED, JButton_WEIGHT_GYRO_ANGLE,
			JButton_WEIGHT_MOTOR_DISTANCE, JButton_WEIGHT_MOTOR_SPEED;

	private static ButtonHandler bh = new ButtonHandler();
	private static DataOutputStream outData;
	private static DataInputStream inData;
	private static NXTConnector link;

	public NXTremoteControl_TA() {
		setTitle("Control");
		setBounds(650, 350, 500, 500);
		setLayout(new GridLayout(6, 1));

		final String GYRO_SPEED_INIT = "-2.8";
		final String GYRO_ANGLE_INIT = "-8.2";
		final String MOTOR_DISTANCE_INIT = "0.042";
		final String MOTOR_SPEED_INIT = "0.25";

		J_WEIGHT_GYRO_SPEED = new JTextField();
		J_WEIGHT_GYRO_SPEED.setText(GYRO_SPEED_INIT);

		J_WEIGHT_GYRO_ANGLE = new JTextField();
		J_WEIGHT_GYRO_ANGLE.setText(GYRO_ANGLE_INIT);

		J_WEIGHT_MOTOR_DISTANCE = new JTextField();
		J_WEIGHT_MOTOR_DISTANCE.setText(MOTOR_DISTANCE_INIT);

		J_WEIGHT_MOTOR_SPEED = new JTextField();
		J_WEIGHT_MOTOR_SPEED.setText(MOTOR_SPEED_INIT);

		JButton_WEIGHT_GYRO_SPEED = new JButton("send Gyro Speed");
		JButton_WEIGHT_GYRO_SPEED.addActionListener(bh);

		JButton_WEIGHT_GYRO_ANGLE = new JButton("send Gyro Angle");
		JButton_WEIGHT_GYRO_ANGLE.addActionListener(bh);

		JButton_WEIGHT_MOTOR_DISTANCE = new JButton("send Motor Distance");
		JButton_WEIGHT_MOTOR_DISTANCE.addActionListener(bh);

		JButton_WEIGHT_MOTOR_SPEED = new JButton("send Motor Speed");
		JButton_WEIGHT_MOTOR_SPEED.addActionListener(bh);

		add(J_WEIGHT_GYRO_SPEED);
		add(JButton_WEIGHT_GYRO_SPEED);

		add(J_WEIGHT_GYRO_ANGLE);
		add(JButton_WEIGHT_GYRO_ANGLE);

		add(J_WEIGHT_MOTOR_DISTANCE);
		add(JButton_WEIGHT_MOTOR_DISTANCE);

		add(J_WEIGHT_MOTOR_SPEED);
		add(JButton_WEIGHT_MOTOR_SPEED);

		connect = new JButton(" Connect ");
		connect.addActionListener(bh);
		connect.addKeyListener(bh);
		add(connect);

		disconnect = new JButton(" Disconnect ");
		disconnect.addActionListener(bh);
		disconnect.addKeyListener(bh);
		add(disconnect);

		quit = new JButton("Quit");
		quit.addActionListener(bh);
		add(quit);

	}

	public static void main(String[] args) {
		final NXTremoteControl_TA NXTrc = new NXTremoteControl_TA();
		NXTrc.setVisible(true);
		NXTrc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}// End main

	private static class ButtonHandler implements ActionListener, KeyListener
	{

		@Override
		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource() == quit)
				System.exit(0);
			if (ae.getSource() == connect)
				connect();
			if (ae.getSource() == disconnect)
				disconnect();

			if (outData != null)
				try {
					final Object obj = ae.getSource();
					float parameter;
					if (obj == JButton_WEIGHT_GYRO_SPEED) {
						outData.write(0);
						parameter = Float.parseFloat(J_WEIGHT_GYRO_SPEED.getText());
						outData.writeFloat(parameter);
						outData.flush();
					} else if (obj == JButton_WEIGHT_GYRO_ANGLE) {
						outData.write(1);
						parameter = Float.parseFloat(J_WEIGHT_GYRO_ANGLE.getText());
						outData.writeFloat(parameter);
						outData.flush();
					} else if (obj == JButton_WEIGHT_MOTOR_DISTANCE) {
						outData.write(2);
						parameter = Float.parseFloat(J_WEIGHT_MOTOR_DISTANCE.getText());
						outData.writeFloat(parameter);
						outData.flush();
					} else if (obj == JButton_WEIGHT_MOTOR_SPEED) {
						outData.write(3);
						parameter = Float.parseFloat(J_WEIGHT_MOTOR_SPEED.getText());
						outData.writeFloat(parameter);
						outData.flush();
					}

				} catch (final IOException ioe) {
					System.out.println("\nIO Exception writeInt");
				}
		}// End ActionEvent(for buttons)

		@Override
		public void keyPressed(KeyEvent ke) {}

		@Override
		public void keyTyped(KeyEvent ke) {
			if (outData != null) {
				final int key = ke.getKeyChar();
				final int callbackMethodNo = 1;

				try {

					outData.write(callbackMethodNo);
					outData.writeFloat(key);
					outData.flush();

				} catch (final IOException ioe) {
					System.out.println("IO Exception write.");
				}

			}

		}// End keyTyped

		@Override
		public void keyReleased(KeyEvent ke) {}

	}// End ButtonHandler

	public static void connect() {
		link = new NXTConnector();

		if (link.connectTo("btspp://")) {
			outData = new DataOutputStream(link.getOutputStream());
			inData = new DataInputStream(link.getInputStream());
			System.out.println("\nNXT is Connected");
		} else
			System.out.println("\nNo NXT find using Bluetooth");

	}// End connect

	public static void disconnect() {

		try {
			if (outData != null)
				outData.close();
			if (inData != null)
				inData.close();
			if (link != null)
				link.close();
		}

		catch (final IOException ioe) {
			System.out.println("\nIO Exception writing bytes");
		}
		System.out.println("\nClosed data streams");

	}// End disconnect

}// End ControlWindow class