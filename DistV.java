/*Jason Grant
 * 7/23/18
 * CS 610-850 Summer 18
 * Programming Assignment #3 */
public class DistV {//class to hold distance from source node to each of the other nodes in the graph
	public DistV(String name, int distance) {
		this.name = name;
		this.distance=distance;
		vecced=false;
	}
	
	String name;//name of the node that we are traveling to from source node
	int distance =0; //distance from source node to node identified by String name variable
	boolean vecced;//used in Dijkstra's Algorithm to "remove" distance vector with minimum value from DV Queue
	
	public String getName() {
		return name;
	}
	
	public int getDistance() {
		return distance;
	}
	
	public void setDistance(int ice) {
		this.distance = ice;
	}
	
	public boolean getVecced() {
		return vecced;
	}
	
	public void setVecced(boolean hi) {
		vecced=hi;
	}
}
