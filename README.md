# tokentm Android 服务SDK
##### 服务分类
1.  登陆注册
2.  上链
3.  认证
4.  私有数据管理

##### 编译环境
*  JDK  >=1.8
*  Android SDK  >=21

##### 快速接入
  ```
   implementation('org.web3j:core:3.3.1-android') {
        exclude group: 'com.squareup.okhttp3', module: 'okhttp'
        exclude module: 'rxjava'
    }
    implementation 'com.github.NBXXF:xxf_android_http:2.4.4'
    implementation 'io.reactivex.rxjava2:rxjava:2.2.12'
    implementation 'com.squareup.retrofit2:retrofit:2.6.1'
    implementation 'com.google.code.gson:gson:2.8.5' 
    
   ```
#### 初始化
  ```
//初始化
 CertClient.init(this);
  ```