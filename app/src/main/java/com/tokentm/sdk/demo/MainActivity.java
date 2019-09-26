package com.tokentm.sdk.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.token.card.utils.gm.SM2Signer;
import com.tokentm.sdk.BackupChunkDTO;
import com.tokentm.sdk.CertDataSource;
import com.tokentm.sdk.demo.databinding.ActivityMainBinding;

import org.bouncycastle.jcajce.provider.digest.SM3;
import org.web3j.utils.Numeric;

import java.security.MessageDigest;

import hello.Hello;
import io.reactivex.functions.Consumer;


public class MainActivity extends Activity {


    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }


    private void initView() {
        binding.btLoadGoAar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testArr();
            }
        });
        binding.btLoadSoFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSoFile();
            }
        });
        binding.btBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CertDataSource.getInstance()
                        .backupData(new BackupChunkDTO(null, null, null, null, 0))
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                Log.d("======>", "v:" + aLong);
                            }
                        });
            }
        });
    }

    private void testArr() {
        try {
            String ssss = Hello.greetings("ssss");
            Toast.makeText(this, "res:" + ssss, Toast.LENGTH_LONG).show();
        } catch (Throwable e) {
            e.printStackTrace();
            Toast.makeText(this, "error:" + Log.getStackTraceString(e), Toast.LENGTH_LONG).show();
        }
    }

    private void loadSoFile() {
        try {
            final String privateKey = "8e10e5f4806902afad620e720c0c84c5ce9d741f94c3072a090b1d1e52c07055";

            byte[] rlpBytes = "Hello".getBytes();
            MessageDigest digest = new SM3.Digest();
            digest.update(rlpBytes);
            rlpBytes = digest.digest();
            String message = Numeric.toHexString(rlpBytes);

            SM2Signer sm2Signer = new SM2Signer("crypto");

            SM2Signer.Signature signature = sm2Signer.signature(rlpBytes, Numeric.hexStringToByteArray(privateKey));
            System.out.println("=====>" + signature.getRHex());
            System.out.println("======>" + signature.getSHex());
            Toast.makeText(this, "yes", Toast.LENGTH_LONG).show();
            // NativeGoCrypto.INSTANCE.C_Sign(new GoString(message), new GoString(privateKey));

            //System.out.println(Numeric.toHexString(sm2Signer.privateKeyToPublicKey(privateKey)));
        } catch (Throwable e) {
            e.printStackTrace();
            Toast.makeText(this, "error:" + Log.getStackTraceString(e), Toast.LENGTH_LONG).show();
        }
    }


}
