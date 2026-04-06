package com.xmaptech.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Result {

    success(666,"666~~ 操作成功"),
    error(999,"宝宝~~ 系统出错了"),

    add_data(888,"666~~ 宝宝成功保存一条数据"),
    search_data(777,"666~~ 宝宝搜索了 好多数据"),
    search_data_error(444,"数据搜索异常了 咋办呀~~")

    ;


    private final int code;
    private final String msg;
}
