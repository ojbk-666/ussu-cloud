package cn.ussu.modules.files.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import cn.ussu.common.core.base.BaseController;
import cn.ussu.common.core.model.vo.JsonResult;
import cn.ussu.common.core.model.vo.LocalFileVo;
import cn.ussu.modules.files.properties.LocalUploadProperties;
import cn.ussu.modules.files.service.SysFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.FileAlreadyExistsException;
import java.util.List;

@RestController
public class SysFileController extends BaseController {

    @Autowired
    private SysFileService sysFileService;
    @Autowired
    private LocalUploadProperties localUploadProperties;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public LocalFileVo upload(@RequestParam("file") MultipartFile multipartFile
            , @RequestParam(value = "path", required = false) String path) {
        try {
            if (multipartFile.isEmpty()) {
                throw new IllegalArgumentException("文件为空");
            }
            return sysFileService.uploadFile(multipartFile, path);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 列出文件列表
     */
    @GetMapping("/list")
    public List<LocalFileVo> list(String path) {
        return sysFileService.listFile(path);
    }

    /**
     * 创建文件夹
     */
    @PutMapping
    public JsonResult mkdir(@RequestParam(value = "path", required = false) String path
            , @RequestParam("name") String dir) {
        // /a/
        path = StrUtil.nullToEmpty(path);
        path = StrUtil.addPrefixIfNot(path, StrPool.SLASH);
        path = StrUtil.addSuffixIfNot(path, StrPool.SLASH);
        // b
        dir = StrUtil.removePrefix(dir, StrPool.SLASH);
        dir = StrUtil.removeSuffix(dir, StrPool.SLASH);
        String rootPath = localUploadProperties.getLocalFilePath();
        // /local/a/b
        FileUtil.mkdir(rootPath + path + dir);
        return JsonResult.ok();
    }

    /**
     * 删除文件
     */
    @DeleteMapping
    public JsonResult rm(@RequestParam("path") String relativePath) {
        Assert.notBlank(relativePath, "文件不存在");
        // 文件夹检查
        relativePath = StrUtil.addPrefixIfNot(relativePath, StrPool.SLASH);
        String absolutePath = localUploadProperties.getLocalFilePath() + relativePath;
        // Assert.isFalse(FileUtil.isDirectory(absolutePath), "不能删除文件夹");
        boolean b = FileUtil.del(absolutePath);
        return b ? JsonResult.ok() : JsonResult.error();
    }

    /**
     * 重命名文件或文件夹名称
     *
     * @param path 相对路径
     * @param name 新文件或文件夹名称
     * @return
     */
    @PostMapping
    public JsonResult rename(@RequestParam("path") String path, @RequestParam("name") String name) {
        checkReqParamThrowException(path);
        path = StrUtil.addPrefixIfNot(path, StrPool.SLASH);
        try {
            FileUtil.rename(new File(localUploadProperties.getLocalFilePath() + path), name, false);
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                return JsonResult.error("文件/夹[{" + name + "}]已存在");
            } else {
                return JsonResult.error(e.getMessage());
            }
        }
        return JsonResult.ok();
    }

}
