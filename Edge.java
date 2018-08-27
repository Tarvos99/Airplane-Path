/*Jason Grant
 * 7/23/18
 * CS 610-850 Summer 18
 * Programming Assignment #3 */
public class Edge {//class to contain information about edges in graph
	public Edge (String source, String desti, int airfare) {
		this.source=source;//source vertex that is part of this edge
		this.desti=desti;//destination vertex that is part of this edge
		this.airfare=airfare;//airfare value from source to destination vertex; or just distance between the 2 nodes
	}
	
	String source;
	String desti;
	int airfare;
	
	public int getAirFare() {
		return airfare;
	}
	
	public String getSource() {
		return source;
	}
	
	public String getDesti() {
		return desti;
	}

}
