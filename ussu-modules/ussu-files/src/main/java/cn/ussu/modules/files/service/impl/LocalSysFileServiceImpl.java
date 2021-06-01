package cn.ussu.modules.files.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import cn.ussu.common.core.entity.LocalFileVo;
import cn.ussu.modules.files.properties.LocalUploadProperties;
import cn.ussu.modules.files.service.SysFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocalSysFileServiceImpl implements SysFileService {

    @Autowired
    private LocalUploadProperties localUploadProperties;

    @Override
    public LocalFileVo uploadFile(MultipartFile file, String path) throws IOException {
        // 原文件名称
        String originalFilename = file.getOriginalFilename();
        // 获取一个新文件名
        String newFileName = getNewFileName(originalFilename);
        Date now = new Date();
        String parentPath = null;
        if (path != null) {
            parentPath = path.trim();
        } else {
            // 每日一个文件夹
            parentPath = DateUtil.format(now, DatePattern.PURE_DATE_PATTERN);
        }
        // 移除/ 如果存在
        parentPath = StrUtil.removePrefixIgnoreCase(parentPath, StrPool.SLASH);
        // 文件路径 a/b.jpg
        String relativePath = parentPath + StrPool.SLASH + newFileName;
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

    @Override
    public List<LocalFileVo> listFile(String path) {
        // 处理路径
        if (path == null) {
            path = StrUtil.EMPTY;
        }
        if (!path.startsWith(StrPool.SLASH)) {
            path = StrPool.SLASH + path;
        }
        // List<File> fileList = FileUtil.loopFiles(new File(localUploadProperties.getLocalFilePath() + path), 1, null);
        File[] fileList = FileUtil.ls(localUploadProperties.getLocalFilePath() + path);
        return Arrays.stream(fileList).sorted(sort()).map(item -> {
            boolean isFile = FileUtil.isFile(item);
            String relativePath = FileUtil.getAbsolutePath(item)
                    .substring(localUploadProperties.getLocalFilePath().length())
                    .replaceAll("\\\\", StrPool.SLASH);
            LocalFileVo vo = new LocalFileVo()
                    .setDomain(localUploadProperties.getDomain())
                    .setName(FileUtil.getName(item))
                    .setTimestamp(FileUtil.lastModifiedTime(item).getTime())
                    .setPath(relativePath);
            if (isFile) {
                vo.setTypeFile(FileUtil.getMimeType(vo.getName()))
                        .setSize(FileUtil.size(item))
                        .setOriginalName(vo.getName());
            } else {
                vo.setTypeFolder();
            }
            return vo;
        }).collect(Collectors.toList());
    }

    private Comparator<File> sort() {
        return new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                boolean f1d = FileUtil.isDirectory(o1);
                boolean f2d = FileUtil.isDirectory(o2);
                if (f1d && f2d) {
                    return 0;
                } else if (f1d && !f2d) {
                    return -1;
                } else {
                    return 1;
                }
            }
        };
    }

}
