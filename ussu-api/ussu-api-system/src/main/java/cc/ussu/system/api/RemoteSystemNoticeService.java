package cc.ussu.system.api;

import cc.ussu.common.core.constants.ServiceNameConstants;
import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.system.api.vo.SendNoticeVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 远程调用通知
 */
@FeignClient(value = ServiceNameConstants.SERVICE_SYSETM, contextId = "remoteSystemNoticeService")
public interface RemoteSystemNoticeService {

    @PutMapping("/sys-notice/send-notice")
    JsonResult notice(@Validated @RequestBody SendNoticeVO sendNoticeVO);

    @PutMapping("/sys-notice/send-notice-super-admin")
    JsonResult noticeSuperAdmin(@Validated @RequestBody SendNoticeVO sendNoticeVO);

}
