//
//  QDShareManager.m
//  XiangTong
//
//  Created by shangyuchen on 2018/8/10.
//  Copyright © 2018年 Facebook. All rights reserved.
//

#import "QDShareManager.h"
#import <UMSocialCore/UMSocialCore.h>
#import "QDShareResp.h"

static NSString * const XTShareTypeMessage = @"message";
static NSString * const XTShareTypeWebpage = @"webpage";
static NSString * const XTShareTypeImage = @"image";

@implementation QDShareManager

@synthesize bridge = _bridge;

RCT_EXPORT_MODULE()

- (instancetype) init
{
  self = [super init];
  if (self) {
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(handleOpenURL:) name:@"RCTOpenURLNotification" object:nil];
  }
  return self;
}

- (void) dealloc
{
  [[NSNotificationCenter defaultCenter] removeObserver:self];
}

- (BOOL) handleOpenURL:(NSNotification *)aNotification
{
  NSString * aURLString =  [aNotification userInfo][@"url"];
  NSURL * aURL = [NSURL URLWithString:aURLString];
  return [[UMSocialManager defaultManager] handleOpenURL:aURL];
}

- (dispatch_queue_t) methodQueue
{
  return dispatch_get_main_queue();
}

+ (BOOL) requiresMainQueueSetup
{
  return YES;
}

RCT_EXPORT_METHOD(registerPlaform:(int)platform
                  appkey:(NSString*)appkey
                  appsecret:(NSString*)appsecret
                  redirectURL:(NSString*)redirectURL)
{
  [[UMSocialManager defaultManager] setPlaform:platform
                                        appKey:appkey
                                     appSecret:appsecret
                                   redirectURL:redirectURL];
}

RCT_EXPORT_METHOD(shareMessage:(int)platform
                  text:(NSString*)text)
{
  [self didShare:XTShareTypeMessage platformType:platform title:nil desc:text thumImage:nil image:nil url:nil];
}

RCT_EXPORT_METHOD(shareWebpageMessage:(int)platform
                  title:(NSString*)title
                  desc:(NSString*)desc
                  thumImage:(NSString*)thumImage
                  url:(NSString*)url)
{
  [self didShare:XTShareTypeWebpage platformType:platform title:title desc:desc thumImage:thumImage image:nil url:url];
}

RCT_EXPORT_METHOD(shareImageMessage:(int)platform
                  title:(NSString*)title
                  desc:(NSString*)desc
                  thumImage:(NSString*)thumImage
                  image:(NSString*)image)
{
  [self didShare:XTShareTypeImage platformType:platform title:title desc:desc thumImage:thumImage image:image url:nil];
}

- (void) didShare:(NSString*)shareType
     platformType:(UMSocialPlatformType)platform
            title:(NSString*)title
             desc:(NSString*)desc
        thumImage:(NSString*)thumImage
            image:(NSString*)image
              url:(NSString*)url
{
  UMSocialMessageObject* messageObject = [UMSocialMessageObject messageObject];
  if ([shareType isEqualToString:XTShareTypeMessage]) {
    messageObject.text = desc;
  }
  else if ([shareType isEqualToString:XTShareTypeWebpage]) {
    messageObject.shareObject = [self webpageObject:title desc:desc thumImage:thumImage url:url];
  }
  else if ([shareType isEqualToString:XTShareTypeImage]) {
    messageObject.shareObject = [self imageObject:title desc:desc thumImage:thumImage image:image];
  }
  __weak QDShareManager* weakSelf = self;
  void (^completion) (id, NSError*) = ^ (id result, NSError* error) {
    [weakSelf handleShareResp:result error:error shareType:shareType platform:platform];
  };
  [[UMSocialManager defaultManager] shareToPlatform:platform
                                      messageObject:messageObject
                              currentViewController:nil
                                         completion:completion];
}

- (UMShareWebpageObject*) webpageObject:(NSString*)title
                                   desc:(NSString*)desc
                              thumImage:(NSString*)thumImage
                                    url:(NSString*)url
{
  UMShareWebpageObject* shareObject = [UMShareWebpageObject shareObjectWithTitle:title descr:desc thumImage:thumImage];
  shareObject.webpageUrl = url;
  return shareObject;
}

- (UMShareImageObject*) imageObject:(NSString*)title
                               desc:(NSString*)desc
                          thumImage:(NSString*)thumImage
                              image:(NSString*)image
{
  UMShareImageObject* shareObject = [UMShareImageObject shareObjectWithTitle:title descr:desc thumImage:thumImage];
  shareObject.shareImage = image;
  return shareObject;
}

- (void) handleShareResp:(id)result error:(NSError*)error shareType:(NSString*)shareType platform:(UMSocialPlatformType)platform
{
  int code = 0;
  NSString* message = @"share success";
  NSString* type = @"WebpageMessageResp";
  if (shareType == XTShareTypeImage) {
    type = @"ImageMessageResp";
  }
  if (error) {
    if (error.userInfo[@"NSLocalizedFailureReason"]) {
      message = error.userInfo[@"NSLocalizedFailureReason"];
    }
    else if (error.userInfo[@"message"]) {
      message = error.userInfo[@"message"];
    }
    else {
      message = @"share failed";
    }
    code = (int)error.code;
  }
  NSMutableDictionary* body = @{@"errCode": @(code)}.mutableCopy;
  body[@"message"] = message;
  body[@"type"] = type;
  // 0:成功 1:失败 2:取消
  if (code != 0) {
    if (code == UMSocialPlatformErrorType_Cancel) {
      code = 2;
    } else {
      code = 1;
    }
  }
  body[@"status"] = @(code);
  body[@"platform"] = @(platform);
  [[QDShareResp instance] sendShareRespMessage:body];
}

@end
