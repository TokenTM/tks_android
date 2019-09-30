package com.token.card.utils.chain;

/**
 * 上链确认结果
 */
public class ConfirmResult {

    /**
     * 交易是否执行成功
     */
    private final boolean success;

    /**
     * 交易是否达到确认条件
     */
    private final boolean confirmed;

    /**
     * 当前区块高度
     */
    private final long currentBlockNum;

    /**
     * 交易所在的区块高度
     */
    private final long transactionBlockNum;

    /**
     * 确认交易数
     */
    private final int threshold;

    public ConfirmResult(boolean success, boolean confirmed, long currentBlockNum, long transactionBlockNum, int threshold) {
        this.success = success;
        this.confirmed = confirmed;
        this.currentBlockNum = currentBlockNum;
        this.transactionBlockNum = transactionBlockNum;
        this.threshold = threshold;
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public long getCurrentBlockNum() {
        return currentBlockNum;
    }

    public long getTransactionBlockNum() {
        return transactionBlockNum;
    }

    public int getThreshold() {
        return threshold;
    }
}
