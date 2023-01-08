package cc.ussu.modules.files.service.impl;

import cc.ussu.modules.files.properties.LocalUploadProperties;
import cc.ussu.modules.files.service.SysFileService;
import cc.ussu.system.api.vo.FileVO;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class LocalSysFileServiceImpl implements SysFileService {

    @Autowired
    private LocalUploadProperties localUploadProperties;

    @Override
    public FileVO uploadFile(MultipartFile multipartFile, String directory) throws IOException {
        Assert.isFalse(multipartFile == null || multipartFile.isEmpty(), "文件不能为空");
        directory = StrUtil.blankToDefault(directory, StrUtil.EMPTY);
        directory = StrUtil.replace(directory, "..", StrUtil.EMPTY);
        // 以/结尾
        if (StrUtil.isNotBlank(directory)) {
            directory = StrUtil.addSuffixIfNot(directory, StrUtil.SLASH);
        }
        String relativePath = getRootImagePath() + directory;
        String originalFilename = multipartFile.getOriginalFilename();
        String fileName = originalFilename;
        if (FileUtil.exist(relativePath + fileName)) {
            String dotExt = StrUtil.DOT + FileUtil.extName(originalFilename);
            fileName = UUID.fastUUID().toString(true) + dotExt;
        }
        String absolutePath = relativePath + fileName;
        File file = new File(absolutePath);
        FileUtil.mkParentDirs(file);
        multipartFile.transferTo(file);
        // https://img.seasmall.top/
        long size = multipartFile.getSize();
        String urlPrefix = localUploadProperties.getDomainWithoutSuffixSlash() + localUploadProperties.getLocalFilePrefix() + StrUtil.SLASH;
        return new FileVO().setSize(size).setName(fileName)
            .setLength(FileUtil.readableFileSize(size)).setUpdateTime(new Date())
            .setUrl(urlPrefix + directory + StrUtil.SLASH + fileName);
    }

    /**
     * 获取一个新文件名
     */
    public String getNewFileName(String originalFileName) {
        return UUID.fastUUID().toString(true) + StrPool.DOT + FileUtil.extName(originalFileName);
    }

    private String getRootImagePath() {
        // return StrUtil.appendIfMissing(localUploadProperties.getLocalFilePath(), File.separator);
        return localUploadProperties.getLocalFilePath();
    }

    @Override
    public List<FileVO> listFile(String directory, String sort, String order) {
        List<FileVO> list = new LinkedList<>();
        directory = StrUtil.blankToDefault(directory, StrUtil.EMPTY);
        directory = StrUtil.replace(directory, "..", StrUtil.EMPTY);
        directory = StrUtil.removePrefix(directory, StrUtil.SLASH);
        String absolutePath = getRootImagePath() + directory;
        // 遍历文件
        File[] files = FileUtil.ls(absolutePath);
        // http://localhost:9060/abc/
        String urlPrefix = localUploadProperties.getDomainWithoutSuffixSlash() + localUploadProperties.getLocalFilePrefix() + StrUtil.SLASH;
        for (File file : files) {
            boolean isDirectory = FileUtil.isDirectory(file);
            FileVO fileVo = new FileVO().setName(FileUtil.getName(file))
                .setIsDirectory(isDirectory)
                .setUpdateTime(FileUtil.lastModifiedTime(file));
            if (!isDirectory) {
                long size = FileUtil.size(file);
                fileVo.setLength(FileUtil.readableFileSize(size)).setSize(size);
                String rp = StrUtil.isNotBlank(directory) ? directory + StrUtil.SLASH : StrUtil.EMPTY;
                fileVo.setUrl(urlPrefix + StrUtil.removePrefix(rp + fileVo.getName(), StrUtil.SLASH));
                if (isImg(FileTypeUtil.getType(file))) {
                    fileVo.setThumbnail(fileVo.getUrl());
                }
            }
            list.add(fileVo);
        }
        // 排序
        if (StrUtil.isNotBlank(sort)) {
            boolean isAsc = "asc".equals(order);
            sort = "length".equals(sort) ? "size" : sort;
            List<FileVO> sortList = CollUtil.sortByProperty(list, sort);
            if (isAsc) {
                return sortList;
            } else {
                return CollUtil.reverse(list);
            }
        }
        return list;
    }

    private boolean isImg(String fileType) {
        return "bmpjpgjpegpnggiftif".contains(fileType);
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
