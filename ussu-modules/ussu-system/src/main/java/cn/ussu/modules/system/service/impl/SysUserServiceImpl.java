package cn.ussu.modules.system.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import cn.ussu.common.datasource.model.vo.ReturnPageInfo;
import cn.ussu.common.core.exception.RequestEmptyException;
import cn.ussu.common.security.util.SecurityUtils;
import cn.ussu.modules.system.core.util.DefaultPageFactory;
import cn.ussu.modules.system.entity.SysUser;
import cn.ussu.modules.system.entity.SysUserRole;
import cn.ussu.modules.system.mapper.SysUserMapper;
import cn.ussu.modules.system.model.param.SysUserParam;
import cn.ussu.modules.system.service.ISysUserRoleService;
import cn.ussu.modules.system.service.ISysUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
 * @since 2020-05-06
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired(required = false)
    private SysUserMapper sysUserMapper;
    @Autowired
    private ISysUserRoleService sysUserRoleService;

    @Override
    public SysUser findByAccount(String account) {
        if (StrUtil.isBlank(account)) throw new RequestEmptyException();
        QueryWrapper<SysUser> qw = new QueryWrapper<>();
        qw.eq("account", account);
        return this.sysUserMapper.selectOne(qw);
    }

    @Override
    public ReturnPageInfo<SysUser> findPage(SysUserParam sysUserParam) {
        LambdaQueryWrapper<SysUser> qw = new LambdaQueryWrapper<>();
        if (sysUserParam != null) {
            qw.eq(StrUtil.isNotBlank(sysUserParam.getAccount()), SysUser::getAccount, sysUserParam.getAccount())
                    .like(StrUtil.isNotBlank(sysUserParam.getName()), SysUser::getName, sysUserParam.getName())
                    .eq(StrUtil.isNotBlank(sysUserParam.getPhone()), SysUser::getPhone, sysUserParam.getPhone())
                    .eq(sysUserParam.getStatus() != null, SysUser::getStatus, sysUserParam.getStatus())
                    .ge(sysUserParam.getCreateTimeStart() != null, SysUser::getCreateTime, sysUserParam.getCreateTimeStart())
                    .le(sysUserParam.getCreateTimeEnd() != null, SysUser::getCreateTime, sysUserParam.getCreateTimeEnd())
                    .orderByAsc(SysUser::getDeptId, SysUser::getSort);
        }
        IPage iPage = this.sysUserMapper.selectPage(DefaultPageFactory.getPage(), qw);
        return DefaultPageFactory.createReturnPageInfo(iPage);
    }

    @Transactional
    public void saveSysUserRole(String userId, List<String> roleIds) {
        List<SysUserRole> list = roleIds.stream().map(item -> new SysUserRole().setUserId(userId).setRoleId(item)).collect(Collectors.toList());
        sysUserRoleService.saveBatch(list);
    }

    @Transactional
    @Override
    public boolean addOne(SysUser sysUser) {
        // 检查用户名
        LambdaQueryWrapper<SysUser> qw = new LambdaQueryWrapper<>();
        qw.eq(SysUser::getAccount, sysUser.getAccount());
        Integer c = sysUserMapper.selectCount(qw);
        if (c > 0) {
            throw new IllegalArgumentException("登录名不可用");
        }
        sysUser.setPassword(SecurityUtils.encryptPassword(MD5.create().digestHex(sysUser.getPassword())));
        int insert = this.sysUserMapper.insert(sysUser);
        saveSysUserRole(sysUser.getId(), sysUser.getRoleIds());
        return insert > 0;
    }

    @Transactional
    @Override
    public boolean updateOne(SysUser sysUser) {
        int i = this.sysUserMapper.updateById(sysUser.setAccount(null).setPassword(null));
        boolean b = this.sysUserRoleService.remove(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, sysUser.getId()));
        saveSysUserRole(sysUser.getId(), sysUser.getRoleIds());
        return i > 0 && b;
    }

    @Transactional
    @Override
    public boolean deleteUser(List<String> userIdList) {
        userIdList.remove("super");
        int i = sysUserMapper.deleteBatchIds(userIdList);
        LambdaQueryWrapper<SysUserRole> qw = new LambdaQueryWrapper<>();
        qw.in(SysUserRole::getUserId, userIdList);
        boolean b = sysUserRoleService.remove(qw);
        return i > 0 && b;
    }
}
