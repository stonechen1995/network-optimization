/**
 * 
 */
package networkoptimization;

/**
 * @author Stone
 *
 */
class Edge {

	private int nodeA;
	private int nodeB;
	private int weight;

	Edge(int nodeA, int nodeB, int weight) {
		if (nodeA == nodeB) throw new IllegalArgumentException("Both nodes on the edge should not be the same!");

		this.nodeA = nodeA; 
		this.nodeB = nodeB;
		this.weight = weight;
	}
	
	//if no such node, return -1
	int getNode(int srcNode) {
		if (srcNode == nodeA)
			return nodeB;
		else if (srcNode == nodeB)
			return nodeA;
		return -1;
	}
	
	int getNodeA() {
		return nodeA;
	}
	
	int getNodeB() {
		return nodeB;
	}
	
	int getWeight() {
		return this.weight;
	}

	void printEdge() {
		System.out.println(nodeA + " - "+ nodeB + ": weight: " + weight);
	}
	
	void setWeight(int newWeight) {
		this.weight = newWeight;
	}
	
	@Override
	public
	String toString() {
		return nodeA + " - "+ nodeB + ": weight: " + weight;
	}

	/*
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Edge edge = new Edge(1, 2, 10);
		edge.printEdge();
	}*/

}
