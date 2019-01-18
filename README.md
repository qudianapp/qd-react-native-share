# qd-react-native-share

## Getting started

`$ npm install @qudian_mobile/qd-react-native-share --save`

### Mostly automatic installation

`$ react-native link @qudian_mobile/qd-react-native-share`

### Manual installation


#### Android

1. Open up `android/app/src/main/java/[...]/MainApplication.java`
    ```java

    import com.qudian.UMQDUtils;

    UMQDUtils.init(this,BuildConfig.UM_SHARE_KEY);
            UMQDUtils.registerPlaform(UMQDUtils.SINA
                    ,BuildConfig.UM_SHARE_ITEM_SINA_KEY
                    ,BuildConfig.UM_SHARE_ITEM_SINA_SECRET
                    ,BuildConfig.UM_SHARE_ITEM_SINA_URL);
            UMQDUtils.registerPlaform(UMQDUtils.WEIXIN
                    ,BuildConfig.UM_SHARE_ITEM_WECHAT_KEY
                    ,BuildConfig.UM_SHARE_ITEM_WECHAT_SECRET
                    ,BuildConfig.UM_SHARE_ITEM_WECHAT_URL);
            UMQDUtils.registerPlaform(UMQDUtils.QQ
                    ,BuildConfig.UM_SHARE_ITEM_QQ_KEY
                    ,BuildConfig.UM_SHARE_ITEM_QQ_SECRET
                    ,BuildConfig.UM_SHARE_ITEM_QQ_URL);
    ```

    Open up `XXReactPackge.java`
    
    ``` java
    import com.qudian.rn.module.QDShareModule;

    list.add(new QDShareModule(reactContext));
    ```

2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':@qudian_mobile_qd-react-native-share'
    project(':@qudian_mobile_qd-react-native-share').projectDir = new File(rootProject.projectDir, '../node_modules/@qudian_mobile/qd-react-native-share/android')
  	```
3. Insert the following lines in `android/gradle.properties`:
    ```
    UM_SHARE_KEY=XXXXX

    UM_SHARE_ITEM_SINA_PLATFORM=0
    UM_SHARE_ITEM_SINA_KEY=XXXXX
    UM_SHARE_ITEM_SINA_SECRET=XXXXX
    UM_SHARE_ITEM_SINA_URL=XXXXX

    UM_SHARE_ITEM_WECHAT_PLATFORM=1
    UM_SHARE_ITEM_WECHAT_KEY=XXXXX
    UM_SHARE_ITEM_WECHAT_SECRET=XXXXX
    UM_SHARE_ITEM_WECHAT_URL=null

    
    UM_SHARE_ITEM_QQ_PLATFORM=4
    UM_SHARE_ITEM_QQ_KEY=XXXXX
    UM_SHARE_ITEM_QQ_SECRET=XXXXX
    UM_SHARE_ITEM_QQ_URL=null
    
    UM_SHARE_TENCENT_KEY=XXXXX
    ```

4. Insert the following lines in `android/app/build.gradle`:
  	```
      android {
        buildTypes {
            release {
                buildConfigField("String", "UM_SHARE_KEY", "\"" + project.UM_SHARE_KEY + "\"")

                buildConfigField("String", "UM_SHARE_ITEM_SINA_KEY", "\"" + project.UM_SHARE_ITEM_SINA_KEY + "\"")
                buildConfigField("String", "UM_SHARE_ITEM_SINA_SECRET", "\"" + project.UM_SHARE_ITEM_SINA_SECRET + "\"")
                buildConfigField("String", "UM_SHARE_ITEM_SINA_URL", "\"" + project.UM_SHARE_ITEM_SINA_URL + "\"")


                buildConfigField("String", "UM_SHARE_ITEM_WECHAT_KEY", "\"" + project.UM_SHARE_ITEM_WECHAT_KEY + "\"")
                buildConfigField("String", "UM_SHARE_ITEM_WECHAT_SECRET", "\"" + project.UM_SHARE_ITEM_WECHAT_SECRET + "\"")
                buildConfigField("String", "UM_SHARE_ITEM_WECHAT_URL", "\"" + project.UM_SHARE_ITEM_WECHAT_URL + "\"")


                buildConfigField("String", "UM_SHARE_ITEM_QQ_KEY", "\"" + project.UM_SHARE_ITEM_QQ_KEY + "\"")
                buildConfigField("String", "UM_SHARE_ITEM_QQ_SECRET", "\"" + project.UM_SHARE_ITEM_QQ_SECRET + "\"")
                buildConfigField("String", "UM_SHARE_ITEM_QQ_URL", "\"" + project.UM_SHARE_ITEM_QQ_URL + "\"")
            }
        }
      }
      dependencies {
        ...
        compile project(':@qudian_mobile_qd-react-native-share')
        ...
      }
  	```

### iOS
```
#TODO
```

## Usage

```javascript
import {shareImageMessage, shareWebpageMessage, SocialPlatform} from '@qudian_mobile/qd-react-native-share';


    renderSharePannel = () => {
        const shareCallback = this._onShareClick;
        const items = [];
        items.push(
            <ShareItem
                image={Images.ICON_WECHAT_SESSION}
                title={'微信'}
                type={SocialPlatform.WechatSession}
                callback={shareCallback}
                key={0}
            />
        );
        items.push(
            <ShareItem
                image={Images.ICON_WECHAT_TIMELINE}
                title={'朋友圈'}
                type={SocialPlatform.WechatTimeLine}
                callback={shareCallback}
                key={1}
            />
        );
        items.push(
            <ShareItem
                image={Images.ICON_QQ}
                title={'QQ'}
                type={SocialPlatform.QQ}
                callback={shareCallback}
                key={3}
            />
        );
        items.push(
            <ShareItem
                image={Images.ICON_QQ_ZONE}
                title={'QQ空间'}
                type={SocialPlatform.Qzone}
                callback={shareCallback}
                key={4}
            />
        );
        return (
            <View style={[{flexDirection: 'row', marginTop: 13 }]}>
                {items}
            </View>
        )
    };


    _onShareClick = (type) => {
        switch (type) {
            case 1: // 微信
                this._traceAdEvent('xxx')
                break;
            case 2: // 朋友圈
                this._traceAdEvent('xxx')
                break;
            case 4: // QQ
                this._traceAdEvent('xxx')
                break;
            case 5: // QQ空间
                this._traceAdEvent('xxx')
                break;
        }
        if (!!this.props.shareInfo) {
            const shareUrl = xxx
            shareWebpageMessage(type, title, text, image, shareUrl);
        }
    };




```

