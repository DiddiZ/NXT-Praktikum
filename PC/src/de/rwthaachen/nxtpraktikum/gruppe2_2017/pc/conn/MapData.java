package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn;

/**
 * 
 * @author Justus
 *
 * This is a dynamic data structure to save all information 
 * gained by the NXT about his environment.
 * The Data is ordered by the x-coordinates in first place 
 * and in second place by the y-coordinates.
 */


public class MapData {
	
	private int x, y;
	private boolean isObstacle;
	public MapData next, previous;
	
	
	/**
	 * The constructor for a MapData object: 
	 * @param x_init: the x-coordinate of the MapData
	 * @param y_init: the y-coordinate of the MapData
	 * @param obstacle_init: set to true, if the MapData marks an obstacle; otherwise, it will be a free area
	 */
	public MapData(int x_init, int y_init, boolean obstacle_init){
		this.x = x_init;
		this.y = y_init;
		this.isObstacle = obstacle_init;
		this.next = null;
		this.previous = null;
	}
	
	
	/**
	 * This method will add a new MapData to the existing Data (sorted)
	 * @param newData: the new MapData that has to be added
	 */
	public void append(MapData newData){
		/*The newData has already an existing entry in the MapData
		 * if either the old or the new entry marks an obstacle, the Data will now mark this entry with obstacle
		 */
	
		
		if(this.x==newData.getX()&&this.y==newData.getY()){	
			if(newData.getIsObstacle()){
				this.isObstacle = true;
			}
		}
		
		//The newData is located before the actual entry in the MapData
		if(this.x>newData.getX()||(this.x==newData.getX()&&this.y>newData.getY())){
			//The actual entry is the first; newData becomes first instead
			if(this.previous == null){
				this.previous = newData;
				newData.next = this;
			}
			else {
			//NewData fits between the actual entry and the entry before
				if(this.previous.getX()<newData.getX()||(this.previous.getX()==newData.getX()&&this.previous.getY()<newData.getY())){
			
					this.previous.next = newData;
					newData.previous = this.previous;
					this.previous = newData;
					newData.next = this;
				}
			//NewData has a lower place in the order of the data
				else{
					this.previous.append(newData);
				}
			}	
		}
		
		//The newData is located behind the actual entry in the MapData
		if(this.x<newData.getX()||(this.x==newData.getX()&&this.y<newData.getY())){
			//The actual entry is the last; newData becomes last instead
			if(this.next==null){
				this.next = newData;
				newData.previous = this;
			}
			else{
			//NewData fits between the actual entry and the entry behind
				if(this.next.getX()>newData.getX()||(this.next.getX()==newData.getX()&&this.next.getY()>newData.getY())){
					
					this.next.previous = newData;
					newData.next = this.next;
					this.next = newData;
					newData.previous = this;
				}
			//NewData has a higher place in the order of the MapData	
				else{
					this.next.append(newData);
				}
			}
		}
	}
	
	
	/**
	 * This method searches in the Data whether the coordinates are marked as an obstacle or free area
	 * @param x_test: The x-coordinate of the Data the method searches for
	 * @param y_test: The y-coordinate of the Data the method searches for
	 * @return: true, if a MapData exists that marks the Coordinates as an obstacle
	 * 
	 * 
	 */
	public boolean isObstacle(long x_test, long y_test){
		MapData temp = this.getMapData(x_test, y_test);
		if(temp != null){
			return temp.getIsObstacle();
		}
		else{
			return false;
		}
	}
	
	
	/**
	 * This method searches for an entry in the Data with fitting coordinates
	 * @param x_test: The x-coordinate of the Data the method searches for
	 * @param y_test: The y-coordinate of the Data the method searches for
	 * @return: true, if an entry with fitting coordinates exists in the MapData
	 */
	public boolean isKnown(long x_test, long y_test){
		if(this.getMapData(x_test, y_test)!=null){
			return true;
		}
		else{
			return false;
		}
	}
	
	
	/**
	 * This method returns the x-coordinate of the MapData
	 * @return the x-coordinate of the MapData
	 */
	public int getX(){
		return this.x;
	}
	
	
	/**
	 * This method returns the y-coordinate of the MapData
	 * @return the y-coordinate of the MapData
	 */
	public int getY(){
		return this.y;
	}
	
	
	/**
	 * This method returns whether the Data is marked as an obstacle
	 * @return true, if this Data marks an obstacle, otherwise returns false
	 */
	public boolean getIsObstacle(){
		return this.isObstacle;
	}
	
	
	/**
	 * This method returns a MapData to fitting coordinates
	 * @param x_c: the x-coordinate of the MapData searched for
	 * @param y_c: the y-coordinate of the MapData searched for
	 * @return a MapData with fitting coordinates; null if there is no fitting MapData
	 */
	public MapData getMapData(long x_c, long y_c){
		//return true, if the actual entry has the coordinates searched for
				if(this.x==x_c && this.y==y_c){
					return this;
				}
				
				//if the coordinates are located before the actual entry
				if(this.x>x_c||(this.x==x_c&&this.y>y_c)){
					//if the previous entry has lower coordinates (or does not even exist), no fitting entry exists
					if(this.previous == null || this.previous.getX()<x_c|| (this.previous.getX()==x_c&&this.previous.getY()<y_c)){
						return null;
					}
					//otherwise search more in front of the data
					else{
						return this.previous.getMapData(x_c, y_c);
					}
				}
				
				//if the coordinates are located behind the actual entry
				if(this.x<x_c||(this.x==x_c&&this.y<y_c)){
					//if the next entry has higher coordinates (or does not even exist), no fitting entry exists
					if(this.next == null || this.next.getX()>x_c || (this.next.getX()==x_c&&this.next.getY()>y_c)){
						return null;
					}
					//otherwise search more in front of the data
					else{
						return this.next.getMapData(x_c, y_c);
					}
				}
				
				//THIS SHOULD NEVER BE THE CASE
				return null;
	}
}