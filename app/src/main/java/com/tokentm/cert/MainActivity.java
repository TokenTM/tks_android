package com.tokentm.cert;

import android.app.Activity;
import android.os.Bundle;

import com.tokentm.cert.databinding.ActivityMainBinding;

public class MainActivity extends Activity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
