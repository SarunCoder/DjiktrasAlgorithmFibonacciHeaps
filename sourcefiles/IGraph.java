/**
 * @author Sakthivel Manikam Arunachalam
 * @version 1.0
 */


import java.util.List;
/**
 * Interface methods for a Graph.
 * @author Sakthivel Manikam Arunachalam
 * @version 1.0
 */
public interface IGraph {

    /**
     * Add an edge to the Graph.
     * @param v1 source vertex
     * @param v2 destination vertex
     * @param cost cost of the edge
     * @return true if edge added
     */
    boolean addEdge(short source, short destination, short cost);

     /**
     * Remove and edge the Graph.
     * @param source source vertex
     * @param destination destination vertex
     * @return true if edge is removed
     */
    boolean removeEdge(short source, short destination);

    /**
     * Returns whether an edge is present in between edges.
     * @param source source vertex
     * @param destination destination vertex
     * @return True is edge present or False if nor present
     */
    boolean isEdgePresent(short source, short destination);

    /**
     * Returns all the reachable neighbors of the given vertex.
     * @param source source vertex
     * @return list of neighbors
     */
    List<Edge> reachableNeigbours(short source);
}
