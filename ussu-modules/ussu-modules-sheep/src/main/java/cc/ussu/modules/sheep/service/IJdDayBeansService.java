package cc.ussu.modules.sheep.service;

import cc.ussu.modules.sheep.entity.JdDayBeans;
import cc.ussu.modules.sheep.task.jdbeans.vo.TodayBeanVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 每日京豆收支 服务类
 * </p>
 *
 * @author mp-generator
 * @since 2022-04-01 11:13:57
 */
public interface IJdDayBeansService extends IService<JdDayBeans> {

    void updateRecentBeans(String ck);

    void updateRecentBeans(TodayBeanVO todayBeanVo, String pin);

}
