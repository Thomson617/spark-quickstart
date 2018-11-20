package com.sun.dateStructure.linkedList;

/**
 * @Title   有序链表
对于某些应用来说，在链表中保持数据有序是很有用的，具有这个特性的链表叫作“有序链表”.
通常，有序链表的删除只限于最大值或最小值，不过，有时候也会查找和删除某一特定点，
但这种操作对于有序链表来说效率不高.
有序链表优于有序数组的地方在于插入的效率更高（不需要像数组那样移动元素），
另外链表可以灵活地扩展大小，而数组的大小是固定的。但是这种效率的提高和灵活的优势是以算法的复杂为代价的
---------------------
 * @author SunTao
 * @date 2018/10/25 16:17
 */

public class SortedList {

    private Link first;  //指向链表中的第一个链结点

    public SortedList(){
        first = null;
    }

    //插入
    public void insert(Link link){
        Link previous = null;
        Link cur = first;
        while(cur != null &&link.age>cur.age){  //链表由大到小排列
            previous = cur;
            cur = cur.next;
        }

        if(previous == null){  //如果previous为null，则证明当前链结点为表头
            this.first = link;
        }else{
            previous.next = link;
        }

        link.next = cur;

    }

    //删除第一个链结点，返回删除的链结点引用
    public Link deleteFirst() throws Exception{
        if(isEmpty()){
            throw new Exception("链表为空！不能进行删除操作");
        }
        Link temp = first;
        first = first.next;
        return temp;
    }

    //打印出所有的链表元素
    public void displayList(){
        Link cur = first;
        while(cur != null){  //循环打印每个链结点
            cur.displayLink();
            cur = cur.next;
        }
    }

    //判空
    public boolean isEmpty(){
        return (first == null);
    }

}
