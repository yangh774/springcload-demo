package com.yangh.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * @author "杨航_2211"
 * @Descript {
 * 介绍：
 * }
 * @Project SpringCloud-Alibaba
 * @date 2023/04/12 上午 09:34
 */
public interface UserBalancedecrease {
    // 减少余额
    int userBalanceDecrease(@Param("userName") String userName, @Param("money")String money);
}
