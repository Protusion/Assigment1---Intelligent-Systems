package search;

import java.util.ArrayList;

/** 
 * This interface must be implemented by all classes which define a search 
 * problem to provide the complete definition. Notice that all these functions
 * are problem-dependent. Heuristic is included too, as it also depends on the problem.
 */
 
public interface SearchProblem{
	
    /**  Returns the initial state of the problem */
    public State initialState();
    
    /**  Returns the state resulting of applying an action to another state */
    public State applyAction(State state, Action action);
   
    /** Returns the set of actions that can be applied to a certain state */
    public ArrayList<Action> getPossibleActions(State state);
    
    /** Returns the cost of applying an action over a state */
    public double cost(State state, Action action);
    
    /** Tests if an state is the goal */
    public boolean testGoal(State chosen);

    /** The returns the heuristic value of an state */
    public double heuristic(State state);
    
	/** Sets parameters of the search if necessary */
	public abstract void setParams(String[] params);
}
