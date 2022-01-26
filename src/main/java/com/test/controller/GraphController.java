package com.test.controller;

import com.test.entity.Graph;
import com.test.entity.Node;

/**
 * @program: testWithMaven
 * @description: 图
 * @author: gwb
 * @create: 2022-01-11 13:38
 **/
public class GraphController {

    public static Graph createGraph(int[][] matrix){
        Graph graph = new Graph();

        for (int[] arr : matrix) {
            int from = arr[0];
            int to = arr[1];
            int value = arr[2];

            Node fromNode = new Node(from);
            Node toNode = new Node(to);

            if (!graph.nodes.containsKey(from)){
                graph.nodes.put(from, fromNode);
            }

            if (!graph.nodes.containsKey(to)){
                graph.nodes.put(to, toNode);
            }
        }
        return graph;
    }
}
