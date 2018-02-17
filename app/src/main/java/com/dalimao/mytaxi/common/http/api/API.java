package com.dalimao.mytaxi.common.http.api;

/**
 * Created by jinny on 2018/2/10.
 */

public class API {

    public static final String TEST_GET         = "/get?uid=${uid}";
    public static final String TEST_POST        = "/post";
    /**
     * 获取验证码
     */
    public static final String GET_SMS_CODE     = "/f34e28da5816433d/getMsgCode?phone=${phone}";
    /**
     * 验证验证码
     */
    public static final String CHECK_SMS_CODE   = "/f34e28da5816433d/checkMsgCode?phone=${phone}&code=${code}";
    /**
     * 检查用户是否存在
     */
    public static final String CHECK_USER_EXIST = "/f34e28da5816433d/isUserExist?phone=${phone}";
    /**
     * 注册
     */
    public static final String REGISTER         = "/f34e28da5816433d/register";

    /**
     * 配置域名信息
     */
    public static class Config {
        /**
         * 测试环境的域名
         */
        private static final String TEST_DOMAIN = "http://cloud.bmob.cn";
        /**
         * 发布环境的域名
         */
        private static final String RELEASE_DOMAIN = "http://cloud.bmob.cn";

        private static final String TEST_APP_ID = "e90928398db0130b0d6d21da7bde357e";
        private static final String RELEASE_APP_ID = "e90928398db0130b0d6d21da7bde357e";
        private static final String TEST_APP_KEY = "514d8f8a2371bdf1566033f6664a24d2";
        private static final String RELEASE_APP_KEY = "514d8f8a2371bdf1566033f6664a24d2";

        /**
         * 定义AppId
         */
        private static String appID = TEST_APP_ID;

        /**
         * 定义AppKey
         */
        private static String appKey = TEST_APP_KEY;

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
            appID = deBug ? TEST_APP_ID : RELEASE_APP_ID;
            appKey = deBug ? TEST_APP_KEY : RELEASE_APP_KEY;
        }

        /**
         * 使用getDomain把域名返回出去
         * @return
         */
        public static String getDomain() {
            return domain;
        }

        public static String getAppID() {
            return appID;
        }

        public static String getAppKey() {
            return appKey;
        }
    }
}
