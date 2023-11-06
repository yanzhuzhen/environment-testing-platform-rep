package Exmpl.Service.Inter;

import org.springframework.web.multipart.MultipartFile;

public interface fileServiceInter {
    String upload(MultipartFile file, String module);
    void deleteFile(String url);
}
