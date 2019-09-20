package com.tokentm.cert;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public interface CertDataSource {

    static CertDataSource getInstance() {
        return CertRepositoryImpl.getInstance();
    }

    /**
     * 备份
     *
     * @param backupChunkDTO
     * @return
     */
    Observable<Long> backupData(BackupChunkDTO backupChunkDTO);


    /**
     * 备份 批量
     *
     * @param backupChunkDTOS
     * @return
     */
    Observable<Long> backupDatas(List<BackupChunkDTO> backupChunkDTOS);


    /**
     * 获取备份
     *
     * @param version
     * @return
     */
    Observable<List<BackupChunkDTO>> getBackupDatas(long version);


    /**
     * 获取备份 按类别查询
     *
     * @param type
     * @param version
     * @return
     */
    Observable<List<BackupChunkDTO>> getBackupDatas(String type, long version);

}
