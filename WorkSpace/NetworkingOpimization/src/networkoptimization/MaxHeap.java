package networkoptimization;

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

class MaxHeap {
	
	private int[] verticesName; //each cell stores the vertex ID
    private int[] data; //data[i] corresponds to vertices[i]
    private int[] pos; //vertexName = 3 is stored in verticesName[5] -> pos[3] = 5;
    private int actualSize; //actual size of the heap
    
	MaxHeap(int predefinedSize) {
		this.verticesName = new int[predefinedSize];
		this.data = new int[predefinedSize];
		this.pos = new int[predefinedSize]; 
		this.actualSize = 0;
    }
	
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
	
	int getHeapPosition(int vertexName) {
		return pos[vertexName];
	}
	
	void printPosition() {
		System.out.println("heap pos: ");
        for (int id = 0; id < verticesName.length; id++) {
            System.out.println("pos[" + id + "] = " + pos[id]); 
        }
        System.out.println();
        
	}
	
	int getMax() {
		return verticesName[0];
	}
	
	void insert(int vertexName, int value) {
		verticesName[actualSize] = vertexName;
		data[actualSize] = value;
		pos[vertexName] = actualSize;
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
	
	int remove(int nodeIdx) {
		//pos[verticesName[nodeIdx]] = 0;
		swap(actualSize-1, nodeIdx);
		actualSize--;
		if (data[nodeIdx] > data[getParentID(nodeIdx)]) {
			moveUp(nodeIdx);
		} else {
			moveDown(nodeIdx);
		}
		
		return verticesName[actualSize];
//		return data[actualSize];
	}
	
	int remove() {
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
	
	int getVertexName(int i) {
		return verticesName[i];
	}
	
	int getData(int i) {
		return data[i];
	}
	
	int getDataFromVertexName(int vertexName) {
		for (int i = 0; i < actualSize; i++) {
			if (vertexName == verticesName[i])
				return data[i];
		}
		
		return -1;
	}
	
	private void swap(int nodeA, int nodeB) {
		int temp = pos[verticesName[nodeB]];
		pos[verticesName[nodeB]] = pos[verticesName[nodeA]];
		pos[verticesName[nodeA]] = temp;
		temp = verticesName[nodeA];
		verticesName[nodeA] = verticesName[nodeB];
		verticesName[nodeB] = temp;
		temp = data[nodeA];
		data[nodeA] = data[nodeB];
		data[nodeB] = temp;
	}
	
	public void printHeap()
    {
		System.out.println("Root: VertexName -> " + getVertexName(0)+ " : " + getData(0));
        for (int id = 0; id <= actualSize / 2; id++) {
        	if (id < actualSize)
                System.out.print("(" + id + ")parent: " + verticesName[id] + " = " + data[id]);
        	if (2 * id + 1 < actualSize)
            	System.out.print("; (" + (2 * id + 1) + ")left: " + verticesName[2 * id + 1] + " = " + data[2 * id + 1]);
        	if (2 * id + 2 < actualSize)
            	System.out.print("; (" + (2 * id + 2) + ")right: " + verticesName[2 * id + 2] + " = " + data[2 * id + 2]);
        	System.out.println();
        }
        System.out.println("");
    }
	
	public void printHeapArray()
    {
		System.out.println("Root: VertexName -> " + getVertexName(0)+ " : " + getData(0));
        for (int id = 0; id < actualSize; id++) {
            System.out.println("(" + id + ")->" + verticesName[id] + " : " + data[id]); 
        }
        System.out.println();
        printPosition();
    }
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
        MaxHeap heapTest = new MaxHeap(9);
//        heapTest.insert(5, 16);
//        heapTest.insert(0, 18);
//        heapTest.insert(8, 14);
//        heapTest.insert(3, 19);
        

        
        
//      heapTest.printHeap();
        heapTest.insert(0, 6);
//        heapTest.printHeap();
        heapTest.insert(1, 10);
//        heapTest.printHeap();
        heapTest.insert(2, 20);
//        heapTest.printHeap();
        heapTest.insert(3, 22);
//        heapTest.printHeap();
        heapTest.insert(4, 33);
//        heapTest.printHeap();
        heapTest.insert(5, 3);
//        heapTest.printHeap();
        heapTest.insert(6, 99);
//        heapTest.printHeap();
        heapTest.insert(7, 24);
//        heapTest.printHeap();
        heapTest.insert(8, 42);
        heapTest.printHeap();
        
        heapTest.remove(0);
        heapTest.remove(0);
        heapTest.printHeap();
 
	}
}