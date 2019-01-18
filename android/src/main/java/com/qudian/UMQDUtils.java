package com.qudian;

import android.content.Context;

import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

public class UMQDUtils {
    public static final int SINA = 0;
    public static final int WEIXIN = 1;
    public static final int WEIXIN_CIRCLE = 2;
    public static final int WEIXIN_FAVORITE = 3;
    public static final int QQ = 4;
    public static final int QZONE = 5;
    public static final int TENCENT = 6;
    public static void init(Context context,String UM_SHARE_KEY){
        UMConfigure.init(context, UM_SHARE_KEY
                , "umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
    }

    public static void registerPlaform(int platformKey, String appKey, String appSecret, String redirectUrl) {
        switch (platformKey) {
            case 0:
                PlatformConfig.setSinaWeibo(appKey, appSecret, redirectUrl);
                break;
            case 1:
                PlatformConfig.setWeixin(appKey, appSecret);
                break;
            case 4:
                PlatformConfig.setQQZone(appKey, appSecret);
                break;
            default:
        }

    }
}
