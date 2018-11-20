package com.sun.dateStructure.stack;

/**
 * @Title
 * @author SunTao
 * @date 2018/10/25 16:01
 */
public class BrecketChecker {

    private String input;  //存储待检查的字符串

    //构造方法，接受待检查的字符串
    public BrecketChecker(String in) {
        this.input = in;
    }

    //检查分隔符匹配的方法
    public void check() {
        int strLength = input.length();
        Stack stack = new Stack(strLength);
        for (int i = 0; i < strLength; i++) {
            char ch = input.charAt(i);  //一次获取串中的单个字符
            switch (ch) {
                case '{':
                case '[':
                case '(':
                    //如果为左分隔符，压入栈
                    stack.push(ch);
                    break;
                case '}':
                case ']':
                case ')':
                    //如果为右分隔符，与栈顶元素进行匹配
                    if (!stack.isEmpty()) {
                        char chx = (char) stack.pop();

                        if ((ch == '{' && chx != '}') || (ch == '(' && chx != ')') || (ch == '[' && chx != ']')) {
                            System.out.println("匹配出错！字符：" + ch + ",下标：" + i);
                        }
                    } else {
                        System.out.println("匹配出错！字符：" + ch + ",下标：" + i);
                    }
                default:
                    break;
            }
        }

        if (!stack.isEmpty()) {
            //匹配结束时如果栈中还有元素，证明右分隔符缺失
            System.out.println("有括号没有关闭！");
        }
    }

}
