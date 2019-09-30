package com.tokentm.sdk.wallet;


import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.WalletFile;

import java.io.File;

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
     * key store文件内容
     */
    private String keyStoreFileContent;

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
        keyStoreFileContent = new String(FileUtils.readBytes(keyStoreFile));
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
        return keyStoreFileContent;
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
                ", keyStoreFileContent='" + keyStoreFileContent + '\'' +
                ", privateKey='" + privateKey + '\'' +
                ", credentials=" + credentials +
                '}';
    }


}
