package com.sun.dateStructure.binaryTree;

/**
 * @Title
 * @author SunTao
 * @date 2018/10/25 16:24
 */
//树节点的封装类
public class Node {
    int age;
    String name;
    Node leftChild;  //左子节点的引用
    Node rightChild; //右子节点的引用

    public Node(int age, String name) {
        this.age = age;
        this.name = name;
    }

    //打印该节点的信息
    public void displayNode() {
        System.out.println("name:" + name + ",age:" + age);
    }
}
