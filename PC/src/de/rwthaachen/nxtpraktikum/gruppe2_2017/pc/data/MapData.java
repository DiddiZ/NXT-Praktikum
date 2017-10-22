package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.data;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.HashMap;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.nav.Navigator;

/**
 * This is a dynamic data structure to save all information gained by the NXT about his environment.
 *
 * @author Justus, Robin
 */

public final class MapData extends HashMap<Point, Float>
{
	private static final float LEARN_RATE = 0.2f;
	private static final float LEARN_RATE_DEFECTIVE = 0.02f;

	/**
	 * This method appends new information about the environment to the existing data.
	 * If the new information is about a segment that has not been included before,
	 * this method saves the value of isObstacle as float.
	 * <p>
	 * The method calculates an obstruction-value in case there is more than one information about the same segment.
	 * The new value is weighted based on its defective value for the recalculation.
	 * The weighting is needed to prevent single defective data from damaging the MapData.
	 * 
	 * @param x: The x-coordinate of the segment that is appended
	 * @param y: The y-coordinate of the segment that is appended
	 * @param isObstacle: A boolean indicating whether the new data marks an obstacle or not
	 * @param defective: A boolean indicating whether the new data could be invalid
	 */

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
	 * As our obstacle-value is approximated, it is saved as a float instead of a boolean.
	 * The method interprets a segment as an obstacle, if its obstruction-value is higher than 0.5
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

	/**
	 * This method calculates, whether a certain shape within the map is completely free of obstacles
	 * Searches iterative for every tile within the shape.
	 * 
	 * @param shape: The shape which is to be tested
	 * @return true, if any tile inside the shape is marked as an obstacle
	 */
	public boolean isObstacled(Shape shape) {
		final Rectangle bounds = shape.getBounds();

		final int minX = Navigator.discrete(bounds.getMinX()), maxX = Navigator.discrete(bounds.getMaxX()), minY = Navigator.discrete(bounds.getMinY()), maxY = Navigator.discrete(bounds.getMaxY());

		for (int x = minX; x <= maxX; x += Navigator.MAP_SQUARE_LENGTH) {
			for (int y = minY; y <= maxY; y += Navigator.MAP_SQUARE_LENGTH) {
				if (shape.contains(x, y) && isObstacle(x, y)) {
					return true;
				}
			}
		}
		return false;
	}
}
