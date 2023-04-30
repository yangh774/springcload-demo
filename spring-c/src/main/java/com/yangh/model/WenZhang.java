package com.yangh.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.annotation.Documented;

/**
 * @author "杨航_2211"
 * @Descript {
 * 介绍：
 * }
 * @Project SpringCloud-Alibaba
 * @date 2023/04/28 上午 09:26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WenZhang {
    private String biaoti;
    private String neirong;
    private String date;
    // 至于评论、应该是另外的表了、发布文章的时候、没办法传评论的值把

}
