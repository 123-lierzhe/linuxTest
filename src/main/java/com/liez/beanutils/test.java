package com.liez.beanutils;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * liez
 *
 * @date 2022/4/28
 */
public class test {

    public static void main(String[] args) {
        A a = new A();
        a.setId(1);
        a.setNames(Arrays.asList(1,2,3));

        B b = new B();

        BeanUtils.copyProperties(a,b);
        System.out.println(b);
        for (String name : b.getNames()) {
            System.out.println(name);
        }
    }
}
