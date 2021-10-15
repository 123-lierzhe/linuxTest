package com.liez.service.impl;

import com.liez.dao.TrainDao;
import com.liez.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TrainServiceImpl implements TrainService {

    @Autowired
    private TrainDao trainDao;

    @Override
    public Map<String,Object> getLastTrainCount() {
        return trainDao.getLastTrainCount();
    }

    @Override
    public Map<String,Object> getHavintSet() {
        return trainDao.getHavintSet();
    }

    @Override
    public void insertTrainCount(int size,String id) {
        trainDao.insertTrainCount(size,id);
    }

    @Override
    public void insertTrainSetCount(int size,String id) {
        trainDao.insertTrainSetCount(size,id);
    }

    @Override
    public void insertBath(List<Map<String, Object>> animalList) {
        trainDao.insertBath(animalList);
    }

    @Override
    public List<Map<String,Object>> selectBanch(List<String> idList) {
        List<Map<String, Object>> maps = trainDao.selectBanch(idList);
        return maps;
    }
}
