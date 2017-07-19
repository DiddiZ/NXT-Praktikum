package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.aStarAlg;

/**
 * This class is the wrapper for the PointNodes used in the a-star Algorithm. It adds a priority so the Elements can be sorted.
 * @author Fabian
 *
 */
public class QueueElement {
	private PointNode pointNode;
	private double priority;
	
	/**
	 * Constructor
	 * @param p PointNode to put in this QueueElement
	 * @param priority Priority to Queue up the certain PointNode
	 */
	public QueueElement(PointNode p, double priority){
		this.pointNode = p;
		this.priority = priority;
	}
	
	/**
	 * Getter for pointNode
	 * @return this.PointNode
	 */
	public PointNode getPointNode(){
		return this.pointNode;
	}
	
	/**
	 * Getter for Priority
	 * @return this.priority in double.
	 */
	public double getPriority(){
		return this.priority;
	}
	
	/**
	 * Setter for priority
	 * @param priority the QueueElement's priority in double.
	 */
	public void setPriority(double priority){
		this.priority = priority;
	}
	
	/**
	 * equals method for QueueElement. Only returns the result of PointNode.equals(n), which returns the result of Point.equals(p).
	 * @param e The element which is to compare with this
	 * @return true if this.pointNode.getPoint().equals(e.getPointNode().getPoint())
	 */
	public boolean equals(QueueElement e){
		return pointNode.equals(e.getPointNode());
	}

}
