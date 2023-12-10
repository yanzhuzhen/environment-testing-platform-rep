package Exmpl.Controller;

import Exmpl.Service.fileService;
import Exmpl.Utils.Result;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

import static Exmpl.Utils.oparateLogUtils.opalog;


@RestController
@RequestMapping("api/oss/file")
public class OSSController {
    @Resource
    private fileService fileService;
    //文件上传
    @PostMapping("/upload")
    public Result upload(MultipartFile file, String module){
        String url = fileService.upload(file,module);
        opalog("上传OSS");
        return Result.ok(url).message("文件上传成功");
    }

    @DeleteMapping("/delete/{url}")
    public Result delete(String url){
        fileService.deleteFile(url);
        opalog("删除OSS");
        return Result.ok().message("已删除上传文件");
    }
}
