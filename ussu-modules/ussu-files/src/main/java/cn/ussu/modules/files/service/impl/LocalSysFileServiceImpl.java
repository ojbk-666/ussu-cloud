package cn.ussu.modules.files.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.text.StrPool;
import cn.ussu.common.core.entity.LocalFileVo;
import cn.ussu.modules.files.properties.LocalUploadProperties;
import cn.ussu.modules.files.service.SysFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

@Service
public class LocalSysFileServiceImpl implements SysFileService {

    @Autowired
    private LocalUploadProperties localUploadProperties;

    @Override
    public LocalFileVo uploadFile(MultipartFile file) throws IOException {
        // 原文件名称
        String originalFilename = file.getOriginalFilename();
        // 获取一个新文件名
        String newFileName = getNewFileName(originalFilename);
        Date now = new Date();
        // 每日一个文件夹
        String datePath = DateUtil.format(now, DatePattern.PURE_DATE_PATTERN);
        // 文件路径 a/b.jpg
        String relativePath = datePath + StrPool.SLASH + newFileName;
        // 本地文件存储的绝对路径
        File absolutePath = new File(localUploadProperties.getLocalFilePath() + StrPool.SLASH + relativePath);
        // 创建父文件夹
        FileUtil.mkdir(absolutePath);
        // 保存文件
        file.transferTo(absolutePath);
        return new LocalFileVo()
                .setDomain(localUploadProperties.getDomain())
                .setName(newFileName)
                .setOriginalName(originalFilename)
                .setPath(relativePath)
                .setSize(file.getSize())
                .setUrl(localUploadProperties.getDomain() + StrPool.SLASH + relativePath)
                .setTimestamp(now.getTime());
    }

    /**
     * 获取一个新文件名
     */
    public String getNewFileName(String originalFileName) {
        return UUID.fastUUID().toString(true) + StrPool.DOT + FileUtil.extName(originalFileName);
    }

}
