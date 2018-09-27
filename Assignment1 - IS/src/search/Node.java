package search;

import java.util.Comparator;

/**
 *  Implements a node of the search tree. The node represents a generic state. 
 */

public class Node{
	
	/* 
	 * Different comparators provided by the class. They indicate the criteria by which
	 * nodes can be ordered. Example:
	 * 
	 * 		PriorityQueue<Node> open = new PriorityQueue<Node>(100, Node.BY_HEURISTIC);
	 * 
	 * This sentence builds a priority queue where nodes are ordered according to their
	 * heuristic value.
	 */
	public static final Comparator<Node> BY_DEPTH = new DepthComparator();
	public static final Comparator<Node> BY_HEURISTIC = new HeuristicComparator();
	public static final Comparator<Node> BY_COST = new CostComparator();
	public static final Comparator<Node> BY_EVALUATION = new EvaluationComparator();
	
	/* Relevant information for the search algorithms. */
    private State state;	 		// State that the node corresponds to. 
    private int depth=0; 		    // Depth of the node in the tree.
    private double cost=0; 	 		// Cost of the path from the root to the node. Corresponds to g(n).
    private double heuristic=0;  	// Value of the heuristic function for the node. Corresponds to h(n).
    private double evaluation = 0;  // Evaluation of the node. Corresponds to f(n) = g(n) + h(n)
    private Node parent;		 	// Reference to the parent node in the search tree. 
    private Action action;   		// Action applied to the parent to obtain this node. 
    
    /**  Constructor. Takes as parameter an state and builds the corresponding node.*/
    public Node(State state){
		this.state =  state;
    }
    
    // Access to the fields of the node.
    
    /** Returns the state represented by the nod. */
    public State getState(){ return state; }
    
    /** Sets the depth of the node. */
    public void setDepth(int depth){ this.depth=depth; }
    /** Returns the depth of the node. */
    public int getDepth() { return depth; }
    /** Sets the cost of the node. */
    public void setCost(double cost){
    		this.cost = cost;
    		this.evaluation = this.cost + this.heuristic;
    }
    /** Returns the cost of the node. */
    public double getCost() { return cost; }
    /** Fixes the value of the heuristic. */
    public void setHeuristic(double heuristic){ 
    		this.heuristic = heuristic;
    		this.evaluation = this.cost + this.heuristic;
    }
    /** Returns the heuristic function for the node. */
    public double getHeuristic() { return heuristic; }
    /** Returns the value of the evaluation for the node */
    public double getEvaluation() { return evaluation; }
    
    /** Returns a reference to the parent node in the tree. */
    public Node getParent() { return parent; }
    /** Fixes the reference to the parent in the tree. */
    public void setParent(Node node) {	this.parent=node; }
	
    /** Returns the action applied to the parent to create the node. */
    public Action getAction() { return action;}
    /** Sets the action applied to the parent to create the node. */ 
    public void setAction(Action action) { this.action = action; }
    
    // Auxiliar functions

	/** 
	 * Checks if two nodes are similar (represent the same state). The method overrides the one in
	 * the class Object so that it is used by the structures provided by Java, as for example HashSet.
	 */
	@Override
	public boolean equals(Object anotherNode){
		// If the object is not a Node, returns false, but reports the error.
		if (!(anotherNode instanceof Node)){
			System.out.println("Trying to compare two objects of different classes.");
			return false;
		}
		// If the object is a node, compares their states.
		return this.state.equals(((Node) anotherNode).getState());
	}
	
    /** 
     *  Prints the node in a String. Determines what is printed by System.out.println(node);
     *  It is simple because it is the format used by the debugger.
     */
    public String toString(){
    	return("Node("+state.toString()+")");
    }
    
    /** 
     * Prints all the information of the node. 
     */
    public void print(){
    	System.out.println("Node ("+state.toString()+"):" +"\n\tdepth:"+depth+" \n\tg:"+cost+"\n\th:"+heuristic+".");
    }
    
	// Implementation of the comparators in static classes. 
    
	/** 
     * Compares nodes by depth
     */
	public static class DepthComparator implements Comparator<Node> {
		public int compare(Node nodeA, Node nodeB) {
			double nodeACost = nodeA.getDepth();
			double nodeBCost = nodeB.getDepth();
			
			if (nodeACost<nodeBCost) return -1; 
			else if (nodeBCost<nodeACost) return 1;
			else  return 0; 
		}
	}	
	
	/** 
     * Compares nodes by cost
     */
	public static class CostComparator implements Comparator<Node> {
		public int compare(Node nodeA, Node nodeB) {
			double nodeACost = nodeA.getCost();
			double nodeBCost = nodeB.getCost();
			
			if (nodeACost<nodeBCost) return -1;
			else if (nodeBCost<nodeACost) return 1;
			else return 0;
		}
	}	
	
    /** 
     * Compares nodes by heuristic.
     */
	public static class HeuristicComparator implements Comparator<Node> {
		public int compare(Node nodeA, Node nodeB) {
			double nodeAHeur = nodeA.getHeuristic();
			double nodeBHeur = nodeB.getHeuristic();
			
			if (nodeAHeur<nodeBHeur) return -1;
			else if (nodeBHeur<nodeAHeur) return 1;
			else return 0;
		}
	}	
	
    /** 
     * Compares nodes by evaluation
     */
	public static class EvaluationComparator implements Comparator<Node> {
		public int compare(Node nodeA, Node nodeB) {
			double nodeAEval = nodeA.getEvaluation();
			double nodeBEval = nodeB.getEvaluation();
			
			if (nodeAEval<nodeBEval) return -1;
			else if (nodeBEval<nodeAEval) return 1;
			else return 0;
		}
	}
}
