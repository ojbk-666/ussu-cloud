package cc.ussu.modules.system.controller;

import cc.ussu.common.core.constants.ServiceNameConstants;
import cc.ussu.common.core.constants.StrConstants;
import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.common.datasource.util.MybatisPlusUtil;
import cc.ussu.common.log.annotation.SystemLog;
import cc.ussu.common.log.constants.SystemLogConstants;
import cc.ussu.common.redis.util.DictUtil;
import cc.ussu.common.security.annotation.PermCheck;
import cc.ussu.common.security.auth.AuthLogic;
import cc.ussu.common.security.util.SecurityUtil;
import cc.ussu.modules.system.entity.*;
import cc.ussu.modules.system.entity.vo.SelectUserParamVO;
import cc.ussu.modules.system.mapper.SysUserMapper;
import cc.ussu.modules.system.service.*;
import cc.ussu.system.api.model.LoginUser;
import cc.ussu.system.api.vo.SysUserVO;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统用户 前端控制器
 * </p>
 *
 * @author liming
 * @since 2021-12-17 14:53:58
 */
@SystemLog(serviceName = ServiceNameConstants.SERVICE_SYSETM, group = "用户管理")
@RestController
@RequestMapping("${ussu.mapping-prefix.system}/sys-user")
public class SysUserController extends BaseController {

    private static final String PERM_PREFIX = "system:user:";

    @Autowired
    private ISysUserService service;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private ISysDeptService sysDeptService;
    @Autowired
    private ISysRoleService sysRoleService;
    @Autowired
    private ISysPostService sysPostService;
    @Autowired
    private ISysUserRoleService sysUserRoleService;
    @Autowired
    private ISysUserPostService sysUserPostService;
    @Autowired
    private ISysMenuService menuService;

    private LambdaQueryWrapper<SysUser> getLambdaQueryWrapper(SysUser p) {
        LambdaQueryWrapper<SysUser> qw = Wrappers.lambdaQuery(SysUser.class)
                .eq(StrUtil.isNotBlank(p.getDeptId()), SysUser::getDeptId, p.getId())
                .eq(StrUtil.isNotBlank(p.getDisableFlag()), SysUser::getDisableFlag, p.getDisableFlag())
                .eq(StrUtil.isNotBlank(p.getUserType()), SysUser::getUserType, p.getUserType())
                .eq(p.getSex() != null, SysUser::getSex, p.getSex())
                .like(StrUtil.isNotBlank(p.getAccount()), SysUser::getAccount, p.getAccount())
                .like(StrUtil.isNotBlank(p.getNickName()), SysUser::getNickName, p.getNickName())
                .like(StrUtil.isNotBlank(p.getPhone()), SysUser::getPhone, p.getPhone())
                .like(StrUtil.isNotBlank(p.getEmail()), SysUser::getEmail, p.getEmail());
        return qw;
    }

    @PermCheck(PERM_PREFIX + SELECT)
    @GetMapping("/page")
    public JsonResult page(SysUser p) {
        /*LambdaQueryWrapper qw = getLambdaQueryWrapper(p).orderByDesc(SysUserVO::getCreateTime);
        Page<SysUserVO> page = service.page(MybatisPlusUtil.getPage(), qw);
        for (SysUserVO user : page.getRecords()) {
            if (StrUtil.isNotBlank(user.getDeptId())) {
                user.setDept(new SysDept().setId(user.getDeptId()).selectById());
            }
            // 获取角色
            List<String> roleIdList = sysUserRoleService.list(Wrappers.lambdaQuery(SysUserRole.class).eq(SysUserRole::getUserId, user.getId()))
                    .stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(roleIdList)) {
                List<String> roleNameList = sysRoleService.list(Wrappers.lambdaQuery(SysRole.class).in(SysRole::getId, roleIdList)).stream().map(SysRole::getRoleName).collect(Collectors.toList());
                user.setRoleNameList(roleNameList);
            }
        }
        return page;*/
        return MybatisPlusUtil.getResult(sysUserMapper.getPageList(MybatisPlusUtil.getPage(), p));
    }

    /**
     * 导出
     */
    @PermCheck(PERM_PREFIX + EXPORT)
    @GetMapping("/export")
    public void export(SysUser p) throws IOException {
        List<SysUser> list = service.list(getLambdaQueryWrapper(p));
        // 通过工具类创建writer，默认创建xls格式
        ExcelWriter writer = ExcelUtil.getWriter();
        //自定义标题别名
        writer.addHeaderAlias("name", "姓名");
        writer.addHeaderAlias("account", "账号");
        writer.addHeaderAlias("sex", "性别");
        writer.addHeaderAlias("phone", "手机号");
        writer.addHeaderAlias("email", "邮箱");
        // 默认的，未添加alias的属性也会写出，如果想只写出加了别名的字段，可以调用此方法排除之
        writer.setOnlyAlias(true);
        // 合并单元格后的标题行，使用默认标题样式
        writer.merge(4, "用户列表");
        // 设置内容字体
        Font font = writer.createFont();
        font.setFontHeight(new Short("280"));
        // 第二个参数表示是否忽略头部样式
        writer.getStyleSet().setFont(font, false);
        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(list, true);
        // setDownloadFileResponseHeader(ExcelUtil.XLS_CONTENT_TYPE, "user_list.xls");
        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        // 关闭writer，释放内存
        writer.close();
        IoUtil.close(out);
    }

    /**
     * 下载导入模板
     */
    @PermCheck(PERM_PREFIX + IMPORT)
    @GetMapping("/download-import-template")
    public void downloadImportTemplate(HttpServletResponse response) {
        InputStream stream = ResourceUtil.getStream("excel/user_import_template.xls");
        ServletUtil.write(response, stream, MediaType.APPLICATION_OCTET_STREAM_VALUE, "user_import_template.xls");
        IoUtil.close(stream);
    }

    /**
     * todo 导入
     */
    @SystemLog(name = "导入")
    @PermCheck(PERM_PREFIX + IMPORT)
    @PostMapping("/import")
    public void importUser(@RequestPart("file") MultipartFile multipartFile) throws IOException {
        if (!multipartFile.isEmpty()) {
            // List<SysUserVO> sysUserList = ExcelImportUtil.readList(multipartFile.getInputStream(), SysUserVO.class);
            // service.importUser(sysUserList);
        }
    }

    /**
     * 单个详情
     */
    @PermCheck(PERM_PREFIX + SELECT)
    @GetMapping({"/{id}", "/"})
    public JsonResult<Map<String, Object>> detail(@PathVariable(required = false) String id) {
        List<SysRole> roles = sysRoleService.list(Wrappers.lambdaQuery(SysRole.class)
                .orderByAsc(SysRole::getRoleSort));
        List<SysPost> posts = sysPostService.list(Wrappers.lambdaQuery(SysPost.class)
                .orderByAsc(SysPost::getPostSort));
        SysUser user = null;
        List<String> roleIds = null;
        List<String> postIds = null;
        if (StrUtil.isNotBlank(id)) {
            user = service.getById(id);
            if (user != null && StrUtil.isNotBlank(user.getDeptId())) {
                user.setDept(sysDeptService.getById(user.getDeptId()));
            }
            // 取用户角色id集合
            List<SysUserRole> userRoleList = sysUserRoleService.list(Wrappers.lambdaQuery(SysUserRole.class).eq(SysUserRole::getUserId, id));
            roleIds = userRoleList.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
            // 取用户岗位id集合
            List<SysUserPost> userPostList = sysUserPostService.list(Wrappers.lambdaQuery(SysUserPost.class).eq(SysUserPost::getUserId, id));
            postIds = userPostList.stream().map(SysUserPost::getPostId).collect(Collectors.toList());
            if (user != null) {
                user.setRoleIds(roleIds).setPostIds(postIds);
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("roles", roles);
        map.put("posts", posts);
        map.put("roleIds", roleIds);
        map.put("postIds", postIds);
        return JsonResult.ok(map);
    }

    /**
     * 通过用户名查询一个用户
     */
    @GetMapping("/get-by-account/{account}")
    public JsonResult<LoginUser> getByAccount(@PathVariable String account) {
        SysUser user = service.getByAccount(account);
        if (user == null) {
            return JsonResult.error("用户不存在");
        }
        SysUserVO userVO = fillSysUserInfoNoPerms(user);
        LoginUser loginUser = fillLoginUserPerms(userVO);
        return JsonResult.ok(loginUser);
    }

    /**
     * 根据用户id查询
     */
    @GetMapping("/get-by-id/{userId}")
    public JsonResult getUserById(@PathVariable String userId) {
        SysUser user = service.getById(userId);
        if (user == null) {
            return JsonResult.error("用户不存在");
        }
        return JsonResult.ok(user);
    }

    /**
     * 通过指定字段查询单个用户
     */
    @GetMapping("/get-by-ext")
    public JsonResult<LoginUser> getByExt(SysUser query, Boolean detail) {
        if (detail == null) {
            detail = true;
        }
        SysUser user = service.getOne(Wrappers.lambdaQuery(SysUser.class)
            .eq(StrUtil.isNotBlank(query.getExt1()), SysUser::getExt1, query.getExt1())
            .eq(StrUtil.isNotBlank(query.getExt2()), SysUser::getExt2, query.getExt2())
            .eq(StrUtil.isNotBlank(query.getExt3()), SysUser::getExt3, query.getExt3())
            .eq(StrUtil.isNotBlank(query.getExt4()), SysUser::getExt4, query.getExt4())
            .eq(query.getExt5() != null, SysUser::getExt5, query.getExt5())
            .eq(query.getExt6() != null, SysUser::getExt6, query.getExt6())
        );
        if (user == null) {
            return JsonResult.error("未找到用户");
        }
        if (detail) {
            SysUserVO userVO = fillSysUserInfoNoPerms(user);
            LoginUser loginUser = fillLoginUserPerms(userVO);
            return JsonResult.ok(loginUser);
        } else {
            LoginUser loginUser = new LoginUser();
            loginUser.setUser(BeanUtil.toBean(user, SysUserVO.class));
            return JsonResult.ok(loginUser);
        }
    }

    /**
     * 详情
     */
    @PermCheck(PERM_PREFIX + SELECT)
    @GetMapping("/detail/{id}")
    public JsonResult<SysUserVO> detailById(@PathVariable String id) {
        SysUser user = service.getById(id);
        Assert.notNull(user, "该数据不存在");
        SysUserVO userVO = fillSysUserInfoNoPerms(user);
        return JsonResult.ok(userVO);
    }

    /**
     * 补充用户信息（角色信息，岗位信息）
     */
    private SysUserVO fillSysUserInfoNoPerms(SysUser user) {
        SysUserVO userVO = BeanUtil.toBean(user, SysUserVO.class);
        String userId = user.getId();
        // 获取部门信息
        String deptId = user.getDeptId();
        if (StrUtil.isNotBlank(deptId)) {
            SysDept dept = sysDeptService.getById(deptId);
            if (dept != null) {
                userVO.setDeptName(dept.getName());
            }
        }
        // 角色信息
        Set<String> roleIdList = sysUserRoleService.getByUserId(userId).stream().map(SysUserRole::getRoleId).collect(Collectors.toSet());
        userVO.setRoleIdList(roleIdList);
        if (CollUtil.isNotEmpty(roleIdList)) {
            List<SysRole> roles = sysRoleService.listByIds(roleIdList);
            Set<SysRole> filterRoles = roles.stream().filter(r -> StrConstants.CHAR_FALSE.equals(r.getDisableFlag())).collect(Collectors.toSet());
            userVO.setRoleCodeList(filterRoles.stream().map(SysRole::getRoleCode).collect(Collectors.toSet()));
            userVO.setRoleNameList(filterRoles.stream().map(SysRole::getRoleName).collect(Collectors.toSet()));
        }
        // 岗位信息
        Set<String> postIdList = sysUserPostService.getByUserId(userId).stream().map(SysUserPost::getPostId).collect(Collectors.toSet());
        userVO.setPostIdList(postIdList);
        if (CollUtil.isNotEmpty(postIdList)) {
            List<SysPost> posts = sysPostService.listByIds(postIdList);
            Set<SysPost> filterPosts = posts.stream().filter(r -> StrConstants.CHAR_FALSE.equals(r.getDisableFlag())).collect(Collectors.toSet());
            userVO.setPostCodeList(filterPosts.stream().map(SysPost::getPostCode).collect(Collectors.toSet()));
            userVO.setPostNameList(filterPosts.stream().map(SysPost::getPostName).collect(Collectors.toSet()));
        }
        return userVO;
    }

    private LoginUser fillLoginUserPerms(SysUserVO userVO) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUser(userVO);
        loginUser.setUserId(userVO.getId());
        loginUser.setAccount(userVO.getAccount());
        loginUser.setRoleCodeList(userVO.getRoleCodeList());
        Set<String> permissions = menuService.listActionByUserId(userVO.getId());
        if (SecurityUtil.isSuperAdmin(userVO.getId())) {
            permissions.add(AuthLogic.ALL_PERMISSION);
        }
        loginUser.setPermissions(permissions);
        return loginUser;
    }

    /**
     * 修改状态
     */
    @SystemLog(name = SystemLogConstants.CHANGE_STATUS)
    @PermCheck(PERM_PREFIX + EDIT)
    @PostMapping("/changeStatus")
    public JsonResult changeStatus(@RequestBody SysUser p) {
        Assert.notBlank(p.getId(), "id不能为空");
        Assert.notBlank(p.getDisableFlag(), "状态不能为空");
        service.updateById(new SysUser().setId(p.getId()).setDisableFlag(p.getDisableFlag()));
        return JsonResult.ok();
    }

    /**
     * 修改角色
     */
    @SystemLog(name = "授权角色")
    @PermCheck(PERM_PREFIX + EDIT)
    @PostMapping("/authRole")
    public JsonResult authRole(@RequestBody SysUser p) {
        service.authRole(p);
        return JsonResult.ok();
    }

    /**
     * 通过角色分页获取
     */
    @PermCheck(SysRoleController.PERM_PREFIX + "auth-user")
    @GetMapping("/page-by-role")
    public JsonResult pageByRole(@RequestParam String roleId, SysUser p) {
        Set<String> userIds = sysUserRoleService.list(Wrappers.lambdaQuery(SysUserRole.class).eq(SysUserRole::getRoleId, roleId)).stream().map(SysUserRole::getUserId).collect(Collectors.toSet());
        LambdaQueryWrapper<SysUser> qw = getLambdaQueryWrapper(p);
        if (CollUtil.isNotEmpty(userIds)) {
            qw.in(SysUser::getId, userIds);
        } else {
            qw.eq(SysUser::getDelFlag, true);
        }
        Page page = service.page(MybatisPlusUtil.getPage(), qw);
        return MybatisPlusUtil.getResult(page);
    }

    /**
     * 排除角色分页获取
     */
    @PermCheck(SysRoleController.PERM_PREFIX + "auth-user")
    @GetMapping("/page-by-role/exclude")
    public JsonResult pageByRoleExclude(@RequestParam String roleId, SysUser p) {
        Set<String> userIds = sysUserRoleService.list(Wrappers.lambdaQuery(SysUserRole.class).eq(SysUserRole::getRoleId, roleId)).stream().map(SysUserRole::getUserId).collect(Collectors.toSet());
        LambdaQueryWrapper<SysUser> qw = getLambdaQueryWrapper(p);
        if (CollUtil.isNotEmpty(userIds)) {
            qw.notIn(SysUser::getId, userIds);
        }
        Page page = service.page(MybatisPlusUtil.getPage(), qw);
        return MybatisPlusUtil.getResult(page);
    }

    /**
     * 通过角色分页获取，如果有角色则筛选角色
     *
     * @param p
     */
    @GetMapping("/selectUser")
    public JsonResult selectUserOrByRoleCode(SelectUserParamVO p) {
        IPage<SysUser> iPage = sysUserMapper.getSelectUserList(MybatisPlusUtil.getPage(), p);
        return MybatisPlusUtil.getResult(iPage);
    }

    /**
     * 重置密码
     */
    @SystemLog(name = "重置密码")
    @PermCheck(PERM_PREFIX + "resetPwd")
    @PostMapping("/resetPwd/{ids}")
    public JsonResult resetPwd(@PathVariable String ids) {
        List<String> idList = CollUtil.removeBlank(StrUtil.split(ids, StrUtil.COMMA));
        Assert.notEmpty(idList, "id不能为空");
        String password = DictUtil.getValue("system", "ussu:defaultPassword", "admin");
        String md5Password = MD5.create().digestHex(password, CharsetUtil.CHARSET_UTF_8);
        service.update(new SysUser().setPassword(SecurityUtil.encryptPassword(md5Password)), Wrappers.lambdaQuery(SysUser.class).in(SysUser::getId, idList));
        return JsonResult.ok();
    }

    @SystemLog(name = SystemLogConstants.INSERT)
    @PermCheck(PERM_PREFIX + ADD)
    @PutMapping({"", "/add"})
    public JsonResult add(@RequestBody SysUser p) {
        service.add(p);
        return JsonResult.ok();
    }

    @SystemLog(name = SystemLogConstants.UPDATE)
    @PermCheck(PERM_PREFIX + EDIT)
    @PostMapping({"", "/edit"})
    public JsonResult edit(@RequestBody SysUser p) {
        service.edit(p);
        return JsonResult.ok();
    }

    @SystemLog(name = SystemLogConstants.DELETE)
    @PermCheck(PERM_PREFIX + DELETE)
    @DeleteMapping({"/{ids}", "/del/{ids}"})
    public JsonResult del(@PathVariable String ids) {
        service.del(ids);
        return JsonResult.ok();
    }

}

