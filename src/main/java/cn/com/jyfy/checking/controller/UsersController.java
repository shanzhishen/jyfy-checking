package cn.com.jyfy.checking.controller;

import cn.com.jyfy.checking.mapper.UsersMapper;
import cn.com.jyfy.checking.utils.CommonValue;
import cn.com.jyfy.checking.utils.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Api("人员信息")
@RestController
@RequestMapping("user")
public class UsersController {

    @Autowired
    private UsersMapper usersMapper;


    @ApiOperation("获取工作人员的列表")
    @GetMapping("getList")
    public JsonObject getList(){
        List<Map<String,Object>> normalUser = usersMapper.getUserByRoleCode(CommonValue.ROLE_WORKER);
        return new JsonObject(normalUser);
    }


}
