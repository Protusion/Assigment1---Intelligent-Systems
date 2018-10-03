package problems.maze;

import java.util.HashSet;
import problems.maze.MazeState;
import search.State;
import utils.Position;

/**
 *  Represents an state, which corresponds with a position (cell) of the maze.
 */
public class MazeState extends State implements Cloneable{
	
	public Position position;
        public HashSet<Position> cheeseEaten;
        public boolean damaged;

	@Override
	public boolean equals(Object anotherState) {
		if (!(anotherState instanceof MazeState)){
			System.out.println("Trying to compare two objects of different classes.");
			return false;
		}
		if(this.position.equals(((MazeState)anotherState).position) && 
                   this.cheeseEaten.equals(((MazeState)anotherState).cheeseEaten) && 
                   this.damaged == ((MazeState)anotherState).damaged) return true;
		else return true;
	}

	@Override
	public int hashCode() {
		
		return 0;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}
}
