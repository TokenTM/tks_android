package com.tokentm.sdk.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tokentm.sdk.BuildConfig;
import com.tokentm.sdk.http.DefaultRxHttpCacheDirectoryProvider;
import com.tokentm.sdk.http.GlobalGsonConvertInterceptor;
import com.tokentm.sdk.http.ResponseDTO;
import com.xxf.arch.annotation.BaseUrl;
import com.xxf.arch.annotation.GsonInterceptor;
import com.xxf.arch.annotation.RxHttpCacheProvider;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @author liuboyu  E-mail:545777678@qq.com
 * @Date 2019-06-14
 * @Description 上链相关api
 */

@BaseUrl(BuildConfig.API_URL)
@GsonInterceptor(GlobalGsonConvertInterceptor.class)
@RxHttpCacheProvider(DefaultRxHttpCacheDirectoryProvider.class)
public interface ChainApiService {


    /**
     * 创建DID
     *
     * @param jsonObject
     * @return
     */
    @GET("chain/did/create")
    Observable<ResponseDTO<String>> createDID(@Body JsonObject jsonObject);


    /**
     * 获取 Nonce
     *
     * @param address
     * @return
     */
    @GET("moac/nonce/{address}")
    Observable<ResponseDTO<Long>> getNonce(@Path("address") String address);

    /**
     * 公司上链签名并将法人写入合约
     *
     * @param jsonObject
     * @return
     */
    @POST("moac/chain/company")
    Observable<ResponseDTO<JsonElement>> companySignCredential(@Body JsonObject jsonObject);

    /**
     * 公司上链失败后重写法人合约信息
     *
     * @param jsonObject
     * @return
     */
    @POST("moac/chain/legal_person")
    Observable<ResponseDTO<JsonElement>> legalPersonContractChain(@Body JsonObject jsonObject);

    /**
     * 员工上链签名
     *
     * @param jsonObject
     * @return
     */
    @POST("moac/chain/employee")
    Observable<ResponseDTO<JsonElement>> employeeSignCredential(@Body JsonObject jsonObject);

    /**
     * 员工离职上链签名
     *
     * @param jsonObject
     * @return
     */
    @POST("moac/chain/employee")
    Observable<ResponseDTO<JsonElement>> employeeLeaveSignCredential(@Body JsonObject jsonObject);

    /**
     * 伙伴关系上链签名
     *
     * @param jsonObject
     * @return
     */
    @POST("moac/chain/partner")
    Observable<ResponseDTO<JsonElement>> relationShipSignCredential(@Body JsonObject jsonObject);


    /**
     * 加入组织上链签名
     *
     * @param jsonObject
     * @return
     */
    @POST("moac/chain/org_member")
    Observable<ResponseDTO<JsonElement>> orgMemberSignCredential(@Body JsonObject jsonObject);


    /**
     * 发放证书上链签名
     *
     * @param jsonObject
     * @return
     */
    @POST("moac/chain/certificate/issue")
    Observable<ResponseDTO<JsonElement>> postCertificateSignCredential(@Body JsonObject jsonObject);


    /**
     * 电子合同审批上链签名
     *
     * @param jsonObject
     * @return
     */
    @POST("moac/chain/contract")
    Observable<ResponseDTO<JsonElement>> contractSignCredential(@Body JsonObject jsonObject);

}
