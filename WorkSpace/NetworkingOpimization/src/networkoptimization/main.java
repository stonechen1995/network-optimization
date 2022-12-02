package networkoptimization;

import java.util.ArrayList;
import java.util.Random;

class main {

	static void test1(int testCount) {
		GraphGenerator test1 = new GraphGenerator();
		
		Graph sparseGraph = test1.generateSparseGraph(100, 6);
		Graph denseGraph = test1.generateDenseGraph(100, 20);
		
		//sparseGraph.printGraph();
		int result1 = 0;
		int result2 = 0;
		int result3 = 0;
		int result4 = 0;
		int count = 0;
		while(result1 == result2 && result3 == result4 && count++ < testCount) {
			//System.out.println("=====================================");
			sparseGraph = test1.generateSparseGraph(100, 6);
			result1 = sparseGraph.findMBPath_Heap(8, 9);
			result2 = sparseGraph.findMBPath_Baisc(8, 9);
			denseGraph = test1.generateDenseGraph(100, 20);
			result3 = denseGraph.findMBPath_Heap(8, 9);
			result4 = denseGraph.findMBPath_Baisc(8, 9);
			//break;
		}
		if (count >= testCount) System.out.println("test1 all good");
		else System.out.println("test1 failed");
	}
	
	static void test2(int testCount) throws Exception {
		GraphGenerator test2 = new GraphGenerator();
		
		
		int result1 = 0;
		int result2 = 0;
		int result3 = 0;
		int result4 = 0;
		int result5 = 0;
		int result6 = 0;
		int count = 0;
		while(result1 == result2 && result2 == result3 &&
				result4 == result5 && result5 == result6 && count++ < testCount) {
			//System.out.println("=====================================");
			Graph sparseGraph = test2.generateSparseGraph(5000, 6);
			Graph denseGraph = test2.generateDenseGraph(5000, 20);
//			sparseGraph.printGraph();
			result1 = sparseGraph.findMBPath_Baisc(3, 4);
			result2 = sparseGraph.findMBPath_Heap(3, 4);
			result3 = sparseGraph.findMBPath_Kruskals(3, 4);
			result4 = denseGraph.findMBPath_Baisc(3, 4);
			result5 = denseGraph.findMBPath_Heap(3, 4);
			result6 = denseGraph.findMBPath_Kruskals(3, 4);
			//break;
		}
		if (count >= testCount) System.out.println("test2 all good");
		else {
			System.out.println("test2 failed");
			throw new Exception("test failed");
		}
	}
	
	static void mainTest() {
		GraphGenerator graphGenerator = new GraphGenerator();
		Random vertexIDGenerator = new Random();
		int predefinedSize = 5000;
		int requiredDegree = 6;
		int requiredPercentage = 20;
		Graph[] sparseGraph = new Graph[5];
		Graph[] denseGraph = new Graph[5];
		int[] start = new int[5];
		int[] goal = new int[5];
		

		//generate 5 random pairs of s-t vertices
		for (int i = 0; i < 5; i++) {
			start[i] = vertexIDGenerator.nextInt(5000);
			goal[i] = vertexIDGenerator.nextInt(5000);
		}
		//generate 5 random pairs of sparseGraph and denseGraph
		System.out.println("Starting to generate 5 pairs of graph");
		for (int i = 0; i < 5; i++) {
			sparseGraph[i] = graphGenerator.generateSparseGraph(predefinedSize, requiredDegree);
			denseGraph[i] = graphGenerator.generateDenseGraph(predefinedSize, requiredPercentage);
			System.out.println("Finished generated No." + i + " pair of graphs");
		}
		//start to run the test using three different algorithms.
		int[] resultSparseBasic = new int[5];
		int[] resultSparseHeap = new int[5];
		int[] resultSparseKruskals = new int[5];
		int[] resultDenseBasic = new int[5];
		int[] resultDenseHeap = new int[5];
		int[] resultDenseKruskals = new int[5];
		long[] durationSparseBasic = new long[5];
		long[] durationSparseHeap = new long[5];
		long[] durationSparseKruskals = new long[5];
		long[] durationDenseBasic = new long[5];
		long[] durationDenseHeap = new long[5];
		long[] durationDenseKruskals = new long[5];

		
		for (int i = 0; i < 5; i++) {
			System.out.println("Starting No." + i + " testing: ");
			
			long startTime = System.nanoTime();
			resultSparseBasic[i] = sparseGraph[i].findMBPath_Baisc(start[i], goal[i]);
			long endTime = System.nanoTime();
			durationSparseBasic[i] = endTime - startTime;
			
			startTime = System.nanoTime();
			resultSparseHeap[i] = sparseGraph[i].findMBPath_Heap(start[i], goal[i]);
			endTime = System.nanoTime();
			durationSparseHeap[i] = endTime - startTime;
			
			startTime = System.nanoTime();
			resultSparseKruskals[i] = sparseGraph[i].findMBPath_Kruskals(start[i], goal[i]);
			endTime = System.nanoTime();
			durationSparseKruskals[i] = endTime - startTime;
			
			startTime = System.nanoTime();
			resultDenseBasic[i] = denseGraph[i].findMBPath_Baisc(start[i], goal[i]);
			endTime = System.nanoTime();
			durationDenseBasic[i] = endTime - startTime;
			
			startTime = System.nanoTime();
			resultDenseHeap[i] = denseGraph[i].findMBPath_Heap(start[i], goal[i]);
			endTime = System.nanoTime();
			durationDenseHeap[i] = endTime - startTime;
			
			startTime = System.nanoTime();
			resultDenseKruskals[i] = denseGraph[i].findMBPath_Kruskals(start[i], goal[i]);
			endTime = System.nanoTime();
			durationDenseKruskals[i] = endTime - startTime;
			
			if (resultSparseBasic[i] == resultSparseHeap[i] && 
					resultSparseHeap[i] == resultSparseKruskals[i] && 
					resultDenseBasic[i] ==  resultDenseHeap[i] &&
					resultDenseHeap[i] == resultDenseKruskals[i]) {
				System.out.println("No." + i + " testing are all good;");
			} else {
				System.out.println("No." + i + " testing failed");
				break;
			}
		}
		System.out.println();
		for (int i = 0; i < 5; i++) {
			System.out.println("The running duration for No." + i + " Sparse Graph: ");
			System.out.println("Basic testing:   " + durationSparseBasic[i]/100000 + " ms, MaximumBW = " + resultSparseBasic[i]);
			System.out.println("Heap testing:    " + durationSparseHeap[i]/100000 + " ms, MaximumBW = " + resultSparseHeap[i]);
			System.out.println("Kruskal testing: " + durationSparseKruskals[i]/100000 + " ms, MaximumBW = " + resultSparseKruskals[i]);
//			System.out.println();
			System.out.println("The running duration for No." + i + " Dense Graph: ");
			System.out.println("Basic testing:   " + durationDenseBasic[i]/100000 + " ms, MaximumBW = " + resultDenseBasic[i]);
			System.out.println("Heap testing:    " + durationDenseHeap[i]/100000 + " ms, MaximumBW = " + resultDenseHeap[i]);
			System.out.println("Kruskal testing: " + durationDenseKruskals[i]/100000 + " ms, MaximumBW = " + resultDenseKruskals[i]);
			System.out.println();
		}
		
		int totalTimeSparseBasic = 0;
		int totalTimeSparseHeap = 0;
		int totalTimeSparseKruskal = 0;
		int totalTimeDenseBasic = 0;
		int totalTimeDenseHeap = 0;
		int totalTimeDenseKruskal = 0;
		
		for (int i = 0; i < 5; i++) {
			totalTimeSparseBasic += durationSparseBasic[i]/100000;	
			totalTimeSparseHeap += durationSparseHeap[i]/100000;
			totalTimeSparseKruskal += durationSparseKruskals[i]/100000;
			totalTimeDenseBasic += durationDenseBasic[i]/100000;
			totalTimeDenseHeap += durationDenseHeap[i]/100000;
			totalTimeDenseKruskal += durationDenseKruskals[i]/100000;

		}
		System.out.println("totalTimeSparseBasic = " + totalTimeSparseBasic + " ms");
		System.out.println("totalTimeSparseHeap = " + totalTimeSparseHeap + " ms");
		System.out.println("totalTimeSparseKruskal = " + totalTimeSparseKruskal + " ms");
		System.out.println("totalTimeDenseBasic = " + totalTimeDenseBasic + " ms");
		System.out.println("totalTimeDenseHeap = " + totalTimeDenseHeap + " ms");
		System.out.println("totalTimeDenseKruskal = " + totalTimeDenseKruskal + " ms");
		System.out.println("totalTimeBasic = " + (totalTimeSparseBasic + totalTimeDenseBasic) + " ms");
		System.out.println("totalTimeHeap = " + (totalTimeSparseHeap + totalTimeDenseHeap) + " ms");
		System.out.println("totalTimeKruskal = " + (totalTimeSparseKruskal + totalTimeDenseKruskal) + " ms");
		
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		test1();
//		test2(10);
		
//		GraphGenerator test2 = new GraphGenerator();
//		Graph sparseGraph = test2.generateSparseGraph(10, 3);
//		MaxSortingHeap msHeap = new MaxSortingHeap(sparseGraph.getNumVertices());
//		msHeap.insert(sparseGraph.);
		
		mainTest();
	}

}
