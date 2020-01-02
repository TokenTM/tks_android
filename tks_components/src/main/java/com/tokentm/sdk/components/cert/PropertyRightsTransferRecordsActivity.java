package com.tokentm.sdk.components.cert;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.tokentm.sdk.components.cert.adapter.PropertyRightsTransferRecordsAdapter;
import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.databinding.TksComponentsActivityPropertyRightsTransferRecordsBinding;
import com.tokentm.sdk.model.TransferCommodityActionDTO;
import com.tokentm.sdk.model.TransferInfoDTO;
import com.tokentm.sdk.source.CommodityService;
import com.tokentm.sdk.source.TokenTmClient;
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
public class PropertyRightsTransferRecordsActivity extends BaseTitleBarActivity {

    private static final String KEY_ID = "id";
    private String id;
    private TksComponentsActivityPropertyRightsTransferRecordsBinding binding;
    private PropertyRightsTransferRecordsAdapter userCertificationRecordAdapter;

    public IStateLayoutVM stateLayoutVM = new StateLayoutVM(new Action() {
        @Override
        public void run() throws Exception {
            loadData();
        }
    });

    public static void launch(Context context, String did) {
        context.startActivity(getLauncher(context, did));
    }

    private static Intent getLauncher(Context context, String id) {
        return new Intent(context, PropertyRightsTransferRecordsActivity.class)
                .putExtra(KEY_ID,id );
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TksComponentsActivityPropertyRightsTransferRecordsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        loadData();
    }


    private void initView() {
        setTitle("物权转移记录");
        id = getIntent().getStringExtra(KEY_ID);
        binding.setStateLayoutVM(stateLayoutVM);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(userCertificationRecordAdapter = new PropertyRightsTransferRecordsAdapter());
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

        TokenTmClient.getService(CommodityService.class).getCommodityTransferActionRecords(id)
                .compose(XXF.bindToLifecycle(this))
                .compose(XXF.bindToProgressHud(new ProgressHUDTransformerImpl.Builder(this)))
                .subscribe(new Consumer<List<TransferCommodityActionDTO>>() {
                    @Override
                    public void accept(List<TransferCommodityActionDTO> transferCommodityActionDTOS) throws Exception {
                        userCertificationRecordAdapter.bindData(true, transferCommodityActionDTOS);
                    }
                });

    }
}
