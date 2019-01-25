//
//  QDShareResp.h
//  XiangTong
//
//  Created by QuDian on 2018/8/10.
//  Copyright © 2018年 Facebook. All rights reserved.
//

#import <React/RCTEventEmitter.h>
#import <React/RCTBridgeModule.h>

@interface QDShareResp : RCTEventEmitter <RCTBridgeModule>

+ (QDShareResp*) instance;

- (void) sendShareRespMessage:(NSDictionary*)message;

@end
