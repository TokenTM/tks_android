package com.tokentm.sdk.model;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 用户物权转移记录
 */
public class UserPropertyRightsTransferRecords {

    /**
     * companyId : string
     * info : string
     * time : 0
     * txHash : string
     * type : string
     * userId : string
     */

    public String companyId;
    public String info;
    public long time;
    public String txHash;
    /**
     * 	string
     * 类型：user_verified(员工认证), employee_verified(员工认证), employee_turnover(员工离职), create_card(创建名片)
     */
    public String type;
    public String userId;
}
