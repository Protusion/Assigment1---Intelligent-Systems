/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search;

import java.util.ArrayList;
import java.util.PriorityQueue;
import search.*;

/**
 *
 * @author Alberto
 */
public class BreathFirst extends SearchAlgorithm {

    protected ArrayList<State> explored = new ArrayList();
    protected PriorityQueue open = new PriorityQueue(100, Node.BY_DEPTH);

    @Override
    public void setParams(String[] params) {
        Node initialNode = new Node(super.problem.initialState());
        open.add(initialNode);
    }

    @Override
    public void doSearch() {
        //super.generatedNodes = 1;
        while (!open.isEmpty()) {
            Node extractedNode = (Node) open.remove();
            //super.generatedNodes++;
            if (!explored.contains(extractedNode.getState())) {
                if (problem.testGoal(extractedNode.getState())) {
                    super.actionSequence.add(extractedNode.getAction());
                    Node parent = extractedNode.getParent();

                    for (State state : explored) {

                        if (state.equals(parent.getState()) && !super.isInitialNode(parent)) {
                            super.totalCost += problem.cost(state, parent.getAction());
                            super.actionSequence.add(parent.getAction());
                            parent = parent.getParent();
                            //super.exploredMaxSize--;
                        }
                        explored.remove(state);
                    }
                    super.solutionFound = true;
                } else {
                    explored.add(extractedNode.getState());
                    super.exploredMaxSize++;
                    open.add(super.getSuccessors(extractedNode));
                    super.expandedNodes++;
                }
            }
        }
    }
}
