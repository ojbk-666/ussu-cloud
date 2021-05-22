package cn.ussu.common.core.base;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.ussu.common.core.constants.StrConstants;
import cn.ussu.common.core.exception.RequestEmptyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 控制器 基类
 *
 * @author liming
 * @date 2020-01-03 15:32
 */
public class BaseController {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    // @Autowired
    // protected UssuProperties ussuProperties;
    @Autowired
    public HttpServletRequest request;
    @Autowired
    public HttpServletResponse response;
    @Autowired
    public ServletContext servletContext;

    /**
     * 检查分页查询参数
     *
     * @return
     */
    protected boolean checkPageParam() {
        //每页多少条数据
        try {
            int limit = Integer.valueOf(request.getParameter("limit"));
            if (limit <= 0) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        //第几页
        try {
            int page = Integer.valueOf(request.getParameter("page"));
            if (page < 0) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * new HashMap<String, Object>()
     * @return
     */
    protected Map<String, Object> getNewHashMap() {
        return new HashMap<String, Object>();
    }

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
     * @param checkAndThrowException 是否检查ids并抛出{@link RequestEmptyException}
     * @return
     */
    protected List<String> splitCommaList(String ids, boolean checkAndThrowException) {
        if (checkAndThrowException && StrUtil.isBlank(ids)) throw new RequestEmptyException();
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
     * 检查指定参数是否为空或空字符串，是则抛出异常
     *
     * @param o 待检测参数
     */
    protected void checkReqParamThrowException(Object o){
        if (o == null) throw new RequestEmptyException();
        if (o instanceof String && StrUtil.isBlank(((String) o))) throw new RequestEmptyException();
    }

    /**
     * @desc 获取文件列表,注意：这个方法不能使用自动注入的request对象
     * @date 2014-5-23 下午04:28:20
     * @author xubin
     */
    public List<MultipartFile> getFiles(HttpServletRequest request, String fieldName) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        List<MultipartFile> list = multipartRequest.getFiles(fieldName);
        return list;
    }

    /**
     * @param mFile
     * @return 返回格式如: jpg(不包含.)
     * @Description 获取文件扩展名
     * @date 2014-5-23 下午04:53:51
     * @author xubin
     */
    public String getFileExt(MultipartFile mFile) {
        String fileName = mFile.getOriginalFilename();
        String ext = "";
        int i = fileName.lastIndexOf(".");
        if (i != -1) {
            ext = fileName.substring(i).replace(".", "");
        }
        return ext;
    }

    /**
     * @param ext
     * @return
     * @desc 根据时间戳获取一个新的文件名字(兼容没有后缀的情况)
     * @author xubin
     * @date 2019年3月25日 下午5:39:12
     */
    public String getFileNewName(String ext) {

        String newName = DateUtil.date().getTime()+"";
        if (ext != null && !"".equals(ext)) {
            newName += "." + ext;
        }
        return newName;
    }

    /**
     * 生成文件缩略图
     *
     * @param filePath 文件路径
     */
    protected void generatorImgThumbnail(String filePath) {
        if (filePath.lastIndexOf(".") == -1) return;
        // 判断是否是图片和图片类型
        String type = FileUtil.getType(new File(filePath));
        String extName = FileUtil.extName(filePath);
        if (type == null) type = extName;
        if ("jpg".equals(type) || "png".equals(type) || "gif".equals(type) || "bmp".equals(type)) {
            String targetFilePath = filePath.substring(0, filePath.lastIndexOf(".")) + "_scale." + extName;
            // 计算生成的缩略图图片比例
            BufferedImage read = ImgUtil.read(filePath);
            int height = read.getHeight();
            int width = read.getWidth();
            int targetHeight = 90;
            int targetWidth = width * targetHeight / height;
            // 图片太小则跳过压缩 直接拷贝
            if (height <= targetHeight) {
                FileUtil.copyFile(filePath, targetFilePath);
            } else {
                ImgUtil.scale(new File(filePath), new File(targetFilePath), targetWidth, targetHeight, null);
            }
        }
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
