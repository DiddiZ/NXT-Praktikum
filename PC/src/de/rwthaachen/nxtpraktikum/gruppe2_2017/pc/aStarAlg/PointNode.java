package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.aStarAlg;

import java.awt.Point;

/**
 * This class is the wrapper for elements to use in the a-star Algorithm.
 *
 * @author Fabian
 */
public class PointNode
{
	private final Point point;
	private double weight;
	private PointNode pred;

	/**
	 * Constructor
	 *
	 * @param point
	 */
	public PointNode(Point point) {
		this.point = point;
		weight = 0.0;
		pred = null;
	}

	private PointNode(Point point, double weight, PointNode pred) {
		this.point = point;
		this.weight = weight;
		this.pred = pred;
	}

	/**
	 * Standard getter for point
	 *
	 * @return this.point
	 */
	public Point getPoint() {
		return point;
	}

	/**
	 * Standard getter for weight
	 *
	 * @return this.weight
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * Standard getter for pred
	 *
	 * @return this.pred
	 */
	public PointNode getPred() {
		return pred;
	}

	/**
	 * Getter for all pred elements with an index i. i=0 is this, i=1 is pred etc.
	 *
	 * @param i index of pred
	 * @return pred(i)
	 */
	public PointNode getPred(int i) {
		if (i == 0 || pred == null) {
			return this;
		}
		return pred.getPred(i - 1);
	}

	/**
	 * Standard setter for weight
	 *
	 * @param weight the node's weight in double
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}

	/**
	 * Standard setter for pred
	 *
	 * @param pred the node's pred in PointNode
	 */
	public void setPred(PointNode pred) {
		this.pred = pred;
	}

	/**
	 * Equals method for PointNode. Only checks point.equals(), and not the other parameters
	 *
	 * @param n the other PointNode which is to compare
	 * @return true if point.equals(n) is true, else false.
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof PointNode) {
			return point.equals(((PointNode)o).getPoint());
		}
		return false;
	}

	/**
	 * Returns the length of the chain created by recursion. 0 if pred == null.
	 *
	 * @return the length of the chain created by recursion. 0 if pred == null.
	 */
	public int getChainLength() {
		if (pred == null) {
			return 0;
		}
		return pred.getChainLength() + 1;
	}

	@Override
	public PointNode clone() {
		Point res;
		if (point != null) {
			res = new Point((int)point.getX(), (int)point.getY());
		} else {
			res = null;
		}
		return new PointNode(res, weight, pred.clone());
	}

}
