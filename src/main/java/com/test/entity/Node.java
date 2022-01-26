package com.test.entity;

import java.util.ArrayList;

/**
 * @program: testWithMaven
 * @description: 图-点
 * @author: gwb
 * @create: 2022-01-11 11:24
 **/
public class Node {

    public int value;
    public int in;
    public int out;
    public ArrayList<Node> nexts;
    public ArrayList<Edge> edges;

    public Node(int value){
        this.value = value;
        in = 0;
        out = 0;
        nexts = new ArrayList<>();
        edges = new ArrayList<>();
    }
}
