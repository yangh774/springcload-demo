package com.yangh.util;

import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author "杨航_2211"
 * @Descript {
 * 介绍：这是minio的工具类 包含文件上传下
 * }
 * @Project SpringCloud-Alibaba
 * @date 2023/04/29 下午 06:56
 */
@Component
public class MinioUtil {
    // 注意依赖注入不要注入错了
    @Autowired
    private MinioClient minioClient;

    /**
     *
     * @param multipartFile 支持批量上传
     * @param bucketName 需要上传到的捅空间 考虑到文章和用户分类的空间
     * @return 图片的文件名
     */
    public ArrayList uploadImages(MultipartFile[] multipartFile, String bucketName) {
        if (multipartFile == null) {
            return null;
        }
        // 长度是文件的数量
        ArrayList<String> names = new ArrayList<>(multipartFile.length);
        // 遍历图片的个数
        for (MultipartFile file : multipartFile) {
            String filename = file.getOriginalFilename();
            String[] split = filename.split("\\."); //根据 点号 截取文件名和后缀 反斜杠是转义

            // 重新取名 文件名 + _ +时间 + 后缀
            filename = split[0] + "_" + new SimpleDateFormat("yyyy:MM:dd").format(new Date()) + "." + split[1];
//            filename = split[0] + "_" + System.currentTimeMillis() + "." + split[1];
//            filename = UUID.randomUUID().toString() + split[1]; UUID也是常见的名字

            // 开始上传
            InputStream in = null;
            try {
                in = file.getInputStream();
                minioClient.putObject(PutObjectArgs.builder()
                        .bucket(bucketName) // 捅空间
                        .object(filename)   // 文件名
                        .stream(in, file.getSize(), -1) //流、可获得的、大小最后一个？
                        .contentType(file.getContentType()) // 文件类型
                        .build()
                );
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        // 这里可以抛全局异常
                    }
                }
            }
            names.add(filename); //文件名存入集合
        }
        return names; // 返回拥有文件名的集合 数据库存名字就好了
    }

    /**
     * 文件下载
     * @param fileName 文件名
     * @param bucketName 空间名
     */
    public ResponseEntity downloadFile(String fileName,String bucketName) {
        ResponseEntity<byte[]> response = null;
        InputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            in = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(fileName).build());
            out = new ByteArrayOutputStream();
            IOUtils.copy(in, out);

            // 字节数组输出流 转为字节数组
            byte[] bytes = out.toByteArray();
            HttpHeaders httpHeaders = new HttpHeaders(); // 设置头信息
            httpHeaders.add("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            httpHeaders.setContentLength(bytes.length);
            httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            httpHeaders.setAccessControlExposeHeaders(Arrays.asList("*"));

            // 响应内容
            response = new ResponseEntity<byte[]>(bytes, httpHeaders, HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response;
    }

    /**
     * 批量删除文件对象
     * @param bucketName 存储bucket名称
     * @param objects 文件名列表集合
     */
    public Iterable<Result<DeleteError>> removeObjects(String bucketName, List<String> objects) {
        List<DeleteObject> dos = objects.stream().map(e -> new DeleteObject(e)).collect(Collectors.toList());
        Iterable<Result<DeleteError>> results = minioClient.removeObjects(RemoveObjectsArgs.builder().bucket(bucketName).objects(dos).build());
        return results;
    }

    /**
     * 获取文件外链
     *
     * @param objectName 文件名称
     * @param expires    过期时间
     * @param bucketName 空间名称
     * @return url
     * @author 杨航
     */
    public String getObjectURL(String objectName,String bucketName, int expires) {
        try {
            String objectUrl = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs
                    .builder()
                    .bucket(bucketName) // 空间
                    .object(objectName) // 文件名
                    .method(Method.GET) // 请求方式
                    .expiry(expires, TimeUnit.DAYS) // 有效时间
                    .build());
            return objectUrl;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 创建存储bucket
     * @param bucketName 存储bucket名称
     * @return Boolean
     */
    public Boolean makeBucket(String bucketName) {
        try {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 删除存储bucket
     * @param bucketName 存储bucket名称
     * @return Boolean
     */
    public Boolean removeBucket(String bucketName) {
        try {
            minioClient.removeBucket(RemoveBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
