package cc.ussu.modules.ecps.order.service;

import cc.ussu.modules.ecps.order.entity.EbOrderLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单日志	Name	Code	Comment	Default Value	Data Type	Length	 服务类
 * </p>
 *
 * @author liming
 * @since 2021-08-03
 */
public interface IEbOrderLogService extends IService<EbOrderLog> {

    EbOrderLog detail(Integer id);

    void add(EbOrderLog p);

    void edit(EbOrderLog p);

    void del(String ids);

}
