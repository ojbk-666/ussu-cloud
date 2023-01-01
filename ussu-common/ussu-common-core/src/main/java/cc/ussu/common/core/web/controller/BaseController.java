package cc.ussu.common.core.web.controller;

import cc.ussu.common.core.constants.StrConstants;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 控制器 基类
 *
 * @author liming
 * @date 2020-01-03 15:32
 */
public class BaseController {

    @Autowired
    public HttpServletRequest request;
    @Autowired
    public HttpServletResponse response;
    @Autowired
    public ServletContext servletContext;

    protected static final String SELECT = "select";
    protected static final String ADD = "add";
    protected static final String EDIT = "edit";
    protected static final String DELETE = "delete";
    protected static final String EXPORT = "export";
    protected static final String IMPORT = "import";

    /**
     * 字符串是否合法
     *
     * @param str
     * @return
     */
    protected boolean isStrValid(String str) {
        return StrUtil.isNotBlank(str) && !"undefined".equals(str);
    }

    protected boolean isStrInvalid(String str) {
        return !isStrValid(str);
    }

    /**
     * 将字符串以逗号分隔处理为list
     *
     * @param ids                    字符串
     * @return
     */
    protected List<String> splitCommaList(String ids) {
        String[] split = ids.split(StrConstants.COMMA);
        List<String> list = Arrays.stream(split).filter(s -> StrUtil.isNotBlank(s)).collect(Collectors.toList());
        return list;
    }

    /**
     * 获取请求参数String
     */
    protected String getReqParam(String key) {
        return request.getParameter(key);
    }

    /**
     * @desc 获取文件列表,注意：这个方法不能使用自动注入的request对象
     */
    public List<MultipartFile> getFiles(HttpServletRequest request, String fieldName) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        List<MultipartFile> list = multipartRequest.getFiles(fieldName);
        return list;
    }

    /**
     * 根据时间戳获取一个新的文件名字(兼容没有后缀的情况)
     */
    public String getFileNewName(String ext) {

        String newName = DateUtil.date().getTime()+"";
        if (ext != null && !"".equals(ext)) {
            newName += "." + ext;
        }
        return newName;
    }

    /**
     * 下载文件
     */
    protected ResponseEntity renderFile(File file) {
        return renderFile(file, null);
    }

    /**
     * 返回前台文件流
     *
     * @param file 文件
     * @param newName 新文件名
     * @return
     * @throws Exception
     */
    protected ResponseEntity renderFile(File file, String newName) {
        if (!file.exists()) {
            return null;
        }
        //处理中文编码
        String downloadFileName = file.getName();
        String extName = FileUtil.extName(file);
        int i = downloadFileName.lastIndexOf(".");
        if (i!=-1) {
            downloadFileName = downloadFileName.substring(0, i);
        }
        if (StrUtil.isNotBlank(newName)) {
            downloadFileName = newName;
        } else {
            downloadFileName = getFileNewName(extName);
        }
        downloadFileName += '.' + extName;
        String newFileName = new String(downloadFileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        //设置头信息
        HttpHeaders headers = new HttpHeaders();
        //设置下载的附件 (newFileName必须处理中文名称)
        headers.setContentDispositionFormData("attachment", newFileName);
        //设置MIME类型
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        //HttpStatus.CREATED是HTTP的状态码201
        return new ResponseEntity<byte[]>(FileUtil.readBytes(file), headers, HttpStatus.CREATED);
    }

    /**
     * 返回前台文件按流
     *
     * @param filePath 文件全路径
     * @param downloadFileName 下载时的文件名称
     * @return
     */
    protected ResponseEntity renderFile(String filePath, String downloadFileName) {
        return renderFile(new File(filePath), downloadFileName);
    }

    /**
     * 返回前台文件流
     *
     * @param filePath 文件全路径
     * @return
     * @throws Exception
     */
    protected ResponseEntity renderFile(String filePath) {
        return renderFile(new File(filePath));
    }

}
