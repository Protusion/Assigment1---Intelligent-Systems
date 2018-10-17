package problems.maze;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import search.State;
import search.Action;
import search.SearchProblem;
import utils.Position;

import visualization.ProblemView;
import visualization.ProblemVisualizable;

/** 
 * Extends SearchProblem and implements the functions which define
 * the maze problem. Always uses two cheeses.  
 */
public class MazeProblem implements SearchProblem, ProblemVisualizable{
	
	// Uses always three cheeses (to make it easier implementation).
	private static final int NUM_CHEESES = 3;
	// Penalty factor for fight with the cat. 
	private static final double PENALTY = 2;
	
	/* Maze */
	Maze maze;
	
    /** Builds a maze */
	public MazeProblem(){
		this.maze = new Maze(10);
	}
	
	public MazeProblem(int size, int seed, int cats){
		this.maze = new Maze(size, seed, cats, NUM_CHEESES);
	}

	@Override
	public void setParams(String[] args) {
		// The maze already exists.
		// It is generated with the new parameters. 
		int size=this.maze.size;
		int seed= this.maze.seed;
		int cats = this.maze.numCats;
		
		if (args.length==3)
			try{
			   size = Integer.parseInt(args[0]);
			   cats = Integer.parseInt(args[1]);
			   seed = Integer.parseInt(args[2]);
			} catch(Exception e){
									System.out.println("Parameters for MazeProblem are incorrect.");
									return;
			                     }	
		
		// Generates the new maze. 
		this.maze = new Maze(size, seed, cats, NUM_CHEESES);
	}
	
	// PROBLEM REPRESENTATION (CORRESPONDS TO THE FUNCTIONS IN THE INTERFACE SearchProblem)
	

	@Override
	public State initialState() {
                
		MazeState initialState = new MazeState();
                initialState.position = maze.input();
                initialState.damaged = 0;
                //initialState.cheeseEaten.clear();
                
		return initialState;
	}

	@Override
	public State applyAction(State state, Action action) {
                Set<Position> reachablepositions;
		reachablepositions = maze.reachablePositions(((MazeState)state).position);
                MazeState nextState = ((MazeState)state).clone();
                int x = nextState.position.x;
                int y = nextState.position.y;
                String actionS = action.getId();
                if(actionS.equals("RIGHT") && reachablepositions.contains(new Position(x+1,y))){
                    nextState.position = new Position(x+1,y);
                }else if(actionS.equals("LEFT") && reachablepositions.contains(new Position(x-1,y))){
                    nextState.position = new Position(x-1,y);
                }else if(actionS.equals("UP") && reachablepositions.contains(new Position(x,y+1))){
                    nextState.position = new Position(x,y+1);
                }else if(actionS.equals("DOWN") && reachablepositions.contains(new Position(x,y-1))){
                    nextState.position = new Position(x,y-1);
                }else if(actionS.equals("EAT") && maze.cheesePositions.contains(new Position(x,y))){
                    nextState.cheeseEaten.add(new Position(x,y));
                }
                  
                if(maze.containsCat(nextState.position)){
                    nextState.damaged++;
                }
		return nextState;
	}

	@Override
	public ArrayList<Action> getPossibleActions(State state) {
                ArrayList<Action> possibleActions = new ArrayList<Action>();
                Set<Position> reachablepositions;
                reachablepositions = maze.reachablePositions(((MazeState)state).position);
                int x = ((MazeState)state).position.x;
                int y = ((MazeState)state).position.y;
                for(int i = 0; i < reachablepositions.size();i++){
                    if(reachablepositions.contains(new Position(x+1,y))){
                        possibleActions.add(MazeAction.RIGHT);
                    }else if(reachablepositions.contains(new Position(x-1,y))){
                        possibleActions.add(MazeAction.LEFT);
                    }else if(reachablepositions.contains(new Position(x,y+1))){
                        possibleActions.add(MazeAction.UP);
                    }else if(reachablepositions.contains(new Position(x,y-1))){
                        possibleActions.add(MazeAction.DOWN);
                    }else if(maze.cheesePositions.contains(new Position(x,y))){
                        possibleActions.add(MazeAction.EAT);
                    }
                }
		return possibleActions;
	}

	@Override
	public double cost(State state, Action action) {
		if(((MazeState)state).damaged < 2 && ((MazeState)state).damaged > 1){
                    return 2;
                }
		return 1;
	}

	@Override
	public boolean testGoal(State chosen) {
		return ((MazeState)chosen).position.equals(maze.output()) &&
                        maze.cheesePositions.equals(((MazeState)chosen).cheeseEaten) && 
                        ((MazeState)chosen).damaged < 2;
	}

	@Override
	public double heuristic(State state) {
		// TODO Auto-generated method stub
		return 0;
	}
	
    
	// VISUALIZATION
	/** Returns a panel with the view of the problem. */
	@Override
	public ProblemView getView(int sizePx) {
		return new MazeView(this, sizePx);
	}	
}
