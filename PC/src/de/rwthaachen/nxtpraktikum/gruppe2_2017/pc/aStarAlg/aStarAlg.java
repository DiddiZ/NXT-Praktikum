package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.aStarAlg;

import java.awt.Point;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.MapData;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn.NXTData;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.gui.Navigator.MAP_SQUARE_LENGTH;




public class aStarAlg {
	private MapData map;
	private NXTData data;
	public final float SAFE_DISTANCE = 21.0f;
	
	public aStarAlg(MapData map, NXTData data){
		this.map = map;
		this.data = data;
	}

	public boolean queueContainsPoint(PriorityQueue<QueueElement> openlist, QueueElement current){
		int size = openlist.size();
		Object[] trashlist =  openlist.toArray();
		boolean result = false;
		for(int i = 0; i < size; i++){
			QueueElement a = (QueueElement)trashlist[i];
			if(a!=null && current!=null){
				if(a.getPointNode().getPoint().equals(current.getPointNode().getPoint()))result = true;
			}
		}
		return result;
	}
	
	public boolean setContainsPoint(Set<PointNode> closedlist, PointNode n){
		int size = closedlist.size();
		Object[] trashlist = closedlist.toArray();
		boolean result = false;
		for(int i = 0; i < size; i++){
			PointNode a = (PointNode)trashlist[i];
			if(a!=null && n!=null){
				if(a.getPoint().equals(n.getPoint()))result = true;
			}
		}
		return result;
	}
	
	
	
	public int countSuccessors(int x, int y){
		int count = 0;
		if(map.isKnown(x+MAP_SQUARE_LENGTH, y))count++;
		if(map.isKnown(x+MAP_SQUARE_LENGTH, y+MAP_SQUARE_LENGTH))count++;
		if(map.isKnown(x, y+MAP_SQUARE_LENGTH))count++;
		if(map.isKnown(x-MAP_SQUARE_LENGTH, y+MAP_SQUARE_LENGTH))count++;
		if(map.isKnown(x-MAP_SQUARE_LENGTH, y))count++;
		if(map.isKnown(x-MAP_SQUARE_LENGTH, y-MAP_SQUARE_LENGTH))count++;
		if(map.isKnown(x, y-MAP_SQUARE_LENGTH))count++;
		if(map.isKnown(x+MAP_SQUARE_LENGTH, y-MAP_SQUARE_LENGTH))count++;
		return count;
	}
	
	public PointNode aStarAlgorithm(Point position, Point destination){
		
		Comparator<QueueElement> comp = new PriorityQueueComparator();
		PriorityQueue<QueueElement> openlist = new PriorityQueue<QueueElement>(11, comp);
		Set<PointNode> closedlist = new HashSet<PointNode>();
		openlist.add(new QueueElement(new PointNode(position), 0));
		
		
		while(!openlist.isEmpty()){
			//System.out.println("CurrentList not empty.");
			PointNode current = openlist.remove().getPointNode();
			if(current.getPoint().equals(destination)){
				return current;
			}
			closedlist.add(current);
			expandNode(current, destination, openlist, closedlist);
			
		}
		return null;
	}
	
	/**
	 * The expandNode method used in the a-star Algorithm
	 * @param current
	 * @param destination
	 * @param openlist
	 * @param closedlist
	 */
	public void expandNode(PointNode current, Point destination, PriorityQueue<QueueElement> openlist, Set<PointNode> closedlist){
		for(int i = 0; i < 8; i++){
			//checking if new Neighbor is available in map
			PointNode successor = getNeighbor(i, current);
			if(isFree((int)successor.getPoint().getX(), (int)successor.getPoint().getY()) && !closedlist.contains(successor)){
				//calculation of new g value:
				double c = MAP_SQUARE_LENGTH;
				if(i % 2 == 1)c= Math.sqrt(2*MAP_SQUARE_LENGTH*MAP_SQUARE_LENGTH);
				double tentative_g = current.getWeight() + c;
				//Getting real successor if it already exists
				QueueElement realSuccQueue = getRealSuccessor(successor, openlist);
				PointNode realSucc;
				if(realSuccQueue==null){
					realSucc = successor;
				}else{
					realSucc = realSuccQueue.getPointNode();
				}
				//checking if new Route to Map Element is better than the old one
				if(!(openlist.contains(new QueueElement(successor, 0)) && tentative_g >=realSucc.getWeight())){
					
					//setting Values for successor
					realSucc.setPred(current);
					realSucc.setWeight(tentative_g);
					
					//calculating new Priority
					double f = tentative_g + Math.sqrt((destination.getX()-realSucc.getPoint().getX())*(destination.getX()-realSucc.getPoint().getX())+(destination.getY()-realSucc.getPoint().getY())*(destination.getY()-realSucc.getPoint().getY()));
					
					//appending successor
					if(queueContainsPoint(openlist, realSuccQueue)){
						realSuccQueue.setPriority(f);
						openlist.remove(realSuccQueue);
						openlist.add(realSuccQueue);
					}else{
						openlist.add(new QueueElement(realSucc, f));
					}
				}
			}
		}
	}
	
	/**
	 * This method returns a QueueElement form openlist, which is similar to successor( checked with .equals())
	 * @param successor the elmenet to search for in openlist
	 * @param openlist a priorityQueue
	 * @return the instance of successor which is actually in openlist
	 */
	public QueueElement getRealSuccessor(PointNode successor, PriorityQueue<QueueElement> openlist){
		for(QueueElement a : openlist){
			if(a!=null && successor!=null){
				if(a.getPointNode().getPoint().equals(successor.getPoint()))return a;
				
			}
		}
		return null;
	}
	
	/**
	 * This Method creates a new PointNode for each potential Neighbor a PointNode can have. If this Position is free or not must be checked later.
	 * @param i reaches form 0 to 7, creates a neighbor each.
	 * @param p the PointNode which is used to search neighbors from
	 * @return a neighbor of p
	 */
	public PointNode getNeighbor(int i, PointNode p){
		int x = (int)p.getPoint().getX();
		int y = (int)p.getPoint().getY();
		if(i==0){
			x+=MAP_SQUARE_LENGTH;
		}
		if(i==1){
			x+=MAP_SQUARE_LENGTH;
			y+=MAP_SQUARE_LENGTH;
		}
		if(i==2){
			y+=MAP_SQUARE_LENGTH;
		}
		if(i==3){
			x-=MAP_SQUARE_LENGTH;
			y+=MAP_SQUARE_LENGTH;
		}
		if(i==4){
			x-=MAP_SQUARE_LENGTH;
		}
		if(i==5){
			x-=MAP_SQUARE_LENGTH;
			y-=MAP_SQUARE_LENGTH;
		}
		if(i==6){
			y-=MAP_SQUARE_LENGTH;
		}
		if(i==7){
			x+=MAP_SQUARE_LENGTH;
			y-=MAP_SQUARE_LENGTH;
		}
		return new PointNode(new Point(x,y));
	}
	
	/**@author Fabian, Justus
	 * This method returns true if the coordinates in map are available to stand on or to pass through within a save area.
	 * @param x x coordinate of Point to check
	 * @param y y coordinate of Point to check
	 * @return true if coordinates are not occupied
	 */
	public boolean isFree(int x, int y){
		boolean free = true;
		//checking if on position. Position is always free.
		int posX = (int) data.getPositionX();
		int posY = (int) data.getPositionY();
		if(x < posX+10 && x > posX-10 && y < posY+10 && y > posY - 10){
			return true;
		}
		int squareNumber = (int) (SAFE_DISTANCE/MAP_SQUARE_LENGTH)+1;
		for(int i = x-squareNumber*MAP_SQUARE_LENGTH; i<= x+squareNumber*MAP_SQUARE_LENGTH; i+=MAP_SQUARE_LENGTH){
			for(int j = y-squareNumber*MAP_SQUARE_LENGTH; j<= y+squareNumber*MAP_SQUARE_LENGTH; j+=MAP_SQUARE_LENGTH){
				if(map.isObstacle(i, j)){
					free = false;
				}
			}
		}
		return free;
	}
	

}
