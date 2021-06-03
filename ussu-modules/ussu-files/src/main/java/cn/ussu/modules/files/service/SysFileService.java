package cn.ussu.modules.files.service;

import cn.ussu.common.core.model.vo.LocalFileVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface SysFileService {

    LocalFileVo uploadFile(MultipartFile file, String parentPath) throws IOException;

    default List<LocalFileVo> listFile(String path) {
        return null;
    }

}
