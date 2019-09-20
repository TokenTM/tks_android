package com.tokentm.cert;

import android.app.Application;
import android.content.Context;

import java.util.Objects;

public class CertClient {
    private static Application _application;

    public static void init(Context context) {
        _application = (Application) Objects.requireNonNull(context.getApplicationContext());
    }

    static Application get_application() {
        return _application;
    }
}
