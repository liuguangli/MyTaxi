package com.dalimao.mytaxi.common.http.api;

/**
 * Created by liuguangli on 17/4/29.
 */

public class API {

    public static final String TEST_GET= "/get?uid=${uid}";
    public static final String TEST_POST = "/post";

    /**
     *  配置域名信息
     */
    public static class Config {
        private static final String TEST_DOMAIN = "http://httpbin.org";
        private static final String RElEASE_DOMAIN = "http://httpbin.org";
        private static String domain = TEST_DOMAIN;

        public static void setDebug(boolean debug) {
            domain = debug ? TEST_DOMAIN : RElEASE_DOMAIN;
        }
        public static String getDomain() {
            return domain;
        }
    }
}
