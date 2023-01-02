package cc.ussu.modules.dczx.service;

import cc.ussu.modules.dczx.entity.DcTrusteeship;
import cc.ussu.modules.dczx.entity.DcUserStudyPlan;
import cc.ussu.modules.dczx.entity.vo.DcTrusteeshipVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 托管列表 服务类
 * </p>
 *
 * @author mp-generator
 * @since 2022-09-09 20:32:44
 */
public interface IDcTrusteeshipService extends IService<DcTrusteeship> {

    void createTaskAsync(String username, String password, List<DcUserStudyPlan> notFinishedList);

    DcTrusteeship getByUsername(String username);

    void add(DcTrusteeshipVO p);

    void edit(DcTrusteeshipVO p);

    void delIds(List<String> idList);
}
