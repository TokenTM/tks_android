package com.tokentm.sdk.uidemo.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.tokentm.sdk.uidemo.R;
import com.tokentm.sdk.uidemo.databinding.InputIdentityCompanyTxHashDidDialogBinding;
import com.xxf.arch.fragment.XXFDialogFragment;
import com.xxf.arch.utils.ToastUtils;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 查看他人链信认证的时候, 调用
 */
public class InputIdentityAndCompanyParamsDialog extends XXFDialogFragment {

    private final static String LISTENER = "listener";
    InputIdentityCompanyTxHashDidDialogBinding binding;

    public static InputIdentityAndCompanyParamsDialog newInstance(OnInputIdentityCompanyparamsListener listener) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(LISTENER, listener);
        InputIdentityAndCompanyParamsDialog inputCompanyNameFragmentDialog = new InputIdentityAndCompanyParamsDialog();
        inputCompanyNameFragmentDialog.setArguments(bundle);
        return inputCompanyNameFragmentDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = InputIdentityCompanyTxHashDidDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        Bundle arguments = getArguments();
        if (arguments == null) {
            return;
        }
        OnInputIdentityCompanyparamsListener listener = (OnInputIdentityCompanyparamsListener) arguments.getSerializable(LISTENER);
        binding.tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String etIdentityTxHash = binding.etIdentityTxHash.getText().toString().trim();
                String etCompanyTxHash = binding.etCompanyTxHash.getText().toString().trim();
                String etIdentityDid = binding.etIdentityDid.getText().toString().trim();
                String etCompanyDid = binding.etCompanyDid.getText().toString().trim();
                if (!TextUtils.isEmpty(etIdentityTxHash)
                        && !TextUtils.isEmpty(etCompanyTxHash)
                        && !TextUtils.isEmpty(etIdentityDid)
                        && !TextUtils.isEmpty(etCompanyDid) && listener != null) {
                    listener.getIdentityCompanyParams(etIdentityTxHash, etCompanyTxHash, etIdentityDid, etCompanyDid);
                    dismiss();
                } else {
                    ToastUtils.showToast("数据不能为空");
                }

            }
        });

        binding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Window win = getDialog().getWindow();
        FragmentActivity activity = getActivity();
        // 一定要设置Background，如果不设置，window属性设置无效
        if (win != null && activity != null) {
            win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            DisplayMetrics dm = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            WindowManager.LayoutParams params = win.getAttributes();
            params.gravity = Gravity.CENTER;
            // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            win.setAttributes(params);
            win.getAttributes().windowAnimations = R.style.AnimBottomDialog;
        }
    }
}
