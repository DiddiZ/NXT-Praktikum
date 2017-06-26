package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui.gamepad;

import static java.lang.Math.abs;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.Controller.Type;
import net.java.games.input.ControllerEnvironment;

/**
 * Provides x,y, and z axis for a controller. Uses an internal thread for polling.
 *
 * @author Robin
 */
public final class Gamepad implements Closeable
{
	/**
	 * Values for x, y, and z axis in interval [-1,1]
	 */
	public float xAxis, yAxis, zAxis;

	private final Controller controller;
	private Component xAxisComponent, yAxisComponent, zAxisComponent;

	private boolean active = true;

	public Gamepad(Controller controller) {
		this.controller = controller;

		for (final Component component : controller.getComponents()) {
			switch (component.getName()) {
				case "X-Achse":
					xAxisComponent = component;
					break;
				case "Y-Achse":
					yAxisComponent = component;
					break;
				case "Z-Achse":
					zAxisComponent = component;
					break;
			}
		}

		if (xAxisComponent == null || yAxisComponent == null || zAxisComponent == null) {
			throw new IllegalArgumentException("Controller doesn't have x,y or z axis");
		}

		new PollThread().start();
	}

	@Override
	public void close() throws IOException {
		active = false;
	}

	/**
	 * @return whether controller is active and polled data is current.
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Tries to find a connected gamepad. Asks user for selection, if multiple are found.
	 *
	 * @return a new Gamepad or null
	 */
	public static Gamepad findGamepad() {
		final List<Controller> gamepads = new ArrayList<>();
		for (final Controller c : ControllerEnvironment.getDefaultEnvironment().getControllers()) {
			if (c.getType().equals(Type.GAMEPAD)) {
				gamepads.add(c);
			}
		}

		if (gamepads.isEmpty()) {
			return null;
		}
		if (gamepads.size() == 1) {
			return new Gamepad(gamepads.get(0));
		}

		final JComboBox<Controller> cmb = new JComboBox<>(new Vector<>(gamepads));
		final Object[] msg = {
				"Select gamepad:",
				cmb};

		if (JOptionPane.showOptionDialog(null, msg, "Gamepad Selection", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null) != JOptionPane.OK_OPTION) {
			return null;
		}
		return new Gamepad((Controller)cmb.getSelectedItem());
	}

	private static float applyDeadzone(float oldPollData, float pollData, float deadZone) {
		if (abs(pollData - oldPollData) >= deadZone) {
			return pollData;
		}
		if (pollData == 1.0f) {
			return 1.0f;
		}
		if (pollData == -1.0f) {
			return -1.0f;
		}
		if (abs(pollData) < deadZone) {
			return 0;
		}

		return oldPollData;
	}

	private final class PollThread extends Thread
	{
		public PollThread() {
			setDaemon(true);
		}

		@Override
		public void run() {
			while (active) {
				if (!controller.poll()) {
					active = false;
				}

				try {
					Thread.sleep(20);
				} catch (final InterruptedException ex) {
					ex.printStackTrace();
				}

				// Update axis
				xAxis = applyDeadzone(xAxis, xAxisComponent.getPollData(), xAxisComponent.getDeadZone());
				yAxis = applyDeadzone(yAxis, yAxisComponent.getPollData(), yAxisComponent.getDeadZone());
				zAxis = applyDeadzone(zAxis, zAxisComponent.getPollData(), zAxisComponent.getDeadZone());
			}
		}
	}
}
