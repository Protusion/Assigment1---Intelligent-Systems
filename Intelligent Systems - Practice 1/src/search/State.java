package search;

/**
 *  Represents a generic state. All states will be compared, hashed or printed.
 *  The rest of functionalities are problem dependent.
 */
public abstract class State {
	
	/** 
	 * Checks if two states are similar. The method overrides the one provided by the Object class
	 * and is used by some classes in Java. For instance, the method HashSet.contains makes
	 * use of equals.
	 */
	@Override
	public abstract boolean equals(Object anotherState);
	
	/** 
	 * Basic hashing function. Overrides the one in Object and is used in classes such
	 * as HashSet.
	 */
	@Override
	public abstract int hashCode();
	
	/** 
	 * Prints the state.
	 */
	@Override
	public abstract String toString();
	
}
