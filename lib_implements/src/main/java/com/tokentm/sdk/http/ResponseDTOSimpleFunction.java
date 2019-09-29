package com.tokentm.sdk.http;

import io.reactivex.functions.Function;


/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class ResponseDTOSimpleFunction<T> implements Function<ResponseDTO<T>, T> {
    @Override
    public T apply(ResponseDTO<T> tResponseDTO) throws Exception {
        return tResponseDTO.data;
    }
}
