package cn.com.jyfy.checking.service;

import cn.com.jyfy.checking.utils.JsonObject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UploadService {

    /**
     *  读取excel数据
     */
    List<List<String>> readExcel(MultipartFile file);

    /**
     * 处理排班数据
     */
    JsonObject handlerClassInfo(List<List<String>> data);

    /**
     * 处理指纹数据
     * @param data
     * @param start
     * @return
     */
    JsonObject handlerFingerPrint(List<List<String>> data, String start);


    JsonObject handerChangeClass(List<List<String>> data);
}
