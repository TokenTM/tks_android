package com.tokentm.sdk.components.cert;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.tokentm.sdk.source.TokenTmClient;
import com.tokentm.sdk.components.cert.adapter.UserPropertyRightsTransferRecordsAdapter;
import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.databinding.TksComponentsUserActivityPropertyRightsTransferRecordsBinding;
import com.tokentm.sdk.model.CertificateCommodityActionDTO;
import com.tokentm.sdk.source.CommodityService;
import com.xxf.arch.XXF;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;
import com.xxf.view.databinding.statelayout.IStateLayoutVM;
import com.xxf.view.databinding.statelayout.StateLayoutVM;
import com.xxf.view.loading.ViewState;

import java.util.List;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 物权转移记录
 */
public class UserPropertyRightsTransferRecordsActivity extends BaseTitleBarActivity
        implements UserPropertyRightsTransferRecordsPresenter {

    private static final String KEY_DID = "did";
    private String did;
    private TksComponentsUserActivityPropertyRightsTransferRecordsBinding binding;
    private UserPropertyRightsTransferRecordsAdapter userCertificationRecordAdapter;

    public IStateLayoutVM stateLayoutVM = new StateLayoutVM(new Action() {
        @Override
        public void run() throws Exception {
            loadData();
        }
    });

    public static Intent getLauncher(Context context, String did) {
        return new Intent(context, UserPropertyRightsTransferRecordsActivity.class)
                .putExtra(KEY_DID, did);
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
        did = getIntent().getStringExtra(KEY_DID);
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
        TokenTmClient.getService(CommodityService.class).getActionRecords(did)
                .compose(XXF.bindToLifecycle(this))
                .compose(XXF.bindToProgressHud(new ProgressHUDTransformerImpl.Builder(this)))
                .subscribe(new Consumer<List<CertificateCommodityActionDTO>>() {
                    @Override
                    public void accept(List<CertificateCommodityActionDTO> certificateCommodityActionDTOS) throws Exception {
                        userCertificationRecordAdapter.bindData(true,certificateCommodityActionDTOS);
                    }
                });
    }
}
