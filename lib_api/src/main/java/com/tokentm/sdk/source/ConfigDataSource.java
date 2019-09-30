package com.tokentm.sdk.source;

import com.tokentm.sdk.model.SecurityQuestionDTO;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public interface ConfigDataSource extends RepoService {

    /**
     * 获取身份密码模版
     *
     * @return
     */
    Observable<List<SecurityQuestionDTO>> getSecurityQuestionTemplate();
}
