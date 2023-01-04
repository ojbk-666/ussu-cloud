package cc.ussu.modules.system.service.impl;

import cc.ussu.common.redis.util.DictUtil;
import cc.ussu.common.security.util.SecurityUtil;
import cc.ussu.modules.system.entity.SysUser;
import cc.ussu.modules.system.entity.SysUserPost;
import cc.ussu.modules.system.entity.SysUserRole;
import cc.ussu.modules.system.mapper.SysUserMapper;
import cc.ussu.modules.system.service.ISysUserPostService;
import cc.ussu.modules.system.service.ISysUserRoleService;
import cc.ussu.modules.system.service.ISysUserService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.Master;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统用户 服务实现类
 * </p>
 *
 * @author liming
 * @since 2021-12-17 14:53:58
 */
@Master
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private ISysUserRoleService sysUserRoleService;
    @Autowired
    private ISysUserPostService sysUserPostService;
    public static final String DEFAULT_PASSWORD_PARAM_KEY = "ussu:defaultPassword";
    public static final String DEFAULT_PASSWORD_IF_PARAM_NULL = "admin";

    @Override
    public boolean checkExistAccount(SysUser p) {
        long count = count(Wrappers.lambdaQuery(SysUser.class).eq(SysUser::getAccount, p.getAccount())
                .ne(StrUtil.isNotBlank(p.getId()), SysUser::getId, p.getId()));
        return count > 0;
    }

    @Override
    public SysUser getByAccount(String account) {
        if (StrUtil.isBlank(account)) {
            return null;
        }
        SysUser user = getOne(Wrappers.lambdaQuery(SysUser.class).eq(SysUser::getAccount, account));
        return user;
    }

    @Override
    @Transactional
    public void add(SysUser p) {
        // Assert.notBlank(p.getAccount(), "用户名不能为空");
        // Assert.notBlank(p.getName(), "昵称不能为空");
        Assert.isFalse(checkExistAccount(p), "用户名已存在");
        String defaultPassword = DictUtil.getValue("system",  DEFAULT_PASSWORD_PARAM_KEY, DEFAULT_PASSWORD_IF_PARAM_NULL);
        p.setPassword(SecurityUtil.encryptPassword(StrUtil.isBlank(p.getPassword()) ? defaultPassword : p.getPassword()));
        super.save(p);
        if (CollUtil.isNotEmpty(p.getRoleIds())) {
            List<SysUserRole> userRoleList = p.getRoleIds().stream().map(r -> new SysUserRole().setUserId(p.getId()).setRoleId(r)).collect(Collectors.toList());
            sysUserRoleService.saveBatch(userRoleList);
        }
        if (CollUtil.isNotEmpty(p.getPostIds())) {
            List<SysUserPost> userRoleList = p.getPostIds().stream().map(r -> new SysUserPost().setUserId(p.getId()).setPostId(r)).collect(Collectors.toList());
            sysUserPostService.saveBatch(userRoleList);
        }
    }

    @Override
    @Transactional
    public void edit(SysUser p) {
        Assert.notBlank(p.getId(), "id不能为空");
        // Assert.notBlank(p.getAccount(), "用户名不能为空");
        // Assert.notBlank(p.getName(), "昵称不能为空");
        Assert.isFalse(checkExistAccount(p), "用户名已存在");
        p.setPassword(null);
        super.updateById(p);
        sysUserRoleService.remove(Wrappers.lambdaQuery(SysUserRole.class).eq(SysUserRole::getUserId, p.getId()));
        if (CollUtil.isNotEmpty(p.getRoleIds())) {
            List<SysUserRole> userRoleList = p.getRoleIds().stream().map(r -> new SysUserRole().setUserId(p.getId()).setRoleId(r)).collect(Collectors.toList());
            sysUserRoleService.saveBatch(userRoleList);
        }
        sysUserPostService.remove(Wrappers.lambdaQuery(SysUserPost.class).eq(SysUserPost::getUserId, p.getId()));
        if (CollUtil.isNotEmpty(p.getPostIds())) {
            List<SysUserPost> userRoleList = p.getPostIds().stream().map(r -> new SysUserPost().setUserId(p.getId()).setPostId(r)).collect(Collectors.toList());
            sysUserPostService.saveBatch(userRoleList);
        }
    }

    /**
     * 删除
     */
    @Override
    @Transactional
    public void del(String ids) {
        List<String> idList = CollUtil.removeBlank(StrUtil.split(ids, StrUtil.COMMA));
        Assert.notEmpty(idList, "id不能为空");
        super.removeByIds(idList);
        sysUserRoleService.deleteByUserIds(idList);
        sysUserPostService.remove(Wrappers.lambdaQuery(SysUserPost.class).in(SysUserPost::getUserId, idList));
    }

    @Override
    @Transactional
    public void authRole(SysUser p) {
        Assert.notBlank(p.getId(), "id不能为空");
        sysUserRoleService.remove(Wrappers.lambdaQuery(SysUserRole.class).eq(SysUserRole::getUserId, p.getId()));
        if (CollUtil.isNotEmpty(p.getRoleIds())) {
            List<SysUserRole> userRoleList = p.getRoleIds().stream().map(r -> new SysUserRole().setUserId(p.getId()).setRoleId(r)).collect(Collectors.toList());
            sysUserRoleService.saveBatch(userRoleList);
        }
    }

    /**
     * 导入用户
     *
     * @param sysUserList
     */
    @Transactional
    @Override
    public void importUser(List<SysUser> sysUserList) {
        Assert.notEmpty(sysUserList, "未获取到数据");
        List<String> accountList = CollUtil.getFieldValues(sysUserList, "account", String.class);
        Assert.isTrue(sysUserList.size() == accountList.size(), "存在重复的账号");
        // 验证账号重复
        List<SysUser> exists = super.list(Wrappers.lambdaQuery(SysUser.class).select(SysUser::getAccount).in(SysUser::getAccount, accountList));
        Assert.isTrue(exists.size() == 0, "以下账号已存在：{}", exists.stream().map(SysUser::getAccount).collect(Collectors.joining(StrUtil.COMMA)));
        String defaultPassword = DictUtil.getValue("system", DEFAULT_PASSWORD_PARAM_KEY, DEFAULT_PASSWORD_IF_PARAM_NULL);
        String pwd = SecurityUtil.encryptPassword(defaultPassword);
        String remark = SecurityUtil.getLoginUser().getAccount() + "于" + DateUtil.now() + "导入";
        for (SysUser user : sysUserList) {
            user.setPassword(pwd).setRemark(remark);
        }
        super.saveBatch(sysUserList);
    }
}
