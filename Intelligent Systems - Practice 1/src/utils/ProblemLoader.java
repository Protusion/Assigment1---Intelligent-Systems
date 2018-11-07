package utils;

import search.SearchProblem;

/**
 * This class allows creating the instance of an problem given its name and parameters.
 */
public class ProblemLoader{
	/**
	 * Takes the name of the class implementing the search problem and returns an instance.
	 */
	public static SearchProblem generateProblem(String problemName, String[] params){
		try{
			@SuppressWarnings("unchecked")
			Class<SearchProblem> problemClass = (Class<SearchProblem>) Class.forName("problems."+problemName);
			SearchProblem problem = problemClass.newInstance();
			problem.setParams(params);
			return problem;
		}
		catch (Exception E){
			System.out.println("The problem "+problemName+" can't be built.");
			System.exit(-1);
		}
		return null;
	}	  
}
