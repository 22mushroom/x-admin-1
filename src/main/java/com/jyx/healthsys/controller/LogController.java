package com.jyx.healthsys.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jyx.Data_unification.Unification;
import com.jyx.healthsys.entity.Body;
import com.jyx.healthsys.entity.BodyNotes;
import com.jyx.healthsys.entity.SportInfo;
import com.jyx.healthsys.entity.User;
import com.jyx.healthsys.entity.Log;
import com.jyx.healthsys.service.IBodyNotesService;
import com.jyx.healthsys.service.IBodyService;
import com.jyx.healthsys.service.IUserService;
import com.jyx.healthsys.service.ILogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 金义雄
 * @since 2023-02-23
 */
//声明此类是一个RestController，即RESTful风格的控制器，控制用户相关的请求。
//是一种设计风格，通过URI来定位资源，并使用HTTP协议中的请求方式（GET、POST、PUT、DELETE等）对资源进行操作
@RestController
@RequestMapping("/log")
public class LogController {
    @Autowired
    private IUserService userService;

    @Autowired
    private IBodyService bodyService;

    @Autowired
    private IBodyNotesService bodyNotesService;

    @Autowired
    private ILogService logService;
    /**
     * 获取所有用户
     * @return 返回用户列表
     */
    @GetMapping("/all")
    public Unification<List<Log>> getAllLog(){
        List<Log> list = logService.list();
        return Unification.success(list,"查询成功");
    }




    /**

     根据查询条件获取用户列表，分页查询

     @param username 查询条件：用户名，可选



     @param pageNo 当前页码

     @param pageSize 页面大小

     @return 返回Unification包装后的用户列表，包含总数和当前页码的用户信息列表
     */
    @GetMapping("/list")
    public Unification<Map<String,Object>> getLogList(@RequestParam(value = "username", required = false) String username,
                                                       @RequestParam(value = "date", required = false) String date,
                                                       @RequestParam("pageNo") Long pageNo,
                                                       @RequestParam("pageSize") Long pageSize) {

        LambdaQueryWrapper<Log> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(StringUtils.hasLength(username), Log::getUsername, username);
        wrapper.eq(StringUtils.hasLength(date), Log::getDate, date);
        Page<Log> page = new Page<>(pageNo, pageSize);

       logService.page(page, wrapper);
        Map<String, Object> data = new HashMap<>();
        data.put("total", page.getTotal()); // 用户总数
        data.put("rows", page.getRecords()); // 用户列表
        return Unification.success(data);
    }

/*
    @PostMapping("/add")
    public Unification<?> addUser(@RequestBody User user){
        boolean result = userService.addUser(user);
        if (result) {
            return Unification.success("新增成功");
        } else {
            return Unification.fail("用户名已存在");
        }
    }
   */

    @DeleteMapping("/{id}")
    public Unification<Log> deleteLogById(@PathVariable("id") Integer id){
        logService.deletLogById(id);
        return  Unification.success("删除成功");
    }


}