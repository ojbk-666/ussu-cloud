package cn.ussu.modules.system.feign;

import cn.ussu.common.core.constants.ServiceConstants;
import cn.ussu.common.core.entity.LocalFileVo;
import cn.ussu.modules.system.core.config.MultipartSupportConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = ServiceConstants.SERVICE_FILES, configuration = MultipartSupportConfig.class)
public interface RemoteFileService {

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    LocalFileVo upload(@RequestPart("file") MultipartFile file, @RequestParam(value = "path", required = false) String path);

    @GetMapping("/list")
    List<LocalFileVo> list(@RequestParam("path") String path);

    @PutMapping
    boolean mkdir(@RequestParam(value = "path", required = false) String path, @RequestParam("name") String name);

    @DeleteMapping
    boolean delete(@RequestParam("path") String path);

    @PostMapping
    public boolean rename(@RequestParam("path") String path, @RequestParam("name") String name);

}
