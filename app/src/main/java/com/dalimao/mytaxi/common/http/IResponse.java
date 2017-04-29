package com.dalimao.mytaxi.common.http;

import java.util.Map;

/**
 * Created by liuguangli on 17/4/24.
 */
public interface IResponse {
    // 状态码
    int getCode();
    // 数据体
    String getData();


}
