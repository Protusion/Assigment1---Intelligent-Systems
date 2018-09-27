package visualization;

import javax.swing.JPanel;

import search.Action;
import search.State;

/** 
 * Extends JPanel to create the functionalities necessary to visualize problems. 
 * This functionalities are only graphical.
 */
public abstract class ProblemView extends JPanel{
	
	/** Sets the current state in the view. */
	public abstract void setState(State state);
	
	/** 
	 *  Performs an action in the view. In most cases, the action is not necessary. 
	 *  However, sometimes the graphical representation can depend also of the action.
	 *  For instance, running vs walking or jumping.
	 */
	public abstract void takeAction(Action action, State toState);

}
