package com.tokentm.sdk.wallet;


import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.WalletFile;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class WalletResult {

    private ECKeyPair mEcKeyPair;
    private WalletFile mWallet;

    /**
     * 钱包关联的本地文件
     */
    private File keyStoreFile;


    /**
     * 私钥
     */
    private String privateKey;

    /**
     * 钱包凭证
     */
    private Credentials credentials;


    public WalletResult() {
    }


    public WalletResult(ECKeyPair ecKeyPair, WalletFile walletFile, File keyStoreFile) {
        this.mEcKeyPair = ecKeyPair;
        this.mWallet = walletFile;
        this.keyStoreFile = keyStoreFile;
        credentials = Credentials.create(mEcKeyPair);
        privateKey = mEcKeyPair.getPrivateKey().toString(16);
    }


    /**
     * 私钥
     *
     * @return
     */
    public String getPrivateKey() {
        return privateKey;
    }


    /**
     * 读取整个钱包内容
     *
     * @return
     */
    public String getKeyStoreFileContent() {
        //及时查询吧！！！
        return new String(FileUtils.readBytes(keyStoreFile), StandardCharsets.UTF_8);
    }

    /**
     * 读取原始的ECKeyPaire
     *
     * @return
     */
    public ECKeyPair getEcKeyPair() {
        return mEcKeyPair;
    }


    /**
     * 获取凭证
     *
     * @return
     */
    public Credentials getCredentials() {
        return credentials;
    }

    public File getKeyStoreFile() {
        return keyStoreFile;
    }

    @Override
    public String toString() {
        return "WalletResult{" +
                "mEcKeyPair=" + mEcKeyPair +
                ", mWallet=" + mWallet +
                ", keyStoreFile=" + keyStoreFile +
                ", privateKey='" + privateKey + '\'' +
                ", credentials=" + credentials +
                '}';
    }


}
