package com.liez.策略模式.初版;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liez
 * @date 2022/7/22
 */
public class StartagyFactory {

    private static final Map<String,Startagy> startagyMap = new HashMap<>();

    static {
        startagyMap.put("A",new StartagyA());
        startagyMap.put("B",new StartagyB());
    }

    public static Startagy getStartagy(String type){
        return startagyMap.get(type);
    }

}
