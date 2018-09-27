import java.util.ArrayList;
import java.util.Arrays;

import search.SearchAlgorithm;
import search.SearchProblem;
import search.Action;
import visualization.ProblemVisualization;
import utils.AlgorithmLoader;
import utils.ProblemLoader;

/** Utility class. Solves a search problem with a search algorithm and shows stats. */
public class Solver {

	public static void main(String[] args){
		// The first argument is the size of the window (0 means no window)
		int sizePx = Integer.parseInt(args[0]);
		
		// Separation mark between problem and algorithms ('--')
		int sep;
		for (sep=1;sep<args.length;sep++)
			if (args[sep].equals("--"))
				break;
		
		// Generates the problem. 
		String problemName = args[1];
		String[] problemParams = Arrays.copyOfRange(args, 2, sep);
		SearchProblem problem = ProblemLoader.generateProblem(problemName, problemParams);

		// Generates the algorithm. 
		String algorithmName = args[sep+1];
		String[] algorithmParams = Arrays.copyOfRange(args, sep+2, args.length);
		SearchAlgorithm algorithm = AlgorithmLoader.generateAlgorithm(algorithmName, algorithmParams);

		// Sets the problem
		algorithm.setProblem(problem);
		// Carries out the search.
		algorithm.search();
		
		// Reads the solution
		ArrayList<Action> solution = algorithm.result();
		
		// Shows the results
		System.out.println("Problem :"+problemName);
		System.out.println("Results for the algorithm: "+algorithmName);
		
		if (algorithm.getSolutionFound())
			System.out.println("\tCost:"+algorithm.getTotalCost());
		else
			System.out.println("NO SOLUTION FOUND");
		
		System.out.println("\tExpanded nodes: "+algorithm.getExpandedNodes());	
		System.out.println("\tMaximum size of the set of open nodes: "+algorithm.getOpenMaxSize());
		System.out.println("\tMaximum size of the set of explored states: "+algorithm.getExploredMaxSize());
		System.out.println("\tTime (milliseconds): "+algorithm.getSearchTime());	
		
		// Returns if solution does not have to be shown.
		if (sizePx==0)
			System.exit(0);
		
		// Visualization
		ProblemVisualization problemVisualization = new ProblemVisualization(problem, sizePx);
		problemVisualization.visualize(problem.initialState(), solution);		
		// Waits five seconds and closes.
		try {
			Thread.sleep(5000);
		}catch (InterruptedException e){}
		problemVisualization.close();		
	}	
}
