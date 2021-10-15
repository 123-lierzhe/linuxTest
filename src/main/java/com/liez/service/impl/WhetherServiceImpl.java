package com.liez.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.liez.service.WhetherService;
import com.liez.utils.HttpClientUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class WhetherServiceImpl implements WhetherService {


    @Value("${baidu.map.ak}")
    private String baiduMapAk;


    @Override
    public String getWhether(String localName) {
        Map<String, Object> locationMap = getCurrentLocal(localName);
        //纬度值
        String lat = String.format("%.4f", locationMap.get("lat"));
        //经度值
        String lng = String.format("%.4f", locationMap.get("lng"));
        String latAndLng = lat + ":" + lng;
//        String getWhetherUrl = "https://api.seniverse.com/v3/pro/weather/grid/moment.json?key=" + "SPcTiO9rW3BpDQh-X" + "&location=" + latAndLng + "&language=zh-Hans&unit=c&advanced=2.1";
        String getWhetherUrl = "https://api.seniverse.com/v3/pro/weather/grid/moment.json?key="+"SPcTiO9rW3BpDQh-X"+"&location=29.6024:106.6572&language=zh-Hans&unit=c&advanced=2.1";
        String s = HttpClientUtils.doGet(getWhetherUrl, null);
        System.out.println(s);
        return s;
    }

    public Map<String, Object> getCurrentLocal(String localName) {
        String getCurrentLocalUrl = "https://api.map.baidu.com/geocoding/v3/?address=" + localName + "&output=json&ak=" + baiduMapAk + "&callback=";
        String localIPDesc = HttpClientUtils.doGet(getCurrentLocalUrl, null);
        JSONObject localIpJson = JSONObject.parseObject(String.valueOf(localIPDesc));
        //lat-纬度值，lng-经度值普华和诚(北京)信息有限公司
        Map<String, Object> rusultMap = (Map<String, Object>) localIpJson.get("result");
        Map<String, Object> locationMap = (Map<String, Object>) rusultMap.get("location");
        return locationMap;
    }
}
