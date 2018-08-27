/*Jason Grant
 * 7/23/18
 * CS 610-850 Summer 18
 * Programming Assignment #3 */
public class Node {//more robust version of Edge.java Class
	
	public Node(String name1, String name2) {
		this.name1=name1;
		this.name2=name2;
	}
	String name1;//names of the 2 vertices connected to vertex
	String name2;
	boolean visited = false;//used in truepath method to see if I have traveled across this edge already
	boolean shortpath = false;//used to see if edge is part of shortest path tree
	
	public String getName1() {
		return name1;
	}
	
	public String getName2() {
		return name2;
	}
	
	public boolean getVisit() {
		return visited;
	}
	
	public void setVisit(boolean hi) {
		visited = hi;
	}
	
	public boolean getShortPath() {
		return shortpath;
	}
	
	public void setShortPath(boolean hi) {
		shortpath = hi;
	}

}
