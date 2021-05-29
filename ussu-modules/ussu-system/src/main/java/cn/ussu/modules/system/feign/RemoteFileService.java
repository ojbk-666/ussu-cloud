package cn.ussu.modules.system.feign;

import cn.ussu.common.core.constants.ServiceConstants;
import cn.ussu.common.core.entity.LocalFileVo;
import cn.ussu.modules.system.core.config.MultipartSupportConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = ServiceConstants.SERVICE_FILES, configuration = MultipartSupportConfig.class)
public interface RemoteFileService {

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    LocalFileVo upload(@RequestPart("file") MultipartFile file);

}
