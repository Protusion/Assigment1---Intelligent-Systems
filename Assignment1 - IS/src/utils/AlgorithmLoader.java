package utils;

import search.SearchAlgorithm;

/**
 * This class allows creating the instance of an algorithm given its name.
 */
public class AlgorithmLoader{
	/**
	 * Takes the name of the class implementing the search algorithm and
	 * returns an instance.
	 */
	public static SearchAlgorithm generateAlgorithm(String algorithmName, String[] params){
		try{
			Class algorithmClass = Class.forName("algorithms."+algorithmName);
			SearchAlgorithm algorithm = (SearchAlgorithm) algorithmClass.newInstance();
			algorithm.setParams(params);
			return algorithm;
		}
		catch (Exception E){
			System.out.println("Impossible to build an instance of "+algorithmName);
			System.exit(-1);
		}
		return null;
	}
}
