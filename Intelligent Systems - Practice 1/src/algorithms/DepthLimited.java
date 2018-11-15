/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import search.*;

/**
 *
 * @author Alberto
 */
public class DepthLimited extends SearchAlgorithm {

    private Set<State> explored = new HashSet<State>(); // Set of explored nodes
    private Stack<Node> open = new Stack<Node>();  // LIFO Queue (Stack)
    private int limit;

    @Override
    public void setParams(String[] params) {
        limit = Integer.valueOf(params[0]);
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
