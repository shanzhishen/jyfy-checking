package cn.com.jyfy.checking;

import cn.com.jyfy.checking.controller.CalculateController;
import cn.com.jyfy.checking.controller.LoginController;
import cn.com.jyfy.checking.entity.*;
import cn.com.jyfy.checking.entity.model.WeekPaperTreeModel;
import cn.com.jyfy.checking.mapper.*;
import cn.com.jyfy.checking.service.CalculateService;
import cn.com.jyfy.checking.service.ProjectService;
import cn.com.jyfy.checking.service.UploadService;
import cn.com.jyfy.checking.utils.CommonUtil;
import cn.com.jyfy.checking.utils.CommonValue;
import cn.com.jyfy.checking.utils.StringUtil;
import cn.com.jyfy.checking.word.WordUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import freemarker.template.Template;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CheckingApplicationTests {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private MenuElementMapper menuElementMapper;

    @Test
    public void contextLoads() {
        System.out.println(roleMapper.getRolesByJnum("3190"));
    }


    @Test
    public void test1() {
        List<RoleDO> roleDOS = new ArrayList<>();
        RoleDO roleDO1 = new RoleDO();
        roleDO1.setRoleId(4);
        RoleDO roleDO2 = new RoleDO();
        roleDO2.setRoleId(13);
        roleDOS.add(roleDO1);
        roleDOS.add(roleDO2);

        System.out.println(menuElementMapper.getMenuElementByRole(roleDOS));

    }



}
