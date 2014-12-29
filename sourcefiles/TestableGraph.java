/**
 * @author Sakthivel Manikam Arunachalam
 * @version 1.0
 */

import java.util.List;
import java.util.ListIterator;
/**
 * Extended class to represent a graph with vertices, edges and edge costs.
 * Has a print functionality included
 * @author Sakthivel Manikam Arunachalam
 * @version 1.0
 */
public class TestableGraph extends Graph {
    /**
     * Derived class constructor.
     * @param n number of vertices
     * @param d density of the graph
     * @param s source vertex number
     * @throws Exception from the base class constructor
     */
    public TestableGraph(final short n, final double d, final short s)
            throws Exception {
            super(n, d, s);
    }

    /**
     * Derived class constructor for just initializing the graph.
     * @param n number of vertices
     */
    public TestableGraph(final short n) {
        super(n);
    }
    /**
     * Method to print the graph to output for debugging purpose.
     */
    public final void printGraph() {
        StringBuilder str = new StringBuilder();
        str.append("Vertex" + " " + "Neighbor" + " " + "Cost");
        str.append(System.getProperty("line.separator"));
        int vertexNum = 0;
        for (ListIterator<List<Edge>> topiter = this.getAdjacencyList().listIterator(); topiter.hasNext();) {
            topiter.next();
            for (ListIterator<Edge> iter = this.getAdjacencyList().get(vertexNum).listIterator(); iter.hasNext();) {
                Edge temp  = iter.next();
                str.append(vertexNum).append("    ");
                str.append(temp.getNeighborNumber()).append("    ");
                str.append(temp.getEdgeCost()).append("    ");
                str.append(System.getProperty("line.separator"));
            }
            vertexNum++;
        }
        System.out.println(str);
    }
}