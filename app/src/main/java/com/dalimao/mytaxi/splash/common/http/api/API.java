package com.dalimao.mytaxi.splash.common.http.api;

/**
 * Created by jinny on 2018/2/10.
 */

public class API {

    public static final String TEST_GET = "/get?uid=${uid}";
    public static final String TEST_POST = "/post";

    /**
     * 配置域名信息
     */
    public static class Config {
        /**
         * 测试环境的域名
         */
        private static final String TEST_DOMAIN = "http://httpbin.org";
        /**
         * 发布环境的域名
         */
        private static final String RELEASE_DOMAIN = "http://httpbin.org";
        /**
         * 定义域名
         */
        private static String domain = TEST_DOMAIN;

        /**
         * 通过setDeBug方法来改变域名
         * @param deBug
         */
        public static void setDeBug(boolean deBug) {
            domain = deBug ? TEST_DOMAIN : RELEASE_DOMAIN;
        }

        /**
         * 使用getDomain把域名返回出去
         * @return
         */
        public static String getDomain() {
            return domain;
        }
    }
}
