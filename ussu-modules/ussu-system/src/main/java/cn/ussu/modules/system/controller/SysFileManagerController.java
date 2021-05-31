package cn.ussu.modules.system.controller;

import cn.hutool.core.lang.Assert;
import cn.ussu.common.core.base.BaseAdminController;
import cn.ussu.common.core.entity.JsonResult;
import cn.ussu.common.core.entity.LocalFileVo;
import cn.ussu.modules.system.feign.RemoteFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件管理
 */
@RestController
@RequestMapping("/files")
public class SysFileManagerController extends BaseAdminController {

    @Autowired
    private RemoteFileService remoteFileService;

    @GetMapping
    public JsonResult list(@RequestParam String path) {
        List<LocalFileVo> list = remoteFileService.list(path);
        return JsonResult.ok().data(list);
    }

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    public JsonResult upload(@RequestParam("file") MultipartFile multipartFile
            , String path) {
        Assert.isTrue(!multipartFile.isEmpty());
        LocalFileVo vo = remoteFileService.upload(multipartFile, path);
        return JsonResult.ok().data(vo);
    }

    /**
     * 创建新文件夹
     */
    @PutMapping
    public JsonResult mkdir(@RequestBody LocalFileVo param) {
        checkReqParamThrowException(param);
        if (remoteFileService.mkdir(param.getPath(), param.getName())) {
            return JsonResult.ok();
        }
        return JsonResult.error();
    }

    /**
     * 删除文件
     */
    @DeleteMapping
    public JsonResult delete(@RequestParam LocalFileVo param) {
        checkReqParamThrowException(param);
        if (remoteFileService.delete(param.getPath())) {
            return JsonResult.ok();
        }
        return JsonResult.error();
    }

    /**
     * 重命名
     */
    @PostMapping
    public JsonResult rename(@RequestBody LocalFileVo param) {
        checkReqParamThrowException(param);
        if (remoteFileService.rename(param.getPath(), param.getName())) {
            return JsonResult.ok();
        }
        return JsonResult.error();
    }

}
