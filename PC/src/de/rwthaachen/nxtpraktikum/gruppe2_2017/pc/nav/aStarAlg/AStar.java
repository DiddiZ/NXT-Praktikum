package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.nav.aStarAlg;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.nav.Navigator.MAP_SQUARE_LENGTH;

import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.data.MapData;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.data.NXTData;

/**
 * 
 * @author Fabian & Robin
 *
 */
public class AStar
{
	private static final float SAFE_DISTANCE = 21.0f;

	private final MapData map;
	private final NXTData data;

	public AStar(MapData map, NXTData data) {
		this.map = map;
		this.data = data;
	}

	/**
	 * This algorithm returns a chain of points, which represents the shortest path from position to destination.
	 * @param position Starting position
	 * @param destination Destination position
	 * @param maxPathLength maxPathLength aborts if path becomes longer than this
	 * @return an instance of PointNode, which represents a path from position to destination.
	 */
	public PointNode aStarAlgorithm(Point position, Point destination, double maxPathLength) {
		final PriorityQueue<PointNode> openlist = new PriorityQueue<>();
		final Set<PointNode> closedlist = new HashSet<>();
		openlist.add(new PointNode(position, 0, position.distance(destination), null));

		while (!openlist.isEmpty()) {
			// System.out.println("CurrentList not empty.");
			final PointNode current = openlist.remove();
			if (current.getPoint().equals(destination)) {
				return current;
			}
			if (current.getWeight() > maxPathLength) {
				return null;
			}

			closedlist.add(current);
			expandNode(current, destination, openlist, closedlist);
		}
		return null;
	}

	/**
	 * The expandNode method used in the a-star Algorithm
	 *
	 * @param current current Node
	 * @param destination destination coordinate
	 * @param openlist the openlist of the A* algorithm
	 * @param closedlist the closedlist of the A* algorithm
	 */
	private void expandNode(PointNode current, Point destination, PriorityQueue<PointNode> openlist, Set<PointNode> closedlist) {
		if (!isFree(current.getPoint().x, current.getPoint().y)) { // Discard start node, if occupied
			return;
		}
		for (int i = 0; i < 8; i++) {
			// checking if new Neighbor is available in map
			final PointNode successor = getNeighbor(i, current, destination);
			if (isFree(successor.getPoint().x, successor.getPoint().y) && !closedlist.contains(successor)) {
				// Getting real successor if it already exists
				final PointNode realSuccQueue = findInOpenlist(successor.getPoint(), openlist);

				if (realSuccQueue != null) { // Node exists in openlist
					if (realSuccQueue.getWeight() <= successor.getWeight()) { // Existing node is better, leave unchanged
						continue;
					}

					// Update if better
					realSuccQueue.setPred(current);
					realSuccQueue.setWeight(successor.getWeight());
					openlist.remove(realSuccQueue);
					openlist.add(successor);
				} else { // Node was unvisited, add it
					openlist.add(successor);
				}
			}
		}
	}

	/**
	 * This method returns a PointNode from openlist, which is similar to successor( checked with .equals())
	 * @param p point which is to find in openlist
	 * @param openlist openlist of the A* algorithm
	 * @return the element from openlist which is similar to p
	 */
	private static PointNode findInOpenlist(Point p, PriorityQueue<PointNode> openlist) {
		for (final PointNode a : openlist) {
			if (a.getPoint().equals(p)) {
				return a;
			}
		}
		return null;
	}

	/**
	 * This Method creates a new PointNode for each potential Neighbor a PointNode can have. If this Position is free or not must be checked later.
	 *
	 * @param i reaches form 0 to 7, creates a neighbor each.
	 * @param p the PointNode which is used to search neighbors from
	 * @return a neighbor of p
	 */
	private static PointNode getNeighbor(int i, PointNode p, Point destination) {
		int x = p.getPoint().x;
		int y = p.getPoint().y;

		if (i == 0) {
			x += MAP_SQUARE_LENGTH;
		} else if (i == 1) {
			x += MAP_SQUARE_LENGTH;
			y += MAP_SQUARE_LENGTH;
		} else if (i == 2) {
			y += MAP_SQUARE_LENGTH;
		} else if (i == 3) {
			x -= MAP_SQUARE_LENGTH;
			y += MAP_SQUARE_LENGTH;
		} else if (i == 4) {
			x -= MAP_SQUARE_LENGTH;
		} else if (i == 5) {
			x -= MAP_SQUARE_LENGTH;
			y -= MAP_SQUARE_LENGTH;
		} else if (i == 6) {
			y -= MAP_SQUARE_LENGTH;
		} else if (i == 7) {
			x += MAP_SQUARE_LENGTH;
			y -= MAP_SQUARE_LENGTH;
		}
		final double tentative_g = p.getWeight() + (i % 2 == 0 ? MAP_SQUARE_LENGTH : Math.sqrt(2) * MAP_SQUARE_LENGTH);
		final double f = tentative_g + Math.hypot(destination.x - x, destination.y - y); // Heuristic

		return new PointNode(new Point(x, y), tentative_g, f, p);
	}

	/**
	 * This method returns true if the coordinates in map are available to stand on or to pass through within a save area.
	 *
	 * @author Fabian, Justus
	 * @param x x coordinate of Point to check
	 * @param y y coordinate of Point to check
	 * @return true if coordinates are not occupied
	 */
	public boolean isFree(int x, int y) {
		// checking if on position. Position is always free.
		final float posX = data.getPositionX(), posY = data.getPositionY();
		if (x < posX + 10 && x > posX - 10 && y < posY + 10 && y > posY - 10) {
			return true;
		}
		return !map.isObstacled(new Ellipse2D.Float(x, y, SAFE_DISTANCE, SAFE_DISTANCE));
	}
}
