package com.tokentm.sdk;

import com.tokentm.sdk.source.BackupDataSource;
import com.xxf.arch.http.XXFHttp;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
class BackupRepositoryImpl implements BackupDataSource {
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

    CertApiService certApiService;

    private BackupRepositoryImpl() {
        certApiService = XXFHttp.getApiService(CertApiService.class);
    }

    @Override
    public Observable<Long> backupData(BackupChunkDTO backupChunkDTO) {
        return certApiService
                .backupData(backupChunkDTO)
                .map(new ResponseDTOSimpleFunction<Long>());
    }

    @Override
    public Observable<Long> backupDatas(List<BackupChunkDTO> backupChunkDTOS) {
        return certApiService
                .backupDatas(backupChunkDTOS)
                .map(new ResponseDTOSimpleFunction<Long>());
    }

    @Override
    public Observable<List<BackupChunkDTO>> getBackupDatas(long version) {
        return certApiService.getBackupDatas(version)
                .map(new ResponseDTOSimpleFunction<List<BackupChunkDTO>>());
    }

    @Override
    public Observable<List<BackupChunkDTO>> getBackupDatas(String type, long version) {
        return certApiService.getBackupDatas(type, version)
                .map(new ResponseDTOSimpleFunction<List<BackupChunkDTO>>());
    }
}
