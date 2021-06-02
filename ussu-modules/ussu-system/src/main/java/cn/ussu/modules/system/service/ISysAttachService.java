package cn.ussu.modules.system.service;

import cn.ussu.common.datasource.model.vo.ReturnPageInfo;
import cn.ussu.modules.system.entity.SysAttach;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 附件表 服务类
 * </p>
 *
 * @author liming
 * @since 2020-05-17
 */
public interface ISysAttachService extends IService<SysAttach> {

    /**
     * 分页查询
     */
    ReturnPageInfo<SysAttach> findPage(Map param);

    /**
     * 新增
     */
    // void addOne(SysAttach obj);

    /**
     * 修改
     */
    // void updateOne(SysAttach obj);

    /**
     * 附件id查询附件路径，会缓存
     * @param attachId 附件id
     * @return
     */
    String findPathById(String attachId);

    /**
     * 通过文件路径获取附件对象
     */
    SysAttach findByFilePath(String filePath);

    /**
     * 列出文件列表
     * @param basePath 基础路径
     * @param subPath 子文件加
     */
    // List<Object> diskFilesList(String basePath, String subPath);

}
