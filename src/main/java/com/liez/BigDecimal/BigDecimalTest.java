package com.liez.BigDecimal;

import java.math.BigDecimal;

/**
 * @author liez
 * @date 2022/7/12
 */
public class BigDecimalTest {
    public static void main(String[] args) {
        //使用BigDecimal原因
        System.out.println(0.2 + 0.1);
        System.out.println(0.3 - 0.1);
        System.out.println(0.2 * 0.1);
        System.out.println(0.3 / 0.1);
        System.out.println("-------------------------------------------------------");

        //    BigDecimal(int)       创建一个具有参数所指定整数值的对象。
        //
        //BigDecimal(double) 创建一个具有参数所指定双精度值的对象。 //不推荐使用
        //
        //BigDecimal(long)    创建一个具有参数所指定长整数值的对象。
        //
        //BigDecimal(String) 创建一个具有参数所指定以字符串表示的数值的对象。//推荐使用

        //    add(BigDecimal)        BigDecimal对象中的值相加，然后返回这个对象。
        //
        //subtract(BigDecimal) BigDecimal对象中的值相减，然后返回这个对象。
        //
        //multiply(BigDecimal)  BigDecimal对象中的值相乘，然后返回这个对象。
        //
        //divide(BigDecimal)     BigDecimal对象中的值相除，然后返回这个对象。
        //
        //toString()                将BigDecimal对象的数值转换成字符串。
        //
        //doubleValue()          将BigDecimal对象中的值以双精度数返回。
        //
        //floatValue()             将BigDecimal对象中的值以单精度数返回。
        //
        //longValue()             将BigDecimal对象中的值以长整数返回。
        //
        //intValue()               将BigDecimal对象中的值以整数返回。

        //为什么BigDecimal(double)  不推荐使用，
        BigDecimal intStr = new BigDecimal("222");
        BigDecimal doubleStr = new BigDecimal(1.11);
        System.out.println(intStr);
        System.out.println(doubleStr);
        System.out.println("-------------------------------------------------------");

        //当double必须用作BigDecimal的源时，请使用Double.toString(double)转成String，然后使用String构造方法，或使用BigDecimal的静态方法valueOf，如下
        BigDecimal intString = BigDecimal.valueOf(1.11);
        BigDecimal doubleString = new BigDecimal(Double.toString(1.111));
        System.out.println(intString);
        System.out.println(doubleString);
        System.out.println("-------------------------------------------------------");

        //加减乘除
        BigDecimal first = new BigDecimal("36");
        BigDecimal second = new BigDecimal("12");
        System.out.println("加法"+first.add(second));
        System.out.println("减法"+first.subtract(second));
        System.out.println("乘法"+first.multiply(second));
        System.out.println("除法"+first.divide(second));
        System.out.println("-------------------------------------------------------");


        double a = 1.233;
        float b = 3.3f;
        BigDecimal firsts = new BigDecimal(Double.toString(a));
        BigDecimal seconds = new BigDecimal(Float.toString(b));
        System.out.println(firsts.multiply(seconds));
        System.out.println(a*b);

    //    保留后两位
        System.out.println("保留后两位"+firsts.divide(seconds,2,BigDecimal.ROUND_UP));

    }
}
