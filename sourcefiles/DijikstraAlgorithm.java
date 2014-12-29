/**
 * @author Sakthivel Manikam Arunachalam
 * @version 1.0
 */


import java.util.List;
import java.util.LinkedList;
import java.util.ListIterator;
/**
 * Class to run the shortest path algorithm using simple scheme.
 * An Array is used by the simple scheme to determine
 * the next smallest vertex to visit.
 * @author Sakthivel Manikam Arunachalam
 * @version 1.0
 */
public class DijikstraAlgorithm {

    /**
     * Static String for Algorithm exception.
     */
    private static final String ALGO_EXCEPTION = "Dijikstra simple algorithm exception";

    /**
     * Constructor used to initialize the graph variables.
     * @param g graph on which algorithm is run
     * @param s source node in the graph
     */
    public DijikstraAlgorithm(Graph g, short s) {
        this.myGraph = g;
        this.source = s;
        this.pathCosts = new Integer[g.getNumberOfVertices()];
        this.dijikstraQueue = new LinkedList();
    }


    /**
     * Graph Object.
     */
    private Graph myGraph;
    /**
     * Source node.
     */
    private short source;
    /**
     * Cost of path from source to all nodes.
     * This Array is used by the simple scheme to determine
     * the next smallest vertex to visit.
     */
    private Integer[] pathCosts;
    /**
     * Queue used by Djikstra algorithm.
     */
    private List<Integer> dijikstraQueue;


    /**
     * getter for path costs from source to other nodes.
     * @return the path costs to the caller
     */
    public final Integer[] getPathCosts() {
        return this.pathCosts;
    }


    /**
     * Run the Dijikstra Algorithm in simple scheme.
     * This is the actual function that run the algorithm
     * @throws Exception when encountered with exceptional cases
     */
    public final void runSimpleAlgorithm() throws Exception {
        runSimpleInitializer();
        //Dijikstra algorithm start
        this.pathCosts[this.source] = 0;
        while (!this.dijikstraQueue.isEmpty()) {
            Integer nextSmallestVertex = getnextsmallestNode();
            if (!removeMin(nextSmallestVertex)) {
                throw new Exception(ALGO_EXCEPTION);
            }
            List<Edge> neighbors = this.myGraph.getAdjacencyList().get(nextSmallestVertex);
            for (ListIterator<Edge> iter = neighbors.listIterator(); iter.hasNext();) {
                Edge e = iter.next();
                Integer toReach = this.pathCosts[nextSmallestVertex] + e.getEdgeCost();
                if (toReach < this.pathCosts[e.getNeighborNumber()]) {
                    this.pathCosts[e.getNeighborNumber()] = toReach;
                }
            }
        }
    }


    /**
     * Remove Minimum from dijikstra queue.
     * @param vertexToRemove the vertex that should be removed
     * @return true if the vertex is removed
     */
    private boolean removeMin(Integer vertexToRemove) {
        for (int i = 0; i < this.dijikstraQueue.size(); i++) {
            if (this.dijikstraQueue.get(i).intValue() == vertexToRemove.intValue()) {
                this.dijikstraQueue.remove(i);
                return true;
            }
        }
        return false;
    }


    /**
     * Run initializer function before algorithm.
     */
    private void runSimpleInitializer() {
        this.dijikstraQueue.clear();
        for (int i = 0; i < this.myGraph.getNumberOfVertices(); i++) {
            this.pathCosts[i] = Integer.MAX_VALUE;
            this.dijikstraQueue.add(i);
        }
    }


    /**
     * Function to get the next smallest vertex in the queue.
     * This is a linear complexity function O(n) worst case
     * This is the main drawback of the simple scheme dijikstra algorithm
     * using linear list.
     * @return the smallest next vertex number
     */
    private int getnextsmallestNode() {
        Integer smallestVertex = this.dijikstraQueue.get(0);
        int smallestCost = this.pathCosts[smallestVertex];
        for (ListIterator<Integer> iter = this.dijikstraQueue.listIterator(); iter.hasNext();) {
            Integer vertex = iter.next();
            if (this.pathCosts[vertex] < smallestCost) {
                smallestCost = this.pathCosts[vertex];
                smallestVertex = vertex;
            }
        }
        return smallestVertex;
    }
}