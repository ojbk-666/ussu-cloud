package cc.ussu.modules.files.service.impl;

import cc.ussu.modules.files.properties.OssAliyunProperties;
import cc.ussu.modules.files.service.FileService;
import cc.ussu.system.api.vo.FileVO;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Primary
@Service
public class OssAliyunFileServiceImpl implements FileService, InitializingBean, DisposableBean {

    @Autowired
    private OssAliyunProperties ossAliyunProperties;
    private static OSS ossClient = null;

    @Override
    public FileVO uploadFile(MultipartFile file, String parentPath) throws IOException {

        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        // String endpoint = ossAliyunProperties.getEndpoint();
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        // String accessKeyId = ossAliyunProperties.getAccessKeyId();
        // String accessKeySecret = ossAliyunProperties.getAccessKeySecret();
        // 填写Bucket名称，例如examplebucket。
        // String bucketName = "examplebucket";
        String bucketName = ossAliyunProperties.getBucket();
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
        // String objectName = "exampledir/exampleobject.txt";
        String objectName = StrUtil.addSuffixIfNot(parentPath, StrUtil.SLASH) + file.getOriginalFilename();
        objectName = StrUtil.removePrefix(objectName, StrUtil.SLASH);

        // 创建OSSClient实例。
        // OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            // 填写字符串。
            // String content = "Hello OSS，你好世界";

            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, new ByteArrayInputStream(file.getBytes()));

            // 如果需要上传时设置存储类型和访问权限，请参考以下示例代码。
            // ObjectMetadata metadata = new ObjectMetadata();
            // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
            // metadata.setObjectAcl(CannedAccessControlList.Private);
            // putObjectRequest.setMetadata(metadata);

            // 设置该属性可以返回response。如果不设置，则返回的response为空。
            putObjectRequest.setProcess("true");
            // 上传字符串。
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            // 如果上传成功，则返回200。
            System.out.println(result.getResponse().getStatusCode());
            FileVO fileVO = new FileVO()
                .setIsDirectory(false).setUpdateTime(new Date()).setName(file.getOriginalFilename())
                .setSize(file.getSize()).setLength(FileUtil.readableFileSize(file.getSize()))
                .setUrl(result.getResponse().getUri());
            return fileVO;
        } catch (OSSException oe) {
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                + "a serious internal problem while trying to communicate with OSS, "
                + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return null;
    }

    /**
     * 列举文件列表
     *
     * https://help.aliyun.com/document_detail/198637.html#section-1mr-uk8-f3k
     */
    @Override
    public List<FileVO> listFile(String directory, String sort, String order) {
        directory = StrUtil.nullToDefault(directory, StrUtil.EMPTY);
        directory = StrUtil.removePrefix(StrUtil.removeSuffix(directory, StrUtil.SLASH), StrUtil.SLASH);
        String bucketName = ossAliyunProperties.getBucket();
        // 设置每页列举200个文件。
        int maxKeys = 1000;
        try {
            // abc/
            String nextMarker = StrUtil.isBlank(directory) ? directory : StrUtil.addSuffixIfNot(directory, StrUtil.SLASH);
            ObjectListing objectListing;
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName).withMarker(directory).withPrefix(nextMarker).withMaxKeys(maxKeys);
            objectListing = ossClient.listObjects(listObjectsRequest);
            LinkedList<FileVO> fileVOList = new LinkedList<>();
            // 文件列表
            List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
            for (OSSObjectSummary sum : sums) {
                String key = sum.getKey();
                String s = StrUtil.removePrefix(key, nextMarker);
                if (StrUtil.isBlank(s) || StrUtil.SLASH.equals(s)) {
                    continue;
                }
                int countSlash = StrUtil.count(s, StrUtil.SLASH);
                if (countSlash < 1) {
                    // 文件
                    fileVOList.add(new FileVO().setName(s).setSize(sum.getSize())
                        .setLength(FileUtil.readableFileSize(sum.getSize())).setIsDirectory(false)
                        .setUpdateTime(sum.getLastModified())
                        .setUrl(convertFileUrl(sum.getKey())));
                } else if (countSlash == 1 && StrUtil.endWith(s, StrUtil.SLASH)) {
                    // 文件夹
                    fileVOList.add(new FileVO().setName(s.substring(0, s.length() -1)).setIsDirectory(true));
                } else if (countSlash > 1) {
                    // 子文件夹
                }
            }
            return fileVOList;
        } catch (OSSException oe) {
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Error Message:" + ce.getMessage());
        }
        return new ArrayList<>();
    }

    private String convertFileUrl(String key) {
        if (StrUtil.isNotBlank(ossAliyunProperties.getCustomDomain())) {
            return ossAliyunProperties.getCustomDomain() + "/" + key;
        }
        return ossAliyunProperties.getEndpoint().replace("https://", "https://" + ossAliyunProperties.getBucket() + ".") + StrUtil.SLASH + key;
    }

    @Override
    public void destroy() throws Exception {
        if (ossClient != null) {
            ossClient.shutdown();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String endpoint = ossAliyunProperties.getEndpoint();
        String accessKeyId = ossAliyunProperties.getAccessKeyId();
        String accessKeySecret = ossAliyunProperties.getAccessKeySecret();
        ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }
}
