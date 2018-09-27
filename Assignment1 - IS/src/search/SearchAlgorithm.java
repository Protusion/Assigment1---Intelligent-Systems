package search;

import java.util.ArrayList;

/** 
 * This class must be extended by all classes which implement a search 
 * algorithm. Descending classes must implement two methods, the one which
 * searches for the path, and the one returning the result.
 */
public abstract class SearchAlgorithm{
	
	// PROBLEM AND SOLUTION
	
	/* Formulation of the problem. */
	protected SearchProblem problem;
	
    /* Sequence of actions which will store the solution. */
    protected ArrayList<Action> actionSequence;	
    
    // SEARCH STATISTICS
    
    /* Stores the cost */
    protected double totalCost;
    
	/* Stores the number of nodes expanded during the search.*/
	protected long expandedNodes;     
	
	/* Stores the number of nodes that have been generated during the search. */
	protected long generatedNodes;
	
	/* Stores the maximum size of the set of open nodes */
    protected long openMaxSize;

    /* Stores the maximum size of the set of explored states */
    protected long exploredMaxSize;

	/* Stores the time needed to finish the search */
	protected long searchTime;
	
	/* Whether a solution has been found */
	protected boolean solutionFound;
	
	// ABSTRACT METHODS
	
	/** Sets parameters of the search if necessary */
	public abstract void setParams(String[] params);
	
    /** Carries out the search and returns the the result. */    
    public abstract void doSearch();  
    
    // SEARCH
    
	/** Resets the search statistics and solution. */
	public void initSearch(){
		// Inits performance variables
    	totalCost = 0;
		expandedNodes = 0;
		generatedNodes = 0;
		openMaxSize = 0;
		exploredMaxSize = 0;
		solutionFound = false;
		// Creates a new sequence of actions
		actionSequence= new ArrayList<Action>();		
	}
	
	/** Carries out the search */
	public void search(){
		initSearch();
		searchTime = System.currentTimeMillis();
		doSearch();
		searchTime = System.currentTimeMillis()-searchTime;
	}
	
    // AUXILIAR SEARCH METHODS
    
	/** 
	 * Checks if the node contains the initial state.
	 */
	public boolean isInitialNode(Node node){
		return problem.initialState().equals(node.getState());
	}
    
	/**
	 *  Return the successors of a given node. It is necessary to update
	 *  information such as actions, costs, etc.
	 *  This function corresponds to EXPAND seen in class. 
	 */
	public ArrayList<Node> getSuccessors(Node node){
		ArrayList<Node> successors = new ArrayList<Node>();
		// Obtains the possible actions
		ArrayList<Action> actions = problem.getPossibleActions(node.getState());
		// For each action. 
		for (Action action: actions){
			// Generates the state.
			State newState = problem.applyAction(node.getState(), action);
			// Creates the node and fixes the action used
			Node newNode = new Node(newState);
			// Updates the number of nodes that have been generated. 
			generatedNodes++;
			// Set the parent.
			newNode.setParent(node);
			// Fixes the action used to create the new node.
			newNode.setAction(action);
			// Calculates the cost. It depends on the problem.
			double costAction = problem.cost(node.getState(), action);
			newNode.setCost(node.getCost()+ costAction);
			// Adds the heuristic. It depends on the problem.
			newNode.setHeuristic(problem.heuristic(newState));
			// Updates its depth.
			newNode.setDepth(node.getDepth()+1);
			//Adds it to the list.
			successors.add(newNode);
		}
		// Keeps track of the number of nodes expanded. 		
		expandedNodes++;
		return successors;
	}		
	
	// UTILITY
		
	/** 
	 * Sets the search problem.
	 */
	public void setProblem(SearchProblem problem){
		this.problem = problem;
	}

	/** 
	 * Returns the cost of the solution.
	 */
	public double getTotalCost(){
		return totalCost;
	}
	
	/**
	 * Returns the number of expanded nodes.
	 */
    public long getExpandedNodes(){
    		return expandedNodes;
    }
    
	/**
	 * Returns the number of generated nodes.
	 */
    public long getGeneratedNodes(){
    		return generatedNodes;
    }
    
    /** Returns the maximum size reached by the set of open nodes */
    public long getOpenMaxSize() {
    		return openMaxSize;
    }
    		
    /** Returns the maximum size reached by the set of explored states */
    public long getExploredMaxSize() {
    		return exploredMaxSize;
    }    
    
    /** 
     * Returns the search time.
     */
    public long getSearchTime(){
    	return searchTime;
    }
    
    /**
     * Returns the solution found.
     */
    public boolean getSolutionFound(){
    	return solutionFound;
    }
    
    /** 
     * Returns the solution to the problem. 
     */
    public ArrayList<Action> result(){
    	return actionSequence;
    }           
}