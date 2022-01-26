package com.test.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @program: testWithMaven
 * @description:
 * @author: gwb
 * @create: 2021-12-30 09:33
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    private Integer id;
    //@JsonIgnore
    private String serial;

    private List<Integer> jsonTest;

    private String getPrivateMethod(){
        System.out.println("私有方法被调用了");
        return "PrivateMethod";
    }
}
