package problems.maze;

import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;

import search.State;
import search.Action;
import search.SearchProblem;
import utils.Position;

import visualization.ProblemView;
import visualization.ProblemVisualizable;

/**
 * Extends SearchProblem and implements the functions which define the maze problem. Always uses two cheeses.
 */
public class MazeProblem implements SearchProblem, ProblemVisualizable {

    // Uses always three cheeses (to make it easier implementation).
    private static final int NUM_CHEESES = 3;
    // Penalty factor for fight with the cat. 
    private static final double PENALTY = 2;

    /* Maze */
    Maze maze;

    /**
     * Builds a maze
     */
    public MazeProblem() {
        this.maze = new Maze(10);
    }

    public MazeProblem(int size, int seed, int cats) {
        this.maze = new Maze(size, seed, cats, NUM_CHEESES);
    }

    @Override
    public void setParams(String[] args) {
        // The maze already exists.
        // It is generated with the new parameters. 
        int size = this.maze.size;
        int seed = this.maze.seed;
        int cats = this.maze.numCats;

        if (args.length == 3) {
            try {
                size = Integer.parseInt(args[0]);
                cats = Integer.parseInt(args[1]);
                seed = Integer.parseInt(args[2]);
            } catch (Exception e) {
                System.out.println("Parameters for MazeProblem are incorrect.");
                return;
            }
        }

        // Generates the new maze. 
        this.maze = new Maze(size, seed, cats, NUM_CHEESES);
    }

    // PROBLEM REPRESENTATION (CORRESPONDS TO THE FUNCTIONS IN THE INTERFACE SearchProblem)
    @Override
    public State initialState() {
        /* Obtains the initial potition fromt the maze and assigns it to the position of the first state */
        MazeState initialState = new MazeState(maze.input().x, maze.input().y);

        return initialState;
    }

    @Override
    public State applyAction(State state, Action action) {
        MazeState nextState = ((MazeState) state).clone(); // Clones the state passed through parameters
        /* Checks if the position of the given state contains a cat, if so, damage is updated */
        if (maze.containsCat(nextState.position)) {
            nextState.damaged++;
        }
        /* Extracting x and y coordinates from the given state */
        int x = nextState.position.x;
        int y = nextState.position.y;
        /* Checking which action it is */
        if (action.equals(MazeAction.EAT)) {
            nextState.cheeseEaten.add(new Position(x, y)); // If eat, updates nextState position
        } else if (action.equals(MazeAction.RIGHT)) {
            nextState.position = new Position(x + 1, y); // If right, updates nextState position
        } else if (action.equals(MazeAction.LEFT)) {
            nextState.position = new Position(x - 1, y); // If left, updates nextState position
        } else if (action.equals(MazeAction.UP)) {
            nextState.position = new Position(x, y - 1); // If up, updates nextState position
        } else if (action.equals(MazeAction.DOWN)) {
            nextState.position = new Position(x, y + 1); // If down, updates nextState position
        }
        return nextState; // Returns a new state with the position that the action leads to
    }

    @Override
    public ArrayList<Action> getPossibleActions(State state) {
        /* ArrayList of possible actions to be returned */
        ArrayList<Action> PossibleActions = new ArrayList<Action>();
        /* ArrayList of the adjacent possible positions */
        Set<Position> ReachablePositions = maze.reachablePositions(((MazeState) state).position);
        /* x and y coordinates from the given state */
        int x = ((MazeState) state).position.x;
        int y = ((MazeState) state).position.y;
        /* Checks the state's damage */
        if (((MazeState) state).damaged < 2) {
            
            for (Position position : ReachablePositions) {
                if (position.equals(new Position(x, y + 1))) {
                    PossibleActions.add(MazeAction.DOWN); // If DOWN is possible, update the ArrayList of possible actions
                } else if (position.equals(new Position(x + 1, y))) {
                    PossibleActions.add(MazeAction.RIGHT); // If RIGHT is possible, update the ArrayList of possible actions
                } else if (position.equals(new Position(x, y - 1))) {
                    PossibleActions.add(MazeAction.UP); // If UP is possible, update the ArrayList of possible actions
                } else if (position.equals(new Position(x - 1, y))) {
                    PossibleActions.add(MazeAction.LEFT); // If LEFT is possible, update the ArrayList of possible actions
                }
            }
            if (maze.containsCheese(new Position(x, y))) {
                PossibleActions.add(MazeAction.EAT); // If EAT is possible, update the ArrayList of possible actions
            }
        }
        return PossibleActions; // Returns the set of possible actions
    }

    @Override
    public double cost(State state, Action action) {
        /* Checks damage */
        if (((MazeState) state).damaged == 1 && !action.equals(MazeAction.EAT)) {
            return PENALTY; // Damaged -> Returns PENALTY (cost per action = 2)
        }
        return 1; // Not damaged -> Returns normal cost (cost per action = 1)
    }

    @Override
    public boolean testGoal(State chosen) {
        /* Checks position, cheeses eaten and damage */
        return ((MazeState) chosen).position.equals(maze.output())
                && maze.cheesePositions.equals(((MazeState) chosen).cheeseEaten)
                && ((MazeState) chosen).damaged < 2;
    }

    /* -------------------------------- HEURISTICS ---------------------------------------------- */
 /* Straight path from the current position to the exit (No walls No cats)*/
    @Override
    public double heuristic(State state) {
        return abs(((MazeState) state).position.x - maze.output().x) + abs(((MazeState) state).position.y - maze.output().y);
    }

    /* Gets only 1 cheese (if there is any left), which is the furthest cheese from the exit. After it goes to the exit */
    public double heuristic2(State state) {
        Position startingPosition = new Position(((MazeState) state).position.x, ((MazeState) state).position.y);
        Position furthestCheese = null;
        for(Position cheese : maze.cheesePositions){
            furthestCheese = cheese;
            break;
        }
        if (!((MazeState) state).cheeseEaten.isEmpty()) {
            double distance = 0, cheeseD;
            for (Position cheese : ((MazeState) state).cheeseEaten) {
                cheeseD = abs(startingPosition.x - cheese.x) + abs(startingPosition.y - cheese.y);
                if (distance < cheeseD) {
                    cheeseD = distance;
                    furthestCheese = cheese; 
                }
            }
            return distance + abs(maze.output().x - furthestCheese.x) + abs(maze.output().y - furthestCheese.y);
        }
        return abs(startingPosition.x - maze.output().x) + abs(startingPosition.y - maze.output().y);
    }

    /* Looks for the closest cheese from the current position until it gets to the exit (No walls No cats */
    public double heuristic3(State state) {
        Position actualPosition = new Position(((MazeState) state).position.x, ((MazeState) state).position.y);
        LinkedList<Position> cheesesPositions = new LinkedList<Position>();
        for (Position cheeses : maze.cheesePositions) {
            if (!((MazeState) state).cheeseEaten.contains(cheeses)) {
                cheesesPositions.add(cheeses);
            }
        }

        int x, y, x1, y1;
        double distance = Double.MAX_VALUE, totalDistance = 0;
        x = actualPosition.x;
        y = actualPosition.y;
        int numberCheeses = cheesesPositions.size();

        for (int i = 0; i < numberCheeses; i++) {
            Position cheeseselected = new Position(0, 0);
            for (Position cheeseI : cheesesPositions) {
                x1 = cheeseI.x;
                y1 = cheeseI.y;
                double distancetocheese;
                distancetocheese = abs((x - x1)) + abs((y - y1));

                if (distance > distancetocheese) {
                    distance = distancetocheese;
                    cheeseselected = cheeseI;
                }

            }
            totalDistance += distance;
            x = cheeseselected.x;
            y = cheeseselected.y;
            cheesesPositions.remove(cheeseselected);
            distance = Double.MAX_VALUE;
        }

        totalDistance = totalDistance + abs(x - maze.output().x) + abs(y - maze.output().y);
        return totalDistance;
    }

    /* Looks for the shortest path going from the furthest cheeses to the exit (No walls No cats */
    public double heuristic4(State state) {
        Position actualPosition = new Position(((MazeState) state).position.x, ((MazeState) state).position.y);
        LinkedList<Position> cheesesPositions = new LinkedList<Position>();
        for (Position cheeses : maze.cheesePositions) {
            if (!((MazeState) state).cheeseEaten.contains(cheeses)) {
                cheesesPositions.add(cheeses);
            }
        }

        int x, y, x1, y1;
        double distance = 0, totalDistance = 0;
        x = actualPosition.x;
        y = actualPosition.y;
        int numberCheeses = cheesesPositions.size();

        for (int i = 0; i < numberCheeses; i++) {
            Position cheeseselected = new Position(0, 0);
            for (Position cheeseI : cheesesPositions) {
                x1 = cheeseI.x;
                y1 = cheeseI.y;
                double distancetocheese;
                distancetocheese = abs((maze.output().x - x1)) + abs((maze.output().y - y1));

                if (distance < distancetocheese) {
                    cheeseselected = cheeseI;
                    distance = distancetocheese;
                }

            }
            totalDistance += abs(x - cheeseselected.x) + abs(y - cheeseselected.y);
            x = cheeseselected.x;
            y = cheeseselected.y;
            cheesesPositions.remove(cheeseselected);
            distance = 0;
        }

        totalDistance += abs(x - maze.output().x) + abs(y - maze.output().y);
        System.out.println(totalDistance);
        return totalDistance;
    }

    /* Looks for the closest cheese until all 3 are eaten, after that, it goes to the exit (No walls No cats) */
    public double heuristic5(State state) {
        Position actualPosition = new Position(((MazeState) state).position.x, ((MazeState) state).position.y);
        LinkedList<Position> cheesesPositions = new LinkedList<Position>();
        for (Position cheeses : maze.cheesePositions) {
            if (!((MazeState) state).cheeseEaten.contains(cheeses)) {
                cheesesPositions.add(cheeses);
            }
        }

        int x, y, x1, y1;
        double distance = Double.MAX_VALUE, totalDistance = 0;
        x = actualPosition.x;
        y = actualPosition.y;
        int numberCheeses = cheesesPositions.size();

        for (int i = 0; i < numberCheeses; i++) {
            Position cheeseselected = new Position(0, 0);
            for (Position cheeseI : cheesesPositions) {
                x1 = cheeseI.x;
                y1 = cheeseI.y;
                double distancetocheese;
                distancetocheese = abs((x - x1)) + abs((y - y1));

                if (distance > distancetocheese) {
                    distance = distancetocheese;
                    cheeseselected = cheeseI;
                }

            }
            totalDistance += distance;
            x = cheeseselected.x;
            y = cheeseselected.y;
            cheesesPositions.remove(cheeseselected);
            distance = Double.MAX_VALUE;
        }

        totalDistance = totalDistance + abs(x - maze.output().x) + abs(y - maze.output().y);
        return totalDistance;
    }

    // VISUALIZATION
    /**
     * Returns a panel with the view of the problem.
     */
    @Override
    public ProblemView getView(int sizePx) {
        return new MazeView(this, sizePx);
    }
}
