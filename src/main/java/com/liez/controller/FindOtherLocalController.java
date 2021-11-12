package com.liez.controller;

import com.alibaba.fastjson.JSONObject;
import com.liez.service.FindOtherLocalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("system")
public class FindOtherLocalController {

    @Autowired
    private FindOtherLocalService findOtherLocalService;

    @PostMapping("getOtherLocal")
    public void getOtherLocal(@RequestBody JSONObject params) throws Exception {
        findOtherLocalService.getOtherLocal(params.getString("path"));
    }
}
