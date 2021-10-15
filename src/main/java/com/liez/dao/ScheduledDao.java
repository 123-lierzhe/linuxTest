package com.liez.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ScheduledDao {

    String getCronByMethodName(String methodName);
}
