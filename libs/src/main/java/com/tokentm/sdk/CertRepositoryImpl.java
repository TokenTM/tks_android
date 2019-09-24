package com.tokentm.sdk;

import com.xxf.arch.http.XXFHttp;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
class CertRepositoryImpl implements CertDataSource {
    private static volatile CertDataSource INSTANCE;

    public static CertDataSource getInstance() {
        if (INSTANCE == null) {
            synchronized (CertDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CertRepositoryImpl();
                }
            }
        }
        return INSTANCE;
    }

    CertApiService certApiService;

    private CertRepositoryImpl() {
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
