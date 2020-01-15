package com.tokentm.sdk.components.utils;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Lifecycle;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;

import com.tokentm.sdk.components.cert.CompanyCertActivity;
import com.tokentm.sdk.components.cert.CompanyCertSubmitFileActivity;
import com.tokentm.sdk.components.cert.PropertyRightsTransferRecordsActivity;
import com.tokentm.sdk.components.cert.UserCertByIDCardActivity;
import com.tokentm.sdk.components.cert.model.CompanyCertParams;
import com.tokentm.sdk.components.cert.model.UserCertByIDCardParams;
import com.tokentm.sdk.components.identitypwd.model.BindUDID;
import com.tokentm.sdk.components.identitypwd.model.CertificationResultWrapper;
import com.tokentm.sdk.components.identitypwd.view.ChainCertificationActivity;
import com.tokentm.sdk.components.identitypwd.view.ChainCertificationOtherActivity;
import com.tokentm.sdk.components.identitypwd.view.IdentityAndCompanyCertificationDialog;
import com.tokentm.sdk.components.identitypwd.view.IdentityCertificationNotForceDialog;
import com.tokentm.sdk.components.identitypwd.view.IdentityConfirmActivity;
import com.tokentm.sdk.components.identitypwd.view.IdentityPwdChangeActivity;
import com.tokentm.sdk.components.identitypwd.view.IdentityPwdDecryptActivity;
import com.tokentm.sdk.components.identitypwd.view.IdentityPwdInputDialog;
import com.tokentm.sdk.model.CertUserInfoStoreItem;
import com.tokentm.sdk.model.ChainCertResult;
import com.tokentm.sdk.model.CompanyCertInfoStoreItem;
import com.tokentm.sdk.model.IdentityInfoStoreItem;
import com.tokentm.sdk.source.IdentityService;
import com.tokentm.sdk.source.TokenTmClient;
import com.xxf.arch.XXF;
import com.xxf.arch.activity.XXFActivity;
import com.xxf.arch.core.activityresult.ActivityResult;

import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

import static com.xxf.arch.activity.XXFActivity.KEY_ACTIVITY_RESULT;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description sdk UI组件工具
 */
public class ComponentUtils {

    /**
     * 启动身份确认页面
     * 已记录到doc/勿动
     *
     * @param activity
     * @param userPhone
     * @param consumer  返回uDID
     */
    @SuppressLint("CheckResult")
    public static void launchUserIdentityConfirmActivity(FragmentActivity activity, String userPhone, Consumer<BindUDID> consumer) {
        XXF.startActivityForResult(activity,
                IdentityConfirmActivity.getLauncher(activity, userPhone),
                7000)
                .filter(new Predicate<ActivityResult>() {
                    @Override
                    public boolean test(ActivityResult activityResult) throws Exception {
                        return activityResult.isOk();
                    }
                })
                .map(new Function<ActivityResult, BindUDID>() {
                    @Override
                    public BindUDID apply(ActivityResult activityResult) throws Exception {
                        return (BindUDID) activityResult.getData().getSerializableExtra(KEY_ACTIVITY_RESULT);
                    }
                })
                .take(1)
                .compose(XXF.bindUntilEvent(activity, Lifecycle.Event.ON_DESTROY))
                .compose(XXF.bindToErrorNotice())
                .subscribe(consumer);
    }

    /**
     * 忘记身份密码
     * 已记录到doc/勿动
     *
     * @param activity
     * @param uDID
     */
    public static void launchForgotIdentityPwd(FragmentActivity activity, String uDID, Consumer<Boolean> consumer) {
        TokenTmClient.getService(IdentityService.class)
                .getUDID(uDID)
                .compose(XXF.bindToErrorNotice())
                .subscribe(new Consumer<IdentityInfoStoreItem>() {
                    @Override
                    public void accept(IdentityInfoStoreItem identityInfoStoreItem) throws Exception {
                        XXF.startActivityForResult(
                                activity,
                                IdentityPwdDecryptActivity.getLauncher(activity, identityInfoStoreItem, null),
                                1007)
                                .filter(new Predicate<ActivityResult>() {
                                    @Override
                                    public boolean test(ActivityResult activityResult) throws Exception {
                                        return activityResult.isOk();
                                    }
                                })
                                .take(1)
                                .map(new Function<ActivityResult, Boolean>() {
                                    @Override
                                    public Boolean apply(ActivityResult activityResult) throws Exception {
                                        return activityResult.getData().getBooleanExtra(KEY_ACTIVITY_RESULT, false);
                                    }
                                })
                                .subscribe(consumer);
                    }
                });
    }

    /**
     * 修改身份密码
     * 已记录到doc/勿动
     */
    public static void launchChangeIdentityPwd(FragmentActivity activity, String did, Consumer<Boolean> consumer) {
        XXF.startActivityForResult(activity,
                IdentityPwdChangeActivity.getLauncher(
                        activity,
                        did), 2001)
                .filter(new Predicate<ActivityResult>() {
                    @Override
                    public boolean test(ActivityResult activityResult) throws Exception {
                        return activityResult.isOk();
                    }
                })
                .take(1)
                .map(new Function<ActivityResult, Boolean>() {
                    @Override
                    public Boolean apply(ActivityResult activityResult) throws Exception {
                        return activityResult.getData().getBooleanExtra(KEY_ACTIVITY_RESULT, false);
                    }
                })
                .subscribe(consumer);
    }


    /**
     * show 身份密码输入框dialog
     * 已记录到doc/勿动
     *
     * @param activity
     * @param uDID
     * @param dialogConsumer
     */
    public static void showIdentityPwdDialog(FragmentActivity activity, String uDID, BiConsumer<DialogInterface, String> dialogConsumer) {
        IdentityPwdInputDialog.showUserIdentityPwdInputDialogNoStampAnim(activity, uDID, dialogConsumer)
                .show();
    }

    /**
     * show 身份密码输入框带盖章动画dialog
     *
     * @param activity
     * @param uDID
     * @param dialogConsumer
     */
    public static void showIdentityPwdDialogWithStampAnim(FragmentActivity activity, String uDID, BiConsumer<DialogInterface, String> dialogConsumer) {
        IdentityPwdInputDialog.showUserIdentityPwdInputDialogWithStampAnim(activity, uDID, dialogConsumer)
                .show();
    }

    /**
     * show 身份认证和企业认证 dialog  非强制
     * 已记录doc
     *
     * @param activity
     */
    public static void showIdentityAndCompanyCertificationDialogNotForce(FragmentActivity activity, UserCertByIDCardParams userCertByIDCardParams, CompanyCertParams companyCertParams, Consumer<CertificationResultWrapper> certificationResultParamsConsumer) {
        new IdentityAndCompanyCertificationDialog(activity, new BiConsumer<DialogInterface, Boolean>() {
            @Override
            public void accept(DialogInterface dialogInterface, Boolean aBoolean) throws Exception {
                //启动身份认证,非强制
                if (aBoolean) {
                    launchUserCertActivity(activity, userCertByIDCardParams, new Consumer<ChainCertResult<CertUserInfoStoreItem>>() {
                        @Override
                        public void accept(ChainCertResult<CertUserInfoStoreItem> chainResult) throws Exception {
                            CertificationResultWrapper.Builder certificationResultParams = new CertificationResultWrapper.Builder();
                            certificationResultParams.setIdentityCertificationResult(chainResult);
                            launchCompanyCertActivity(activity, companyCertParams, new Consumer<ChainCertResult<CompanyCertInfoStoreItem>>() {
                                @Override
                                public void accept(ChainCertResult<CompanyCertInfoStoreItem> companyCertInfoStoreItemChainCertResult) throws Exception {
                                    certificationResultParams.setCompanyCertificationResult(companyCertInfoStoreItemChainCertResult);
                                    certificationResultParamsConsumer.accept(certificationResultParams.build());
                                }
                            });
                        }
                    });
                }
            }
        }).show();
    }

    /**
     * show 身份认证 dialog  非强制
     * 已记录doc
     *
     * @param activity
     * @param consumer
     */
    public static void showIdentityCertificationDialogNotForce(FragmentActivity activity, UserCertByIDCardParams userCertByIDCardParams, Consumer<ChainCertResult<CertUserInfoStoreItem>> consumer) {
        new IdentityCertificationNotForceDialog(activity, new BiConsumer<DialogInterface, Boolean>() {
            @Override
            public void accept(DialogInterface dialogInterface, Boolean aBoolean) throws Exception {
                //启动身份认证,非强制
                if (aBoolean) {
                    launchUserCertActivity(activity, new UserCertByIDCardParams.Builder(userCertByIDCardParams).build(), consumer);
                }
            }
        }).show();
    }

    /**
     * 启动用户实名认证 页面
     * 已记录doc
     *
     * @param activity
     * @param userCertByIDCardParams
     */
    @SuppressLint("CheckResult")
    public static void launchUserCertActivity(FragmentActivity activity, UserCertByIDCardParams userCertByIDCardParams, Consumer<ChainCertResult<CertUserInfoStoreItem>> consumer) {
        XXF.startActivityForResult(
                activity,
                UserCertByIDCardActivity.getLauncher(activity,
                        userCertByIDCardParams
                ),
                7100)
                .filter(new Predicate<ActivityResult>() {
                    @Override
                    public boolean test(ActivityResult activityResult) throws Exception {
                        return activityResult.isOk();
                    }
                })
                .map(new Function<ActivityResult,ChainCertResult<CertUserInfoStoreItem>>() {
                    @Override
                    public ChainCertResult<CertUserInfoStoreItem> apply(ActivityResult activityResult) throws Exception {
                        return (ChainCertResult<CertUserInfoStoreItem>) activityResult.getData().getSerializableExtra(KEY_ACTIVITY_RESULT);
                    }
                })
                .take(1)
                .compose(XXF.bindUntilEvent(activity, Lifecycle.Event.ON_DESTROY))
                .compose(XXF.bindToErrorNotice())
                .subscribe(consumer);
    }

    /**
     * 启动企业或者组织认证 页面
     * 已记录doc
     *
     * @param activity
     * @param companyCertParams
     * @param consumer
     */
    @SuppressLint("CheckResult")
    public static void launchCompanyCertActivity(FragmentActivity activity, CompanyCertParams companyCertParams, Consumer<ChainCertResult<CompanyCertInfoStoreItem>> consumer) {
        XXF.startActivityForResult(
                activity,
                companyCertParams.getCompanyType() == null
                        ?
                        CompanyCertActivity.getLauncher(activity, companyCertParams)
                        :
                        CompanyCertSubmitFileActivity.getLauncher(activity, companyCertParams),
                7101)
                .filter(new Predicate<ActivityResult>() {
                    @Override
                    public boolean test(ActivityResult activityResult) throws Exception {
                        return activityResult.isOk();
                    }
                })
                .map(new Function<ActivityResult, ChainCertResult<CompanyCertInfoStoreItem>>() {
                    @Override
                    public ChainCertResult<CompanyCertInfoStoreItem> apply(ActivityResult activityResult) throws Exception {
                        return (ChainCertResult<CompanyCertInfoStoreItem>) activityResult.getData().getSerializableExtra(XXFActivity.KEY_ACTIVITY_RESULT);
                    }
                })
                .take(1)
                .compose(XXF.bindUntilEvent(activity, Lifecycle.Event.ON_DESTROY))
                .compose(XXF.bindToErrorNotice())
                .subscribe(consumer);
    }

    /**
     * 启动 物权转移 页面
     * 已记录doc
     *
     * @param activity
     */
    @SuppressLint("CheckResult")
    public static void launchGoodsTransferRecordsActivity(FragmentActivity activity, String id) {
        PropertyRightsTransferRecordsActivity.launch(activity, id);
    }

    /**
     * 查看自己 链信服务
     * 已记录doc
     *
     * @param activity
     * @param uTxHash
     * @param cTxHash
     * @param uDid
     * @param cDid
     * @param consumer
     */
    public static void launchChainCertificationActivity(FragmentActivity activity, String uTxHash, String cTxHash, String uDid, String cDid, Consumer<CertificationResultWrapper> consumer) {
        XXF.startActivityForResult(
                activity,
                ChainCertificationActivity.getLauncher(activity,
                        uTxHash, cTxHash, uDid, cDid),
                1002)
                .filter(new Predicate<ActivityResult>() {
                    @Override
                    public boolean test(ActivityResult activityResult) throws Exception {
                        return activityResult.isOk();
                    }
                })
                .compose(XXF.bindUntilEvent(activity, Lifecycle.Event.ON_DESTROY))
                .take(1)
                .map(new Function<ActivityResult, CertificationResultWrapper>() {
                    @Override
                    public CertificationResultWrapper apply(ActivityResult activityResult) throws Exception {
                        return (CertificationResultWrapper) activityResult.getData().getSerializableExtra(KEY_ACTIVITY_RESULT);
                    }
                })
                .subscribe(consumer);

    }

    /**
     * 查看别人链信服务
     * 已记录doc
     *
     * @param activity
     * @param oTxHash
     * @param cTxHash
     * @param oDid
     * @param cDid
     */
    public static void launchChainCertificationOther(FragmentActivity activity, String oTxHash, String cTxHash, String oDid, String cDid) {
        ChainCertificationOtherActivity.launch(activity,
                oTxHash, cTxHash, oDid, cDid);

    }
}
