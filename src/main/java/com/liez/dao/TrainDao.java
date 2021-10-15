package com.liez.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface TrainDao {
    Map<String,Object> getLastTrainCount();

    Map<String,Object> getHavintSet();

    void insertTrainCount(@Param("size") int size, @Param("id") String id);

    void insertTrainSetCount(int size, String id);

    void insertBath(List<Map<String, Object>> animalList);

    List<Map<String,Object>> selectBanch(List<String> idList);
}
