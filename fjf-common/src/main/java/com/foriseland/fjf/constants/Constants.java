package com.foriseland.fjf.constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Administrator
 * @ Created by Administrator on 2014/11/24.
 */
public class Constants {
    /**
     * 有效状态
     */
    public static final Short STATUS_OPEN = 1;
    
    /**
     * 有效状态
     */
    public static final Short STATUS_CLOSE = 0;
    
    /**
     * 有效状态
     */
    public static final Short STATUS_DELETE = -1;

    /**
     * 重置密码
     */
    public static final String RESET_PWD = "123456";


    public static final int INVITATION_NOT_USED = 1;

    public static final int INVITATION_USED = 2;
    
    public class QDResponse {
    	
        /**
         * 请求成功,参考标准http返回码 
         */
        public static final int CODE_200 = 200;
		/**
		 * 错误请求 : 请求无效
		 */
		public static final int CODE_400 = 400;	
		/**
		 * 未授权：权限不足
		 */
		public static final int CODE_401 = 401;
		/**
		 * 禁止访问： 要求SSL访问、IP地址被拒绝、链接用户过多
		 */
		public static final int CODE_403 = 403;
		/**
		 * 无法找到请求文件
		 */
		public static final int CODE_404 =404;
        /**
         * 请求失败 ：内部服务器错误
         */
        public static final int CODE_500 = 500;
        /**
         * Service Unavailable错误：拒绝服务
         */
        public static final int CODE_503 = 503;

        /**
         * 成功的描述
         */
        public static final String MESSAGE_200 = "操作成功";

        /**
         * 成功的描述
         */
        public static final String MESSAGE_500 = "操作失败";

    }

    public class UCCacheConstants {

        public static final String CONNECT_NAME = "qd-usercenter";

        public static final String UCENTER_BASE_KEY = "UCENTER_BASE_KEY_";

        public static final String CACHE_KEY_PUSER_BEAN = "CACHE_KEY_PUSER_BEAN_";

        public static final String CACHE_KEY_MENU_BEAN = "CACHE_KEY_MENU_BEAN_";

        public static final String CACHE_KEY_ROLE_BEAN = "CACHE_KEY_ROLE_BEAN_";

    }

    public class QDConfig {
        public static final String IOS_CLIENT = "ios";

        public static final String IOS_APP_VERSION = "IOS_APP_VERSION";

        public static final String IOS_FORCE_UPDATE = "IOS_FORCE_UPDATE";            // ios 强制更新

        public static final String IOS_DOWNLOAD_URL = "IOS_DOWNLOAD_URL";            // ios 下载路径

        public static final String ANDRIOD_CLIENT = "andriod";

        public static final String ANDRIOD_APP_VERSION = "ANDRIOD_APP_VERSION";

        public static final String ANDRIOD_FORCE_UPDATE = "ANDRIOD_FORCE_UPDATE";        // andriod强制更新

        public static final String ANDRIOD_DOWNLOAD_URL = "ANDRIOD_DOWNLOAD_URL";        // andriod下载路径

        public static final String SUPPORT_CITIES = "SUPPORT_CITIES";
    }


    public static final String LIST = "List";

    /**
     * utf8编码
     */
    public static final String CODE_UTF8 = "UTF-8";

    /**
     * MD5编码
     */
    public static final String CODE_MD5 = "MD5";

    /**
     * 竖线分隔符[|]
     */
    public static final String VERTICALLINE = "|";

    /**
     * 逗号分隔符 [,]
     */
    public static final String COMMA = ",";

    /**
     * 斜线分隔符[/]
     */
    public static final String SLASH = "/";

    /**
     * 等号分割符[=]
     */
    public static final String EQUAL = "=";

    /**
     * 竖线分隔符的正则表达式
     */
    public static final String VERTICALLINE_REGEX = "\\|";

    /**
     * 短信服务
     */
    public class SmsConfig {
        public static final String URL = "http://api.taovip.com/v1/sms/send.json";

        public static final String TPL_URL = "http://api.taovip.com/v1/sms/tpl_send.json";

        public static final String KEY = "297340669946d1e09448bd33f1fbab8d";
    }

    /**
     * A-Z的不可变集合
     */
    public static final List<String> UPPER_LETTER = Collections.unmodifiableList(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
            "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"));

    public static final int POLLING_INTERVAL_TIME = 15;
    
	public class QDCookie {

		public static final String Cookie_WX_OpenID 										= "wxOpenID";
		public static final String Cookie_WX_PublicsHash 									= "wxPublicsHash";	
		public static final String Cookie_WX_Potential 										= "wxPotential";	
		public static final String Cookie_User 												= "user";
		public static final String Cookie_WX_PotentialID 									= "wxPotentialID";
		public static final String Cookie_WX_Return_Url										="url";
		public static final String Cookie_WX_Return_Code 									= "code"; 
		public static final String Cookie_WX_Return_Code_Param 								= "codeParam"; 
		public static final String Cookie_Target_Url 										= "targetUrl";
		public static final String Cookie_Union_Id 											= "unionId";
		


	    public static final String COOKIE_HOME_NAME 										= "arthurtest";   // cookie name
//	    public static final String COOKIE_DOMAIN ="localhost" ;  // cookie's domain
	    public static final String USER_NAME 												= "userName";
	    public static final String USER_ID 													= "userId";
	    public static final String CURRENT_VISITOR 											="currentVisitor";
	    public static final String Refer													="refer";
		public static final String ZONGHE_FLAG 												= "zonghe_flag";


	}
    
}
