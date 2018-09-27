package problems.maze;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

import java.util.Set;

import visualization.ProblemView;
import search.Action;
import search.State;
import utils.Position;

/** Shows a maze */
public class MazeView extends ProblemView{
	
	// Colors
	private static Color boundsColor = new Color(200,200,200);
	private static Color sandColor = new Color(255,222,173);
	private static Color woodColor = new Color(90,61,39);
	private static Color grassColor= new Color(57,127,40);
	private static Color backgroundColor = new Color(120,100,50);
	private static Color waterColor = new Color(34, 79, 189);
	
	// Images
	public static final Image hamster = Toolkit.getDefaultToolkit().getImage("problems/maze/imgs/hamster.png");
	public static final Image hamster2 = Toolkit.getDefaultToolkit().getImage("problems/maze/imgs/hamster2.png");
	public static final Image cheese = Toolkit.getDefaultToolkit().getImage("problems/maze/imgs/queso.png");	
	public static final Image cat = Toolkit.getDefaultToolkit().getImage("problems/maze/imgs/cat.png");
	public static final Image cat2 = Toolkit.getDefaultToolkit().getImage("problems/maze/imgs/cat2.png");
	// Image of the maze
	BufferedImage mazeImage;		
	// Scaled images
	Image scaledHamster, scaledHamster2, scaledCheese, scaledCat, scaledCat2;	
			
	
	// Maze and status	
	private MazeProblem mazeProblem;			       // Maze	
	private MazeState currentState = null;          // Current state
	
	// Some measures of interest
	private int sizePx = 600; 				// Size of the view
	private int cellSizePx;					// Size of each cell	
	private int marginPx;					// Size of the margin	
	private int boundWidthPx;               // Size of the bounds
	private double speedPx = 10;	         	// Speed of the hamster (pixels each 0.05s)	
	
	// Position of the hamster (keeps track)
	private Position posHamsterPx;		    // Coordinates of the hamster in pixels depends on the state and MOVEMENT

	/**
	 * Builds the view panel given a maze and its size in pixels
	 */
	public MazeView(MazeProblem mazeProblem, int sizePx){
		this.mazeProblem = mazeProblem;
		this.sizePx = sizePx;
		// Calculates dimensions
		cellSizePx = (sizePx-40) / mazeProblem.maze.size;
		marginPx = (sizePx - cellSizePx * mazeProblem.maze.size)/2;
		boundWidthPx = marginPx/2;
		speedPx = (cellSizePx*2)/20;	// pixels each 1/20 second  (Four cells/second)	
		
		// Scales the images according to the size
		scaledHamster = hamster.getScaledInstance((int)(cellSizePx*0.5), (int)(cellSizePx*0.5), Image.SCALE_SMOOTH);
		scaledHamster2 = hamster2.getScaledInstance((int)(cellSizePx*0.5), (int)(cellSizePx*0.5), Image.SCALE_SMOOTH);
		scaledCheese = cheese.getScaledInstance((int)(cellSizePx*0.5), (int)(cellSizePx*0.5), Image.SCALE_SMOOTH);
		scaledCat = cat.getScaledInstance((int)(cellSizePx*0.5), (int)(cellSizePx*0.5), Image.SCALE_SMOOTH);
		scaledCat2 = cat2.getScaledInstance((int)(cellSizePx*0.5), (int)(cellSizePx*0.5), Image.SCALE_SMOOTH);
		
		// Generates the background
		generateMazeImage();		
	}
	
	// INTERFACE
	
	/** Updates the position of the hamster */
	@Override
	public void setState(State currentState) {
		this.currentState = (MazeState) currentState;
		posHamsterPx = posImageToPx(this.currentState.position);
		repaint();
	}

	/** Moves the hamster */
	@Override
	public void takeAction(Action action, State toState) {
		MazeAction mazeAction = ((MazeAction) action);
		MazeState mazeToState = ((MazeState) toState);
		currentState = mazeToState;
		/// Waits one second to eat
		if (mazeAction == MazeAction.EAT)
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
		// Otherwise, moves.
		else	
			moveHamsterToPosition(mazeToState.position);
		
		// If there is a cat, also waits one second
		if (mazeProblem.maze.catPositions.contains(currentState.position))
		try {
			Thread.sleep(1000);
		} 	catch (InterruptedException e) {}		
	}
	
	/** Move hamster to position */
	public void moveHamsterToPosition(Position position){
		Position positionPx = posImageToPx(position);
		moveHamsterToPositionPx(positionPx);
	}
    
	// GRAPHICS
		
	/** Generates the main maze image (Background and maze, no images) */
	private void generateMazeImage(){
		// Creates the image
		mazeImage=new BufferedImage(sizePx, sizePx, BufferedImage.TYPE_INT_RGB);
		Graphics2D mazeGraphics2D = mazeImage.createGraphics();		
		mazeGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,	RenderingHints.VALUE_ANTIALIAS_ON);
		
		// Background and grass
		mazeGraphics2D.setColor(grassColor);
		mazeGraphics2D.fillRect(0, 0, sizePx, sizePx);	
		mazeGraphics2D.setColor(backgroundColor);
		mazeGraphics2D.fillRect(1*marginPx, 1*marginPx, sizePx-2*marginPx, sizePx-2*marginPx);	
		
		// Paints the bounds (tiles)
		mazeGraphics2D.setColor(boundsColor);
		for (int pos=0;pos<mazeProblem.maze.size;pos++){
			int posBoundPx= posToPx(pos);
			if (pos!=mazeProblem.maze.inputX) mazeGraphics2D.fill3DRect(posBoundPx, marginPx/2, cellSizePx, marginPx/2,true);	
			if (pos!=mazeProblem.maze.outputX) mazeGraphics2D.fill3DRect(posBoundPx, sizePx-marginPx, cellSizePx, marginPx/2,true);
			mazeGraphics2D.fill3DRect(marginPx/2, posBoundPx, marginPx/2, cellSizePx,true);
			mazeGraphics2D.fill3DRect(sizePx-marginPx, posBoundPx, marginPx/2, cellSizePx,true);			
		}
		// Corners
		mazeGraphics2D.fill3DRect(marginPx/2, marginPx/2, marginPx/2, marginPx/2,true);
		mazeGraphics2D.fill3DRect(sizePx-marginPx, marginPx/2, marginPx/2, marginPx/2,true);
		mazeGraphics2D.fill3DRect(marginPx/2, sizePx-marginPx, marginPx/2, marginPx/2,true);
		mazeGraphics2D.fill3DRect(sizePx-marginPx, sizePx-marginPx, marginPx/2, marginPx/2,true);
		
		// Paints the cells
		mazeGraphics2D.setColor(sandColor);
		Position posCellPx;                   // Position of the current cell
		int[] cellBoundsPx = {0,0,0,0};       // Margins of the cell (mark walls)
		for (int posX=0;posX<mazeProblem.maze.size;posX++){
			for (int posY=0;posY<mazeProblem.maze.size;posY++){
				    posCellPx = posToPx(new Position(posX,posY));
				    cellBoundsPx = cellBounds(posX, posY); // Calculate the limits according to connections.
					RoundRectangle2D cellShape = new RoundRectangle2D.Double(posCellPx.x+1+cellBoundsPx[0], posCellPx.y+1+cellBoundsPx[1], cellSizePx+-1+cellBoundsPx[2], cellSizePx-1+cellBoundsPx[3], 10, 10);
					mazeGraphics2D.fill(cellShape);						
			}
		}	
	}
	
	/** Paints the component (updates images)*/
	public void paintComponent(Graphics graphics) {
		Graphics2D graphics2D = (Graphics2D) graphics;
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,	RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2D.drawImage(mazeImage,0,0,this);	
		
		Position posFigurePx;

		// Paints the hamster
		if (currentState!=null) {
			//******************
			// COMPLETAR. 
			//
			// if (el hamster no está dañado)
			//    graphics2D.drawImage(scaledHamster, posHamsterPx.x, posHamsterPx.y, this);
			// else
		    //    graphics2D.drawImage(scaledHamster2, posHamsterPx.x, posHamsterPx.y, this);
			//
			//******************
		}		
		
		// Paints the cats
		for (Position catPosition: mazeProblem.maze.catPositions) {
			posFigurePx= posImageToPx(catPosition);
			if (catPosition.equals(currentState.position))
				graphics2D.drawImage(scaledCat, posFigurePx.x, posFigurePx.y, this);	
			else
				graphics2D.drawImage(scaledCat2, posFigurePx.x, posFigurePx.y, this);	
		}
		
		// Paints the cheeses
		//for (Position cheesePosition: mazeProblem.maze.cheesePositions)
			//******************
			// COMPLETAR. 
			//
			// if ( El hamster no se ha comido el queso en cheese position){
            //	posFigurePx= posImageToPx(cheesePosition);
			//	graphics2D.drawImage(scaledCheese, posFigurePx.x, posFigurePx.y, this);
			// }
			//
			//******************	
	}	
	
	
	/** Considers reachable cells to paint each cell */
	private int[] cellBounds(int posX, int posY) {
		int[] cellBoundsPx = {0,0,0,0}; 
		Set<Position> reachable = mazeProblem.maze.reachablePositions(new Position(posX, posY));
		if (!reachable.contains(new Position(posX,posY-1))) { 		// WALL UP
			cellBoundsPx[1]=5;
			cellBoundsPx[3]=-5;
		}
		if (!reachable.contains(new Position(posX,posY+1))) {		// WALL DOWN
			cellBoundsPx[3]=cellBoundsPx[3]-5;
		}	
		if (!reachable.contains(new Position(posX+1,posY))) {       // WALL RIGHT
			cellBoundsPx[2]=-5;
		}
		if (!reachable.contains(new Position(posX-1,posY))) {       // WALL LEFT
			cellBoundsPx[0]=5;
			cellBoundsPx[2]=cellBoundsPx[2]-5;
		}
		return cellBoundsPx;
	}
	

	/** Moves the hamster to a certain position, given in pixels*/
	private void moveHamsterToPositionPx(Position positionPx){

		// Calculates the distance
		int distX = positionPx.x-posHamsterPx.x;
		int distY = positionPx.y-posHamsterPx.y;
		double dist = Math.sqrt(distX*distX+distY*distY);
		
		// Calculates the number of frames
		int numFrames = (int) (dist/speedPx);
		
		// Moves the hamster
		int newX, newY;
		for ( ; numFrames>0; numFrames--) {
			newX=posHamsterPx.x+(positionPx.x-posHamsterPx.x)/numFrames;
			newY=posHamsterPx.y+(positionPx.y-posHamsterPx.y)/numFrames;
			posHamsterPx = new Position(newX, newY);      // New position
			repaint();
			try {                            // Waits 0.05 seconds
				Thread.sleep(50);
			} catch (InterruptedException e) {}
		}
	}
	
	// UTILS

	/** Changes a position to pixels */
	private int posToPx(int x){ 
		return (int) (x * cellSizePx + marginPx); 
	}
	private Position posToPx(Position position){
		return new Position((int) (position.x * cellSizePx + marginPx), (int) (position.y * cellSizePx + marginPx)); 
	}	
	private int[] posToPx(int x, int y){
		int[] posPx = {(int) (x * cellSizePx + marginPx), (int) (y * cellSizePx + marginPx)};
		return posPx;
	}
	/** Calculates the position of an image, given the position of the cell.*/
	private Position posImageToPx(Position position){
		int x = (int) (position.x * cellSizePx + marginPx + cellSizePx*0.25);
		int y = (int) (position.y * cellSizePx + marginPx + cellSizePx*0.25);
		return new Position(x, y); 
	}
	/** Returns the dimension of the view */
    public Dimension getPreferredSize() {
        return new Dimension(sizePx, sizePx);
    }
 
}
