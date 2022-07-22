package com.liez.策略模式.初版;

/**
 * @author liez
 * @date 2022/7/22
 */
public class Test {

    public static void main(String[] args) {
        String type = "B";
        Startagy startagy = StartagyFactory.getStartagy(type);
        startagy.alg();
    }
}
