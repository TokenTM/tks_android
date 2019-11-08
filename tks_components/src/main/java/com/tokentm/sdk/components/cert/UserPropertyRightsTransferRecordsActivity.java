package com.tokentm.sdk.components.cert;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.tokentm.sdk.components.cert.adapter.UserPropertyRightsTransferRecordsAdapter;
import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.databinding.TksComponentsUserActivityPropertyRightsTransferRecordsBinding;
import com.xxf.view.databinding.statelayout.IStateLayoutVM;
import com.xxf.view.databinding.statelayout.StateLayoutVM;
import com.xxf.view.loading.ViewState;

import io.reactivex.functions.Action;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 物权转移记录
 */
public class UserPropertyRightsTransferRecordsActivity extends BaseTitleBarActivity implements UserPropertyRightsTransferRecordsPresenter {

    TksComponentsUserActivityPropertyRightsTransferRecordsBinding binding;
    UserPropertyRightsTransferRecordsAdapter userCertificationRecordAdapter;

    public IStateLayoutVM stateLayoutVM = new StateLayoutVM(new Action() {
        @Override
        public void run() throws Exception {
            loadData();
        }
    });

    public static Intent getLauncher(Context context) {
        return new Intent(context, UserPropertyRightsTransferRecordsActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TksComponentsUserActivityPropertyRightsTransferRecordsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        loadData();
    }


    private void initView() {
        setTitle("物权转移记录");
        binding.setStateLayoutVM(stateLayoutVM);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(userCertificationRecordAdapter = new UserPropertyRightsTransferRecordsAdapter());
        userCertificationRecordAdapter.registerAdapterDataObserver(new DataChangeAdapterObserver() {
            @Override
            protected void updateUI() {
                stateLayoutVM.setLayoutState(userCertificationRecordAdapter.getDataSize() > 0 ? ViewState.VIEW_STATE_CONTENT : ViewState.VIEW_STATE_EMPTY);
            }
        });
    }

    /**
     * 加载数据
     */
    private void loadData() {
//        TokenTmClient.getService(CertService.class)
//                .getPropertyRightsTransferRecords(null)
//                .compose(XXF.bindToLifecycle(this))
//                .compose(XXF.bindToProgressHud(new ProgressHUDTransformerImpl.Builder(this)))
//                .subscribe(new Consumer<List<UserPropertyRightsTransferRecords>>() {
//                    @Override
//                    public void accept(List<UserPropertyRightsTransferRecords> userPropertyRightsTransferRecords) throws Exception {
//                        userCertificationRecordAdapter.bindData(true,userPropertyRightsTransferRecords);
//                    }
//                });
    }
}