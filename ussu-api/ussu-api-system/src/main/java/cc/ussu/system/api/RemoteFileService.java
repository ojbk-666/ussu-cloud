package cc.ussu.system.api;

import cc.ussu.common.core.constants.ServiceNameConstants;
import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.vo.LocalFileVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = ServiceNameConstants.SERVICE_FILES/*, configuration = MultipartSupportConfig.class*/)
public interface RemoteFileService {

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    LocalFileVo upload(@RequestPart("file") MultipartFile file, @RequestParam(value = "path", required = false) String path);

    @GetMapping("/list")
    List<LocalFileVo> list(@RequestParam("path") String path);

    @PutMapping
    JsonResult mkdir(@RequestParam(value = "path", required = false) String path, @RequestParam("name") String name);

    @DeleteMapping
    JsonResult delete(@RequestParam("path") String path);

    @PostMapping
    JsonResult rename(@RequestParam("path") String path, @RequestParam("name") String name);

}
