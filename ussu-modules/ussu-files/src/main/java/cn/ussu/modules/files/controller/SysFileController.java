package cn.ussu.modules.files.controller;

import cn.ussu.common.core.base.BaseController;
import cn.ussu.common.core.entity.LocalFileVo;
import cn.ussu.modules.files.service.SysFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class SysFileController extends BaseController {

    @Autowired
    private SysFileService sysFileService;

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

}
