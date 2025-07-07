package com.jyx.healthsys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jyx.healthsys.entity.Log;

import java.util.Map;

public interface ILogService  extends IService<Log> {


    boolean addLog(Log log);

    Log getLogById(Integer id);


    void deletLogById(Integer id);


}
