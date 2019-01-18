package com.qudian.rn.module;

import android.util.Log;
import android.widget.Toast;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

/**
 * 分享平台集合
 */
public class QDShareModule extends ReactContextBaseJavaModule {

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
//            Toast.makeText(getCurrentActivity(), "分享成功", Toast.LENGTH_LONG).show();
            reportEventToRN(platform, 0);
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
//            Toast.makeText(getCurrentActivity(), "分享失败" + t.getMessage(), Toast.LENGTH_LONG).show();
            reportEventToRN(platform, 1);
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
//            Toast.makeText(getCurrentActivity(), "取消分享", Toast.LENGTH_LONG).show();
            reportEventToRN(platform, 2);
        }
    };

    /**
     *
     * @param platform
     * @param status 0: 成功, 1: 失败, 2: 取消
     */
    private void reportEventToRN(SHARE_MEDIA platform, int status) {
        WritableMap map = Arguments.createMap();
        map.putInt("platform", deparsePlatform(platform));
        map.putInt("status", status);
        getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit("QDShareResponse", map);
    }

    public QDShareModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "QDShareManager";
    }

    /**
     * 注册分享平台
     *
     * @param platformKey
     * @param appKey
     * @param appSecret
     * @param redirectUrl
     */
    @ReactMethod
    public void registerPlaform(int platformKey, String appKey, String appSecret, String redirectUrl) {
        // 已经在application 中注册过
        /*switch (platformKey) {
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
        }*/

    }

    /**
     * 分享文本消息
     *
     * @param platform
     * @param text
     */
    @ReactMethod
    public void shareTextMessage(int platform, String text) {
        new ShareAction(getCurrentActivity()).withText(text)
                .setPlatform(parsePlatform(platform))
                .setCallback(shareListener).share();
    }

    /**
     * 分享网页消息
     *
     * @param platform
     * @param url
     * @param title
     * @param desc
     * @param thumbImage
     */
    @ReactMethod
    public void shareWebpageMessage(int platform, String title, String desc, String thumbImage, String url) {
        UMWeb web = new UMWeb(url);
        web.setTitle(title);
        web.setThumb(new UMImage(getCurrentActivity(), thumbImage));
        web.setDescription(desc);
        new ShareAction(getCurrentActivity()).withMedia(web)
                .setPlatform(parsePlatform(platform))
                .setCallback(shareListener).share();
    }

    /**
     * 分享图片消息
     *
     * @param platform
     * @param title
     * @param desc
     * @param thumbImage
     * @param image
     */
    @ReactMethod
    public void shareImageMessage(int platform, String title, String desc, String thumbImage, String image) {
        UMImage imageurl = new UMImage(getCurrentActivity(), image);
        imageurl.setThumb(new UMImage(getCurrentActivity(), thumbImage));
        new ShareAction(getCurrentActivity()).withMedia(imageurl)
                .setPlatform(parsePlatform(platform))
                .setCallback(shareListener).share();
    }

    private SHARE_MEDIA parsePlatform(int platformKey) {
        switch (platformKey) {
            case 0:
                return SHARE_MEDIA.SINA;
            case 1:
                return SHARE_MEDIA.WEIXIN;
            case 2:
                return SHARE_MEDIA.WEIXIN_CIRCLE;
            case 3:
                return SHARE_MEDIA.WEIXIN_FAVORITE;
            case 4:
                return SHARE_MEDIA.QQ;
            case 5:
                return SHARE_MEDIA.QZONE;
            case 6:
                return SHARE_MEDIA.TENCENT;
            default:
                return SHARE_MEDIA.WEIXIN;
        }
    }

    private int deparsePlatform(SHARE_MEDIA shareMedia) {
        switch (shareMedia) {
            case SINA:
                return 0;
            case WEIXIN:
                return 1;
            case WEIXIN_CIRCLE:
                return 2;
            case WEIXIN_FAVORITE:
                return 3;
            case QQ:
                return 4;
            case QZONE:
                return 5;
            case TENCENT:
                return 6;
            default:
                    return -1;
        }
    }

    enum Platfrom {
        Sina,
        WechatSession,
        WechatTimeLine,
        WechatFavorite,
        QQ,
        Qzone,
        TencentWb,
        Sms,
        Email,
    }
}
