package cn.ussu.modules.system.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import cn.ussu.common.core.base.BaseAdminController;
import cn.ussu.common.core.constants.ErrorMsgConstants;
import cn.ussu.common.core.constants.StrConstants;
import cn.ussu.common.core.constants.SwaggerConstants;
import cn.ussu.common.core.entity.JsonResult;
import cn.ussu.common.core.exception.RequestEmptyException;
import cn.ussu.common.security.util.SecurityUtils;
import cn.ussu.modules.system.entity.SysDept;
import cn.ussu.modules.system.entity.SysRole;
import cn.ussu.modules.system.entity.SysUser;
import cn.ussu.modules.system.entity.SysUserRole;
import cn.ussu.modules.system.model.param.SysUserParam;
import cn.ussu.modules.system.model.vo.ThirdLoginFormAlipayVo;
import cn.ussu.modules.system.service.ISysDeptService;
import cn.ussu.modules.system.service.ISysRoleService;
import cn.ussu.modules.system.service.ISysUserRoleService;
import cn.ussu.modules.system.service.ISysUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统用户 前端控制器
 * </p>
 *
 * @author liming
 * @since 2020-05-06
 */
@RestController
@RequestMapping("/sys-user")
// @InsertLog(code = "sys-user", name = "用户管理")
public class SysUserController extends BaseAdminController {

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysRoleService sysRoleService;
    @Autowired
    private ISysDeptService sysDeptService;
    @Autowired
    private ISysUserRoleService sysUserRoleService;

    @ApiOperation(value = SwaggerConstants.list)
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataTypeClass = String.class, paramType = SwaggerConstants.paramType_form)
            , @ApiImplicitParam(name = "limit", value = "分页大小", required = true, dataTypeClass = String.class, paramType = SwaggerConstants.paramType_form)
            , @ApiImplicitParam(value = "查询参数", dataTypeClass = String.class, paramType = SwaggerConstants.paramType_form)
    })
    @PreAuthorize("@pc.check('sys-user:select')")
    @GetMapping
    public Object list(SysUserParam sysUserParam) {
        return JsonResult.ok().data(sysUserService.findPage(sysUserParam));
    }

    /**
     * 获取用户详情
     */
    @GetMapping("/{userId}")
    public Object detail(@PathVariable String userId) {
        SysUser userInfo = sysUserService.getById(userId);
        if (userInfo != null) {
            // 设置部门信息
            if (StrUtil.isNotBlank(userInfo.getDeptId())) {
                SysDept sysDept = new SysDept().setId(userInfo.getDeptId()).selectById();
                userInfo.setSysDept(sysDept).setDeptName(sysDept.getName());
            }
            List<SysRole> roles = sysRoleService.list();
            LambdaQueryWrapper<SysUserRole> qw = new LambdaQueryWrapper<>();
            qw.eq(SysUserRole::getUserId, userId).select(SysUserRole::getRoleId);
            List<String> userRoleIds = sysUserRoleService.list(qw).stream().map(item -> item.getRoleId()).collect(Collectors.toList());
            return JsonResult.ok().data(userInfo).put("roles", roles).put("roleIds", userRoleIds);
        }
        return JsonResult.error(ErrorMsgConstants.ACCOUNT_UNKNOWN);
    }

    /**
     * 添加用户
     */
    @PreAuthorize("@pc.check('sys-user:add')")
    @PutMapping
    public Object addOne(@RequestBody SysUser sysUser) {
        this.sysUserService.addOne(sysUser);
        return JsonResult.ok();
    }

    /**
     * 编辑用户
     */
    @PreAuthorize("@pc.check('sys-user:edit')")
    @PostMapping
    public Object updateOne(@RequestBody SysUser sysUser) {
        sysUserService.updateOne(sysUser);
        return JsonResult.ok();
    }

    /**
     * 删除用户
     */
    @ApiOperation(value = SwaggerConstants.delete, notes = "删除用户")
    @PreAuthorize("@pc.check('sys-user:delete')")
    @DeleteMapping("/{id}")
    public Object delete(@PathVariable("id") @ApiParam(name = "id", value = SwaggerConstants.paramDesc_delete, required = true) String id) {
        List<String> idList = splitCommaList(id, true);
        sysUserService.deleteUser(idList);
        return JsonResult.ok();
    }

    /**
     * 根据用户名获取用户信息
     */
    @GetMapping("/account")
    public Object getSysUserByUsername(@RequestParam String username) {
        if (StrUtil.isBlank(username)) throw new RequestEmptyException();
        SysUser sysUser = sysUserService.findByAccount(username);
        if (sysUser != null) {
            if (StrUtil.isNotBlank(sysUser.getDeptId())) {
                SysDept sysDept = sysDeptService.getById(sysUser.getDeptId());
                sysUser.setSysDept(sysDept);
            }
            List<SysRole> roleList = sysRoleService.findListByUserId(sysUser.getId());
            sysUser.setRoleList(roleList);
        }
        return JsonResult.ok().data(sysUser);
    }

    /**
     * 改变用户状态
     */
    @PostMapping("/changeStatus")
    public Object changeStatus(@RequestBody SysUser sysUser) {
        checkReqParamThrowException(sysUser.getId());
        checkReqParamThrowException(sysUser.getStatus());
        if (SecurityUtils.isSuperAdmin(sysUser.getId())) {
            throw new IllegalArgumentException(ErrorMsgConstants.UNAUTHORIZED);
        }
        new SysUser().setId(sysUser.getId()).setStatus(sysUser.getStatus()).updateById();
        return JsonResult.ok();
    }

    @PostMapping("/profile")
    public Object updateUserProfile(@RequestBody SysUser sysUser) {
        new SysUser().setId(SecurityUtils.getUserId())
                .setNickName(sysUser.getNickName())
                .setPhone(sysUser.getPhone())
                .setEmail(sysUser.getEmail())
                .updateById();
        return JsonResult.ok();
    }

    /**
     * 修改当前用户密码
     */
    @ApiOperation(value = "修改当前登录用户的密码")
    @PostMapping("/updatePwd")
    public Object updatePwd(@RequestBody SysUserParam sysUserParam) {
        checkReqParamThrowException(sysUserParam.getOldPassword());
        checkReqParamThrowException(sysUserParam.getNewPassword());
        // 验证旧密码
        SysUser user = new SysUser().setId(SecurityUtils.getUserId()).selectById();
        if (!SecurityUtils.matchesPassword(MD5.create().digestHex(sysUserParam.getOldPassword()), user.getPassword())) {
            return JsonResult.error("密码错误");
        }
        boolean b = new SysUser()
                .setId(user.getId())
                .setPassword(SecurityUtils.encryptPassword(MD5.create().digestHex(sysUserParam.getNewPassword())))
                .updateById();
        if (b) return JsonResult.ok();
        else return JsonResult.error();
    }

    /**
     * 重置指定用户密码
     */
    @ApiOperation(value = "重置指定用户密码")
    @PostMapping("/resetUserPwd")
    @PreAuthorize("@pc.check('sys-user:resetpwd')")
    public Object resetPwdUser(@RequestBody SysUser sysUser) {
        if (StrUtil.isAllBlank(sysUser.getId(), sysUser.getPassword())) {
            throw new RequestEmptyException();
        }
        if (SecurityUtils.isSuperAdmin(sysUser.getId())) {
            throw new IllegalArgumentException(ErrorMsgConstants.UNAUTHORIZED);
        }
        String pwd = SecurityUtils.encryptPassword(MD5.create().digestHex(sysUser.getPassword()));
        boolean b = new SysUser().setId(sysUser.getId()).setPassword(pwd).updateById();
        if (b) return JsonResult.ok();
        else return JsonResult.error();
    }

    /**
     * 批量更新
     *
     * @param sysUser
     * @param ids
     * @return
     */
    @PreAuthorize("@pc.check('sys-user:edit')")
    @PostMapping("/updateBatch")
    public Object updateBatch(SysUser sysUser, String ids) {
        checkReqParamThrowException(ids);
        List<SysUser> list = new ArrayList<>();
        for (String id : ids.split(StrConstants.COMMA)) {
            if (StrUtil.isBlank(id)) continue;
            SysUser newSysUser = ObjectUtil.clone(sysUser);
            list.add(newSysUser.setId(id));
        }
        sysUserService.updateBatchById(list);
        return JsonResult.ok();
    }

    /**
     * 写入用户-alipay
     */
    @PostMapping("/insertOrUpdateByThirdAlipay")
    public JsonResult insertOrUpdateByThirdAlipay(ThirdLoginFormAlipayVo thirdLoginFormAlipayVo) {
        if (thirdLoginFormAlipayVo == null) {
            throw new RequestEmptyException();
        }
        checkReqParamThrowException(thirdLoginFormAlipayVo.getUserId());
        SysUser existUser = new SysUser().setId(thirdLoginFormAlipayVo.getUserId()).selectById();
        // 不存在用户则创建并返回
        if (existUser == null) {
            new SysUser().setId(thirdLoginFormAlipayVo.getUserId())
                    .setAccount(thirdLoginFormAlipayVo.getUserId())
                    .setStatus(1).setDeptId("1000")
                    .setNickName(thirdLoginFormAlipayVo.getNickName())
                    .setAvatar(thirdLoginFormAlipayVo.getAvatar())
                    .setSex("m".equals(thirdLoginFormAlipayVo.getGender()) ? 1 : 2)
                    .insert();
            new SysUserRole().setUserId(thirdLoginFormAlipayVo.getUserId())
                    .setRoleId("1000")
                    .insert();
        }
        return JsonResult.ok().data(getSysUserByUsername(thirdLoginFormAlipayVo.getUserId()));
    }

    /*@GetMapping("/export")
    public Object export2Excel(@RequestParam Map param) throws Exception {
        String ids = MapUtil.getStr(param, "ids");
        String querySex = MapUtil.getStr(param, "query_sex");
        String queryName = MapUtil.getStr(param, "query_name");
        String queryAccount = MapUtil.getStr(param, "query_account");
        QueryWrapper<SysUser> qw = new QueryWrapper<>();
        qw.eq(StrUtil.isNotBlank(querySex), "sex", querySex)
                .eq(StrUtil.isNotBlank(queryAccount), "account", queryAccount)
                .like(StrUtil.isNotBlank(queryName), "name", queryName)
                .in(StrUtil.isNotBlank(ids), "id", ids.split(StrConstants.COMMA));
        List<SysUser> list = sysUserService.list(qw);
        Map<String, Object> m = getNewHashMap();
        m.put("userList", list);
        // 读取模板
        String rootPath = Kit.getRootPath();
        File tplFile = new File(rootPath + "/tpl_doc/system/export_user.xls");
        File f = new File(rootPath + "/tpl_doc/system/temp/" + Kit.getIdByMPStr() + ".xls");
        FileUtil.mkdir(f.getParent());
        FileUtil.file(f);
        String p2 = f.getAbsolutePath();
        XLSTransformer xlsTransformer = new XLSTransformer();
        xlsTransformer.transformXLS(tplFile.getAbsolutePath(), m, p2);
        ResponseEntity responseEntity = renderFile(f, "系统用户列表");
        FileUtil.del(f);
        return responseEntity;
    }*/

}
