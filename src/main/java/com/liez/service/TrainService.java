package com.liez.service;

import java.util.List;
import java.util.Map;

public interface TrainService {
    Map<String,Object> getLastTrainCount();

    Map<String,Object> getHavintSet();

    void insertTrainCount(int size,String id);

    void insertTrainSetCount(int size, String id);

    void insertBath(List<Map<String, Object>> animalList);

    List<Map<String,Object>> selectBanch(List<String> idList);
}
