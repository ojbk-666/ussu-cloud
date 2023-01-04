package cc.ussu.modules.sheep.service.impl;

import cc.ussu.modules.sheep.entity.JdDayBeans;
import cc.ussu.modules.sheep.mapper.JdDayBeansMapper;
import cc.ussu.modules.sheep.service.IJdDayBeansService;
import cc.ussu.modules.sheep.task.jd.util.JdCkWskUtil;
import cc.ussu.modules.sheep.task.jdbeans.service.JdBeansService;
import cc.ussu.modules.sheep.task.jdbeans.vo.TodayBeanVO;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.dynamic.datasource.annotation.Master;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 每日京豆收支 服务实现类
 * </p>
 *
 * @author mp-generator
 * @since 2022-04-01 11:13:57
 */
@Master
@Service
public class JdDayBeansServiceImpl extends ServiceImpl<JdDayBeansMapper, JdDayBeans> implements IJdDayBeansService {

    public static final String JD_COOKIE = "JD_COOKIE";

    @Autowired
    private JdBeansService jdBeansService;

    @Override
    public void updateRecentBeans(String ck) {
        // 提取pin
        String pin = JdCkWskUtil.getPinByCk(ck);
        // 查询今日资产
        TodayBeanVO todayBeanVo = jdBeansService.todayBean(ck);
        updateRecentBeans(todayBeanVo, pin);
    }

    @Override
    public synchronized void updateRecentBeans(TodayBeanVO todayBeanVo, String pin) {
        if (todayBeanVo != null) {
            // 更新数据库记录
            BigDecimal income = todayBeanVo.getIncome();
            BigDecimal out = todayBeanVo.getOut();
            BigDecimal incomeYesterday = todayBeanVo.getIncomeYesterday();
            BigDecimal outYesterday = todayBeanVo.getOutYesterday();
            JdDayBeans jdDayBeans = new JdDayBeans().setIncomeBean(income.intValue()).setOutBean(out.intValue()).setJdUserId(pin);
            // 更新今日记录
            JdDayBeans todayRecord = getTodayRecord(pin);
            if (todayRecord == null) {
                // 新增
                super.save(jdDayBeans.setCreateDate(new Date()));
            } else {
                super.updateById(jdDayBeans.setId(todayRecord.getId()));
            }
            // 昨日记录更新
            if (0 != incomeYesterday.intValue()) {
                JdDayBeans jdDayBeansYesterday = new JdDayBeans().setIncomeBean(incomeYesterday.intValue()).setOutBean(outYesterday.intValue()).setJdUserId(pin);
                JdDayBeans yesterdayRecord = getYesterdayRecord(pin);
                if (yesterdayRecord == null) {
                    // 新增
                    super.save(jdDayBeansYesterday.setCreateDate(DateUtil.yesterday()));
                } else {
                    super.updateById(jdDayBeansYesterday.setId(yesterdayRecord.getId()));
                }
            }
        }
    }

    private JdDayBeans getTodayRecord(String pin) {
        if (StrUtil.isBlank(pin)) {
            return null;
        }
        Date now = new Date();
        return SpringUtil.getBean(IJdDayBeansService.class)
                .getOne(Wrappers.lambdaQuery(JdDayBeans.class).eq(JdDayBeans::getJdUserId, pin)
                        .between(JdDayBeans::getCreateDate, DateUtil.beginOfDay(now), DateUtil.endOfDay(now)));
    }

    private JdDayBeans getYesterdayRecord(String pin) {
        if (StrUtil.isBlank(pin)) {
            return null;
        }
        DateTime yesterday = DateUtil.yesterday();
        return SpringUtil.getBean(IJdDayBeansService.class)
                .getOne(Wrappers.lambdaQuery(JdDayBeans.class).eq(JdDayBeans::getJdUserId, pin)
                        .between(JdDayBeans::getCreateDate, DateUtil.beginOfDay(yesterday), DateUtil.endOfDay(yesterday)));
    }

}
