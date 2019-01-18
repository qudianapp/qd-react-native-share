/**
 * 分享
 * Created by 刘俊荣
 */
import {
    NativeModules,
    Platform ,
    NativeEventEmitter,
    DeviceEventEmitter,
} from 'react-native';
import { EventEmitter } from 'events';
import Toast from "@remobile/react-native-toast";

const emitter = new EventEmitter();
let shareManager;
let handler;

if (Platform.OS === 'ios') {
    shareManager = NativeModules.QDShareManager;
    handler = new NativeEventEmitter(NativeModules.QDShareResp);
} else {
    shareManager = NativeModules.QDShareManager;
    handler = DeviceEventEmitter;
}

const SocialPlatform = {
    Sina               : 0, //新浪
    WechatSession      : 1, //微信聊天
    WechatTimeLine     : 2,//微信朋友圈
    WechatFavorite     : 3,//微信收藏
    QQ                 : 4,//QQ聊天页面
    Qzone              : 5,//qq空间
    TencentWb          : 6,//腾讯微博
    Sms                : 13,//短信
    Email              : 14,//邮件
};

handler.addListener('QDShareResponse', resp => {
    emitter.emit('QDShareResponse', resp);
});

/**
 * 分享文本消息
 * @param {SocialPlatform} platform 分享平台
 * @param {String} text 分享文案
 * @returns {Promise<any>}
 */
function shareTextMessage(platform, text) {
    return new Promise((resolve, reject) => {
        shareManager.shareMessage(platform, text);
        emitter.once('QDShareResponse', resp => {
            if (resp.status === 0) {
                Toast.showShortCenter('分享成功');
                resolve(resp);
            } else {
                reject(new ShareError(resp));
            }
        })
    });
}

/**
 * 分享网页消息
 * @param {SocialPlatform} platform 分享平台
 * @param {String} title 分享标题
 * @param {String} desc 描述文案
 * @param {String} thumImage 缩略图链接
 * @param {String} url 网页链接
 * @returns {Promise}
 */
function shareWebpageMessage(platform, title, desc, thumImage, url?='https://same.dabaiqiche.com/act/page/activity/11079') {
    return new Promise((resolve, reject) => {
        shareManager.shareWebpageMessage(platform, title, desc, thumImage, url);
        emitter.once('QDShareResponse', resp => {
            if (resp.status === 0) {
                Toast.showShortCenter('分享成功');
                resolve(resp);
            } else {
                reject(new ShareError(resp));
            }
        })
    });
}

/**
 * 分享图片消息
 * @param {SocialPlatform} platform 分享平台
 * @param {String} title 分享标题
 * @param {String} desc 描述文案
 * @param {String} thumImage 缩略图链接
 * @param {String} image 图片链接
 * @returns {Promise}
 */
function shareImageMessage(platform, title, desc, thumImage, image) {
    return new Promise((resolve, reject) => {
        shareManager.shareImageMessage(platform, title, desc, thumImage, image);
        emitter.once('QDShareResponse', resp => {
            if (resp.status === 0) {
                Toast.showShortCenter('分享成功');
                resolve(resp);
            } else {
                reject(new ShareError(resp));
            }
        })
    });
}

class ShareError extends Error {
    constructor(resp) {
        const message = resp.status || resp.status.toString();
        super(message);
        this.name = 'ShareError';
        this.code = resp.status;

        // avoid babel's limition about extending Error class
        // https://github.com/babel/babel/issues/3083
        if (typeof Object.setPrototypeOf === 'function') {
            Object.setPrototypeOf(this, ShareError.prototype);
        } else {
            this.__proto__ = ShareError.prototype;
        }
    }
}

export {
    shareTextMessage,
    shareWebpageMessage,
    shareImageMessage,
    SocialPlatform,
}
