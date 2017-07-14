package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn;

import java.awt.Point;
import java.util.HashMap;

/**
 * @author Justus, Robin
 *         This is a dynamic data structure to save all information
 *         gained by the NXT about his environment.
 *         The Data is ordered by the x-coordinates in first place
 *         and in second place by the y-coordinates.
 */

public final class MapData extends HashMap<Point, Float>
{
	private static final float LEARN_RATE = 0.2f;
	private static final float LEARN_RATE_DEFECTIVE = 0.02f;

	public synchronized void append(int x, int y, boolean isObstacle, boolean defective) {
		final float newObstruction = isObstacle ? 1f : 0f;
		final Float obstruction = get(new Point(x, y));

		float newValue;
		if (obstruction == null) {// new tile
			newValue = newObstruction;
		} else if (!defective) { // Update obstacle only with proper data
			newValue = newObstruction * LEARN_RATE + obstruction * (1f - LEARN_RATE);
		} else { // Update obstacle only with proper data
			newValue = newObstruction * LEARN_RATE_DEFECTIVE + obstruction * (1f - LEARN_RATE_DEFECTIVE);
		}
		put(new Point(x, y), newValue);
	}

	/**
	 * This method searches in the Data whether the coordinates are marked as an obstacle or free area
	 *
	 * @param x: The x-coordinate of the Data the method searches for
	 * @param y: The y-coordinate of the Data the method searches for
	 * @return: true, if a MapData exists that marks the Coordinates as an obstacle
	 */
	public boolean isObstacle(int x, int y) {
		final Float obstruction = get(new Point(x, y));
		return obstruction != null && obstruction > 0.5f;
	}

	/**
	 * This method searches for an entry in the Data with fitting coordinates
	 *
	 * @param x_test: The x-coordinate of the Data the method searches for
	 * @param y_test: The y-coordinate of the Data the method searches for
	 * @return: true, if an entry with fitting coordinates exists in the MapData
	 */
	public boolean isKnown(int x, int y) {
		return containsKey(new Point(x, y));
	}
}
