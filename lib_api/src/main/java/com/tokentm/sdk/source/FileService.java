package com.tokentm.sdk.source;

import java.io.File;

import io.reactivex.Observable;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 文件存储服务
 */
public interface FileService extends RepoService {
    /**
     * 文件上传
     *
     * @param did
     * @param file
     * @param targetDid 查看权限的did
     * @return 文件id
     */
    Observable<String> upload(String did, File file, String targetDid);
}
