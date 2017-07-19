package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.aStarAlg;

import java.awt.Point;

/**
 * This class is the wrapper for elements to use in the a-star Algorithm.
 * @author Fabian
 *
 */
public class PointNode {
	private Point point;
	private double weight;
	private PointNode pred;
	
	/**
	 * Constructor
	 * @param point
	 */
	public PointNode(Point point){
		this.point = point;
		this.weight = 0.0;
		this.pred = null;
	}
	
	/**
	 *  Standard getter for point
	 * @return this.point
	 */
	public Point getPoint(){
		return this.point;
	}
	
	/**
	 * Standard getter for weight
	 * @return this.weight
	 */
	public double getWeight(){
		return this.weight;
	}
	
	/**
	 * Standard getter for pred
	 * @return this.pred
	 */
	public PointNode getPred(){
		return this.pred;
	}
	
	/**
	 * Getter for all pred elements with an index i. i=0 is this, i=1 is pred etc.
	 * @param i index of pred
	 * @return pred(i)
	 */
	public PointNode getPred(int i){
		if(i == 0 || this.pred == null){
			return this;
		}else{
			return this.pred.getPred(i - 1);
		}
	}
	
	/**
	 * Standard setter for weight
	 * @param weight the node's weight in double
	 */
	public void setWeight(double weight){
		this.weight = weight;
	}
	
	/**
	 * Standard setter for pred
	 * @param pred the node's pred in PointNode
	 */
	public void setPred(PointNode pred){
		this.pred = pred;
	}
	
	/**
	 * Equals method for PointNode. Only checks point.equals(), and not the other parameters
	 * @param n the other PointNode which is to compare
	 * @return true if point.equals(n) is true, else false.
	 */
	public boolean equals(PointNode n){
		return point.equals(n.getPoint());
	}
	
	/**
	 * Returns the length of the chain created by recursion. 0 if pred == null.
	 * @return the length of the chain created by recursion. 0 if pred == null.
	 */
	public int getChainLength(){
		if(pred == null){
			return 0;
		}else{
			return pred.getChainLength() + 1;
		}
	}
	
	
	
	

}
