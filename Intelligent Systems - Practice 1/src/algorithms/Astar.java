/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import search.*;

/**
 *
 * @author Alberto
 */
public class Astar extends SearchAlgorithm {

    private Set<State> explored = new HashSet<State>(); // Set of explored nodes
    private PriorityQueue<Node> open = new PriorityQueue<Node>(Node.BY_EVALUATION);

    @Override
    public void setParams(String[] params) {

    }

    public void calculateSolution(Node node) {
        this.actionSequence.add(node.getAction());
        Node currentNode = node.getParent();
        this.totalCost = node.getCost();

        while (!this.isInitialNode(currentNode)) {
            this.actionSequence.add(currentNode.getAction());
            currentNode = currentNode.getParent();
        }

        Collections.reverse(actionSequence);
    }

    @Override
    public void doSearch() {
        Node initialNode = new Node(this.problem.initialState());
        open.add(initialNode);
        while (!open.isEmpty() && !solutionFound) {
            Node extractedNode = open.poll();
            if (!explored.contains(extractedNode.getState())) {
                if (problem.testGoal(extractedNode.getState())) {
                    this.solutionFound = true;
                    this.calculateSolution(extractedNode);
                } else {
                    explored.add(extractedNode.getState());
                    this.exploredMaxSize++;
                    for (Node node : getSuccessors(extractedNode)) {
                        open.add(node);
                    }
                    
                }
            }
            if (open.size() > this.openMaxSize) {
                        this.openMaxSize = open.size();
                    }

        }
    }
}
