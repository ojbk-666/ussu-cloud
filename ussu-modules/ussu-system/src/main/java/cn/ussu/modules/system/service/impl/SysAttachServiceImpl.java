package cn.ussu.modules.system.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.ussu.common.core.constants.RedisConstants;
import cn.ussu.common.datasource.model.vo.ReturnPageInfo;
import cn.ussu.common.redis.service.RedisService;
import cn.ussu.modules.system.core.util.DefaultPageFactory;
import cn.ussu.modules.system.entity.SysAttach;
import cn.ussu.modules.system.mapper.SysAttachMapper;
import cn.ussu.modules.system.service.ISysAttachService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 附件表 服务实现类
 * </p>
 *
 * @author liming
 * @since 2020-05-17
 */
@Service
public class SysAttachServiceImpl extends ServiceImpl<SysAttachMapper, SysAttach> implements ISysAttachService {

    // @Autowired
    // private UssuProperties ussuProperties;
    @Autowired
    private RedisService redisUtil;
    @Autowired
    private SysAttachMapper mapper;

    /**
     * 分页查询
     */
    @Override
    public ReturnPageInfo<SysAttach> findPage(Map param) {
        QueryWrapper<SysAttach> qw = new QueryWrapper<>();
        // 搜索条件
        IPage iPage = this.mapper.findPage(DefaultPageFactory.getPage(), param);
        return DefaultPageFactory.createReturnPageInfo(iPage);
    }

    /*@Override
    @Transactional
    public void addOne(SysAttach obj) {
        this.mapper.insert(obj);
    }*/

    /*@Override
    @Transactional
    public void updateOne(SysAttach obj) {
        this.mapper.updateById(obj);
    }*/

    @Override
    public String findPathById(String attachId) {
        if (StrUtil.isBlank(attachId)) {
            return null;
        }
        String attachPath = ((String) redisUtil.getCacheObject(RedisConstants.SYS_ATTACH_ID_PATH_ + attachId));
        if (StrUtil.isBlank(attachPath)) {
            // 查询并缓存
            SysAttach sysAttach = this.mapper.selectById(attachId);
            attachPath = sysAttach != null ? sysAttach.getFilePath() : null;
            redisUtil.setCacheObject(RedisConstants.SYS_ATTACH_ID_PATH_ + attachId, attachPath, 300L);
        }
        return attachPath;
    }

    @Override
    public SysAttach findByFilePath(String filePath) {
        if (StrUtil.isBlank(filePath)) {
            return null;
        }
        QueryWrapper<SysAttach> qw = new QueryWrapper<>();
        qw.eq("file_path", filePath);
        SysAttach sysAttach = this.mapper.selectOne(qw);
        return sysAttach;
    }

    /*@Override
    public List<Object> diskFilesList(String basePath, String subPath) {
        String uploadRootPath = Kit.getUploadRootPath();
        File tempUploadRotPath = new File(uploadRootPath);
        if (!tempUploadRotPath.exists() || !tempUploadRotPath.isAbsolute()) {
            String property = System.getProperty("user.dir");
            if(uploadRootPath.startsWith(File.separator)) {
                uploadRootPath = property + uploadRootPath;
            } else {
                uploadRootPath = property + File.separator + uploadRootPath;
            }
        }
        if (StrUtil.isBlank(basePath)) basePath = "/";
        String path = uploadRootPath + basePath + subPath;
        if (path.endsWith("//")) path = path.substring(0, path.length()-1);
        File[] ls = FileUtil.ls(path);
        String imageServer = SysParamUtil.get(ParamKeyConstants.IMAGE_SERVER);
        boolean imageServerCanUseFlag = StrUtil.isNotBlank(imageServer);
        String fileDownloadBaseUrl = "";
        List<Object> list = new ArrayList<>();
        for (File f : ls) {
            boolean isDir = f.isDirectory();
            String extName = FileUtil.extName(f);
            String fileName = FileUtil.getName(f);
            int idx = fileName.lastIndexOf("." + extName);
            // 跳过缩略图文件,缩略图不显示在文件列表
            if (!isDir && idx != -1 && fileName.substring(0, idx).endsWith("_scale")) continue;
            String absolutePath = f.getAbsolutePath();
            ChooseFileObject cfo = new ChooseFileObject();
            cfo.setIsDir(isDir);
            cfo.setName(FileUtil.getName(f));
            cfo.setExt(extName);
            cfo.setHasSm(false);
            String relativePath = f.getAbsolutePath().substring(uploadRootPath.length()).replaceAll("\\\\", "/");
            int absPathIdx = absolutePath.lastIndexOf(".");
            // 设置缩略图url
            if (!isDir && absPathIdx != -1) {
                String scaleAbsolutePath = absolutePath.substring(0, absPathIdx) + "_scale." + extName;
                // 是否有缩略图
                if (FileUtil.exist(scaleAbsolutePath)) {
                    cfo.setHasSm(true);
                    cfo.setSmUrl(imageServer + relativePath.substring(0, relativePath.lastIndexOf(".")) + "_scale." + extName);
                }
            }
            cfo.setUrl(imageServer + relativePath);
            if (!isDir) {
                SysAttach sa = this.findByFilePath(absolutePath.substring(uploadRootPath.length()).replaceAll("\\\\", "/"));
                cfo.setAttachPath(relativePath);
                if (sa != null) {
                    cfo.setAttachId(sa.getId());
                    cfo.setAttachPath(sa.getFilePath());
                    cfo.setName(sa.getFileName());
                    cfo.setUrl(imageServer + sa.getFilePath());
                }
            }
            list.add(cfo);
        }
        return list;
    }*/

}
