package com.tokentm.sdk.uidemo.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.tokentm.sdk.uidemo.R;
import com.tokentm.sdk.uidemo.databinding.InputCompanyNameDialogBinding;
import com.tokentm.sdk.uidemo.utils.DisplayUtils;
import com.xxf.arch.fragment.XXFDialogFragment;
import com.xxf.arch.utils.ToastUtils;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 输入公司名称
 */
public class InputCompanyNameFragmentDialog extends XXFDialogFragment {

    private final static String LISTENER = "listener";
    InputCompanyNameDialogBinding binding;

    public static InputCompanyNameFragmentDialog newInstance(OnInputCompanyNameListener listener) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(LISTENER, listener);
        InputCompanyNameFragmentDialog inputCompanyNameFragmentDialog = new InputCompanyNameFragmentDialog();
        inputCompanyNameFragmentDialog.setArguments(bundle);
        return inputCompanyNameFragmentDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = InputCompanyNameDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        Bundle arguments = getArguments();
        if (arguments == null) {
            return;
        }
        OnInputCompanyNameListener listener = (OnInputCompanyNameListener) arguments.getSerializable(LISTENER);
        binding.tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String companyName = binding.etInputCompanyName.getText().toString().trim();
                if ("".equals(companyName)) {
                    ToastUtils.showToast("公司名称不能为空");
                    return;
                }
                if (listener != null) {
                    listener.onInputCompanyName(companyName);
                }
                dismiss();
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
            params.width = DisplayUtils.dip2px(activity, 270);
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            win.setAttributes(params);
            win.getAttributes().windowAnimations = R.style.AnimBottomDialog;
        }
    }
}
