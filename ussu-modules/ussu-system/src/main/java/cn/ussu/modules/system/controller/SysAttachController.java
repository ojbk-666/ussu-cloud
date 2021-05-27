package cn.ussu.modules.system.controller;

import cn.hutool.core.util.StrUtil;
import cn.ussu.common.core.base.BaseAdminController;
import cn.ussu.common.core.constants.SwaggerConstants;
import cn.ussu.common.core.entity.JsonResult;
import cn.ussu.common.core.exception.RequestEmptyException;
import cn.ussu.common.security.annotation.PermCheck;
import cn.ussu.modules.system.entity.SysAttach;
import cn.ussu.modules.system.service.ISysAttachService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 附件表 控制器
 * </p>
 *
 * @author liming
 * @since 2020-05-17
 */
@RestController
@RequestMapping("/sys-attach")
// @InsertLog(code = "sys-attach", name = "附件")
public class SysAttachController extends BaseAdminController {

    @Autowired
    private ISysAttachService service;

    /*
     * 分页查询
     */
    @ApiOperation(value = SwaggerConstants.list)
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataTypeClass = String.class, paramType = SwaggerConstants.paramType_form)
            , @ApiImplicitParam(name = "limit", value = "分页大小", required = true, dataTypeClass = String.class, paramType = SwaggerConstants.paramType_form)
            , @ApiImplicitParam(value = "查询参数", dataTypeClass = String.class, paramType = SwaggerConstants.paramType_form)
    })
    @GetMapping
    public Object list(@RequestParam @ApiParam(required = false) Map param) {
        return service.findPage(param);
    }

    /**
     * 新增
     */
    @ApiOperation(value = SwaggerConstants.add)
    @PutMapping
    @PermCheck("system:attach:select")
    public Object add(@ApiParam SysAttach obj) {
        obj.insert();
        return JsonResult.ok();
    }

    /**
     * 修改
     */
    @ApiOperation(value = SwaggerConstants.edit)
    @PostMapping
    @PermCheck("system:attach:edit")
    public Object edit(@ApiParam SysAttach obj) {
        if (StrUtil.isBlank(obj.getId())) throw new RequestEmptyException();
        obj.updateById();
        return JsonResult.ok();
    }

    /**
     * 删除
     */
    @ApiOperation(value = SwaggerConstants.deleteBatch)
    @DeleteMapping("/{id}")
    @PermCheck("system:attach:delete")
    public Object delete(@PathVariable("id") @ApiParam(name = "id",required = true,type = SwaggerConstants.paramType_path) String id) {
        List<String> idList = splitCommaList(id, true);
        service.removeByIds(idList);
        return JsonResult.ok();
    }

    @ApiOperation(value = SwaggerConstants.deleteBatch)
    @PermCheck("system:attach:delete")
    @DeleteMapping("/delete")
    public Object deleteUnRest(@ApiParam(name = "id",value = SwaggerConstants.paramDesc_delete) String id,@ApiParam(name = "attachPath",value = "附件路径") String attachPath,@ApiParam(name = "deleteFile",value = "是否同时删除文件") boolean deleteFile) {
        if (StrUtil.isNotBlank(id)) service.removeById(id);
        // String filePath = Kit.getUploadRootPath() + attachPath;
        // if (deleteFile && FileUtil.exist(filePath) && FileUtil.isFile(filePath)) {
        //     FileUtil.del(filePath);
        // }
        return JsonResult.ok();
    }

    /**
     * 获取文件列表
     */
    /*@ApiOperation(value = "获取文件列表")
    @PermCheck("system:attach:select")
    @GetMapping("/fileChoose")
    public Map files(@ApiParam(name = "基础路径") String basePath, @RequestParam("dir") @ApiParam(name = "子文件夹") String subPath) {
        List<Object> list = service.diskFilesList(basePath, subPath);
        return JsonResult.ok().data(list);
    }*/

    /**
     * 创建新文件夹
     */
    @ApiOperation(value = "创建新文件夹")
    @PermCheck("system:attach:edit")
    @PostMapping("/newDir")
    public Map newDir(String dirName, String dir) {
        if (StrUtil.isBlank(dirName) || StrUtil.isBlank(dir)) {
            throw new RequestEmptyException();
        }
        dir = dir.endsWith("/") ? dir : dir + "/";
        // String imageServer = Kit.getUploadRootPath();
        // FileUtil.mkdir(imageServer + dir + dirName);
        return JsonResult.ok();
    }

}
