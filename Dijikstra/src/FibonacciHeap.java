/**
 * @author Sakthivel Manikam Arunachalam
 * @version 1.0
 */


import java.util.ArrayList;
import java.util.List;

/**
 * Class to represent a fibonacci heap.
 * @author Sakthivel Manikam Arunachalam
 * @version 1.0
 */
public class FibonacciHeap {

    /**
     * Constructor to initialize a new fibonacci heap.
     * Creates a new fibonacci heap with 0 nodes.
     */
    public FibonacciHeap() {
        this.minimum = null;
        this.numberOfNodes = 0;
    }


    /**
     * Pointer to the node with minimum key in the fibonacci heap.
     */
    private Node minimum;


    /**
     * number of nodes currently in the fibonacci heap.
     */
    private short numberOfNodes;


    /**
     * getter for number of nodes.
     * @return numberOfNodes in the fibonacci heap
     */
    public final short getNumberOfNodes() {
        return numberOfNodes;
    }


    /**
     * Insert a new node in the fibonacci heap.
     * @param nNum vertex number of the newnode
     * @param keyVal key value of the new node
     * @return Node which got inserted
     */
    public final Node insert(int nNum, int keyVal) {
        Node newNode = new Node(nNum, keyVal);

        minimum = meld(minimum, newNode);

        ++numberOfNodes;

        return newNode;
    }


    /**
     * Melds two circular doubly linked lists into one list.
     * @param list1 pointer to first doubly linked list
     * @param list2 pointer to first doubly linked list
     * @return Node pointer to the new list
     */
    private static Node meld(Node list1, Node list2) {
        if (list1 == null && list2 == null) {
            return null;
        } else if (list1 == null) {
            return list2;
        } else if (list2 == null) {
            return list1;
        } else {
            Node next = list1.rightSibling;
            list1.rightSibling = list2.rightSibling;
            list1.rightSibling.leftSibling = list1;
            list2.rightSibling = next;
            list2.rightSibling.leftSibling = list2;

            if (list1.keyValue < list2.keyValue) {
                return list1;
            } else {
                return list2;
            }
        }
    }


    /**
     * Return the current minimum Node of the fibonacci heap.
     * @return the minimum Node without removing the node
     */
    public final Node returnMin() {
        return minimum;
    }


    /**
     * Returns true if the heap is empty.
     * @return true if heap is empty
     */
    public final boolean isHeapEmpty() {
        return minimum == null;
    }


    /**
     * Function that removes the minimum node from the fibonacci heap.
     * This function also does a consolidate which helps to bring down
     * amortized complexity of remove minimum to O(log n)
     * @return the minimum Node
     * @throws Exception when encountered with exceptional cases
     */
    public final Node removeMin() throws Exception {
        if (isHeapEmpty()) {
            throw new Exception("Cannot remove minimum from empty heap");
        }

        Node minNode = minimum;
        --numberOfNodes;

        if (minimum.rightSibling == minimum) {
            minimum = null;
        } else {
            minimum.leftSibling.rightSibling = minimum.rightSibling;
            minimum.rightSibling.leftSibling = minimum.leftSibling;
            minimum = minimum.rightSibling;
        }

        if (minNode.child != null) {
            Node c = minNode.child;
            c.parent = null;
            c = c.rightSibling;
            while (c != minNode.child) {
                c.parent = null;
                c = c.rightSibling;
            }
        }

        minimum = meld(minimum, minNode.child);
        if (minimum == null) {
            return minNode;
        }

        //Consolidate teh fibonacci tree
        //This contributes to the better amortized complexity
        //of the remove min functionality
        List<Node> degreeTable = new ArrayList<>();

        List<Node> nodes = new ArrayList<>();
        for (Node n = minimum; nodes.isEmpty() || nodes.get(0) != n; n = n.rightSibling) {
            nodes.add(n);
        }

        for (Node n : nodes) {
            while (true) {

                while (n.degree >= degreeTable.size()) {
                    degreeTable.add(null);
                }

                if (degreeTable.get(n.degree) == null) {
                    degreeTable.set(n.degree, n);
                    break;
                }

                Node temp = degreeTable.get(n.degree);
                degreeTable.set(n.degree, null);

                Node min = null;
                Node max = null;
                if (temp.keyValue < n.keyValue) {
                    min = temp;
                    max = n;
                } else {
                    min = n;
                    max = temp;
                }

                max.leftSibling.rightSibling = max.rightSibling;
                max.rightSibling.leftSibling = max.leftSibling;

                max.rightSibling = max;
                max.leftSibling = max;

                min.child = meld(min.child, max);

                max.parent = min;

                max.childCut = false;

                ++min.degree;

                n = min;
            }
            if (n.keyValue <= minimum.keyValue) {
                minimum = n;
            }
        }
        return minNode;
    }


    /**
     * This function decreases the key for Node.
     * @param node pointer to that Node
     * @param keyVal new key value
     */
    public final void decreaseKey(Node node, int keyVal) {
        node.keyValue = keyVal;

        if (node.parent != null && node.keyValue <= node.parent.keyValue) {
            removeParentConnection(node);
        }

        if (node.keyValue <= minimum.keyValue) {
            minimum = node;
        }
    }


    /**
     * Removes the connection of the child Node from its parent.
     * @param node pointer to the child Node
     */
    private void removeParentConnection(Node node) {
        node.childCut = false;

        if (node.parent == null) {
            return;
        }

        if (node.rightSibling != node) {
            node.rightSibling.leftSibling = node.leftSibling;
            node.leftSibling.rightSibling = node.rightSibling;
        }

        if (node.parent.child == node) {
            if (node.rightSibling != node) {
                node.parent.child = node.rightSibling;
            } else {
                node.parent.child = null;
            }
        }

        --node.parent.degree;

        node.leftSibling = node;
        node.rightSibling = node;
        minimum = meld(minimum, node);

        if (node.parent.childCut) {
            removeParentConnection(node.parent);
        } else {
            node.parent.childCut = true;
        }

        node.parent = null;
    }


    /**
     * Deletes the arbitrary Node from the fibonacci heap.
     * @param node pointer to the Node
     * @return the Node which is deleted
     */
    public Node deleteNode(Node node) throws Exception{

        decreaseKey(node, Integer.MIN_VALUE);

        return removeMin();
    }


    /**
    * Class to represent each vertexNode in the Dijikstra algorithm
    * using Fibonacci heap.
    */
   public static class Node {

       /**
        * Non-default Constructor for class.
        * @param nNum vertex number of the graph
        * @param kVal key value of the node
        */
       public Node(final int nNum, final int kVal) {
           this.rightSibling = this;
           this.leftSibling = this;
           this.nodeNumber = nNum;
           this.keyValue = kVal;
       }
       /**
        * Vertex node number.
        */
       private int nodeNumber;
       /**
        * Key value of the node.
        */
       private int keyValue;
       /**
        * degree - number of child nodes in the child list.
        */
       private int degree = 0;
       /**
        * Denotes the childCut value of this node.
        */
       private boolean childCut = false;
       /**
        * Pointer to the parent node of this node.
        */
       private Node parent;
       /**
        * Pointer to right sibling in the doubly linked list.
        */
       private Node rightSibling;
       /**
        * Pointer to left sibling in the doubly linked list.
        */
       private Node leftSibling;
       /**
        * Pointer to any of the children.
        */
       private Node child;

       /**
        * getter for nodeNumber.
        * @return nodeNumber
        */
       public final int getNodeNumber() {
           return nodeNumber;
       }

       /**
        * setter for nodeNumber.
        * @param nodeNum to set
        */
       public final void setNodeNumber(final int nodeNum) {
           this.nodeNumber = nodeNum;
       }

       /**
        * getter for key value.
        * @return key value
        */
       public final int getKeyValue() {
           return keyValue;
       }

       /**
        * setter for key value.
        * @param keyVal to set
        */
       public final void setKeyValue(final int keyVal) {
           this.keyValue = keyVal;
       }

       /**
        * getter for the node degree.
        * @return degree of the node
        */
       public final int getDegree() {
           return degree;
       }

       /**
        * setter for the node degree.
        * @param deg to set
        */
       public final void setDegree(final int deg) {
           this.degree = deg;
       }

       /**
        * getter for chidCut.
        * @return childCut
        */
       public final boolean isChildCut() {
           return childCut;
       }

       /**
        * setter for childCut.
        * @param cCut to set
        */
       public final void setChildCut(final boolean cCut) {
           this.childCut = cCut;
       }

       /**
        * getter for parent node.
        * @return pointer to parent node
        */
       public final Node getParent() {
           return parent;
       }

       /**
        * setter for parent node.
        * @param p node pointer
        */
       public final void setParent(final Node p) {
           this.parent = p;
       }

       /**
        * getter for right sibling.
        * @return pointer to right sibling  node
        */
       public final Node getRightSibling() {
           return rightSibling;
       }

       /**
        * setter for right sibling.
        * @param rSib node pointer
        */
       public final void setRightSibling(final Node rSib) {
           this.rightSibling = rSib;
       }

       /**
        * getter for the left sibling.
        * @return pointer to left sibling node
        */
       public final Node getLeftSibling() {
           return leftSibling;
       }

       /**
        * setter for the left sibling.
        * @param lSib node pointer
        */
       public final void setLeftSibling(final Node lSib) {
           this.leftSibling = lSib;
       }

       /**
        * getter for child pointer.
        * @return pointer to the child node
        */
       public final Node getChild() {
           return child;
       }

       /**
        * setter for child node.
        * @param c node pointer
        */
       public final void setChild(final Node c) {
           this.child = c;
       }
   }
}