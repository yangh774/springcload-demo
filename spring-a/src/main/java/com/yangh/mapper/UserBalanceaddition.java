package com.yangh.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * @author "杨航_2211"
 * @Descript {
 * 介绍：
 * }
 * @Project SpringCloud-Alibaba
 * @date 2023/04/12 上午 09:24
 */
public interface UserBalanceaddition {
    // 添加余额
    int userBalanceAddition(@Param("userName") String userName,@Param("money")String money);

    // 投资
    int touziSql();
}
