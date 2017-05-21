package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm.GetHandler;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm.SetHandler;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.CommunicatorPC;

public class NXTremoteControl_TA extends JFrame
{
	private final JButton quit, connect, disconnect;
	private final JTextField J_WEIGHT_GYRO_SPEED, J_WEIGHT_GYRO_ANGLE,
			J_WEIGHT_MOTOR_DISTANCE, J_WEIGHT_MOTOR_SPEED;
	private final JButton JButton_WEIGHT_GYRO_SPEED, JButton_WEIGHT_GYRO_ANGLE,
			JButton_WEIGHT_MOTOR_DISTANCE, JButton_WEIGHT_MOTOR_SPEED;

	private final CommunicatorPC communicator = new CommunicatorPC();

	private final ButtonHandler bh = new ButtonHandler();

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

	private class ButtonHandler implements ActionListener, KeyListener
	{

		@Override
		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource() == quit)
				System.exit(0);
			else if (ae.getSource() == connect) {
				if (!communicator.isConnected())
					communicator.connect();
			} else if (ae.getSource() == disconnect) {
				if (communicator.isConnected())
					communicator.disconnect();
			} else if (communicator.isConnected())
				try {
					final Object obj = ae.getSource();
					if (obj == JButton_WEIGHT_GYRO_SPEED)
						// final float value = Float.parseFloat(J_WEIGHT_GYRO_SPEED.getText());
						// communicator.sendSet(SetHandler.PARAM_WEIGHT_GYRO_SPEED, value);
						communicator.sendGet(GetHandler.PARAM_BATTERY_VOLTAGE);
					else if (obj == JButton_WEIGHT_GYRO_ANGLE) {
						final float value = Float.parseFloat(J_WEIGHT_GYRO_ANGLE.getText());
						communicator.sendSet(SetHandler.PARAM_WEIGHT_GYRO_INTEGRAL, value);
					} else if (obj == JButton_WEIGHT_MOTOR_DISTANCE) {
						final float value = Float.parseFloat(J_WEIGHT_MOTOR_DISTANCE.getText());
						communicator.sendSet(SetHandler.PARAM_WEIGHT_MOTOR_DISTANCE, value);
					} else if (obj == JButton_WEIGHT_MOTOR_SPEED) {
						final float value = Float.parseFloat(J_WEIGHT_MOTOR_SPEED.getText());
						communicator.sendSet(SetHandler.PARAM_WEIGHT_MOTOR_SPEED, value);
					}
				} catch (final IOException ex) {
					ex.printStackTrace();
				}
		}// End ActionEvent(for buttons)

		@Override
		public void keyPressed(KeyEvent ke) {}

		@Override
		public void keyTyped(KeyEvent ke) {
			if (communicator != null) {

				;
				// final int key = ke.getKeyChar();

				try {
					// TODO Send Move on keys
					// outData.write(callbackMethodNo);
					// outData.writeFloat(key);
					// outData.flush();
					communicator.sendGet(GetHandler.PARAM_BATTERY_VOLTAGE);

				} catch (final IOException ex) {
					ex.printStackTrace();
				}
			}
		}// End keyTyped

		@Override
		public void keyReleased(KeyEvent ke) {}

	}// End ButtonHandler
}// End ControlWindow class