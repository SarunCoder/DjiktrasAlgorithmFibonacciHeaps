/**
 * @author Sakthivel Manikam Arunachalam
 * @version 1.0
 */

/**
 * Class to hold the node data in the adjacency list.
 * @author Sakthivel Manikam Arunachalam
 * @version 1.0
 */
public class Edge {
    /**
     * Neighbor number.
     */
    private short neighborNumber;
    /**
     * edgeCost.
     */
    private short edgeCost;


    /**
     * Setter for neighbor number.
     * @param n neighbor number
     */
    public final void setNeighborNumber(final short n) {
        this.neighborNumber = n;
    }


    /**
     * Setter for edge cost.
     * @param c cost of edge
     */
    public final void setEdgeCost(final short c) {
        this.edgeCost = c;
    }


    /**
     * getter for Neighbor number.
     * @return neighbor number
     */
    public final short getNeighborNumber() {
        return this.neighborNumber;
    }


    /**
     * getter for edge cost.
     * @return edge cost
     */
    public final short getEdgeCost() {
        return this.edgeCost;
    }
}