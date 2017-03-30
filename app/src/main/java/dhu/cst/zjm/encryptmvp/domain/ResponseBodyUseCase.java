package dhu.cst.zjm.encryptmvp.domain;

import dhu.cst.zjm.encryptmvp.api.repository.BaseRepository;
import dhu.cst.zjm.encryptmvp.domain.base.BaseResponseBodyUseCase;
import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by zjm on 3/29/2017.
 */

public class ResponseBodyUseCase implements BaseResponseBodyUseCase<ResponseBody> {
    private BaseRepository mRepository;
    private int downloadFileId;

    public ResponseBodyUseCase(BaseRepository repository) {
        this.mRepository = repository;
    }

    public void setDownloadFileId(int downloadFileId) {
        this.downloadFileId = downloadFileId;
    }

    @Override
    public Observable<ResponseBody> downloadFile(String downloadId) {
        return mRepository.downloadFile(downloadId);
    }
}
