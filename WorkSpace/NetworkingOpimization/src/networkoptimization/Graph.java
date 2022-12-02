package networkoptimization;

import java.util.ArrayList;


/**
 * @author Stone
 * 
 * 
 */
class Graph {

	//Class members
	private int numVertices; //number of vertices in the graph needs to be predefined
	private int numEdges;
	private ArrayList<Edge> allEdges;
	private ArrayList<Edge>[] adjListMap; //list of edges; adjListMap[i] represents a list a edges of vertex i
	//To return an edge set of a vertex, do "adjListMap[i]".
	
	//Constructor
	Graph(int numVertices) {
		this.numVertices = numVertices;
		numEdges = 0;
		allEdges = new ArrayList<Edge>();
		adjListMap = new ArrayList[numVertices];
		
		for (int i = 0; i < numVertices; i++) {
			adjListMap[i] = new ArrayList<Edge>();
		}
	}

	void addEdge(int nodeA, int nodeB, int weight) {
		if (nodeA > numVertices || nodeA < 0 || nodeB > numVertices || 
				nodeB < 0 || weight < 0 || nodeA == nodeB )
			throw new IllegalArgumentException("Invalid arguments");

		if (this.getEdgeBtw(nodeA, nodeB) != null) {
//			System.out.println("Edge Exisiting, printing the graph: ");
//			this.printGraph();
			throw new NullPointerException("Edge existing");
		}
		
//		if (this.adjListMap[nodeA].contains(nodeB)) 
//			throw new NullPointerException("Edge existing");
		
		Edge newEdge = new Edge(nodeA, nodeB, weight);
		adjListMap[nodeA].add(newEdge);
		adjListMap[nodeB].add(newEdge);
		allEdges.add(newEdge);
		numEdges++;
	}
	
	void addEdge(Edge edge) {
		addEdge(edge.getNodeA(), edge.getNodeB(), edge.getWeight());
	}
	
	ArrayList<Edge> getEdgesofNode(int node) {
		return adjListMap[node];
	}
	
	Edge getEdgeBtw(int nodeA, int nodeB) {
		ArrayList<Edge> edgeSet;
		if (this.getEdgesofNode(nodeA).size() < this.getEdgesofNode(nodeB).size()) {
			edgeSet = this.getEdgesofNode(nodeA);
			for (Edge edge : edgeSet) if (edge.getNode(nodeA) == nodeB) return edge;
		} else {
			edgeSet = this.getEdgesofNode(nodeB);
			for (Edge edge : edgeSet) if (edge.getNode(nodeB) == nodeA) return edge;
		}
		return null;
	}

	int getNumVertices()
	{
		return numVertices;
	}

	int getNumEdges()
	{
		return numEdges;
	}

	ArrayList<Edge> getAllEdges() {
		return allEdges;
	}
	
	ArrayList<Edge>[] getMap() { 
		//TODO: a new copy or return the same one?
		return adjListMap;
	}

	void printGraphInfo() {
		System.out.println("numVertices: " + getNumVertices() + "; numEdges: " + getNumEdges() + "; avgDegree: " + getNumEdges()*2/getNumVertices() );
	}
	
	void printGraph() {
		System.out.println("numVertices: " + getNumVertices() + "; numEdges: " + getNumEdges() + "; avgDegree: " + getNumEdges()*2/getNumVertices() );
		for (int vertexIdx = 0; vertexIdx < this.getNumVertices(); vertexIdx++) {
			System.out.println(vertexIdx + ": (" + this.getEdgesofNode(vertexIdx).size() + " neighbors)");
			if (adjListMap[vertexIdx].isEmpty()) System.out.print("No incident edges!");
			else {
				for (Edge edge : adjListMap[vertexIdx]) {
					System.out.print("   ");
					edge.printEdge();
				}
			}
		}
	}
	
	
	//An algorithm for Max-Bandwidth-Path based on a modification of 
	  //Dijkstra’s algorithm without using a heap structure;
	int findMBPath_Baisc(int start, int goal) {
		int[] status = new int[getNumVertices()]; //unseen = 0; fringe = 1; seen = 2;
		int[] dad = new int[getNumVertices()];
		int[] bw = new int[getNumVertices()];
		ArrayList<Integer> fringeQueue = new ArrayList<Integer>();
		
		status[start] = 2; 
		dad[start] = -1;
		
		//enqueue all edges of starting node into the fringeQueue
		for (Edge edge : getEdgesofNode(start)) {
			int neighbor = edge.getNode(start);
			status[neighbor] = 1;
			dad[neighbor] = start;
			bw[neighbor] = edge.getWeight();
			fringeQueue.add(neighbor);
		}
		//System.out.println("added all neighbors of starting node: " + start + " to the fringeQueue");
		
		while (!fringeQueue.isEmpty()) {
//			System.out.println(fringeQueue);
			int fringeNameWithMaxBW = fringeQueue.get(0);
			int temp = 0;
			for (int i = 0; i < fringeQueue.size(); i++) {
				int fringe = fringeQueue.get(i);
				if (bw[fringe] > bw[fringeNameWithMaxBW]) {
					fringeNameWithMaxBW = fringe;
					temp = i;
				}
			}
			status[fringeNameWithMaxBW] = 2;
//			System.out.println("1. remove the root (max): " + fringeNameWithMaxBW + " : " + bw[fringeNameWithMaxBW]);
			fringeQueue.remove(temp);
			for (Edge edge : getEdgesofNode(fringeNameWithMaxBW)) {
				int neighbor = edge.getNode(fringeNameWithMaxBW);
				if (status[neighbor] == 0) {
					status[neighbor] = 1;
					dad[neighbor] = fringeNameWithMaxBW;
					bw[neighbor] = Integer.min(edge.getWeight(), bw[fringeNameWithMaxBW]); 
					fringeQueue.add(neighbor);
//					System.out.println("2. insert the vertex: " + neighbor + " : " + bw[neighbor]);
				} else if (status[neighbor] == 1 && 
						bw[neighbor] < Integer.min(edge.getWeight(), bw[fringeNameWithMaxBW])) {
//					System.out.println("3. update the node: " + neighbor);
					bw[neighbor] = Integer.min(edge.getWeight(), bw[fringeNameWithMaxBW]);
					dad[neighbor] = fringeNameWithMaxBW;
				}
			}
		}
		
		if (status[goal] != 2) {
			System.out.println("no path from " + start + " to " + goal);
			return -1; //no path from start to goal
		}
		else constructPath(start, goal, dad, bw);
		
		return bw[goal];
	}
	
	//An algorithm for Max-Bandwidth-Path based on a modification of 
	  //Dijkstra’s algorithm using a heap structure for fringes;
	int findMBPath_Heap(int start, int goal) {
		int[] status = new int[getNumVertices()]; //unseen = 0; fringe = 1; seen = 2;
		int[] dad = new int[getNumVertices()];
		int[] bw = new int[getNumVertices()];
		MaxHeap fringeQueue = new MaxHeap(getNumVertices());

		status[start] = 2; 
		dad[start] = -1;

		//enqueue all edges of starting node into the fringeQueue
		for (Edge edge : getEdgesofNode(start)) {
			int neighbor = edge.getNode(start);
			status[neighbor] = 1;
			dad[neighbor] = start;
			bw[neighbor] = edge.getWeight();
			fringeQueue.insert(neighbor, bw[neighbor]);
		}
		//System.out.println("added all neighbors of starting node: " + start + " to the fringeQueue");

		while (fringeQueue.size() > 0) {
//			fringeQueue.printHeapArray();
			int fringeNameWithMaxBW = fringeQueue.remove();
//			System.out.println("1. remove the root (max): " + fringeNameWithMaxBW + " : " + bw[fringeNameWithMaxBW]);
//			fringeQueue.printHeapArray();
			status[fringeNameWithMaxBW] = 2;
			for (Edge edge : getEdgesofNode(fringeNameWithMaxBW)) {
				int neighbor = edge.getNode(fringeNameWithMaxBW);
				if (status[neighbor] == 0) {
					status[neighbor] = 1;
					dad[neighbor] = fringeNameWithMaxBW;
					bw[neighbor] = Integer.min(edge.getWeight(), bw[fringeNameWithMaxBW]);
					fringeQueue.insert(neighbor, bw[neighbor]);
//					System.out.println("2. insert the vertex: " + neighbor + " : " + bw[neighbor]);
//					fringeQueue.printHeapArray();
				} else if (status[neighbor] == 1 && 
						bw[neighbor] < Integer.min(edge.getWeight(), bw[fringeNameWithMaxBW])) {
					int neighborPos = fringeQueue.getHeapPosition(neighbor);
					fringeQueue.remove(neighborPos);
//					System.out.println("3. update (remove) the node: " + neighbor);
//					fringeQueue.printHeapArray();
					bw[neighbor] = Integer.min(edge.getWeight(), bw[fringeNameWithMaxBW]);
					fringeQueue.insert(neighbor, bw[neighbor]);
//					System.out.println("3. update (insert) the node: " + neighbor);
//					fringeQueue.printHeapArray();
					dad[neighbor] = fringeNameWithMaxBW;
				}
			}
		}
		
		if (status[goal] != 2) {
			System.out.println("no path from " + start + " to " + goal);
			return -1; //no path from start to goal
		}
		else constructPath(start, goal, dad, bw);

		return bw[goal];
	}
	
	//An algorithm for Max-Bandwidth-Path based on a modification of 
	  //Kruskal’s algorithm, in which the edges are sorted by HeapSort.
	int findMBPath_Kruskals(int start, int goal) {
		int[] status = new int[getNumVertices()]; //unseen = 0; fringe = 1; seen = 2;
		int[] dad = new int[getNumVertices()];
		int[] bw = new int[getNumVertices()];
		int[] rank = new int[getNumVertices()];
		int[] color = new int[getNumVertices()]; //0 means white, 
		Graph maxSpTree = new Graph(getNumVertices());
		ArrayList<Edge> edgeList = new ArrayList<Edge>();
		//list all edges in order
		MaxSortingHeap edgeMaxHeap = new MaxSortingHeap(getNumEdges());
		for (Edge edge : allEdges) {
//			edge.printEdge();
			edgeMaxHeap.insert(edge);
		}
		//makeSet
		for (int vertex = 0; vertex < getNumVertices(); vertex++) {
			dad[vertex] = -1;
			rank[vertex] = 0;
		}
		//find and union
		for (int i = 0; i < getNumEdges(); i++) {
			Edge edge = edgeMaxHeap.remove();
			int rootA = findRoot(edge.getNodeA(), dad);
			int rootB = findRoot(edge.getNodeB(), dad);
			if (rootA != rootB) {
				union(rootA, rootB, dad, rank);
				maxSpTree.addEdge(edge);
			}
		}
		maxSpTree.bfs(start, goal, bw);
		return bw[goal];
	}

	public ArrayList<Integer> bfs(int start, int goal, int[] bw) {
		if (start >= getNumVertices() || goal >= getNumVertices() 
				|| start < 0 || goal < 0) return null;
		int[] parent = new int[getNumVertices()];
		ArrayList<Integer> queue = new ArrayList<Integer>();
		boolean[] visited = new boolean[getNumVertices()];
		queue.add(start);
		bw[start] = Integer.MAX_VALUE;
		while(!queue.isEmpty()) {
			int curr = queue.remove(0);
			if (curr == goal) return constructPath(start, goal, parent, bw);
			for (Edge edge : getEdgesofNode(curr)) {
				int neighbor = edge.getNode(curr);
				bw[neighbor] = Integer.min(edge.getWeight(), bw[curr]);
				if (!visited[neighbor]) {
					queue.add(neighbor);
					parent[neighbor] = curr;
					visited[neighbor] = true;
				}
			}
		}
		return null;
	}
	
	
	private int findRoot(int vertex, int[] dad)  {
		int curr = vertex;
		ArrayList<Integer> stack = new ArrayList<Integer>();
		while (dad[curr] != -1) { 
			stack.add(curr);
			curr = dad[curr];
		}
		while (!stack.isEmpty()) {
			int node = stack.remove(stack.size()-1);
			dad[node] = curr;
		}
		return curr;
	}
	
	private void union(int rootA, int rootB, int[] dad, int[] rank) {
		if (rank[rootA] > rank[rootB]) dad[rootB] = rootA;
		else if (rank[rootB] > rank[rootA]) dad[rootA] = rootB;
		else {
			dad[rootB] = rootA;
			rank[rootA]++;
		}
	}

	private ArrayList<Integer> constructPath(int start, int goal, int[] dad, int[] bw) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		int curr = goal;
		while (curr != start) {
			result.add(0, curr);
			curr = dad[curr];
		}
		result.add(0, start);
		//System.out.println("BandWidth = " + bw[goal] + " : " + result);
		return result;
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Graph testG1 = new Graph(8);
		testG1.addEdge(1, 2, 20);
		testG1.addEdge(3, 2, 20);
		testG1.addEdge(3, 4, 20);
		testG1.addEdge(5, 4, 20);
		testG1.addEdge(5, 6, 20);
		testG1.addEdge(6, 7, 20);
		testG1.addEdge(1, 0, 20);
		testG1.addEdge(0, 7, 20);
		
		try {
			testG1.addEdge(0, 7, 30);
		} catch (NullPointerException e) {
			System.out.println("Edge exisiting");
		}

		testG1.getEdgesofNode(1).get(0).setWeight(100);
		
		testG1.printGraph();
		
		System.out.println("find edge");
		testG1.getEdgeBtw(0,1).printEdge();
	}
	
}

