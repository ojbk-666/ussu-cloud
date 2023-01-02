package cc.ussu.modules.dczx.api;

import cc.ussu.modules.dczx.base.BaseDczxController;
import cc.ussu.modules.dczx.entity.DcAnnouncement;
import cc.ussu.modules.dczx.entity.vo.AnnouncementVO;
import cc.ussu.modules.dczx.service.IDcAnnouncementService;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 东财在线 公告
 */
// @Tag(name = "DczcxAnnouncementApiController", description = "东财在线插件公告")
@RestController
@RequestMapping("/api/dczx/announcement")
public class DczcxAnnouncementApiController extends BaseDczxController  {

    @Autowired
    private IDcAnnouncementService announcementService;

    /**
     * 获取公告列表
     */
    // @Operation(description = "获取公告列表")
    @GetMapping
    public List<AnnouncementVO> list() {
        Date now = new Date();
        List<DcAnnouncement> list = announcementService.list(Wrappers.lambdaQuery(DcAnnouncement.class)
                .le(DcAnnouncement::getStartTime, now).ge(DcAnnouncement::getEndTime, now));
        List<AnnouncementVO> voList = list.stream().map(r -> BeanUtil.toBean(r, AnnouncementVO.class)).collect(Collectors.toList());
        return voList;
    }

}
