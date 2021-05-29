package cn.ussu.modules.files.service;

import cn.ussu.common.core.entity.LocalFileVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface SysFileService {

    LocalFileVo uploadFile(MultipartFile file) throws IOException;

}
