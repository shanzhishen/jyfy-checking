package cn.com.jyfy.checking.controller;

import cn.com.jyfy.checking.entity.UsersDO;
import cn.com.jyfy.checking.mapper.UsersMapper;
import cn.com.jyfy.checking.service.LoginService;
import cn.com.jyfy.checking.service.UploadService;
import cn.com.jyfy.checking.utils.JsonObject;
import cn.com.jyfy.checking.utils.LoginException;
import cn.com.jyfy.checking.utils.Md5Util;
import cn.com.jyfy.checking.utils.StringUtil;
import cn.com.jyfy.checking.utils.annotation.RequiresRoles;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * @author Miracle
 * @since 2019-4-24
 */
@Api(value = "登陆相关")
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private UsersMapper usersMapper;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ApiOperation("登陆")
    @PostMapping("login")
    public JsonObject login(String jnum, String pwd, HttpServletRequest request) {
        if (StringUtil.isNull(jnum, pwd)) {
            return new JsonObject("用户名或者密码错误");
        }
        if(jnum.startsWith("0") && jnum.length() == 4){
            jnum = jnum.substring(1);
        }

        JsonObject obj = loginService.login(jnum, pwd, request);
        return obj;
    }

    @ApiOperation("登出")
    @PostMapping("logout")
    public JsonObject logout(HttpServletRequest request) {
        UsersDO usersDO = LoginController.getUser(request);

        HttpSession session = request.getSession();
        session.setAttribute("user",null);
        logger.info( "登出系统：" + usersDO);
        return new JsonObject();
    }


//    @RequestMapping("regImage")
    public void regImage() {
    }

    @ApiOperation("获取menu")
    @GetMapping("getMenu")
    public JsonObject getMenu(HttpServletRequest request, @RequestHeader("server-code") Integer serverCode) {
        if(serverCode == null){
            return new JsonObject("参数错误");
        }
        UsersDO usersDO = getUser(request);
        return loginService.getMenu(usersDO,serverCode);
    }

    public static UsersDO getUser(HttpServletRequest request){
        HttpSession session = request.getSession();
        if(session.getAttribute("user") == null){
            throw new LoginException("请先登录系统");
        }
        return (UsersDO) session.getAttribute("user");
    }


    @ApiOperation("考核人员")
    @GetMapping("checkedUsers")
    public JsonObject getCheckedUser (){
        QueryWrapper<UsersDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("role_id",2);
        queryWrapper.orderByAsc("ord");
        List<UsersDO> usersDOList = usersMapper.selectList(queryWrapper);
        return new JsonObject(usersDOList);
    }


    @ApiOperation("修改密码")
    @PostMapping("changePwd")
    public JsonObject changePwd(HttpServletRequest req,String pwd,String newPwd){
        if(StringUtil.isNull(pwd,newPwd)){
            return new JsonObject("密码错误");
        }
        UsersDO usersDO = LoginController.getUser(req);
        if(!StringUtil.regBoolean(newPwd,StringUtil.PWD_REG)){
            return new JsonObject("密码中必须包含6位字母、数字、特殊字符");
        }

        String oldPwd = Md5Util.md5Password(pwd);
        String newPwd1 = Md5Util.md5Password(newPwd);
        if(usersDO.getPassword().equals(oldPwd)){
            usersDO.setPassword(newPwd1);
        }else {
            return new JsonObject("原密码不正确");
        }

        UpdateWrapper<UsersDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("jnum",usersDO.getJnum());
        usersMapper.update(usersDO,updateWrapper);
        return new JsonObject();
    }

    @GetMapping("test")
    @RequiresRoles("worker")
    public JsonObject test(){
        System.out.println("test");
        return new JsonObject(JsonObject.SUCCESS,"test");
    }

}
