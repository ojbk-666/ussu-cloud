package cc.ussu.modules.log.controller;

import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.log.api.vo.LogVO;
import cc.ussu.modules.log.es.domain.LogEntity;
import cc.ussu.modules.log.es.mapper.EsLogMapper;
import cn.easyes.starter.config.EasyEsConfigProperties;
import cn.hutool.core.bean.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log")
public class EsLogController extends BaseController {

    @Autowired(required = false)
    private EasyEsConfigProperties easyEsConfigProperties;
    @Autowired(required = false)
    private EsLogMapper esLogMapper;

    /**
     * 保存日志
     */
    @PutMapping
    public JsonResult save(@RequestBody LogVO vo) {
        if (easyEsConfigProperties != null && easyEsConfigProperties.isEnable() && esLogMapper != null) {
            esLogMapper.insert(BeanUtil.toBean(vo, LogEntity.class));
        } else {
            return JsonResult.error("es未启用");
        }
        return JsonResult.ok();
    }

}
