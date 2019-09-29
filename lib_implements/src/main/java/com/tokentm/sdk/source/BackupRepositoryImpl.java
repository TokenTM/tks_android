package com.tokentm.sdk.source;

import com.tokentm.sdk.api.IBackApiService;
import com.tokentm.sdk.http.ResponseDTOSimpleFunction;
import com.tokentm.sdk.model.BackupChunkDTO;
import com.xxf.arch.http.XXFHttp;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class BackupRepositoryImpl implements BackupDataSource {
    private static volatile BackupDataSource INSTANCE;

    public static BackupDataSource getInstance() {
        if (INSTANCE == null) {
            synchronized (BackupDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BackupRepositoryImpl();
                }
            }
        }
        return INSTANCE;
    }

    IBackApiService backupApiService;

    private BackupRepositoryImpl() {
        backupApiService = XXFHttp.getApiService(IBackApiService.class);
    }

    @Override
    public Observable<Long> backupData(BackupChunkDTO backupChunkDTO) {
        return backupApiService
                .backupData(backupChunkDTO)
                .map(new ResponseDTOSimpleFunction<Long>());
    }

    @Override
    public Observable<Long> backupDatas(List<BackupChunkDTO> backupChunkDTOS) {
        return backupApiService
                .backupDatas(backupChunkDTOS)
                .map(new ResponseDTOSimpleFunction<Long>());
    }

    @Override
    public Observable<List<BackupChunkDTO>> getBackupDatas(long version) {
        return backupApiService.getBackupDatas(version)
                .map(new ResponseDTOSimpleFunction<List<BackupChunkDTO>>());
    }

    @Override
    public Observable<List<BackupChunkDTO>> getBackupDatas(String type, long version) {
        return backupApiService.getBackupDatas(type, version)
                .map(new ResponseDTOSimpleFunction<List<BackupChunkDTO>>());
    }
}
