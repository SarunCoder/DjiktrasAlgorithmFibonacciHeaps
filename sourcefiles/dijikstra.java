/**
 * @author Sakthivel Manikam Arunachalam
 * @version 1.0
 */

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.InputMismatchException;
/**
 * Main class to run the project execution.
 * @author Sakthivel Manikam Arunachalam
 * @version 1.0
 */
public final class dijikstra {

    /**
     * Static strings for Exceptions.
     */
    private static final String WRONG_NUMBER_ARGUMENTS = "Wrong number of arguments in input";
    private static final String WRONG_ARGUMENTS = "Wrong arguments in input";
    private static final String WRONG_FILE_INPUTS = "Wrong edge inputs in file";
    private static final String UNCONNECTED_GRAPH = "Inputs cannot generate connected graph";

    /**
     * Private constructor to prevent Utility class instantiation.
     */
    private dijikstra() {
        throw new AssertionError("No instantiation allowed for Utility class");
    }

    /**
     * Graph Object.
     */
    private static TestableGraph myGraph;
    /**
     * Number of vertices of the graph.
     */
    private static short numberOfNodes;
    /**
     * Number of edges of the graph.
     */
    private static int numberOfEdges;
    /**
     * Source vertex of the graph.
     */
    private static short sourceNode;
    /**
     * User input density of the graph.
     */
    private static double graphDensity;
    /**
     * Density of the graph for analysis.
     */
    private static double[] randomizedDensities = {0.1, 1, 20, 50, 75, 100};
    /**
     * Number of nodes in the graph for analysis.
     */
    private static short[] randomizedNumberOfNodes = {1000, 3000, 5000};


    /**
     * Main function.
     * @param args the command line arguments
     * Format for command line arguments ||
     * Format 1 :
     *  java dijikstra -r n d x ||
     *  n - number of vertices
     *  d - density of the graph
     *  x - source vertex number ||
     * Format 2 :
     *  java dijikstra -s file-path ||
     * Format 3 :
     *  java dijikstra -f file-path
     */
    public static void main(final String[] args) {
        try {
            int argsLength = args.length;
            if (argsLength < 2 || argsLength > 4) {
                throw new IllegalArgumentException(WRONG_NUMBER_ARGUMENTS);
            } else if (argsLength == 2 && "-s".equals(args[0].toLowerCase())) {

                FileInputStream file = new FileInputStream(args[1]);
                simpleSchemeFileInput(file);
            } else if (argsLength == 2 && "-f".equals(args[0].toLowerCase())) {

                FileInputStream file = new FileInputStream(args[1]);
                fibonacciSchemeFileInput(file);
            } else if (argsLength == 4 && "-r".equals(args[0].toLowerCase())) {

                numberOfNodes = (short) Integer.parseInt(args[1]);
                graphDensity = Double.parseDouble(args[2]);
                sourceNode = (short) Integer.parseInt(args[3]);
                if (numberOfNodes <= 0) {
                    throw new IllegalArgumentException(WRONG_ARGUMENTS);
                }
                if (graphDensity <= 0 || graphDensity > 100) {
                    throw new IllegalArgumentException(WRONG_ARGUMENTS);
                }
                if (sourceNode < 0 || sourceNode >= numberOfNodes) {
                    throw new IllegalArgumentException(WRONG_ARGUMENTS);
                }
                randomizedRun();
            } else {
                throw new IllegalArgumentException(WRONG_ARGUMENTS);
            }
        } catch (Exception ex) {
            System.out.println("Program Exception---" + ex.getMessage());
            ex.printStackTrace();
        }
    }


    /**
     * Function that runs the simple scheme dijikstra algorithm from user input.
     * @param file file input stream of the data input file
     * @throws Exception when encountered with exceptional cases
     */
    private static void simpleSchemeFileInput(final FileInputStream file)
            throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(file));
        String readLine = null;
        int edgesAdded = 0;
        for (int i = 0; (readLine = reader.readLine()) != null; i++) {
            if (i == 0) {
                 String[] params = readLine.split(" ");
                 if (params.length != 1) {
                     throw new IllegalArgumentException(WRONG_ARGUMENTS);
                 } else {
                     sourceNode = (short) Integer.parseInt(params[0]);
                 }
            } else if (i == 1) {
                String[] params = readLine.split(" ");
                 if (params.length != 2) {
                     throw new IllegalArgumentException(WRONG_ARGUMENTS);
                 } else {
                     numberOfNodes = (short) Integer.parseInt(params[0]);
                     if (numberOfNodes <= 0) {
                         throw new IllegalArgumentException(WRONG_ARGUMENTS);
                     }
                     if (sourceNode < 0 || sourceNode >= numberOfNodes) {
                        throw new IllegalArgumentException(WRONG_ARGUMENTS);
                     }
                     numberOfEdges = Integer.parseInt(params[1]);
                     if (numberOfEdges < (numberOfNodes - 1)) {
                         throw new Exception("Insufficient number of edges to form connected graph");
                     }
                     myGraph = new TestableGraph(numberOfNodes);
                 }
            } else {
                String[] params = readLine.split(" ");
                 if (params.length != 3) {
                     throw new IllegalArgumentException(WRONG_ARGUMENTS);
                 } else {
                     short v1 = (short) Integer.parseInt(params[0]);
                     short v2 = (short) Integer.parseInt(params[1]);
                     if (v1 == v2) {
                         throw new IllegalArgumentException(WRONG_FILE_INPUTS);
                     }
                     if (v1 < 0 || v1 >= numberOfNodes) {
                         throw new IllegalArgumentException(WRONG_FILE_INPUTS);
                     }
                     if (v2 < 0 || v2 >= numberOfNodes) {
                         throw new IllegalArgumentException(WRONG_FILE_INPUTS);
                     }
                     short cost = (short) Integer.parseInt(params[2]);
                     if (cost <= 0) {
                         throw new IllegalArgumentException(WRONG_FILE_INPUTS);
                     }
                     myGraph.addEdge(v1, v2, cost);
                     edgesAdded++;
                 }
            }
        }
        if (edgesAdded != numberOfEdges) {
            throw new InputMismatchException("Insufficient Edges in input");
        }
        double density = (double) numberOfEdges / (double) (numberOfNodes * (numberOfNodes - 1) / 2.0);
        if (!myGraph.depthFirstSearch(sourceNode, density)) {
            throw new Exception(UNCONNECTED_GRAPH);
        } else {
            long start = 0;
            long stop = 0;
            DijikstraAlgorithm algo = new DijikstraAlgorithm(myGraph, (short) sourceNode);
            start = System.currentTimeMillis();
            algo.runSimpleAlgorithm();
            stop = System.currentTimeMillis();
            Integer[] distanceCosts = algo.getPathCosts();
            System.out.println("Shortest paths from simple scheme");
            for (int i = 0; i < distanceCosts.length; i++) {
                System.out.println(distanceCosts[i]);
//                System.out.println(distanceCosts[i] + " //" + " cost from node " + sourceNode + " to " + i);
            }
            System.out.println("Simple scheme Time       :" + (stop - start));
        }
    }


    /**
     * Function that runs the dijikstra algorithm in fibonacci scheme.
     * @param file file input stream of the data input file
     * @throws Exception when encountered with exceptional cases
     */
    private static void fibonacciSchemeFileInput(final FileInputStream file)
            throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(file));
        String readLine = null;
        int edgesAdded = 0;
        for (int i = 0; (readLine = reader.readLine()) != null; i++) {
            if (i == 0) {
                 String[] params = readLine.split(" ");
                 if (params.length != 1) {
                     throw new IllegalArgumentException(WRONG_ARGUMENTS);
                 } else {
                     sourceNode = (short) Integer.parseInt(params[0]);
                 }
            } else if (i == 1) {
                String[] params = readLine.split(" ");
                 if (params.length != 2) {
                     throw new IllegalArgumentException(WRONG_ARGUMENTS);
                 } else {
                     numberOfNodes = (short) Integer.parseInt(params[0]);
                     if (numberOfNodes <= 0) {
                         throw new IllegalArgumentException(WRONG_ARGUMENTS);
                     }
                     if (sourceNode < 0 || sourceNode >= numberOfNodes) {
                        throw new IllegalArgumentException(WRONG_ARGUMENTS);
                     }
                     numberOfEdges = Integer.parseInt(params[1]);
                     if (numberOfEdges < (numberOfNodes - 1)) {
                         throw new Exception("Insufficient number of edges to form connected graph");
                     }
                     myGraph = new TestableGraph(numberOfNodes);
                 }
            } else {
                String[] params = readLine.split(" ");
                 if (params.length != 3) {
                     throw new IllegalArgumentException(WRONG_ARGUMENTS);
                 } else {
                     short v1 = (short) Integer.parseInt(params[0]);
                     short v2 = (short) Integer.parseInt(params[1]);
                     if (v1 == v2) {
                         throw new IllegalArgumentException(WRONG_FILE_INPUTS);
                     }
                     if (v1 < 0 || v1 >= numberOfNodes) {
                         throw new IllegalArgumentException(WRONG_FILE_INPUTS);
                     }
                     if (v2 < 0 || v2 >= numberOfNodes) {
                         throw new IllegalArgumentException(WRONG_FILE_INPUTS);
                     }
                     short cost = (short) Integer.parseInt(params[2]);
                     if (cost <= 0) {
                         throw new IllegalArgumentException(WRONG_FILE_INPUTS);
                     }
                     myGraph.addEdge(v1, v2, cost);
                     edgesAdded++;
                 }
            }
        }
        if (edgesAdded != numberOfEdges) {
            throw new InputMismatchException("Insufficient Edges in input");
        }
        double density = (double) numberOfEdges / (double) (numberOfNodes * (numberOfNodes - 1) / 2.0);
        if (!myGraph.depthFirstSearch(sourceNode, density)) {
            throw new Exception(UNCONNECTED_GRAPH);
        } else {
            long startF = 0;
            long stopF = 0;
            DijikstraFibAlgorithm algo = new DijikstraFibAlgorithm(myGraph, (short) sourceNode);
            startF = System.currentTimeMillis();
            algo.runFibonacciAlgorithm();
            stopF = System.currentTimeMillis();
            Integer[] distanceFibCosts = algo.getPathCostsFib();
            System.out.println("Shortest paths from Fibonacci scheme");
            for (int i = 0; i < distanceFibCosts.length; i++) {
                System.out.println(distanceFibCosts[i]);
//                System.out.println(distanceFibCosts[i] + " //" + " cost from node " + sourceNode + " to " + i);
            }
            System.out.println("Fibonacci Scheme Time     :" + (stopF - startF));
        }
    }


    /**
     * Caller function.
     * Runs the dijikstra algorithm from specified user inputs in random mode.
     * This function calls both the simple and fibonacci scheme modes for
     * the same graph that is generated.
     * @throws Exception when encountered with exceptional cases
     */
    private static void randomizedRun() throws Exception {

        long start1 = 0;
        long stop1 = 0;
        start1 = System.currentTimeMillis();
        myGraph = new TestableGraph(numberOfNodes, graphDensity, sourceNode);
        stop1 = System.currentTimeMillis();
        System.out.println("Graph Generation Time   : " + (stop1 - start1));
        //myGraph.printGraph();
        randomizedSimpleSchemeRun(sourceNode);
        randomizedFibonacciSchemeRun(sourceNode);
    }


    /**
     * Runs the dijikstra algorithm from the given source vertex and the
     * generated graph using the simple scheme.
     * Also calculates the time taken to run the algorithm
     * @param source the source vertex to find shortest paths
     * @throws Exception when encountered with exceptional cases
     */
    private static void randomizedSimpleSchemeRun(final int source) throws Exception {

        long start = 0;
        long stop = 0;
        DijikstraAlgorithm algo = new DijikstraAlgorithm(myGraph, (short) source);
        start = System.currentTimeMillis();
        algo.runSimpleAlgorithm();
        stop = System.currentTimeMillis();
        Integer[] distanceCosts = algo.getPathCosts();
//        for (int i = 0; i < distanceCosts.length; i++) {
//            System.out.println("Shortest paths from simple scheme");
//            System.out.println("Node--" + i + "--Cost--" + distanceCosts[i]);
//        }
        System.out.println("Simple scheme Time      : " + (stop - start));
    }


    /**
     * Runs the dijikstra algorithm from the given source vertex and the
     * generated graph using the fibonacci scheme.
     * Also calculates the time taken to run the algorithm
     * @param source the source vertex to find shortest paths
     * @throws Exception when encountered with exceptional cases
     */
    private static void randomizedFibonacciSchemeRun(final int source) throws Exception {

        long startF = 0;
        long stopF = 0;
        DijikstraFibAlgorithm algo = new DijikstraFibAlgorithm(myGraph, (short) source);
        startF = System.currentTimeMillis();
        algo.runFibonacciAlgorithm();
        stopF = System.currentTimeMillis();
        Integer[] distanceFibCosts = algo.getPathCostsFib();
//        for (int i = 0; i < distanceFibCosts.length; i++) {
//            System.out.println("Shortest paths from Fibonacci scheme");
//            System.out.println("Node--" + i + "--Cost--" + distanceFibCosts[i]);
//        }
        System.out.println("Fibonacci Scheme Time   : " + (stopF - startF));
    }


    /**
     * This function is used only internally to generate a larege number of
     * sample runs in simple and fibonacci scheme for data analysis.
     * This function is not intended to be used when running normal inputs
     * Run Analysis set.
     * @throws Exception when exception occurs
     */
    private static void randomizedAnalysis() throws Exception {
        //Set of inputs
//        for (int i = 0; i < density.length; i++) {
//            for (int j = 0; j < numberOfNodes.length; j++) {
//                if (!((density[i] == 0.1) && (numberOfNodes[j] == 1000))) {
//                    randomizedSimpleSchemeRun(i, j);
//                }
//            }
//        }
//        randomizedRun(0, 3);
    }
}