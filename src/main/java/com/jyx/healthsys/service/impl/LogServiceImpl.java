package com.jyx.healthsys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jyx.config.JwtConfig;
import com.jyx.healthsys.entity.Body;
import com.jyx.healthsys.entity.Menu;
import com.jyx.healthsys.entity.Log;

import com.jyx.healthsys.entity.Log;
import com.jyx.healthsys.mapper.LogMapper;

import com.jyx.healthsys.mapper.LogMapper;
import com.jyx.healthsys.service.IBodyService;
import com.jyx.healthsys.service.IMenuService;

import com.jyx.healthsys.service.ILogService;
import com.jyx.healthsys.service.ILogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 金义雄
 * @since 2023-02-23
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements ILogService {

    @Autowired
    private RedisTemplate redisTemplate;



    @Autowired
    private IMenuService menuService;





    @Autowired
    private JwtConfig jwtConfig;


    @Autowired
    private IBodyService bodyMapper;





    @Override
    public boolean addLog(Log log) {

            // 写入日志表
            this.baseMapper.insert(log);



        return true;

    }

    @Override
    public Log getLogById(Integer id) {

        // 根据用户ID查询用户信息
        Log log = this.baseMapper.selectById(id);
        System.out.println(log);

        return log;
    }






    @Override
    public void deletLogById(Integer id) {
        // 通过用户ID删除用户
        this.baseMapper.deleteById(id);

    }



}