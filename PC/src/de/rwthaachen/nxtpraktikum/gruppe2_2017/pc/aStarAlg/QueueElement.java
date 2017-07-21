package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.aStarAlg;

/**
 * This class is the wrapper for the PointNodes used in the a-star Algorithm. It adds a priority so the Elements can be sorted.
 *
 * @author Fabian
 */
public class QueueElement
{
	private final PointNode pointNode;
	private double priority;

	/**
	 * Constructor
	 *
	 * @param p PointNode to put in this QueueElement
	 * @param priority Priority to Queue up the certain PointNode
	 */
	public QueueElement(PointNode p, double priority) {
		pointNode = p;
		this.priority = priority;
	}

	/**
	 * Getter for pointNode
	 *
	 * @return this.PointNode
	 */
	public PointNode getPointNode() {
		return pointNode;
	}

	/**
	 * Getter for Priority
	 *
	 * @return this.priority in double.
	 */
	public double getPriority() {
		return priority;
	}

	/**
	 * Setter for priority
	 *
	 * @param priority the QueueElement's priority in double.
	 */
	public void setPriority(double priority) {
		this.priority = priority;
	}

	/**
	 * equals method for QueueElement. Only returns the result of PointNode.equals(n), which returns the result of Point.equals(p).
	 *
	 * @param e The element which is to compare with this
	 * @return true if this.pointNode.getPoint().equals(e.getPointNode().getPoint())
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof QueueElement) {
			return pointNode.equals(((QueueElement)o).getPointNode());
		}
		return false;
	}

	@Override
	public QueueElement clone() {
		return new QueueElement(pointNode.clone(), priority);
	}
}
