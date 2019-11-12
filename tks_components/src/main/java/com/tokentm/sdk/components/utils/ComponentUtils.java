package com.tokentm.sdk.components.utils;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Lifecycle;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;

import com.tokentm.sdk.TokenTmClient;
import com.tokentm.sdk.components.cert.CompanyCertActivity;
import com.tokentm.sdk.components.cert.CompanyChainCertificationActivity;
import com.tokentm.sdk.components.cert.UserCertByIDCardActivity;
import com.tokentm.sdk.components.cert.UserPropertyRightsTransferRecordsActivity;
import com.tokentm.sdk.components.cert.model.CompanyCertParams;
import com.tokentm.sdk.components.cert.model.UserCertByIDCardParams;
import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.identitypwd.CompanyCertificationDetailsActivity;
import com.tokentm.sdk.components.identitypwd.CompanyCertificationInstructionsActivity;
import com.tokentm.sdk.components.identitypwd.CompanyCompanyEnterpriseCertificationAlertDialog;
import com.tokentm.sdk.components.identitypwd.UserIdentityAuthenticationAlertDialog;
import com.tokentm.sdk.components.identitypwd.UserIdentityPwdDecryptDialog;
import com.tokentm.sdk.components.identitypwd.UserIdentityPwdInputDialog;
import com.tokentm.sdk.components.identitypwd.UserIdentityPwdReSetActivity;
import com.tokentm.sdk.components.identitypwd.UserIdentityPwdSetActivity;
import com.tokentm.sdk.model.CompanyCertResult;
import com.tokentm.sdk.source.IdentityPwdService;
import com.xxf.arch.XXF;
import com.xxf.arch.activity.XXFActivity;
import com.xxf.arch.core.activityresult.ActivityResult;

import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description sdk UI组件工具
 */
public class ComponentUtils {

    /**
     * 启动身份密码设置页面
     *
     * @param activity
     * @param userPhone
     * @param consumer
     */
    @SuppressLint("CheckResult")
    public static void launchUserIdentityPwdActivity(FragmentActivity activity, String userPhone, Consumer<String> consumer) {
        XXF.startActivityForResult(activity,
                UserIdentityPwdSetActivity.getLauncher(activity, userPhone),
                7000)
                .filter(new Predicate<ActivityResult>() {
                    @Override
                    public boolean test(ActivityResult activityResult) throws Exception {
                        return activityResult.isOk();
                    }
                })
                .map(new Function<ActivityResult, String>() {
                    @Override
                    public String apply(ActivityResult activityResult) throws Exception {
                        return activityResult.getData().getStringExtra(BaseTitleBarActivity.KEY_ACTIVITY_RESULT);
                    }
                })
                .take(1)
                .compose(XXF.bindUntilEvent(activity, Lifecycle.Event.ON_DESTROY))
                .compose(XXF.bindToErrorNotice())
                .subscribe(consumer);
    }

    /**
     * 启动重置身份密码页面
     *
     * @param activity
     * @param uDID
     * @param phone
     * @param consumer
     */
    @SuppressLint("CheckResult")
    public static void launchUserIdentityPwdAReSetctivity(FragmentActivity activity, String uDID, String phone, Consumer<Boolean> consumer) {
        XXF.startActivityForResult(activity,
                UserIdentityPwdReSetActivity.getLauncher(activity, uDID, phone),
                7001)
                .filter(new Predicate<ActivityResult>() {
                    @Override
                    public boolean test(ActivityResult activityResult) throws Exception {
                        return activityResult.isOk();
                    }
                })
                .map(new Function<ActivityResult, Boolean>() {
                    @Override
                    public Boolean apply(ActivityResult activityResult) throws Exception {
                        return activityResult.getData().getBooleanExtra(BaseTitleBarActivity.KEY_ACTIVITY_RESULT, false);
                    }
                })
                .take(1)
                .compose(XXF.bindUntilEvent(activity, Lifecycle.Event.ON_DESTROY))
                .compose(XXF.bindToErrorNotice())
                .subscribe(consumer);
    }

    /**
     * UDID 是否可用
     *
     * @param uDID
     * @return
     */
    public static boolean isUDIDAccessible(String uDID) {
        return TokenTmClient.getService(IdentityPwdService.class)
                .isUDIDAccessible(uDID)
                .onErrorReturnItem(false)
                .blockingFirst(false);
    }


    /**
     * 解密UDID
     *
     * @param activity
     * @param uDID
     * @param dialogConsumer
     */
    public static void decryptUDID(FragmentActivity activity, String uDID, BiConsumer<DialogInterface, Boolean> dialogConsumer) {
        new UserIdentityPwdDecryptDialog(activity, uDID, dialogConsumer)
                .show();
    }

    /**
     * show 身份密码输入框dialog
     *
     * @param activity
     * @param uDID
     * @param dialogConsumer
     */
    public static void showIdentityPwdDialog(FragmentActivity activity, String uDID, BiConsumer<DialogInterface, String> dialogConsumer) {
        UserIdentityPwdInputDialog.showUserIdentityPwdInputDialogNoStampAnim(activity, uDID, dialogConsumer)
                .show();
    }

    /**
     * show 企业认证 dialog
     *
     * @param activity
     * @param dialogConsumer
     */
    public static void showCompanyCompanyEnterpriseCertificationAlertDialog(FragmentActivity activity, BiConsumer<DialogInterface, Boolean> dialogConsumer) {
        new CompanyCompanyEnterpriseCertificationAlertDialog(activity, dialogConsumer)
                .show();
    }

    /**
     * show 身份认证 dialog
     *
     * @param activity
     * @param dialogConsumer
     */
    public static void showUserIdentityAuthenticationAlertDialog(FragmentActivity activity, BiConsumer<DialogInterface, Boolean> dialogConsumer) {
        new UserIdentityAuthenticationAlertDialog(activity, dialogConsumer).show();
    }

    /**
     * 启动用户实名认证 页面
     *
     * @param activity
     * @param userCertByIDCardParams
     * @param consumer
     */
    @SuppressLint("CheckResult")
    public static void launchUserCertActivity(FragmentActivity activity, UserCertByIDCardParams userCertByIDCardParams, Consumer<String> consumer) {
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
                .map(new Function<ActivityResult, String>() {
                    @Override
                    public String apply(ActivityResult activityResult) throws Exception {
                        return activityResult.getData().getStringExtra(XXFActivity.KEY_ACTIVITY_RESULT);
                    }
                })
                .take(1)
                .compose(XXF.bindUntilEvent(activity, Lifecycle.Event.ON_DESTROY))
                .compose(XXF.bindToErrorNotice())
                .subscribe(consumer);
    }

    /**
     * 启动公司认证 页面
     *
     * @param activity
     * @param companyCertParams
     * @param consumer
     */
    @SuppressLint("CheckResult")
    public static void launchCompanyCertActivity(FragmentActivity activity, CompanyCertParams companyCertParams, Consumer<CompanyCertResult> consumer) {
        XXF.startActivityForResult(
                activity,
                CompanyCertActivity.getLauncher(
                        activity,
                        companyCertParams
                ),
                7101)
                .filter(new Predicate<ActivityResult>() {
                    @Override
                    public boolean test(ActivityResult activityResult) throws Exception {
                        return activityResult.isOk();
                    }
                })
                .map(new Function<ActivityResult, CompanyCertResult>() {
                    @Override
                    public CompanyCertResult apply(ActivityResult activityResult) throws Exception {
                        return (CompanyCertResult) activityResult.getData().getSerializableExtra(XXFActivity.KEY_ACTIVITY_RESULT);
                    }
                })
                .take(1)
                .compose(XXF.bindUntilEvent(activity, Lifecycle.Event.ON_DESTROY))
                .compose(XXF.bindToErrorNotice())
                .subscribe(consumer);
    }

    /**
     * 启动 物权转移 页面
     *
     * @param activity
     * @param consumer
     */
    @SuppressLint("CheckResult")
    public static void launchUserPropertyRightsTransferRecordsActivity(FragmentActivity activity, String did, Consumer<CompanyCertResult> consumer) {
        XXF.startActivityForResult(
                activity,
                UserPropertyRightsTransferRecordsActivity.getLauncher(activity, did), 7101)
                .filter(new Predicate<ActivityResult>() {
                    @Override
                    public boolean test(ActivityResult activityResult) throws Exception {
                        return activityResult.isOk();
                    }
                })
                .map(new Function<ActivityResult, CompanyCertResult>() {
                    @Override
                    public CompanyCertResult apply(ActivityResult activityResult) throws Exception {
                        return (CompanyCertResult) activityResult.getData().getSerializableExtra(XXFActivity.KEY_ACTIVITY_RESULT);
                    }
                })
                .take(1)
                .compose(XXF.bindUntilEvent(activity, Lifecycle.Event.ON_DESTROY))
                .compose(XXF.bindToErrorNotice())
                .subscribe(consumer);
    }

    /**
     * 启动 认证说明 页面
     *
     * @param activity
     * @param consumer
     */
    @SuppressLint("CheckResult")
    public static void launchCompanyCertificationInstructionsActivity(FragmentActivity activity, String txHash, Consumer<String> consumer) {
        XXF.startActivityForResult(
                activity,
                CompanyCertificationInstructionsActivity.getLauncher(activity, txHash), 7101)
                .filter(new Predicate<ActivityResult>() {
                    @Override
                    public boolean test(ActivityResult activityResult) throws Exception {
                        return activityResult.isOk();
                    }
                })
                .map(new Function<ActivityResult, String>() {
                    @Override
                    public String apply(ActivityResult activityResult) throws Exception {
                        return (String) activityResult.getData().getSerializableExtra(XXFActivity.KEY_ACTIVITY_RESULT);
                    }
                })
                .take(1)
                .compose(XXF.bindUntilEvent(activity, Lifecycle.Event.ON_DESTROY))
                .compose(XXF.bindToErrorNotice())
                .subscribe(consumer);
    }

    /**
     * 启动 认证详情 页面
     *
     * @param activity
     * @param consumer
     */
    @SuppressLint("CheckResult")
    public static void launchUserCertificationDetailsActivity(FragmentActivity activity, String txHash, Consumer<String> consumer) {
        XXF.startActivityForResult(
                activity,
                CompanyCertificationDetailsActivity.getLauncher(activity, txHash), 7101)
                .filter(new Predicate<ActivityResult>() {
                    @Override
                    public boolean test(ActivityResult activityResult) throws Exception {
                        return activityResult.isOk();
                    }
                })
                .map(new Function<ActivityResult, String>() {
                    @Override
                    public String apply(ActivityResult activityResult) throws Exception {
                        return (String) activityResult.getData().getSerializableExtra(XXFActivity.KEY_ACTIVITY_RESULT);
                    }
                })
                .take(1)
                .compose(XXF.bindUntilEvent(activity, Lifecycle.Event.ON_DESTROY))
                .compose(XXF.bindToErrorNotice())
                .subscribe(consumer);
    }

    /**
     * 开启链信认证
     *
     * @param activity
     * @param consumer
     */
    @SuppressLint("CheckResult")
    public static void launchCompanyChainCertificationActivity(FragmentActivity activity, Consumer<CompanyCertResult> consumer) {
        XXF.startActivityForResult(
                activity,
                CompanyChainCertificationActivity.getLauncher(activity), 7101)
                .filter(new Predicate<ActivityResult>() {
                    @Override
                    public boolean test(ActivityResult activityResult) throws Exception {
                        return activityResult.isOk();
                    }
                })
                .map(new Function<ActivityResult, CompanyCertResult>() {
                    @Override
                    public CompanyCertResult apply(ActivityResult activityResult) throws Exception {
                        return (CompanyCertResult) activityResult.getData().getSerializableExtra(XXFActivity.KEY_ACTIVITY_RESULT);
                    }
                })
                .take(1)
                .compose(XXF.bindUntilEvent(activity, Lifecycle.Event.ON_DESTROY))
                .compose(XXF.bindToErrorNotice())
                .subscribe(consumer);
    }
}