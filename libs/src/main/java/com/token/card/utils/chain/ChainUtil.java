package com.token.card.utils.chain;

import com.token.card.utils.gm.SM2Signer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ChainUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChainUtil.class);

    private ChainUtil() {

    }

    /**
     * 调用智能合约
     *
     * @param rpcUrl    以太坊节点RPC地址，ex. http://127.0.0.1:8545
     * @param sm2Signer 国密签名实例
     * @param params    调用参数信息
     * @return
     * @throws Exception
     */
    public static String invokeContract(String rpcUrl, SM2Signer sm2Signer, ContractParams params) throws Exception {

        LOGGER.info("[invokeContract] rpcUrl: {}, sender: {}, contract: {}, method: {}",
                rpcUrl, params.getSenderAddress(), params.getContractAddress(), params.getContractMethodName());

        Web3j web3j = getWeb3j(rpcUrl);

        List<RlpType> rlpTypeList = new ArrayList<>();
        final int nonce = getNonce(web3j, params.getSenderAddress());
        LOGGER.info("[invokeContract] address: {}, nonce: {}", params.getSenderAddress(), nonce);
        rlpTypeList.add(RlpString.create(BigInteger.valueOf(nonce)));
        rlpTypeList.add(RlpString.create(BigInteger.valueOf(10L)));
        rlpTypeList.add(RlpString.create(BigInteger.valueOf(4000000L)));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(params.getContractAddress())));
        rlpTypeList.add(RlpString.create(BigInteger.ZERO));

        final String encodeFunction = encodeFunction(params);
        LOGGER.info("[invokeContract] encode function: {}", encodeFunction);
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(encodeFunction)));

        byte[] rlpBytes = RlpEncoder.encode(new RlpList(rlpTypeList));
        LOGGER.info("[invokeContract] before sha3Hash256: {}", Numeric.toHexString(rlpBytes));
        rlpBytes = sm2Signer.sha3Hash256(rlpBytes);
        LOGGER.info("[invokeContract] after sha3Hash256: {}", Numeric.toHexString(rlpBytes));

        SM2Signer.Signature signature = sm2Signer.signature(rlpBytes, params.getSenderPrvKey());

        String publicKey = sm2Signer.privateKeyToPublicKeyHex(params.getSenderPrvKey());
        LOGGER.info("[invokeContract] public key: {}, r: {}, s: {}",
                publicKey, signature.getRHex(), signature.getSHex());

        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(publicKey)));
        rlpTypeList.add(RlpString.create(BigInteger.ZERO));
        rlpTypeList.add(RlpString.create(signature.getR()));
        rlpTypeList.add(RlpString.create(signature.getS()));

        rlpBytes = RlpEncoder.encode(new RlpList(rlpTypeList));
        String rawTransaction = Numeric.toHexString(rlpBytes);
        LOGGER.info("[invokeContract] raw transaction: {}", rawTransaction);

        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(rawTransaction).send();
        if (ethSendTransaction.hasError()) {
            Response.Error error = ethSendTransaction.getError();
            LOGGER.error("[invokeContract] eth send raw transaction error. code: {}, msg: {}, data: {}",
                    error.getCode(), error.getMessage(), error.getData());
            throw new Exception(error.getMessage());
        }

        final String txHash = ethSendTransaction.getTransactionHash();
        LOGGER.info("[invokeContract] transaction hash: {}", txHash);
        return txHash;
    }

    /**
     * 验证交易是否成功
     *
     * @param rpcUrl 以太坊节点RPC地址，ex. http://127.0.0.1:8545
     * @param txHash 交易HASH
     * @return
     */
    public static boolean success(String rpcUrl, String txHash) {
        LOGGER.info("[success] rpcUrl: {}, transaction hash: {}", rpcUrl, txHash);
        EthGetTransactionReceipt txReceipt = null;
        try {
            txReceipt = getWeb3j(rpcUrl).ethGetTransactionReceipt(txHash).send();
        } catch (Exception e) {
            LOGGER.info("[success] request error. rpcUrl: {}, transaction hash: {}", rpcUrl, txHash, e);
        }

        if (txReceipt.getResult() == null) {
            LOGGER.info("[success] result is null.");
            return false;
        }

        if (txReceipt.hasError()) {
            Response.Error error = txReceipt.getError();
            LOGGER.info("[success] response returns error. code: {}, msg: {}, data: {}",
                    error.getCode(), error.getMessage(), error.getData());
            return false;
        }

        return Numeric.toBigInt(txReceipt.getResult().getStatus()).intValue() == 1;
    }

    /**
     * 验证交易是否达到确认条件
     *
     * @param rpcUrl    以太坊节点RPC地址，ex. http://127.0.0.1:8545
     * @param txHash    交易HASH
     * @param threshold 确认数
     * @return
     */
    public static ConfirmResult confirmed(String rpcUrl, String txHash, int threshold) {
        LOGGER.info("[confirmed] rpcUrl: {}, txHash: {}, threshold: {}", rpcUrl, txHash, threshold);

        boolean success = success(rpcUrl, txHash);
        if (!success) {
            LOGGER.info("[confirmed] transaction execute fail. rpcUrl: {}, txHash: {}, threshold: {}", rpcUrl, txHash, threshold);
            return new ConfirmResult(false, false, 0, 0, threshold);
        }

        BigInteger txBlockNum = BigInteger.ZERO;
        BigInteger currentBlockNum = BigInteger.ZERO;
        boolean confirmed = false;
        try {
            org.web3j.protocol.core.methods.response.Transaction transaction = getTransaction(rpcUrl, txHash);
            txBlockNum = transaction.getBlockNumber();
            currentBlockNum = getCurrentBlockNum(rpcUrl);
            confirmed = currentBlockNum.subtract(txBlockNum).intValue() >= threshold;
        } catch (Exception e) {
            LOGGER.error("[confirmed] error. rpcUrl: {}, txHash: {}, threshold: {}",
                    rpcUrl, txHash, threshold, e);
        }
        LOGGER.info("[confirmed] rpcUrl: {}, txHash: {}, threshold: {}, txBlockNum: {}, currentBlockNum: {}, confirmed: {}",
                rpcUrl, txHash, threshold, txBlockNum, currentBlockNum, confirmed);
        return new ConfirmResult(true, confirmed, currentBlockNum.longValue(), txBlockNum.longValue(), threshold);
    }

    public static org.web3j.protocol.core.methods.response.Transaction getTransaction(String rpcUrl, String txHash) throws Exception {
        LOGGER.info("[getTransaction] rpcUrl: {}, txHash: {}", rpcUrl, txHash);
        org.web3j.protocol.core.methods.response.Transaction transaction = getWeb3j(rpcUrl)
                .ethGetTransactionByHash(txHash).send().getResult();
        LOGGER.info("[getTransaction] rpcUrl: {}, txHash: {}, blockHash: {}, blockNum: {}",
                rpcUrl, txHash, transaction.getBlockHash(), transaction.getBlockNumber());
        return transaction;
    }

    /**
     * 获取当前区块高度
     *
     * @param rpcUrl 以太坊节点RPC地址，ex. http://127.0.0.1:8545
     * @return
     * @throws Exception
     */
    public static BigInteger getCurrentBlockNum(String rpcUrl) throws Exception {
        LOGGER.info("[getCurrentBlockNum] rpcUrl: {}", rpcUrl);
        BigInteger blockNumber = getWeb3j(rpcUrl).ethBlockNumber().send().getBlockNumber();
        LOGGER.info("[getCurrentBlockNum] rpcUrl: {}, blockNumber: {}", rpcUrl, blockNumber);
        return blockNumber;
    }

    /**
     * 调用查询类智能合约方法
     *
     * @param rpcUrl 以太坊节点RPC地址，ex. http://127.0.0.1:8545
     * @param params 调用参数信息
     * @return 智能合约的返回值
     * @throws Exception
     */
    public static List<Object> callContract(String rpcUrl, ContractParams params) throws Exception {
        String encodeFunction = encodeFunction(params);

        Web3j web3j = getWeb3j(rpcUrl);
        EthCall ethCall = web3j.ethCall(
                Transaction.createEthCallTransaction(params.getSenderAddress(),
                        params.getContractAddress(), encodeFunction),
                DefaultBlockParameterName.LATEST).send();

        List<Type> typeList = FunctionReturnDecoder.decode(ethCall.getValue(), params.getOutParams());
        if (typeList == null && typeList.isEmpty()) {
            return Collections.emptyList();
        }

        //replace  typeList.stream().map(Type::getValue).collect(Collectors.toList());
        List<Object> result = new ArrayList<>();
        for (Type type : typeList) {
            result.add(type.getValue());
        }
        //return typeList.stream().map(Type::getValue).collect(Collectors.toList());
        return result;
    }

    public static <T> T singleResultCallContract(String rpcUrl, ContractParams params) throws Exception {
        List<Object> resultList = callContract(rpcUrl, params);
        return (T) resultList.get(0);
    }

    public static int getNonce(Web3j web3j, String address) throws Exception {
        EthGetTransactionCount transactionCount = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.PENDING).send();
        return transactionCount.getTransactionCount().intValue();
    }

    public static Web3j getWeb3j(String rpcUrl) {
        Web3j web3j = WEB3J_CACHE.get(rpcUrl);
        if (web3j == null) {
            synchronized (ChainUtil.class) {
                web3j = WEB3J_CACHE.get(rpcUrl);
                if (web3j == null) {
                    web3j = Web3j.build(new HttpService(rpcUrl));
                    WEB3J_CACHE.put(rpcUrl, web3j);
                }
            }
        }
        return web3j;
    }

    private static String encodeFunction(ContractParams params) {
        //List<TypeReference<?>> typeReferenceList = params.getOutParams().stream().collect(Collectors.toList());
        List<TypeReference<?>> typeReferenceList = new ArrayList<>();
        typeReferenceList.addAll(params.getOutParams());

        Function function = new Function(params.getContractMethodName(), params.getContractParams(), typeReferenceList);
        return FunctionEncoder.encode(function);
    }

    private static final Map<String, Web3j> WEB3J_CACHE = new HashMap<>();
}
