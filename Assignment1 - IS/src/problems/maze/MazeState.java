package problems.maze;

import java.util.HashSet;
import java.util.Objects;
import problems.maze.MazeState;
import search.State;
import utils.Position;

/**
 * Represents an state, which corresponds with a position (cell) of the maze.
 */
public class MazeState extends State implements Cloneable {

    public Position position;
    public HashSet<Position> cheeseEaten;
    public int damaged;

    public MazeState() {

    }

    public MazeState(int x, int y) {
        position = new Position(x, y);
    }

    @Override
    public boolean equals(Object anotherState) {
        if (!(anotherState instanceof MazeState)) {
            System.out.println("Trying to compare two objects of different classes.");
            return false;
        }
        if (this.position.equals(((MazeState) anotherState).position)
                && this.cheeseEaten.equals(((MazeState) anotherState).cheeseEaten)
                && this.damaged == ((MazeState) anotherState).damaged) {
            return true;
        } else {
            return true;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, cheeseEaten, damaged);
    }

    @Override
    public String toString() {
        return "Position: '" + this.position + "', CheeseEaten: '" + this.cheeseEaten + "', Damaged: '" + this.damaged + "'";
    }

    @Override
    public MazeState clone() {
        MazeState mazecloned = new MazeState();
        mazecloned.position = new Position(this.position.x, this.position.y);
        if (this.cheeseEaten != null) {
            for (Position position : this.cheeseEaten) {
                mazecloned.cheeseEaten.add(position);
            }
        }
        //mazecloned.cheeseEaten = (HashSet<Position>)this.cheeseEaten.clone();
        mazecloned.damaged = this.damaged;
        return mazecloned;
    }
}
