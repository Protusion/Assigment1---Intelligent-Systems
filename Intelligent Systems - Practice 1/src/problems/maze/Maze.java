package problems.maze;

import utils.Position;

import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/** 
 *  This class generates a maze with depth-first search.
 *  Both cheese and cats are randomly distributed. 
 */
public class Maze implements Cloneable{

	// PARAMETERS
	
	/* Size, must be an odd number */
	public final int size;
	/* Seed (used for copy)*/
	public final int seed;
	/* Number of cats */
	protected final int numCats;
	/* Number of cheeses */
	protected final int numCheeses;
	/* Probability of introducing an aditional links */
    protected final double probAddLinks = 0.15;
    
    // MAZE. 

	/* Undirected graph. It is stored in a HashMap so that nodes can be accessed directly.
	 * cellGraph.get((x,y)) returns the list of positions reachable from (x,y) */
	protected HashMap<Position, Set<Position>> cellGraph;
	/* Input and output positions */
	protected int inputX, outputX;
	/* Cats and cheeses */
	protected Set<Position> catPositions;
	protected Set<Position> cheesePositions;
	
	
	// UTILS
	
	/* Random number generator. */
	private static Random random = new Random();
	
	// PUBLIC METHODS (MAZE API)
	
	/** Creates a maze with seed equals 0 */
	public Maze(int size){
		this(size, 0);  // Calls the other constructor
	}
	
	/** Creates a maze with a given seed */
	public Maze(int size, int seed){
		this(size, seed, 4, 2);
	}
	
	/** Creates a maze with a given seed */
	public Maze(int size, int seed, int numCats, int numCheeses){
		this.size = size;
		this.seed = seed;
		this.numCats = numCats;
		this.numCheeses = numCheeses;
		generate(seed);
	}
	
	// The maze can only be read from outside the package. 
	
	/** Returns the set of cells reachable from anocher */
	public Set<Position> reachablePositions(int x, int y){ return cellGraph.get(new Position(x,y)); }	
	public Set<Position> reachablePositions(Position cell){ return cellGraph.get(cell); }	
	
	/** Input and output */
	public Position input() { return new Position(inputX,0); }
	public Position output() { return new Position(outputX, size-1); }
	
	/* Cats and cheese */
	public boolean containsCat(Position position) { return catPositions.contains(position); }
	public boolean containsCat(int x, int y) { return catPositions.contains(new Position(x,y)); }
	
	public boolean containsCheese(Position position) { return cheesePositions.contains(position); }
	public boolean containsCheese(int x, int y) { return cheesePositions.contains(new Position(x,y)); }
	
	// PRIVATE
	
	/** Generates the maze with depth-search method. */
	private void generate(int seed){
		// Fixes the seed
		random.setSeed(seed);
		// Creates the graph
		cellGraph = new HashMap<Position, Set<Position>>();	
		// Generates the graph
		generateGraph();
		// Input and output
		inputX = random.nextInt(size);
		outputX = random.nextInt(size);
		// Puts the cats
		addCats();
		// Puts the cheese
		addCheese();
	}
	
	/** Generates the graph representing the maze */
	private void generateGraph() {
		// Creates the first node and uses it as root of the tree
		Position origin = new Position(random.nextInt(size), random.nextInt(size));
		addNode(origin);
		// Generates the maze recursively
		explore(origin);
	}
	
	/** Auxiliary function for building the graph. Explores and expands a node */
	private void explore(Position currentCell){
		// Generates the adjacencies
		ArrayList<Position> surroundingCells = surroundingCells(currentCell);
		// Iterates over the adjacencies
		for (Position neighbourCell: surroundingCells){
			// If the corresponding position has not been explored.
			if (!cellGraph.containsKey(neighbourCell)){
				// Adds the node and link
				addNode(neighbourCell);
				addLink(currentCell, neighbourCell);		
				// Explores the new node
				explore(neighbourCell);
			}
			// Open additional paths to make solution easier (add links)
			else if (random.nextDouble()<probAddLinks){
				addLink(currentCell, neighbourCell);	
			}
		}
	}
	
	/** Adds the node corresponding to a position*/
	private void addNode(Position cell){
		cellGraph.put(cell, new HashSet<Position>());
	}
	
	/** Adds the link */
	private void addLink(Position cell1, Position cell2){
		cellGraph.get(cell1).add(cell2);
		cellGraph.get(cell2).add(cell1);
	}
	
	/** Generate the possible adjacencies for a cell */
	private ArrayList<Position> surroundingCells(Position cell){
		// Coordinates
		int posX = cell.x;
		int posY = cell.y;
		// Creates the list
		ArrayList<Position> adjacencies = new ArrayList<Position>();
		// Generates the adjacencies
		if (posY>0) adjacencies.add(new Position(posX,posY-1));        // UP
		if (posY<size-1) adjacencies.add(new Position(posX,posY+1));   // DOWN
		if (posX>0) adjacencies.add(new Position(posX-1,posY));        // LEFT
		if (posX<size-1) adjacencies.add(new Position(posX+1,posY));   // RIGHT
		// Shuffles and returns
		Collections.shuffle(adjacencies, random);
		return adjacencies;
	}

	/** Adds the cats */
	private void addCats() {
		catPositions = new HashSet<Position>();
		int countCats = 0;
		Position catPosition;
		do{
			catPosition = new Position(random.nextInt(size), random.nextInt(size));
			if (!catPositions.contains(catPosition)){
				catPositions.add(catPosition);
				countCats++;
			}
		} while(countCats<numCats);			
	}
	
	/** Adds the cheese */
	private void addCheese(){
		cheesePositions = new HashSet<Position>();
		int countCheese = 0;
		Position cheesePosition;
		do{
			cheesePosition = new Position(random.nextInt(size), random.nextInt(size));
			if (!cheesePositions.contains(cheesePosition) && !catPositions.contains(cheesePosition)){
				cheesePositions.add(cheesePosition);
				countCheese++;
			}
		} while(countCheese<numCheeses);		
	}
	
	/** Clones the maze */;
	public Maze clone(){
		return new Maze(this.size, this.seed, this.numCats, this.numCheeses);
	}
    // MAIN
	
	/** Main function, used for testing. */
	public static void main(String[] args){
		Maze maze = new Maze(6,0);
	}
}
