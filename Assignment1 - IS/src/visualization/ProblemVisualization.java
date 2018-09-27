package visualization;

import javax.swing.JFrame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import search.State;
import search.Action;
import search.SearchProblem;

/** 
 * This class allows visualizing a search problem 
 * which implements the interface ProblemView. 
 * 
 *  Creates the view and visualizes series of actions.
 */
public class ProblemVisualization{

	/** Reference to the problem */
	protected SearchProblem problem;	

	/** Graphical view of the problem. */
	public ProblemView view;  	

	/** Window. */
	private JFrame window;					

	/** 
	 * Constructor. Takes a problem and the size of the window (in pixels) used 
	 * to display the view. 
	 */
	public ProblemVisualization(SearchProblem problem, int sizePx){
		// If the problem can not be visualized, reports the error.
		if (!(problem instanceof ProblemVisualizable)){
			System.out.println("This problem is not visualizable.");
			return;
		}
		
		this.problem = problem;
		
		// Creates the problem view
		this.view = ((ProblemVisualizable) problem).getView(sizePx);
		
		// Creates the window with the view.
		window = new JFrame("Problem visualization");
		window.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		window.getContentPane().add(view);
		window.pack();
		window.setVisible(true);
	}
	
	/** 
	 * Visualizes a sequence of actions starting from an initial state.
	 */
	public void visualize(State initialState, ArrayList<Action> actionSequence){
		State currentState, newState;
		// Current state.
		currentState = initialState;
		// Fixes the state in the view
		view.setState(currentState);
		// Iterates over the actions
		for (Action action: actionSequence){
			// Generates the new state
			newState = problem.applyAction(currentState, action);
			// Visualizes the action from the current state to the newState
			view.takeAction(action, newState);
			// Updates the current state
			currentState = newState;
		}
	}
	
	/** Closes the window */
    public void close(){
    	window.dispose();
    }
    
	/** Main function, used for testing. */
	public static void main(String[] args) {
		// Uses an instance of MazeProblem as example
		problems.maze.MazeProblem problem = new problems.maze.MazeProblem(10, 0, 4);
		problems.maze.MazeState initialState = new problems.maze.MazeState(5,3);
		ArrayList<Action> actions = new ArrayList<Action>();
		actions.add(problems.maze.MazeAction.RIGHT);
		actions.add(problems.maze.MazeAction.RIGHT);
		actions.add(problems.maze.MazeAction.DOWN);
		actions.add(problems.maze.MazeAction.DOWN);
		actions.add(problems.maze.MazeAction.DOWN);
		actions.add(problems.maze.MazeAction.LEFT);
		actions.add(problems.maze.MazeAction.EAT);
		actions.add(problems.maze.MazeAction.UP);
		
		// Remaining code is generic. 
		ProblemVisualization problemVisualization = new ProblemVisualization(problem, 800);
		problemVisualization.visualize(initialState, actions);
		
		// Waits five seconds and closes.
		try {
			Thread.sleep(5000);
		}catch (InterruptedException e){}
		problemVisualization.close();	
	}
}
