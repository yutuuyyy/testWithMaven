package com.test.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @program: testWithMaven
 * @description: 图
 * @author: gwb
 * @create: 2022-01-11 13:38
 **/
public class Graph {

    public HashMap<Integer, Node> nodes;
    public Set<Edge> edges;

    public Graph(){
        nodes = new HashMap<>();
        edges = new HashSet<>();
    }
}
