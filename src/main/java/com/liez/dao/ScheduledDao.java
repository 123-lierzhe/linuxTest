package com.liez.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ScheduledDao {

    int getJobHistoryRows(@Param("rowsId") String rowsId);

    int getJobCurrentRows();

    List<String> getBetterRowsDetailId(@Param("betterRows") int betterRows);

    List<Map<String, Object>> getDetailById(List<String> betterRowsDetailId);

    void updateRows(int currentRows);

    List<Map<String, Object>> getAllUseingScheduled();
}
