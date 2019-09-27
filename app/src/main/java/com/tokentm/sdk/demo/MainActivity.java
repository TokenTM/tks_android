package com.tokentm.sdk.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.token.card.utils.gm.SM2Signer;
import com.tokentm.sdk.demo.databinding.ActivityMainBinding;

import org.bouncycastle.jcajce.provider.digest.SM3;
import org.web3j.utils.Numeric;

import java.security.MessageDigest;


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
        binding.btCreateWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WalletActivity.launch(v.getContext());
            }
        });
        binding.btBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void testArr() {
//        try {
//            String ssss = Hello.greetings("ssss");
//            Toast.makeText(this, "res:" + ssss, Toast.LENGTH_LONG).show();
//        } catch (Throwable e) {
//            e.printStackTrace();
//            Toast.makeText(this, "error:" + Log.getStackTraceString(e), Toast.LENGTH_LONG).show();
//        }

      /*  try {
            String helloHx = Sm_crypto.c_Hash256Bysha3("hello");
            System.out.println("=====>" + helloHx);
            Toast.makeText(this, "yes:" + helloHx, Toast.LENGTH_LONG).show();
        } catch (Throwable e) {
            e.printStackTrace();
            Toast.makeText(this, "error:" + Log.getStackTraceString(e), Toast.LENGTH_LONG).show();
        }
*/
        final String privateKey = "8e10e5f4806902afad620e720c0c84c5ce9d741f94c3072a090b1d1e52c07055";

        byte[] rlpBytes = "Hello".getBytes();
        MessageDigest digest = new SM3.Digest();
        digest.update(rlpBytes);
        rlpBytes = digest.digest();
        String message = Numeric.toHexString(rlpBytes);

        com.token.card.utils.gm.SM2Signer sm2Signer = new com.token.card.utils.gm.SM2Signer();

        SM2Signer.Signature signature = sm2Signer.signature(rlpBytes, Numeric.hexStringToByteArray(privateKey));
        System.out.println("=========>" + signature.getRHex());
        System.out.println("=========>" + signature.getSHex());
        // NativeGoCrypto.INSTANCE.C_Sign(new GoString(message), new GoString(privateKey));

        //System.out.println(Numeric.toHexString(sm2Signer.privateKeyToPublicKey(privateKey)));
    }


}
