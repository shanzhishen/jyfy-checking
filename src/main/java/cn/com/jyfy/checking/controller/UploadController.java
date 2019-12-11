package cn.com.jyfy.checking.controller;

import cn.com.jyfy.checking.entity.UsersDO;
import cn.com.jyfy.checking.service.UploadService;
import cn.com.jyfy.checking.utils.JsonObject;
import cn.com.jyfy.checking.utils.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api("上传数据")
@RestController
@RequestMapping("upload")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ApiOperation("上传排班数据")
    @PostMapping("classInfo")
    public JsonObject getClassInfo(@RequestParam("excel")MultipartFile file, HttpServletRequest request){
        if(file == null){
            return new JsonObject("上传失败");
        }
        printWriter(request,file.getOriginalFilename());
        List<List<String>> data = uploadService.readExcel(file);
        if (data == null){
            return new JsonObject("读取失败");
        }
        uploadService.handlerClassInfo(data);
        return new JsonObject();
    }

    @ApiOperation("上传指纹数据")
    @PostMapping("fingerPrint")
    public JsonObject getFingerPrint(@RequestParam("excel")MultipartFile file,@RequestParam("start") String start, HttpServletRequest request){
        if(file == null){
            return new JsonObject("上传失败");
        }
        if (StringUtil.isNull(start)){
            return new JsonObject("未传月份");
        }
        printWriter(request,file.getOriginalFilename());
        List<List<String>> data = uploadService.readExcel(file);
        if (data == null){
            return new JsonObject("读取失败");
        }

        return uploadService.handlerFingerPrint(data,start);

    }

    @ApiOperation("上传替班数据")
    @PostMapping("changeClass")
    public JsonObject changeClass(@RequestParam("excel")MultipartFile file, HttpServletRequest request){
        if(file == null){
            return new JsonObject("上传失败");
        }
        printWriter(request,file.getOriginalFilename());
        List<List<String>> data = uploadService.readExcel(file);

        if (data == null){
            return new JsonObject("读取失败");
        }

        return uploadService.handerChangeClass(data);
    }


    private void printWriter(HttpServletRequest request,String s){
        UsersDO usersDO = LoginController.getUser(request);
        logger.info(usersDO.getUserName() + "上传了数据： " + s);
    }
}
