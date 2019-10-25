package com.tokentm.sdk.components.cert;

import android.app.Application;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.xxf.arch.viewmodel.XXFViewModel;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class UserCertByIDCardVM extends XXFViewModel {
    public ObservableField<String> userName = new ObservableField<>();
    public ObservableField<String> userIDCard = new ObservableField<>();
    public ObservableField<String> userIDCardFrontPic = new ObservableField<>();
    public ObservableField<String> userIDCardBackPic = new ObservableField<>();
    public ObservableField<String> userIDCardHandedPic = new ObservableField<>();
    public ObservableBoolean submitable = new ObservableBoolean();
    private Observable.OnPropertyChangedCallback submitableCallback = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            submitable.set(!TextUtils.isEmpty(userName.get())
                    && !TextUtils.isEmpty(userIDCard.get())
                    && !TextUtils.isEmpty(userIDCardFrontPic.get())
                    && !TextUtils.isEmpty(userIDCardBackPic.get())
                    && !TextUtils.isEmpty(userIDCardHandedPic.get()));
        }
    };

    public UserCertByIDCardVM(@NonNull Application application) {
        super(application);
        this.userName.addOnPropertyChangedCallback(submitableCallback);
        this.userIDCard.addOnPropertyChangedCallback(submitableCallback);
        this.userIDCardFrontPic.addOnPropertyChangedCallback(submitableCallback);
        this.userIDCardBackPic.addOnPropertyChangedCallback(submitableCallback);
        this.userIDCardHandedPic.addOnPropertyChangedCallback(submitableCallback);
    }
}
