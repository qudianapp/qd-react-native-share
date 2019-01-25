//
//  QDShareResp.m
//  XiangTong
//
//  Created by QuDian on 2018/8/10.
//  Copyright © 2018年 Facebook. All rights reserved.
//

#import "QDShareResp.h"

@implementation QDShareResp

RCT_EXPORT_MODULE();

+ (QDShareResp*) instance
{
  return [QDShareResp allocWithZone:nil];
}

+ (instancetype) allocWithZone:(struct _NSZone *)zone
{
  static QDShareResp* instance = nil;
  static dispatch_once_t predicate;
  dispatch_once(&predicate, ^{
    instance = [super allocWithZone:zone];
  });
  return instance;
}

- (NSArray*) supportedEvents
{
  return @[@"QDShareResponse"];
}

- (void) sendShareRespMessage:(NSDictionary *)message
{
  if (self.bridge) {
    [self sendEventWithName:@"QDShareResponse" body:message];
  }
}

@end
