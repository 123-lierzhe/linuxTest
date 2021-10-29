package com.liez.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
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

    void insertData(@Param("id") String id, @Param("isAlive") Boolean isAlive, @Param("createTime") Date createTime);

    List<Map<String, Object>> selectData();
}
