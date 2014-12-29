/**
 * @author Sakthivel Manikam Arunachalam
 * @version 1.0
 */


import java.util.List;
/**
 * Class to run the shortest path algorithm using fibonacci scheme.
 * A Fibonacci heap is used by this scheme to determine
 * the next smallest vertex to visit.
 * @author Sakthivel Manikam Arunachalam
 * @version 1.0
 */
public class DijikstraFibAlgorithm {

    /**
     * Static String for Algorithm exception.
     */
    private static final String ALGO_EXCEPTION = "Dijikstra fibonacci algorithm exception";


    /**
     * Constructor used to initialize the graph variables.
     * @param g graph on which algorithm is run
     * @param s source node in the graph
     */
    public DijikstraFibAlgorithm(Graph g, short s) {
        this.myGraph = g;
        this.source = s;
        this.pathCostsFib = new Integer[g.getNumberOfVertices()];
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
     * Cost of path from source to all nodes in fibonacci scheme.
     */
    private Integer[] pathCostsFib;


    /**
     * getter for path costs from source to other nodes.
     * @return the path costs to the caller
     */
    public final Integer[] getPathCostsFib() {
        return pathCostsFib;
    }


    /**
     * Run the Dijikstra Algorithm in fibonacci scheme.
     * This is the actual function that run the algorithm
     * @throws Exception when encountered with exceptional cases
     */
    public final void runFibonacciAlgorithm() throws Exception {

        FibonacciHeap dijikstraHeap = new FibonacciHeap();
        FibonacciHeap.Node[] newNodeArray = new FibonacciHeap.Node[this.myGraph.getNumberOfVertices()];
        this.pathCostsFib[this.source] = 0;
        for (int i = 0; i < this.myGraph.getNumberOfVertices(); i++) {
            if (i != this.source) {
                this.pathCostsFib[i] = Integer.MAX_VALUE;
            }
            newNodeArray[i] = dijikstraHeap.insert(i, this.pathCostsFib[i]);
        }
        while (dijikstraHeap.returnMin() != null) {
            FibonacciHeap.Node min = dijikstraHeap.removeMin();
            if (min == null) {
                throw new Exception(ALGO_EXCEPTION);
            }
            List<Edge> neighbors = this.myGraph.getAdjacencyList().get(min.getNodeNumber());
            for (int i = 0; i < neighbors.size(); i++) {
                Edge e = neighbors.get(i);
                Integer toReach = this.pathCostsFib[min.getNodeNumber()] + e.getEdgeCost();
                if (toReach < this.pathCostsFib[e.getNeighborNumber()]) {
                    this.pathCostsFib[e.getNeighborNumber()] = toReach;
                    dijikstraHeap.decreaseKey(newNodeArray[e.getNeighborNumber()], toReach.intValue());
                }
            }
        }
    }
}