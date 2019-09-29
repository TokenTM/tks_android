package com.token.card.utils.chain;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Int256;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ContractParams {

    /**
     * 发送者私钥
     */
    private byte[] senderPrvKey;

    /**
     * 发送者地址
     */
    private String senderAddress;

    /**
     * 合约地址
     */
    private String contractAddress;

    /**
     * 合约方法名称
     */
    private String contractMethodName;

    private final List<Type> contractParams = new ArrayList<>();

    private final List<TypeReference<Type>> outParams = new ArrayList<>();

    public byte[] getSenderPrvKey() {
        return senderPrvKey;
    }

    public void setSenderPrvKey(byte[] senderPrvKey) {
        this.senderPrvKey = senderPrvKey;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getContractMethodName() {
        return contractMethodName;
    }

    public void setContractMethodName(String contractMethodName) {
        this.contractMethodName = contractMethodName;
    }

    public List<Type> getContractParams() {
        return contractParams;
    }

    public List<TypeReference<Type>> getOutParams() {
        return outParams;
    }

    /**
     * 顺序新增方法调用参数
     * @param type 参数类型 {@link ParamType}
     * @param value 参数值
     * @return
     */
    public ContractParams addParam(ParamType type, String value) {
        this.contractParams.add(buildType(type, value));
        return this;
    }

    public void setOutParamType(ParamType... paramTypes) {
        if (paramTypes != null && paramTypes.length > 0) {
            for (int i = 0; i < paramTypes.length; ++i) {
                this.outParams.add(buildTypeReference(paramTypes[i]));
            }
        }
    }

    private Type buildType(ParamType type, String value) {
        switch (type) {
            case STRING:
                return new Utf8String(value);
            case BOOL:
                return new Bool(Boolean.valueOf(value));
            case INT256:
                return new Int256(new BigInteger(value, 10));
            case ADDRESS:
                return new Address(value);
            default:
                return new Utf8String(value);
        }
    }

    private TypeReference buildTypeReference(ParamType type) {
        switch (type) {
            case STRING:
                return new TypeReference<Utf8String>() {
                };
            case BOOL:
                return new TypeReference<Bool>() {
                };
            case INT256:
                return new TypeReference<Int256>() {
                };
            case ADDRESS:
                return new TypeReference<Address>() {
                };
            default:
                return new TypeReference<Utf8String>() {
                };
        }
    }

}
