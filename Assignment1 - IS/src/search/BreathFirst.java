/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search;

import java.util.ArrayList;
import search.*;

/**
 *
 * @author Alberto
 */
public class BreathFirst extends SearchAlgorithm{

    @Override
    public void setParams(String[] params) {
        /* NOTHING */
    }

    @Override
    public void doSearch() {
        ArrayList<Node> open = new ArrayList<Node>();
        Node initialState = new Node(super.problem.initialState());
        open.add(initialState);
        while(!open.isEmpty()){
            
        }
    }
    
}
