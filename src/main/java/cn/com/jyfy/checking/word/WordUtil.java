package cn.com.jyfy.checking.word;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.*;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

/**
 * @Created by Miracle
 * @Date 2019/10/29 8:05
 */
@Component
public class WordUtil {
    public static Logger logger = LoggerFactory.getLogger(WordUtil.class);

    @Value("${checking.weekpaper.template.path}")
    public String TEMPALTE_PATH;

    /**
     * 生成word文件(全局可用)
     *
     * @param data         word中需要展示的动态数据，用map/pojo来保存
     * @param templateName word模板名称，例如：test.ftl
     * @param fileFullPath 要生成的文件全路径
     */
    public void createWord(Map<?, ?> data, String templateName, String fileFullPath) {
        logger.info("【createWord】：==>方法进入");
        logger.info("【fileFullPath】：==>" + fileFullPath);
        logger.info("【templateName】：==>" + templateName);
        // 创建配置实例
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_29);
        // 配置兼容java8时间类
        configuration.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_29) {
            @Override
            public TemplateModel wrap(Object obj) throws TemplateModelException {
                if (obj instanceof LocalDate) {
                    return new SimpleDate(Date.valueOf((LocalDate) obj));
                }
                if (obj instanceof LocalTime) {
                    return new SimpleDate(Time.valueOf((LocalTime) obj));
                }
                if (obj instanceof LocalDateTime) {
                    return new SimpleDate(Timestamp.valueOf((LocalDateTime) obj));
                }
                return super.wrap(obj);
            }
        });
        configuration.setDefaultEncoding("UTF-8");
        // 设置处理空值
        configuration.setClassicCompatible(true);

        TemplateLoader templateLoader;
        Writer out = null;
        try {
            templateLoader = new FileTemplateLoader(new File(TEMPALTE_PATH));
            // 设置ftl模板文件加载方式
            configuration.setTemplateLoader(templateLoader);
            File file = new File(fileFullPath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
            Template template = configuration.getTemplate(templateName);
            template.process(data, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 生成的zip文件带子文件夹(全局可用)
     *
     * @param zipfullPath  压缩后的zip文件全路径
     * @param fileFullPath 压缩前的文件全路径
     * @param isKeepDirStr 是否保留原来的目录结构,true:保留目录结构; false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     */
    public static void createZip(String zipfullPath, String fileFullPath, boolean isKeepDirStr) {
        InputStream inputStream = null;
        ZipArchiveOutputStream zip = null;
        try {
            zip = new ZipArchiveOutputStream(new FileOutputStream(zipfullPath));
            zip.setEncoding("gbk");
            File file = new File(fileFullPath);
            compressZip(inputStream, zip, file, file.getName(), isKeepDirStr);//递归压缩
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (zip != null) {
                    zip.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 递归压缩方法(仅限于此类中用于压缩zip文件)
     *
     * @param inputStream  输入流
     * @param zip          zip输出流
     * @param sourceFile   源文件
     * @param fileName     文件夹名或文件名
     * @param isKeepDirStr 是否保留原来的目录结构,true:保留目录结构; false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws Exception
     */
    private static void compressZip(InputStream inputStream, ZipArchiveOutputStream zip, File sourceFile, String fileName, boolean isKeepDirStr) throws Exception {
        logger.info("【compressZip:sourceFile】：==>" + sourceFile.getPath());
        logger.info("【compressZip:fileName】：==>" + fileName);
        if (sourceFile.isFile()) {
            //读文件流
            inputStream = new BufferedInputStream(new FileInputStream(sourceFile));
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();

            //将读取的文件输出到zip中
            zip.putArchiveEntry(new ZipArchiveEntry(fileName));
            zip.write(buffer);
            zip.closeArchiveEntry();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if (isKeepDirStr) {
                    zip.putArchiveEntry(new ZipArchiveEntry(fileName + File.separator));//空文件夹的处理
                    zip.closeArchiveEntry();// 没有文件，不需要文件的copy
                }
            } else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构,注意：file.getName()前面需要带上父文件夹的名字加一斜杠,不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                    if (isKeepDirStr) {
                        compressZip(inputStream, zip, file, fileName + File.separator + file.getName(), isKeepDirStr);
                    } else {
                        compressZip(inputStream, zip, file, file.getName(), isKeepDirStr);
                    }
                }
            }
        }
    }
}
