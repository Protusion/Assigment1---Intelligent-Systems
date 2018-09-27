package search;

/**
 *  Represents a generic action. Actions can be enumerated and common to each state 
 *  in the problem (8-puzzle, maze), but can also be unique for each state (routing), 
 *  therefore an action must be able to return its own description. 
 */
public interface Action {
	public String getId();
}
