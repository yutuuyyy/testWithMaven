package com.test.entity;

/**
 * @program: testWithMaven
 * @description: 图-边
 * @author: gwb
 * @create: 2022-01-11 11:27
 **/
public class Edge {
    Node from;
    Node to;
    int weight;

    public Edge(Node from, Node to, int weight){
        this.from = from;
        this.to = to;
        this.weight = weight;
    }
}
