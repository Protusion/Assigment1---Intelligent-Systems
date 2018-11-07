/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import search.*;

/**
 *
 * @author Alberto
 */
public class DepthLimited extends SearchAlgorithm {

    protected ArrayList<State> explored = new ArrayList<State>(); // Set of explored nodes
    protected Stack<Node> open = new Stack<Node>();  // LIFO Queue (Stack)
    protected int limit;

    @Override
    public void setParams(String[] params) {
        limit = Integer.valueOf(params[0]);
    }

    public void calculateSolution(Node node) {
        this.actionSequence.add(node.getAction());
        Node currentNode = node.getParent();
        this.totalCost += problem.cost(currentNode.getState(), currentNode.getAction());

        while (!this.isInitialNode(currentNode)) {
            this.totalCost += problem.cost(currentNode.getState(), currentNode.getAction());
            this.actionSequence.add(currentNode.getAction());
            currentNode = currentNode.getParent();
        }

        Collections.reverse(actionSequence);
    }

    @Override
    public void doSearch() {
        Node initialNode = new Node(this.problem.initialState());
        open.push(initialNode);
        while (!open.isEmpty() && !solutionFound) {
            Node extractedNode = open.pop();
            if (!explored.contains(extractedNode.getState())) {
                if (problem.testGoal(extractedNode.getState())) {
                    this.solutionFound = true;
                    this.calculateSolution(extractedNode);
                } else {
                    explored.add(extractedNode.getState());
                    this.exploredMaxSize++;
                    if (extractedNode.getDepth() < limit) {
                        for (Node node : getSuccessors(extractedNode)) {
                            open.add(node);
                        }
                    }
                }
            }
            if (open.size() > this.openMaxSize) {
                this.openMaxSize = open.size();
            }
        }
    }
}
