package com.tokentm.sdk.source;

import com.tokentm.sdk.model.NodeServiceEncryptDecryptItem;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 加解密分割服务
 */
public interface NodeEncryptService extends RepoService {


    /**
     * 加密数据
     *
     * @param uDid
     * @param data
     * @param phone
     * @param smsCode
     * @return
     */
    Observable<List<NodeServiceEncryptDecryptItem>> encrypt(String uDid, String data, String phone, String smsCode);


    /**
     * 解密数据
     *
     * @param uDid
     * @param data
     * @param phone
     * @param smsCode
     * @return
     */
    Observable<String> decrypt(String uDid, List<NodeServiceEncryptDecryptItem> data, String phone, String smsCode);
}
