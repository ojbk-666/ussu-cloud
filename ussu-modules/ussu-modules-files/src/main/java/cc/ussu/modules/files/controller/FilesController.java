package cc.ussu.modules.files.controller;

import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.modules.files.properties.LocalUploadProperties;
import cc.ussu.modules.files.service.FileService;
import cc.ussu.system.api.vo.FileVO;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class FilesController extends BaseController {

    @Autowired
    private FileService fileService;
    @Autowired
    private LocalUploadProperties localUploadProperties;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FileVO upload(@RequestParam("file") MultipartFile multipartFile
        , @RequestParam(value = "directory", required = false) String directory) {
        try {
            if (multipartFile.isEmpty()) {
                throw new IllegalArgumentException("文件为空");
            }
            return fileService.uploadFile(multipartFile, directory);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 列出文件列表
     */
    @GetMapping("/list")
    public JsonResult<List<FileVO>> list(String directory, String sort, String order) {
        List<FileVO> fileVOS = fileService.listFile(directory, sort, order);
        return JsonResult.ok(fileVOS);
    }

    /**
     * 创建新文件夹
     */
    // @PermCheck("other:files:select")
    @PutMapping("/createFolder")
    public JsonResult createNewFolder(@RequestBody Map map) {
        String directory = MapUtil.getStr(map, "directory");
        directory = StrUtil.replace(directory, "..", StrUtil.EMPTY);
        // directory = StrUtil.removePrefix(directory, StrUtil.SLASH);
        if (StrUtil.isNotBlank(directory)) {
            directory = StrUtil.addSuffixIfNot(directory, StrUtil.SLASH);
            directory = StrUtil.addPrefixIfNot(directory, StrUtil.SLASH);   //  /abc/def/
        }
        String name = MapUtil.getStr(map, "name");
        Assert.notBlank(name);
        String absolutePath = localUploadProperties.getLocalFilePathWithoutSuffixSlash() + directory + name;
        FileUtil.mkdir(absolutePath);
        return JsonResult.ok();
    }

    /**
     * 删除文件/夹
     */
    @DeleteMapping("/delete")
    public JsonResult deleteFolderAndFile(@RequestBody Map map) {
        String directory = MapUtil.getStr(map, "directory");
        List<String> nameList = MapUtil.get(map, "nameArr", List.class);
        directory = StrUtil.replace(directory, "..", StrUtil.EMPTY);
        if (StrUtil.isNotBlank(directory)) {
            directory = StrUtil.addSuffixIfNot(directory, StrUtil.SLASH);
            directory = StrUtil.addPrefixIfNot(directory, StrUtil.SLASH);   //  /abc/def/
        }
        String relativePath = StrUtil.addSuffixIfNot(localUploadProperties.getLocalFilePathWithoutSuffixSlash() + directory, StrUtil.SLASH);
        if (CollUtil.isNotEmpty(nameList)) {
            for (String s : nameList) {
                if (StrUtil.isNotBlank(s)) {
                    FileUtil.del(relativePath + s);
                }
            }
        }
        return JsonResult.ok();
    }

    /**
     * 富文本编辑器文件上传
     */
    @PostMapping("/upload-tinymce")
    public JsonResult upload(@RequestPart("file") MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return JsonResult.error("文件不能为空");
        }
        String[] allow = new String[]{
            "jpg", "jpeg", "png", "gif", "bmp",
            "tiff", "ico", "mp3", "mp4", "avi",
            "rmv", "rmvb", "flv"
        };
        // 判断文件类型
        String mimeType = FileUtil.extName(multipartFile.getOriginalFilename());
        if (StrUtil.containsAnyIgnoreCase(mimeType, allow)) {
            String parentPath = "tinymce/" + DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN);
            return JsonResult.ok(fileService.uploadFile(multipartFile, parentPath));
        } else {
            return JsonResult.error("所选文件格式不支持");
        }
    }

}
