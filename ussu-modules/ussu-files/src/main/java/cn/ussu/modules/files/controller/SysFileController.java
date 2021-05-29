package cn.ussu.modules.files.controller;

import cn.ussu.common.core.base.BaseController;
import cn.ussu.common.core.entity.LocalFileVo;
import cn.ussu.modules.files.service.SysFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class SysFileController extends BaseController {

    @Autowired
    private SysFileService sysFileService;

    @PostMapping(value = "/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public LocalFileVo upload(@RequestParam("file") MultipartFile multipartFile) {
        try {
            if (multipartFile.isEmpty()) {
                throw new IllegalArgumentException("文件为空");
            }
            return sysFileService.uploadFile(multipartFile);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
