package networkoptimization;

import java.util.ArrayList;

/**
 * @author Stone
 * Heap Structure Write subroutines for the max-heap structure. 
 * In particular, your implementation should include subroutines for maximum, insert, and delete.
 * Since the heap structure you implement will be used for a Dijkstra-style algorithm in the routing protocol, 
 * we suggest the following data structures in your implementation:
 * (done)• The vertices of a graph are named by integers 0, 1, . . ., 4999;
 * (done)• The heap is given by an array H[5000], where each element H[i] gives the name of a vertex in the graph;
 * (done)• The vertex “values” are given in another array D[5000]. 
 *     Thus, to find the value of a vertex H[i] in the heap, we can use D[H[i]].
 * • In the operation delete(v) that deletes the vertex v from the heap H[5000], 
 *     you need to find the position of the vertex in the heap. For this, 
 *     you can use another array P[5000] so that P[v] is the position (i.e., index) of vertex v in the heap H[5000]. 
 *     Note that this array P[5000] should be modified accordingly when you move vertices in the heap H[5000].
 */

class MaxSortingHeap {
	
	private Edge[] edges; //each cell stores the an edge
    private int[] data; //data[i] stores the weight o the 
    private int actualSize; //actual size of the heap
    
    MaxSortingHeap(int predefinedSize) {
		this.edges = new Edge[predefinedSize];
		this.data = new int[predefinedSize];
		this.actualSize = 0;
    }
    
//    MaxSortingHeap(Edge[] edges) {
//    	this.edges = edges;
//		this.edges = new Edge[predefinedSize];
//		this.data = new int[predefinedSize];
//		this.actualSize = 0;
//    }
	
	int size() {
		return actualSize;
	}
	
	private int getLeftChildNodeID(int nodeIdx) {
		return 2 * nodeIdx + 1;
	}
	
	private int getRightChildNodeID(int nodeIdx) {
		return 2 * nodeIdx + 2;
	}
	
	private int getParentID(int nodeIdx) {
		return (nodeIdx - 1) / 2;
	}
	
	Edge getMax() {
		return edges[0];
	}
	
	void insert(Edge edge) {
		int value = edge.getWeight();
		edges[actualSize] = edge;
		data[actualSize] = value;
		moveUp(actualSize);
        actualSize++;
	}
	
	private void moveUp(int nodeIdx) {
		//System.out.println("move up: nodeIdx = " + nodeIdx + " > 0; data[nodeIdx] = " + data[nodeIdx] 
				//+ " < data[getParentID(nodeIdx)] = " + data[getParentID(nodeIdx)]);
		if (nodeIdx > 0 && data[nodeIdx] > data[getParentID(nodeIdx)]) {
			//System.out.println("swapped");
			swap(nodeIdx, getParentID(nodeIdx));
			moveUp(getParentID(nodeIdx));
		}
	}
	
	Edge remove(int nodeIdx) {
		//pos[verticesName[nodeIdx]] = 0;
		swap(actualSize-1, nodeIdx);
		actualSize--;
		if (data[nodeIdx] > data[getParentID(nodeIdx)]) {
			moveUp(nodeIdx);
		} else {
			moveDown(nodeIdx);
		}
		
		return edges[actualSize];
//		return data[actualSize];
	}
	
	Edge remove() {
		return remove(0);
	}
	
	private void moveDown(int nodeIdx) {
		int temp = swapParentWithChildren(nodeIdx);
		if (temp != -1) 
			moveDown(temp);
	}
	
	//swap the parent with the largest child; return true if swapped, otherwise return false;
	private int swapParentWithChildren(int parentIdx) { 
		//System.out.println("swapParentWithChildren starts: " + "parentDataIdx = " + parentIdx);
		int maxIdx = parentIdx;
		
		if (getLeftChildNodeID(parentIdx) < actualSize && 
				data[getLeftChildNodeID(parentIdx)] > data[maxIdx])
			maxIdx = getLeftChildNodeID(parentIdx);
		if (getRightChildNodeID(parentIdx) < actualSize && 
				data[getRightChildNodeID(parentIdx)] > data[maxIdx])
			maxIdx = getRightChildNodeID(parentIdx);

		if (maxIdx != parentIdx) {
			swap(maxIdx, parentIdx);
			//printHeap();
			//System.out.println("swapped successed");
			return maxIdx; 
		}
		
		//System.out.println("swapped failed");
		return -1;
	}
	
	Edge getEdge(int i) {
		return edges[i];
	}
	
	int getData(int i) {
		return data[i];
	}
	
	int getDataFromEdge(Edge edge) {
		for (int i = 0; i < actualSize; i++) {
			if (edge == edges[i])
				return data[i];
		}
		
		return -1;
	}
	
	private void swap(int nodeA, int nodeB) {
		Edge tempEdge = edges[nodeA];
		edges[nodeA] = edges[nodeB];
		edges[nodeB] = tempEdge;
		int tempWeight = data[nodeA];
		data[nodeA] = data[nodeB];
		data[nodeB] = tempWeight;
	}
	
	public void printHeap()
    {
		System.out.println("Root: edgeID -> " + getEdge(0)+ " : " + getData(0));
        for (int id = 0; id <= actualSize / 2; id++) {
        	if (id < actualSize)
                System.out.print("(" + id + ")parent: " + edges[id] + " = " + data[id]);
        	if (2 * id + 1 < actualSize)
            	System.out.print("; (" + (2 * id + 1) + ")left: " + edges[2 * id + 1] + " = " + data[2 * id + 1]);
        	if (2 * id + 2 < actualSize)
            	System.out.print("; (" + (2 * id + 2) + ")right: " + edges[2 * id + 2] + " = " + data[2 * id + 2]);
        	System.out.println();
        }
        System.out.println("");
    }
	
	public void printHeapArray()
    {
		System.out.println("Root: edgeID -> " + getEdge(0)+ " : " + getData(0));
        for (int heapID = 0; heapID < actualSize; heapID++) {
            System.out.println("(" + heapID + ")->" + edges[heapID] + " : " + data[heapID]); 
        }
        System.out.println();
    }
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
        MaxSortingHeap heapTest = new MaxSortingHeap(9);
        heapTest.printHeap();
        heapTest.insert(new Edge(0, 1, 6));
        heapTest.printHeap();
        heapTest.insert(new Edge(0, 2, 10));
        heapTest.printHeap();
        heapTest.insert(new Edge(0, 3, 12));
        heapTest.printHeap();
        heapTest.insert(new Edge(0, 4, 3));
        heapTest.printHeap();
        heapTest.insert(new Edge(0, 5, 5));
        heapTest.printHeap();
        heapTest.insert(new Edge(0, 6, 9));
        heapTest.printHeap();
        heapTest.insert(new Edge(0, 7, 1));
        heapTest.printHeap();
        heapTest.insert(new Edge(0, 8, 64));
        heapTest.printHeap();
        heapTest.insert(new Edge(0, 9, 26));
        heapTest.printHeap();
        
        heapTest.remove(0);
        heapTest.remove(0);
        heapTest.printHeap();
        heapTest.printHeapArray();*/
		
		GraphGenerator test2 = new GraphGenerator();
		Graph sparseGraph = test2.generateSparseGraph(10, 3);
		sparseGraph.printGraphInfo();
		MaxSortingHeap heapTest2 = new MaxSortingHeap(sparseGraph.getNumEdges());
		ArrayList<Edge> allEdges = sparseGraph.getAllEdges();
		for (Edge e : allEdges) {
			e.printEdge();
			heapTest2.insert(e);
		}
 
		heapTest2.printHeap();
	}
}