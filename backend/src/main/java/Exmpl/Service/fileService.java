package Exmpl.Service;

import Exmpl.Config.oss.ossProperties;
import Exmpl.Service.Inter.fileServiceInter;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.apache.commons.io.FilenameUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@Transactional
public class fileService implements fileServiceInter {

    @Resource
    ossProperties ossProperties;
    @Override
    public String upload(MultipartFile file, String module) {
        String endPoint =  ossProperties.getEndpoint();
        String keyId =  ossProperties.getKeyId();
        String keySecret =  ossProperties.getKeySecret();
        String bucketName =  ossProperties.getBucketName();
        try {
            //创建OSSClient实列
            OSS ossClient = new OSSClientBuilder().build(endPoint, keyId, keySecret);
            //上传文件流
            InputStream inputStream = file.getInputStream();
            //获取旧名称
            String originalFilename = file.getOriginalFilename();
            //获取后缀
            String extension = FilenameUtils.getExtension(originalFilename);
            //重命名文件
            String newName = UUID.randomUUID().toString().replace("-", "")+"." + extension;
            String date = new DateTime().toString("yyyy/MM/dd");
            newName = module + "/" + date + "/" + newName;
            ossClient.putObject(bucketName,newName,inputStream);
            ossClient.shutdown();
            return "https://" + bucketName + "." + endPoint + "/" + newName;
        }catch (IOException e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void deleteFile(String url) {
        String endPoint =  ossProperties.getEndpoint();
        String keyId =  ossProperties.getKeyId();
        String keySecret =  ossProperties.getKeySecret();
        String bucketName =  ossProperties.getBucketName();
        //创建OSSClient实列
        OSS ossClient = new OSSClientBuilder().build(endPoint, keyId, keySecret);
        String host  ="http://" + bucketName + "." + endPoint ;

        //获取名称
        String name = url.substring(host.length());
        ossClient.deleteObject(bucketName, name);
    }
}
