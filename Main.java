/*Jason Grant
 * 7/23/18
 * CS 610-850 Summer 18
 * Programming Assignment #3 */
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);//scan for user input
		
		System.out.println("Hey there Zhenliang, please input the total number of airports for this simulation: ");
		int size = scanner.nextInt();
		
		System.out.println("Enter the name of the origin Airport: ");
		String source = scanner.next();
		
		System.out.println("Enter the name of the destination Airport: ");
		String finale = scanner.next();
		
		System.out.println("Enter Source to Destination distance values for any of the "+size+" Airports in this simulation (Type 'Done' when you are finished):");
		
		String [] edges = new String [size];
		int back = 0;
		int stop = 0;
		
		while(stop == 0) {//collect all user-input edges
			if (back == edges.length -1) { //if circular array is about to be full, double it, then continue adding edges
				String [] nfc = new String [edges.length*2];
				
				for(int i = 0; i < edges.length; i ++) {
					nfc[i] = edges[i];
				}
				edges = nfc;
			}
			String next = scanner.next();
			if(next.equals("Done")) {
				stop = 1;
			}
			else {
				edges[back] = next;
				back = (back +1)  % (edges.length);
			}
		}
		int edgeqlen = 0;
		for(int i = 0; i < edges.length; i ++) {//used to determine how big edge array should be
			if(edges[i] != null) {
				edgeqlen++;
			}
		}
		
		Edge [] edgeQ = new Edge [edgeqlen/3];//array to hold all the edges
		int pos = 0;
		
		for(int q = 0; q < edges.length && edges[q]!=null; q=q+3) {//used to fill edgeQ array
			Edge cool = new Edge (edges[q], edges[q+1], Integer.parseInt(edges[q+2]));
			edgeQ[pos]= cool;
			pos++;
		}
		
		Vertices [] vectorQ = new Vertices [size];//array to hold all the vertices
		vectorQ[0] = new Vertices (source);
		vectorQ[size-1] = new Vertices (finale);
		int vPlace = 1;
		
		try {
			for(int h = 0; h < edgeQ.length; h ++) {//used to fill vectorQ
				String name1 = edgeQ[h].getSource();
				String name2 = edgeQ[h].getDesti();
				
				int here1 =0;
				int here2=0;
				for(int x = 0; x < vectorQ.length; x++) {//make sure to only add vertices that are not already in vectorQ
					if(vectorQ[x]!= null && name1.equals(vectorQ[x].getName())) {
						here1++;
					}
				}
				
				if(here1 ==0) {
					Vertices newv = new Vertices(name1);
					vectorQ[vPlace]= newv;
					vPlace++;
				}
				
				for(int x = 0;  x < vectorQ.length; x++) {
					if(vectorQ[x]!= null && name2.equals(vectorQ[x].getName())) {
						here2++;
					}
				}
				
				if(here2 ==0) {
					Vertices newv = new Vertices(name2);
					vectorQ[vPlace]= newv;
					vPlace++;
				}
			}
			System.out.println();

			
			DistV [] coolbeans = dijk(vectorQ, edgeQ);//perform Dijkstra's Algorithm to create array of distance vectors with shortest distance from source node to all other nodes

			
			Edge [] shorttree = shortPath(coolbeans, edgeQ); //find edges that would be apart of a shortest path tree for this graph

			String [] finpath = truePath(shorttree, vectorQ,  source, finale);//used to find a shortest path from source to destination using only edges part of shortest path tree
			
			System.out.print("The Shortest Distance from "+source+" to "+finale+" is: ");
			for(int y =0; y < coolbeans.length;y++) {
				if(coolbeans[y].getName().equals(finale)) {
					System.out.print(coolbeans[y].getDistance());
					break;
				}
			}
			System.out.println();
			System.out.print("One of the Shortest Paths from "+source+" to "+finale+" is as follows: "+source);
			for(int x = 1; x < finpath.length; x++) {
				if(finpath[x]!=null) {
					System.out.print(" -> "+finpath[x]);
				}
			}
			

		}
		catch(ArrayIndexOutOfBoundsException exception) {//only occurs when more nodes that expected/improper edge format
			System.out.println("The number of nodes entered does not match the number of nodes referenced in Source to Destination values");
			System.out.println("Either that or your input did not match expected input formatting");
		}
		
		scanner.close();
	}
	
	public static String [] truePath(Edge [] shorttree,Vertices [] vecs, String sauce, String endin) {//find shortest path from source node to destination node
		int vecnum = 0;
		for(int i = 0; i < vecs.length;i++) {
			if(vecs[i]!=null)
				vecnum++;
		}
		
		Vertices [] coolvecs = new Vertices[vecnum];//array to exclude any potential null values in vertices array
		int entry = 0;
		for(int j =0; j < vecs.length; j++) {//intialize coolvecs array
			if(vecs[j]!=null) {
				coolvecs[entry]= vecs[j];
				entry++;
			}
		}
		
		Node [][] system = new Node[vecnum][vecnum];//2-d array to represent travel from one node to any other node
		for(int x =0; x < system.length; x++) {//initialize system 2d array
			for(int y=0; y< system[x].length; y++) {
				system[x][y]= new Node(coolvecs[x].getName(),coolvecs[y].getName());
			}
		}
		
		for(int x =0; x < system.length; x++) {//mark shortest path edges in graph
			for(int y=0; y< system[x].length; y++) {
				for(int z = 0; z < shorttree.length; z++) {
					if(shorttree[z]!=null) {
						if(system[x][y].getName1().equals(shorttree[z].getDesti())||system[x][y].getName1().equals(shorttree[z].getSource())){
							if(system[x][y].getName2().equals(shorttree[z].getDesti())||system[x][y].getName2().equals(shorttree[z].getSource())){
								system[x][y].setShortPath(true);
							}
						}
					}

				}
			}
		}
		
		int currentpos = 0;
		for(int x =0; x < system.length; x++) {//find starting column in systems 2d array for source vertex
			if(system[0][x].getName2().equals(sauce)) {
				currentpos=x;
				break;
			}
		}
		
		boolean foundit= false; //boolean to indicate we have reach our desired destination vertex
		int cursor=1; //used to indicate postion in finpath array
		String [] finpath = new String [coolvecs.length];//this should eventually hold the path to take from source to destination node
		finpath[0]= sauce;
		
		int q = 0;
		outerloop:
		while(!foundit) {

				if(system[q][currentpos].getShortPath()) {//check if current edge is a shortest path
					if(!system[q][currentpos].getVisit()) {//check if i have visited this node already
						
						if(!system[q][currentpos].getName1().equals(system[q][currentpos].getName2())) {//make sure its not a JFK -> JFK type edge
							
							finpath[cursor]= system[q][currentpos].getName1();
							cursor++;
							system[q][currentpos].setVisit(true);//covers case with JFK to LAX and LAX to JFK edge are both counted as visisted
							system[currentpos][q].setVisit(true);
							
							if(finpath[cursor-1].equals(endin)) {//if i found destination, break from loop and change boolean to true
								foundit=true;
								break outerloop;
							}
							else {
								for(int x =0; x < system.length; x++) {//else change currentpos variable and go again. check in a new column
									if(system[0][x].getName2().equals(finpath[cursor-1])) {
										currentpos=x;
										q =0;
										
									}
								}
								
							}
						}
						else {
							if(q == system.length-1) {//if I went down wrong path, delete most recently entered vertex and go along new potential shortest path
								cursor--;
								for(int x =0; x < system.length; x++) {//else change currentpos variable and go again
									if(system[0][x].getName2().equals(finpath[cursor-1])) {
										currentpos=x;
										q =0;
										
									}
								}
							}
							else {//other wise continue searching down column for shortest path
								q++;
							}

						}
						
					}
					else {
						if(q == system.length-1) {//if I went down wrong path, delete most recently entered vertex and go along new potential shortest path
							cursor--;
							for(int x =0; x < system.length; x++) {//else change currentpos variable and go again
								if(system[0][x].getName2().equals(finpath[cursor-1])) {
									currentpos=x;
									q =0;
									
								}
							}
						}
						else {//other wise continue searching down column for shortest path
							q++;
						}

					}
				}
				else {
					if(q == system.length-1) {//if I went down wrong path, delete most recently entered vertex and go along new potential shortest path
						cursor--;
						for(int x =0; x < system.length; x++) {//else change currentpos variable and go again
							if(system[0][x].getName2().equals(finpath[cursor-1])) {
								currentpos=x;
								q =0;
								
							}
						}

					}
					else {//other wise continue searching down column for shortest path
						q++;
					}

				}
			
		}
		
		return finpath;
	}
	
	
	public static Edge [] shortPath(DistV [] dv, Edge [] edges) {//build shortest path tree
		Edge [] path = new Edge [edges.length];//contains only edges that can be part of shortest path
		int position = 0;
		
		for(int i = 0; i < edges.length; i++) {//take each edge and see if it is a shortest path by comparing it to distance vectors
			String name1 = edges[i].getDesti();
			String name2 = edges[i].getSource();
			
			for (int k =0; k < dv.length; k++) {
				if(dv[k].getName().equals(name1)) {
					for(int j=0; j < dv.length; j++) {
						if(dv[j].getName().equals(name2)) {//use |D(x) - D(y)| = dist(x,y) formula to see if edge is part of shortest path tree
							if(edges[i].getAirFare() == Math.abs(dv[j].getDistance() - dv[k].getDistance())) {
								path[position]= edges[i];
								position++;
							}
						}
					}
				}
			}
		}
		return path;
	}

	public static DistV [] dijk(Vertices [] nodes, Edge [] arcs) {//execute Dijkstra's Algorithm
		
		DistV [] dv = new DistV [nodes.length]; //create distance vectors from source node to all other nodes
		
		for(int i = 0; i < nodes.length; i++) {
			dv[i]  = new DistV(nodes[i].getName(), 999999999);//large number to represent infinity, or unknown distance
		}
		
		dv[0].setDistance(0); // distance from source node to itself is 0
		for(int j = 1; j < dv.length; j++) {//initialize distance vector array with values of distance from source node to known neighbors
			String sauce = dv[0].getName();
			String initdest = dv[j].getName();
			
			for (int q = 0; q < arcs.length; q++) {
				if(arcs[q].getSource().equals(sauce) || arcs[q].getDesti().equals(sauce)) {
					if(arcs[q].getSource().equals(initdest) || arcs[q].getDesti().equals(initdest)) {
						dv[j].setDistance(arcs[q].getAirFare());
					}
				}
			}
		}
		
		int min = 1;
		int cc = 0;

		while(cc < dv.length) {//update dv after going through priority q of nodes

			for(int q = 1; q <dv.length; q ++) {//find find not null dv and make it min D(v)
				if(!dv[q].getVecced()) {
					min = q;
				}
			}
			
			for(int g = 1; g < dv.length; g++) {//see if new min is indeed the min for DV array
				if(!dv[g].getVecced() && (dv[g].getDistance() < dv[min].getDistance())) {
					min = g;
				}
			}

				
				String cool = nodes[min].getName();
				for (int q = 0; q < arcs.length; q++) {//use min DV to update distance vector values for all other DVs
					if(arcs[q].getSource().equals(cool) || arcs[q].getDesti().equals(cool)) {
						String nice = arcs[q].getSource().equals(cool) ? arcs[q].getDesti() : arcs[q].getSource(); //other node
						for(int l = 0; l < dv.length; l++) {
							if( dv[l].getVecced()==false && dv[l].getName().equals(cool)) {
								for(int y = 0; y < dv.length; y++) {
									if(dv[y].getVecced()==false && dv[y].getName().equals(nice)) {
										if( dv[l].getDistance() + arcs[q].getAirFare() < dv[y].getDistance()) {
											dv[y].setDistance(dv[l].getDistance() + arcs[q].getAirFare());
										}
									}
								}
							}
						}
					}

				}
				dv[min].setVecced(true);//mark this DV so that I essentially "remove" it from Priority Q and don't need to update its values
				cc++;
		}
		return dv;
	}

}
