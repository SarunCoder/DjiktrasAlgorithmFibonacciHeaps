/**
 * @author Sakthivel Manikam Arunachalam
 * @version 1.0
 */


import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Set;
import java.util.HashSet;

/**
 * Class to represent a graph with vertices, edges and edge costs.
 * @author Sakthivel Manikam Arunachalam
 * @version 1.0
 */
public class Graph implements IGraph {

    /**
     * STATIC float for percentage computation.
     */
    private static final float PERCENT = 100;
    /**
     * STATIC String for Density Exception.
     */
    private static final String LOW_DENSITY = "The input density of the Graph is too low to generate a connected Graph";
    /**
     * STATIC String for MAX random edge cost.
     */
    private static final int MAX_RANDOM_COST = 1000;
    /**
     * Hold the number of vertices of the graph.
     */
    private short numberOfVertices;
    /**
     * Holds the adjacency list for the entire nodes of the graph.
     */
    private final List<List<Edge>> adjacencyList;

    /**
     * Setter for edge cost.
     * @param n number of vertices
     */
    public final void setNumberOfVertices(final short n) {
        this.numberOfVertices = n;
    }

    /**
     * getter for number of vertices.
     * @return number of vertices
     */
    public final int getNumberOfVertices() {
        return this.numberOfVertices;
    }

    /**
     * getter for adjacency list.
     * @return the adjacency list
     */
    public final List<List<Edge>> getAdjacencyList() {
        return this.adjacencyList;
    }

    /**
     * Default constructor.
     */
    public Graph() {
        //Do nothing constructor
        this.numberOfVertices = 0;
        this.adjacencyList = null;
    }

    /**
     * Constructor that allocates memory.
     * @param n the number of vertices
     */
    public Graph(final short n) {
        this.numberOfVertices = n;

        this.adjacencyList = new ArrayList<>();
        for (int i = 0; i < this.numberOfVertices; i++) {
            this.adjacencyList.add(new ArrayList<Edge>());
        }
    }

    /**
     * Constructor to be used when generating a graph with randomized inputs.
     * The density calculation is always performed to create ceil of the max
     * edges when encountered with decimals-Assumption.
     * @param n the number of vertices
     * @param d density of the graph
     * @param s source vertex
     * @throws Exception when the density is too low
     * to generate a connected graph.
     */
    public Graph(final short n, final double d, final short s) throws Exception {
        this(n);
        double numberOfEdges;
        numberOfEdges = (double) d * (((double) n * ((double) n - 1.0)) / 2.0) / PERCENT;
        numberOfEdges = Math.ceil(numberOfEdges);
        if (numberOfEdges < this.numberOfVertices - 1) {
            throw new Exception(LOW_DENSITY);
        }
        int testCounter  = 0; ///To be removed later
        System.out.println("Number of edges    : " + numberOfEdges);
        do {
            testCounter++; ///To be removed later
            for (int i = 0; i < this.numberOfVertices; i++) {
                this.adjacencyList.get(i).clear();
            }
            System.out.println("INFO  :  Generating randomized edges and cost");
            if (d != 100.0 && d != 0.1) {
                for (int i = 0; i < numberOfEdges;) {
                    Random numGenerator = new Random();
                    int v1 = numGenerator.nextInt(this.numberOfVertices);
                    int v2 = numGenerator.nextInt(this.numberOfVertices);
                    int cost = numGenerator.nextInt(MAX_RANDOM_COST) + 1;
                    if (addEdge((short) v1, (short) v2, (short) cost)) {
                        i++;
                    }
                }
            } else if (d == 0.1) {
                int numEdges = 0;
                for (int i = 0; i < this.numberOfVertices;) {
                    Random numGenerator = new Random();
                    int v1 = i;
                    int v2 = -1;
                    do {
                        v2 = numGenerator.nextInt(this.numberOfVertices);
                    } while(v2 == i);
                    int cost = numGenerator.nextInt(MAX_RANDOM_COST) + 1;
                    if (addEdge((short) v1, (short) v2, (short) cost)) {
                        numEdges++;
                        i++;
                    }
                }
                for (int i = numEdges; i < numberOfEdges; ) {
                    Random numGenerator = new Random();
                    int v1 = numGenerator.nextInt(this.numberOfVertices);
                    int v2 = numGenerator.nextInt(this.numberOfVertices);
                    int cost = numGenerator.nextInt(MAX_RANDOM_COST) + 1;
                    if (addEdge((short) v1, (short) v2, (short) cost)) {
                        i++;
                        numEdges++;
                    }
                }
//                System.out.println("Number of edges added to graph--- " + numEdges);
            } else {
                for (int i = 0; i < this.numberOfVertices; i++) {
                    for (int j = 0; j < this.numberOfVertices; j++) {
                        Random numGenerator = new Random();
                        int cost = numGenerator.nextInt(MAX_RANDOM_COST) + 1;
                        if (i != j) {
                            addEdge((short) i, (short) j, (short) cost);
                        }
                    }
                }
            }
        } while(!(isProperGraph(s, d)));
        System.out.println("INFO  :  Number of trials to generate a proper connected random Graph : " + testCounter);
    }


    /**
     * Check whether the graph is connected using DFS.
     * @param s source vertex
     * @param d density of the graph
     * @return true when graph is properly connected.
     */
    public final boolean isProperGraph(final short s, final double d) {
        return depthFirstSearch(s, d);
    }


    /**
     * Depth first search to check if the graph is connected from the source.
     * @param s source vertex
     * @param d density of the graph
     * @return true if the graph is properly connected
     */
    public final boolean depthFirstSearch(final short s, final double d) {
        if (d == 100.0) {
            return true;
        }
        System.out.println("INFO  :  Testing depth first search");
        Set visitedVertices = new HashSet();
        Deque<Short> stck = new ArrayDeque();
        stck.push(s);
        while (!stck.isEmpty()) {
            Short vertex = stck.pop();
            if (!visitedVertices.contains(vertex)) {
                visitedVertices.add(vertex);
                List<Edge> neighbors = this.adjacencyList.get(vertex);
                for (ListIterator<Edge> iter = neighbors.listIterator(); iter.hasNext();) {
                    stck.push(iter.next().getNeighborNumber());
                }
            }
        }
        return visitedVertices.size() == this.numberOfVertices;
    }


    /**
     * Add an edge to the Graph.
     * @param v1 source vertex
     * @param v2 destination vertex
     * @param cost cost of the edge
     * @return true if edge added
     */
    @Override
    public final boolean addEdge(final short v1, final short v2,
            final short cost) {
        if ((v1 == v2) || (cost == 0) || isEdgePresent(v1, v2)) {
            return false;
        }
        boolean retVal1;
        boolean retVal2;
        Edge vertex1Neighbor = new Edge();
        vertex1Neighbor.setNeighborNumber(v2);
        vertex1Neighbor.setEdgeCost(cost);
        retVal1 = this.adjacencyList.get(v1).add(vertex1Neighbor);
        Edge vertex2Neighbor = new Edge();
        vertex2Neighbor.setNeighborNumber(v1);
        vertex2Neighbor.setEdgeCost(cost);
        retVal2 = this.adjacencyList.get(v2).add(vertex2Neighbor);
        return (retVal1 & retVal2);
    }


    /**
     * Remove and edge the Graph.
     * @param source source vertex
     * @param destination destination vertex
     * @return true if edge is removed
     */
    @Override
    public final boolean removeEdge(final short source,
            final short destination) {
        throw  new UnsupportedOperationException("This operation is not yet supported as per this project");
    }


    /**
     * Returns whether an edge is present in between edges.
     * @param source source vertex
     * @param destination destination vertex
     * @return True is edge present or False if nor present
     */
    @Override
    public final boolean isEdgePresent(final short source, final short dest) {
        boolean retVal;
        retVal = false;
        List<Edge> neighbors = this.adjacencyList.get(source);
        for (ListIterator<Edge> iter = neighbors.listIterator(); iter.hasNext();) {
            Edge temp = iter.next();
            if (dest == temp.getNeighborNumber()) {
                retVal = true;
            }
        }
        return retVal;
    }


    /**
     * Returns all the reachable neighbors of the given vertex.
     * @param source source vertex
     * @return list of neighbors
     */
    @Override
    public final List<Edge> reachableNeigbours(final short source) {
        return this.adjacencyList.get(source);
    }
}
