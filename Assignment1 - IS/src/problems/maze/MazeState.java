package problems.maze;

import problems.maze.MazeState;
import search.State;
import utils.Position;

/**
 *  Represents an state, which corresponds with a position (cell) of the maze.
 */
public class MazeState extends State implements Cloneable{
	
	/** An state is includes a position given by the coordinates (x,y) */
	public Position position;

	@Override
	public boolean equals(Object anotherState) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}
}
