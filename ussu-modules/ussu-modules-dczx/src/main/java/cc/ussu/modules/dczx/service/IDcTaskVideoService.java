package cc.ussu.modules.dczx.service;

import cc.ussu.modules.dczx.entity.DcTaskVideo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 视频信息 服务类
 * </p>
 *
 * @author mp-generator
 * @since 2022-09-02 14:01:34
 */
public interface IDcTaskVideoService extends IService<DcTaskVideo> {

    List<DcTaskVideo> listByCourseVersion(String courseVersion);

    void saveVideoByStudyPlan(String ck, String userid,String courseId);

}
