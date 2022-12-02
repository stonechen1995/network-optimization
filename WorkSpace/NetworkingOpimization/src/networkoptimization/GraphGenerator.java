/**
 * 
 */
package networkoptimization;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Stone
 * Random Graph Generation. Write subroutines that generate two kinds of “random” undirected graphs of 5000 vertices.
 * • In the first graph G1, the average vertex degree is 6;
 * • In the second graph G2, each vertex is adjacent to about 20% of the other vertices, which are randomly chosen;
 * • Randomly assign positive weights to edges in the graphs.
 * Your graphs should be ”random” enough. Therefore, in the graph G1, the pairs of vertices that are adjacent 
 *   should be chosen randomly. 
 * And in the graph G2, the number of neighbors and the neighbors of a vertex should be chosen randomly. 
 * To make sure that your graphs are connected, you should start with a cycle that contains 
 *   all vertices of the graphs, then add the rest edges randomly.
 */
class GraphGenerator {
	//class members
	private Random rand;
	private int numberOfVertices = 0;

	//Constructor
	GraphGenerator() {
		rand = new Random();
	}

	int generateRandomWeight() {
		return rand.nextInt(1000-10) + 11;
//		return rand.nextInt(10) + 11;
	}

	int generateRandomVertex() {
		return rand.nextInt(numberOfVertices);
	}

	Graph generateSparseGraph(int numVertices, int avgDegreeNum_predefined) {
		if (numVertices <= 0)
			throw new IllegalArgumentException("Number of vertices must be positive when generate a graph");
		if (avgDegreeNum_predefined < 2)
			throw new IllegalArgumentException("The choosen average degree is too small, less than 2");

		numberOfVertices = numVertices;
		//generate a connected and cyclic graph first
		Graph sparseGraph = generateConnectedGraph(numVertices, true);
		//System.out.println("totalDegree = " + sparseGraph.getNumEdges()*2);
		//generate the rest of the graph.
		for (int i = 0; i < numVertices; i++) {
			while (sparseGraph.getNumEdges()*2/numVertices < avgDegreeNum_predefined) {
				//System.out.println(sparseGraph.getNumEdges()*2/numVertices + " < " + avgDegreeNum_predefined);
				int nodeA = generateRandomVertex();
				int nodeB = generateRandomVertex();
				int weight = generateRandomWeight();
				//System.out.println("nodeA: " + nodeA + "; nodeB: " + nodeB + "; weight: " + weight);
				try {
					sparseGraph.addEdge(nodeA, nodeB, weight);
					//System.out.println("totalDegree = " + sparseGraph.getNumEdges()*2);
				} catch (Exception e) {
					;
				}
			}
		}
			return sparseGraph;
	}

	Graph generateDenseGraph(int numVertices, int degreePercentage_predefined) {
		if (numVertices <= 0)
			throw new IllegalArgumentException("Number of vertices must be positive when generate a graph");
		if (degreePercentage_predefined*numVertices/100 < 2)
			throw new IllegalArgumentException("The choosen average degree percentage is too small");

		numberOfVertices = numVertices;
		int requiredNumOfNeighbors = degreePercentage_predefined*numVertices/100;
//		System.out.println("requiredNeighborsNumber = " + requiredNumOfNeighbors);
		
		//generate a connected and cyclic graph first
		Graph denseGraph = generateConnectedGraph(numVertices, true);
//		System.out.println("totalDegree = " + denseGraph.getNumEdges()*2);
		//generate the rest of the graph.
		for (int vertexID = 0; vertexID < numVertices; vertexID++) {
//			System.out.println("verexID = " + vertexID + ": " + denseGraph.getEdgesofNode(vertexID).size() + " < " + requiredNeighborsNumber);
			int offset = 0;
			while (denseGraph.getEdgesofNode(vertexID).size() < requiredNumOfNeighbors - offset) {
				int otherNode = generateRandomVertex();
				int innerCount = 0;
				while (denseGraph.getEdgesofNode(otherNode).size() > requiredNumOfNeighbors) {
					otherNode = generateRandomVertex();
//					if (innerCount++ >= (int)((10/(double)numVertices)*5000)) {
//						System.out.println("innercounter = " + innerCount + " : " + vertexID + " - " + otherNode + ": all vertices incident greater than required number!");
						offset++;
						break;
//					}
				}
				int weight = generateRandomWeight();
//				System.out.println("nodeA: " + vertexID + "; nodeB: " + otherNode + "; weight: " + weight);
				try {
					denseGraph.addEdge(vertexID, otherNode, weight);
//					System.out.println();
//					System.out.println("totalDegree = " + denseGraph.getNumEdges()*2);
				} catch (NullPointerException e) {
					//System.out.println("Edge existing; again!");
				} catch (IllegalArgumentException e) {
					//System.out.println("Invalid arguments; again!");
				}
			}
//			System.out.println("verexID = " + vertexID + ": " + denseGraph.getEdgesofNode(vertexID).size() + " < " + requiredNeighborsNumber);
		}

		return denseGraph;
	}

	//generate a connected graph
	private Graph generateConnectedGraph(int numVertices, boolean isCyclic) {
		int startingNode = 0;
		int lastNode = 0;
		Graph connectedGraph = new Graph(numVertices);
		ArrayList<Integer> tempArray = new ArrayList<Integer>(); 
		for (int i = 1; i < numVertices; i++) tempArray.add(i);
		//System.out.println(tempArray);
		while (!tempArray.isEmpty() ) {
			int weight = generateRandomWeight(); //from 10 to 1000
			int otherNode = rand.nextInt(tempArray.size());
			if (tempArray.get(otherNode) == startingNode) continue; //should not happen
			//System.out.println("startingNode: " + startingNode + "; otherNode: " + otherNode + "; tempArray[otherNode]: " + tempArray.get(otherNode));
			try {
				connectedGraph.addEdge(startingNode, tempArray.get(otherNode), weight);
				//System.out.println("totalDegree = " + connectedGraph.getNumEdges()*2);
			} catch (Exception e) {
				continue;
			}
			startingNode = tempArray.get(otherNode);
			lastNode = tempArray.remove(otherNode);
			//System.out.println(tempArray);
		}
		//System.out.println("lastNode = " + lastNode);
		if (isCyclic) {
			connectedGraph.addEdge(0, lastNode, generateRandomWeight());
		}
		return connectedGraph;
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Create a randomgraph object
		GraphGenerator randomGraph = new GraphGenerator();

		//create a random sparse graph
		Graph sparseGraph = randomGraph.generateSparseGraph(5000,6);
		// Print the graph
		System.out.println("The generated random sparse graph: ");
		sparseGraph.printGraphInfo();

		// Create a random dense graph
		Graph denseGraph = randomGraph.generateDenseGraph(5000,20);
		// Print the graph
		System.out.println("The generated random dense graph: ");
		denseGraph.printGraphInfo();
	}

}
