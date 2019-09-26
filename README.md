# tokentm Android 服务SDK
#### 特征
  * 利用区块链技术去中心化,不可篡改,可以防伪的特性,SDK提供了区块链的无技术门槛障碍的解决方案,让普通开发者也能享受区块链带来的快感,不仅如此,而且我们平台提供了自己的节点和自己的加密方式,从而提高了大众区块链节点访问的安全性,我们目前SDK主要支持三大特性,区块链访问,信用认证,和私有数据备份;  
  * 区块链访问:如果用传统sdk,你需要理解区块链,什么是上链,什么是私钥,什么是keystore，keystore存储等等一系列问题,而我们提供了类似中心化sdk抽象,让你接入区块链成本和技术门槛不那么高  
  * 信用认证:普通的认证方式,都是依赖人工审核或者手机号+生份证或者其他证件的认证方式,但是只能依赖于某个公司的中心化平台，不具备权威性,而且中心化系统容易作恶,有人员篡改的危险,所以存放在链上是最权威和安全的,这样所有接触区块链的产品都可以直接使用您的认证,从而避免多平台多产品认证  
  * 私有数据备份:如果放在中心化系统上,客户的隐私数据如手机银行卡等等信息都随时可以被泄漏,我们提供了基于区块链技术的私有数据备份,从而保证了客户的隐私和数据的安全性;
  
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
 TokenTmClient.init(this);
  ```