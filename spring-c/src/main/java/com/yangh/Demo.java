package com.yangh;

import com.yangh.model.WenZhang;
import com.yangh.model.Wenzhang2;
import com.yangh.util.MinioUtil;
import io.minio.Result;
import io.minio.messages.DeleteError;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

/**
 * @author "杨航_2211"
 * @Descript {
 * 介绍：
 * }
 * @Project SpringCloud-Alibaba
 * @date 2023/04/13 上午 10:06
 */
@RestController
public class Demo {
    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    @GetMapping("/jianqian")
    public String demo() {
        Date date = DateUtils.addSeconds(new Date(), 30);
        ScheduledFuture<?> schedule = taskScheduler.schedule(() -> {
            System.out.println("执行定时任务-测试构建10");
        }, date);
        return "{222:222}";
    }

    // 处理文章的发布
    @GetMapping("/jianqian2")
    @InvestmentRequestUtilAnnotate
    public String demo2(WenZhang wenZhang, Wenzhang2 wenzhang2) {
        System.out.println(wenZhang);
        System.out.println(wenzhang2 + "哎呦这个无需过滤");
        return "{222:222}";
    }

    // 依赖注入 或 静态调用都可以
    @Autowired
    private MinioUtil minioUtil;

    @RequestMapping("/uploadImages")
    public Object upload(MultipartFile[] multipartFiles) {
        ArrayList arrayList = minioUtil.uploadImages(multipartFiles,"demo1");
        if (arrayList == null) {
            return "{error:上传失败}";
        }
        return "{success:上传成功共："+arrayList.size()+"个文件";
    }
    // 文件下载
    @RequestMapping("/downloadImages")
    public ResponseEntity downloadImages(String fileName,String bucketName) {
        return minioUtil.downloadFile(fileName, bucketName);
    }
    // 文件删除、支持批量
    @RequestMapping("/removeImages")
    public Iterable<Result<DeleteError>> removeImages(@RequestParam("fileNames") List<String> fileNames, String bucketName) {
       return minioUtil.removeObjects(bucketName, fileNames);
    }

    // 文件访问链接、前端可以通过src 指定网络链接 进行展示
    @RequestMapping("/imagesURL")
    public String imagesURL(String fileName,
                            @RequestParam(defaultValue = "demo1") String bucketName,
                            @RequestParam(defaultValue = "7") Integer expires) {
        return minioUtil.getObjectURL(fileName, bucketName, expires);
    }

    // 测试json 的传参方式
    @RequestMapping("/demoRequest")
    public String imagesURL(@RequestBody WenZhang wenZhang,String biaoti,String neirong){
        return wenZhang + "_" + biaoti + "_" + neirong;
    }
}
