package cn.com.jyfy.checking.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @Created by Miracle
 * @Date 2019/10/29 17:24
 */
public class FileUtil {

    private final static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static void downLoadFile(String fullPath, HttpServletResponse response) {
        logger.info("【downLoadFile:fullPath】：==>" + fullPath);

        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            //创建文件
            File file = new File(fullPath);
            String fileName = file.getName();

        //读文件流
        inputStream = new BufferedInputStream(new FileInputStream(file));
        byte[] buffer = new byte[inputStream.available()];
        inputStream.read(buffer);

        //清空响应
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/octet-stream; charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));
        response.setHeader("Content-Length", "" + file.length());
        //写文件流
        outputStream = new BufferedOutputStream(response.getOutputStream());
        outputStream.write(buffer);
        outputStream.flush();
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}


    public static void deleteFiles(File f){
        if (!f.exists()){
            return;
        }
        //首先判断f是文件还是文件夹
        if(f.isDirectory()){
            //创建文件数组对象
            File[] files = f.listFiles();
            //开始遍历
            for (File key : files) {
                //如果是文件，就直接删除
                if(key.isFile()){
                    key.delete();
                }else{//如果key是文件夹，就递归
                    deleteFiles(key);//
                }
            }
        }else {
            //最后删除这个文件夹就可以了
            f.delete();
        }
    }


}
