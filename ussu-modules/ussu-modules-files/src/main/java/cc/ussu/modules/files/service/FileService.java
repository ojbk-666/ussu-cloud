package cc.ussu.modules.files.service;

import cc.ussu.system.api.vo.FileVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface FileService {

    FileVO uploadFile(MultipartFile file, String parentPath) throws IOException;

    default List<FileVO> listFile(String directory, String sort, String order) {
        return new ArrayList<>();
    }

}
